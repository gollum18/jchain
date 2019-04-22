package jchain.util;

/**
 * Thrown when a request is made for a Transaction via its hash and it is not found.
 * @author Christen Ford
 * @since 4/22/2019
 */
public class NoSuchTransactionException extends Exception {
    
    //
    // FIELDS
    //

    // A SHAR-256 double hash
    private String sHash;

    //
    // CONSTRUCTORS
    //

    /**
     * Returns an instance of a NoSuchTransactionException containing the indicated hash.
     * @param hash The SHA-256 double hash that triggered the exception.
     */
    public NoSuchTransactionException(String hash) {
        this(hash, "");
    }

    /**
     * Returns an instance of a NoSuchTransactionException containing the indicated hash and message.
     * @param hash The SHA-256 double hash that triggered the exception.
     * @param msg The message associated with the exception.
     */
    public NoSuchTransactionException(String hash, String msg) {
        super(msg);
        sHash = hash;
    }

    //
    // ACCESSORS/MUTATORS
    //

    /**
     * Returns the hash that triggered the NoSuchTransactionException.
     * @return A SHA-256 double hash.
     */
    public String getTransactionHash() {
        return sHash;
    }

    //
    // METHODS
    //

    /**
     * Returns a String representation of the NoSuchTransactionException.
     * @return A String representing the NoSuchTransactionException.
     */
    @Override
    public String toString() {
        return String.format("NoSuchTransactionException: Hash %s not found!\nMessage: %s\n", sHash, getMessage());
    }

}
