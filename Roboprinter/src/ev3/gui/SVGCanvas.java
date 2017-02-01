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

import ev3.parser.Line;

/**
 *
 * @author user
 */
public class SVGCanvas extends JPanel {

    ArrayList<Line> lines = new ArrayList();

    public SVGCanvas() {
        this.setBackground(Color.white);
    }

    public void paintInCanvas(String function, ArrayList<Line> linesToDraw) {
		System.out.println("function: " + function);
		lines = linesToDraw;
	}

    public void paint(Graphics g) {
        super.paint(g); // clears drawing area
        g.setColor(Color.black);
        for (Line l : lines) {
            g.drawLine(l.getX0(), l.getY0(), l.getX1(), l.getY1());
        }

    } // end method paint 

}
