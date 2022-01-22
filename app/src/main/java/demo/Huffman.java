package demo;

import com.github.jinahya.bit.io.*;

import java.io.*;
import java.nio.channels.FileChannel;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.PriorityQueue;

public class Huffman {
    public static long calculateCharacterFrequencies(ByteReader reader, HashMap<Byte, Integer> resTable) throws IOException {
        long[] counter = new long[1]; // java sucks, scala rulez
        reader.readFile(i -> {
            byte c = (byte) i;
            resTable.compute( // put or update k, v in hashmap
                    c,
                    (k, v) -> (v == null) ? 1 : v + 1
            );
            counter[0]++;
        });
        return counter[0];
    }

    public static PriorityQueue<BinaryTree<Node>> createHeap(HashMap<Byte, Integer> frequencyTable, HashMap<Byte, BinaryTree<Node>> resMap) {
        var res = new PriorityQueue<BinaryTree<Node>>();
        frequencyTable.forEach((k, v) -> {
            BinaryTree<Node> leaf = new BinaryTree<>(new Node(k, v));
            res.add(leaf);
            resMap.put(k, leaf);
        });
        return res;
    }

    public static BinaryTree<Node> buildHuffmanTree(PriorityQueue<BinaryTree<Node>> heap) {
        while (true) {
            var min1 = heap.poll();
            var min2 = heap.poll();
            if (min2 != null) {
                var freq = min1.getNode().getFrequency() + min2.getNode().getFrequency();
                var sum = new BinaryTree<>(new Node(-1, freq), min1, min2);
                min1.setParent(sum);
                min2.setParent(sum);
                heap.add(sum);
            } else {
                return min1;
            }
        }
    }

    public static void calculateCodeFromHuffmanTree(long fileLength, BinaryTree<Node> tree, HashMap<Byte, BinaryTree<Node>> nodes, ByteReader in, Path outPath) throws IOException {
        var out = openBitFileForWrite(outPath);
        serializeTree(tree, out);
        writeCompressedBitStream(fileLength, in, nodes, out);
    }

    private static void writeCompressedBitStream(long fileLength, ByteReader in, HashMap<Byte, BinaryTree<Node>> map, BitOutput bitOut) throws IOException {
        bitOut.writeLong64(fileLength);

        in.readFile(c -> {
            try {
                calculateCodeR(map, (byte) c, bitOut);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });

        bitOut.align(1);
        bitOut.flush();
    }

    private static BitOutput openBitFileForWrite(Path outPath) {
        var byteOutput = BufferByteOutput.adapting(() -> {
            try {
                return FileChannel.open(outPath, StandardOpenOption.WRITE);
            } catch (IOException er) {
                throw new RuntimeException("failed to open " + outPath, er);
            }
        });
        var bitOutput = BitOutputAdapter.from(byteOutput);
        return bitOutput;
    }

    private static void serializeTree(BinaryTree<Node> tree, BitOutput out) throws IOException {
        try (var bos = new ByteArrayOutputStream()) {
            try (var oos = new ObjectOutputStream(new BufferedOutputStream(bos))) {
                oos.writeObject(tree);
                oos.flush();
            }
            var bytes = bos.toByteArray();

            out.writeInt32(bytes.length);

            for (byte c : bytes) {
                out.writeByte8(c);
            }
        }
    }

    public static void calculateCodeR(HashMap<Byte, BinaryTree<Node>> map, byte c, BitOutput bitOut) throws IOException {
        var acc = new ArrayList<Boolean>();
        var node = map.get(c);
        while (!node.isRoot()) {
            acc.add(node.isBit());
            node = node.getParent();
        }
        Collections.reverse(acc);
        acc.forEach(b -> {
            try {
                bitOut.writeBoolean(b); // write code to file
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }

    public static void decode(Path path) throws IOException, ClassNotFoundException {
        try (var decoded = new FileOutputStream("readme.md")) {
            // try (var decoded = new FileOutputStream("figuren2.json")) {
            var byteInput = BufferByteInput.adapting(() -> {
                try {
                    return FileChannel.open(path, StandardOpenOption.READ);
                } catch (IOException er) {
                    throw new RuntimeException("failed to open " + path, er);
                }
            });
            var biteIn = BitInputAdapter.from(byteInput);
            var treeLength = biteIn.readInt32();
            byte[] ar = new byte[(int) treeLength];
            for (int i = 0; i < treeLength; i++) {
                ar[i] = biteIn.readByte8();
            }
            var bis = new ByteArrayInputStream(ar);
            var in = new ObjectInputStream(bis);
            var root = (BinaryTree<Node>) in.readObject();

            var c = biteIn.readLong64();
            var node = root;
            while (c > 0) {
                try {
                    var bit = biteIn.readBoolean();
                    node = node.nextNode(bit);
                    if (node.isLeaf()) {
                        decoded.write(node.getNode().getCharacter());
                        c--;
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
