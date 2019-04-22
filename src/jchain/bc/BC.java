package jchain.bc;

import java.util.LinkedList;
import java.util.ListIterator;

import jchain.bc.Block;
import jchain.bc.Transaction;
import jchain.util.NoSuchBlockException;
import jchain.util.NoSuchTransactionException;

/**
 * Implements a means to create an (currently) in-memory blockchain.
 * @author Christen Ford
 */
public class BC {

    //
    // FIELDS
    //

    // Contains the blocks in the blockchain
    private LinkedList<Block> mBlocks;

    /**
     * Returns an instance of a BC object with an initial block.
     * @param initialBlock A genesis block for the blockchain.
     */
    public BC(Block initialBlock) {
        if (initialBlock == null) {
            throw new NullPointerException();
        }
        mBlocks = new LinkedList<>();
        mBlocks.add(initialBlock);
    }

    /**
     * Adds a block to the blockchain. Block integrity is enforced with 
     *  this method, in that the block to be added must have the current lead block's hash pointed to by it's block header.
     * @param block A Block object.
     *  pointed to by the block's header is not the same as the lead block's hash.
     */
    public void addBlock(Block block) {
        if (block == null) {
            throw new NullPointerException();
        }
        // make sure the hashes match up
        if (!block.getBlockHeader().getPreviousBlockHash().equals(
                mBlocks.getLast().getHash())) {
            throw new IllegalArgumentException("ERROR: Previous hash pointed to by block does not match the most recent blocks hash! Block not added!");
        }
        mBlocks.addLast(block);
    }

    /**
     * Tries to get a block via the blocks hash. Will return null
     *  if there is no block found with the given hash.
     * @param hash A block hash (as a hexstring).
     * @return A block whose hash matches the indicated hash.
     */
    public Block getBlockByHash(String hash) throws NoSuchBlockException {
        if (hash == null || hash.length() == 0) {
            throw new IllegalArgumentException();
        }
        ListIterator<Block> iterator = mBlocks.listIterator(0);
        Block block = null;
        while (iterator.hasNext()) {
            block = iterator.next();
            if (block.getHash().equals(hash)) {
                break;
            }
        }
        throw new NoSuchBlockException(hash);
    }

    /**
     * Tries to get a block from the blockchain by height.
     * @param height The height to retrieve at.
     * @return The block at the given height in the blockchain.
     */
    public Block getBlockByHeight(int height) throws NoSuchBlockException {
        // verify the height
        if (height < 0 || height >= mBlocks.size()) {
            throw new NoSuchBlockException(height);
        }
        return mBlocks.get(height);
    }

    /**
     * Gets the lead block in the chain.
     * @return The lead block in the blockchain.
     */
    public Block getLeadBlock() {
        return mBlocks.getLast();
    }

    /**
     * Gets the height (# of blocks) of the blockchain.
     * @return The number of blocks in the blockchain.
     */
    public int getHeight() {
        return mBlocks.size();
    }

    /**
     * Tries to get a transaction via its hash. Will return null if no transaction was found with the given hash.
     * @param A SHA-256 double hash.
     * @return A Transaction whose hash matches the given hash.
     */
    public Transaction getTransactionByHash(String hash) throws NoSuchTransactionException {
        if (hash == null || hash.length() == 0) {
            throw new IllegalArgumentException();
        }
        // start the iterator at the first block
        ListIterator<Block> iterator = mBlocks.listIterator(0);
        // iterate through the blocks one-by-one
        Block block = null;
        while (iterator.hasNext()) {
            // if the block has a transaction with the indicated hash, return the transaction
            block = iterator.next();
            if (block.contains(hash)) {
                return block.getTransaction(hash);
            }
        }
        // otherwise throw an exception
        throw new NoSuchTransactionException(hash);
    }

    /**
     * 
     */
    @Override
    public String toString() {
        int height = 0;
        StringBuilder sb = new StringBuilder();
        ListIterator<Block> iterator = mBlocks.listIterator();
        while (iterator.hasNext()) {
            sb.append("Block at height: ").append(height).append("\n");
            sb.append(iterator.next()).append("\n");
            height++;
        }
        return sb.toString();
    }

}
