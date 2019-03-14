import java.util.Collection;
import java.util.Iterator;

public class MerkleTree {

    private MerkleNode mRoot;

    /**
     * Return an instance of a empty MerkleTree.
     */
    public MerkleTree() {}

    /**
     * Return an instance of a MerkleTree containing a single 
     *  transaction.
     * @param tx The transaction to store in the root of the tree.
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

    /**
     * Adds a transaction to the MerkleTree.
     * @param tx A tranasction.
     */
    public void add(Transaction tx) {
        if (tx == null) {
            throw new NullPointerException();
        }
        if (mRoot == null) {
            mRoot = new MerkleNode(tx);
        } else {
            mRoot.add(tx);
        }
    }

    /**
     * Computes the root hash of the MerkleTree.
     */
    public String computeHash() {
        if (mRoot == null) {
            throw new NullPointerException();
        } return mRoot.computeHash();
    }

    public boolean contains(String txHash) {
        if (txHash == null || txHash.length() == 0) {
            throw new IllegalArgumentException();
        }
        if (mRoot == null) {
            throw new NullPointerException();
        } return mRoot.contains(txHash);
    }

    public Transaction get(String txHash) {
        if (txHash == null || txHash.length() == 0) {
            throw new IllegalArgumentException();
        }
        if (mRoot == null) {
            throw new NullPointerException();
        } return mRoot.get(txHash);
    }

    public static MerkleTree load(
            Collection<Transaction> txList) {
        
    }

    public void save() {
        // todo: implement writing the tree to file
    }

    private class MerkleNode {
        
        // fields

        private Transaction mTransaction;
        private MerkleNode mLeft;
        private MerkleNode mRight;

        // constructors

        public MerkleNode(Transaction tx) {
            if (tx == null) {
                throw new NullPointerException();
            }
            mTransaction = tx;
        }

        // methods

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
            // check if there is a left child and no right
            else if (mLeft != null && mRight == null) {
                mRight = new MerkleNode(mTransaction);
                mTransaction = null;
                mRight.add(tx);
            }
            // check if there is a right child and no left
            else if (mLeft == null && mRight != null) {
                mLeft = new MerkleNode(mTransaction);
                mTransaction = null;
                mLeft.add(tx);
            }
            // otherwise there are two children, add based on 
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
         * Determines if this subtree contains a specified 
         *   transaction or not based on the transaction hash.
         * @param txHash A SHA-256 double hash hexstring.
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

    } // end MerkleNode

} // end MerkleTree
