package ar.edu.uns.cs.ed.tdas;

public class Node<E> {
    protected E element;
    protected Node<E> next;

    public Node(E e){
        element = e;
        next = null;
    }

    public Node(E e, Node<E> n){
        element = e;
        next = n;
    }

    public E getElement(){
        return element;
    }

    public void setElement(E e){
        element = e;
    }

    public Node<E> getNext(){
        return next;
    }

    public void setNext(Node<E> n){
        next = n;
    }
}
