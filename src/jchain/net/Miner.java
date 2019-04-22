package jchain.net;

import jchain.bc.Transaction;
import jchain.net.TransactionListener;
import jchain.net.TxnMemoryPool;

/**
 * Represents a miner in the jchain system.
 */
public class Miner implements TransactionListener {
    private TxnMemoryPool mPool;

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
}
