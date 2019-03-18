package jchain.bc;

import java.util.LinkedList;
import java.util.ListIterator;

import jchain.bc.Block;
import jchain.bc.Transaction;

public class BC {
    
    // todo: change to a better data structure, an in-memory 
    //  blockchain is very inefficient space wise, and this 
    //  will not offer the best random access to the chain
    private LinkedList<Block> mBlocks;

    public BC(Block initialBlock) {
        mBlocks = new LinkedList<>();
        mBlocks.add(initialBlock);
    }

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

    public Block getBlockByHeight(int height) {
        return mBlocks.get(height);
    }

    public Block getLeadBlock() {
        return mBlocks.getLast();
    }

    /**
     * Tries to get a transaction via its hash. Will return null
     *  if no transaction was found with the given hash.
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
