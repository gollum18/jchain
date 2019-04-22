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
        if (txAmt <= 0) {
            throw new IllegalArgumentException("Error: Cannot test blockchain functionality! Must specify at least one transaction be created!");
        }
        // Create the test and run it
        BCFuncTest funcTest = new BCFuncTest(txAmt);
        try {
            funcTest.run();
        } catch (Exception ex) {
            return false;
        }
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
        MiningHarnessTest testHarness = new MiningHarnessTest(txs);
        // Subscribe the miner to receive transactions
        for (; miners > 0; miners--) {
            // all miners in this harness have the same pool size
            testHarness.subscribe(new Miner());
        }
        // Runs the test harness
        try {
            testHarness.run();
        } catch (Exception ex) {
            return false;
        }
        return true;
    }

} // END TestBC
