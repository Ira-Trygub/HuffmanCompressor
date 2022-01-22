package demo;

import java.io.Serializable;

public class Node implements Comparable<Node>, Serializable {
    private final int character;
    private final long frequency;

    public Node(int character, long frequency) {
        this.character = character;
        this.frequency = frequency;
    }

    public int getCharacter() {
        return character;
    }

    public long getFrequency() {
        return frequency;
    }

    @Override
    public int compareTo(Node o) {
        return Long.compare(getFrequency(), o.getFrequency());
    }

    public String toStrong() {
        return "Node(" + this.character + " ," + this.frequency + ")";
    }
}
