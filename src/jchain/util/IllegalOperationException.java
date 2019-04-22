package jchain.util;

/**
 * Defines a custom exception that tracks illegal operations
 * performed within jchain.
 * @author Christen Ford
 * @since 4/21/2019
 */
public class IllegalOperationException extends Exception {

    //
    // FIELDS
    //

    private String sOperation;

    //
    // CONSTRUCTORS
    //

    /**
     * Returns an instance of an IllegalOperationException 
     * containing the indicated operation.
     * @param op The operation that triggered the exception.
     */
    public IllegalOperationException(String op) {
        this(op, "");
    }

    /**
     * Returns an instance of an IllegalOperationException 
     * containing the indicated operation and message.
     * @param op The operation that triggered the exception.
     * @param msg The message attached to the exception.
     */
    public IllegalOperationException(String op, String msg) {
        super(msg);
        sOperation = op;
    }
    
    //
    // ACCESSORS/MUTATORS
    //
    
    /**
     * Returns the operation that triggered the exception.
     * @return The operation that triggered the exception.
     */
    public String getOperation() {
        return sOperation;
    }
    
    //
    // METHODS
    //

    /**
     * Returns a string representation of the 
     * IllegalOperationException.
     */
    @Override
    public String toString() {
        return String.format("Triggered by Operation: <%s>\nMessage: <%s>", sOperation, getMessage());
    }

}
