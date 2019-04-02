package jchain.net;

import java.util.HashMap;

import jchain.bc.Transaction;

/**
 * Implements a transaction pool in a blockchain system.
 * The transaction pool is limited in size, once reached the Node will
 * not continue to accept transactions until it clears some form its 
 * pool.
 *
 * The transaction pool tries to follow the Rule of K Confirmations in 
 * that the probability of any given transaction being included in a 
 * block is based on how many times the transaction has been confirmed 
 * by other nodes. A transaction that has a higher confirmation count 
 * has a higher chance to be included in the next block.
 *
 * Like Bitcoin, transactions that have received 6 transactions are 
 * always confirmed in the next block.
 */
public class TXPool {

    public static final int MAX_CONFIRMATIONS = 6;

    // Tracks the bucket that the transaction is in
    //  Prevents us from having to ask every confirmation map whether it contains the transaction
    private mConfirmations HashMap<String, int>;
    // Where transactions are actually stored
    private mTxPool HashMap<int, HashMap<String, Transaction>>;
    
    /**
    * Returns an instance of a TXPool object with the specified parameters
    */
    public TXPool() {
        mConfirmations = new HashMap<String, int>();
        mTxPool = new HashMap<int, HashMap<String, Transaction>>();
        // initialize pools for the transaction levels
        for (int i = 0; i <= 6; i++) {
            mTxPool.put(i, new HashMap<String, Transaction>());
        }
    }
    
    /**
    * Adds a transaction to the transaction pool at the lowest confirmation level
    */
    public boolean add(Transaction tx) {
        String txHash = tx.getHash();
        // do not allow transactions that are already in the pool
        if (containsTransaction(txHash)) {
            return false;
        }
        // add the transaction to the pool at confirmation level 0
        mConfirmations.put(txHash, 0);
        mTxPool.get(0).put(txHash, tx);
        return true;
    }
    
    /**
    * Increments the confirmation counter on a transaction.
    */
    public void confirm(String txHash) {
        // do nothing if the transaction is not in the pool
        if (!containsTransaction(txHash)) {
            return;
        }
        // get the transactions current position
        int confirmations = mConfirmations.get(txHash);
        // do nothing if the transaction is already at max confirmations
        if (confirmations == MAX_CONFIRMATIONS) {
            return;
        }
        // get the transaction
        Transaction tx = mTxPool.get(confirmations).get(txHash);
        // remove the transaction
        mTxPool.get(confirmations).remove(txHash);
        // add the transaction to the next confirmation up
        mConfirmations.put(txHash, confirmations+1);
        mTxPool.get(confirmations+1).put(txHash, tx);
    }
    
    /**
    * Determines if the transaction pool contains the specified transaction.
    */
    public boolean containsTransaction(String txHash) {
        return mConfirmations.containsKey(txHash);
    }
    
    /**
    * Gets the number of confirmations a transaction has given a transaction hash.
    */
    public int getConfirmations(String txHash) {
        if (!containsTransaction(txHash)) {
            return -1;
        }
        return mConfirmations.get(txHash);
    }
    
    /**
    * Gets a random transaction from one of the confirmation pools where higher confirmation
    *   transactions have higher priority.
    */
    public Transaction getTransaction() {
        // TODO: Implement gettig a transaction from the tx pool using the above mechanism
        return null;
    }

}
