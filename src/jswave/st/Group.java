/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jswave.st;

import java.awt.Color;

/**
 *
 * @author lenovo
 */
public class Group extends Widget {
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
    
    public boolean isStart(int l) {
        return start == l;
    }

    public boolean isEnd(int l) {
        return end == l;
    }
}


