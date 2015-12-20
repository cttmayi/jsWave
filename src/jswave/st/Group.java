/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jswave.st;

import java.awt.Color;
import java.awt.Graphics;
import jswave.Util;
import jswave.ui.WaveJPanel;

/**
 *
 * @author lenovo
 */
public class Group extends Widget {
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

    public void draw(Graphics g, WaveJPanel panel, double wdt, int g_gap) {

        y0 = panel.getLineY0(start) - g_gap/3*2;
        y2 = panel.getLineY2(end) + g_gap/3 - 5;

        

        g.setColor(color);

        if(start >= offsetLine) {
            g.fillRect(offsetX, y0, screenW-offsetX, 3);
            g.fillRect(offsetX, y0, 3, 7);
        }
        if (end >= offsetLine) {
            g.fillRect(offsetX, y2, screenW-offsetX, 3);
            g.fillRect(offsetX, y2 - 4, 3, 7);
        }
        
        
        
        
        //if (!enable) {
            g.fillRect(0, y0, offsetX-2, Util.FontHeight + 2);
            drawName(g, name, y0 + Util.FontHeight, Util.FontHeight);
        //}
        
    }
}


