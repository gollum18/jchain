package jchain.bc;

import java.util.Collection;
import java.util.Iterator;
import java.util.Objects;

import jchain.bc.Header;
import jchain.bc.Transaction;
import jchain.util.BCUtil;
import jchain.util.MerkleTree;

public class Block {

    // predetermined 'secret' magic number
    private static final int MAGIC_NUMBER = 0xD8B4BEF9;
    // block size is measured in bytes (2 million bytes)
    public static final int BLOCK_SIZE = 2000000;

    private Header mBlockHeader;
    private int nTransactionCount = 0;
    private MerkleTree mTransactions;
    private String sHash;

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

    /**
     * Returns the block header.
     */
    public Header getBlockHeader() {
        return mBlockHeader;
    }

    /**
     * Returns the block hash.
     */
    public String getHash() {
        return sHash;
    }

    /**
     * Returns the transaction count.
     */
    public int getTransactionCount() {
        return nTransactionCount;
    }

    /**
     * Computes a SHA-256 double hash for the block and returns
     *  it.
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
     */
    public boolean contains(String txHash) {
        return mTransactions.contains(txHash);
    }

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

    @Override
    public int hashCode() {
        return Objects.hashCode(this);
    }

    /**
     * Trys to get a transaction from the block with the given 
     *  transaction hash.
     * @param txHash A SHA-256 double hash hexstring.
     */
    public Transaction getTransaction(String txHash) {
        return mTransactions.get(txHash);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Block Hash: ").append(sHash).append("\n");
        sb.append("Transactions: ").append("\n");
        sb.append(mTransactions.toString()).append("\n");
        return sb.toString();
    }

}
