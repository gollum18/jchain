package jchain.net;

import jchain.net.Subscriber;

/*
 * Defines an interface for pushing items to subscribers.
 * @author Christen Ford
 * @since 04/21/2019
 */
public interface Publisher<V> {
    
    /**
     * Pushes an item to all subscribers.
     * @param item The item to push.
     */
    public void push(V item);
    
    /**
     * Subscribes the subscriber to receive items pushed by 
     * the publisher.
     * @param subscriber A Subscriber to subscribe.
     */
    public void subscribe(Subscriber<V> subscriber);
    
    /**
     * Unsubscribes the subscriber from receiveing items 
     * pushed by the Publisher.
     * @param subscriber A Subscriber to unsubscribe.
     */
    public void unsubscribe(Subscriber<V> subscriber);

}
