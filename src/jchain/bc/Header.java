package jchain.bc;

import java.util.Collection;

import jchain.bc.Transaction;
import jchain.util.BCUtil;
import jchain.util.Hashable;

/**
 * Represents a block header in a blockchain system.
 */
public class Header implements Hashable {

    // constants

    public static final int VERSION_NUMBER = 1;

    // fields

    private int nVersionNumber;
    private int nTimestamp;
    private int nBits;
    private int nNonce = 0;
    private String sPrevBlockHash;
    private String sHash;

    // constructors

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

    // accessors/mutators

    /**
     * Gets the version number of the block.
     */
    public int getVersionNumber() {
        return nVersionNumber;
    }

    /**
     * Gets the timestamp when this block was created.
     */
    public int getTimestamp() {
        return nTimestamp;
    }

    /**
     * Gets the total amount of bits comprising the block.
     */
    public int getBits() {
        return nBits;
    }

    /**
     * Gets the nonce of this header.
     */
    public int getNonce() {
        return nNonce;
    }

    /**
     * Gets the previous block hash pointed to by this header.
     */
    public String getPreviousBlockHash() {
        return sPrevBlockHash;
    }

    /**
     * Computes the block header hash.
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
     * Gets the hash of the block header.
     */
    public String getHash() {
        if (sHash == null || sHash.length() == 0) {
            sHash = computeHash();
        }
        return sHash;
    }

}
