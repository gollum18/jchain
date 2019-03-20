package jchain.bc;

import java.util.Collection;
import java.util.Iterator;
import java.util.Objects;

import jchain.bc.Header;
import jchain.bc.Transaction;
import jchain.util.BCUtil;
import jchain.util.MerkleTree;

/**
 * Implements a Block in a blockchain system.
 */
public class Block {

    // constants

    // predetermined 'secret' magic number
    private static final int MAGIC_NUMBER = 0xD8B4BEF9;
    // block size is measured in bytes (2 million bytes)
    public static final int BLOCK_SIZE = 2000000;

    // fields

    private Header mBlockHeader;
    private int nTransactionCount = 0;
    private MerkleTree mTransactions;
    private String sHash;

    // constructors

    /**
     * Returns an instance of a Block object containing the indicated 
     * transactions and linked to a previous block using the given 
     * previous block hash.<br>
     * Null transactions in the collection are <u><b>NOT</b></u> added to 
     * the block, they are instead skipped over and a warning is printed to 
     * standard out.
     * @param txList A collection of transactions.
     * @param prevBlockHash The lead blocks hash.
     * @exception IllegalArgumentException If the txList is null or empty.
     * If the prevBlockHash is null or empty.
     */
    public Block(Collection<Transaction> txList, 
            String prevBlockHash) {
        if (txList == null || txList.size() == 0) {
            throw new IllegalArgumentException();
        }
        if (prevBlockHash == null || 
                prevBlockHash.length() == 0) {
            throw new IllegalArgumentException();
        }
        // create the merkletree
        mTransactions = new MerkleTree();
        Iterator<Transaction> iterator = txList.iterator();
        int size = 0; // measured in bytes, only measure tx sizes
        // dont care about the header info, I'm not that precise
        while (iterator.hasNext() && 
                size < BLOCK_SIZE) {
            Transaction tx = iterator.next();
            if (tx == null) {
                System.err.println("WARNING: Transaction not added, transaction was null!");
                continue;
            }
            size += tx.bytes();
            if (size < BLOCK_SIZE) {
                mTransactions.add(tx);
                nTransactionCount++;
            }
        }
        // create the header
        mBlockHeader = new Header(prevBlockHash, Math.floorDiv(size, 8));
        // determine the block hash
        sHash = computeHash();
    }

    // accessors/mutators

    /**
     * Returns the block header.
     * @return The blocks header.
     */
    public Header getBlockHeader() {
        return mBlockHeader;
    }

    /**
     * Returns the block hash.
     * @return The blocks hash.
     */
    public String getHash() {
        return sHash;
    }

    /**
     * Returns the Transaction count.
     * @return The number of Transactions contained in the block.
     */
    public int getTransactionCount() {
        return nTransactionCount;
    }

    // methods

    /**
     * Computes a SHA-256 double hash for the block and returns
     * it.<br>
     * The hash consists of the following components:
     * <ol>
     * <li>The magic number constant.</li>
     * <li>The transaction count.</li>
     * <li>The MerkleTree root hash.</li>
     * <li>The block version number.</li>
     * <li>The timestamp when the block was created.</li>
     * <li>The number of bits comprising all of the transactions.</li>
     * <li>The nonce.</li>
     * <li>The previous block hash.</li>
     * </ol>
     * @return A SHA-256 double hash as a hexstring.
     */
    public String computeHash() {
        StringBuilder sb = new StringBuilder();
        sb.append(MAGIC_NUMBER);
        sb.append(nTransactionCount);
        sb.append(mTransactions.computeHash());
        sb.append(mBlockHeader.getVersionNumber());
        sb.append(mBlockHeader.getTimestamp());
        sb.append(mBlockHeader.getBits());
        sb.append(mBlockHeader.getNonce());
        sb.append(mBlockHeader.getPreviousBlockHash());
        return BCUtil.getInstance().doubleHash(sb.toString());
    }

    /**
     * Determines if the block contains the indicated transaction.
     * @param txHash The transaction hash.
     * @return True if the block contains a Transaction with the indicated
     * hash, false otherwise.
     */
    public boolean contains(String txHash) {
        return mTransactions.contains(txHash);
    }

    /**
     * Determines if an object is equal to this Block or not.
     * @param obj An object to determine equality against.
     * @return True if the Object is a block and has the same hash as this 
     * Block, false otherwise.
     */
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (!(obj instanceof Block)) {
            return false;
        }
        Block block = (Block)obj;
        return sHash.equals(block.getHash());
    }

    /**
     * Determines a Java hashCode for this Block.
     * @return A 'unique' hashCode for this Block.
     */
    @Override
    public int hashCode() {
        return Objects.hashCode(this);
    }

    /**
     * Trys to get a transaction from the block with the given 
     *  transaction hash.
     * @param txHash A SHA-256 double hash hexstring.
     * @return A Transaction object if it exists, null otherwise.
     */
    public Transaction getTransaction(String txHash) {
        return mTransactions.get(txHash);
    }

    /**
     * Returns a String representation of this Block.
     * @return A String object representing this Block.
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Block Hash: ").append(sHash).append("\n");
        sb.append("Transactions: ").append("\n");
        sb.append(mTransactions.toString()).append("\n");
        return sb.toString();
    }

}
