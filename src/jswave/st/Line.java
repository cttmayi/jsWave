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

    public static Line newData(ArrayList<Integer> time, ArrayList<Integer> colors, ArrayList<Integer> ys, ArrayList<String> names) {
        Line list = new Line();
        list.type = Wave.LINE;

        for (int id=0; id<colors.size(); id++) {
            Color color = ColorMake(colors.get(id));
            list.listColor.add(color);
        }
        list.listTime = time;
        list.listY = ys;
        list.listName = names;
        
        for (int y: ys) {
            list.setHeightMax(y + Util.fontHeight);
        }

        return list;
    }
    
    public void draw(Graphics g, int y, double wdt) {
        //System.out.println("Line:" + y);
        ArrayList<Integer> x = getX(timeX, offsetX, wdt);

        int timeStart = x.get(0);
        if (timeStart < offsetX) timeStart = offsetX;

        drawName(g, getName(), y, getHeightMax());

        setY(y - Line.lineHeight, y);
        clearTouch();
        for (int timeId=1; timeId<x.size(); timeId++) {
            if (x.get(timeId) < offsetX) {timeStart = offsetX; continue;}

            g.setColor(listColor.get(timeId));
            int timeEnd = x.get(timeId);

            int yy = y;
            if (timeId < listY.size()) {
                yy = y - listY.get(timeId);
            }
            g.fillRect(timeStart, yy-Line.lineHeight, timeEnd - timeStart, Line.lineHeight);
            addTouch(timeId-1, timeStart, yy-10, timeEnd, yy);

            String str = null;
            if (timeId < listName.size()) {
                str = listName.get(timeId);
                if (str != null && !str.equals("")) {
                    str = Util.trimDownText(str, timeEnd - timeStart - 8);
                    //g.setColor(colorFont);
                    g.drawString(str, timeStart + 4, yy - 5);
                }
            }

            timeStart = timeEnd;

            if (timeStart > screenW) break;
        }
    }    
}
