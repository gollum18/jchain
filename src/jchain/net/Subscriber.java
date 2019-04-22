package jchain.net;

/**
 * Defines an interface for objects to receive items.
 */
public interface Subscriber<V> {

    /**
     * Implemented by implementors to receive items 
     * pushed to the Subscriber via a Publisher.
     */
    public void receive(V item);

}
