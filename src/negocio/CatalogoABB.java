package negocio;

import enums.TipoRecorrido;
import estructuras.ListaEnlazada;
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

    // =============================================================== recorrer

    /** Recorrido en preorden, inorden o posorden. Devuelve la lista enlazada propia. */
    public ListaEnlazada<Libro> recorrer(TipoRecorrido tipo) {
        if (tipo == null) {
            throw new IllegalArgumentException("El tipo de recorrido es obligatorio.");
        }
        ListaEnlazada<Libro> resultado = new ListaEnlazada<>();
        recorrer(raiz, tipo, resultado);
        return resultado;
    }

    private void recorrer(NodoLibro nodo, TipoRecorrido tipo, ListaEnlazada<Libro> resultado) {
        if (nodo == null) {
            return;                                        // caso base
        }
        if (tipo == TipoRecorrido.PREORDEN) {
            resultado.insertar(nodo.getDato());            // nodo - izq - der
        }
        recorrer(nodo.getIzquierdo(), tipo, resultado);
        if (tipo == TipoRecorrido.INORDEN) {
            resultado.insertar(nodo.getDato());            // izq - nodo - der
        }
        recorrer(nodo.getDerecho(), tipo, resultado);
        if (tipo == TipoRecorrido.POSORDEN) {
            resultado.insertar(nodo.getDato());            // izq - der - nodo
        }
    }

    // ================================================================== traza

    /**
     * Traza del recorrido:
     * "entra X" al entrar al nodo, "VISITA X" al procesarlo y "null -> regresa" en un hijo vacío,
     * con 2 espacios por nivel. La última línea es "Secuencia TIPO: ...".
     */
    public String trazaRecorrido(TipoRecorrido tipo) {
        if (tipo == null) {
            throw new IllegalArgumentException("El tipo de recorrido es obligatorio.");
        }
        StringBuilder traza = new StringBuilder();
        StringBuilder secuencia = new StringBuilder();
        traza(raiz, tipo, 0, traza, secuencia);
        traza.append("Secuencia ").append(tipo).append(": ").append(secuencia.toString().trim());
        return traza.toString();
    }

    private void traza(NodoLibro nodo, TipoRecorrido tipo, int nivel,
                       StringBuilder traza, StringBuilder secuencia) {
        String sangria = "  ".repeat(nivel);
        if (nodo == null) {
            traza.append(sangria).append("null -> regresa\n");       // caso base
            return;
        }
        int codigo = nodo.getDato().getCodigo();
        traza.append(sangria).append("entra ").append(codigo).append('\n');

        if (tipo == TipoRecorrido.PREORDEN) {
            visitar(codigo, sangria, traza, secuencia);
        }
        traza(nodo.getIzquierdo(), tipo, nivel + 1, traza, secuencia);
        if (tipo == TipoRecorrido.INORDEN) {
            visitar(codigo, sangria, traza, secuencia);
        }
        traza(nodo.getDerecho(), tipo, nivel + 1, traza, secuencia);
        if (tipo == TipoRecorrido.POSORDEN) {
            visitar(codigo, sangria, traza, secuencia);
        }
    }

    private void visitar(int codigo, String sangria, StringBuilder traza, StringBuilder secuencia) {
        traza.append(sangria).append("VISITA ").append(codigo).append('\n');
        secuencia.append(codigo).append(' ');
    }

    // =============================================================== modificar

    /** Modifica título, autor y año (el código no cambia). Devuelve false si no existe. */
    public boolean modificar(int codigo, String titulo, String autor, int anio) {
        NodoLibro nodo = buscarNodo(raiz, codigo);
        if (nodo == null) {
            return false;
        }
        // Se validan los datos antes de cambiar nada, para no dejar el libro a medias.
        new Libro(codigo, titulo, autor, anio);
        nodo.getDato().setTitulo(titulo);
        nodo.getDato().setAutor(autor);
        nodo.getDato().setAnio(anio);
        return true;
    }

    // ================================================================ eliminar

    /**
     * Elimina un libro. Cubre los tres casos:
     * hoja, nodo con un hijo y nodo con dos hijos (se reemplaza por el sucesor inorden).
     */
    public boolean eliminar(int codigo) {
        if (buscarNodo(raiz, codigo) == null) {
            return false;
        }
        raiz = eliminar(raiz, codigo);
        return true;
    }

    private NodoLibro eliminar(NodoLibro nodo, int codigo) {
        if (nodo == null) {
            return null;
        }
        int actual = nodo.getDato().getCodigo();
        if (codigo < actual) {
            nodo.setIzquierdo(eliminar(nodo.getIzquierdo(), codigo));
        } else if (codigo > actual) {
            nodo.setDerecho(eliminar(nodo.getDerecho(), codigo));
        } else {
            // Caso 1 (hoja) y caso 2 (un hijo): sube el hijo que exista, o null.
            if (nodo.getIzquierdo() == null) {
                return nodo.getDerecho();
            }
            if (nodo.getDerecho() == null) {
                return nodo.getIzquierdo();
            }
            // Caso 3 (dos hijos): sucesor inorden = menor del subárbol derecho.
            Libro sucesor = minimo(nodo.getDerecho());
            nodo.setDato(sucesor);
            nodo.setDerecho(eliminar(nodo.getDerecho(), sucesor.getCodigo()));
        }
        return nodo;
    }

    private Libro minimo(NodoLibro nodo) {
        if (nodo.getIzquierdo() == null) {
            return nodo.getDato();                         // caso base: el más a la izquierda
        }
        return minimo(nodo.getIzquierdo());
    }
}
