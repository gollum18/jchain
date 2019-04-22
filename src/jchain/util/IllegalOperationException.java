package jchain.util;

public class IllegalOperationException extends Exception {

    private String sOperation;

    public IllegalOperationException(String op) {
        this(op, "");
    }

    public IllegalOperationException(String op, String msg) {
        super(msg);
        sOperation = op;
    }

    @Override
    public String toString() {
        return String.format("Triggered by Operation: <%s>\nMessage: <%s>", sOperation, getMessage());
    }

}
