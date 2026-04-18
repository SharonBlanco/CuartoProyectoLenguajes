package ui;

import core.*;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.io.File;
import java.net.URISyntaxException;

/**
 * Ventana principal. Controla navegación entre menú, juegos y scores.
 * Monitorea la carpeta juegos/ automáticamente con JuegosWatcher.
 */
public class MainFrame extends JFrame implements GameObserver {

    private GameManager manager = new GameManager();
    private MenuPanel menuPanel;
    private ScoresPanel scoresPanel;
    private JuegosWatcher watcher;
    private Thread watcherThread;

    public MainFrame() {
        setTitle("★ GAME PLATFORM ★");
        setSize(800, 600);
        setMinimumSize(new Dimension(640, 480));
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setBackground(MenuPanel.BG);

        manager.addUIObserver(this);
        menuPanel   = new MenuPanel(manager, this);
        scoresPanel = new ScoresPanel(manager.getScoreManager());

        cargarJuegosIniciales();
        iniciarWatcher();
        mostrarMenu();

        // Detener el watcher al cerrar
        addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent e) {
                if (watcher != null) watcher.detener();
            }
        });
    }

    // ── Navegación ──────────────────────────────────────────────────

    public void mostrarMenu() {
        menuPanel.construirMenu();
        setContentPane(menuPanel);
        revalidate(); repaint();
    }

    public void mostrarScores() {
        scoresPanel.actualizar();
        JPanel wrap = new JPanel(new BorderLayout());
        wrap.setBackground(MenuPanel.BG);
        wrap.add(crearBarra(), BorderLayout.NORTH);
        wrap.add(scoresPanel, BorderLayout.CENTER);
        setContentPane(wrap);
        revalidate(); repaint();
    }

    public void abrirJuego(JuegoPlugin juego) {
        JPanel wrap = new JPanel(new BorderLayout());
        wrap.setBackground(MenuPanel.BG);
        wrap.add(crearBarra(), BorderLayout.NORTH);

        JPanel juegoWrap = new JPanel(new BorderLayout());
        juegoWrap.setBackground(MenuPanel.BG);
        juegoWrap.setBorder(new EmptyBorder(4, 4, 4, 4));
        juegoWrap.add(juego.getVista(), BorderLayout.CENTER);

        wrap.add(juegoWrap, BorderLayout.CENTER);
        setContentPane(wrap);
        revalidate(); repaint();
        juego.iniciar();
    }

    // ── Observer ────────────────────────────────────────────────────

    @Override
    public void juegoTerminado(String nombreJuego, int puntaje) {
        SwingUtilities.invokeLater(() -> scoresPanel.actualizar());
    }

    // ── Carga inicial ────────────────────────────────────────────────

    private File encontrarCarpetaJuegos() {
        File f1 = new File("juegos");
        if (f1.exists() && f1.isDirectory()) return f1;
        try {
            File claseDir = new File(
                MainFrame.class.getProtectionDomain().getCodeSource().getLocation().toURI()
            );
            File base = claseDir.isDirectory() ? claseDir.getParentFile().getParentFile()
                                               : claseDir.getParentFile();
            File f2 = new File(base, "juegos");
            if (f2.exists() && f2.isDirectory()) return f2;
        } catch (URISyntaxException e) {
            System.err.println("No se pudo resolver ruta: " + e.getMessage());
        }
        f1.mkdir();
        return f1;
    }

    private void cargarJuegosIniciales() {
        File carpeta = encontrarCarpetaJuegos();
        System.out.println("Cargando juegos desde: " + carpeta.getAbsolutePath());
        File[] jars = carpeta.listFiles(f -> f.getName().endsWith(".jar"));
        if (jars == null) return;
        for (File jar : jars) cargarJar(jar);
    }

    // ── Watcher automático ───────────────────────────────────────────

    private void iniciarWatcher() {
        File carpeta = encontrarCarpetaJuegos();

        watcher = new JuegosWatcher(
            carpeta,
            // JAR agregado → cargar y actualizar menú
            jar -> SwingUtilities.invokeLater(() -> {
                JuegoPlugin nuevo = cargarJar(jar);
                if (nuevo != null) {
                    System.out.println("Watcher: nuevo juego detectado → " + nuevo.getNombre());
                    mostrarMenu();
                }
            }),
            // JAR eliminado → quitar del manager y actualizar menú
            nombreJar -> SwingUtilities.invokeLater(() -> {
                System.out.println("Watcher: JAR eliminado → " + nombreJar);
                manager.eliminarJuegoPorJar(nombreJar);
                mostrarMenu();
            })
        );

        watcherThread = new Thread(watcher, "JuegosWatcher");
        watcherThread.setDaemon(true); // muere con la app
        watcherThread.start();
        System.out.println("Watcher activo en: " + carpeta.getAbsolutePath());
    }

    private JuegoPlugin cargarJar(File jar) {
        try {
            JuegoPlugin juego = Loader.cargar(jar);
            if (juego != null) {
                boolean agregado = manager.agregarJuego(juego, jar.getName());
                if (agregado) {
                    System.out.println("Juego cargado: " + juego.getNombre());
                    return juego;
                }
            }
        } catch (Exception e) {
            System.err.println("Error cargando " + jar.getName() + ": " + e.getMessage());
        }
        return null;
    }

    // ── UI helpers ──────────────────────────────────────────────────

    private JPanel crearBarra() {
        JPanel barra = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 5));
        barra.setBackground(new Color(12, 12, 32));
        barra.setBorder(new MatteBorder(0, 0, 2, 0, MenuPanel.COL_PURP));

        JButton btn = new JButton("◄ MENU");
        btn.setFont(MenuPanel.FONT_SMALL);
        btn.setForeground(MenuPanel.COL_PURP);
        btn.setBackground(new Color(25, 10, 40));
        btn.setFocusPainted(false);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.setBorder(new PixelBorder(MenuPanel.COL_PURP, new Color(80, 20, 120), 1));
        btn.addActionListener(e -> mostrarMenu());
        barra.add(btn);

        JLabel lbl = new JLabel("★ GAME PLATFORM ★");
        lbl.setFont(MenuPanel.FONT_SMALL);
        lbl.setForeground(MenuPanel.COL_GRAY);
        barra.add(lbl);

        return barra;
    }
}
