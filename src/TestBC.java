import java.util.LinkedList;

public class TestBC {

    public static void main(String[] args) {
        Transaction tx = new Transaction(
                new String[]{"A:30"},
                new String[]{"B:30"});
        Transaction tx2 = new Transaction(
                new String[]{"A:25", "B:25"},
                new String[]{"C:25", "D:15", "E:10"});
        Transaction tx3 = new Transaction(
                new String[]{"A:10", "B:15", "C:50"},
                new String[]{"D:75"});
        Transaction tx4 = new Transaction(
                new String[]{"A:45"},
                new String[]{"B:20", "C:25"});
        LinkedList<Transaction> txs = new LinkedList<>();
        txs.add(tx);
        txs.add(tx2);
        txs.add(tx3);
        txs.add(tx4);
        Block b1 = new Block(txs, BCUtil.getInstance().doubleHash("This is the genesis block!! There is no prior block. So this hash is just generated from this string."));
        BC chain = new BC(b1);
        txs.remove(tx3);
        txs.remove(tx4);
        Block b2 = new Block(txs, chain.getLeadBlock().getHash());
        chain.addBlock(b2);
        System.out.println(chain.toString());
    }

}
