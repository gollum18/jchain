package jchain.util;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import jchain.bc.Transaction;
import jchain.util.BCUtil;

// todo: convert this to a generic structure so I can use it in other projects
public class MerkleTree {

    // fields

    private MerkleNode mRoot;

    // constructors

    /**
     * Return an instance of a empty MerkleTree.
     */
    public MerkleTree() {}

    /**
     * Return an instance of a MerkleTree containing a single 
     *  transaction.
     * @param tx The transaction to store in the root of the tree.
     * @return An instance of the MerkleTree class.
     */
    public MerkleTree(Transaction tx) {
        if (tx == null) {
            throw new IllegalArgumentException();
        }
        mRoot = new MerkleNode(tx);
    }

    /**
     * Return an instance of a MerkleTree containing all of the 
     *  transactions in the given collection.
     * @param txList A collection containing Transaction objects.
     * @return An instance of the MerkleTree class.
     */
    public MerkleTree(Collection<Transaction> txList) {
        if (txList == null) {
            throw new IllegalArgumentException();
        }
        Iterator<Transaction> iterator = txList.iterator();
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
    public void add(Transaction tx) {
        if (tx == null) {
            throw new NullPointerException();
        }
        if (mRoot == null) {
            mRoot = new MerkleNode(tx);
        } else {
            if (contains(tx)) {
                throw new IllegalArgumentException("Cannot add transaction! Transaction already in tree.");
            } mRoot.add(tx);
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
    public boolean contains(String txHash) {
        if (txHash == null || txHash.length() == 0) {
            throw new IllegalArgumentException();
        }
        if (mRoot == null) {
            throw new NullPointerException();
        } return mRoot.contains(txHash);
    }

    /**
     * Determines if the tree contains the specified transaction or not.
     * @param tx A Transaction object.
     * @return True if the tree contains the transaction, false otherwise.
     */
    public boolean contains(Transaction tx) {
        return contains(tx.getHash());
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
    public Transaction get(String txHash) {
        if (txHash == null || txHash.length() == 0) {
            throw new IllegalArgumentException();
        }
        if (mRoot == null) {
            throw new NullPointerException();
        } return mRoot.get(txHash);
    }

    /**
     * Rebuilds a MerkleTree given a List of transactions.
     * @param txList A List<Transaction> of transactions.
     * @exception UnsupportedOperationException Thrown if called, this 
     *  method is not implemented yet.
     */
    public static MerkleTree load(List<Transaction> txList) {
        // todo: implement loading a merkle tree given a 
        //   collection
        throw new UnsupportedOperationException();        
    }

    /**
     * Persists the MerkleTree to local storage.
     * @exception UnsupportedOperationException Thrown if called, this 
     *  method is not implemented yet.
     */
    public void save() {
        // todo: implement writing the tree to file
        throw new UnsupportedOperationException();
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

        private Transaction mTransaction;
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
        public MerkleNode(Transaction tx) {
            if (tx == null) {
                throw new NullPointerException();
            }
            mTransaction = tx;
        }

        // methods

        /**
         * Computes a SHA-256 double hash hexstring representing the 
         *  MerkleNode.
         * @return A SHA-256 double hash hexstring.
         */
        public String computeHash() {
            // check for a leaf node
            if (mTransaction != null) {
                return mTransaction.getHash();
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
        public void add(Transaction tx) {
            // check the transaction
            if (tx == null) {
                throw new IllegalArgumentException();
            }
            // check for a leaf node
            if (mTransaction != null) {
                mLeft = new MerkleNode(mTransaction);
                mRight = new MerkleNode(tx);
                mTransaction = null;
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
                    mLeft.add(tx);
                } else if (right < left) {
                    mRight.add(tx);
                } else {
                    // if the heights are the same pick a branch
                    //   at random
                    if (Math.random() <= 0.5) {
                        mLeft.add(tx);
                    } else {
                        mRight.add(tx);
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
        public boolean contains(String txHash) {
            // check txHash
            if (txHash == null || txHash.length() == 0) {
                throw new IllegalArgumentException();
            }
            // check for a leaf node
            if (mTransaction != null) {
                if (mTransaction.getHash().equals(txHash)) {
                    return true;
                } return false;
            }
            // otherwise we are at an internal node
            else {
                // try the left sub-tree
                boolean left = false;
                if (mLeft != null) {
                    left = mLeft.contains(txHash);
                }
                // try the right sub-tree
                boolean right = false;
                if (mRight != null) {
                    right = mRight.contains(txHash);
                }
                // only one needs to be true to indicate we 
                //   found the a node with the txhash
                return left || right;
            }
        } // end contains

        /**
         * Attempts to get a transaction from the tree using the 
         *   transaction hash.
         * @param txHash A SHA-256 double hash hexstring.
         */
        public Transaction get(String txHash) {
            // check the hash
            if (txHash == null || txHash.length() == 0) {
                throw new IllegalArgumentException();
            }
            // check for a leaf node
            if (mTransaction != null) {
                if (mTransaction.getHash().equals(txHash)) {
                    return mTransaction;
                } return null;
            }
            // otherwise we are at an internal node
            else {
                // try the left sub-tree
                Transaction tx = null;
                if (mLeft != null) {
                    tx = mLeft.get(txHash);
                    if (tx != null) {
                        if (tx.getHash().equals(txHash)) {
                            return tx;
                        }
                    }
                }
                // try the right sub-tree
                if (mRight != null) {
                    tx = mRight.get(txHash);
                    if (tx != null) {
                        if (tx.getHash().equals(txHash)) {
                            return tx;
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
            if (mTransaction != null) {
                return mTransaction.toString();
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
