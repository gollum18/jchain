package jchain.bc;

import java.util.ArrayList;
import java.util.ListIterator;
import java.util.Objects;

import jchain.util.BCUtil;

public class Transaction {
    
    public static final int VERSION = 1;
    
    private int nVersionNumber;
    private int nInCounter;
    private int nOutCounter;
    private ArrayList<String> mInputs;
    private ArrayList<String> mOutputs;
    private String sHash;

    public Transaction(String[] inputs, String[] outputs) {
        this(inputs, outputs, VERSION);
    }

    public Transaction(String[] inputs, String[] outputs, int versionNumber) {
        if (inputs == null || inputs.length == 0) {
            throw new IllegalArgumentException();
        }
        if (outputs == null || outputs.length == 0) {
            throw new IllegalArgumentException();
        }
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

    public int getVersionNumber() {
        return nVersionNumber;
    }

    public int getInputCount() {
        return nInCounter;
    }

    public int getOutputCount() {
        return nOutCounter;
    }
    
    public ListIterator<String> getInputs() {
        return mInputs.listIterator();
    }

    public ListIterator<String> getOutputs() {
        return mOutputs.listIterator();
    }

    public String getHash() {
        return sHash;
    }

    /**
     * Determines the transaction size in bytes.
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

    @Override
    public int hashCode() {
        return Objects.hashCode(this);
    }

    public void printTransaction() {
        System.out.println(this);
    }

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
