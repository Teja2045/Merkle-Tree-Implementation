package merkleTree;

import java.util.ArrayList;
import java.util.List;

import utils.DigestUtils;
import utils.Utils;

public class MerkleNode {
    public String hash;
    MerkleNode left;
    MerkleNode right;

    MerkleNode(String hash) {
        this.hash = hash;
    }

    MerkleNode(MerkleNode left, MerkleNode right, DigestUtils digestUtils) {
        
        if (Utils.compare(left.hash, right.hash))
            this.hash = digestUtils.getHash(left.hash + right.hash);
        else
            this.hash = digestUtils.getHash(right.hash + left.hash);

        this.left = left;
        this.right = right;
    }

    static List<MerkleNode> getLeafNodes(List<String> transactions, DigestUtils digestUtils) {
        List<String> hashes = digestUtils.getHashList(transactions);
        List<MerkleNode> nodes = new ArrayList<>();
        for (String hash : hashes)
            nodes.add(new MerkleNode(hash));
        return nodes;
    }
}