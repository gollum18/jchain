# jchain/util Package
This package contains classes that provide useful utility functions for the blockchain. I have tried to explain each within its respective class, but I will briefly explain them here to.

Most of these classes are not meant to be directly instantiated, rather they usually implement the Singleton design pattern and provide a method for retrieving a singleton instance of the class.

---
## Classes

### BCUtil
This class provides helper functions for the various classes in the blockchain such as computing hashes and converting a hash to a hexstring (actually at this time, thats all that it implements). This class is a singleton so we do not have to keep initializing new MessageDigest objects for hash generation (although in reality, I do not know if this will work well in a P2P environmennt).

### Hashable
This interface requires implementors to implement the computeHash() method required of any class that generates a SHA-256 double hash.

### MerkleTree
This class provides implements a recursive MerkleTree data structure. A MerkleTree or HashTree is a binary tree data structure used to generate cryptographically resilient hashes. In the context of a blockchain, it is used to store transactions in order to generate the Merkle root hash used in block hash calculations. There is no explicit requirement that Merkle trees be balanced, however I have tried to implement this tree in such a manner that it is self-balancing (as it is my (completely untested) assumption that a fully balanced Merkle tree produces better hashes than an unbalanced Merkle tree).

Merkle trees are push-down trees where internal nodes do not contain any actual date, they just perform hash computation on their children, while leaf nodes contain actual data that is stored in the tree. A hash is computed bycalling a recursive function on the root node to descend down the tree to each leaf node and compute the hash on the way back up. As the hash function returns up the tree, a new hash is generated at each intermediate internal node that is the hash of the nodes child hashes. This continues all the back up to the root node, giving up the Merkle root hash.

Currently, this tree only functions with Transaction objects, however it is on my todolist to convert this structure to a generic tree. I just have to implement an interface to force objects that want stored in the tree to implement the required methods, as well as change some things in the MerkleTree class to accomadate this.
