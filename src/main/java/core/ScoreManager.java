package core;

import java.io.*;
import java.util.*;

/**
 * Gestiona y persiste los top-3 puntajes de cada juego.
 */
public class ScoreManager {

    private static final String ARCHIVO = "scores.dat";
    private Map<String, List<Integer>> scores = new HashMap<>();

    public ScoreManager() {
        cargarDesdeArchivo();
    }

    public void guardarPuntaje(String juego, int puntaje) {
        scores.putIfAbsent(juego, new ArrayList<>());
        scores.get(juego).add(puntaje);
        scores.get(juego).sort(Collections.reverseOrder());

        if (scores.get(juego).size() > 3) {
            scores.get(juego).subList(3, scores.get(juego).size()).clear();
        }

        guardarEnArchivo();
    }

    public List<Integer> getTop3(String juego) {
        return scores.getOrDefault(juego, new ArrayList<>());
    }

    public Map<String, List<Integer>> getTodos() {
        return Collections.unmodifiableMap(scores);
    }

    @SuppressWarnings("unchecked")
    private void cargarDesdeArchivo() {
        File f = new File(ARCHIVO);
        if (!f.exists()) return;

        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(f))) {
            scores = (Map<String, List<Integer>>) ois.readObject();
        } catch (Exception e) {
            System.err.println("No se pudo cargar historial: " + e.getMessage());
            scores = new HashMap<>();
        }
    }

    private void guardarEnArchivo() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(ARCHIVO))) {
            oos.writeObject(scores);
        } catch (IOException e) {
            System.err.println("No se pudo guardar puntajes: " + e.getMessage());
        }
    }
}
