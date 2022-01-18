import java.util.*;

public class Huffman {
    static int charsCount;
    PriorityQueue<HuffNode> heap;

//    BinaryTree binaryTree;


    HashMap<Byte, Integer> calculateCharacterFrequencies(byte[] arrByte) {
        HashMap<Byte, Integer> frequencyTable = new HashMap<>();
        charsCount = arrByte.length;
        for (int i = 0; i < arrByte.length; i++) {
            if (frequencyTable.keySet().contains(arrByte[i])) {
                Integer count = frequencyTable.get(arrByte[i]);
                frequencyTable.put(arrByte[i], count + 1);
            } else {
                frequencyTable.put(arrByte[i], 1);
            }
        }
        return frequencyTable;
    }


    void createHeap(HashMap<Byte, Integer> frequencyTable) {
        ArrayList<HuffNode> nodesList = new ArrayList<>();
        for (Map.Entry<Byte, Integer> kv : frequencyTable.entrySet()) {
            nodesList.add(new HuffNode(kv.getKey(), kv.getValue()));
            heap = new PriorityQueue<HuffNode>(nodesList);
        }
    }

    public BinaryTree buildHuffmanTree() {
        BinaryTree binaryTree;
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
            if (min1Tree.headHuffNode.frequency < min2Tree.headHuffNode.frequency) {
                tempBinaryTree.createLeftNode(min1);
                tempBinaryTree.createRightNode(min2);
            } else {
                tempBinaryTree.createLeftNode(min2);
                tempBinaryTree.createRightNode(min1);
            }

            min1Tree = tempBinaryTree;
            min1 = (HuffNode) tempBinaryTree.headHuffNode;
            min2Tree = new BinaryTree<>(heap.poll());
            System.out.println("min1Tree " + min1Tree.headHuffNode.frequency);
//            System.out.println("min2Tree " + min2Tree.headHuffNode.frequency);


        }
        binaryTree = min1Tree;

        binaryTree.setMarks();
        return binaryTree;
    }

    public Byte[] calculateCodeFromHuffmanTree(BinaryTree binaryTree, byte[] arrByte) {
        Byte[] res = new Byte[arrByte.length];
        for (int i = 0; i < arrByte.length; i++) {
//            Byte co;
//
//            HuffNode left = (HuffNode) binaryTree.getLeftNode().getHuffNode();
//            HuffNode right = (HuffNode) binaryTree.getRightNode().getHuffNode();
             res = calculateCodeR(binaryTree, arrByte[i]);
        }
        return res;
    }

    public Byte[] calculateCodeR(BinaryTree visitedTree, Byte by) {
        ArrayList<Byte> outputArrayList = new ArrayList<>();
        Byte co;
        if (!visitedTree.isLeaf()) {
            HuffNode left = (HuffNode) visitedTree.getLeftNode().getHuffNode();
            HuffNode right = (HuffNode) visitedTree.getRightNode().getHuffNode();
            if (left.getCharacter() == by) {
                co = 0;
                outputArrayList.add(co);
            } else if (right.getCharacter() == by) {
                co = 1;
                outputArrayList.add(co);
            } else if (left.getCharacter() < 0) {
                co = 0;
                visitedTree = visitedTree.getLeftNode();
                outputArrayList.add(co);
            } else if (right.getCharacter() < 0) {
                co = 1;
                visitedTree = visitedTree.getRightNode();
                outputArrayList.add(co);
            }
        }
        return outputArrayList.toArray(new Byte[outputArrayList.size()]);
    }

    public Byte[] decode(BinaryTree<HuffNode> binaryTree, Byte[] arrByte) {
        int element;
        ArrayList<Integer> decodeArrayList = new ArrayList<>();
        for (int i = 0; i < arrByte.length; i++) {
            while (binaryTree.isLeaf()) {
                if (binaryTree.getLeftNode().leftMark == arrByte[i]) {
                    element = binaryTree.getLeftNode().getHuffNode().getCharacter();
                    decodeArrayList.add(element);
                    binaryTree = binaryTree.getLeftNode();
                } else if (binaryTree.getRightNode().rightMark == arrByte[i]) {
                    element = binaryTree.getRightNode().getHuffNode().getCharacter();
                    decodeArrayList.add(element);
                    binaryTree = binaryTree.getRightNode();
                }
            }
        }
        return decodeArrayList.toArray(new Byte[decodeArrayList.size()]);
    }
}
