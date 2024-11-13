import java.util.ArrayList;
import java.util.List;

public class BinaryTree<T extends Comparable<T>> {
    private Node<T> root;

    public boolean add(T value) {
        if (root == null) {
            root = new Node<>(value, Color.Black);
            return true;
        }
        return addNode(root, value);
    }

    private boolean addNode(Node<T> node, T value) {
        if (node.value.compareTo(value) == 0)
            return false;
        if (node.value.compareTo(value) > 0) {
            if (node.leftChild != null) {
                boolean result = addNode(node.leftChild, value);
                node.leftChild = rebalanced(node.leftChild);
                return result;
            } else {
                node.leftChild = new Node<>(value, Color.Red);
                return true;
            }
        } else {
            if (node.rightChild != null) {
                boolean result = addNode(node.rightChild, value);
                node.rightChild = rebalanced(node.rightChild);
                return result;
            } else {
                node.rightChild = new Node<>(value, Color.Red);
                return true;
            }
        }
    }

    public boolean contain(T value) {
        Node<T> currentNode = root;
        while (currentNode != null) {
            if (currentNode.value.equals(value))
                return true;
            if (currentNode.value.compareTo(value) > 0)
                currentNode = currentNode.leftChild;
            else
                currentNode = currentNode.rightChild;
        }
        return false;
    }

    public boolean remove(T value) {
        if (!contain(value))
            return false;
        Node<T> deleteNode = root;
        Node<T> prevNode = root;
        while (deleteNode != null) {
            if (deleteNode.value.compareTo(value) == 0) {
                Node<T> currentNode = deleteNode.rightChild;
                if (currentNode == null) {
                    if (deleteNode == root) {
                        root = root.leftChild;
                        root.color = Color.Black;
                        return true;
                    }
                    if (deleteNode.leftChild == null) {
                        deleteNode = null;
                        return true;
                    }
                    if (prevNode.leftChild == deleteNode)
                        prevNode.leftChild = deleteNode.leftChild;
                    else
                        prevNode.rightChild = deleteNode.leftChild;
                    return true;
                }
                while (currentNode.leftChild != null)
                    currentNode = currentNode.leftChild;
                deleteNode.value = currentNode.value;
                currentNode = null;
                return true;
            }
            if (prevNode != deleteNode) {
                if (prevNode.value.compareTo(value) > 0)
                    prevNode = prevNode.leftChild;
                else
                    prevNode = prevNode.rightChild;
            }
            if (deleteNode.value.compareTo(value) > 0)
                deleteNode = deleteNode.leftChild;
            else
                deleteNode = deleteNode.rightChild;
        }
        return false;
    }

    private Node<T> rebalanced(Node<T> node) {
        Node<T> result = node;
        boolean needRebalance;
        do {
            needRebalance = false;
            if (result.rightChild != null && result.rightChild.color == Color.Red
                    && (result.leftChild == null || result.leftChild.color == Color.Black)) {
                needRebalance = true;
                result = rightSwap(result);
            }
            if (result.leftChild != null && result.leftChild.color == Color.Red
                    && (result.leftChild.leftChild != null && result.leftChild.leftChild.color == Color.Red)) {
                needRebalance = true;
                result = leftSwap(result);
            }
            if (result.leftChild != null && result.leftChild.color == Color.Red
                    && result.rightChild != null && result.rightChild.color == Color.Red) {
                needRebalance = true;
                colorSwap(result);
            }
        } while (needRebalance);
        return result;
    }

    private Node<T> rightSwap(Node<T> node) {
        Node<T> rightChild = node.rightChild;
        node.rightChild = rightChild.leftChild;
        rightChild.leftChild = node;
        rightChild.color = node.color;
        node.color = Color.Red;
        return rightChild;
    }

    private Node<T> leftSwap(Node<T> node) {
        Node<T> leftChild = node.leftChild;
        node.leftChild = leftChild.rightChild;
        leftChild.rightChild = node;
        leftChild.color = node.color;
        node.color = Color.Red;
        return leftChild;
    }

    private void colorSwap(Node<T> node) {
        node.color = Color.Red;
        node.leftChild.color = Color.Black;
        node.rightChild.color = Color.Black;
    }

    public void print() {
        printInOrder(root);
    }

    private void printInOrder(Node<T> node) {
        if (node != null) {
            printInOrder(node.leftChild);
            System.out.print(node.value + " ");
            printInOrder(node.rightChild);
        }
    }

    private static class Node<T> {
        T value;
        Node<T> leftChild;
        Node<T> rightChild;
        Color color;

        Node(T value, Color color) {
            this.value = value;
            this.color = color;
            leftChild = null;
            rightChild = null;
        }
    }

    private enum Color {
        Red, Black
    }
}
