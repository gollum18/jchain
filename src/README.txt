Name: Christen Ford
CSUID: 2741896
Project: Project 3
Class: CIS 593 - Blockchain and Cryptocurrency Programming

Language Used: Java

Note: Alongside this documentation, you should have received the following classes (as Java source code):
- BC.java
- BCUtil.java
- Block.java
- Header.java
- MerkleTree.java
- TestBC.java
- Transaction.java

While I feel that the documentation contained in each source file should be able to speak to the class itself. A brief overview of each class is presented here:

- BC.java: Implements a Blockchain (in memory for now). It defines methods for searching for blocks and transactions (by hash, and in the case of blocks, by height). Of course, it also allows for adding blocks (currently, they do not need validated). Finally, BC.java implements a toString() override that 'pretty' prints all of the blocks in the chain.

- BCUtil.java: Implements both static and non-static (singleton) methods that provide utility functions for the other classes. These methods include calculating a double hash and converting a byte array to a hex representation. Other methods will certainly be added to it as this implementation evolves.

- Block.java: Implements a block in a blockchain. Contains methods for interacting with the block header where appropriate as well as computing the blockhash. Blocks are immutable, wherein, once they are created, they cannot be changed (that is, they expose no ways to change their underlying data sources).

- Header.java: Implements a block header in a blockchain. Just like blocks, block headers are immutable. Once they are created, they cannot be changed. A block header primarily acts as a repository for the block storing useful information like the nonce and previous block hash.

- MerkleTree.java: Implements a binary Merkle (Hash) tree. This implementation is defined recursively and contains an internal MerkleNode class that handles all of the heavy operations for the MerkleTree including adding Transactions and computing the root hash. The MerkleTree exposes two constructors, one that takes a single transaction (genesis transaction), or a collection of transactions. However, once created, adding additional transactions to the tree must be done in a serial, non-batch fashion. This is such a useful data structure, that I am planning on making it generic for use in other data structures (after I work out some kinks with it - none of which impact adding or computing hashes - this is more refactoring than anything).

- TestBC.java: Implements basic testing of the BC class (and by extension, Block, Header, Transaction, and MerkleTree classes). Testing is setup per the parameters of the assignment. Transaction inputs and outputs are generated randomly. Each time you run this class you *should* receive a unique blockchain back. I have yet to run into a case where the random transaction generation generates an exactly identical chain.

- Transaction.java: Implements a transaction in a blockchain. Transactions can store any number of inputs and outputs (bu they must have at least one input and at least one output. Valid I/O format is always a string in the form (address:amount), although this is not explictly checked yet - something for my todo list I suppose. Inputs and outputs are passed into the transaction during construction as two string arrays. These are then stored internally in their own respective ArrayLists which are intialized to the same size as their respective array (to save as much space as possible. I considered using arrays for this, but I wanted to offer a way to allow users to access the inputs and outputs without being able to modify their containing data structre, while at the same time, being space aware.

Compilation Instructions:
Extract all Java source files listed above to the same directory.

The best way to compile the project is to manually compile the source files using javac. It is imperative (at this time) that each Java source file be compiled in order for the project to function. You *should* be able to just compile TestBC.java and that in turn should cause javac to compile every class referenced by it. However, if not then compile them in this order:
    MerkleTree.java AND BCUtil.java (order does not matter in this case)
    Transaction.java
    Header.java
    Block.java
    BC.java
    TestBC.java
These are compiled using the following command: javac [filename]

Alternatively if you have at least Python 3.3 available on your machine, you can compile the project in one command by telling Python to tell javac to compile it for you:
    'python build.py'
This file simply executes javac for every *.java file in the source directory. It should be noted that 'build.py' is not aware of your java installation. I make the assumption that javac is set up in your Windows or Unix/Linux PATH environment variable. If it is not, build.py will fail. In that case, simply fall back to compiling the java source files by hand.

Running the Project:
Once you have the project compiled (either by hand, or through build.py), simply execute java on the TestBC.class file.
