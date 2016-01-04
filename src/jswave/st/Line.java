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

/**
 *
 * @author cttmayi
 */
public class Line extends Wave {
    public static final int lineHeight = 3;    
    
    public ArrayList<Color> listColor = new ArrayList<Color>();
    public ArrayList<Integer> listY = new ArrayList<Integer>();
    public ArrayList<String> listName = new ArrayList<String>();

    public static int add(String name, ArrayList<Integer> time, ArrayList<Integer> colors, ArrayList<Integer> ys, ArrayList<String> names) {
        Line wave = new Line();
        wave.type = Wave.LINE;
        wave.setName(name);
        
        
        for (int id=0; id<colors.size(); id++) {
            Color color = ColorMake(colors.get(id));
            wave.listColor.add(color);
        }
        wave.listTime = time;
        wave.listY = ys;
        wave.listName = names;
        
        for (int y: ys) {
            wave.setHeightMax(y + Util.fontHeight);
        }

        return Wave.add(wave);
    }
    
    @Override
    public void draw(Graphics g, int y, double wdt) {
        //System.out.println("Line:" + y);
        ArrayList<Integer> x = getX(timeX, offsetX, wdt);

        int timeStart = x.get(0);
        if (timeStart < offsetX) timeStart = offsetX;

        drawName(g, getName(), y, getHeightMax());

        setY(y - getHeightMax()/2, y);
        clearTouch();
        for (int timeId=1; timeId<x.size(); timeId++) {
            if (x.get(timeId) < offsetX) {timeStart = offsetX; continue;}

            g.setColor(listColor.get(timeId));
            int timeEnd = x.get(timeId);

            int yy;
            int ww;
            if (timeId < listY.size()) {
                yy = y - getHeightMax()/2 - listY.get(timeId)/2;
                ww = listY.get(timeId);
            }
            else {
                yy = y - getHeightMax()/2 -Line.lineHeight/2;
                ww = Line.lineHeight;
            }
            g.fillRect(timeStart, yy, timeEnd - timeStart, ww);
            addTouch(timeId-1, timeStart, yy, timeEnd, yy+ww);

            String str = null;
            if (timeId < listName.size()) {
                str = listName.get(timeId);
                if (str != null && !str.equals("")) {
                    str = Util.trimDownText(str, timeEnd - timeStart - 8);
                    g.drawString(str, timeStart + 4, yy - 5);
                }
            }

            timeStart = timeEnd;

            if (timeStart > screenW) break;
        }
    }    
}
