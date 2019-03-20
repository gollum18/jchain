package jchain.bc;

import java.util.LinkedList;
import java.util.ListIterator;

import jchain.bc.Block;
import jchain.bc.Transaction;

/**
 * Implements a means to create an (currently) in-memory blockchain.
 */
public class BC {
    
    // todo: change to a better data structure, an in-memory 
    //  blockchain is very inefficient space wise, and this 
    //  will not offer the best random access to the chain
    //  tldr: NOT SCALABLE with a p2p network
    private LinkedList<Block> mBlocks;

    /**
     * Returns an instance of a BC object with an initial block.
     * @param initialBlock A genesis block for the blockchain.
     * @exception NullPointerException If the initialBlock is null.
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
     *  this method, in that the block to be added must have the current 
     *  lead block's hash pointed to by it's block header.
     * @param block A Block object.
     * @exception NullPointerException If the block is null.
     * @exception IllegalArgumentException If the previous block hash 
     *  pointed to by the block's header is not the same as the lead 
     *  block's hash.
     */
    public void addBlock(Block block) {
        if (block == null) {
            throw new NullPointerException();
        }
        // make sure the hashes match up
        if (!block.getBlockHeader().getPreviousBlockHash().equals(
                mBlocks.getLast().getHash())) {
            throw new IllegalArgumentException("ERROR: Block previous hash does not match the most recent blocks hash! Block not added!");
        }
        mBlocks.addLast(block);
    }

    /**
     * Tries to get a block via the blocks hash. Will return null
     *  if there is no block found with the given hash.
     * @param hash A block hash (as a hexstring).
     * @exception IllegalArgumentException If the hash is null or empty.
     */
    public Block getBlockByHash(String hash) {
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
        return null;
    }

    /**
     * Tries to get a block from the blockchain by height.
     * @param height The height to retrieve at.
     * @exception IndexOutOfBoundsException If the height is less than zero
     *  or greater than or equal to the height of the chain.
     */
    public Block getBlockByHeight(int height) {
        // verify the height
        if (height < 0 || height >= mBlocks.size()) {
            throw new IndexOutOfBoundsException();
        }
        return mBlocks.get(height);
    }

    /**
     * Gets the lead block in the chain.
     */
    public Block getLeadBlock() {
        return mBlocks.getLast();
    }

    /**
     * Gets the height (# of blocks) of the block chain.
     */
    public int getHeight() {
        return mBlocks.size();
    }

    /**
     * Tries to get a transaction via its hash. Will return null
     *  if no transaction was found with the given hash.
     * @exception IllegalArgumentException If the hash is null or empty.
     */
    public Transaction getTransactionByHash(String hash) {
        if (hash == null || hash.length() == 0) {
            throw new IllegalArgumentException();
        }
        ListIterator<Block> iterator = mBlocks.listIterator(0);
        Block block = null;
        while (iterator.hasNext()) {
            block = iterator.next();
            if (block.contains(hash)) {
                return block.getTransaction(hash);
            }
        }
        return null;
    }

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
