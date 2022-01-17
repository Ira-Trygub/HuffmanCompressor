import java.util.*;

public class Huffman {
    static int charsCount;
    PriorityQueue<HuffNode> heap;
    HashMap<Byte, Integer> frequencyTable = new HashMap<>();
    BinaryTree binaryTree;

    void calculateCharacterFrequencies(byte[] arrByte) {
        charsCount = arrByte.length;
        for (int i = 0; i < arrByte.length; i++) {
            if (frequencyTable.keySet().contains(arrByte[i])) {
                Integer count = frequencyTable.get(arrByte[i]);
                frequencyTable.put(arrByte[i], count + 1);
            } else {
                frequencyTable.put(arrByte[i], 1);
            }
        }
    }


    void createHeap() {
        ArrayList<HuffNode> nodesList = new ArrayList<>();
        for (Map.Entry<Byte, Integer> kv : frequencyTable.entrySet()) {
            nodesList.add(new HuffNode(kv.getKey(), kv.getValue()));
            heap = new PriorityQueue<HuffNode>(nodesList);
        }
    }

    BinaryTree buildHuffmanTree() {
        int sumFreq = 0;
        HuffNode min1 = heap.poll();
        BinaryTree<HuffNode> min1Tree = new BinaryTree<>(min1);
        while (!heap.isEmpty()) {
            HuffNode min2 = heap.poll();
            BinaryTree<HuffNode> min2Tree = new BinaryTree<>(min2);


            System.out.println("min1Tree " + min1Tree.headHuffNode.frequency);
            System.out.println("min2Tree " + min2Tree.headHuffNode.frequency);
            sumFreq = (min1Tree.headHuffNode.frequency + min2Tree.headHuffNode.frequency);
            HuffNode tempHead = new HuffNode(-1, sumFreq);
//       heap.add(tempHead);
            BinaryTree tempBinaryTree = new BinaryTree(tempHead);
            if (min1Tree.headHuffNode.frequency < min2Tree.headHuffNode.frequency){
                tempBinaryTree.createLeftNode(min1);
                tempBinaryTree.createRightNode(min2);
            }else {
                tempBinaryTree.createLeftNode(min2);
                tempBinaryTree.createRightNode(min1);
            }

            min1Tree = tempBinaryTree;
            min1 = (HuffNode) tempBinaryTree.headHuffNode;
            min2Tree = new BinaryTree<>(heap.poll());
            System.out.println("min1Tree " + min1Tree.headHuffNode.frequency);
//            System.out.println("min2Tree " + min2Tree.headHuffNode.frequency);

        }
        return binaryTree;
    }
}
