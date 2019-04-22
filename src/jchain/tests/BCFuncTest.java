package jchain.tests;

import java.util.LinkedList;

import jchain.bc.*;
import jchain.util.*;

public class BCFuncTest {

    Transaction[] mTxArray;
    BC mBC;

    public BCFuncTest(int txAmt) {
        if (txAmt <= 0) {
            throw new IllegalArgumentException("Error: Cannot test blockchain functionality, must include at least one transaction in the test!");
        }
        mTxArray = new Transaction[txAmt];
    }

    public Transaction getTransaction(int index) {
        if (index < 0 || index >= mTxArray.length) {
            throw new IndexOutOfBoundsException("Error: Cannot index transaction, index is outside of the bounds of the TX array!");
        }
        return mTxArray[index];
    }

    public BC getBC() {
        return mBC;
    }

    public int getTxAmt() {
        return mTxArray.length;
    }

    public void setTxAmt(int txAmt) {
        if (txAmt <= 0) {
            throw new IllegalArgumentException("Error: Cannot test blockchain functionality, must include at least one transaction in the test!");
        }
        mTxArray = new Transaction[txAmt];
    }

    public void run() {
        // create some transaction objects
        int input = 0, output = 0;
        String[] inputs = null, outputs = null;
        for (int i = 0; i < mTxArray.length; i++) {
            input = (int)Math.floor(Math.random() * 100);
            output = (int)Math.floor(Math.random() * input);
            inputs = new String[]{"A:"+String.valueOf(input)};
            outputs = new String[]{"B:"+String.valueOf(output)};
            mTxArray[i] = new Transaction(inputs, outputs);
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
        mBC = new BC(genesisBlock);

        // prep for the next block
        txList.clear();
        for (int i = 0; i < 5; i++) {
            txList.add(mTxArray[i]);
        }

        // create the next block at height 1
        mBC.addBlock(new Block(mBC.getLeadBlock().getHash(), txList));

        // prep for the next block
        txList.clear();
        for (int i = 5; i < mTxArray.length; i++) {
            txList.add(mTxArray[i]);
        }

        // create the next block at height 2
        mBC.addBlock(new Block(mBC.getLeadBlock().getHash(), txList));

        // get the block at height 1 and print it
        System.out.println("Retrieving the block at height 1:");
        System.out.println(mBC.getBlockByHeight(1));

        // get a random transaction from the chain and print it
        int pos = (int)(Math.floor(Math.random()*mTxArray.length));
        System.out.println("Getting tx with hash: " + mTxArray[pos].getHash() + " from the chain:");
        String txHash = mTxArray[pos].getHash();
        System.out.println(mBC.getTransactionByHash(txHash));
    }

}
