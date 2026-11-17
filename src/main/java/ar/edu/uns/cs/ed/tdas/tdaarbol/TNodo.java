package ar.edu.uns.cs.ed.tdas.tdaarbol;

import ar.edu.uns.cs.ed.tdas.Position;
import ar.edu.uns.cs.ed.tdas.tdalista.PositionList;
import ar.edu.uns.cs.ed.tdas.tdalista.TdaLista;

public class TNodo<E> implements Position<E>{
    private E elem;
    private TNodo<E> padre;
    private PositionList<TNodo<E>> hijos;

    public TNodo(TNodo<E> p, E e){
        elem=e;
        padre=p;
        hijos= new TdaLista<TNodo<E>>();
    }
    public E element(){
        return elem;
    }
    public void setElement(E elem){
        this.elem=elem;
    }
    public TNodo<E> getPadre(){
        return padre;
    }
    public void setPadre(TNodo<E> padre){
        this.padre=padre;
    }
    public PositionList<TNodo<E>> getHijos(){
        return hijos;
    }
}
    

