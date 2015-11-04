/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jswave.st;

import java.awt.Color;
import java.util.ArrayList;
import static jswave.st.Data.ColorMake;

/**
 *
 * @author lenovo
 */
public class Connection {
    
    public int time;
    public int start;
    public int end;
    public Color color;

    public static Color ColorMake(int colori) {
        return new Color((0xFF0000 & colori) >> 16, (0xFF00 & colori) >> 8, 0xFF & colori);
    }

    public int getX(int ts, int xs, double wdt) {
        return (int)((time - ts) * wdt) + xs;
    }
    
    
    public static Connection newData(int time, int start, int end, int color) {
        Connection list = new Connection();
        list.time = time;
        list.color = ColorMake(color);
        list.start = start;
        list.end = end;
        return list;
    }
    
}
