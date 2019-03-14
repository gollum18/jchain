import java.util.LinkedList;

public class TestBC {

    public static void main(String[] args) {
        String[] inputs = new String[]{"A:50"};
        String[] outputs = new String[]{"B:20", "C:25"};
        Transaction tx = new Transaction(inputs, outputs);
        Transaction tx2 = new Transaction(inputs, outputs);
        Transaction tx3 = new Transaction(inputs, outputs);
        Transaction tx4 = new Transaction(inputs, outputs);
        LinkedList<Transaction> txs = new LinkedList<>();
        txs.add(tx);
        txs.add(tx2);
        txs.add(tx3);
        txs.add(tx4);
        Block b1 = new Block(txs, BCUtil.getInstance().doubleHash("This is the genesis block!! There is no prior block. So this hash is just generated from this string."));
        System.out.println(b1.getHash());
    }

}
