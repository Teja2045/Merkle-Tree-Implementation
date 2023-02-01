package merkle

import (
	"bytes"
	"crypto/sha256"
	"fmt"
)

var leaves = []*Leaf{}
var merkleroot *Node

// Leaf represents a leaf node in the Merkle Tree
type Leaf struct {
	Data []byte
	Hash []byte
}

// Node represents an inner node in the Merkle Tree
type Node struct {
	Left  *Node
	Right *Node
	Hash  []byte
}

// NewLeaf creates a new leaf node with the given data
func NewLeaf(data []byte) *Leaf {
	hash32 := sha256.Sum256(data)
	hash := []byte{}
	hash = append(hash, hash32[:]...)
	return &Leaf{
		Data: data,
		Hash: hash[:],
	}
}

// create a node from Leaf Node
func NewNodeFromLeaf(leaf *Leaf) *Node {
	return &Node{
		Hash: leaf.Hash,
	}
}

// creates a new node with left and right nodes: right might be nil sometimes
func NewNode(left, right *Node) *Node {
	var hash []byte
	if right == nil {
		hash32 := sha256.Sum256(append(left.Hash, left.Hash...))
		hash = append(hash, hash32[:]...)
	} else {
		hash32 := sha256.Sum256(append(left.Hash, right.Hash...))
		hash = append(hash, hash32[:]...)
	}
	return &Node{
		Left:  left,
		Right: right,
		Hash:  hash[:],
	}
}

// creates merkle tree
// Algorithm :
// first leaves is created
// then leaves to nodes
// using nodes we build the merkle tree by decomposing level by level
func BuildMerkleTree() {
	var nodes []*Node
	for i := 0; i < len(leaves); i += 2 {
		var leftnode, rightnode *Node
		leftnode = NewNodeFromLeaf(leaves[i])
		if i+1 < len(leaves) {
			rightnode = NewNodeFromLeaf(leaves[i+1])
		}
		nodes = append(nodes, NewNode(leftnode, rightnode))
	}

	for len(nodes) > 1 {
		var tempnodes []*Node
		for i := 0; i < len(nodes); i += 2 {
			var leftnode, rightnode *Node
			leftnode = nodes[i]
			if i+1 < len(nodes) {
				rightnode = nodes[i+1]
			}
			node := NewNode(leftnode, rightnode)
			tempnodes = append(tempnodes, node)
		}
		nodes = tempnodes
		//fmt.Println(len(nodes))
	}

	//fmt.Println("the merkle tree has been built...")
	if len(nodes) > 0 {
		merkleroot = nodes[0]
	} else {
		merkleroot = nil
	}
}

// add node and rebuild the merkle tree
func Add(data string) {
	leaf := NewLeaf([]byte(data))
	leaves = append(leaves, leaf)
	fmt.Println(data, " is added")
	BuildMerkleTree()
}

// delete node and rebuild the merkle tree
func Delete(data string) {
	bytedata := []byte(data)
	for i := 0; i < len(leaves); i++ {
		leafdata := leaves[i].Data
		if bytes.Equal(leafdata, bytedata) {
			leaves = append(leaves[:i], leaves[i+1:]...)
			BuildMerkleTree()
			fmt.Println(data, " is deleted...")
			break
		}
		fmt.Println("can't delete ", data, ", as it is not present...")
	}
}

// prepares for actual check
func Check(data string) bool {
	var hash []byte
	bytedata := []byte(data)
	hash32 := sha256.Sum256(bytedata)
	hash = append(hash, hash32[:]...)
	return CheckNode(merkleroot, hash)
}

// CheckNode returns true if the target node is present in the Merkle Tree
// Alogirthm : DFS
func CheckNode(root *Node, target []byte) bool {
	if root == nil {
		return false
	}

	//fmt.Println(string())
	if bytes.Equal(root.Hash, target) {
		return true
	}
	var left, right bool
	if root.Left != nil {
		left = CheckNode(root.Left, target)
	}
	if root.Right != nil {
		right = CheckNode(root.Right, target)
	}
	return left || right
}

// getter for leaves
func Leaves() []*Leaf {
	return leaves
}

// getter for merkleroot
func MerkleRoot() *Node {
	return merkleroot
}
