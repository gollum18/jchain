package jchain.net;

import java.util.Collection;
import java.util.LinkedList;

import jchain.bc.Transaction;
import jchain.util.BCUtil;
import jchain.util.IllegalOperationException;

/**
 * Synchronized transaction pool used by miners to track seen transactions.
 * @author Christen Ford
 */
public class TxnMemoryPool {
    
    // declare a min pool size for efficiencies sake
    public static final int MIN_POOL_SIZE = 128;
    // only allow storing up to 2048 transactions in memory at a time
    public static final int MAX_POOL_SIZE = 2048;
    
    // stores the transactions for the transaction pool object
    private volatile LinkedList<Transaction> mTxPool;
    // the current size of the pool
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
     * @param tx A transaction to add to the pool.
     * @throws IllegalOperationException If the indicated transaction is null or the pool is full.
     */
    public synchronized void add(Transaction tx) throws IllegalOperationException {
        if (tx == null) {
            throw new IllegalOperationException("Add TX to Pool", "Unable to add tx to pool, tx is not valid!");
        }
        // block until unlocked
        if (mTxPool.size() == nPoolSize) {
            throw new IllegalOperationException("Add TX to Pool", "Transaction tx was ignored, txpool is full!");
        }
        mTxPool.add(tx);
    }

    /**
     * Determines if the transaction pool contains a specified transaction or not.
     * @param tx A transaction to check for.
     * @return True if the transaction is in the pool, false otherwise.
     */
    public synchronized boolean contains(Transaction tx) {
        return mTxPool.contains(tx);
    }
    
    /**
     * Return the number of transactions in the pool.
     */
    public synchronized int count() {
        return mTxPool.size();
    }

    /**
     * Return a random transaction in the list.
     * @return A Transaction from the pool.
     * @throws IllegalOperationException If the pool is empty.
     */
    public synchronized Transaction get() throws IllegalOperationException {
        if (mTxPool.size() == 0) {
            throw new IllegalOperationException("Cannot get tx from txpool, pool is empty!");
        }
        // generate a random position to pull from, this is where we pay for using a linked list, although since the transaction pool is limited in size, it `should` be ok
        // TODO: this should follow the rule of k confirmations, but it is okay for now since we are working at getting mining working on a single node
        int pos = BCUtil.getInstance().randRange(mTxPool.size());
        Transaction tx = mTxPool.get(pos);
        mTxPool.remove(tx);
        return tx;
    }
    
    /**
     * Determines whether the pool contains transactions or not.
     * @return boolean - True if the pool contains at least one transaction, false otherwise.
     */
    public synchronized boolean isEmpty() {
        return mTxPool.isEmpty();
    }

    /**
     * Returns the amount of transactions that can be stored in the pool.
     * @return int - The amount of transactions the pool can hold.
     */
    public int size() {
        return nPoolSize;
    }

}
