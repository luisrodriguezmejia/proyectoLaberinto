import java.util.List;

//Resultado de una búsqueda: ruta, costo, celdas exploradas y tiempo medido.

public class Resultado {
    private final String algoritmo;
    private final boolean encontrada;
    private final List<Celda> ruta;
    private final int costoTotal;
    private final int celdasExploradas;
    private final long tiempoNanosegundos;
    private final boolean[][] visitadas;

    public Resultado(String algoritmo, boolean encontrada, List<Celda> ruta, int costoTotal,
                     int celdasExploradas, long tiempoNanosegundos, boolean[][] visitadas) {
        this.algoritmo = algoritmo;
        this.encontrada = encontrada;
        this.ruta = ruta;
        this.costoTotal = costoTotal;
        this.celdasExploradas = celdasExploradas;
        this.tiempoNanosegundos = tiempoNanosegundos;
        this.visitadas = visitadas;
    }

    public String getAlgoritmo() { return algoritmo; }

    public boolean isEncontrada() { return encontrada; }

    public List<Celda> getRuta() { return ruta; }

    //Suma de los costos de las celdas de la ruta, sin contar la celda de inicio.
    public int getCostoTotal() { return costoTotal; }

   // Número de pasos (movimientos) de la ruta. 
    public int getLongitud() { return ruta.isEmpty() ? 0 : ruta.size() - 1; }

    public int getCeldasExploradas() { return celdasExploradas; }

    public long getTiempoNanosegundos() { return tiempoNanosegundos; }

    public double getTiempoMilisegundos() { return tiempoNanosegundos / 1_000_000.0; }

    public boolean fueVisitada(int fila, int columna) { return visitadas[fila][columna]; }

    @Override
    public String toString() {
        return String.format("%-28s costo=%d  pasos=%d  exploradas=%d  tiempo=%.3f ms",
                algoritmo, costoTotal, getLongitud(), celdasExploradas, getTiempoMilisegundos());
    }
}
