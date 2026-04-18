package core;

import java.io.File;
import java.nio.file.*;
import java.util.function.Consumer;

/**
 * Monitorea la carpeta "juegos/" en un hilo separado.
 * Notifica cuando se agrega o elimina un JAR.
 */
public class JuegosWatcher implements Runnable {

    private final File carpeta;
    private final Consumer<File> onAgregado;
    private final Consumer<String> onEliminado; // nombre del archivo
    private volatile boolean activo = true;

    public JuegosWatcher(File carpeta, Consumer<File> onAgregado, Consumer<String> onEliminado) {
        this.carpeta     = carpeta;
        this.onAgregado  = onAgregado;
        this.onEliminado = onEliminado;
    }

    public void detener() { activo = false; }

    @Override
    public void run() {
        try {
            WatchService watcher = FileSystems.getDefault().newWatchService();
            carpeta.toPath().register(watcher,
                StandardWatchEventKinds.ENTRY_CREATE,
                StandardWatchEventKinds.ENTRY_DELETE
            );

            while (activo) {
                WatchKey key = watcher.poll(500, java.util.concurrent.TimeUnit.MILLISECONDS);
                if (key == null) continue;

                for (WatchEvent<?> event : key.pollEvents()) {
                    Path nombre = (Path) event.context();
                    String nombreStr = nombre.toString();

                    if (!nombreStr.endsWith(".jar")) continue;

                    if (event.kind() == StandardWatchEventKinds.ENTRY_CREATE) {
                        // Esperar un momento para que el archivo termine de copiarse
                        Thread.sleep(300);
                        File jar = new File(carpeta, nombreStr);
                        if (jar.exists()) onAgregado.accept(jar);
                    } else if (event.kind() == StandardWatchEventKinds.ENTRY_DELETE) {
                        onEliminado.accept(nombreStr);
                    }
                }

                key.reset();
            }

            watcher.close();
        } catch (Exception e) {
            System.err.println("JuegosWatcher error: " + e.getMessage());
        }
    }
}
