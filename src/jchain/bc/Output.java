package jchain.bc;

import jchain.util.BCUtil;
import jchain.util.Hashable;
import jchain.util.IllegalOperationException;

/**
 * Represents an output in a Transaction.
 */
public class Output implements Hashable {

    //
    // FIELDS
    //

    // The value of the Output in the Transaction
    private int nValue;
    // The index of the Output in the Transaction
    private int nIndex;
    // The script associated with the Output
    private String sScript;
    // The SHA-256 double hash of the Output
    private String sHash;

    //
    // CONSTRUCTORS
    //

    /**
     * Returns an instance of an Output with the specified value, index, and script.
     * @param minis The amount of minis constituting the Output. A mini is 1/1000th of a JCoin.
     * @param index The index of the Output in the transaction.
     * @param script The script associated with the Output.
     */
    public Output(int minis, int index, String script) {
        if (minis <= 0) {
            throw new IllegalArgumentException("Error: Amount of minis in output must be > 0.");
        }
        if (index < 0) {
            throw new IllegalArgumentException("Error: Output index must be >= 0.");
        }
        nValue = minis;
        nIndex = index;
        sScript = script;
        sHash = computeHash();
    }

    //
    // ACCESSORS/MUTATORS
    //

    /**
     * Returns the value of the Output in minis.
     * @return The value of the Output (in minis) in the transaction.
     */
    public int getValueAsMinis() {
        return nValue;
    }
    
    /**
     * Returns the value of the Output in JCoins.
     * @return The value of the Output (in JCoins) in the transaction.
     */
    public double getValueAsJCoins() {
        try {
            return BCUtil.mint(nValue);
        } catch (IllegalOperationException ex) {
            System.err.println(ex);
            return Double.NaN;
        }
    }

    /**
     * Returns the index of the Output.
     * @return The index of the Output in the transaction.
     */
    public int getIndex() {
        return nIndex;
    }

    /**
     * Returns the script associated with the Output.
     * @return A null/empty String or a script.
     */
    public String getScript() {
        return sScript;
    }

    //
    // METHODS
    //
    
    /**
     * Returns the number of bytes in the output.
     */
    public int bytes() {
        // This assumes that Java integers are 32 bytes which is, 
        //  of course, platform-dependent
        return (32 * 2) + sScript.getBytes().length;
    }

    /**
     * Overriden method provided by the Hashable interface.
     * Computes a SHA-256 double hash for this Output.
     * @return A computed SHA-256 double hash for this Output.
     */
    public String computeHash() {
        StringBuilder sb = new StringBuilder();
        // Include timestamp and random number since it is very possible multiple outputs could have the same value, index, and script - don't want collisions
        sb.append(BCUtil.now());
        sb.append(BCUtil.getInstance().randRange(0, Integer.MAX_VALUE));
        sb.append(getValueAsMinis());
        sb.append(getIndex());
        sb.append(getScript());
        return BCUtil.getInstance().doubleHash(sb.toString());
    }

    /**
     * Overriden method provided by the Hashable interface.
     * Returns the SHA-256 double hash for the Output.
     * @return A SHA-256 double hash as a hexstring.
     */
    public String getHash() {
        if (sHash == null || sHash.length() == 0) {
            sHash = computeHash();
        } return sHash;
    }
    
    /**
     * Returns a String representation of the Output.
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Value (minis): ").append(getValueAsMinis());
        sb.append("Value: (JCoins): ").append(getValueAsJCoins());
        sb.append(", Index: ").append(nIndex);
        return sb.toString();   
    }

}
