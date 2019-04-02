package jchain.net;

import java.net.InetAddress;
import java.net.ServerSocket;

import jchain.net.TXPool;

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
 *
 * JChain supports both IPV4 and IPV6 addresses.
 */
public class Node {

    public static final int MAX_PEERS = 50;
    public static final int SERVER_PORT = 61308;

    private class Peer {
        
        // stores the IPV4/IPV6 address of the peer
        private InetAddress mPeerAddress;
        // stores when the last heartbeat message was received for the peer
        private long nLastHeartbeat;
        // stores the status of the peer, if true, then this peer won't
        //  receive messages from the node
        private boolean bDead;

        public Peer(InetAddress addr) {
            nLastHeartbeat = System.currentTimeMillis();
        }

        public InetAddress getAddress() {
            return mPeerAddress;
        }

        public long getLastHeartbeat() {
            return mLastHeartbeat;
        }

        public void registerHeartbeat() {
            mLastHeartbeat = System.currentTimeMillis();
        }

    }

    private class ServerThread extends Thread {

        // stores addresses of peer nodes
        private static final Peer[] = new Peer[MAX_PEERS];
        // stores when the node came online
        private final long nOnTime = System.currentTimeMillis();
        // stores the amount of peers the node currently has
        private int nPeers = 0;

        /**
         * Starts the server listening on port 61308.
         */
        public void run() {
            
        }

    }

}
