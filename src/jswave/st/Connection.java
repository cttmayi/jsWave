/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jswave.st;

import java.awt.Color;
import java.awt.Graphics;
import jswave.Util;
import jswave.ui.WaveJPanel;


/**
 *
 * @author cttmayi
 */
public class Connection extends Widget{
    
    public int time;
    public int start;
    public int end;
    public Color color;

    public static Color ColorMake(int colori) {
        return new Color((0xFF0000 & colori) >> 16, (0xFF00 & colori) >> 8, 0xFF & colori);
    }

    public int getX(int ts, int xs, double wdt) {
        return Util.getX(time, xs, ts, wdt);
        //return (int)((time - ts) * wdt) + xs;
    }
    
    public int getTime() {
        return time;
    }
    
    public static Connection newData(int time, int start, int end, int color) {
        Connection list = new Connection();
        list.time = time;
        list.color = ColorMake(color);
        list.start = start;
        list.end = end;
        return list;
    }

    public void draw(Graphics g, WaveJPanel panel, double wdt) {

        if (enable) {
            int x = getX(timeX, offsetX, wdt);

            if (x < offsetX) return;
            if (x > screenW) return;

            g.setColor(color);

            int yStart, yEnd, yO;
            if (start < end) {
                yStart = panel.getLineY2(start);
                yEnd = panel.getLineY1(end);
                yO = yEnd - 4;
            }
            else {
                yStart = panel.getLineY1(start);
                yEnd = panel.getLineY2(end);
                yO = yEnd;
            }
            if (yStart != yEnd) {
                g.drawLine(x, yStart, x, yEnd);
                g.fillOval(x-2, yO, 4, 4);
            }
        }
    }
}
