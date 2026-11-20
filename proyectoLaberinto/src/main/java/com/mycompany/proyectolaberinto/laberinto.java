import java.util.List;

//Capa de abstracción del laberinto.
//Los algoritmos de búsqueda SOLO usan esta interfaz, por lo que se puede
//cambiar la representación (matriz o grafo) sin modificar la búsqueda.
public interface Laberinto {
    int LIBRE = 0;
    int PARED = 1;

    int getFilas();

    int getColumnas();

//true si la fila o columna estan dentro del laberinto y no es pared
    boolean esLibre(int fila, int columna);

//Costo de entrar a la celda (0 si es pared)
    int getCosto(int fila, int columna);

//Celdas libres adyacentes (arriba, abajo, izquierda, derecha)
    List<Celda> getVecinos(Celda celda);

//Costo mínimo entre las celdas libres (se usa en la heurística de A*)
    int getCostoMinimo();

    String getNombreRepresentacion();

//Cuenta las celdas libres recorriendo todo el laberinto. 
    default int contarCeldasLibres() {
        int total = 0;
        for (int f = 0; f < getFilas(); f++) {
            for (int c = 0; c < getColumnas(); c++) {
                if (esLibre(f, c)) total++;
            }
        }
        return total;
    }
}
