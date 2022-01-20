package demo;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;

public class Application {
    public static void main(String[] args) throws IOException {
        Huffman huffman = new Huffman();
        ByteReader reader = new ByteReaderImpl("app/src/main/resources/readme.md");
//        ByteReader reader = new ByteReaderImpl("app/src/main/resources/textdoc.txt");
//        ByteReader reader = new ByteReaderImpl("app/src/main/resources/figuren.json");
        HashMap<Byte, Integer> frequencyTable = huffman.calculateCharacterFrequencies(reader);
        var heap = Huffman.createHeap(frequencyTable);

        var map = new HashMap<Byte, BinaryTree<HuffNode>>();
        BinaryTree<HuffNode> binaryTree = Huffman.buildHuffmanTree(map, heap);

        Path arch = Files.createTempFile("arch", ".huf");
        System.out.println(arch.getFileName());
        Huffman.calculateCodeFromHuffmanTree(map, reader, arch);

        Huffman.decode(binaryTree, arch);
    }
}
