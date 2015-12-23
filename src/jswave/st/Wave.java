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
 * @author cttmayi
 */
public class Wave extends Widget{
    
    public static final ArrayList<Wave> array = new ArrayList<Wave>();

    public static ArrayList<Wave> getArray() {
        return array;
    }

    public static void clear() {
        array.clear();
    }
    
    public static int add(Wave data) {
        array.add(data);
        return array.size() - 1;
    }
    
    public static Wave get(int id) {
        return array.get(id);
    }

    public static int size() {
        return array.size();
    }
    
    public static int getLineY0(int line) {
        if (line - offsetLine >= 0) {
            return Wave.get(line).getY0();
        }
        else {
            return offsetY;
        }
    }

    public static int getLineY1(int line) {
        if (line - offsetLine >= 0) {
            return Wave.get(line).getY1();
        }
        else {
            return offsetY;
        }
    }

    public static int getLineY2(int line) {
        if (line - offsetLine >= 0) {
            return Wave.get(line).getY2();
        }
        else {
            return offsetY;
        }
    }

    public static int getLineM(int line) {
        return (getLineY0(line) + getLineY2(line))/2;
    }

    public static int LINE = 0;
    public static int DIAGRAM = 1;
    public static int HISTOGRAM = 2;

    private static final Color colorSplitLine = new Color(255,0,0,30);

    public int type;

    private String name = null;

    public Color outBorderColor = null;

    private final ArrayList<Touch> touchs;

    private int heightMax = 10;

    private int yStart = 0;
    private int yEnd = 0;
    
    private boolean isCallbackEnable;

    public Wave() {
        this.isCallbackEnable = true;
        this.touchs = new ArrayList<Touch>();
    }

    public void setHeightMax(int height) {
        if (heightMax < height) {
            heightMax = height;
        }
    }

    public int getHeightMax() {
        return heightMax;
    }

    public void clearTouch() {
        touchs.clear();
    }

    public void addTouch (int id, int x1, int y1, int x2, int y2) {
        touchs.add(Touch.newData(id, x1, y1, x2, y2));
    }

    public void setCallbackEnable(boolean enable) {
        isCallbackEnable = enable;
    }

    public boolean inLine (int y) {
        return getY0() <= y && y <= getY2();
    }

    public int getCallbackId(int x, int y) {
        int id = -1;
        if (isCallbackEnable && enable && inLine(y)) {
            for (Touch touch: touchs) {
                if (touch.isInRange(x, y)) {
                    id =  touch.getId();
                }
                else if (id >=0) {
                    break;
                }
            }
        }
        return id;
    }

    public boolean inNameZone(int x, int y) {
        int id = -1;
        if (isCallbackEnable && inLine(y)) {
            if (x < offsetX) {
                return enable;
            }
        }
        return false;
    }

    public void setY(int start, int end) {
        yStart = start;
        yEnd = end;
    }

    public int getY0() {
        if (enable) {
            return yEnd - heightMax;
        }
        else {
            return yEnd;
        }
    }

    public int getY1() {
        return yStart;
    }

    public int getY2() {
        return yEnd;
    }

    public int getYM() {
        return (yStart + yEnd)/2;
    }    

    public void setTime(ArrayList<Integer> time) {
        listTime = time;    
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }

    public ArrayList<Integer> getX(int ts, int xs, double wdt) {
        listX = timeToX(listTime, ts, xs, wdt);
        return listX;
    }

    public ArrayList<Integer> listTime = new ArrayList<Integer>();
    public ArrayList<Integer> listX = new ArrayList<Integer>();

    public ArrayList<Integer> timeToX(ArrayList<Integer> times,  int ts, int xs, double wdt) {
        ArrayList<Integer> x = new ArrayList<Integer>();
        for (Integer t:times) {
            x.add(Util.getX(t, xs,ts, wdt));
        }
        return x;
    }

    public static Color ColorMake(int colori) {
        return Util.colorMake(colori);
    }

    public void draw(Graphics g, int y, double wdt) {
        
    }

    
    public static void show(Graphics g, int y, double wdt, int gap, int g_gap) {
        int groupId = -1;
        for (int id=offsetLine; id<Wave.size(); id ++) {
            Wave list = Wave.get(id);
            if (groupId != list.group) {
                y += g_gap;
                groupId = list.group;
            }
            else if (list.enable) {
                y += gap;
            }

            if (list.enable) {
                y += list.getHeightMax();

                g.setColor(colorSplitLine);
                g.fillRect(0, y, screenW, 1);
                
                if (y < screenH) {
                    list.draw(g, y, wdt);
                }
                else {
                    list.setY(y, y);
                }
            }
            else {
                list.setY(y, y);
            }
        }
    }
    
}
