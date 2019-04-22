package jchain.net;

import java.util.LinkedList;

import jchain.bc.Transaction;
import jchain.util.BCUtil;
import jchain.util.IllegalOperationException;

/**
 * Used by miners to simulate an in-memory transaction pool.
 */
public class TxnMemoryPool {
    
    // declare a min pool size for efficiencies sake
    public static final int MIN_POOL_SIZE = 128;
    // only allow storing up to 2048 transactions in memory at a time
    public static final int MAX_POOL_SIZE = 2048;
    
    private LinkedList<Transaction> mTxPool;
    private int nPoolSize;
    
    /**
     * Returns a new instance of a TxnMemoryPool with a pool size equal to
     * MAX_POOL_SIZE / 2.
     */
    public TxnMemoryPool() {
        this(MAX_POOL_SIZE/2);
    }

    /**
     * Returns a new instance of a TxnMemoryPool with a specified pool size. 
     * If the pool size is less than MIN_POOL_SIZE, then the pool is initialized
     * to size MIN_POOL_SIZE. If the pool size is greater than MAX_POOL_SIZE, 
     * then the pool is initialized to size MAX_POOL_SIZE.
     * @param poolSize The size of the transaction pool.
     */
    public TxnMemoryPool(int poolSize) {
        if (poolSize < MIN_POOL_SIZE) {
            poolSize = MIN_POOL_SIZE;
        } else if (poolSize > MAX_POOL_SIZE) {
            poolSize = MAX_POOL_SIZE;
        }
        // initialize the pool size
        nPoolSize = poolSize;
        // initialize the synchronized transaction pool to capacity
        // cant win here really, either use a linkedlist and lose fast random access, or use an arraylist and have holes in the list
        // ... maybe I can use a better data structure here that allows fast random access and maintains its structure
        mTxPool = new LinkedList<Transaction>();
    }

    /**
     * Adds the transaction tx to the transaction pool onliy if the tx is valid, and the pool is not full.
     */
    public void add(Transaction tx) throws IllegalOperationException {
        if (tx == null) {
            throw new IllegalOperationException("Add TX to Pool", "Unable to add tx to pool, tx is not valid!");
        }
        if (mTxPool.size() == nPoolSize) {
            throw new IllegalOperationException("Add TX to Pool", "Transaction tx was ignored, txpool is full!");
        }
        mTxPool.add(tx);
    }

    /**
     * Determines if the transaction pool contains a specified transaction or not.
     */
    public boolean contains(Transaction tx) {
        return mTxPool.contains(tx);
    }

    /**
     * Return a random transaction in the list.
     */
    public Transaction get() throws IllegalOperationException {
        if (mTxPool.size() == 0) {
            throw new IllegalOperationException("Cannot get tx from txpool, pool is empty!");
        }
        // generate a random position to pull from, this is where we pay for using a linked list, although since the transaction pool is limited in size, it `should` be ok
        // TODO: this should follow the rule of k confirmations
        int pos = BCUtil.getInstance().randRange(mTxPool.size());
        Transaction tx = mTxPool.get(pos);
        mTxPool.remove(pos);
        return tx;
    }

}