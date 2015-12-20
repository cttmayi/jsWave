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
public class Histogram extends Data{
    public int width;
    
    public ArrayList<Integer> listTime2 = new ArrayList<Integer>();
    public ArrayList<Color> listColor = new ArrayList<Color>();
    public ArrayList<Integer> listY = new ArrayList<Integer>();
    public ArrayList<String> listName = new ArrayList<String>();

    
    public ArrayList<Integer> getX2(int ts, int xs, double wdt) {
        listX = timeToX(listTime2, ts, xs, wdt);
        return listX;
    }
    
    public static Histogram newData(ArrayList<Integer> time, ArrayList<Integer> time2, 
            ArrayList<Integer> ys, ArrayList<Integer> colors, ArrayList<String> names, int heightMax) {
        Histogram list = new Histogram();
        list.type = Data.HISTOGRAM;
        for (int id=0; id<colors.size(); id++) {
            Color color = ColorMake(colors.get(id));
            list.listColor.add(color);
        }
        for (int id=0; id<ys.size(); id++) {
            int y = ys.get(id);
            if (heightMax < y) {
                y = heightMax;
            }
            list.listY.add(y);
        }
        
        list.setHeightMax(heightMax);
        list.listTime = time;
        list.listTime2 = time2;
        list.listName = names;
        return list;
    }    

    public void draw(Graphics g, int y, double wdt) {
        //System.out.println("Histogram:" + y);
        ArrayList<Integer> x = getX(timeX, offsetX, wdt);
        ArrayList<Integer> x2 = getX2(timeX, offsetX, wdt);

        setY(y - getHeightMax(), y);
        clearTouch();
        drawName(g, getName(), y, getHeightMax());
        for (int timeId=0; timeId<x.size(); timeId++) {
            int xx = x.get(timeId);
            int xx2 = x2.get(timeId);
            
            if (xx > screenW) break;
            if (xx2 < offsetX) continue;
            
            if (xx < offsetX) xx = offsetX;
            
            
            
            int w = xx2 - xx;
            if (w <= 0) w = 1;
            
            int yh = listY.get(timeId);
            
            g.setColor(listColor.get(timeId));
            g.fillRect(xx, y-yh, w, yh);
            if (outBorderColor != null) {
                g.setColor(outBorderColor);
                g.drawRect(xx, y-yh, w, yh);
            }
            
            addTouch(timeId, xx, y-yh, xx+w, y);
            
            String str = null;
            if (timeId < listName.size()) {
                str = listName.get(timeId);
            }
            if (str != null && !str.equals("")) {
                str = Util.trimDownText(str, w - 8);

                g.setColor(colorFont);
                
                int yy = listY.get(timeId) - Util.FontHeight;
                if (yy < Util.FontHeight) {
                    yy = Util.FontHeight;
                }
                
                g.drawString(str, xx + 4, y - yy - 2);
            }            
        }        
    }
    
    
}
