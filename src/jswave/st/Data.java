/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jswave.st;

import java.awt.Color;
import java.util.ArrayList;
import jswave.Util;

/**
 *
 * @author lenovo
 */
public class Data {
    
    public static int LINE = 0;
    public static int DIAGRAM = 1;
    public static int HISTOGRAM = 2;
    public int type;
    private String name = null;
    
    
    private int heightMax = 20;
    
    private int yStart = 0;
    private int yEnd = 0;
    
    
    
    public void setHeightMax(int height) {
        heightMax = height;
    }
    
    public int getHeightMax() {
        return heightMax;
    }
    
    public void setY(int start, int end) {
        yStart = start;
        yEnd = end;
    }
    
    public int getY1() {
        return yStart;
    }

    public int getY2() {
        return yEnd;
    }
    
    
    public void setTime(ArrayList<Integer> time) {
        listTime = time;    
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public String getName() {
        return this.name;
    }
    public ArrayList<Integer> getX(int ts, int xs, double wdt) {
        listX = timeToX(ts, xs, wdt);
        return listX;
    }
    
    public ArrayList<Integer> listTime = new ArrayList<Integer>();
    
    public ArrayList<Integer> listX = new ArrayList<Integer>();
    
    private ArrayList<Integer> timeToX(int ts, int xs, double wdt) {
        ArrayList<Integer> x = new ArrayList<Integer>();
        for (Integer t:listTime) {
            x.add(Util.getX(t, xs,ts, wdt));
        }
        return x;
    }
    
    public static Color ColorMake(int colori) {
        return new Color((0xFF0000 & colori) >> 16, (0xFF00 & colori) >> 8, 0xFF & colori);
    }
    
    
}
