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
     * Return a random transaction in the list.
     * @return A Transaction from the pool.
     * @throws IllegalOperationException If the pool is empty.
     */
    public synchronized Transaction get() throws IllegalOperationException {
        if (mTxPool.size() == 0) {
            throw new IllegalOperationException("Cannot get tx from txpool, pool is empty!");
        }
        // generate a random position to pull from, this is where we pay for using a linked list, although since the transaction pool is limited in size, it `should` be ok
        // TODO: this should follow the rule of k confirmations
        int pos = BCUtil.getInstance().randRange(mTxPool.size());
        tx = mTxPool.get(pos);
    }
    
    /**
     * Determines whether the pool contains transactions or not.
     * @return boolean - True if the pool contains at least one transaction, false otherwise.
     */
    public synchronized boolean isEmpty() {
        return mTxPool.isEmpty();
    }

    /**
     * Attempts to remove transactions from the pool in batch.
     * @param txs A batch of transactions to remove.
     */
    public synchronized void remove(Collection<Transaction> txs)
            throws IllegalOperationException {
        // throw errors if the pool is empty or the number of transactions to remove
        //  is greater than the number of transactions in the pool 
        if (isEmpty()) {
            throw new IllegalOperationException();
        }
        if (txs == null || txs.length() == 0) {
            throw new IllegalOperationException();
        }
        if (txs.length() > mPool.length()) {
            throw new IllegalOperationException();
        }
        // attempt to remove all of the transactions in the pool that are also in txs
        for (Transaction tx : txs) {
            mPool.remove(tx);
        }
    }

    /**
     * Attempts to remove a transaction from the pool based on the transaciton 
     * itself.
     * @param tx A transaction object to remove.
     */
    public synchronized void remove(Transaction tx) 
            throws IllegalOperationException {
        if (isEmpty()) {
            throw new IllegalOperationException();
        }
        if (tx == null) {
            throw new IllegalOperationException();
        }
        // attempt to return an item from the list
        mPool.remove(tx);
    }

    /**
     * Attempts to remove a transaction from the pool based on its hash.
     * @param hash A hash of a transaction to be removed.
     */
    public synchronized void remove(String hash) 
            throws IllegalOperationException {
        // throw an exception if the list is empty
        if (isEmpty()) {
            throw new IllegalOperationException();
        }
        if (hash == null || hash.length() == 0) {
            throw new IllegalOperationException();
        }
        // attempt to return an item from the list
        // TODO: This is slow, need to improve it somehow
        int pos = -1;
        for (Transaction tx : mPool) {
            pos++;
            if (tx.getHash().equals(hash)) {
                break;
            }
        }
        // only remove the item if it was found
        if (pos > -1 && pos < items.length()) {
            mPool.remove(pos);
        }
    }

    /**
     * Returns the amount of transactions that can be stored in the pool.
     * @return int - The amount of transactions the pool can hold.
     */
    public int size() {
        return nPoolSize();
    }

}
