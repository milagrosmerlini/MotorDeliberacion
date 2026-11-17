package ar.edu.uns.cs.ed.tdas.tdalista;
import ar.edu.uns.cs.ed.tdas.Position;


public class DNodo<E> implements Position<E> {
    protected DNodo<E> next;
    protected DNodo<E> prev;
    protected E elem;

    public DNodo (E e){
       elem=e;
       prev=null;
       next=null;
    }
    public DNodo (E e, DNodo<E> p, DNodo<E> n){
        elem=e;
        prev=p;
        next=n;
    }
    public E element(){
        return elem;
    
    }
    public void setElement(E elem){
        this.elem=elem;
    }
    public DNodo<E> getNext(){
        return next;
    }
    public void setNext (DNodo<E> next){
        this.next=next;
    }
    public void setPrev(DNodo<E> prev){
        this.prev=prev;
    }
    public DNodo<E> getPrev(){
        return prev;
    }




}
