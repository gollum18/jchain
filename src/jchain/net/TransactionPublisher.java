package jchain.net;

import jchain.bc.Transaction;
import jchain.net.TransactionListener;

public interface TransactionPublisher {
    public void push(Transaction tx);
    public void subscribe(TransactionListener listener);
    public void unsubscribe(TransactionListener listener);
}
