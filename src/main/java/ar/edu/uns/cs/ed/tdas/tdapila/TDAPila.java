package ar.edu.uns.cs.ed.tdas.tdapila;
import ar.edu.uns.cs.ed.tdas.Node;
import ar.edu.uns.cs.ed.tdas.excepciones.EmptyStackException;

public class TDAPila<E> implements Stack<E>{

    private Node<E> tope;
    private int cantidad;

    public TDAPila(){
        tope = null;
        cantidad = 0;
    }

    public int size(){
        return cantidad;
    }

    public boolean isEmpty(){
        return cantidad == 0;
    }

    public E top() throws EmptyStackException{
        if (isEmpty())
            throw new EmptyStackException("La pila está vacia.");
        E elemento = tope.getElement();
        return elemento;
    }

    public void push(E element){
        Node<E> nuevo = new Node<E>(element);
        nuevo.setNext(tope);
        tope = nuevo;
        cantidad++;
    }

    public E pop(){
        if (isEmpty())
            throw new EmptyStackException("La pila está vacia.");
        E elemento = tope.getElement();
        tope = tope.getNext();
        cantidad--;
        return elemento;
    }
}
