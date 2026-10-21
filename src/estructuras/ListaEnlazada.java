package estructuras;

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * Lista enlazada simple propia (no se usa java.util.List).
 * Guarda cabeza, cola y tamanio para que insertar al final sea O(1).
 * Implementa Iterable para poder recorrerla con for-each en O(n).
 */
public class ListaEnlazada<T> implements Iterable<T> {

    private Nodo<T> cabeza;
    private Nodo<T> cola;
    private int tamanio;

    public ListaEnlazada() {
        cabeza = null;
        cola = null;
        tamanio = 0;
    }

    /** Inserta al final. O(1) gracias a la referencia a la cola. */
    public void insertar(T dato) {
        Nodo<T> nuevo = new Nodo<>(dato);
        if (estaVacia()) {
            cabeza = nuevo;
            cola = nuevo;
        } else {
            cola.setSiguiente(nuevo);
            cola = nuevo;
        }
        tamanio++;
    }

    /** Inserta al inicio. O(1). */
    public void insertarAlInicio(T dato) {
        Nodo<T> nuevo = new Nodo<>(dato);
        nuevo.setSiguiente(cabeza);
        cabeza = nuevo;
        if (cola == null) {
            cola = nuevo;
        }
        tamanio++;
    }

    /** Devuelve el dato de la posición indicada (desde 0). O(n): camina desde la cabeza. */
    public T obtener(int indice) {
        if (indice < 0 || indice >= tamanio) {
            throw new IndexOutOfBoundsException("Índice fuera de rango: " + indice);
        }
        Nodo<T> actual = cabeza;
        for (int i = 0; i < indice; i++) {
            actual = actual.getSiguiente();
        }
        return actual.getDato();
    }

    /** Primer dato de la lista, o null si está vacía. O(1). */
    public T obtenerPrimero() {
        return cabeza == null ? null : cabeza.getDato();
    }

    /** Último dato de la lista, o null si está vacía. O(1). */
    public T obtenerUltimo() {
        return cola == null ? null : cola.getDato();
    }

    /** Indica si el dato está en la lista (usa equals). O(n). */
    public boolean contiene(T dato) {
        for (T elemento : this) {
            if (elemento == null ? dato == null : elemento.equals(dato)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Elimina la primera aparición del dato con dos punteros (anterior y actual).
     * Actualiza cabeza y cola si hace falta. O(n).
     */
    public boolean eliminar(T dato) {
        Nodo<T> anterior = null;
        Nodo<T> actual = cabeza;
        while (actual != null) {
            T valor = actual.getDato();
            if (valor == null ? dato == null : valor.equals(dato)) {
                if (anterior == null) {
                    cabeza = actual.getSiguiente();
                } else {
                    anterior.setSiguiente(actual.getSiguiente());
                }
                if (actual == cola) {
                    cola = anterior;
                }
                tamanio--;
                return true;
            }
            anterior = actual;
            actual = actual.getSiguiente();
        }
        return false;
    }

    public int tamanio() {
        return tamanio;
    }

    public boolean estaVacia() {
        return tamanio == 0;
    }

    /** Iterador con clase anónima: recuerda en que nodo va. O(1) por paso. */
    @Override
    public Iterator<T> iterator() {
        return new Iterator<T>() {
            private Nodo<T> actual = cabeza;

            @Override
            public boolean hasNext() {
                return actual != null;
            }

            @Override
            public T next() {
                if (actual == null) {
                    throw new NoSuchElementException();
                }
                T dato = actual.getDato();
                actual = actual.getSiguiente();
                return dato;
            }
        };
    }

    /** Formato [a, b, c]. */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("[");
        Nodo<T> actual = cabeza;
        while (actual != null) {
            sb.append(actual.getDato());
            if (actual.getSiguiente() != null) {
                sb.append(", ");
            }
            actual = actual.getSiguiente();
        }
        return sb.append("]").toString();
    }
}
