# JChain Documentation (v0.0.1)
##### (or what passes for it)

---
## Project Description
'jchain' is my first attempt at creating a mineable blockchain. It is only in its nascent stages right now, so it lacks networking functionality. The main components to build a blockchain are there though such as blocks, transactions, and even a Merkle tree implementation. I'm not trying to be overambitious with this implementation, in fact it is more a toy blockchain than anything and doesn't really provide any realistic application. 

'jchain' is more intended as a learning aid to myself, as well as anyone else who wants to learn blockchain in a managed code environment. Given that it is written in Java, it will never be as fast (or memory efficient) as say Bitcoin or Ethereum that is written in C/C++, however the use of Java facilitates more rapid development cycles since we don't have to worry about C trappings.

This project was built as a term project for CIS 593: Blockchain and Cryptocurrency Programming. 

I hold to Agile that the best documentation is as little documentation as possible. The functionality and description of each class should be accurately summed up in the respective source code files under the src directory, so they will not be included in this directory, since I tend to be heavily liberal with comments when I am writing code the source should speak for itself.

This project features Prrof-of-Work (POW) mining on a single machine. Adding networking functionality was not a goal of this project, that is left for future work. The miner mines blocks on a separate thread and will start doing so with a random probability of 0.375. I originally had the miner start mining at one transaction in the pool, however, the resulting blocks would always be of size one. This way, the miner will create blocks of random size.

The mining thread will automatically terminate when it reaches below a Blocks are limited to ten transaction in size. That said, each block contains one coinbase transaction as the first transaction in the block; the miner will randomly choose 1-9 other transactions to include in the block based on the siize of the transaction pool.

Currently the POW mining feature is set to RegTest difficulty. However, you may manually set the difficulty within the 'src\jchain\bc\Header.java' file. Aside from RegTest, the other difficulty included with JChain is the TestNet difficulty. Difficulty in Bitcoin-based POW is based on a 0xNNNNNNNN hexadecimal number. The first two leading digits are used as the exponent in the hash calculation while the remaining digits are used as the coefficient in the hash calculation.

The mining pool object is synchronized and thread-safe. All critical operations utilize Java's (rather heavy IMO) 'syncronized' construct. Once entered, said operations will either implictly acquire a lock, or they will block the calling thread. The lock is not released until the method that acquired the lock released it. Initially, the goal was to utilize constructs from Java's Concurrency class, however, this method is cleaner and more in line with modern Software Engineering techniques. 

---
## Project Structure
The project is divided into three folders, 'src', 'out', and 'docs'. The 'src' folder contains the source files for the project. The 'out' fodlder contains the compiled class files for the project. The 'docs' folder contains auto-generated javadoc.


The 'src' folder currently has the following topology:
```bash
src
└── jchain
    ├── bc
    │   ├── BC.java
    │   ├── Block.java
    │   ├── Header.java
    │   ├── Output.java
    │   └── Transaction.java
    ├── net
    │   ├── Miner.java
    │   ├── Publisher.java
    │   ├── Subscriber.java
    │   └── TxnMemoryPool.java
    ├── TestBC.java
    ├── tests
    │   └── MiningHarnessTest.java
    └── util
        ├── BCUtil.java
        ├── Hashable.java
        ├── IllegalOperationException.java
        ├── MerkleTree.java
        ├── NoSuchBlockException.java
        ├── NoSuchTransactionException.java
        └── README.md
```
     
Correspondingly, the 'out' folder has the following structure:
```bash
out
└── jchain
    ├── bc
    │   ├── BC.class
    │   ├── Block.class
    │   ├── Header.class
    │   ├── Output.class
    │   └── Transaction.class
    ├── MiningTestHarnessThread.class
    ├── net
    │   ├── Miner$1.class
    │   ├── Miner$MiningThread.class
    │   ├── Miner.class
    │   ├── Publisher.class
    │   ├── Subscriber.class
    │   ├── TransactionListener.class
    │   ├── TransactionPublisher.class
    │   └── TxnMemoryPool.class
    ├── TestBC.class
    ├── tests
    │   ├── BCFuncTest.class
    │   ├── MiningHarnessTest.class
    │   └── MiningTestHarnessThread.class
    └── util
        ├── BCUtil.class
        ├── Hashable.class
        ├── IllegalOperationException.class
        ├── MerkleTree$MerkleNode.class
        ├── MerkleTree.class
        ├── NoSuchBlockException.class
        └── NoSuchTransactionException.class
```

---
## Build/Compile Instructions
There is only one official way to compile JChain:
- Open a terminal in the root directory of the project.
- Type 'python scripts.py build'
- Hit the enter key
This invoke JChains automated build system. Essentially what happens is that the src folder is recursively analyzed and a sources.txt file is created in the projects root directory. This file is then fed into javac using the Java compilers annotation syntax. At this point, the Java compiler takes over and handles compiling the program.

Output is stored in the 'out' folder in the root directory of the project. It has the same package structure as the src 'folder'.

---
## Execution Instructions
To execute the `TestBC` class, execute the following command from the projects root directory:

**`java -cp out jchain.TestBC`**

Using the scripts.py file to launch the test class is currently a WIP. Conceptually, it should work fine, however, in practice, Java is complaining for no actual reason.

---
## Sample Output from running TestBC
Example output from running the TestBC class:


---


---
## Existing Bugs
- There is an exception beiing thrown when a Miner attempts to dump its transactions back into the pool. I am currently working on this issue.
