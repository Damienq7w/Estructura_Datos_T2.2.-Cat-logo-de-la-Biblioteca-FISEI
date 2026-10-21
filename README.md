# Catálogo de la Biblioteca FISEI con árbol general y ABB en Java

**Asignatura:** Estructura de Datos · **Nivel:** Tercero – Ingeniería de Software
**Estudiante:** Damian Cunalata · **Modalidad:** Individual

## Problema

La Biblioteca FISEI se organiza en **Áreas** y cada Área en **Estantes**. Esta jerarquía se representa con un **árbol general n-ario**. Cada Estante guarda sus libros en un **Árbol Binario de Búsqueda (ABB)** ordenado por el código del libro.

La aplicación permite construir, consultar, recorrer y modificar las dos estructuras. Todas las operaciones de los árboles son recursivas.

En lugar de `java.util.List` se usa una **lista enlazada propia** (`Nodo<T>` y `ListaEnlazada<T>`).

## Organización

```
src/
├── estructuras/  Nodo.java, ListaEnlazada.java
├── enums/        TipoUnidad.java, TipoRecorrido.java
├── modelo/       Libro.java, UnidadBiblioteca.java
├── negocio/      NodoUnidad.java, NodoLibro.java, ArbolBiblioteca.java, CatalogoABB.java
└── app/          Main.java, DatosPrueba.java, PruebasBiblioteca.java
```

> Proyecto en desarrollo en la rama `desarrollo`.
