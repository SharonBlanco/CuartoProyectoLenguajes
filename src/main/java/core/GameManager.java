package core;

import java.util.*;

/**
 * Gestor central de juegos. Implementa Observer para recibir notificaciones.
 * Garantiza Singleton por nombre de juego.
 * Permite eliminar juegos en tiempo de ejecución.
 */
public class GameManager implements GameObserver {

    private Map<String, JuegoPlugin> juegos = new LinkedHashMap<>();
    private ScoreManager scoreManager = new ScoreManager();
    private List<GameObserver> observadoresUI = new ArrayList<>();

    public boolean agregarJuego(JuegoPlugin juego) {
        if (juegos.containsKey(juego.getNombre())) {
            System.out.println("Singleton: " + juego.getNombre() + " ya cargado.");
            return false;
        }
        juegos.put(juego.getNombre(), juego);
        juego.setObservador(this);
        return true;
    }

    /** Elimina el juego asociado a un nombre de archivo JAR */
    public void eliminarJuegoPorJar(String nombreJar) {
        // El nombre del JAR puede no coincidir exactamente con getNombre(),
        // así que eliminamos por el jar guardado en el mapa de jars
        jarsANombres.entrySet().removeIf(e -> {
            if (e.getKey().equals(nombreJar)) {
                String nombreJuego = e.getValue();
                juegos.remove(nombreJuego);
                System.out.println("Juego eliminado: " + nombreJuego);
                return true;
            }
            return false;
        });
    }

    // Mapa jar → nombre del juego para poder eliminar por nombre de archivo
    private Map<String, String> jarsANombres = new LinkedHashMap<>();

    public boolean agregarJuego(JuegoPlugin juego, String nombreJar) {
        if (!agregarJuego(juego)) return false;
        jarsANombres.put(nombreJar, juego.getNombre());
        return true;
    }

    public Collection<JuegoPlugin> getJuegos() {
        return juegos.values();
    }

    public ScoreManager getScoreManager() { return scoreManager; }

    public void addUIObserver(GameObserver obs) { observadoresUI.add(obs); }

    @Override
    public void juegoTerminado(String nombreJuego, int puntaje) {
        System.out.println("Juego terminado: " + nombreJuego + " -> " + puntaje);
        scoreManager.guardarPuntaje(nombreJuego, puntaje);
        for (GameObserver obs : observadoresUI) obs.juegoTerminado(nombreJuego, puntaje);
    }
}
