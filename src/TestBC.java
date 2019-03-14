public class TestBC {

    public static void main(String[] args) {
        String[] inputs = new String[]{"A:50"};
        String[] outputs = new String[]{"B:20", "C:25"};
        Transaction tx = new Transaction(inputs, outputs);
        Transaction tx2 = new Transaction(inputs, outputs);
        Transaction tx3 = new Transaction(inputs, outputs);
        Transaction tx4 = new Transaction(inputs, outputs);
        MerkleTree mt = new MerkleTree();
        mt.add(tx);
        mt.add(tx2);
        mt.add(tx3);
        mt.add(tx4);
        System.out.println(mt.computeHash());
    }

}
