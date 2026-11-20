package com.mycompany.proyectolaberinto;

//Representa una posición en la fila o columna dentro del laberinto.
//Se usa como "nodo" en los algoritmos de búsqueda.
public class celda {
    private final int fila;
    private final int columna;

    public celda(int fila, int columna) {
        this.fila = fila;
        this.columna = columna;
    }

    public int getFila() {
        return fila;
    }

    public int getColumna() {
        return columna;
    }

//Distancia Manhattan entre esta celda y otra (df + dc)
    public int distanciaManhattan(Celda otra) {
        return Math.abs(fila - otra.fila) + Math.abs(columna - otra.columna);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof Celda)) return false;
        Celda otra = (Celda) obj;
        return fila == otra.fila && columna == otra.columna;
    }

    @Override
    public int hashCode() {
        return 31 * fila + columna;
    }

    @Override
    public String toString() {
        return "(" + fila + ", " + columna + ")";
    }
}
