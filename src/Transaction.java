import java.util.Objects;

public class Transaction {
    
    public static final int VERSION = 1;
    
    private int nVersionNumber;
    private int nInCounter;
    private int nOutCounter;
    private String[] mInputs;
    private String[] mOutputs;
    private String sHash;

    public Transaction(String[] inputs, String[] outputs) {
        this(inputs, outputs, VERSION);
    }

    public Transaction(String[] inputs, String[] outputs, int versionNumber) {
        nVersionNumber = versionNumber;
        nInCounter = inputs.length;
        nOutCounter = outputs.length;
        mInputs = inputs;
        mOutputs = outputs;
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

    public String[] getInputs() {
        return mInputs;
    }

    public String[] getOutputs() {
        return mOutputs;
    }

    public String getHash() {
        return sHash;
    }

    public String computeHash() {
        StringBuilder sb = new StringBuilder();
        sb.append(getVersionNumber());
        sb.append(getInputCount());
        for (String input : getInputs()) {
            sb.append(input);
        }
        sb.append(getOutputCount());
        for (String output : getOutputs()) {
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
