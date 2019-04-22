package jchain.net;

import jchain.bc.Transaction;

public interface TransactionListener {
    public void receive(Transaction tx);
}
