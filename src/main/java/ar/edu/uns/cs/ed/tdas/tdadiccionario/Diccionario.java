package ar.edu.uns.cs.ed.tdas.tdadiccionario;

import ar.edu.uns.cs.ed.tdas.excepciones.InvalidEntryException;
import ar.edu.uns.cs.ed.tdas.tdalista.PositionList;
import ar.edu.uns.cs.ed.tdas.tdalista.TdaLista;
import ar.edu.uns.cs.ed.tdas.excepciones.InvalidKeyException;

import ar.edu.uns.cs.ed.tdas.Entry;
import ar.edu.uns.cs.ed.tdas.Position;
import ar.edu.uns.cs.ed.tdas.tdamapeo.Entrada;

public class Diccionario<K,V> implements Dictionary<K,V>{
    private int N;
    private int n;
    private static final double FC= 0.7;
    private PositionList<Entrada<K,V>> [] A;
    
    public Diccionario(){
        N=11;
        n=0;
        A= (PositionList<Entrada<K,V>>[]) new  PositionList[N];
        for(int i=0; i<N ;i++){
            A[i]= new TdaLista<Entrada<K,V>>();
        }
    }
    public int size(){
        return n;
    }
    public boolean isEmpty(){
        return n==0;
    }
    public Entry<K,V> find(K key){
        checkKey(key);
        int i= key.hashCode()% N;
        PositionList<Entrada<K,V>> l= A[i];
        for(Entrada<K,V> e: l){
            if(e.getKey().equals(key))
            return e;
         //se pone break o no es necesario
        }
        return null;

    }
    public Iterable<Entry<K,V>> findAll(K key){
        checkKey(key);
        int i= key.hashCode() % N;
        PositionList<Entrada<K,V>> l= A[i];
        PositionList<Entry<K,V>> entradas= new TdaLista<>();
        for(Entrada<K,V> e: l){
            if(e.getKey().equals(key)){
                entradas.addLast(e);
            }
        }
        return entradas;
    }
    public Entry<K,V> insert(K key, V value){
        checkKey(key);
        int i= key.hashCode() % N;
        PositionList<Entrada<K,V>> l= A[i];

        Entrada<K,V> e= new Entrada<K,V>(key, value);
        l.addLast(e);
        n++;

        return e;
    }
    public Entry<K,V> remove(Entry<K,V> e){
        if(e==null){
            throw new InvalidEntryException("entrada invalida");
        }
        int i= e.getKey().hashCode() % N;
        PositionList<Entrada<K,V>> l= A[i];
        for( Position<Entrada<K,V>> p : l.positions() ){
            Entrada<K,V> entrada= p.element();
            if(entrada.equals(e)){
                l.remove(p);
                n--;
                return entrada;
            }
        }
        throw new InvalidEntryException("entrada no encontrada");

    }
    public Iterable<Entry<K,V>> entries(){
        PositionList<Entry<K,V>> l= new TdaLista<>();
        for (int i=0; i<N; i++){
            for(Entrada<K,V> e : A[i]){
                l.addLast(e);

            }
        }
        return l;
    }
    private void checkKey(K key){
        if (key== null){
            throw new InvalidKeyException("Clave invalida");
        }
    }




    
}
