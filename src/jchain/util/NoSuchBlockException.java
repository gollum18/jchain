package jchain.util;

/**
 * Thrown when retrievals either by height or by hash are made for blocks and the block does not exist or there was some other error retreiveing the block.
 * @author Christen Ford
 * @since 4/22/2019
 */
public class NoSuchBlockException extends Exception {

    //
    // FIELDS
    //

    // the hash of the requested block
    private String sHash = null;
    // the height of the requested block
    private int nHeight = 0;

    //
    // CONSTRUCTORS
    //
    
    /**
     * Creates an instance of a NoSuchBlockException containing the indicated block hash.
     * @param hash A SHA-256 double hash.
     */
    public NoSuchBlockException(String hash) {
        this(hash, "");
    }

    /**
     * Creates an instance of a NoSuchBlockException containing the indicated block height.
     * @param height A block height.
     */
    public NoSuchBlockException(int height) {
        this(height, "");
    }

    /**
     * Creates an instance of a NoSuchBlockException containing the indicated block hash and message.
     * @param hash A SHA-256 double hash.
     * @param msg A message associated with the exception.
     */
    public NoSuchBlockException(String hash, String msg) {
        super(msg);
        sHash = hash;
    }
    
    /**
     * Creates an instance of a NoSuchBlockException containing the indicated block height and message.
     * @param height A block height.
     * @param msg A message associated with the exception.
     */
    public NoSuchBlockException(int height, String msg) {
        super(msg);
        nHeight = height;
    }

    //
    // ACCESSORS/MUTATORS
    //

    /**
     * Returns the SHA-256 double hash that triggered the NoSuchBlockException.
     * @return A SHA-256 double hash hexstring.
     */
    public String getBlockHash() {
        return sHash;
    }

    /**
     * Returns the block height that triggered the NoSuchBlockException.
     * @return A block height.
     */
    public int getBlockHeight() {
        return nHeight;
    }

    //
    // METHODS
    //
    
    /**
     * Returns a String representation of the NoSuchBlockException.
     * @return A String representing the NoSuchBlockException.
     */
    @Override
    public String toString() {
        if (sHash == null) {
            return String.format("NoSuchBlockException: Height %d is invalid!\nMessage: %s\n", nHeight, getMessage());
        } else {
            return String.format("NoSuchBlockException: Hash %s is invalid!\nMessage: %s\n", getMessage());
        }
    }

}
