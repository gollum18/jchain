package jchain.net;

/**
 * Represents an independent node in the blockchain network. 
 * Nodes can asynchronously join or leave the network as they wish. 
 * Upon entering the network, the node is required to 'sync', that is 
 * bring their local chain up to date with the longest chain on the network.
 *
 * Nodes are also responsible for listening for transactions, building 
 * blocks, broadcasting blocks and transactions, and validating blocks.
 * Similar to Bitcoin, new nodes can only join the network by going through
 * a node that is already on the network. That said each node is limited 
 * in the number of nodes it can link to and exchange messages with.
 */
public class Node {
    
}
