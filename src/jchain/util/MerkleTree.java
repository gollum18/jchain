package jchain.util;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import jchain.bc.Transaction;
import jchain.util.BCUtil;
import jchain.util.Hashable;

/**
 * Defines a generic MerkleTree structure that generates  root hashes for use in cryptographic operations.
 * I do not provide much documentation in this class as it is a dummy class. All the work is done in the MerkleNode class.
 * @param T The type to store in the tree, must implement the Hashable interface.
 * @author Christen Ford
 */
public class MerkleTree<T extends Hashable> {

    //
    // FIELDS
    //

    // The Merkle root node.
    private MerkleNode mRoot;

    //
    // CONSTRUCTORS
    //

    /**
     * Returns an instance of an EMPTY MerkleTree.
     */
    public MerkleTree() {}

    /**
     * Return an instance of a MerkleTree containing all of the transactions in the given collection.
     * @param hashableList A collection containing Transaction objects.
     */
    public MerkleTree(Collection<T> hashableList) {
        // throw an exception if the list is null
        if (hashableList == null) {
            throw new IllegalArgumentException("ERROR: Cannot create MerkleTree, no list of transactions given!");
        }
        // throw an exception if the lists iterator is empty
        Iterator<T> iterator = hashableList.iterator();
        if (!iterator.hasNext()) {
            throw new IllegalArgumentException("ERROR: Cannot create MerkleTree, list of transactions is empty!");
        }
        // create the MerkleTree by iterating the iterator
        mRoot = new MerkleNode(iterator.next());
        // the tree wil have at least one item in it
        while (iterator.hasNext()) {
            T iterable = iterator.next();
            // only add iterable if it isnt null, issue warning otherwise
            if (iterable != null) {
                mRoot.add(iterable);
            } else {
                System.err.println("WARNING: Iterable not added to MerkleTree, iterable was null!");
            }
        }
    }

    //
    // METHODS
    //

    /**
     * Adds an object implementing the Hashable interface to the MerkleTree.
     * @param hashable A hashable object.
     */
    public void add(T hashable) {
        if (hashable == null) {
            throw new NullPointerException();
        }
        if (mRoot == null) {
            mRoot = new MerkleNode(hashable);
        } else {
            // check if the hashable is the root node of the tree, exception if it is, otherwise, let the root node handle adding it
            if (contains(hashable.getHash())) {
                throw new IllegalArgumentException("Cannot add transaction! Transaction already in tree.");
            } mRoot.add(hashable);
        }
    }

    /**
     * Computes the root hash of the MerkleTree.
     * @return A SHA-256 double hash hexstring representing the Merkle
     *  root hash.
     */
    public String computeHash() {
        if (mRoot == null) {
            throw new NullPointerException();
        } return mRoot.computeHash();
    }

    /**
     * Determines whether the tree contains a hashable with the specified transaction hash.
     * @param hash A SHA-256 double hash hextstring.
     * @return True if the tree contains a hashable with the indicated hash, false otherwise.
     */
    public boolean contains(String hash) {
        if (hash == null || hash.length() == 0) {
            throw new IllegalArgumentException();
        }
        if (mRoot == null) {
            throw new NullPointerException();
        } return mRoot.contains(hash);
    }

    /**
     * Gets the number of items stored in the tree.
     * @return The number of items in the tree.
     */
    public int count() {
        if (mRoot == null) {
            return 0;
        } return mRoot.count();
    }

    /**
     * Attempts to retrieve a hashable from the tree using the indicated hash.
     * @param hash A SHA-256 double hash hexstring.
     * @return A hashable if one can be found with the given hash, null otherwise.
     */
    public T get(String hash) {
        if (hash == null || hash.length() == 0) {
            throw new IllegalArgumentException();
        }
        if (mRoot == null) {
            throw new NullPointerException();
        } return mRoot.get(hash);
    }

    /**
     * Returns a string representation of the MerkleTree.
     * @return A string representation of the MerkleTree.
     */
    public String toString() {
        if (mRoot == null) {
            return "No transactions to display in MerkleTree!";
        } return mRoot.toString();
    }

    /**
     * Internal class that does the heavy lifting of manipulating the MerkleTree.
     * Internal value is the data type assigned to the MerkleTree class.
     * @author Christen Ford
     */
    private class MerkleNode {
        
        //
        // FIELDS
        //

        // The value stored in this node, will be null if this is an internal node
        private T mHashable;
        // The root of the left sub-tree
        private MerkleNode mLeft;
        // The root of the right sub-tree
        private MerkleNode mRight;

        //
        // CONSTRUCTORS
        //

        /**
         * Returns an instance of a MerkleNode object containing the given
         *  transaction.
         * @param tx A Transaction object.
         * @exception NullPointerException If the Transaction is null.
         * @return An instance of a MerkleNode object.
         */
        public MerkleNode(T hashable) {
            if (hashable == null) {
                throw new NullPointerException();
            }
            mHashable = hashable;
        }

        //
        // METHODS
        //

        /**
         * Computes a SHA-256 double hash hexstring representing the MerkleNode.
         * @return A SHA-256 double hash hexstring.
         */
        public String computeHash() {
            // check for a leaf node
            if (mHashable != null) {
                return mHashable.getHash();
            }
            // build the hash
            StringBuilder sb = new StringBuilder();
            // compute the left and right sub-tree hashes, add them to the to-be-hashed stringbuilders content
            if (mLeft != null) {
                sb.append(mLeft.computeHash());
            }
            if (mRight != null) {
                sb.append(mRight.computeHash());
            }
            // return the double hash to the caller
            return BCUtil.getInstance().doubleHash(sb.toString());
        }

        /**
         * Adds a hashable object to the MerkleTree. This add method prevents the MerkleTree from ever being skewed.
         * @param hashable An object implementing the Hashable interface.
         * @exception IllegalArgumentException If the transaction is null.
         */
        public void add(T hashable) {
            // check the transaction
            if (hashable == null) {
                throw new IllegalArgumentException();
            }
            // check for a leaf node, convert this guy to an internal node and push down the left and right sub-trees
            if (mHashable != null) {
                // left child will have the value of this node
                mLeft = new MerkleNode(mHashable);
                // right child will have the value that is being added
                mRight = new MerkleNode(hashable);
                mHashable = null;
            }
            // otherwise if were at an internal node, 'preserve' balance of the tree using the height() method
            else {
                // get the height of the left sub-tree
                int left = Integer.MAX_VALUE;
                if (mLeft != null) {
                    left = mLeft.height();
                }
                // get the height of the right sub-tree
                int right = Integer.MAX_VALUE;
                if (mRight != null) {
                    right = mRight.height();
                }
                // add to the tree with the lesser height
                if (left < right) {
                    mLeft.add(hashable);
                } else if (right < left) {
                    mRight.add(hashable);
                } else {
                    // if the heights are the same pick a branch
                    //   at random
                    if (Math.random() < 0.5) {
                        mLeft.add(hashable);
                    } else {
                        mRight.add(hashable);
                    }
                }
            }
        } // end add

        /**
         * Determines if this sub-tree contains a specified hashable or not based on the indicated hash.
         * @param hash A SHA-256 double hash hexstring.
         * @exception IllegalArgumentException If the hashable hash is null or empty.
         */
        public boolean contains(String hash) {
            // check txHash
            if (hash == null || hash.length() == 0) {
                throw new IllegalArgumentException();
            }
            // check for a leaf node
            if (mHashable != null) {
                if (mHashable.getHash().equals(hash)) {
                    return true;
                } return false;
            }
            // otherwise we are at an internal node
            else {
                // try the left sub-tree
                boolean left = false;
                if (mLeft != null) {
                    left = mLeft.contains(hash);
                }
                // try the right sub-tree
                boolean right = false;
                if (mRight != null) {
                    right = mRight.contains(hash);
                }
                // only one needs to be true to indicate we 
                //   found the a node with the txhash
                return left || right;
            }
        } // end contains

        /**
         * Gets the number of hashables stored in the sub-tree.
         * @return The number of hashables stored in the sub-tree.
         */
        public int count() {
            // check for a leaf node
            if (mHashable != null) {
                return 1;
            }
            int left = 0, right = 0;
            // get count of left if it exists
            if (mLeft != null) {
                return mLeft.count();
            }
            // get count of right if it exists
            if (mRight != null) {
                return mRight.count();
            }
            // return the sum of the left and right subtrees
            return left + right;
        }

        /**
         * Attempts to get a hashable from the tree using the indicated hash.
         * @param txHash A SHA-256 double hash hexstring.
         */
        public T get(String hash) {
            // check the hash
            if (hash == null || hash.length() == 0) {
                throw new IllegalArgumentException();
            }
            // check for a leaf node
            if (mHashable != null) {
                if (mHashable.getHash().equals(hash)) {
                    return mHashable;
                } return null;
            }
            // otherwise we are at an internal node
            else {
                // try the left sub-tree
                T hashable = null;
                if (mLeft != null) {
                    hashable = mLeft.get(hash);
                    if (hashable != null) {
                        if (hashable.getHash().equals(hash)) {
                            return hashable;
                        }
                    }
                }
                // try the right sub-tree
                if (mRight != null) {
                    hashable = mRight.get(hash);
                    if (hashable != null) {
                        if (hashable.getHash().equals(hash)) {
                            return hashable;
                        }
                    }
                }
                // otherwise no tx found with hash
                return null;
            }
        } // end get

        /**
         * Gets the height of a sub-tree.
         * @return The height of the sub-tree this method is called on.
         */
        public int height() {
            if (mLeft == null && mRight == null) {
                return 1;
            }
            int left = 0;
            if (mLeft != null) {
                left = mLeft.height() + 1;
            }
            int right = 0;
            if (mRight != null) {
                right = mRight.height() + 1;
            }
            return Math.max(left, right);
        } // end height

        /**
         * Returns a String representation of the MerkleNode.
         * @return A String representing the MerkleNode.
         */
        public String toString() {
            if (mHashable != null) {
                return mHashable.toString();
            }
            StringBuilder sb = new StringBuilder();
            if (mLeft != null) {
                sb.append(mLeft.toString()).append("\n");
            }
            if (mRight != null) {
                sb.append(mRight.toString()).append("\n");
            }
            return sb.toString();
        }

    } // end MerkleNode

} // end MerkleTree
