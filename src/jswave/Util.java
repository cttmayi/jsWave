/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jswave;

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
    
}
