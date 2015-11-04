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
public class Histogram extends Data{
    public int width;
    public ArrayList<Color> listColor = new ArrayList<Color>();
    public ArrayList<Integer> listY = new ArrayList<Integer>();
    
    public static Histogram newData(ArrayList<Integer> time, ArrayList<Integer> ys, ArrayList<Integer> colors, int widthItem, int heightMax) {
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
        
        list.heightMax = heightMax;
        list.listTime = time;
        list.width = widthItem;
        return list;
    }    
    
}
