package demo;

import java.io.IOException;
import java.nio.file.Files;
import java.util.HashMap;

public class Application {
    public static void main(String[] args) throws IOException, ClassNotFoundException {
        var reader = new ByteReaderImpl("app/src/main/resources/readme.md");
        // ByteReader reader = new ByteReaderImpl("app/src/main/resources/figuren.json");
        var frequencyTable = new HashMap<Byte, Integer>();
        var fileLength = Huffman.calculateCharacterFrequencies(reader, frequencyTable);
        var map = new HashMap<Byte, BinaryTree<Node>>();
        var heap = Huffman.createHeap(frequencyTable, map);

        var tree = Huffman.buildHuffmanTree(heap);

        var arch = Files.createTempFile("arch", ".huf");
        System.out.println(arch.getFileName());
        Huffman.calculateCodeFromHuffmanTree(fileLength, tree, map, reader, arch);

        Huffman.decode(arch);
    }
}
