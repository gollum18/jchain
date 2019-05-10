package jchain.net;

import jchain.bc.BC;
import jchain.bc.Block;
import jchain.bc.Header;
import jchain.bc.Output;
import jchain.bc.Transaction;
import jchain.net.Subscriber;
import jchain.net.TxnMemoryPool;
import jchain.util.BCUtil;
import jchain.util.IllegalOperationException;

import java.math.BigInteger;
import java.util.LinkedList;

/**
 * Represents a miner in the jchain system. A Miner runs on its own thread of execution.
 * @author Christen Ford
 * @since 4/15/2019
 */
public class Miner implements Subscriber<Transaction> {
    
    //
    // CONSTANTS
    //
    
    public static final int MIN_TXNS = 1;
    
    //
    // FIELDS
    //
    
    // The miners transaction pool, once a transaction is retrieved from here, it is gone unless the Miner returns it, note that this object is fully synchronized
    private TxnMemoryPool mPool;

    // The miners current blockchain
    //  Note that in practice, this would be overwritten if the miner sees 
    //  a chain that is longer than its own
    private BC mBC;

    // The miners mining thread
    private Thread mThread = null;
    
    //
    // CONSTRUCTORS
    //

    /**
     * Returns a new instance of a Miner with the default pool size.
     * @param chain The miners initial blockchain.
     */
    public Miner(BC chain) {
        this(chain, TxnMemoryPool.MAX_POOL_SIZE/2);
    }

    /**
     * Returns a new instance of a Miner with the specified pool size. 
     * If the pool size is invalid, then the miner will have a pool of size
     * TxnMemoryPool.MAX_POOL_SIZE / 2
     * @param chain The Miners initial blockchain.
     * @param poolSize A transaction pool size.
     */
    public Miner(BC chain, int poolSize) {
        if (chain == null || chain.getHeight() == 0) {
            throw new IllegalArgumentException();
        }
        mBC = chain;
        try {
            mPool = new TxnMemoryPool(poolSize);
        } catch (Exception ex) {
            System.out.println("Unable to intialize tx pool with size: " + poolSize + ", invalid pool size.");
            mPool = new TxnMemoryPool();
        }
    }

    //
    // METHODS
    //

    /**
     * Receives broadcasted transactions and adds them to the pool.
     * @param tx A transaction to add to the pool.
     */
    public void receive(Transaction tx) {
        if (!mPool.contains(tx)) {
            try {
                System.out.println(String.format("Miner %s: Adding transaction with hash %s to the mining pool.", this, tx.getHash()));
                mPool.add(tx);
                // check if the mining thread needs (re)started
                // Periodically wake up the miner
                if ((mThread == null || !mThread.isAlive()) && 
                        //mPool.size() >= MIN_TXNS
                        Math.random() > .625) {
                    mThread = new Thread(new MiningThread());
                    mThread.start();
                }
            } catch (Exception ex) {
                System.out.println("Transaction not added to pool:");
                System.out.println(ex.getMessage());
            }
        }
    }

    /**
     * Defines a runnable object that performs mining as long as the 
     * mining pool is not empty. Once empty the thread will terminate.
     *
     * Users of this runnable object should verify that the thread is dead using 
     * instance.isAlive() to create a new runnable if necessary.
     */
    private class MiningThread implements Runnable {

        /**
         * Generates a coinbase transaction that is to be included in each block.
         * @return Transaction A coinbase transaction.
         */
        public Transaction genCoinbaseTx() {
            String[] inputs = new String[] {
                "A:50000"
            };
            Output[] outputs = new Output[] {
                new Output(50000, 0, "coinbase tx")
            };
            return new Transaction(inputs, outputs);
        }

        // implements Proof-of-Work
        public void run() {
            // only mine if the pool is not empty
            LinkedList<Transaction> mTxList = new LinkedList<Transaction>();
            // nonce runs from min 0 to max int value
            int nonce = 0;
            // used for controlling the mining loop
            boolean blockFound = false;
            // loop until the number of blocks in the pool is less 
            //  than the limit - 1 (because of coinbase)
            while (mPool.count() >= MIN_TXNS) {
                // reset the header information
                mTxList.clear();
                nonce = 0;
                // reset control information
                blockFound = false;
                // generate the coinbase transaction
                mTxList.add(genCoinbaseTx());
                int txns = 0;
                // Not entirely sure why +2 works here but it does, probably has to do with the coinbase transaction
                if (mPool.count() < Block.MAX_TXNS-1) {
                    txns = (int)(mPool.count() * Math.random()) + 2;
                } else {
                    txns = (int)((Block.MAX_TXNS-1) * Math.random()) + 2;
                }
                // pull transactions till we reach the tx limit
                while (mTxList.size() < txns) {
                    try {
                        mTxList.add(mPool.get());
                    } catch (IllegalOperationException e) {
                        System.err.println(e);
                        e.printStackTrace();
                        // dump all of the txs back into the pool
                        for (Transaction tx : mTxList) {
                            try {
                                mPool.add(tx);
                            } catch (IllegalOperationException f) {
                                System.err.println(f);
                            }
                        }
                        return;
                    }
                }
                // step through the nonce values 
                while (!blockFound && nonce < Integer.MAX_VALUE) {
                    // generate a header
                    Header header = new Header(
                        mBC.getLeadBlock().getHash(),
                        mTxList,
                        // notice that the nonce is the only 
                        //  value that changes here
                        nonce
                    );
                    // check if header hash is less than target
                    BigInteger hashVal = new BigInteger(header.getMiningHash(), 16);
                    BigInteger target = Header.target();
                    // NOTE: This is a debug statement, pull it out
                    //  when done
                    //System.out.println(String.format("Miner: POW %d < %d", hashVal, target));
                    // Perform POW check
                    if (hashVal.compareTo(target) < 0) {
                        // set blockFound
                        blockFound = true;
                        // create a new block, add it to bc
                        mBC.addBlock(new Block(mTxList, header));
                        // notify that a new block was found
                        //  in reality this would be a broadcast
                        System.out.println(String.format("Miner: Found new block with hash: %s, TX Count: %d", mBC.getLeadBlock().getHash(), mBC.getLeadBlock().getTransactionCount()));
                    }
                    // increment the nonce
                    nonce++;
                }
                // if blockfound is false, then we ran out of nonces
                //  add the transactions back to the pool since 
                //  TxnMemoryPool.get() removes them
                if (!blockFound) {
                    for (Transaction tx : mTxList) {
                        try {
                            mPool.add(tx);
                        } catch (IllegalOperationException ex) {
                            System.err.println("Miner.MiningThread: An exception occurred when adding transactions back to the pool!");
                            continue;
                        }
                    }
                }
            }
            // let go of the reference to help GC
            mTxList = null;
        }

    }

} // END MINER CLASS
