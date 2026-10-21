package negocio;

import modelo.Libro;

/**
 * Árbol Binario de Búsqueda (ABB) de libros ordenado por código.
 * Menores a la izquierda, mayores a la derecha y sin repetidos.
 * Todas las operaciones son recursivas.
 */
public class CatalogoABB {

    private NodoLibro raiz;

    public NodoLibro getRaiz() {
        return raiz;
    }

    public void setRaiz(NodoLibro raiz) {
        this.raiz = raiz;
    }

    // ================================================================ insertar

    /** Inserta un libro por su código. Devuelve false si el código ya existe. */
    public boolean insertar(Libro libro) {
        if (libro == null) {
            throw new IllegalArgumentException("El libro no puede ser null.");
        }
        if (buscarNodo(raiz, libro.getCodigo()) != null) {
            return false;
        }
        raiz = insertar(raiz, libro);
        return true;
    }

    private NodoLibro insertar(NodoLibro nodo, Libro libro) {
        if (nodo == null) {
            return new NodoLibro(libro);                   // caso base: lugar vacío
        }
        if (libro.getCodigo() < nodo.getDato().getCodigo()) {
            nodo.setIzquierdo(insertar(nodo.getIzquierdo(), libro));
        } else {
            nodo.setDerecho(insertar(nodo.getDerecho(), libro));
        }
        return nodo;
    }

    // ================================================================== buscar

    /** Busca un libro por código. Devuelve null si no existe. */
    public Libro buscar(int codigo) {
        NodoLibro nodo = buscarNodo(raiz, codigo);
        return nodo == null ? null : nodo.getDato();
    }

    private NodoLibro buscarNodo(NodoLibro nodo, int codigo) {
        if (nodo == null) {
            return null;                                   // caso base: no existe
        }
        int actual = nodo.getDato().getCodigo();
        if (codigo == actual) {
            return nodo;                                   // caso base: encontrado
        }
        return codigo < actual
                ? buscarNodo(nodo.getIzquierdo(), codigo)
                : buscarNodo(nodo.getDerecho(), codigo);
    }

    // ================================================================== contar

    /** Número de libros del catálogo. */
    public int contar() {
        return contar(raiz);
    }

    private int contar(NodoLibro nodo) {
        if (nodo == null) {
            return 0;
        }
        return 1 + contar(nodo.getIzquierdo()) + contar(nodo.getDerecho());
    }
}
