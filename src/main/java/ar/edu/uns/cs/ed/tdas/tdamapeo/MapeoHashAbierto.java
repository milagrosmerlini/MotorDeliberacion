package ar.edu.uns.cs.ed.tdas.tdamapeo;

import ar.edu.uns.cs.ed.tdas.Position;
import ar.edu.uns.cs.ed.tdas.Entry;
import ar.edu.uns.cs.ed.tdas.tdalista.PositionList;
import ar.edu.uns.cs.ed.tdas.tdalista.TdaLista;
import ar.edu.uns.cs.ed.tdas.excepciones.InvalidKeyException;

public class MapeoHashAbierto<K,V> implements Map<K,V>{
    private int N;
    private int n;
    private PositionList<Entrada<K,V>> [] A;
    private static final double FC = 0.7;
    
    public MapeoHashAbierto(){
        N=11;
        n=0;
        A= (PositionList<Entrada<K,V>>[]) new PositionList[N];//preguntar
        for(int i=0; i<A.length; i++){
            A[i]= new TdaLista<Entrada<K,V>>();

        }

    }
    public V get(K key){
        checkKey(key);
        int i = Math.floorMod(key.hashCode(), N);
        PositionList<Entrada<K,V>> l= A[i];
        V toRet= null;
        for(Entrada<K,V> e : l){
            if(e.getKey().equals(key)){
                toRet= e.getValue();
                break;}
        }

        return toRet;
    }
    
    public V put(K key, V value) {
       checkKey(key);
       int i = Math.floorMod(key.hashCode(), N);
       PositionList<Entrada<K,V>> l= A[i];
       for (Entrada<K,V> e : l){
           if (e.getKey().equals(key)){
              V toRet= e.getValue();
              e.setValue(value);
              return toRet;
            }
        }
        Entrada<K,V> p= new Entrada<K,V>(key, value);
        l.addLast(p);
        n++;
        return null;
    }
    public int size(){
        return n;
    }
    public boolean isEmpty(){
        return n==0;
    }
    public V remove(K key){
        checkKey(key);

    
        int i = Math.floorMod(key.hashCode(), N);
        PositionList<Entrada<K,V>> l= A[i];
        for (Position<Entrada<K, V>> e: l.positions()){
            Entrada<K,V> p= e.element();
            if(p.getKey().equals(key)){
                V toRet= p.getValue();
                l.remove(e);
                n--;
                return toRet;
            }
        }
        return null;
        
    }
    private void checkKey(K key){
        if(key==null){
            throw new InvalidKeyException("clave nula");
        }
    }
    public Iterable<K> keys(){
        PositionList<K> claves= new TdaLista<>();

        for (int i=0; i<N; i++){
            PositionList<Entrada<K,V>> l= A[i];
            for (Entrada<K,V> e : l){
                claves.addLast(e.getKey());
            }
        }
        return claves;
    }
    public Iterable<V> values(){
        PositionList<V> valores= new TdaLista<>();
        for (int i=0; i<N; i++){
            PositionList<Entrada<K,V>> l= A[i];
            for(Entrada<K,V> e: l){
                valores.addLast(e.getValue());
            }
        }
        return valores;
    }
    public Iterable<Entry<K,V>> entries(){
        PositionList<Entry<K,V>> entrada= new TdaLista<>();
        for(int i=0; i<N; i++){
            PositionList<Entrada<K,V>> l= A[i];
            for (Entrada<K,V> e : l){
                entrada.addLast(e);

            }
        }
        return entrada;
    }
}
