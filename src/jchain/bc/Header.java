package jchain.bc;

public class Header {

    public static final int VERSION_NUMBER = 1;

    private int nVersionNumber;
    private int nTimestamp;
    private int nBits;
    private int nNonce = 0;
    private String sPrevBlockHash;

    public Header(String prevBlockHash, int bits) {
        this(prevBlockHash, bits, VERSION_NUMBER);
    }

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

    public int getVersionNumber() {
        return nVersionNumber;
    }

    public int getTimestamp() {
        return nTimestamp;
    }

    public int getBits() {
        return nBits;
    }

    public int getNonce() {
        return nNonce;
    }

    public String getPreviousBlockHash() {
        return sPrevBlockHash;
    }

}
