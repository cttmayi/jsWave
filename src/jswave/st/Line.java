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
public class Line extends Data {
    public static final int lineHeight = 3;
    
    
    public ArrayList<Color> listColor = new ArrayList<Color>();
    public ArrayList<Integer> listY = new ArrayList<Integer>();
    public ArrayList<String> listName = new ArrayList<String>();
    
    
    
    public static Line newData(ArrayList<Integer> time, ArrayList<Integer> colors, ArrayList<Integer> ys, ArrayList<String> names) {
        Line list = new Line();
        list.type = Data.LINE;
        
        for (int id=0; id<colors.size(); id++) {
            Color color = ColorMake(colors.get(id));
            list.listColor.add(color);
        }
        list.listTime = time;
        list.listY = ys;
        list.listName = names;
        
        for (int y: ys) {
            list.setHeightMax(y + Util.FontHeight);
        }

        return list;
    }
    
}
