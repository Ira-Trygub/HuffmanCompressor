package demo;

import com.github.jinahya.bit.io.*;

import java.io.*;
import java.nio.channels.FileChannel;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.*;
import java.util.stream.Stream;

public class Huffman {
    private long counter = 0;

    public HashMap<Byte, Integer> calculateCharacterFrequencies(ByteReader reader) throws IOException {
        var frequencyTable = new HashMap<Byte, Integer>();
        reader.readFile(i -> {
            byte c = (byte) i;
            frequencyTable.compute( // put or ubdate k, v in hashmap
                    c,
                    (k, v) -> (v == null) ? 1 : v + 1
            );
            counter++;
        });
        return frequencyTable;
    }


    public PriorityQueue<HuffNode> createHeap(HashMap<Byte, Integer> frequencyTable) {
        var res = new PriorityQueue<HuffNode>();
        frequencyTable.forEach((k, v) ->
                res.add(new HuffNode(k, v))
        );
        return res;
    }

    public BinaryTree<HuffNode> buildHuffmanTree(HashMap<Byte, BinaryTree<HuffNode>> map, PriorityQueue<HuffNode> heap) {
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

    public void calculateCodeFromHuffmanTree(BinaryTree<HuffNode> binaryTree, HashMap<Byte, BinaryTree<HuffNode>> map, ByteReader reader, Path path) throws IOException {
        var byteOutput = BufferByteOutput.adapting(() -> {
            try {
                return FileChannel.open(path, StandardOpenOption.WRITE);
            } catch (IOException er) {
                throw new RuntimeException("failed to open " + path, er);
            }
        });

        var bitOut = BitOutputAdapter.from(byteOutput);

        try (var bos = new ByteArrayOutputStream()) {
            try (var oos = new ObjectOutputStream(new BufferedOutputStream(bos))) {
                oos.writeObject(binaryTree);
                oos.flush();
            }
            var bytes = bos.toByteArray();
            bitOut.writeInt32(bytes.length);
            for (byte c : bytes) {
                bitOut.writeByte8(c);
            }
        }

        bitOut.writeLong64(counter);
        reader.readFile(c -> {
            try {
                calculateCodeR(map, (byte) c, bitOut);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
        bitOut.align(1);
        bitOut.flush();
    }

    public void calculateCodeR(HashMap<Byte, BinaryTree<HuffNode>> map, byte c, BitOutput bitOut) throws IOException {
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

    public void decode(Path path) throws IOException, ClassNotFoundException {
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
            var root = (BinaryTree<HuffNode>) in.readObject();

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
