package utilities;

import java.util.*;

public class CircularList<E> implements List<E> {

    private Node<E> head;
    private int size = 0;

    private static class Node<E> {
        private E element;
        private Node<E> next;

        public Node(E element, Node<E> next) {
            this.element = element;
            this.next = next;
        }
    }

    @Override
    public int size() {
        return this.size;
    }

    @Override
    public boolean isEmpty() {
        return this.size == 0;
    }

    @Override
    public boolean contains(Object o) {

        if(this.size == 0) return false;

        Node<E> iter = head;
        do{
            if (Objects.equals(iter.element, o)) return true;
            iter = iter.next;
        } while (iter != head);

        return false;
    }

    @Override
    public Iterator iterator() {
        return null;
    }

    @Override
    public Object[] toArray() {
        Object[] objects = new Object[this.size];

        Node<E> iter = head;
        for(int i = 0; i < this.size; i++){
            objects[i] = iter;
            iter = iter.next;
        }

        return objects;
    }

    @Override
    public <T> T[] toArray(T[] a) {
        return null;
    }

    @Override
    public boolean add(E e) {

        if (this.size == 0){
            this.head = new Node<>(e, head);
            this.head.next = this.head;
            this.size++;
            return true;
        }

        Node<E> iter = head;

        while(iter.next != head){
            iter = iter.next;
        }

        Node<E> newNode = new Node<E>(e, head);

        iter.next = newNode;

        this.size++;

        return true;
    }

    @Override
    public boolean remove(Object o) {

        if(this.size == 0) return false;

        Node<E> previous = head;
        Node<E> iter =  head.next;
        do{
            if (Objects.equals(iter.element, o)) {
                previous.next = iter.next;
                iter = iter.next;
                this.size--;
            } else {
                previous = iter;
                iter = iter.next;
            }
        } while (iter != head.next);

        return true;
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        return false;
    }

    @Override
    public boolean addAll(Collection<? extends E> c) {

        if (c.isEmpty()) return false;

        Iterator<? extends E> iterator = c.iterator();

        while(iterator.hasNext()){
            this.add(iterator.next());
        }

        return true;
    }

    @Override
    public boolean addAll(int index, Collection<? extends E> c) {
        return false;
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        return false;
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        return false;
    }

    @Override
    public void clear() {

    }

    @Override
    public E get(int index) {

        Node<E> iter = head;

        for(int i = 0; i < index; i++){
            iter = iter.next;
        }

        return iter.element;
    }

    @Override
    public E set(int index, E element) {
        return null;
    }

    @Override
    public void add(int index, E element) {

        Node<E> iter = head;

        do{
            iter = iter.next;
        }while(iter.next != head);

        Node<E> previous = iter;
        iter = previous.next;

        for(int i = 0; i < index; i++){
            previous = iter;
            iter = iter.next;
        }

        Node<E> newNode = new Node<>(element, iter);
        previous.next = newNode;
    }

    @Override
    public E remove(int index) {

        Node<E> iter = head;

        do{
            iter = iter.next;
        }while(iter.next != head);

        Node<E> previous = iter;
        iter = previous.next;

        for(int i = 0; i < index; i++){
            previous = iter;
            iter = iter.next;
        }

        if (iter == head) {
            head = iter.next;
        }

        previous.next = iter.next;

        return iter.element;

    }

    @Override
    public int indexOf(Object o) {

        Node<E> iter = head;

        int index = 0;

        do{
            if(Objects.equals(iter.element, o)) return index;
            iter = iter.next;
            index++;
        }while(iter != head);

        return -1;
    }

    @Override
    public int lastIndexOf(Object o) {
        return 0;
    }

    @Override
    public ListIterator<E> listIterator() {
        return null;
    }

    @Override
    public ListIterator<E> listIterator(int index) {
        return null;
    }

    @Override
    public List<E> subList(int fromIndex, int toIndex) {
        return null;
    }

    @Override
    public String toString(){
        StringBuilder stringBuilder = new StringBuilder();

        Node<E> iter = head;
        stringBuilder.append("[");
        do{
            stringBuilder.append(iter.element);
            stringBuilder.append(",");
            iter = iter.next;
        }while(iter != head);

        stringBuilder.deleteCharAt(stringBuilder.lastIndexOf(","));
        stringBuilder.append("]");

        return stringBuilder.toString();
    }
}
