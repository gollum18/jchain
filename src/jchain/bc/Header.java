package jchain.bc;

import java.util.Collection;

import jchain.bc.Transaction;
import jchain.util.BCUtil;
import jchain.util.Hashable;

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
    private int nBits;
    private int nNonce = 0;
    private String sPrevBlockHash;
    private String sHash;

    //
    // CONSTRUCTORS
    //

    /**
     * Returns an instance of a block header constructed from the 
     * previous block hash and the transactions list.
     * @param prevBlockHash The hash of the previous block in the chain.
     * @param txList A list of transactions.
     */
    public Header(String prevBlockHash, Collection<Transaction> txList) {
        if (prevBlockHash == null || prevBlockHash.length() == 0) {
            throw new IllegalArgumentException("Error: Cannot create block header without a previous block hash!");
        }
        if (txList == null || txList.size() == 0) {
            throw new IllegalArgumentException("Error: Cannot create block header without a list of transactions!");
        }
        sPrevBlockHash = prevBlockHash;
        nBits = BCUtil.bits(txList);
        nVersionNumber = nVersionNumber;
        nTimestamp = BCUtil.now();
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

}
