/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jswave.js;

import java.util.ArrayList;
import jswave.ui.JsWaveJFrame;
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
    
    public void setTimeRuler(Number us, Number us2) {
        this.frame.getPanel().setTimeRuler(us.longValue(), us2.longValue());
    }
    
    public void setTimePoint(Number us) {
        this.frame.getPanel().setTimePoint(us.intValue());
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

    public void setWaveListener(String name) {
        if (name.equals("")) {
            this.frame.getPanel().funWaveListener = null;
        }
        else {
            this.frame.getPanel().funWaveListener = name;
        }
    }
    
    public void setClickListener(String name) {
        if (name.equals("")) {
            this.frame.getPanel().funClickListener = null;
        }
        else {
            this.frame.getPanel().funClickListener = name;
        }
    }    

    public void setNameListener(String name) {
        if (name.equals("")) {
            this.frame.getPanel().funNameListener = null;
        }
        else {
            this.frame.getPanel().funNameListener = name;
        }
    }    
    
    public int addGroup(String name, Number start, Number end, Number colori, boolean enable) {
        return this.frame.getPanel().addGroup(name, start.intValue(), end.intValue(), colori.intValue(), enable);
    }
    
    public void setWaveOutBorderColor(int id, int color) {
        this.frame.getPanel().setWaveOutBorderColor(id, color);
    }
    
    public int addLine(String name, ArrayList<Number> times, ArrayList<Number> colors, ArrayList<Number> ys, ArrayList<String> names) {
        ArrayList<Integer> timei = Util.an2i(times);
        ArrayList<Integer> colori = Util.an2i(colors);
        ArrayList<Integer> yi = Util.an2i(ys);

        if (Util.isDebug) {
            System.out.println("addLine:" + name);
            int time = Integer.MIN_VALUE;
            
            for (int i=0; i<timei.size(); i++) {
                if (time > timei.get(i)) {
                    System.out.println("[ERROR] ROW:" + i + " ORDER ERROR!");
                }
                time = timei.get(i);
            }
        }
        
        if (!times.isEmpty()) {
            return this.frame.getPanel().addLine(name, timei, colori, yi, names);
        }
        return -1;
    }

    public int addHistogram(String name, ArrayList<Number> times, ArrayList<Number> times2, 
            ArrayList<Number> ys, ArrayList<Number> colors, ArrayList<String> names, int heightMax) {

        ArrayList<Integer> timei = Util.an2i(times);
        ArrayList<Integer> timei2 = Util.an2i(times2);
        ArrayList<Integer> yi = Util.an2i(ys);
        ArrayList<Integer> colori = Util.an2i(colors);
        
        if (Util.isDebug) {
            System.out.println("addHistogram:" + name);
            int time = Integer.MIN_VALUE;
            
            for (int i=0; i<timei.size(); i++) {
                if (time > timei.get(i)) {
                    System.out.println("[ERROR] ROW:" + i + " ORDER ERROR!");
                }
                time = timei2.get(i);
                
                if (timei.get(i) > timei2.get(i)) {
                    System.out.println("[ERROR] ROW:" + i + " SIZE ERROR!");
                }
                
                if (yi.get(i) > heightMax) {
                    System.out.println("[ERROR] Y:" + i + " HEIGHT ERROR!");
                }
            }
        }
        
        if (!times.isEmpty()) {   
            return this.frame.getPanel().addHistogram(name, timei, timei2, yi, colori, names, heightMax);
        }
        
        return -1;
    }
    
    public int addConnection(double time, double start, double end, double color) {
        return this.frame.getPanel().addConnection((int)time, (int)start, (int)end, (int)color);
    }
    
    public void setTable(ArrayList<String> names, ArrayList<String> datas, ArrayList<String> datars) {
        this.frame.setTable(names, datas, datars);
    }
    
    public void setText(String text) {
        this.frame.setText(text);
    }

    public void setInfo(ArrayList<Number> xs, ArrayList<Number> ys, 
            ArrayList<String> names, ArrayList<Number> fgcs, ArrayList<Number> bgcs) {
        
        ArrayList<Integer> xi = Util.an2i(xs);
        ArrayList<Integer> yi = Util.an2i(ys);
        ArrayList<Integer> fi = Util.an2i(fgcs);
        ArrayList<Integer> bi = Util.an2i(bgcs);       
        
        this.frame.getPanel().setInfo(xi, yi, names, fi, bi);
    }
    
    public void debug(Number debug) {
        Util.isDebug = (debug.intValue() != 0);
    }
    
    public void print(String str) {
        System.out.println(str);
    }
    
    
    
    
}
