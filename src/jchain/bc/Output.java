package jchain.bc;

import jchain.util.BCUtil;
import jchain.util.Hashable;

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
     * @param value The value of the Output in the transaction.
     * @param index The index of the Output in the transaction.
     * @param script The script associated with the Output.
     */
    public Output(int value, int index, String script) {
        if (value <= 0) {
            throw new IllegalArgumentException("Error: Output value must be > 0.");
        }
        if (index < 0) {
            throw new IllegalArgumentException("Error: Output index must be >= 0.");
        }
        nValue = value;
        nIndex = index;
        sScript = script;
        sHash = computeHash();
    }

    //
    // ACCESSORS/MUTATORS
    //

    /**
     * Returns the value of the Output.
     * @return The value of the Output in the transaction.
     */
    public int getValue() {
        return nValue;
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
     * Overriden method provided by the Hashable interface.
     * Computes a SHA-256 double hash for this Output.
     * @return A computed SHA-256 double hash for this Output.
     */
    public String computeHash() {
        StringBuilder sb = new StringBuilder();
        // Include timestamp and random number since it is very possible multiple outputs could have the same value, index, and script - don't want collisions
        sb.append(BCUtil.now());
        sb.append(BCUtil.getInstance().randRange(-500000, 500000));
        sb.append(getValue());
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

}
