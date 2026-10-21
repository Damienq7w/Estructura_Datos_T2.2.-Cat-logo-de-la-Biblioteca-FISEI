package negocio;

import enums.TipoUnidad;
import estructuras.ListaEnlazada;
import modelo.UnidadBiblioteca;

/**
 * Nodo del arbol general n-ario.
 * Los hijos se guardan en la lista enlazada propia.
 * Solo los estantes tienen catalogo (ABB de libros).
 */
public class NodoUnidad {

    private UnidadBiblioteca dato;
    private final ListaEnlazada<NodoUnidad> hijos;
    private final CatalogoABB catalogo;

    public NodoUnidad(UnidadBiblioteca dato) {
        if (dato == null) {
            throw new IllegalArgumentException("La unidad no puede ser null.");
        }
        this.dato = dato;
        this.hijos = new ListaEnlazada<>();
        this.catalogo = dato.getTipo() == TipoUnidad.ESTANTE ? new CatalogoABB() : null;
    }

    public UnidadBiblioteca getDato() {
        return dato;
    }

    public void setDato(UnidadBiblioteca dato) {
        this.dato = dato;
    }

    public ListaEnlazada<NodoUnidad> getHijos() {
        return hijos;
    }

    /** Agrega un hijo al final de la lista. O(1). */
    public void agregarHijo(NodoUnidad hijo) {
        hijos.insertar(hijo);
    }

    public boolean esHoja() {
        return hijos.estaVacia();
    }

    /** Devuelve el catalogo del estante, o null si la unidad no es un ESTANTE. */
    public CatalogoABB getCatalogo() {
        return catalogo;
    }
}
