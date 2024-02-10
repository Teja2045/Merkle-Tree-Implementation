package merkleTree;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Stack;

import utils.DigestUtils;
import utils.Utils;

public class MerkleTree {
    public List<String> transactions;
    public MerkleNode root;
    DigestUtils digestUtils;

    public MerkleTree(List<String> transactions, DigestUtils digestUtils) {
        this.transactions = transactions;
        this.digestUtils = digestUtils;
        System.out.println("good1");
        transactions = Utils.extendTillPowerOf2(transactions, "");
        System.out.println("good2");
        buildMerkleTree(transactions);
    }

    void buildMerkleTree(List<String> transactions) {

        List<MerkleNode> nodes = MerkleNode.getLeafNodes(transactions, digestUtils);

        while (nodes.size() > 1) {
            List<MerkleNode> newNodes = new ArrayList<>();
            for (int i = 0; i < nodes.size(); i += 2) {
                newNodes.add(new MerkleNode(nodes.get(i), nodes.get(i + 1), digestUtils));
            }
            nodes = newNodes;
        }

        this.root = nodes.get(0);
    }

    public void createInclusionProof(Stack<String> trace, String txData) throws Exception {
        String txDataHash = digestUtils.getHash(txData);
        if (createProof(root, trace, txDataHash))
            return;
        throw new Exception("Transaction Not Found, can't create proof");
    }

    private boolean createProof(MerkleNode node, Stack<String> trace, String targetLeafHash) {
        if (node.hash.equals(targetLeafHash))
            return true;

        if (node.left == null)
            return false;

        trace.push(node.right.hash);
        if (createProof(node.left, trace, targetLeafHash))
            return true;
        trace.pop();

        trace.push(node.left.hash);
        if (createProof(node.right, trace, targetLeafHash))
            return true;
        trace.pop();

        return false;
    }

    public boolean validateInclusionProof(Stack<String> trace, String transactionData, String rootHash) {

        String currentHash = digestUtils.getHash(transactionData);

        while (!trace.isEmpty()) {
            String siblingHash = trace.pop();
            if (Utils.compare(currentHash, siblingHash))
                currentHash = digestUtils.getHash(currentHash + siblingHash);
            else
                currentHash = digestUtils.getHash(siblingHash + currentHash);

        }

        System.out.println("calculated Hash: " + currentHash + " & " + "root hash: " + rootHash);
        return currentHash.equals(rootHash);
    }

    public void printTree() {
        Queue<MerkleNode> q = new LinkedList<>();
        q.add(root);

        while (q.size() > 0) {
            int levelSize = q.size();

            for (int i = 0; i < levelSize; i++) {
                MerkleNode node = q.remove();
                System.out.print(node.hash + " ");
                if (node.left != null)
                    q.add(node.left);
                if (node.right != null)
                    q.add(node.right);
            }

            System.out.println();
        }
    }

}
