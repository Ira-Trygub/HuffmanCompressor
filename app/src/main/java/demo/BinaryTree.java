package demo;

import java.io.Serializable;

public class BinaryTree<A extends Serializable & Comparable<A>> implements Serializable, Comparable<BinaryTree<A>> {
    private final A node;
    private boolean bit;

    private transient BinaryTree<A> parent;
    private BinaryTree<A> min1;
    private BinaryTree<A> min2;

    public BinaryTree(A node) {
        this.node = node;
    }

    public BinaryTree(A node, BinaryTree<A> min1, BinaryTree<A> min2) {
        this.node = node;
        this.min1 = min1;
        this.min2 = min2;
        min1.setBit(false);
        min2.setBit(true);
    }

    public A getNode() {
        return node;
    }

    public BinaryTree<A> getParent() {
        return parent;
    }

    public boolean isBit() {
        return bit;
    }

    public void setParent(BinaryTree<A> parent) {
        this.parent = parent;
    }

    public void setBit(boolean bit) {
        this.bit = bit;
    }

    public boolean isRoot() {
        return parent == null;
    }

    public boolean isLeaf() {
        return min1 == null;
    }

    public BinaryTree<A> nextNode(boolean bit) {
        return bit ? min2 : min1;
    }

    @Override
    public int compareTo(BinaryTree<A> o) {
        return node.compareTo(o.node);
    }
}
