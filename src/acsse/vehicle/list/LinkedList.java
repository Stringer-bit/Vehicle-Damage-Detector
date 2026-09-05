package acsse.vehicle.list;

public class LinkedList<E> {

    private static class Node<E> {
        E data;
        Node<E> next;

        Node(E data) {
            this.data = data;
        }
    }

    private Node<E> head;
    private Node<E> tail;

    // enqueue
    public void add(E element) {
        Node<E> node = new Node<>(element);

        if (tail != null) {
            tail.next = node;
        }

        tail = node;

        if (head == null) {
            head = node;
        }
    }

    // dequeue
    public E poll() {
        if (head == null) {
            return null;
        }

        E value = head.data;
        head = head.next;

        if (head == null) {
            tail = null;
        }

        return value;
    }

    public boolean isEmpty() {
        return head == null;
    }
}