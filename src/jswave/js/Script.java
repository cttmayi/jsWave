/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jswave.js;

import java.util.ArrayList;
import jswave.JsWaveJFrame;
import jswave.ui.WaveJPanel;

/**
 *
 * @author lenovo
 */
public class Script {
    
    static Script script;
    public static Script getScript() {
        if (script == null) {
            script = new Script();
        }
        return script;
    }
    
    WaveJPanel panel;
    JsWaveJFrame frame;
    

    public void setPanel(WaveJPanel panel) {
        this.panel = panel;
    }
    
    public void addLine(String name, ArrayList<Double> times, ArrayList<Double> colors) {
        
        ArrayList<Integer> timei = new ArrayList<Integer>();
        ArrayList<Integer> colori = new ArrayList<Integer>();
        
        //System.out.println("TIME TYPE:" + times.get(0).getClass().getName());
        //System.out.println("COLOR TYPE:"+ colors.get(0).getClass().getName());
        
        if (!times.isEmpty()) {
            for (double t: times) {
                timei.add((int) t);
            }
            for (double c: colors) {
                colori.add((int) c);
            }        

            panel.addLine(name, timei, colori);
        }
                
    }

    public void addConnection(double time, double start, double end, double color) {
        panel.addConnection((int)time, (int)start, (int)end, (int)color);
    }
    
    
    public void print(String str) {
        System.out.println(str);
    }
    
}
