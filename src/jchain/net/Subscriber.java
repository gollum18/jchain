package jchain.net;

/**
 * Defines an interface for objects to receive items.
 * @author Christen Ford
 * @since 4/21/2019
 */
public interface Subscriber<V> {

    /**
     * Implemented by implementors to receive items 
     * pushed to the Subscriber via a Publisher.
     * @param item An item to receive.
     */
    public void receive(V item);

}
