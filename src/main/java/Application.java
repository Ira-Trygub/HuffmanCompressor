import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.PriorityQueue;

public class Application {
    public static void main(String[] args) throws IOException {
        ByteReader reader = new ByteReader();
        String filename = "D:\\HAW-Hamburg\\AD\\Praktikum04\\src\\main\\resources\\textdoc.txt";
        byte[] characters = reader.channelRead(filename);
Huffman huffman = new Huffman();
     huffman.calculateCharacterFrequencies(characters);
     huffman.createHeap();
//        System.out.println(huffman.heap);
        huffman.buildHuffmanTree();

    }
}
