package jchain.util;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Collection;
import java.util.Iterator;

import jchain.bc.Transaction;

/**
 * Contains both static and non-static (singleton) helper methods that 
 * provide useful functionality for the blockchain.
 */
public class BCUtil {

    // constants

    private final static char[] mHexArray = "0123456789ABCDEF".toCharArray();
    private final static BCUtil mInstance = new BCUtil();

    // fields

    private MessageDigest mDigest;

    // constructors

    /**
     * Singleton constructor that returns an instance of a BCUtil object with a SHAR-256 digestor.
     * Will error out of the JVM if the SHA-256 hashing algorithm is not available on the local platform.
     * @return An instance of the BCUtil class.
     */
    private BCUtil() {
        // try to get a SHA-256 MessageDigest
        try {
            mDigest = MessageDigest.getInstance("SHA-256");
        } 
        // if it isn't supported by the runtime, error out of the system
        catch (NoSuchAlgorithmException ex) {
            System.err.println(ex.getMessage());
            ex.printStackTrace();
            System.exit(1);
        }
    }

    // methods

    /**
     * Returns a singleton instance of the BCUtil class, useful for generating hashes.
     * @return BCUtil A singleton instance of the BCUtil class.
     */
    public static BCUtil getInstance() {
        return mInstance;
    }

    /**
     * Gets the number of bits comprising all transactions in a collection.
     * Note: It is possible that some dangling bytes get cut off from 
     * this calculation, but that is fine.
     * @param txList A collection containing transactions.
     * @return The number of bits across all transactions.
     * @exception IllegalArgumentException If the txList is null or empty.
     */
    public static int bits(Collection<Transaction> txList) {
        if (txList == null || txList.size() == 0) {
            throw new IllegalArgumentException();
        }
        int bytes = 0;
        Iterator<Transaction> itr = txList.iterator();
        while (itr.hasNext()) {
            bytes += itr.next().bytes();
        }
        return Math.floorDiv(bytes, 8);
    }

    /**
     * Converts a byte array to a hexstring.
     * @param bytes The byte array to convert.
     * @return A hexstring representing bytes.
     */
    public String bytesToHex(byte[] bytes) {
        // make sure bytes is valid and contains data, throw an 
        // IllegalArgumentException if it is not
        if (bytes == null || bytes.length == 0) {
            throw new IllegalArgumentException("Error: Cannot hash bytes! Bytes must not be null and contain data!");
        }
        // convert the bytes array to hex string
        // taken from: https://stackoverflow.com/questions/9655181/how-to-convert-a-byte-array-to-a-hex-string-in-java
        char[] hexChars = new char[bytes.length * 2];
        for ( int j = 0; j < bytes.length; j++ ) {
            int v = bytes[j] & 0xFF;
            hexChars[j * 2] = mHexArray[v >>> 4];
            hexChars[j * 2 + 1] = mHexArray[v & 0x0F];
        }
        return new String(hexChars);
    }

    /**
     * Return a SHA-256 double hash of the indicated String.
     * @param value A string to double hash.
     * @return A double SHA-256 hash as a hexstring.
     */
    public String doubleHash(String value) {
        // make sure the digest is initialized, error out if its not
        if (mDigest == null) {
            System.err.println("Error: MessageDigest not initialized, cannot generate hashes!");
            System.exit(1);
        }
        // make sure value is valid and contains data
        // throw IllegalArgumentException if not
        if (value == null || value.length() == 0) {
            throw new IllegalArgumentException("Error: Cannot hash value, value is invalid!");
        }
        try {
            // update the digest
            mDigest.update(value.getBytes("UTF-8"));
        } catch (UnsupportedEncodingException ex) {
            System.err.println(ex.getMessage());
            ex.printStackTrace();
            System.exit(0);
        }
        // get the single hash, update the digest with it
        byte[] hash = mDigest.digest();
        mDigest.update(hash);
        // convert the hash to a hexstring and return it
        return bytesToHex(mDigest.digest());
    }

    /**
     * Gets the time using System.currentTimeMillis() as seconds. Not a 
     * reliable time measurement.
     */
    public static int now() {
        return (int) (System.currentTimeMillis() / 1000);
    }

}
