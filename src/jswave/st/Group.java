/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jswave.st;

import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;
import jswave.Util;

/**
 *
 * @author lenovo
 */
public class Group extends Widget {
    
    public static final ArrayList<Group> array = new ArrayList<Group>();

    public static ArrayList<Group> getArray() {
        return array;
    }

    public static void clear() {
        array.clear();
    }

    public static int add(Group data) {
        array.add(data);
        return array.size() - 1;
    }

    public static Group get(int id) {
        return array.get(id);
    }

    public static int size() {
        return array.size();
    }

    public static int add(String name, int start, int end, int colori, boolean enable) {
        Group group = new Group();
        group.name = name;
        group.start = start;
        group.end = end;
        group.color = Util.colorMake(colori);
        group.enable = enable;

        return Group.add(group);
    }
    
    public static boolean getStatus(int id) {
        return array.get(id).enable;
    }
    
    public String name;

    public int start;
    public int end;

    public int y0;
    public int y2;

    public Color color;

    public boolean enable;
    
    public boolean getEnable() {
        return enable;
    }

    public boolean isInGroup(int l) {
        if (l >= start && l <= end) {
            return true;
        }
        else {
            return false;
        }
    }

    private static int getLineGroup(int line) {
        for (int id=0; id<Group.size(); id++) {
            if (Group.get(id).isInGroup(line)) {
                return id;
            }
        }
        return -1;
    }

    public static void updateGroupStatus() {
        for (int id=0; id<Wave.size(); id ++ ) {
            Wave list = Wave.get(id);
            list.group = getLineGroup(id);
            
            if (list.group >= 0) {
                list.enable = Group.get(list.group).enable;
            }
            else {
                list.enable = true;
            }
        }
        for (Connection c : Connection.getArray()) {
            c.enable = Wave.get(c.start).enable && Wave.get(c.end).enable;
        }
    }
    
    public void draw(Graphics g, double wdt, int g_gap) {

        y0 = Wave.getLineY0(start) - g_gap/3*2;
        y2 = Wave.getLineY2(end) + g_gap/3 - 5;

        g.setColor(color);

        if(start >= offsetLine) {
            g.fillRect(offsetX, y0, screenW-offsetX, 3);
            g.fillRect(offsetX, y0, 3, 7);
        }
        if (end >= offsetLine) {
            g.fillRect(offsetX, y2, screenW-offsetX, 3);
            g.fillRect(offsetX, y2 - 4, 3, 7);
        }

        g.fillRect(0, y0, offsetX-2, Util.fontHeight + 2);
        drawName(g, name, y0 + Util.fontHeight, Util.fontHeight);
    }
    
    public static void show(Graphics g, double wdt, int g_gap) {
        for (Group group : Group.getArray()) {
            group.draw(g, wdt, g_gap);
        }
    }
}


