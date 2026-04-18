package ui;

import core.ScoreManager;
import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.util.*;
import java.util.List;

/** Panel de récords con estética pixel-art. */
public class ScoresPanel extends JPanel {

    private ScoreManager scoreManager;
    private JPanel contenido;

    public ScoresPanel(ScoreManager scoreManager) {
        this.scoreManager = scoreManager;
        setLayout(new BorderLayout(0, 8));
        setBackground(MenuPanel.BG);
        setBorder(new EmptyBorder(16, 30, 16, 30));

        JPanel headerWrap = new JPanel(new GridLayout(3, 1, 0, 2));
        headerWrap.setBackground(MenuPanel.BG);

        JLabel deco = new JLabel("═══════════════════════════", SwingConstants.CENTER);
        deco.setFont(MenuPanel.FONT_SMALL); deco.setForeground(MenuPanel.COL_CYAN);
        headerWrap.add(deco);

        JLabel titulo = new JLabel("HIGH  SCORES", SwingConstants.CENTER);
        titulo.setFont(MenuPanel.FONT_BIG); titulo.setForeground(MenuPanel.COL_YELL);
        headerWrap.add(titulo);

        JLabel deco2 = new JLabel("═══════════════════════════", SwingConstants.CENTER);
        deco2.setFont(MenuPanel.FONT_SMALL); deco2.setForeground(MenuPanel.COL_CYAN);
        headerWrap.add(deco2);

        add(headerWrap, BorderLayout.NORTH);

        contenido = new JPanel();
        contenido.setLayout(new BoxLayout(contenido, BoxLayout.Y_AXIS));
        contenido.setBackground(MenuPanel.BG);

        JScrollPane scroll = new JScrollPane(contenido);
        scroll.setBackground(MenuPanel.BG);
        scroll.getViewport().setBackground(MenuPanel.BG);
        scroll.setBorder(null);
        add(scroll, BorderLayout.CENTER);

        actualizar();
    }

    public void actualizar() {
        contenido.removeAll();

        Map<String, List<Integer>> todos = scoreManager.getTodos();

        if (todos.isEmpty()) {
            contenido.add(Box.createVerticalStrut(40));
            JLabel vacio = new JLabel("  [  NO HAY REGISTROS AUN  ]  ", SwingConstants.CENTER);
            vacio.setFont(MenuPanel.FONT_MED);
            vacio.setForeground(MenuPanel.COL_GRAY);
            vacio.setAlignmentX(Component.CENTER_ALIGNMENT);
            contenido.add(vacio);
        } else {
            String[] medals = {"[1ST]", "[2ND]", "[3RD]"};
            Color[] medalColors = {MenuPanel.COL_YELL, new Color(192,192,192), new Color(205,127,50)};

            for (Map.Entry<String, List<Integer>> entry : todos.entrySet()) {
                JPanel card = new JPanel();
                card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
                card.setBackground(new Color(16, 16, 44));
                card.setBorder(new CompoundBorder(
                    new EmptyBorder(5, 0, 5, 0),
                    new PixelBorder(MenuPanel.COL_CYAN, new Color(0,60,80), 2)
                ));
                card.setAlignmentX(Component.LEFT_ALIGNMENT);
                card.setMaximumSize(new Dimension(Integer.MAX_VALUE, 110));

                JLabel gameName = new JLabel("  ► " + entry.getKey().toUpperCase());
                gameName.setFont(MenuPanel.FONT_MED);
                gameName.setForeground(MenuPanel.COL_CYAN);
                card.add(gameName);

                List<Integer> top = entry.getValue();
                for (int i = 0; i < top.size(); i++) {
                    JLabel sc = new JLabel(String.format("  %s  %06d PTS", medals[i], top.get(i)));
                    sc.setFont(MenuPanel.FONT_SMALL);
                    sc.setForeground(medalColors[i]);
                    card.add(sc);
                }

                contenido.add(card);
                contenido.add(Box.createVerticalStrut(6));
            }
        }

        contenido.revalidate();
        contenido.repaint();
    }
}
