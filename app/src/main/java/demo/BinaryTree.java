package demo;

public class BinaryTree<A> {
    private final A node;
    private boolean bit;

    private BinaryTree<A> parent;
    private BinaryTree<A> left;
    private BinaryTree<A> right;

    public BinaryTree(A node) {
        this.node = node;
        this.bit = true;
    }

    public BinaryTree(A node, BinaryTree<A> left, BinaryTree<A> right) {
        this.node = node;
        this.left = left;
        this.right = right;
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
        return left == null;
    }

    public BinaryTree<A> nextNode(boolean bit) {
        return bit ? right : left;
    }
}