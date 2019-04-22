package jchain.tests;

import java.util.LinkedList;

import jchain.bc.*;
import jchain.net.*;
import jchain.util.BCUtil;

/**
 * Test harness for mining. Implements the Publisher interface for pushing transactions to subscribed miners.
 * @author Christen Ford
 * @since 4/21/2019
 */
public class MiningHarnessTest extends Thread 
    implements Publisher<Transaction> {

    //
    // FIELDS
    //

    // Stores subscribed miners.
    private LinkedList<Subscriber<Transaction>> mSubscribers 
        = new LinkedList<Subscriber<Transaction>>();

    // The number of transactions to generate in this test harness
    private int nTxAmt;

    // Whether the test harness is generating transactions or not.
    private boolean bRunning = true;

    //
    // CONSTRUCTORS
    //

    public MiningHarnessTest(int txAmt) {
        if (txAmt < 1) {
            throw new IllegalArgumentException("Error: Mining test harness must generate at least one transaction!");
        }
        nTxAmt = txAmt;
    }

    //
    // ACCESSORS / MUTATORS
    //

    /**
     * Gets the running status of this MiningHarnessTest.
     */
    public boolean isRunning() {
        return bRunning;
    }

    /**
     * Updates the running status of this MiningHarnessTest. 
     * NOTE: Once stopped, you must create a new MiningHarnessTest
     * to be able to generate more transactions.
     * @param running Whether the MiningHarnessTest is generating
     * transactions or not.
     */
    public void setRunning(boolean running) {
        bRunning = running;
    }

    //
    // METHODS
    //
    
    /**
     * Generates a Transaction and returns it to the caller.
     */
    private Transaction generateTx() {
        // get a random amount of inputs
        int nInputs = BCUtil.getInstance().randRange(1, 10);

        // generate some inputs
        String[] inputs = new String[nInputs];

        // tracks the amount of coins in the input
        int inputAmt = 0;

        // generate the input content
        for (int i = 0; i < inputs.length; i++) {
            int input = BCUtil.getInstance().randRange(1, 2000);
            inputs[i] = String.valueOf(input);
            inputAmt += input;
        }

        // get a random amount of outputs
        int nOutputs = BCUtil.getInstance().randRange(1, nInputs);

        // generate some outputs
        String[] outputs = new String[nOutputs];
        
        // generate the output content
        for (int i = 0; i < outputs.length; i++) {
            int output = BCUtil.getInstance().randRange(1, inputAmt - (outputs.length - i));
            outputs[i] = String.valueOf(output);
        }

        // generate the transaction
        return new Transaction(inputs, outputs);
    }

    /**
     * Pushes the transaction to all subscribed listeners.
     * @param tx A transaction to push.
     */
    public void push(Transaction tx) {
        if (tx == null) {
            throw new IllegalArgumentException("Error: Cannot push transaction, transaction is null!");
        }
        // Broadcasts the received transaction to each subscribed miner
        for (Subscriber<Transaction> subscriber : 
            mSubscribers) {
            subscriber.receive(tx);
        }
    }

    /**
     * Overrides run defined within the Thread class. 
     * Sets up a miner, subscribes it, generates transactions, 
     * transactions and broadcasts them to the subscribed listener.
     */
    @Override
    public void run() {
        int txGenerated = 0;
        
        // Once bRunning is stopped, it can never be enable again from within the context of this Thread, thats just how threads work
        while (bRunning && txGenerated < nTxAmt) {
            // Generate a transaction and send it to the subbed miners
            push(generateTx());

            // Increment the number of generated transactions
            txGenerated++;

            // sleep for 5 seconds
            try {
                sleep(5000);
            } catch (InterruptedException ex) {
                System.out.println(ex.getMessage());
            }
        }
    }

    /**
     * Subscribes the indicated subscriber to receive transactions.
     * @param listener A subscriber to subscribe.
     */
    public void subscribe(
        Subscriber<Transaction> subscriber) {
        if (subscriber != null && !
            mSubscribers.contains(subscriber)) {
            mSubscribers.add(subscriber);
        }
    }

    /**
     * Unsubscribes the indicated subscriber from receiving transactions.
     * @param subscriber A subscriber to unsubscribe.
     */
    public void unsubscribe(
        Subscriber<Transaction> subscriber) {
        if (subscriber != null && 
            mSubscribers.contains(subscriber)) {
            mSubscribers.remove(subscriber);
        }
    }

} // END MiningTestHarness
