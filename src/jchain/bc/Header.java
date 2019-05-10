package jchain.bc;

import java.nio.ByteBuffer;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Collection;

import jchain.bc.Transaction;
import jchain.util.BCUtil;
import jchain.util.Hashable;
import jchain.util.MerkleTree;

/**
 * Represents a block header in a blockchain system.
 */
public class Header implements Hashable {

    //
    // CONSTANTS
    //

    public static final int VERSION_NUMBER = 1;

    //
    // FIELDS
    //

    private int nVersionNumber;
    private int nTimestamp;
    // This guy is easier to mine with
    private static int nBits = 0x207fffff;  // RegTest difficulty
    // Than this guy 
    //private static int nBits = 0x1d00ffff; // TestNet difficulty
    private int nNonce = 0;
    private String sPrevBlockHash;
    private String sHash;
    private String sMiningHash;

    //
    // CONSTRUCTORS
    //

    /**
     * Returns an instance of a block header constructed from the 
     * previous block hash and the transactions list.
     * @param prevBlockHash The hash of the previous block in the chain.
     * @param txList A list of transactions.
     * @param nonce The nonce for the block.
     */
    public Header(String prevBlockHash, Collection<Transaction> txList, int nonce) {
        if (prevBlockHash == null || prevBlockHash.length() == 0) {
            throw new IllegalArgumentException("Error: Cannot create block header without a previous block hash!");
        }
        if (txList == null || txList.size() == 0) {
            throw new IllegalArgumentException("Error: Cannot create block header without a list of transactions!");
        }
        sPrevBlockHash = prevBlockHash;
        nVersionNumber = VERSION_NUMBER;
        nNonce = nonce;
        nTimestamp = BCUtil.now();
        // generate the mining hash
        StringBuilder sb = new StringBuilder();
        sb.append(prevBlockHash);
        // TODO: This seems wasteful to me, sounds like a refactor
        MerkleTree<Transaction> tree = new MerkleTree<>(txList);
        sb.append(tree.computeHash());
        sb.append(nNonce);
        sMiningHash = BCUtil.getInstance().doubleHash(sb.toString());
    }

    //
    // ACCESSORS/MUTATORS
    //

    /**
     * Gets the version number of this block.
     * @return The version number of this block.
     */
    public int getVersionNumber() {
        return nVersionNumber;
    }

    /**
     * Gets the timestamp when this block was created.
     * @return The timestamp when this block was created.
     */
    public int getTimestamp() {
        return nTimestamp;
    }

    /**
     * Gets the total amount of bits comprising the block.
     * @return The total bits in this block.
     */
    public int getBits() {
        return nBits;
    }

    /**
     * Gets the nonce of this header.
     * @return The nonce of this block header.
     */
    public int getNonce() {
        return nNonce;
    }

    /**
     * Gets the previous block hash pointed to by this header.
     * @return The hash of the previous block pointed to by this header.
     */
    public String getPreviousBlockHash() {
        return sPrevBlockHash;
    }

    //
    // METHODS
    //

    /**
     * Computes the block header hash.
     * @return The computed hash of this block header.
     */
    public String computeHash() {
        StringBuilder sb = new StringBuilder();
        sb.append(getVersionNumber());
        sb.append(getTimestamp());
        sb.append(getBits());
        sb.append(getNonce());
        sb.append(getPreviousBlockHash());
        return BCUtil.getInstance().doubleHash(sb.toString());
    }

    /**
     * Gets the hash of this block header.
     * @return The hash of this block header.
     */
    public String getHash() {
        if (sHash == null || sHash.length() == 0) {
            sHash = computeHash();
        }
        return sHash;
    }
    
    public String getMiningHash() {
        return sMiningHash;
    }
    
    /**
     * Returns the target value for mining.
     * @return A target value for mining.
     */
    public static BigInteger target() {
        String bitsHex = Integer.toHexString(nBits);
        int coefficient = Integer.parseInt(bitsHex.substring(2, bitsHex.length()), 16);
        int exponent = Integer.parseInt(bitsHex.substring(0, 2), 16);
        double right = Math.pow(2, 0x8*(exponent-0x3));
        BigDecimal result = new BigDecimal(String.valueOf(coefficient*right));
        return result.toBigInteger();
    }

}
