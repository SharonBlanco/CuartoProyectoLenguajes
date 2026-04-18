package ui;
import core.GameManager;
import core.JuegoPlugin;
import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;
public class MenuPanel extends JPanel {
    private GameManager manager;
    private MainFrame frame;
    private Timer blinkTimer;
    private boolean blinkState = true;
    private JLabel lblInsert;
    static final Color BG        = new Color(8,  8,  24);
    static final Color BG_PANEL  = new Color(16, 16, 44);
    static final Color COL_CYAN  = new Color(0,  220, 255);
    static final Color COL_GREEN = new Color(0,  255, 100);
    static final Color COL_YELL  = new Color(255, 220, 0);
    static final Color COL_RED   = new Color(255, 60,  80);
    static final Color COL_PURP  = new Color(180, 80, 255);
    static final Color COL_GRAY  = new Color(80,  80, 110);
    static final Color COL_WHITE = new Color(220, 220, 255);
    static final Font FONT_BIG   = new Font("Monospaced", Font.BOLD, 36);
    static final Font FONT_MED   = new Font("Monospaced", Font.BOLD, 16);
    static final Font FONT_SMALL = new Font("Monospaced", Font.BOLD, 12);
    public MenuPanel(GameManager manager, MainFrame frame) {
        this.manager = manager;
        this.frame = frame;
        setBackground(BG);
        setLayout(new BorderLayout());
        construirMenu();
    }
    public void construirMenu() {
        removeAll();
        if (blinkTimer != null) blinkTimer.stop();
        JLayeredPane layered = new JLayeredPane();
        layered.setPreferredSize(new Dimension(800, 600));
        JPanel content = new JPanel(new BorderLayout());
        content.setBackground(BG);
        content.setBounds(0, 0, 800, 600);
        JPanel header = new JPanel(new GridLayout(4, 1, 0, 2));
        header.setBackground(BG);
        header.setBorder(new EmptyBorder(24, 0, 16, 0));
        JLabel deco1 = new JLabel("═══════════════════════════════════", SwingConstants.CENTER);
        deco1.setFont(FONT_SMALL); deco1.setForeground(COL_CYAN);
        header.add(deco1);
        JLabel titulo = new JLabel("GAME  PLATFORM", SwingConstants.CENTER);
        titulo.setFont(FONT_BIG); titulo.setForeground(COL_GREEN);
        header.add(titulo);
        JLabel subtitulo = new JLabel("★  PLATAFORMA DE JUEGOS  ★", SwingConstants.CENTER);
        subtitulo.setFont(FONT_MED); subtitulo.setForeground(COL_CYAN);
        header.add(subtitulo);
        JLabel deco2 = new JLabel("═══════════════════════════════════", SwingConstants.CENTER);
        deco2.setFont(FONT_SMALL); deco2.setForeground(COL_CYAN);
        header.add(deco2);
        content.add(header, BorderLayout.NORTH);
        JPanel centroWrapper = new JPanel(new GridBagLayout());
        centroWrapper.setBackground(BG);
        JPanel juegoList = new JPanel();
        juegoList.setLayout(new BoxLayout(juegoList, BoxLayout.Y_AXIS));
        juegoList.setBackground(BG);
        juegoList.setBorder(new EmptyBorder(0, 60, 0, 60));
        JLabel selLabel = new JLabel("▶  SELECCIONA UN JUEGO:", SwingConstants.LEFT);
        selLabel.setFont(FONT_SMALL); selLabel.setForeground(COL_GRAY);
        selLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        juegoList.add(selLabel);
        juegoList.add(Box.createVerticalStrut(12));
        if (manager.getJuegos().isEmpty()) {
            JLabel vacio = new JLabel("  [  AGREGA UN JAR A /juegos  ]");
            vacio.setFont(FONT_SMALL); vacio.setForeground(COL_GRAY);
            vacio.setAlignmentX(Component.CENTER_ALIGNMENT);
            juegoList.add(vacio);
        } else {
            int[] idx = {1};
            for (JuegoPlugin juego : manager.getJuegos()) {
                JButton btn = crearBotonJuego(idx[0]++, juego.getNombre());
                btn.addActionListener(e -> frame.abrirJuego(juego));
                btn.setAlignmentX(Component.CENTER_ALIGNMENT);
                juegoList.add(btn);
                juegoList.add(Box.createVerticalStrut(8));
            }
        }
        centroWrapper.add(juegoList);
        content.add(centroWrapper, BorderLayout.CENTER);
        JPanel footer = new JPanel(new BorderLayout());
        footer.setBackground(new Color(12, 12, 32));
        footer.setBorder(new CompoundBorder(
            new MatteBorder(2, 0, 0, 0, COL_PURP),
            new EmptyBorder(10, 20, 12, 20)
        ));
        JPanel btnRow = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 4));
        btnRow.setBackground(new Color(12, 12, 32));
        JButton btnScore = crearBotonAccion("[ F1 ] RECORDS", COL_YELL);
        btnScore.addActionListener(e -> frame.mostrarScores());
        btnRow.add(btnScore);
        JLabel watcherLabel = new JLabel("● MONITOREANDO /juegos", SwingConstants.CENTER);
        watcherLabel.setFont(FONT_SMALL);
        watcherLabel.setForeground(new Color(0, 180, 80));
        btnRow.add(Box.createHorizontalStrut(20));
        btnRow.add(watcherLabel);
        footer.add(btnRow, BorderLayout.NORTH);
        JLabel lblHint = new JLabel("[ AÑADÍ O QUITÁ JUEGOS COPIANDO/BORRANDO SUS .JAR DE LA CARPETA /juegos ]", SwingConstants.CENTER);
        lblHint.setFont(FONT_SMALL);
        lblHint.setForeground(COL_GRAY);
        footer.add(lblHint, BorderLayout.CENTER);
        lblInsert = new JLabel("INSERT COIN  ●  PRESS START", SwingConstants.CENTER);
        lblInsert.setFont(FONT_SMALL);
        lblInsert.setForeground(COL_WHITE);
        footer.add(lblInsert, BorderLayout.SOUTH);
        content.add(footer, BorderLayout.SOUTH);
        layered.add(content, JLayeredPane.DEFAULT_LAYER);
        MenuPanel.ScanlinesPanel scanlines = new MenuPanel.ScanlinesPanel();
        scanlines.setBounds(0, 0, 800, 600);
        layered.add(scanlines, JLayeredPane.PALETTE_LAYER);
        add(layered, BorderLayout.CENTER);
        blinkTimer = new Timer(600, ev -> {
            blinkState = !blinkState;
            lblInsert.setForeground(blinkState ? COL_WHITE : BG);
        });
        blinkTimer.start();
        revalidate();
        repaint();
    }
    private JButton crearBotonJuego(int num, String nombre) {
        JButton btn = new JButton(String.format("  %d.  %-20s  ▶ PLAY", num, nombre));
        btn.setFont(FONT_MED);
        btn.setForeground(COL_GREEN);
        btn.setBackground(new Color(0, 30, 10));
        btn.setFocusPainted(false);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.setBorder(new PixelBorder(COL_GREEN, new Color(0, 80, 30), 2));
        btn.setMaximumSize(new Dimension(500, 46));
        btn.setHorizontalAlignment(SwingConstants.LEFT);
        btn.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) { btn.setBackground(new Color(0, 60, 20)); btn.setForeground(COL_YELL); }
            public void mouseExited(MouseEvent e)  { btn.setBackground(new Color(0, 30, 10)); btn.setForeground(COL_GREEN); }
        });
        return btn;
    }
    private JButton crearBotonAccion(String texto, Color color) {
        JButton btn = new JButton(texto);
        btn.setFont(FONT_SMALL);
        btn.setForeground(color);
        btn.setBackground(BG_PANEL);
        btn.setFocusPainted(false);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.setBorder(new PixelBorder(color, color.darker(), 1));
        btn.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) { btn.setBackground(new Color(30, 30, 60)); }
            public void mouseExited(MouseEvent e)  { btn.setBackground(BG_PANEL); }
        });
        return btn;
    }
    static class ScanlinesPanel extends JPanel {
        public ScanlinesPanel() { setOpaque(false); }
        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g;
            int h = getHeight(), w = getWidth();
            g2.setColor(new Color(0, 0, 0, 28));
            for (int y = 0; y < h; y += 3) g2.fillRect(0, y, w, 1);
            RadialGradientPaint vignette = new RadialGradientPaint(
                new java.awt.Point(w/2, h/2), Math.max(w, h) * 0.7f,
                new float[]{0.4f, 1f},
                new Color[]{new Color(0,0,0,0), new Color(0,0,10,120)}
            );
            g2.setPaint(vignette);
            g2.fillRect(0, 0, w, h);
        }
    }
}