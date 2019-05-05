package jchain;

import java.util.LinkedList;

import jchain.bc.*;
import jchain.net.*;
import jchain.tests.*;
import jchain.util.*;

/**
 * Contains setup code for automatically launching and 
 * running test against the jchain codebase.
 * Note that test classes are stored in the 
 * jchain.test package.
 * @author Christen Ford
 * @since 4/21/2019
 */
public class TestBC {

    /**
     * Runs the test class.
     * @param args The arguments to passes to main.
     */
    public static void main(String[] args) {
        // test mining functionality
        // 1 miner, 91 transactions
        try {
            outputTest(testMining(1, 91), "Mining");
        } catch (Exception ex) {
            System.err.println(ex.toString());
            System.err.println(ex.getMessage());
        }
    }
    
    public static void outputTest(boolean testResult, String test) {
        if (testResult) {
            System.out.println("Test " + test + ": Passed!");
        } else {
            System.out.println("Test " + test + ": Failed!");
        }
    }
    
    public static boolean testMining(int miners, int txs) {
        if (miners < 1) {
            throw new IllegalArgumentException("Error: Cannot test mining! Must specify at least 1 miner!");
        }
        if (txs < 10) {
            throw new IllegalArgumentException("Error: Cannot test mining! Must specify at least 10 transactions to be created!");
        }
        // Creates a transaction generator, testing 91 transactions
        MiningHarnessTest testHarness = new MiningHarnessTest(txs);
        // create a test blockchain with a genesis block, each 
        //  miner starts with the same blockchain
        String[] testInputs = new String[] {
            "A:10"
        };
        Output[] testOutputs = new Output[] {
            new Output(10, 0, "genesis tx")
        };
        LinkedList<Transaction> testTxs = new LinkedList<>();
        testTxs.add(new Transaction(testInputs, testOutputs));
        Header testHeader = new Header("0000000000000000", testTxs, 0);
        BC testBC = new BC(new Block(testTxs, testHeader));
        // Subscribe the miners to receive transactions
        for (; miners > 0; miners--) {
            Miner miner = new Miner(testBC);
            // all miners in this harness have the same pool size
            testHarness.subscribe(miner);
        }
        // Runs the test harness
        try {
            testHarness.run();
        } catch (Exception ex) {
            System.err.println(ex);
            ex.printStackTrace();
            return false;
        }
        return true;
    }

} // END TestBC
