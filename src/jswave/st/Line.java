/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jswave.st;

import java.awt.Color;
import java.util.ArrayList;

/**
 *
 * @author lenovo
 */
public class Line extends Data {
    public ArrayList<Color> listColor = new ArrayList<Color>();
    
    
    public static Line newData(ArrayList<Integer> time, ArrayList<Integer> colors) {
        Line list = new Line();
        list.type = Data.LINE;
        
        for (int id=0; id<colors.size(); id++) {
            Color color = ColorMake(colors.get(id));
            list.listColor.add(color);
        }
        list.listTime = time;
        return list;
    }
    
}
