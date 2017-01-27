/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ev3.gui;

import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;
import javax.swing.JPanel;

/**
 *
 * @author user
 */
public class SVGCanvas extends JPanel {

    ArrayList<Line> lines = new ArrayList();

    public SVGCanvas() {
        this.setBackground(Color.white);
    }

    public void paintInCanvas(String funcion,String[] arguments) {
        System.out.println("function: " + funcion);
        for (int i = 0; i < arguments.length - 1; i++) {
            String[] p1 = arguments[i].split(" ");
            String[] p2 = arguments[i + 1].split(" ");
            lines.add(new Line(p1[0], p1[1], p2[0], p2[1]));
        }
    }

    public void paint(Graphics g) {
        super.paint(g); // clears drawing area
        g.setColor(Color.black);
        for (Line l : lines) {
            g.drawLine(l.x0, l.y0, l.x1, l.y1);
        }

    } // end method paint 

    class Line {

        int x0;
        int y0;
        int x1;
        int y1;

        public Line(String x0, String y0, String x1, String y1) {
            this.x0 = (int) Math.round(Double.parseDouble(x0));
            this.y0 = (int) Math.round(Double.parseDouble(y0));
            this.x1 = (int) Math.round(Double.parseDouble(x1));
            this.y1 = (int) Math.round(Double.parseDouble(y1));
        }

    }
}
