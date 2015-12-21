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
 * @author cttmayi
 */
public class Info extends Widget {
    
    public static final ArrayList<Info> array = new ArrayList<Info>();

    public static ArrayList<Info> getArray() {
        return array;
    }

    public static void clear() {
        array.clear();
    }
    
    public static int add(Info data) {
        array.add(data);
        return array.size() - 1;
    }
    
    public static Info get(int id) {
        return array.get(id);
    }

    public static int size() {
        return array.size();
    }
    
    public int x;
    public int y;
    public String info;
    public Color fgColor;
    public Color bgColor;

    public static int add(int x, int y, String name, int fg, int bg) {
        Info info = new Info();
        info.x = x;
        info.y = y;
        info.info = name;
        info.fgColor = Util.colorMake(fg);
        info.bgColor = Util.colorMake(bg);
        return Info.add(info); 
    }
}
