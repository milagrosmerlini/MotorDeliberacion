package ar.edu.uns.cs.ed.tdas.tdamapeo;
import ar.edu.uns.cs.ed.tdas.Entry;

public class Entrada<K,V> implements Entry<K,V> {
    private K clave;
    private V valor;
     
    public Entrada(K key, V value){
        clave= key;
        valor= value;
    }
    public V getValue(){
        return valor;

    }
    public K getKey(){
        return clave;
    
    }
    public void setKey(K key){
        clave= key;


    }
    public void setValue(V value){
        valor=value;

    }
    public String toString(){
        return "("+ getKey() + "," + getValue() + ")";
    }
}
