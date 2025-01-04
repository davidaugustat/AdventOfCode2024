package com.davidaugustat.aoc2024.utils;

import java.lang.reflect.Array;
import java.util.HashMap;
import java.util.Map;
import java.util.function.BiPredicate;

/**
 * This class represents a priority queue. The nodes are ordered by their distance from
 * the start node.
 * <p>
 * Initially, the queue is empty, and it can be filled using the insertWithoutDuplicates()
 * method.
 * <p>
 * This priority queue has modifiable priorities, which makes it possible to update the
 * distance of a node and reorder the heap so that the heap condition is met again.
 * <p>
 * The priority queue is based on a binary heap that is realized using an array.
 * <p>
 * Note: I re-purposed this class from an old study project I did in my third semester.
 */
public class BinaryHeapPriorityQueue<T> {

    /**
     * Number of nodes currently stored inside the heap.
     */
    private int currentHeapSize;

    /**
     * Array used internally for the heap.
     */
    private final T[] data;

    /**
     * Map that stores the position of each node inside the data array.
     * If a node does not exist in the heap, the value is Constants.NOT_IN_HEAP
     */
    private final Map<T, Integer> nodePositions;

    private final BiPredicate<T, T> isNode1SmallerThanNode2;

    @SuppressWarnings("unchecked")
    public BinaryHeapPriorityQueue(Class<T> dataType, int numNodesTotal, BiPredicate<T, T> isNode1SmallerThanNode2) {
        this.isNode1SmallerThanNode2 = isNode1SmallerThanNode2;
        data = (T[]) Array.newInstance(dataType, numNodesTotal);
        nodePositions = new HashMap<>();
        currentHeapSize = 0;
    }

    /**
     * Inserts a node into the queue or updates its position if it already exists.
     * <p>
     * If the node does not exist in the queue, it will be inserted and moved to the
     * correct position, so that the heap condition is restored.
     * <p>
     * If the node already exists in the queue, it will be moved up until the heap
     * condition is restored.
     * <p>
     * Note that when calling this method on a node that already exists in the queue,
     * the distance of this node must be equal or less to its previous distance, since the
     * node will only be moved up but not down inside the queue.
     * <p>
     * This method can be used as a "update position if node exists, insert node if it
     * doesn't exist" approach.
     *
     * @param node The node to insert or update
     */
    public void insertWithoutDuplicates(T node) {
        if (nodePositions.containsKey(node)) {
            floatUp(nodePositions.get(node));
            return;
        }

        int position = currentHeapSize;
        data[position] = node;
        nodePositions.put(node, position);
        currentHeapSize++;
        floatUp(position);
    }

    /**
     * Returns the minimal element and removes it from the heap.
     * <p>
     * The heap condition is restored automatically.
     *
     * @return Node with the smallest distance from start node.
     */
    public T poll() {
        T minNode = data[0];
        T lastNode = data[currentHeapSize - 1];
        data[0] = lastNode;

        nodePositions.put(lastNode, 0);
        nodePositions.remove(minNode);
        currentHeapSize--;

        percolate(0);

        return minNode;
    }

    /**
     * Returns the minimal element without removing it from the heap.
     *
     * @return Node with smallest distance from start node.
     */
    public T peak() {
        return data[0];
    }

    /**
     * Checks if a node is inside the heap
     *
     * @param node The of the node to check for
     * @return True if node is inside heap, otherwise false
     */
    public boolean contains(T node) {
        return nodePositions.containsKey(node);
    }

    /**
     * @return true if the heap contains 0 elements, otherwise false
     */
    public boolean isEmpty() {
        return currentHeapSize == 0;
    }

    /**
     * Lets the element at the given position float up until it is at the correct position
     * (--> the parent is smaller or equal to the element), so that the heap condition
     * is restored.
     *
     * @param position The position of the element inside the data array.
     */
    private void floatUp(int position) {
        int currentPosition = position;
        int parentPosition = getParentPosition(currentPosition);
        while (isNode1SmallerThanNode2.test(data[currentPosition], data[parentPosition])) {
            swap(currentPosition, parentPosition);
            currentPosition = parentPosition;
            parentPosition = getParentPosition(currentPosition);
        }
    }

    /**
     * Comparator method that checks if the distance of node 1  from the start node is
     * greater than the distance of node 2 from the start node.
     *
     * @return true if node 1 has a greater distance than node 2, otherwise false.
     */
    private boolean isNode1GreaterThanNode2(T node1, T node2) {
        return isNode1SmallerThanNode2.test(node2, node1);
    }

    /**
     * @return Position of the parent element inside the heap
     */
    private int getParentPosition(int childPosition) {
        return (childPosition - 1) / 2;
    }

    /**
     * @return Position of the left child element in the heap
     */
    private int getLeftChildPosition(int parentPosition) {
        return parentPosition * 2 + 1;
    }

    /**
     * @return Position of the right child element in the heap
     */
    private int getRightChildPosition(int parentPosition) {
        return parentPosition * 2 + 2;
    }

    /**
     * Swaps two elements in the heap.
     * <p>
     * This method swaps the elements in the data[] array and also updates the positions
     * of the nodes in the nodePositions map.
     */
    private void swap(int position1, int position2) {
        // Swap positions of nodes:
        nodePositions.put(data[position1], position2);
        nodePositions.put(data[position2], position1);

        // Swap elements in heap:
        T tmp = data[position1];
        data[position1] = data[position2];
        data[position2] = tmp;
    }

    /**
     * Lets an element at a given position sink into the heap until the heap condition
     * is met.
     * <p>
     * Note that the method only moves elements down, but not up. A smaller element will
     * NOT rise up when calling this method.
     *
     * @param position Position of the element to percolate inside the data array.
     */
    private void percolate(int position) {
        int currentPosition = position;
        int lastPosition = currentHeapSize - 1;

        while (getLeftChildPosition(currentPosition) <= lastPosition) {
            int leftChildPosition = getLeftChildPosition(currentPosition);
            int rightChildPosition = getRightChildPosition(currentPosition);
            int smallestChildPosition = leftChildPosition;

            if (rightChildPosition <= lastPosition) {
                if (isNode1GreaterThanNode2(data[leftChildPosition], data[rightChildPosition])) {
                    smallestChildPosition = rightChildPosition;
                }
            }
            if (isNode1GreaterThanNode2(data[currentPosition], data[smallestChildPosition])) {
                swap(currentPosition, smallestChildPosition);
                currentPosition = smallestChildPosition;
            } else {
                break;
            }
        }
    }
}
