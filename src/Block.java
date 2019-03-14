import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public Block {

    // predetermined 'secret' magic number
    private static final int nMagicNumber = 0xD8B4BEF9;
    // block size is measured in kb
    public static final int nBlockSize = 2048;

    private Header mBlockHeader;
    private int nTransactionCount = 0;
    private MerkleTree mTransactions;
    private String sHash;

    /**
     * Returns the block header.
     */
    public Header getBlockHeader() {
        return mBlockHeader;
    }

    /**
     * Returns the transaction count.
     */
    public int getTransactionCount() {
        return nTransactionCount;
    }

    /**
     * Adds a single transaction to the block.
     */
    public void addTransaction(Transaction tx) {
        
    }

    /**
     * Adds a batch of transactions to the block.
     * @param coll A collection of transactions to add to 
     *  the block.
     */
    public void addTransaction(Collection<Transaction> coll) {
        
    }

    /**
     * Computes a SHA-256 double hash for the block and returns
     *  it.
     */
    public String computeHash() {

    }

    /**
     * Trys to get a transaction from the block with the given 
     *  transaction hash.
     * @param txHash A SHA-256 double hash hexstring.
     */
    public Transaction getTransaction(String txHash) {
        
    }

}
