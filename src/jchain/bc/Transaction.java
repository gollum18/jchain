package jchain.bc;

import java.util.ArrayList;
import java.util.ListIterator;
import java.util.Objects;

import jchain.util.BCUtil;
import jchain.util.Hashable;

public class Transaction implements Hashable {

    // constants

    public static final int VERSION = 1;
    
    // fields

    private int nVersionNumber;
    private int nInCounter;
    private int nOutCounter;
    private ArrayList<String> mInputs;
    private ArrayList<String> mOutputs;
    private String sHash;

    // constructurs

    /**
     * Returns an instance of a Transaction object containing the given
     * inputs and the given outputs. Each transaction must contain at least
     * one input and at least one output.
     * @param inputs A non-empty String array where each String is in the
     * format "{address}:{amount}".
     * @param outputs A non-empty String array where each String is in the 
     * format "{address}:{amount}"
     * @exception IllegalArgumentException If inputs is null or empty. If 
     * outputs is null or empty.
     */
    public Transaction(String[] inputs, String[] outputs) {
        this(inputs, outputs, VERSION);
    }

    /**
     * Returns an instance of a Transaction object containing the given 
     * inputs and outputs, with the indicated version number.
     * @param inputs A non-empty String array where each String is in the 
     * format "{address}:{amount}".
     * @param outputs A non-empty String array where each String is in the 
     * format "{address}:{amount}".
     * @param versionNumber The Transaction version number.
     */
    public Transaction(String[] inputs, String[] outputs, int versionNumber) {
        if (inputs == null || inputs.length == 0) {
            throw new IllegalArgumentException();
        }
        if (outputs == null || outputs.length == 0) {
            throw new IllegalArgumentException();
        }
        /* todo: check the format of each string in the input and 
            output arrays, throw an error if the format is wrong */
        nVersionNumber = versionNumber;
        nInCounter = inputs.length;
        nOutCounter = outputs.length;
        mInputs = new ArrayList<>(inputs.length);
        for (String input : inputs) {
            mInputs.add(input);
        }
        mOutputs = new ArrayList<>(outputs.length);
        for (String output : outputs) {
            mOutputs.add(output);
        }
        sHash = computeHash();
    }

    /**
     * Returns the version number of the Transaction.
     * @return The version number of the Transaction.
     */
    public int getVersionNumber() {
        return nVersionNumber;
    }

    /**
     * Return the amount of inputs contained in the transaction.
     * @return The number of inputs in the transaction.
     */
    public int getInputCount() {
        return nInCounter;
    }

    /**
     * Return the amount of outputs contained in the transaction.
     * @return The number of outputs in the Transaction.
     */
    public int getOutputCount() {
        return nOutCounter;
    }
    
    /**
     * Return a ListIterator<String> object over the inputs of the 
     * transaction.
     * @return A ListIterator<String> object containing all the inputs
     * in the transaction.
     */
    public ListIterator<String> getInputs() {
        return mInputs.listIterator();
    }

    /**
     * Return a ListIterator<String> object over the outputs of the
     * transaction.
     * @return A ListIterator<String> object containing all the outputs 
     * in the transaction.
     */
    public ListIterator<String> getOutputs() {
        return mOutputs.listIterator();
    }

    /**
     * Returns the SHA-256 double hash for the Transaction as a hexstring.
     * @return A SHA-256 double hash as a hexstring.
     */
    public String getHash() {
        return sHash;
    }

    /**
     * Determines the transaction size in bytes.
     * @return The number of bytes comprising all of the inputs and outputs
     * in the transaction.
     */
    public int bytes() {
        int size = 0;
        // add the size of all inputs
        for (String input : mInputs) {
            size += input.getBytes().length;
        }
        // add the size of all outputs
        for (String output : mOutputs) {
            size += output.getBytes().length;
        }
        // add in the size of the hash
        size += sHash.getBytes().length;
        return size;
    }

    /**
     * Computes a SHA-256 double hash for the transaction and returns 
     * it as a hexstring.<br>
     * A transaction hash is calculated as:<br>
     * <ol>
     * <li>The version number of the transaction.</li>
     * <li>The number of inputs in the transaction.</li>
     * <li>The inputs in the transaction.</li>
     * <li>The number of outputs in the transaction.</li>
     * <li>The outputs in the transaction.</li>
     * </ol>
     * @return a SHAR-256 double hash hexstring.
     */
    public String computeHash() {
        StringBuilder sb = new StringBuilder();
        sb.append(getVersionNumber());
        sb.append(getInputCount());
        for (String input : mInputs) {
            sb.append(input);
        }
        sb.append(getOutputCount());
        for (String output : mOutputs) {
            sb.append(output);
        }
        return BCUtil.getInstance().doubleHash(sb.toString());
    }

    /**
     * Determines if an object is equal to this transaction.
     * @return True if the object is a Transaction object and it has the 
     * same hash as this Transaction, false otherwise.
     */
    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (!(obj instanceof Transaction)) {
            return false;
        }
        // two transactions are equal iff their hash is equal
        Transaction tx = (Transaction)obj;
        return getHash().equals(tx.getHash());
    }

    /**
     * Determines the Java hashCode for the transaction.
     * @return A 'unique' hashCode representing the transaction.
     */
    @Override
    public int hashCode() {
        return Objects.hashCode(this);
    }

    /**
     * Determines if the transaction is well-formed.
     * Transactions that are not well-formed will not be broadcast to the 
     * network.
     * @return True if the transaction is well-formed, false otherwise.
     */
    public boolean isWellFormed() {
        // TODO: Determine if the transaction is well-formed

        return true;
    }

    /**
     * Prints the transaction to standard out.
     */
    public void printTransaction() {
        System.out.println(this);
    }

    /**
     * Returns a string representation of this Transaction.
     * @return A string representing this Transaction.
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        // get the string version of the hash
        sb.append("TX Hash: ").append(getHash()).append("\n");
        sb.append("Inputs:\n");
        for (String input : mInputs) {
            sb.append("   ").append(input).append("\n");
        }
        sb.append("Outputs:\n");
        for (String output : mOutputs) {
            sb.append("   ").append(output).append("\n");
        }
        return sb.toString();
    }
}
