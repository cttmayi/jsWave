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
 * @author lenovo
 */
public class TimeRuler extends Widget{

    private static TimeRuler timeRuler = new TimeRuler();
    
    public static TimeRuler getData() {
        return timeRuler;
    }

    private long timeOffset, timeOffset2;
    private int timeRulerPoint;
    
    public long getTimeOffset() {
        return timeOffset;
    }
    public long getTimeOffset2() {
        return timeOffset2;
    }
    
    
    public void setTimeRuler(long us, long us2) {
        timeOffset = us;
        timeOffset2 = us2;
    }

    public void setTimePoint(int us) {
        timeRulerPoint = us;
    }

    private int getTime(int x){
        return Util.getTime(x, offsetX, screenW - offsetX, timeX, timeW);
    }

    private int getX(int t) {
        return Util.getX(t, offsetX, screenW - offsetX, timeX, timeW);
    }
    
    public void draw(Graphics g, int y) {
        drawName(g, "Time", y + 30, 20);

        int ww = 1;
        int tt = getTime(offsetX + 150) - getTime(offsetX);

        while (true) {
            if (ww > tt) break;
            ww = ww * 2;
            if (ww > tt) break;
            ww = ww /2 * 5;            
            if (ww > tt) break;
            ww = ww * 2;
        }

        g.drawLine(offsetX, y + Util.fontHeight * 2 + 2, screenW, y + Util.fontHeight * 2 + 2);

        for (int t=timeX/ww*ww; t<timeX+timeW; t+=ww) {
            int x = getX(t);
            if (x >= offsetX) {
                if (timeOffset != 0) {
                    g.drawString(Util.timeFormatHMS(t + timeOffset, ww), x, y + Util.fontHeight);
                }
                g.drawLine(x, y + Util.fontHeight * 2 + 2, x, y + Util.fontHeight * 2 + 5);
                
                if (timeOffset2 != 0) {
                    g.drawString(Util.timeFormatS(t + timeOffset2, ww), x, y + Util.fontHeight * 2); 
                }
                g.drawString(Util.timeFormatUS(t, ww), x, y + Util.fontHeight * 3); 
            }
        }

        g.setColor(Color.RED);
        g.fillOval(getX(timeRulerPoint)-4, Util.fontHeight * 2 + 2 - 4, 8, 8);
    }
}
