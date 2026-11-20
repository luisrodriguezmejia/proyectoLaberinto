import java.util.ArrayList;
import java.util.List;

//Representación del laberinto como grafo no dirigido con lista de adyacencias.
//Cada celda libre es un nodo con id = fila * columnas + columna.
//Existe una arista entre dos celdas libres adyacentes.
public class RepresentacionGrafo implements Laberinto {
    private static final int[] DF = {-1, 1, 0, 0};
    private static final int[] DC = {0, 0, -1, 1};

    private final int filas;
    private final int columnas;
    private final boolean[] libre;
    private final int[] costo;
    private final ArrayList<ArrayList<Integer>> adyacencia;
    private final int costoMinimo;

    public RepresentacionGrafo(int[][] celdas, int[][] costos) {
        this.filas = celdas.length;
        this.columnas = celdas[0].length;
        int total = filas * columnas;
        this.libre = new boolean[total];
        this.costo = new int[total];
        this.adyacencia = new ArrayList<>();

        int minimo = Integer.MAX_VALUE;
        for (int id = 0; id < total; id++) {
            adyacencia.add(new ArrayList<>());
            int f = id / columnas;
            int c = id % columnas;
            if (celdas[f][c] == LIBRE) {
                libre[id] = true;
                costo[id] = costos[f][c];
                if (costo[id] < minimo) minimo = costo[id];
            }
        }
        this.costoMinimo = (minimo == Integer.MAX_VALUE) ? 1 : minimo;

        // Construcción de las aristas
        for (int id = 0; id < total; id++) {
            if (!libre[id]) continue;
            int f = id / columnas;
            int c = id % columnas;
            for (int k = 0; k < 4; k++) {
                int nf = f + DF[k];
                int nc = c + DC[k];
                if (nf >= 0 && nf < filas && nc >= 0 && nc < columnas && libre[nf * columnas + nc]) {
                    adyacencia.get(id).add(nf * columnas + nc);
                }
            }
        }
    }

    @Override
    public int getFilas() {
        return filas;
    }

    @Override
    public int getColumnas() {
        return columnas;
    }

    @Override
    public boolean esLibre(int fila, int columna) {
        return fila >= 0 && fila < filas && columna >= 0 && columna < columnas
                && libre[fila * columnas + columna];
    }

    @Override
    public int getCosto(int fila, int columna) {
        return esLibre(fila, columna) ? costo[fila * columnas + columna] : 0;
    }

    @Override
    public List<Celda> getVecinos(Celda celda) {
        List<Celda> vecinos = new ArrayList<>();
        int id = celda.getFila() * columnas + celda.getColumna();
        for (int nodo : adyacencia.get(id)) {
            vecinos.add(new Celda(nodo / columnas, nodo % columnas));
        }
        return vecinos;
    }

    @Override
    public int getCostoMinimo() {
        return costoMinimo;
    }

    @Override
    public String getNombreRepresentacion() {
        return "Grafo (lista de adyacencias)";
    }

  //Número de aristas del grafo (cada arista se guarda dos veces).
    public int contarAristas() {
        int suma = 0;
        for (ArrayList<Integer> lista : adyacencia) {
            suma += lista.size();
        }
        return suma / 2;
    }
}
