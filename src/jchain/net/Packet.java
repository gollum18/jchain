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

    /**
     * Packets can be one of various types that indicate the actions 
     * nodes must take when receiving the packet. There are various 
     * types of packets; they wil be detailed when I finalize the 
     * packet design.
     */
    public enum Types {

    }

}
