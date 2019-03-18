Name: Christen Ford
CSUID: 2741896
Project: Project 3
Class: CIS 593 - Blockchain and Cryptocurrency Programming

Language Used: Java

Note: Alongside this documentation, you should have received the following classes (as Java source code found inside the src folder with the following filepaths):
- jchain/bc/BC.java
- jchain/util/BCUtil.java
- jchain/bc/Block.java
- jchain/bc/Header.java
- jchain/util/MerkleTree.java
- jchain/TestBC.java
- jchain/bc/Transaction.java

While I feel that the documentation contained in each source file should be able to speak to the class itself. A brief overview of each class is presented here:

- BC.java: Implements a Blockchain (in memory for now). It defines methods for searching for blocks and transactions (by hash, and in the case of blocks, by height). Of course, it also allows for adding blocks (currently, they do not need validated). Finally, BC.java implements a toString() override that 'pretty' prints all of the blocks in the chain.

- BCUtil.java: Implements both static and non-static (singleton) methods that provide utility functions for the other classes. These methods include calculating a double hash and converting a byte array to a hex representation. Other methods will certainly be added to it as this implementation evolves.

- Block.java: Implements a block in a blockchain. Contains methods for interacting with the block header where appropriate as well as computing the blockhash. Blocks are immutable, wherein, once they are created, they cannot be changed (that is, they expose no ways to change their underlying data sources).

- Header.java: Implements a block header in a blockchain. Just like blocks, block headers are immutable. Once they are created, they cannot be changed. A block header primarily acts as a repository for the block storing useful information like the nonce and previous block hash.

- MerkleTree.java: Implements a binary Merkle (Hash) tree. This implementation is defined recursively and contains an internal MerkleNode class that handles all of the heavy operations for the MerkleTree including adding Transactions and computing the root hash. The MerkleTree exposes two constructors, one that takes a single transaction (genesis transaction), or a collection of transactions. However, once created, adding additional transactions to the tree must be done in a serial, non-batch fashion. This is such a useful data structure, that I am planning on making it generic for use in other data structures (after I work out some kinks with it - none of which impact adding or computing hashes - this is more refactoring than anything).

- TestBC.java: Implements basic testing of the BC class (and by extension, Block, Header, Transaction, and MerkleTree classes). Testing is setup per the parameters of the assignment. Transaction inputs and outputs are generated randomly. Each time you run this class you *should* receive a unique blockchain back. I have yet to run into a case where the random transaction generation generates an exactly identical chain.

- Transaction.java: Implements a transaction in a blockchain. Transactions can store any number of inputs and outputs (bu they must have at least one input and at least one output. Valid I/O format is always a string in the form (address:amount), although this is not explictly checked yet - something for my todo list I suppose. Inputs and outputs are passed into the transaction during construction as two string arrays. These are then stored internally in their own respective ArrayLists which are intialized to the same size as their respective array (to save as much space as possible. I considered using arrays for this, but I wanted to offer a way to allow users to access the inputs and outputs without being able to modify their containing data structre, while at the same time, being space aware.

Compilation Instructions:
First extract the contents of the zip to a directory. Next cd into the root directory of the project (the directory you extracted the zip to). Finally, you have two choices to compile the project:

1.) If you have Python 3.3+ installed, run the following command:
    ./build.py (on Linux/Mac)
The command will automatically select your python3 interpreter and invoke build.py. If this fails, then manually specify your python3 installation like so:
    python3 build.py (on Ubuntu at least, you may have to try a different specification for python3 on different platforms)

If you are on Windows, then make sure that python3 is in your PATH environment variable (either as python3 or simply python if you do not have python2 installed, and invoke the following:
    python build.py (if python3 is the only version you have installed)
    python3 build.py (if you also have python2 installed)

2.) If the build script did not work for you, then you must manually compile the software. Fortunately, this is only two commands (at least for now, but it is bound to change):
    javac -cp ./src -d ./out jchain/TestBC.java (for now, TestBC links in all the other source files, this will certainly change in the future)

It is preferred that you compile the project using the python build script. It automates searching for the all of the java files so you do not have to. It will also automatically call javac on them too.

Of course, if you are using an IDE to work on this project, all of this is handled for you by the IDE itself.

Running the Project:
To run the project, assuming you have correctly compiled the project, cd into the root directory of the project (it should contain the src and out directories, build.py, and this document). 

From there enter the following command to test the project:
java -cp ./out jchain.TestBC
