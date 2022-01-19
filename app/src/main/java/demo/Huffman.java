package demo;

import com.github.jinahya.bit.io.*;

import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.PriorityQueue;

public class Huffman {
    static HashMap<Byte, Integer> calculateCharacterFrequencies(ByteReader reader) throws IOException {
        var frequencyTable = new HashMap<Byte, Integer>();
        reader.readFile(i -> {
            byte c = (byte) i;
            frequencyTable.compute(
                    c,
                    (k, v) -> (v == null) ? 1 : v + 1
            );
        });
        return frequencyTable;
    }


    static PriorityQueue<HuffNode> createHeap(HashMap<Byte, Integer> frequencyTable) {
        var res = new PriorityQueue<HuffNode>();
        frequencyTable.forEach((k, v) ->
                res.add(new HuffNode(k, v))
        );
        return res;
    }

    static public BinaryTree<HuffNode> buildHuffmanTree(HashMap<Byte, BinaryTree<HuffNode>> map, PriorityQueue<HuffNode> heap) {
        var root = new BinaryTree<HuffNode>(heap.poll());
        map.put((byte) root.getNode().getCharacter(), root);

        while (!heap.isEmpty()) {
            var leaf = new BinaryTree<HuffNode>(heap.poll());
            map.put((byte) leaf.getNode().getCharacter(), leaf);

            var freq = root.getNode().getFrequency() + leaf.getNode().getFrequency();
            BinaryTree<HuffNode> sum;
            if (leaf.getNode().getFrequency() < freq) {
                sum = new BinaryTree<HuffNode>(new HuffNode(-1, freq), leaf, root);
                leaf.setBit(false);
                root.setBit(true);
            } else {
                sum = new BinaryTree<HuffNode>(new HuffNode(-1, freq), root, leaf);
                leaf.setBit(true);
                root.setBit(false);
            }
            leaf.setParent(sum);
            root.setParent(sum);
            root = sum;
        }

        return root;
    }

    public static void calculateCodeFromHuffmanTree(HashMap<Byte, BinaryTree<HuffNode>> map, ByteReader reader, Path path) throws IOException {
        var byteOutput = BufferByteOutput.adapting(() -> {
            try {
                return FileChannel.open(path, StandardOpenOption.WRITE);
            } catch (IOException er) {
                throw new RuntimeException("failed to open " + path, er);
            }
        });

        var bitOut = BitOutputAdapter.from(byteOutput);

        reader.readFile(c -> {
            try {
                calculateCodeR(map, (byte) c, bitOut);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
        bitOut.flush();
    }

    public static void calculateCodeR(HashMap<Byte, BinaryTree<HuffNode>> map, byte c, BitOutput bitOut) throws IOException {
        var acc = new ArrayList<Boolean>();
        var node = map.get(c);
        while (!node.isRoot()) {
            acc.add(node.isBit());
            node = node.getParent();
        }
        Collections.reverse(acc);
        acc.forEach(b -> {
            try {
                bitOut.writeBoolean(b);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }

    public static void decode(BinaryTree<HuffNode> root, Path path) throws IOException {
        try (var decoded = new FileOutputStream("textdoc.txt")) {
            var byteInput = BufferByteInput.adapting(() -> {
                try {
                    return FileChannel.open(path, StandardOpenOption.READ);
                } catch (IOException er) {
                    throw new RuntimeException("failed to open " + path, er);
                }
            });
            var biteIn = BitInputAdapter.from(byteInput);
            var node = root;
            while (true) {
                try {
                    var bit = biteIn.readBoolean();
                    node = node.nextNode(bit);
                    if (node.isLeaf()) {
                        decoded.write(node.getNode().getCharacter());
                        node = root;
                    }
                } catch (IOException er) {
                    decoded.flush();
                    return;
                }
            }
        }
    }
}
