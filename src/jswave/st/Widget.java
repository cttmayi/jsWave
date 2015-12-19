/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jswave.st;

import java.awt.Color;
import java.awt.Graphics;
import jswave.Util;

/**
 *
 * @author cttmayi
 */
public class Widget {

    public boolean enable = true;
    public int group = -1;
    
    public static int timeX, timeW;
    public static int offsetLine;
    public static int rangeX1, rangeX2;
    
    public static int offsetX, offsetY;
    
    public static int screenW, screenH;
    
    public static Color colorFont;
    
    public static void drawString(Graphics g, String name, int px, int py, int pw, int ph, boolean up, Color fgColor, Color bgColor) {

        String[] infos = name.split("\n");
        int length = ph/Util.FontHeight;
        
        if (length > infos.length) {
            length = infos.length;
        }
        int h = Util.FontHeight * length;

        int w = 0;
        for (int i=0; i<length; i++) {
            int t = Util.stringWidth(infos[i]) + 4;
            if (w < t) {
                w = t;
            }
        }

        if (w > pw) {
            w = pw;
        }
        
        int x = px;
        int y = py;

        if (up == true) {
            y = y - h - 2;
        }

        if (bgColor != null) {
            g.setColor(bgColor);
            g.fillRect(x - 2, y , w, h + 2);
        }
        for (int i=0; i< length; i++) {
            g.setColor(fgColor);
            String str;
            if (w == pw) {
                str = Util.trimDownText(infos[i], w);
            }
            else {
                str = infos[i];
            }

            g.drawString(str, x, y + Util.FontHeight * (i+1));
        }
    }
    
    
}
