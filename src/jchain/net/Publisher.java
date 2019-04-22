package jchain.net;

import jchain.net.Subscriber;

/*
 * Defines an interface for pushing items to subscribers.
 * @author Christen Ford <c.t.ford@vikes.csuohio.edu>
 * @since 04/21/2019
 */
public interface Publisher<V> {
    
    /**
     * Pushes a transaction to all subscribers.
     * @param tx The transaction to push.
     */
    public void push(V item);
    
    /**
     * Subscribes the subscriber to receive items pushed by 
     * the publisher.
     * @param 
     */
    public void subscribe(Subscriber<V> subscriber);
    
    /**
     * Unsubscribes the subscriber from receiveing items 
     * pushed by the Publisher.
     * @param subscriber 
     */
    public void unsubscribe(Subscriber<V> subscriber);

}
