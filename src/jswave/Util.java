/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jswave;

import java.awt.Color;
import java.util.ArrayList;

/**
 *
 * @author lenovo
 */
public class Util {
    


    public static int getTime(int x, int pX, int timeX, double tdp) { 
        return timeX + (int)(tdp * (x  - pX));
    }
    
    public static int getTime(int x, int pX, int pW, int timeX, int timeW) { 
        return getTime(x, pX, timeX, (double)timeW/pW);
    }
    
    public static int getX(int t, int pX, int timeX, double pdt) {
        return pX + (int)(pdt * (t - timeX));
    }
    
    public static int getX(int t, int pX, int pW, int timeX, int timeW) { 
        return getX(t, pX, timeX, (double)pW/timeW);
    }
    
    public static String getTimeString(int t) {
        if (t > 1000000) {
            return String.valueOf((double)(t/1000)/1000).concat("s");
        }
        else if (t > 1000) {
            return String.valueOf((double)(t)/1000).concat("ms");
        }
        else {
            return String.valueOf(t).concat("us");
        }
    }

    public static Color colorMake(int colori) {
        return new Color((0xFF0000 & colori) >> 16, (0xFF00 & colori) >> 8, 0xFF & colori);
    }
    
    public static ArrayList<Integer> d2t(ArrayList<Double> ds) {
        ArrayList<Integer> ts = new ArrayList<Integer>();
        for (double d: ds) {
            ts.add((int) d);
        }
        return ts;
    }
    
    public static ArrayList<Color> d2c(ArrayList<Double> ds) {
        ArrayList<Color> cs = new ArrayList<Color>();
        for (double d: ds) {
            cs.add(colorMake((int) d));
        }
        return cs;
    }    
    
}
