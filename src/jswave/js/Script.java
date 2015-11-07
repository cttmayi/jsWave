/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jswave.js;

import java.util.ArrayList;
import jswave.JsWaveJFrame;
import jswave.Util;

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

    public void setConnectionListener(String name) {
        if (name.equals("")) {
            this.frame.getPanel().funConnectionListener = null;
        }
        else {
            this.frame.getPanel().funConnectionListener = name;
        }
    }
    
    
    
    public int addLine(String name, ArrayList<Double> times, ArrayList<Double> colors, ArrayList<String> names) {
        
        ArrayList<Integer> timei = Util.d2t(times);
        ArrayList<Integer> colori = Util.d2t(colors);
        
        //System.out.println("TIME TYPE:" + times.get(0).getClass().getName());
        //System.out.println("COLOR TYPE:"+ colors.get(0).getClass().getName());
        //System.out.println(names.getClass().getName());
        
        if (!times.isEmpty()) {
            return this.frame.getPanel().addLine(name, timei, colori, names);
        }
        return -1;
    }

    public int addHistogram(String name, ArrayList<Double> times, ArrayList<Double> times2, 
            ArrayList<Double> ys, ArrayList<Double> colors, ArrayList<String> names, int heightMax) {

        ArrayList<Integer> timei = Util.d2t(times);
        ArrayList<Integer> timei2 = Util.d2t(times2);
        ArrayList<Integer> yi = Util.d2t(ys);
        ArrayList<Integer> colori = Util.d2t(colors);
        
        if (!times.isEmpty()) {   
            return this.frame.getPanel().addHistogram(name, timei, timei2, yi, colori, names, heightMax);
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

    public void setInfo(ArrayList<Double> xs, ArrayList<Double> ys, 
            ArrayList<String> names, ArrayList<Double> fgcs, ArrayList<Double> bgcs) {
        
        ArrayList<Integer> xi = Util.d2t(xs);
        ArrayList<Integer> yi = Util.d2t(ys);
        ArrayList<Integer> fi = Util.d2t(fgcs);
        ArrayList<Integer> bi = Util.d2t(bgcs);       
        
        this.frame.getPanel().setInfo(xi, yi, names, fi, bi);
    }
    
    
    public void print(String str) {
        System.out.println(str);
    }
    
}
