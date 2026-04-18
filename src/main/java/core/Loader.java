package core;

import java.io.File;
import java.net.URL;
import java.net.URLClassLoader;

public class Loader {

    public static JuegoPlugin cargar(File jar) {
        try {
            URL[] urls = { jar.toURI().toURL() };
            // Pasar el classloader del sistema como padre para que vea las clases de core.*
            URLClassLoader loader = new URLClassLoader(urls, Loader.class.getClassLoader());
            Class<?> clase = loader.loadClass("plugin.Juego");
            return (JuegoPlugin) clase.getDeclaredConstructor().newInstance();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
