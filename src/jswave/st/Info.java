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
public class Info {
    public int x;
    public int y;
    public String info;
    public Color fgColor;
    public Color bgColor;
    
    public static Info newData(int x, int y, String name, int fg, int bg) {
        Info info = new Info();
        info.x = x;
        info.y = y;
        info.info = name;
        info.fgColor = Util.colorMake(fg);
        info.bgColor = Util.colorMake(bg);
        return info; 
    }
}
