package ar.edu.uns.cs.ed.tdas.tdaarbol;

import java.util.Iterator;

import ar.edu.uns.cs.ed.tdas.Position;
import ar.edu.uns.cs.ed.tdas.excepciones.BoundaryViolationException;
import ar.edu.uns.cs.ed.tdas.excepciones.EmptyTreeException;
import ar.edu.uns.cs.ed.tdas.excepciones.InvalidOperationException;
import ar.edu.uns.cs.ed.tdas.excepciones.InvalidPositionException;
import ar.edu.uns.cs.ed.tdas.tdalista.PositionList;
import ar.edu.uns.cs.ed.tdas.tdalista.TdaLista;
import ar.edu.uns.cs.ed.tdas.tdamapeo.Map;
import ar.edu.uns.cs.ed.tdas.tdamapeo.MapeoHashAbierto;

public class Arbol<E> implements Tree<E> {
    private TNodo<E> raiz;
    private int cant;

    public Arbol(){
        raiz=null;
        cant=0;

    }
    public int size(){
        return cant;
    }
    public boolean isEmpty(){
        return cant==0;
    }
    public Iterator<E> iterator(){
        PositionList<E> l= new TdaLista<E>(); //E es el tipo de elementos que queres guardar, x eso no va PL<TNodo<E>>
        preOrden(raiz,l);
        return l.iterator();
    

    }
    private void preOrden(TNodo<E> n, PositionList<E> list){
        if(n!= null){
           list.addLast(n.element());
             for (TNodo<E> e : n.getHijos()){
                  preOrden(e,list);

                }
        }
    }
    public Iterable<Position<E>> positions(){
        PositionList<Position<E>> l = new TdaLista<Position<E>>();
        preOrdenI(raiz,l);
        return l;

    }
    private void preOrdenI(TNodo<E> n, PositionList<Position<E>> list){
        if(n!=null){
            list.addLast(n);
            for(TNodo<E> e : n.getHijos()){
                preOrdenI(e,list);
            }
        }
    }
    public E replace(Position<E> m, E e){
        if(m==null){
            throw new InvalidPositionException("posicion invalida");
        }
        TNodo<E> p= checkPosition(m);
        E elem= p.element();
        p.setElement(e);
        return elem;

    }
    private TNodo<E> checkPosition(Position<E> p){
        if (p== null){
            throw new InvalidPositionException("posicion no valida");
        
        }  try{
             TNodo<E> nodo= (TNodo<E>)p;
             return nodo;

       }    catch (ClassCastException e){
            throw new InvalidPositionException("posicion no valida");
       }
    }
    public Position<E> root(){
        if(cant==0){
            throw new EmptyTreeException("arbol vacio");
        }
        return raiz;
    }
    public Position<E> parent(Position<E> v){
        if (isEmpty()){
        throw new InvalidPositionException("arbol vacio");
        }
        TNodo<E> p= checkPosition(v);
        if(p==raiz){
            throw new BoundaryViolationException("posicion invalida");
        }
        return p.getPadre();

    }
    public Iterable<Position<E>> children(Position<E> v){
        TNodo<E> nodo= checkPosition(v);
        PositionList<TNodo<E>> hijos= nodo.getHijos();
        PositionList<Position<E>> lista= new TdaLista<>();
        for(Position<TNodo<E>> e : hijos.positions()){
            lista.addLast(e.element());
        }
        return lista;


    }
    public boolean isInternal(Position<E> v){
        TNodo<E> m= checkPosition(v);
        return (!m.getHijos().isEmpty());
    }
    public boolean isExternal(Position<E> v){
        TNodo<E> e= checkPosition(v);
        return (e.getHijos().isEmpty());
    }
    public boolean isRoot(Position<E> v){
        TNodo<E> e= checkPosition(v);
        return e==raiz;
    }
    public void createRoot( E e){

        if(cant==0){
            raiz= new TNodo<E>(null, e);
            cant++;
        }else{
            throw new InvalidOperationException("ya tiene raiz");
        }
        
    }
    public Position<E> addFirstChild(Position<E> p,E e){
        if(isEmpty()){
            throw new InvalidPositionException("posicion invalida");
        }
        TNodo<E> padre= checkPosition(p);
        TNodo<E> nuevo= new TNodo<E>(padre, e);
        PositionList<TNodo<E>> lista= padre.getHijos();
        lista.addFirst(nuevo);
        cant++;
        return nuevo;
    }
    public Position<E> addLastChild (Position<E> p, E e){
        if(isEmpty()){
            throw new InvalidPositionException("posicion invalida");
        }
        TNodo<E> padre= checkPosition(p);
        TNodo<E> nuevo= new TNodo<E>(padre, e);
        PositionList<TNodo<E>> lista= padre.getHijos();
        lista.addLast(nuevo);
        cant++;
        return nuevo;
    }
    public Position<E> addBefore(Position<E> p, Position<E> rb, E e){
         if(cant==0){
            throw new InvalidPositionException("arbol vacio");
        }
        TNodo<E> padre= checkPosition(p);
        TNodo<E> nuevo= new TNodo<E>(padre, e);
        TNodo<E> rightB= checkPosition(rb);
        PositionList<TNodo<E>> lista= padre.getHijos(); 
       Position<TNodo<E>> posicion = null;

         for (Position<TNodo<E>> hijo : lista.positions()) {
             if (hijo.element() == rightB) {
                  posicion = hijo;
                   break;
                }
            }

        if (posicion == null)
          throw new InvalidPositionException("no se encontro el hermano");

           lista.addBefore(posicion, nuevo);
            cant++;
        return nuevo;
    }
    public Position<E> addAfter(Position<E> p, Position<E> lb, E e){
        if(cant==0){
            throw new InvalidPositionException("arbol vacio");
        }
        TNodo<E> padre= checkPosition(p);
      
        TNodo<E> leftB= checkPosition(lb);
        PositionList<TNodo<E>> lista= padre.getHijos();
          Position<TNodo<E>> posicion = null;

         for (Position<TNodo<E>> hijo : lista.positions()) {
             if (hijo.element() == leftB) {
                  posicion = hijo;
                   break;
                }
            }

        if (posicion == null)
          throw new InvalidPositionException("no se encontro el hermano");
           TNodo<E> nuevo= new TNodo<E>(padre, e);

           lista.addAfter(posicion, nuevo);
            cant++;
         return nuevo;
}
    
    public void removeExternalNode(Position<E> v){
        
        if(cant==0){
            throw new InvalidPositionException("arbol vacio");
        }
        TNodo<E> nodo= checkPosition(v);
        if(!nodo.getHijos().isEmpty()){
            throw new InvalidPositionException("nodo interno");
        }
        if(nodo==raiz){
            raiz=null;
        }else {
             PositionList<TNodo<E>> lista= nodo.getPadre().getHijos();
              Position<TNodo<E>> posicion = null;

        for (Position<TNodo<E>> hijo : lista.positions()) {
            if (hijo.element() == nodo) {
                posicion = hijo;
                break;
            }
        }

        if (posicion == null)
            throw new InvalidPositionException("no se encontro el nodo");

        lista.remove(posicion);
    }

    cant--;
    }
    public void removeInternalNode(Position<E> v){
        if(cant==0){
            throw new InvalidPositionException("posicion invalia");
        }
        if(cant==1){
            raiz=null;
        }
        
        TNodo<E> nodo= checkPosition(v);
        if (nodo.getHijos().isEmpty()) {
                throw new InvalidPositionException("es nodo externo");
        }
        if(nodo==raiz){
            if(nodo.getHijos().size()!=1){
                throw new InvalidPositionException("mas de un hijo");

            
            }else{
                 Position<TNodo<E>> posHijo = nodo.getHijos().first();
                 TNodo<E> nuevoRaiz = posHijo.element();

            nodo.getHijos().remove(posHijo);
            raiz = nuevoRaiz;
            raiz.setPadre(null);
            }

        }else {
        TNodo<E> padre= nodo.getPadre();
        PositionList<TNodo<E>> lista= padre.getHijos();
        Position<TNodo<E>> posNodo= null;
        for(Position<TNodo<E>> e: lista.positions()){
            if(e.element()== nodo){
                posNodo=e;
                break;

            }
        }
        PositionList<TNodo<E>> hijos= nodo.getHijos();
            while (!hijos.isEmpty()) {
            Position<TNodo<E>> posHijo = hijos.first();
            TNodo<E> hijo = posHijo.element();

           hijos.remove(posHijo);
           hijo.setPadre(padre);
           lista.addBefore(posNodo, hijo);

        }
        lista.remove(posNodo);
    }
        cant--;
    }
    public void removeNode(Position<E> v){
        if(cant==0){
            throw new InvalidPositionException("arbol vacio");
        }
        TNodo<E> nodo= checkPosition(v);
        if(nodo==raiz){
            if(nodo.getHijos().size()==1){

                Position<TNodo<E>> rN=raiz.getHijos().first();
				rN.element().setPadre(null);
				raiz.getHijos().remove(rN);
				raiz= rN.element();
            }else if(cant==1){
                raiz=null;
            }else{
                throw new InvalidPositionException("raiz con mas de un hijo");
            }
        }else{
                  TNodo<E> padre= nodo.getPadre();
                   PositionList<TNodo<E>> lista= padre.getHijos();
                   Position<TNodo<E>> posicion= null;
                  for(Position<TNodo<E>> p : lista.positions()){
                       if(p.element()==nodo){
                           posicion=p;
                           break;
                        }
                    }
                    if (posicion == null) {
                      throw new InvalidPositionException("no se encontro la posicion del nodo");
                    }
                    PositionList<TNodo<E>> hijos= nodo.getHijos();
                    while (!hijos.isEmpty()) {
                         TNodo<E> nod = hijos.first().element();
                         lista.addBefore(posicion, nod);
                         nod.setPadre(padre);
                        hijos.remove(hijos.first());
                    }
              
                  lista.remove(posicion);
                }
                  cant--;

    }
    public int sizeSubArbol(Position<E> v){
        if(cant==0){
            throw new InvalidPositionException("arbol vacio");
        }
        TNodo<E> nodo= checkPosition(v);
        PositionList<TNodo<E>> lista= nodo.getHijos();
        int cantSub=1;
         for(TNodo<E> hijo : lista){
            cantSub+= sizeSubArbol(hijo);
         }
         return cantSub;
   }
    public Map<Position<E>,Integer> mapSizeSubArboles(){
       Map<Position<E>, Integer> mapa= new MapeoHashAbierto<Position<E>,Integer>();

       if (!isEmpty()) {
           mapSizeSubarboles(raiz, mapa);
        }

       return mapa;
    }

    private void mapSizeSubarboles(TNodo<E> nodo, Map<Position<E>, Integer> mapa)
        throws InvalidPositionException {

    mapa.put(nodo, sizeSubArbol(nodo));

        for (TNodo<E> hijo : nodo.getHijos()) {
                mapSizeSubarboles(hijo, mapa);
        }
    }
    public int podarSubArbol(Position<E> v){
        TNodo<E> n= checkPosition(v);
        int eliminados= sizeSubArbol(v);
        if(n==raiz){
            raiz=null;
            cant=0;
        }else {
        TNodo<E> padre= n.getPadre();
        PositionList<TNodo<E>> hijos= padre.getHijos();
        for(Position<TNodo<E>> pos : hijos.positions()){
            if(pos.element()==n){
                hijos.remove(pos);
                break;
            }
        }
        cant-= eliminados;
    } return eliminados;
        
    }
    public Arbol<E> clone(){
        Arbol<E> nuevo= new Arbol<E>();
        if(!isEmpty()){
            nuevo.createRoot(raiz.element());
           copiar(raiz, nuevo.raiz, nuevo);


        }
        return nuevo;
    }
    private void copiar(TNodo<E> original, TNodo<E> copia, Arbol<E> nuevo){
        PositionList<TNodo<E>> hijos= original.getHijos();
        for(Position<TNodo<E>> pos: hijos.positions()){
            TNodo<E> hijoO= pos.element();
            TNodo<E> cop= new TNodo<E>(copia, hijoO.element());
            copia.getHijos().addLast(cop);
            nuevo.cant++;
            copiar(hijoO, cop, nuevo);
        }

    }

        

    }

       

    
   

    


