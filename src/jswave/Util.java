/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jswave;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Toolkit;
import java.io.File;
import java.util.ArrayList;

/**
 *
 * @author cttmayi
 */
public class Util {
    
    public static ArrayList<String> paraList = new ArrayList<String>();

    public static int PARA_FILE_PATH = 0; 
    public static int PARA_LIBS_PATH = 1; 

    public static final Font fontArial = new Font("Arial", Font.PLAIN, 12);
    private static final FontMetrics fontArialMetrics = Toolkit.getDefaultToolkit().getFontMetrics(fontArial);;
    public static final int fontHeight = fontArialMetrics.getHeight();
    
    public static boolean isDebug = false;
    
    public static int stringWidth(String str) {
        return fontArialMetrics.stringWidth(str);
    }
    
    public static void addPara(String para){
        paraList.add(para);
    }
    
    public static String getPara(int id){
        if (id < paraList.size()){
            return paraList.get(id);
        }
        return null;
    }
    
    public static boolean isFileExist(String fileName) {
        if (fileName == null) {
            return false;
        }
        File file =new File(fileName);    
        if  (file.exists()  && file.isFile()) {
            return true;
        } 
        else {  
            return false;
        }
    }
    
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

    public static int getW(int t, double pdt) {
        return (int) Math.ceil(pdt * (t));
    }
    
    public static Color colorMake(int colori) {
        return new Color((0xFF0000 & colori) >> 16, (0xFF00 & colori) >> 8, 0xFF & colori);
    }

    public static int o2i(Object o) {
        String type = o.getClass().getName();
        System.out.println(type);
        if (type.equals("java.lang.Double")) {
            
        }
        else if (type.equals("java.lang.Integer")){
            
        }
        else if (type.equals("java.lang.Long")){
            
        }
        return 0;
    } 

    public static ArrayList<Integer> an2i(ArrayList<Number> ds) {
        ArrayList<Integer> ts = new ArrayList<Integer>();
        for (Number d: ds) {
            ts.add(d.intValue());
        }
        return ts;
    }
    
    public static ArrayList<Color> an2c(ArrayList<Number> ds) {
        ArrayList<Color> cs = new ArrayList<Color>();
        for (Number d: ds) {
            cs.add(colorMake(d.intValue()));
        }
        return cs;
    }    

    public static String getJarPath(){  
        String filePath = System.getProperty("java.class.path");  
        String pathSplit = System.getProperty("path.separator");
        
        if(filePath.contains(pathSplit)){
            filePath = filePath.substring(0,filePath.indexOf(pathSplit)) + '\\';
        }
        else if (filePath.endsWith(".jar")) {
            filePath = filePath.substring(0, filePath.lastIndexOf(File.separator) + 1);
        }
        else {
            filePath = filePath + "\\";
        }
        return filePath;  
    } 

    public static String trimDownText(String strValue, int maxWidth) {
        String strReturn = "";
        int chr_width;
        if (strValue != null && !strValue.equals("") && maxWidth > 2) {
            for (int i = strValue.length(); i > -1; i--) {
                strReturn = strValue.substring(0, i);
                chr_width = Util.stringWidth(strReturn);
                if (chr_width < maxWidth) {
                    break;
                }
            }
        }
        return strReturn;
    }
    
    public final static int S = 1000000;
    public final static int MS = 1000;
    public final static int US = 1;
    
    
    public static int s(int s) {
        return s * S;
    }
    
    public static int s(float s) {
        return (int)(s * S);
    }

    public static int ms(int ms) {
        return ms * MS;
    }
   
    public static int ms(float ms) {
        return (int)(ms * MS);
    }
    
    public static int us(int us) {
        return us;
    }   

    public static int us(float us) {
        return (int)(us);
    }  

    public static String getTimeString(int t) {
        if (t > Util.S) {
            return String.format("%.03fs", (double)(t/1000)/1000);
        }
        else if (t > Util.MS) {
            return String.format("%.03fms", (double)(t)/1000);
        }
        else {
            return String.format("%dus", (t));
        }
    }
    
    public static String timeFormatHMS(long t, int w) {
        
        long h = t /60/60/Util.S;
        long m = (t - h *60 * 60* Util.S)/60/Util.S;
        
        float s = (float)((t - h * 60 * 60 * Util.S- m * 60 * Util.S))/Util.S;
        
        if (w >= Util.S && t % Util.S == 0) {
            return String.format("%02d:%02d:%02d", h % 24, m, (int)s);
        }
        else if (w >= Util.MS && t % Util.MS == 0){
            return String.format("%02d:%02d:%02.03f", h % 24, m, s);
        }
        else {
            return String.format("%02d:%02d:%02.04f", h % 24, m, s);
        }
    }
    
    public static String timeFormatS(long t, int w) {
        if (w >= Util.S && t % Util.S == 0) {
            return String.format("%ds",((t))/Util.S);
        }
        else if (w >= Util.MS && t % Util.MS == 0) {
            return String.format("%.03fs",((double)(t))/Util.S);
        }
        else {
            return String.format("%fs",((double)(t))/Util.S);
        }
    }

    public static String timeFormatUS(long t, int w) {
        if (w >= Util.S && t % Util.S == 0) {
            return String.format("%ds",((t))/Util.S);
        }
        else if (w >= Util.MS && t % Util.MS == 0) {
            return String.format("%dms",((t))/Util.MS);
        }
        else {
            //return String.format("%dus",((t))/Util.US);
            return String.format("%.01fms",((double)(t))/Util.MS);
        }
    }
}
