/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jswave.js;

import java.util.ArrayList;
import jswave.JsWaveJFrame;

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
    
    JsWaveJFrame frame;
    
    public void setFrame(JsWaveJFrame frame) {
        this.frame = frame;
    }
    
    public void setRangeListener(String name) {
        if (name.equals("")) {
            this.frame.getPanel().funRangeListener = null;
        }
        else {
            this.frame.getPanel().funRangeListener = name;
        }
    }

    public void setSelectListener(String name) {
        if (name.equals("")) {
            this.frame.getPanel().funSelectListener = null;
        }
        else {
            this.frame.getPanel().funSelectListener = name;
        }
    }
    
    public int addLine(String name, ArrayList<Double> times, ArrayList<Double> colors, ArrayList<String> names) {
        
        ArrayList<Integer> timei = new ArrayList<Integer>();
        ArrayList<Integer> colori = new ArrayList<Integer>();
        
        //System.out.println("TIME TYPE:" + times.get(0).getClass().getName());
        //System.out.println("COLOR TYPE:"+ colors.get(0).getClass().getName());
        System.out.println(names.getClass().getName());
        
        if (!times.isEmpty()) {
            for (double t: times) {
                timei.add((int) t);
            }
            for (double c: colors) {
                colori.add((int) c);
            }        

            return this.frame.getPanel().addLine(name, timei, colori, names);
        }
        return -1;
    }

    public void addConnection(double time, double start, double end, double color) {
        this.frame.getPanel().addConnection((int)time, (int)start, (int)end, (int)color);
    }
    
    public void setTable(ArrayList<String> names, ArrayList<String> datas, ArrayList<String> datars) {
        this.frame.setTable(names, datas, datars);
    }
    
    public void setText(String text) {
        this.frame.setText(text);
    }
    
    public void print(String str) {
        System.out.println(str);
    }
    
}
