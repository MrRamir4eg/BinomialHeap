package org.example;

public class Example {

    public static void main(String[] args) {
        BinomialHeap heap = new BinomialHeap(42);
        heap.insert(31);
        heap.insert(62);
        heap.insert(21);
        heap.insert(26);
        System.out.println(heap.getMin());
        heap.extractMin();
        System.out.println(heap.getMin());
        heap.delete(heap.findNodeWithKey(62));
        System.out.println(heap.getMin());
    }
}
