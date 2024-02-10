import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Stack;

import merkleTree.MerkleTree;
import utils.DigestUtils;

public class Test {
    public static void main(String[] args) throws Exception {
        String[] txs = new String[] { "tx1", "tx2", "tx3" };
        List<String> txList = new ArrayList<>(Arrays.asList(txs));

        DigestUtils digestUtils = new DigestUtils("SHA3-256");
        MerkleTree mTree = new MerkleTree(txList, digestUtils);

        mTree.printTree();

        String tx = "tx4";

        Stack<String> proofTrace = new Stack<>();
        mTree.createInclusionProof(proofTrace, tx);

        System.out.println(mTree.validateInclusionProof(proofTrace, tx, mTree.root.hash));

    }
}