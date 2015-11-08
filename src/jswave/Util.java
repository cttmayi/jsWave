/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jswave;

import java.awt.Color;
import java.io.File;
import java.util.ArrayList;

/**
 *
 * @author lenovo
 */
public class Util {
    
    public static ArrayList<String> paraList = new ArrayList<String>();

    public static int PATH = 0; 
    
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

    public static String getJarPath(){  
        String filePath = System.getProperty("java.class.path");  
        String pathSplit = System.getProperty("path.separator");//windows下是";",linux下是":"  
          
        if(filePath.contains(pathSplit)){  
            filePath = filePath.substring(0,filePath.indexOf(pathSplit));  
        }else if (filePath.endsWith(".jar")) {//截取路径中的jar包名,可执行jar包运行的结果里包含".jar"  
            //此时的路径是"E:\workspace\Demorun\Demorun_fat.jar"，用"/"分割不行  
            //下面的语句输出是-1，应该改为lastIndexOf("\\")或者lastIndexOf(File.separator)  
//          System.out.println("getPath2:"+filePath.lastIndexOf("/"));  
            filePath = filePath.substring(0, filePath.lastIndexOf(File.separator) + 1);  
              
        }  
        return filePath;  
    } 
    
    
    
}
