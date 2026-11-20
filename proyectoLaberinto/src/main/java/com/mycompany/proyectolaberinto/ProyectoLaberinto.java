package com.mycompany.proyectolaberinto;
import java.io.IOException;
import java.util.Random;
import java.util.Scanner;

//Programa principal con menú de consola.
//Genera un laberinto aleatorio con solución y encuentra la ruta de costo mínimo.
public class ProyectoLaberinto {
    private static final Scanner entrada = new Scanner(System.in);
    private static final Random azar = new Random();

    private static Laberinto laberinto;
    private static Celda inicio;
    private static Celda fin;
    private static Resultado ultimoResultado;

    public static void main(String[] args) {
        int opcion;
        do {
            mostrarMenu();
            opcion = leerEntero("Opción", 0, 7, 0);
            switch (opcion) {
                case 1: opcionGenerar(); break;
                case 2: opcionElegirManual(); break;
                case 3: opcionElegirAlAzar(); break;
                case 4: opcionResolver(); break;
                case 5: opcionComparar(); break;
                case 6: opcionMostrar(); break;
                case 7: opcionGuardarImagen(); break;
                default: break;
            }
        } while (opcion != 0);
        System.out.println("Fin del programa.");
    }

    private static void mostrarMenu() {
        System.out.println("\n------------------------- LABERINTO: RUTA DE COSTO MÍNIMO -------------------------");
        System.out.println("1. Generar laberinto aleatorio");
        System.out.println("2. Elegir inicio y fin manualmente");
        System.out.println("3. Elegir inicio y fin al azar");
        System.out.println("4. Resolver (Dijkstra / A* / BFS)");
        System.out.println("5. Comparar los tres algoritmos");
        System.out.println("6. Mostrar laberinto");
        System.out.println("7. Guardar imagen PNG del último resultado");
        System.out.println("0. Salir");
    }

    // ------------------------------------------------------------ opciones

    private static void opcionGenerar() {
        int filas = leerEntero("Filas (mínimo 15)", 15, 101, 21);
        int columnas = leerEntero("Columnas (mínimo 15)", 15, 101, 21);
        long semilla = leerEntero("Semilla (0 = aleatoria)", 0, Integer.MAX_VALUE, 0);
        if (semilla == 0) semilla = System.nanoTime();
        int alg = leerEntero("Generador: 1 = Prim, 2 = Kruskal", 1, 2, 1);
        int rep = leerEntero("Representación: 1 = Matriz, 2 = Grafo", 1, 2, 1);
        int extra = leerEntero("Porcentaje de paredes extra a abrir (0-30)", 0, 30, 8);
        int costoMax = leerEntero("Costo máximo por celda (1-9)", 1, 9, 9);

        laberinto = FabricaLaberinto.crearAleatorio(filas, columnas, semilla,
                alg == 1 ? AlgoritmoGeneracion.PRIM : AlgoritmoGeneracion.KRUSKAL,
                rep == 1 ? TipoRepresentacion.MATRIZ : TipoRepresentacion.GRAFO,
                extra / 100.0, costoMax);

        System.out.println("Laberinto generado y verificado (conexo): " + laberinto.getFilas() + "x"
                + laberinto.getColumnas() + ", representación " + laberinto.getNombreRepresentacion()
                + ", semilla " + semilla);
        Celda[] par = SelectorPuntos.elegirAlAzar(laberinto, azar, (filas + columnas) / 3);
        inicio = par[0];
        fin = par[1];
        ultimoResultado = null;
        System.out.println("Inicio: " + inicio + "  Fin: " + fin + " (elegidos al azar)");
    }

    private static boolean hayLaberinto() {
        if (laberinto == null) {
            System.out.println("Primero genera un laberinto (opción 1).");
            return false;
        }
        return true;
    }

    private static void opcionElegirManual() {
        if (!hayLaberinto()) return;
        int fi = leerEntero("Fila de inicio", 0, laberinto.getFilas() - 1, 1);
        int ci = leerEntero("Columna de inicio", 0, laberinto.getColumnas() - 1, 1);
        int ff = leerEntero("Fila de fin", 0, laberinto.getFilas() - 1, laberinto.getFilas() - 2);
        int cf = leerEntero("Columna de fin", 0, laberinto.getColumnas() - 1, laberinto.getColumnas() - 2);
        Celda pedidoInicio = new Celda(fi, ci);
        Celda pedidoFin = new Celda(ff, cf);
        Celda[] par = SelectorPuntos.validarOReubicar(laberinto, pedidoInicio, pedidoFin, azar);
        if (!par[0].equals(pedidoInicio)) System.out.println("El inicio no era una celda libre; se reubicó.");
        if (!par[1].equals(pedidoFin)) System.out.println("El fin no era válido o alcanzable; se reubicó.");
        inicio = par[0];
        fin = par[1];
        ultimoResultado = null;
        System.out.println("Inicio: " + inicio + "  Fin: " + fin);
    }

    private static void opcionElegirAlAzar() {
        if (!hayLaberinto()) return;
        Celda[] par = SelectorPuntos.elegirAlAzar(laberinto, azar,
                (laberinto.getFilas() + laberinto.getColumnas()) / 3);
        inicio = par[0];
        fin = par[1];
        ultimoResultado = null;
        System.out.println("Inicio: " + inicio + "  Fin: " + fin);
    }

    private static void opcionResolver() {
        if (!hayLaberinto()) return;
        int alg = leerEntero("Algoritmo: 1 = Dijkstra, 2 = A*, 3 = BFS", 1, 3, 2);
        BuscadorRuta buscador = crearBuscador(alg);
        ultimoResultado = buscador.buscar(laberinto, inicio, fin);
        imprimirResultado(ultimoResultado);
        int verVisitadas = leerEntero("¿Mostrar celdas visitadas? 1 = sí, 0 = no", 0, 1, 1);
        Visualizador.imprimir(laberinto, inicio, fin, ultimoResultado, verVisitadas == 1, false);
    }

    private static void opcionComparar() {
        if (!hayLaberinto()) return;
        System.out.println("\nInicio " + inicio + " -> Fin " + fin);
        for (int alg = 1; alg <= 3; alg++) {
            BuscadorRuta buscador = crearBuscador(alg);
            buscador.buscar(laberinto, inicio, fin); // calentamiento de la JVM
            Resultado r = buscador.buscar(laberinto, inicio, fin);
            System.out.println(r);
            if (alg == 2) ultimoResultado = r;
        }
        System.out.println("Nota: BFS minimiza pasos, no costo; Dijkstra y A* minimizan costo.");
    }

    private static void opcionMostrar() {
        if (!hayLaberinto()) return;
        Visualizador.imprimir(laberinto, inicio, fin, ultimoResultado, false, ultimoResultado == null);
    }

    private static void opcionGuardarImagen() {
        if (ultimoResultado == null) {
            System.out.println("Primero resuelve el laberinto (opción 4 o 5).");
            return;
        }
        try {
            ExportadorImagen.guardar(laberinto, inicio, fin, ultimoResultado, "laberinto.png", 24);
            System.out.println("Imagen guardada en laberinto.png");
        } catch (IOException e) {
            System.out.println("No se pudo guardar la imagen: " + e.getMessage());
        }
    }

    // ------------------------------------------------------------- apoyo

    public static BuscadorRuta crearBuscador(int opcion) {
        if (opcion == 1) return new Dijkstra();
        if (opcion == 3) return new BusquedaAnchura();
        return new AEstrella();
    }

    private static void imprimirResultado(Resultado r) {
        System.out.println("\nAlgoritmo: " + r.getAlgoritmo());
        if (!r.isEncontrada()) {
            System.out.println("No se encontró ruta.");
            return;
        }
        System.out.println("Costo total: " + r.getCostoTotal() + " | Pasos: " + r.getLongitud()
                + " | Celdas exploradas: " + r.getCeldasExploradas()
                + String.format(" | Tiempo: %.3f ms", r.getTiempoMilisegundos()));
        System.out.println("Ruta: " + r.getRuta());
    }

    private static int leerEntero(String mensaje, int min, int max, int porDefecto) {
        while (true) {
            System.out.print(mensaje + " [" + porDefecto + "]: ");
            String linea = entrada.hasNextLine() ? entrada.nextLine().trim() : "";
            if (linea.isEmpty()) return porDefecto;
            try {
                int valor = Integer.parseInt(linea);
                if (valor >= min && valor <= max) return valor;
            } catch (NumberFormatException e) {
                // se vuelve a pedir
            }
            System.out.println("Valor inválido. Debe estar entre " + min + " y " + max + ".");
        }
    }
}
