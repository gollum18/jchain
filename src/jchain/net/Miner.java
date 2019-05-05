package jchain.net;

import jchain.bc.BC;
import jchain.bc.Block;
import jchain.bc.Header;
import jchain.bc.Transaction;
import jchain.net.Subscriber;
import jchain.net.TxnMemoryPool;
import jchain.util.IllegalOperationException;

import java.util.LinkedList;

/**
 * Represents a miner in the jchain system. A Miner runs on its own thread of execution.
 * @author Christen Ford
 * @since 4/15/2019
 */
public class Miner implements Subscriber<Transaction> {
    
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
     */
    public Miner() {
        mPool = new TxnMemoryPool();
    }

    /**
     * Returns a new instance of a Miner with the specified pool size. 
     * If the pool size is invalid, then the miner will have a pool of size
     * TxnMemoryPool.MAX_POOL_SIZE / 2
     * @param poolSize A transaction pool size.
     */
    public Miner(int poolSize) {
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
                if (mThread == null || !mThread.isAlive()) {
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

        private boolean bBlockFound = false;

        // technically this method could be passed as an anonymous function above,
        //  but that tends to complicate things, and I like uncomplicated programs
        public void run() {
            // only mine if the pool is not empty
            LinkedList<Transaction> mTxList = new LinkedList<Transaction>();
            // maybe nested while loops here is a bad idea? I've heard tell of certain game engines that nest up to four or five loops deep in their main controller
            while (!mPool.isEmpty()) {
                while (!bBlockFound) {
                    mTxList.add(mPool.get());
                    // generate a header and see if it is below target

                        // if so, yay we found a valid block!!
                }
                mTxList.clear();
            }
            // let go of the reference to help GC
            mTxList = null;
        }

    }

} // END MINER CLASS
