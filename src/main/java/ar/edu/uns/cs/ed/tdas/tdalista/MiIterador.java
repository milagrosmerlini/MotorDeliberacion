package ar.edu.uns.cs.ed.tdas.tdalista;

import java.util.Iterator;
import java.util.NoSuchElementException;

import ar.edu.uns.cs.ed.tdas.Position;
import ar.edu.uns.cs.ed.tdas.excepciones.EmptyListException;
import ar.edu.uns.cs.ed.tdas.excepciones.InvalidPositionException;

public class MiIterador<E> implements Iterator<E> {

    protected PositionList<E> lista;
    protected Position<E> cursor;

    public MiIterador(PositionList<E> l) {

        lista = l;

        if (lista.isEmpty()) {
            cursor = null;
        } else {
            try {
                cursor = lista.first();

            } catch (EmptyListException e) {
                e.printStackTrace();
            }
        }
    }

    public boolean hasNext() {
        return cursor != null;
    }

    public E next() throws NoSuchElementException {

        if (cursor == null) {
            throw new NoSuchElementException("no existe siguiente elemento");
        }

        E toReturn = cursor.element();

        try {

            if (cursor == lista.last()) {
                cursor = null;
            } else {
                cursor = lista.next(cursor);
            }

        } catch (InvalidPositionException e) {
            e.printStackTrace();
        } catch (EmptyListException e) {
            e.printStackTrace();
        }

        return toReturn;
    }
}