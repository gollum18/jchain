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
public class MiningHarnessTest implements Publisher<Transaction> {

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

    /**
     * Creates an instance of a MiningHarnessTest that generates the indicated transactions.
     * @param txAmt The amount of transactions to generate in the test harness.
     */
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
     * @return True if the harness is generating transactions, false otherwise.
     */
    public boolean isRunning() {
        return bRunning;
    }

    /**
     * Updates the running status of this MiningHarnessTest.
     * @param running Whether the MiningHarnessTest is generating transactions or not.
     */
    public void setRunning(boolean running) {
        bRunning = running;
    }

    //
    // METHODS
    //
    
    /**
     * Generates a random output amount for a given inputAmt and a 
     * number of outputs.
     */
    private int getOutputAmt(int inputAmt, int outputs) throws Exception {
        // check the inputs
        if (inputAmt < 0) {
            throw new Exception("Error: Input amount cannot be less than 0!");
        } else if (inputAmt == 0) {
            return 0;
        }
        // check the outputs
        if (outputs <= 0) {
            throw new Exception("Error: There must be at least one output!");
        } else if (outputs == 1) {
            // consume the entire input if there is only one output
            return inputAmt;
        } else {
            int upper = inputAmt/outputs;
            // return 1 if the upper bound is equal to the minimum output
            if (upper == 1) {
                return 1;
            } else {
                return BCUtil.getInstance().randRange(1, upper);
            }
        }
    }
    
    /**
     * Generates a Transaction and returns it to the caller.
     * @return A generated Transaction.
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
        int nOutputs = 1;
        if (nInputs > 1) {
            BCUtil.getInstance().randRange(1, nInputs);
        }

        // generate some outputs
        Output[] outputs = new Output[nOutputs];
        
        // generate the output content
        for (int i = 0; i < outputs.length; i++) {
            int output = 1;
            try {
                output = getOutputAmt(inputAmt, outputs.length-i);
            } catch (Exception e) {
                System.out.println(String.format("An error has occured generating output amount for output %d, using the default output amount 1.", i+1));
            }
            inputAmt -= output;
            outputs[i] = new Output(output, i, "filler script");
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
     * Sets up a miner, subscribes it, generates transactions, transactions and broadcasts them to the subscribed listener.
     */
    public void run() {
        int txGenerated = 0;
        
        while (txGenerated < nTxAmt) {
            if (bRunning) {
                // Generate a transaction and send it to the subbed miners
                push(generateTx());

                // Increment the number of generated transactions
                txGenerated++;

                // sleep for the specified transaction time
                try {
                    Thread.sleep(Block.BLOCK_TIME/10);
                } catch (InterruptedException ex) {
                    System.out.println(ex.getMessage());
		        }
            }
        }
    }

    /**
     * Subscribes the indicated subscriber to receive transactions.
     * @param subscriber A subscriber to subscribe.
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
