package main

import (
	"encoding/hex"
	"fmt"
	merkle "merkletree/merkleTree"
)

func main() {
	merkle.Add("hello")
	merkle.Add("world")
	merkle.Add("vitwit")
	fmt.Println(merkle.Leaves())
	fmt.Println(merkle.Check("vitwit"))
	merkle.Delete("vitwit")
	fmt.Println(merkle.Check("vitwit"))
	merkleroot := hex.EncodeToString(merkle.MerkleRoot().Hash)
	fmt.Println(merkleroot)
}
