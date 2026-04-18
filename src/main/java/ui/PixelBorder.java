package ui;

import javax.swing.border.AbstractBorder;
import java.awt.*;

/** Borde con estética pixel-art: esquinas cuadradas y efecto de doble línea. */
public class PixelBorder extends AbstractBorder {
    private Color outer, inner;
    private int thickness;

    public PixelBorder(Color outer, Color inner, int thickness) {
        this.outer = outer; this.inner = inner; this.thickness = thickness;
    }

    @Override
    public void paintBorder(Component c, Graphics g, int x, int y, int w, int h) {
        Graphics2D g2 = (Graphics2D) g;
        g2.setColor(outer);
        g2.setStroke(new BasicStroke(thickness));
        g2.drawRect(x, y, w-1, h-1);
        g2.setColor(inner);
        g2.setStroke(new BasicStroke(1));
        g2.drawRect(x+thickness+1, y+thickness+1, w-2*(thickness+2), h-2*(thickness+2));
    }

    @Override
    public Insets getBorderInsets(Component c) {
        return new Insets(thickness+4, thickness+4, thickness+4, thickness+4);
    }
}
