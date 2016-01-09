/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jswave.st;

import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;
import jswave.Util;
import static jswave.st.Wave.ColorMake;
import static jswave.st.Widget.colorFont;

/**
 *
 * @author lenovo
 */
public class Node {
    private static final ArrayList<Node> listNode = new ArrayList<Node>();

    private ArrayList<Color> nColors = new ArrayList<Color>();
    private ArrayList<Integer> nYs = new ArrayList<Integer>();
    private ArrayList<Integer> nHs = new ArrayList<Integer>();
    Color frameColor;
    private final int height;

    Node(ArrayList<Integer> ys, ArrayList<Integer> hs, ArrayList<Color> cs, Color fc) {
        nYs = ys;
        nHs = hs;
        nColors = cs;
        frameColor = fc;

        height = nYs.get(nYs.size()-1) + nHs.get(nHs.size()-1) - nYs.get(0);
    }

    public static int add(ArrayList<Integer> hs, ArrayList<Integer> ys, ArrayList<Integer> colori, Integer fci) {
        
        ArrayList<Color> colors = new ArrayList<Color>();

        for (Integer colorii : colori) {
            Color color = ColorMake(colorii);
            colors.add(color);
        }
        Color fc = ColorMake(fci);
        Node node = new Node(ys, hs, colors, fc);

        listNode.add(node);
        return listNode.size() -1;
    }

    public static Node get(int index) {
        return listNode.get(index);
    }

    public int getHeight() {
        return height + nYs.get(0);
    }

    public void draw(Graphics g, String text, int x, int y, int w) {
        int yy = 0, hh = 0;
        if (w > 2 && height > 2) {
            for (int i=0; i<nHs.size(); i++) {
                g.setColor(nColors.get(i));
                yy = y - nYs.get(i);
                hh = nHs.get(i);
                g.fillRect(x, yy-hh, w-1, hh);
            }
        }
        g.setColor(frameColor);
        if (height == 1) {
            g.drawLine(x, y - height - nYs.get(0), x+w-1, y - height - nYs.get(0));
        }
        else if (w == 1) {
            g.drawLine(x, y - height - nYs.get(0), x, y - nYs.get(0));
        }
        else {
            g.drawRect(x, y - height - nYs.get(0), w-1, height);
        }

        if (text != null && !text.equals("")) {
            text = Util.trimDownText(text, w - 8);
            g.setColor(colorFont);
            g.drawString(text, x + 4, yy - hh + Util.fontHeight);
        }
    }
    
    
}
