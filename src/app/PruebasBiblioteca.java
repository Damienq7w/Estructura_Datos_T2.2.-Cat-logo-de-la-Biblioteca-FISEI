package app;

import enums.TipoRecorrido;
import enums.TipoUnidad;
import estructuras.ListaEnlazada;
import modelo.Libro;
import modelo.UnidadBiblioteca;
import negocio.ArbolBiblioteca;
import negocio.CatalogoABB;
import negocio.NodoLibro;

/**
 * Casos de prueba obligatorios del deber (punto 5) y casos adicionales.
 * Muestra "X/N pruebas OK" al final.
 */
public class PruebasBiblioteca {

    private static int total = 0;
    private static int correctas = 0;

    public static void main(String[] args) {
        System.out.println("===== PRUEBAS BIBLIOTECA FISEI =====");

        // 1. Biblioteca solo con la raiz
        ArbolBiblioteca soloRaiz = DatosPrueba.crearSoloRaiz();
        verificar("Solo raiz: altura 0 y 1 hoja",
                soloRaiz.altura() == 0 && soloRaiz.contarHojas() == 1,
                "altura=" + soloRaiz.altura() + " hojas=" + soloRaiz.contarHojas());

        // 2. Biblioteca inicial
        ArbolBiblioteca b = DatosPrueba.crearInicial();
        verificar("Inicial: altura 2, 4 hojas y 13 libros",
                b.altura() == 2 && b.contarHojas() == 4 && b.contarLibrosTotal() == 13,
                "altura=" + b.altura() + " hojas=" + b.contarHojas() + " libros=" + b.contarLibrosTotal());

        // 3. Niveles
        int n1 = b.nivel("BIB");
        int n2 = b.nivel("sw");
        int n3 = b.nivel("SW-E2");
        int n4 = b.nivel("XYZ");
        verificar("Nivel de BIB, sw, SW-E2 y XYZ: 0, 1, 2 y -1",
                n1 == 0 && n2 == 1 && n3 == 2 && n4 == -1,
                n1 + ", " + n2 + ", " + n3 + ", " + n4);

        // 4. ESTANTE como hijo de BIBLIOTECA
        boolean agregado = b.agregar("BIB", new UnidadBiblioteca("ERR", "Estante mal ubicado", TipoUnidad.ESTANTE));
        verificar("Agregar un ESTANTE como hijo de BIBLIOTECA: false", !agregado, String.valueOf(agregado));

        // 5. Recorridos de SW-E1
        CatalogoABB swE1 = b.buscar("SW-E1").getCatalogo();
        String pre = Main.codigos(swE1.recorrer(TipoRecorrido.PREORDEN));
        String in = Main.codigos(swE1.recorrer(TipoRecorrido.INORDEN));
        String pos = Main.codigos(swE1.recorrer(TipoRecorrido.POSORDEN));
        verificar("Recorridos de SW-E1 (pre, in y pos)",
                pre.equals("60 35 20 45 40 80 70 75 90")
                        && in.equals("20 35 40 45 60 70 75 80 90")
                        && pos.equals("20 40 45 35 75 70 90 80 60"),
                "pre=" + pre + " | in=" + in + " | pos=" + pos);

        // 6. Codigo repetido
        boolean repetido = swE1.insertar(new Libro(60, "Repetido", "Autor", 2020));
        verificar("Insertar un codigo repetido: false", !repetido, String.valueOf(repetido));

        // 7. Camino de busqueda y rango
        ListaEnlazada<Integer> camino = swE1.caminoBusqueda(42);
        int rango = swE1.contarEnRango(40, 75);
        verificar("caminoBusqueda(42) = [60, 35, 45, 40] y contarEnRango(40, 75) = 5",
                camino.toString().equals("[60, 35, 45, 40]") && rango == 5,
                "camino=" + camino + " rango=" + rango);

        // 8. Insertar 42 y eliminar 20, 70 y 60
        swE1.insertar(new Libro(42, "Bases de la recursion", "Autor FISEI", 2024));
        swE1.eliminar(20);   // hoja
        swE1.eliminar(70);   // un hijo
        swE1.eliminar(60);   // dos hijos -> sube el sucesor inorden 75
        String preFinal = Main.codigos(swE1.recorrer(TipoRecorrido.PREORDEN));
        verificar("Insertar 42 y eliminar 20, 70 y 60: preorden 75 35 45 40 42 80 90",
                preFinal.equals("75 35 45 40 42 80 90") && swE1.esABBValido(),
                preFinal);

        // 9. Codigo inexistente
        Libro buscado = swE1.buscar(999);
        boolean modificado = swE1.modificar(999, "X", "Y", 2020);
        boolean eliminado = swE1.eliminar(999);
        verificar("Buscar, modificar y eliminar inexistente: null / false / false",
                buscado == null && !modificado && !eliminado,
                buscado + " / " + modificado + " / " + eliminado);

        // 10. Datos invalidos
        boolean codigoInvalido = lanzaExcepcion(() -> new Libro(0, "Titulo", "Autor", 2020));
        boolean anioInvalido = lanzaExcepcion(() -> new Libro(5, "Titulo", "Autor", 1899));
        boolean tituloInvalido = lanzaExcepcion(() -> new Libro(5, "  ", "Autor", 2020));
        verificar("Libro con datos invalidos: IllegalArgumentException",
                codigoInvalido && anioInvalido && tituloInvalido,
                "codigo 0=" + codigoInvalido + " anio 1899=" + anioInvalido + " titulo vacio=" + tituloInvalido);

        // 11. Nieto mal ubicado: 55 queda en el subarbol izquierdo de 50
        CatalogoABB malo = new CatalogoABB();
        NodoLibro r = new NodoLibro(new Libro(50, "A", "A", 2020));
        NodoLibro izq = new NodoLibro(new Libro(30, "B", "B", 2020));
        izq.setDerecho(new NodoLibro(new Libro(55, "C", "C", 2020)));
        r.setIzquierdo(izq);
        r.setDerecho(new NodoLibro(new Libro(70, "D", "D", 2020)));
        malo.setRaiz(r);
        verificar("esABBValido() con un nieto mal ubicado: false",
                !malo.esABBValido(), String.valueOf(malo.esABBValido()));

        // ------------------------------------------------ casos adicionales

        // 12. Busqueda de unidades sin distinguir mayusculas y codigo repetido
        ArbolBiblioteca b2 = DatosPrueba.crearInicial();
        boolean encontrada = b2.buscar("sw-e1") != null;
        boolean repetida = b2.agregar("BIB", new UnidadBiblioteca("sw", "Otra", TipoUnidad.AREA));
        verificar("buscar(\"sw-e1\") existe y agregar codigo repetido \"sw\": false",
                encontrada && !repetida, "encontrada=" + encontrada + " agregada=" + repetida);

        // 13. Modificar un libro existente sin cambiar el codigo
        CatalogoABB swE2 = b2.buscar("SW-E2").getCatalogo();
        boolean mod = swE2.modificar(15, "Bases de datos", "Silberschatz", 2019);
        Libro l15 = swE2.buscar(15);
        verificar("modificar(15, ...) cambia titulo, autor y anio",
                mod && l15.getTitulo().equals("Bases de datos") && l15.getAnio() == 2019,
                String.valueOf(l15));

        // 14. Formato de la traza
        String traza = b2.buscar("TI-E1").getCatalogo().trazaRecorrido(TipoRecorrido.INORDEN);
        String esperada = "entra 50\n  null -> regresa\nVISITA 50\n  null -> regresa\nSecuencia INORDEN: 50";
        verificar("trazaRecorrido de TI-E1 con el formato pedido",
                traza.equals(esperada), traza.replace("\n", " | "));

        // 15. Lista enlazada propia
        ListaEnlazada<Integer> lista = new ListaEnlazada<>();
        lista.insertar(60);
        lista.insertar(35);
        lista.insertarAlInicio(10);
        boolean eliminoCola = lista.eliminar(35);
        verificar("ListaEnlazada: insertar, insertarAlInicio, eliminar la cola y obtener",
                lista.toString().equals("[10, 60]") && eliminoCola && lista.obtenerUltimo() == 60 && lista.obtener(1) == 60,
                lista + " tamanio=" + lista.tamanio());

        // 16. Extra: desdePreorden reconstruye el mismo ABB
        CatalogoABB rec = CatalogoABB.desdePreorden(new int[]{60, 35, 20, 45, 40, 80, 70, 75, 90});
        String preRec = Main.codigos(rec.recorrer(TipoRecorrido.PREORDEN));
        String posRec = Main.codigos(rec.recorrer(TipoRecorrido.POSORDEN));
        verificar("Extra desdePreorden: mismo preorden y posorden de SW-E1",
                preRec.equals("60 35 20 45 40 80 70 75 90") && posRec.equals("20 40 45 35 75 70 90 80 60"),
                "pre=" + preRec + " | pos=" + posRec);

        System.out.println("------------------------------------");
        System.out.println(correctas + "/" + total + " pruebas OK");
    }

    private static void verificar(String nombre, boolean condicion, String obtenido) {
        total++;
        if (condicion) {
            correctas++;
        }
        System.out.printf("%2d. [%s] %s%n      -> %s%n", total, condicion ? "OK" : "FALLA", nombre, obtenido);
    }

    private static boolean lanzaExcepcion(Runnable accion) {
        try {
            accion.run();
            return false;
        } catch (IllegalArgumentException e) {
            return true;
        }
    }
}
