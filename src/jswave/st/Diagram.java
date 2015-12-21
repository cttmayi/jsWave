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
 * @author cttmayi
 */
public class Diagram extends Wave{
    public Color color;

    public int width;
    public ArrayList<Integer> listY = new ArrayList<Integer>();

    public static Diagram newData(ArrayList<Integer> time, ArrayList<Integer> ys, int heightMax, int color) {
        Diagram list = new Diagram();
        list.type = Wave.DIAGRAM;
        list.color = ColorMake(color);
        list.listTime = time;
        for (Integer y : ys) {
            if (heightMax < y) {
                y = heightMax;
            }
            list.listY.add(y);
        }
        list.setHeightMax(heightMax);
        return list;
    }

}
