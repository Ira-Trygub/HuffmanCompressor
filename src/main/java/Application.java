import java.io.File;
import java.io.IOException;
import java.util.*;

public class Application {
    public static void main(String[] args) throws IOException {
        ByteReader reader = new ByteReader();
        String filename = "D:\\HAW-Hamburg\\AD\\Praktikum04\\src\\main\\resources\\textdoc.txt";
        byte[] characters = reader.channelRead(filename);
        Huffman huffman = new Huffman();
        HashMap<Byte, Integer> frequencyTable = huffman.calculateCharacterFrequencies(characters);
        huffman.createHeap(frequencyTable);
        BinaryTree binaryTree = huffman.buildHuffmanTree();
        var outputCode = huffman.calculateCodeFromHuffmanTree(binaryTree, characters);
//        ByteWritter byteWritter = new ByteWritter();
//        byteWritter.writeByteArrayToFile(new File("D:\\HAW-Hamburg\\AD\\Praktikum04\\src\\main\\resources\\code.txt"), outputCode );

        System.out.println(outputCode);
        var res = huffman.decode(binaryTree, outputCode);
        System.out.println(res);
        System.out.println(characters);
    }
}
