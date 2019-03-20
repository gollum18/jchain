package jchain.bc;

/**
 * Represents a block header in a blockchain system.
 */
public class Header {

    // constants

    public static final int VERSION_NUMBER = 1;

    // fields

    private int nVersionNumber;
    private int nTimestamp;
    private int nBits;
    private int nNonce = 0;
    private String sPrevBlockHash;

    // constructors

    /**
     * Returns an instance of a Header object that points to the indicated 
     * previous block hash, and contains the specified amount of bits.
     * @param prevBlockHash A SHA-256 double hash hexstring representing
     * the previous block hash.
     * @param bits The number of bits contained in the block.
     */
    public Header(String prevBlockHash, int bits) {
        this(prevBlockHash, bits, VERSION_NUMBER);
    }

    /**
     * Returns an instance of a Header object that points to the indicated
     * previous block hash, contains the specified amount of bits, and has 
     * the indicated version number.
     * @param prevBlockHash A SHA-256 double hash hexstring representing
     * the previous block hash.
     * @param bits The number of bits contained in the block.
     * @param version The blocks version number.
     */
    public Header(String prevBlockHash, int bits, int version) {
        // the hash must be valid
        if (prevBlockHash == null || 
                prevBlockHash.length() == 0) {
            throw new IllegalArgumentException();
        }
        // blocks cannot be empty
        if (bits <= 0) {
            throw new IllegalArgumentException();
        }
        // one is the original version number
        if (version < 1) {
            throw new IllegalArgumentException();
        }
        sPrevBlockHash = prevBlockHash;
        nBits = bits;
        nVersionNumber = version;
        nTimestamp = (int) (System.currentTimeMillis() / 1000);
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

}
