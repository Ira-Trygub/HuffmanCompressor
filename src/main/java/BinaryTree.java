import java.util.function.Consumer;

public class BinaryTree<HuffNode> {
HuffNode headHuffNode;
BinaryTree left;
BinaryTree right;
int leftMark;
int rightMark;

    public BinaryTree(HuffNode headHuffNode) {
        this.headHuffNode = headHuffNode;
    }


    public HuffNode getHuffNode() {
        return this.headHuffNode;
    }


    public void setHuffNode(HuffNode huffNode) {
        this.headHuffNode = huffNode;
    }


    public BinaryTree<HuffNode> getLeftNode() {
        if (this.left != null) return left;
        return null;
    }


    public BinaryTree<HuffNode> createRightNode(HuffNode d) {
        this.right = new BinaryTree<>(d);
        return this.right;


    }


    public BinaryTree<HuffNode> createLeftNode(HuffNode d) {
        this.left = new BinaryTree<>(d);
        return this.left;
    }

    public void setLeftNode(BinaryTree<HuffNode> leftNode) {
        this.left = leftNode;
    }


    public BinaryTree<HuffNode> getRightNode() {
        if (this.right != null) return right;
        return null;
    }


    public boolean isLeaf() {
        return (this.left == null) && (this.right == null);
    }




    public void setMarks() {
        setMarksR(this);

    }

    private void setMarksR(BinaryTree<HuffNode> tree) {
        if (tree.getLeftNode() != null) {
            leftMark = 0;
            setMarksR(tree.getLeftNode());
        }

        if (tree.getRightNode() != null) {
            rightMark = 1;
            setMarksR(tree.getRightNode());
        }
    }


//
//
//    public void visitPreOrder(Consumer<BinaryTree<HuffNode>> visitor) {
//        visitPreOrderR(this, visitor);
//    }
//
//    private void visitPreOrderR(BinaryTree<HuffNode> tree, Consumer<BinaryTree<HuffNode>> visitor) {
//        visitor.accept(tree);
//        if (tree.getLeftNode() != null) {
//            visitPreOrderR(tree.getLeftNode(), visitor);
//        }
//        if (tree.getRightNode() != null) {
//            visitPreOrderR(tree.getRightNode(), visitor);
//        }
//    }
//
    public void visitInOrder(Consumer<BinaryTree<HuffNode>> visitor) {
        visitInOrderR(this, visitor);

    }

    private void visitInOrderR(BinaryTree<HuffNode> tree, Consumer<BinaryTree<HuffNode>> visitor) {
        if (tree.getLeftNode() != null) {
            visitInOrderR(tree.getLeftNode(), visitor);
        }
        visitor.accept(tree);
        if (tree.getRightNode() != null) {
            visitInOrderR(tree.getRightNode(), visitor);
        }
  }
//
//
//
//
//    public void visitPostOrder(Consumer<BinaryTree<HuffNode>> visitor) {
//        visitPostOrderR(this, visitor);
//    }
//
//    private void visitPostOrderR(BinaryTree<HuffNode> tree, Consumer<BinaryTree<HuffNode>> visitor) {
//        if (tree.getLeftNode() != null) {
//            visitPostOrderR(tree.getLeftNode(), visitor);
//        }
//
//        if (tree.getRightNode() != null) {
//            visitPostOrderR(tree.getRightNode(), visitor);
//        }
//        visitor.accept(tree);
//    }

}
