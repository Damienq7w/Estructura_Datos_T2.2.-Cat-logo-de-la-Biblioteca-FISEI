package negocio;

/**
 * Árbol Binario de Búsqueda (ABB) de libros ordenado por código.
 * Menores a la izquierda, mayores a la derecha y sin repetidos.
 * Las operaciones se implementan en la Parte C.
 */
public class CatalogoABB {

    private NodoLibro raiz;

    public NodoLibro getRaiz() {
        return raiz;
    }

    public void setRaiz(NodoLibro raiz) {
        this.raiz = raiz;
    }
}
