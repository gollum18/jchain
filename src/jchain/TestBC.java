package jchain;

import java.util.LinkedList;

import jchain.bc.*;
import jchain.net.*;
import jchain.tests.MiningTestHarnessThread;
import jchain.util.*;

public class TestBC {

    /**
     * Runs the test class.
     */
    public static void main(String[] args) {
        // test blockchain functionality
        try {
            outputTest(testBlockchain(10), "Blockchain");
        } catch (Exception ex) {
            System.out.println(ex.toString());
        }
        // test mining functionality
        // 1 miner, 91 transactions
        try {
            outputTest(testMining(1, 91), "Mining");
        } catch (Exception ex) {
            System.out.println(ex.toString());
        }
    }
    
    public static void outputTest(boolean testResult, String test) {
        if (testResult) {
            System.out.println("Test " + test + ": Passed!");
        } else {
            System.out.println("Test " + test + ": Failed!");
        }
    }

    public static boolean testBlockchain(int txAmt) {
        // create 10 transaction objects
        Transaction[] txArray = new Transaction[txAmt];
        int input = 0, output = 0;
        String[] inputs = null, outputs = null;
        for (int i = 0; i < txArray.length; i++) {
            input = (int)Math.floor(Math.random() * 100);
            output = (int)Math.floor(Math.random() * input);
            inputs = new String[]{"A:"+String.valueOf(input)};
            outputs = new String[]{"B:"+String.valueOf(output)};
            txArray[i] = new Transaction(inputs, outputs);
        }

        // generate the previous hash
        // SHA-256 hashes are 64 characters in length, pad an empty string
        // with 64 zeros
        String genesisHash = BCUtil.padString(64, '0', "");
        
        // create the genesis transaction
        Transaction genesisTx = new Transaction(
                new String[]{"A:30"},
                new String[]{"B:30"});

        // create a collection object and put the genesis transaction in it
        LinkedList<Transaction> txList = new LinkedList<>();
        txList.add(genesisTx);

        // create the genesis block
        Block genesisBlock = new Block(genesisHash, txList);
        
        // create a chain instance passing in the initial block
        BC chain = new BC(genesisBlock);

        // prep for the next block
        txList.clear();
        for (int i = 0; i < 5; i++) {
            txList.add(txArray[i]);
        }

        // create the next block at height 1
        chain.addBlock(new Block(chain.getLeadBlock().getHash(), txList));

        // prep for the next block
        txList.clear();
        for (int i = 5; i < txArray.length; i++) {
            txList.add(txArray[i]);
        }

        // create the next block at height 2
        chain.addBlock(new Block(chain.getLeadBlock().getHash(), txList));

        // get the block at height 1 and print it
        System.out.println("Retrieving the block at height 1:");
        System.out.println(chain.getBlockByHeight(1));

        // get a random transaction from the chain and print it
        int pos = (int)(Math.floor(Math.random()*txArray.length));
        System.out.println("Getting tx with hash: " + txArray[pos].getHash() + " from the chain:");
        String txHash = txArray[pos].getHash();
        System.out.println(chain.getTransactionByHash(txHash));

        return true;
    }
    
    public static boolean testMining(int miners, int txs) {
        if (miners < 1) {
            throw new IllegalArgumentException("Error: Cannot test mining! Must specify at least 1 miner!");
        }
        if (txs < 10) {
            throw new IllegalArgumentException("Error: Cannot test mining! Must specify at least 10 transactions to be created!");
        }
        // Creates a transaction generator, testing 91 transactions
        MiningTestHarnessThread testHarness = new MiningTestHarnessThread(txs);
        // Subscribe the miner to receive transactions
        for (; miners > 0; miners--) {
            // all miners in this harness have the same pool size
            testHarness.subscribe(new Miner());
        }
        // Runs the test harness
        testHarness.run();
        
        return true;
    }

} // END TestBC
