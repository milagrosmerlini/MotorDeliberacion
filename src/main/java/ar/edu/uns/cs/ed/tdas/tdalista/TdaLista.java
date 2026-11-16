package ar.edu.uns.cs.ed.tdas.tdalista;

import java.util.Iterator;
import java.util.NoSuchElementException;
import ar.edu.uns.cs.ed.tdas.Position;
import ar.edu.uns.cs.ed.tdas.excepciones.BoundaryViolationException;
import ar.edu.uns.cs.ed.tdas.excepciones.EmptyListException;
import ar.edu.uns.cs.ed.tdas.excepciones.InvalidPositionException;



public class TdaLista<E> implements PositionList<E> {
    
    protected DNodo<E> header;
    protected DNodo<E> trailer;
    protected int cant;
    
    public TdaLista(){
        header= new DNodo<E>(null);
        trailer= new DNodo<E>(null);
        header.setNext(trailer);
        trailer.setPrev(header);
        cant=0;
    }
    public int size(){
        return cant;
    }
    public boolean isEmpty(){
        return cant==0;
    }
    public Position<E> first(){
        if (isEmpty()){
            throw new EmptyListException("la lista esta vacia");
        }
        return header.getNext();
    }
    public Position<E> last(){
        if (isEmpty()){
            throw new EmptyListException("la lista esta vacia");
        }
        return trailer.getPrev();
    }
    public Position<E> next(Position<E> p){
        DNodo<E> nodo= checkPosition(p);
        if (isEmpty()){
            throw new InvalidPositionException("la lista esta vacia o la posicion no es valida");
        }
        if (nodo.getNext()== trailer){

            throw new BoundaryViolationException("no hay siguiente");
        }
        return nodo.getNext();
    }
    public Position<E> prev(Position<E> p){
        DNodo<E> nodo= checkPosition(p);
        if (isEmpty()){
            throw new InvalidPositionException("la lista esta vacia o la posicion no es valida");
        }
        if (nodo.getPrev() == header){
            throw new BoundaryViolationException("no hay previo");
        } 
        return nodo.getPrev();
    }
    public void addFirst(E element){
        DNodo<E> siguiente= header.getNext();
        DNodo<E> nodo= new DNodo<E>(element, header, siguiente);
        header.setNext(nodo);
        siguiente.setPrev(nodo);
        cant++;
    }
    public void addLast(E element){
       DNodo<E> anterior= trailer.getPrev();
       DNodo<E> nodo= new DNodo<E>(element, anterior, trailer);
       trailer.setPrev(nodo);
       anterior.setNext(nodo);
       cant++;
    }  
    public void addAfter(Position<E> p, E element){
        if (isEmpty()){
            throw new InvalidPositionException("la lista esta vacia o posicion invalida");
        }
        DNodo<E> nodo= checkPosition(p);
        DNodo<E> siguiente= nodo.getNext();
        DNodo<E> nuevo= new DNodo<E>(element, nodo, siguiente);
        nodo.setNext(nuevo);
        siguiente.setPrev(nuevo);
        cant++;
    }
    public void addBefore(Position<E> p, E element){
        if(isEmpty()){
            throw new InvalidPositionException("la lista esta vacia o posicion invalida");
        }
        DNodo<E> nodo= checkPosition(p);
        DNodo<E> anterior= nodo.getPrev();
        DNodo<E> nuevo= new DNodo<E>(element,anterior,nodo);
        nodo.setPrev(nuevo);
        anterior.setNext(nuevo);
        cant++;
    }
    public E remove(Position<E> p){
        if(isEmpty()){
            throw new InvalidPositionException("posicion invalida o lista vacia");
        }
        DNodo<E> nodo= checkPosition(p);
        DNodo<E> anterior= nodo.getPrev();
        DNodo<E> siguiente= nodo.getNext();
        anterior.setNext(siguiente);
        siguiente.setPrev(anterior);
        cant--;
         nodo.setPrev(null);
         nodo.setNext(null);


        return nodo.element();
    }
    public E set(Position<E> p, E elem){
        if (isEmpty()){
            throw new InvalidPositionException ("la lista esta vacia o posicion invalida");
        }
      DNodo<E> nodo= checkPosition(p);
      E anterior= nodo.element();
      nodo.setElement(elem);
      return anterior;

    }
    private DNodo<E> checkPosition(Position<E> p){
        if (p== null){
            throw new InvalidPositionException("posicion no valida");
        }
        if (p== header){
            throw new InvalidPositionException("posicion invalida");
        }
        if (p==trailer){
            throw new InvalidPositionException("posicion invalida");
        }  try{
             DNodo<E> nodo= (DNodo<E>)p;
        if (nodo.getPrev()== null || nodo.getNext()== null){
            throw new InvalidPositionException("posicion no valida");

        }
        return nodo;

       }    catch (ClassCastException e){
            throw new InvalidPositionException("posicion no valida");
       }
    }

    public Iterator<E> iterator(){

        return new MiIterador<E>(this){
            private DNodo<E> actual= header.getNext();
            public boolean hasNext(){
                return actual!= trailer;
            }
            public E next(){
                if (!hasNext()) {
                   throw new NoSuchElementException("no hay siguiente posicion") ;
                }
                E e=actual.element();
                actual=actual.getNext();
                return e;
            }

        };
    }
    public Iterable<Position<E>> positions(){
       PositionList<Position<E>> pl= new TdaLista<Position<E>>();
       if (cant!=0){
        DNodo<E> n= header.getNext();
        while (n!= trailer){
            pl.addLast(n);
            n=n.getNext();
        }
       }
       return pl;
         
    }

    public void modificar(E e1, E e2){
        if (isEmpty()){
        DNodo<E> nuevo= new DNodo<E>(e2, header, trailer);
        header.setNext(nuevo);
        trailer.setPrev(nuevo);
        DNodo<E> nuevo2= new DNodo<E>(e1, nuevo, trailer);
        nuevo.setNext(nuevo2);
        trailer.setPrev(nuevo2);
        cant+= 2;
        } else
        if (cant==1){
            DNodo<E> primero= header.getNext();
            DNodo<E> n1= new DNodo<E>(e1, primero, trailer);
            primero.setNext(n1);
            trailer.setPrev(n1);
            DNodo<E> n2= new DNodo<E>(e2, primero, n1);
            primero.setNext(n2);
            n1.setPrev(n2);
            cant+= 2;
        }else {
        DNodo<E> primero= header.getNext();
        DNodo<E> segundo= primero.getNext();
      
        DNodo<E> n1= new DNodo<E>(e1, primero, segundo);
        primero.setNext(n1);
        segundo.setPrev(n1);
          DNodo<E> ultimo= trailer.getPrev();
        DNodo<E> anteul= ultimo.getPrev();
        DNodo<E> n2= new DNodo<E>(e2,anteul, ultimo);
        anteul.setNext(n2);
        ultimo.setPrev(n2);
        cant+= 2;
        }

    }
    //dentro de la estrucutura
    //no exhaustivo
    //
    public int superEliminar(E elem) throws EmptyListException{
        if(cant==0){
            throw new EmptyListException("no se puede eliminar de una lista vacia");
           
        }//dentro de la estructura conviene recorrer LISTA A MANO
         int cantE=0;
        DNodo<E> cursor=header.getNext();
        while (cursor!=trailer) {
             if(cursor.element().equals(elem)){
                cursor.getPrev().setNext(cursor.getNext());
                cursor.getNext().setPrev(cursor.getPrev());
                
                cantE++;
                
             }
             cursor=cursor.getNext();
        }
           cant-=cantE;
           return cant;
    }
}
            
    



    



