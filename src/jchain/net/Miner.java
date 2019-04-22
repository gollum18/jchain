package jchain.net;

import jchain.bc.Transaction;
import jchain.net.Subscriber;
import jchain.net.TxnMemoryPool;

/**
 * Represents a miner in the jchain system. A Miner runs on its own thread of execution.
 * @author Christen Ford
 * @since 4/15/2019
 */
public class Miner implements Subscriber<Transaction> {
    
    //
    // FIELDS
    //
    
    // The miners transaction pool, once a transaction is retrieved from here, it is gone unless the Miner returns it
    private TxnMemoryPool mPool;

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
                mPool.add(tx);
            } catch (Exception ex) {
                System.out.println("Transaction not added to pool:");
                System.out.println(ex.getMessage());
            }
        }
    }

    //
    // INNER CLASSES
    //

    /**
     * This class represents a Thread of execution where the Miner is actively mining coins.
     */
    private class MiningThread extends Thread {
        
        //
        // FIELDS
        //

        private boolean bMining;

        //
        // CONSTRUCTORS
        //
        
        // This class has no constructor for now

        //
        // ACCESSORS/MUTATORS
        //

        /**
         *
         */
        public boolean isMining() {
            return bMining;
        }

        /**
         *
         */
        public void setMining(boolean mining) {
            bMining = mining;
        }

        //
        // METHODS
        //

        /**
         *
         */
        public void run() {
            // TODO: Implement mining within this thread
        }

    }

}
