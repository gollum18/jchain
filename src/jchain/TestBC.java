package jchain;

import java.util.LinkedList;

import jchain.bc.BC;
import jchain.bc.Block;
import jchain.bc.Transaction;

public class TestBC {

    public static final int TX_AMOUNT = 10;

    public static void main(String[] args) {
        // create 10 transaction objects
        Transaction[] txArray = new Transaction[TX_AMOUNT];
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
        String genesisHash = padString(64, '0', "");
        
        // create the genesis transaction
        Transaction genesisTx = new Transaction(
                new String[]{"A:30"},
                new String[]{"B:30"});

        // create a collection object and put the genesis transaction in it
        LinkedList<Transaction> txList = new LinkedList<>();
        txList.add(genesisTx);

        // create the genesis block
        Block genesisBlock = new Block(txList, genesisHash);
        
        // create a chain instance passing in the initial block
        BC chain = new BC(genesisBlock);

        // prep for the next block
        txList.clear();
        for (int i = 0; i < 5; i++) {
            txList.add(txArray[i]);
        }

        // create the next block at height 1
        chain.addBlock(new Block(txList, chain.getLeadBlock().getHash()));

        // prep for the next block
        txList.clear();
        for (int i = 5; i < txArray.length; i++) {
            txList.add(txArray[i]);
        }

        // create the next block at height 2
        chain.addBlock(new Block(txList, chain.getLeadBlock().getHash()));

        // get the block at height 1 and print it
        System.out.println("Testing function 1...");
        System.out.println(chain.getBlockByHeight(1));

        // get a random transaction from the chain and print it
        System.out.println("Testing function 2...");
        int pos = (int)(Math.floor(Math.random()*txArray.length));
        String txHash = txArray[pos].getHash();
        System.out.println(chain.getTransactionByHash(txHash));
    }

    // method adapted from: https://stackoverflow.com/questions/13475388/generate-fixed-length-strings-filled-with-whitespaces
    public static String padString(int width, char fill, String toPad) {
        return new String(new char[width - toPad.length()]).replace('\0', fill) + toPad;
    }

}
