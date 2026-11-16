package ar.edu.uns.cs.ed.tdas.tdacola;
import ar.edu.uns.cs.ed.tdas.excepciones.EmptyQueueException;

public class ColaConArreglo<E> implements Queue<E> {
    private int cantEl;
    private int fondo;
    private int frente;
    private E[] elemento;

    public ColaConArreglo(){
        elemento= (E[]) new Object[10];
        cantEl=0;
        frente=0;
        fondo=0;
    }
    public int size(){
        return cantEl;
    }
    public boolean isEmpty(){
        return cantEl==0;
    }
    public E front(){
        if (isEmpty()){
            throw new EmptyQueueException("la cola esta vacia");
        }
        return elemento[frente];
    }
    public void enqueue (E elem){
        if (fondo==elemento.length){
            E[] nuevo= (E[]) new Object[elemento.length * 2];
            for (int i=0; i< elemento.length; i++ ){
                nuevo[i]=elemento[i];

            }
            elemento=nuevo;
        }
        elemento[fondo]=elem;
        cantEl++;
        fondo++;

    }
    public E dequeue(){
        if (isEmpty()){
            throw new EmptyQueueException("la cola esta vacia");

        }
        E elem= elemento[frente];
        elemento[frente]= null;
        frente++;
        cantEl--;
        return elem;
    }
    
}

    

