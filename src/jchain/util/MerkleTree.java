package jchain.util;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import jchain.bc.Transaction;
import jchain.util.BCUtil;
import jchain.util.Hashable;

// todo: convert this to a generic structure so I can use it in other projects
public class MerkleTree<T extends Hashable> {

    // fields

    private MerkleNode mRoot;

    // constructors

    /**
     * Empty constructor.
     */
    public MerkleTree() {}

    /**
     * Return an instance of a MerkleTree containing all of the 
     *  transactions in the given collection.
     * @param txList A collection containing Transaction objects.
     * @return An instance of the MerkleTree class.
     */
    public MerkleTree(Collection<T> hashableList) {
        if (hashableList == null) {
            throw new IllegalArgumentException();
        }
        Iterator<T> iterator = hashableList.iterator();
        if (!iterator.hasNext()) {
            throw new IllegalArgumentException();
        }
        mRoot = new MerkleNode(iterator.next());
        while (iterator.hasNext()) {
            mRoot.add(iterator.next());
        }
    }

    // methods

    /**
     * Adds a transaction to the MerkleTree.
     * @param tx A transaction object.
     * @exception IllegalArgumentException If the transaction is already contained in the tree.
     */
    public void add(T hashable) {
        if (hashable == null) {
            throw new NullPointerException();
        }
        if (mRoot == null) {
            mRoot = new MerkleNode(hashable);
        } else {
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
     * Determines whether the tree contains a transaction with the 
     *  specified transaction hash.
     * @param txHash A SHA-256 double hash hextstring.
     * @exception IllegalArgumentException If the transaction hash is 
     *  null or empty.
     * @exception NullPointerException If the root of the tree is 
     *  null, i.e. if the tree is contains 0 transactions.
     * @return True if the tree contains a transaction with the 
     * indicated transaction hash, false otherwise.
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
     * Attempts to retrieve a transaction from the tree using the 
     *  transaction hash.
     * @param txHash A SHA-256 double hash hexstring.
     * @exception IllegalArgumentException If the transaction hash is 
     *  null or empty.
     * @exception NullPointerException If the root of the tree is null, 
     * i.e. if the tree contains 0 transactions.
     * @return A transaction if one can be found with the given 
     *  transaction hash, null otherwise.
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
     * Internal class that does the heavy lifting of manipulating 
     *  the MerkleTree.
     */
    private class MerkleNode {
        
        // fields

        private T mHashable;
        private MerkleNode mLeft;
        private MerkleNode mRight;

        // constructors

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

        // methods

        /**
         * Computes a SHA-256 double hash hexstring representing the 
         *  MerkleNode.
         * @return A SHA-256 double hash hexstring.
         */
        public String computeHash() {
            // check for a leaf node
            if (mHashable != null) {
                return mHashable.getHash();
            }
            StringBuilder sb = new StringBuilder();
            if (mLeft != null) {
                sb.append(mLeft.computeHash());
            }
            if (mRight != null) {
                sb.append(mRight.computeHash());
            }
            return BCUtil.getInstance().doubleHash(sb.toString());
        }

        /**
         * Adds a transaction to the MerkleTree.
         * @param tx A transaction.
         * @exception IllegalArgumentException If the transaction is null.
         */
        public void add(T hashable) {
            // check the transaction
            if (hashable == null) {
                throw new IllegalArgumentException();
            }
            // check for a leaf node
            if (mHashable != null) {
                mLeft = new MerkleNode(mHashable);
                mRight = new MerkleNode(hashable);
                mHashable = null;
            }
            //  height to 'preserve' balance of tree
            else {
                // I'm not fond of this method
                // todo: use another default for left and right
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
                    if (Math.random() <= 0.5) {
                        mLeft.add(hashable);
                    } else {
                        mRight.add(hashable);
                    }
                }
            }
        } // end add

        /**
         * Determines if this sub-tree contains a specified 
         *   transaction or not based on the transaction hash.
         * @param txHash A SHA-256 double hash hexstring.
         * @exception IllegalArgumentException If the transaction hash is 
         *  null or empty.
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
         * Gets the number of items stored in the sub-tree.
         * @return The number of items stored in the sub-tree.
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
         * Attempts to get a transaction from the tree using the 
         *   transaction hash.
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
