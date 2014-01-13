package ru.rawcode.dev;
import java.awt.Color;
import java.awt.Graphics;
import java.util.Random;
import javax.swing.JFrame;
import javax.swing.JPanel;

@SuppressWarnings("serial")
public class Noise2 extends JPanel{

    public static void main(String [] args) {

        JFrame f = new JFrame("Perlin Noise");
        f.setSize(600, 600);
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.add(new Noise2());
        f.setVisible(true);
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        for(int y = 0; y < size; y++) {
            for(int x = 0; x < size; x++) {
                g.setColor(new Color(perlinnoise[y][x], 0, 0));
                g.fillRect(x * 8, y * 8, 8, 8);
            }
        }
        repaint();
    }
}