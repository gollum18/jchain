package jchain.net;

/**
 * Represents a packet implementing the jchain protocol. The protocol is 
 * responsible for carrying information between nodes on the network. For 
 * speed and simplicity of design, jchain implements a UDP based P2P 
 * network architecture. Nodes need not always hear messages broadcast 
 * from partner nodes. Nodes should automatically bring themselves up
 * to date (arising from missing blocks or transactions) within normal
 * operation of the network.
 *
 * Details of the jchain packet structure are still being worked out.
 */
public class Packet {

    public static final int VERSION_NUMBER = 1;

    /**
     * Packets can be one of various types that indicate the actions 
     * nodes must take when receiving the packet. There are various 
     * types of packets; they wil be detailed when I finalize the 
     * packet design.
     *
     * There are currently four different types of Packets in jchain:
     *  BLOCK Packets: These packets contain a block.
     *  BLOCK_HEADER: These packets contain a block header.
     *  TX Packets: These packets contain a transaction.
     *  HEARTBEAT: These packets are sent by Nodes to inform their 
     *      neighbors that they are still up (a concept I stole from GFS/HDFS)
     *
     * A Node is considered `dead` by a neighbor if the neighboring node
     * does not receive a HEARTBEAT packet from a node within a certain 
     * period of time. A `dead` node is removed from the neighbors 
     * list of peers and will no longer receive broadcasts from that 
     * neighbor.
     */
    public enum Types {
        BLOCK,
        BLOCK_HEADER,
        TX,
        HEARTBEAT
    }

}
