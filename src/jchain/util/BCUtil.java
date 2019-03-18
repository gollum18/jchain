package jchain.util;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class BCUtil {

    private final static char[] mHexArray = "0123456789ABCDEF".toCharArray();
    private final static BCUtil mInstance = new BCUtil();

    private MessageDigest mDigest;

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

    public static BCUtil getInstance() {
        return mInstance;
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

}
