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
        // Subscribe the miner to receive transactions
        for (; miners > 0; miners--) {
            Miner miner = new Miner();
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
