/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jswave.st;


import java.awt.Graphics;
import java.util.ArrayList;

import static jswave.st.Widget.offsetX;
import static jswave.st.Widget.screenW;
import static jswave.st.Widget.timeX;

/**
 *
 * @author lenovo
 */
public class Nodes extends Wave{
    
    public ArrayList<Integer> listTimeEnd = new ArrayList<Integer>();
    public ArrayList<Integer> listNode = new ArrayList<Integer>();
    public ArrayList<String> listName = new ArrayList<String>();
    
    
    public static int add(String name, int height, ArrayList<Integer> startTimes, ArrayList<Integer> endTimes, ArrayList<Integer> nodeIndexs, ArrayList<String> nodeNames) {
        
        Nodes wave = new Nodes();
        wave.type = Wave.NODES;
        wave.setName(name);
        wave.setHeightMax(height);
        
        wave.listTime = startTimes;
        wave.listTimeEnd = endTimes; 
        wave.listNode = nodeIndexs;
        wave.listName = nodeNames;
        
        return  Wave.add(wave);
    }
    
    public ArrayList<Integer> getX2(int ts, int xs, double wdt) {
        listX = timeToX(listTimeEnd, ts, xs, wdt);
        return listX;
    }
    
    public ArrayList<Integer> getW(double wdt) {
        listX = timeToW(listTime, listTimeEnd, wdt);
        return listX;
    }
    @Override
    public void draw(Graphics g, int y, double wdt) {
        ArrayList<Integer> x = getX(timeX, offsetX, wdt);
        ArrayList<Integer> x2 = getX2(timeX, offsetX, wdt);
        //ArrayList<Integer> ws = getW(wdt);

        setY(y - getHeightMax(), y);
        clearTouch();
        drawName(g, getName(), y, getHeightMax());
        int lastX = -1;
        int lastH = -1;
        for (int id=0; id<x.size(); id++) {
            int xx = x.get(id);
            int xx2 = x2.get(id);

            if (xx > screenW) break;
            if (xx2 < offsetX) continue;

            if (xx < offsetX) xx = offsetX;

            int w = xx2 - xx + 1;
            
            Node node = Node.get(listNode.get(id));
            
            if (xx == lastX && lastH >= node.getHeight()){
                xx = xx + 1;
                w = w - 1;
            }
            
            if (w != 0) {
                //System.out.println("Draw:" + id + ":" + xx + ":" + xx2 + " w:" + w);
                String name = null;
                if (id < listName.size()) {
                    name = listName.get(id);
                }

                
                node.draw(g, name, xx, y, w);
                addTouch(id, xx, y-node.getHeight(), xx+w, y);
                lastX = xx2;
                lastH = node.getHeight();
            }
        }        
    }
    
    
}
