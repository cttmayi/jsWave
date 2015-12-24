/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jswave.js;

import java.util.ArrayList;
import jswave.ui.JsWaveJFrame;
import jswave.Util;
import jswave.st.Connection;
import jswave.st.Group;
import jswave.st.Histogram;
import jswave.st.Info;
import jswave.st.Line;
import jswave.st.TimeRuler;
import jswave.st.Wave;

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
        TimeRuler.getData().setTimeRuler(us.longValue(), us2.longValue());
    }

    public void setTimeRange(Number x, Number w) {
        this.frame.getPanel().setTimeRange(x.intValue(), w.intValue());
    }

    public int getTimeOffset() {
        return this.frame.getPanel().getTimeOffset();
    }

    public int getTimeWidth() {
        return this.frame.getPanel().getTimeWidth();
    }

    public void setTimePoint(Number us, Number color) {
        TimeRuler.getData().setTimePoint(us.intValue(), color.intValue());
    }

    public void addKeyListener(String key, String func) {
        this.frame.addKeyListener(key, func);
    }

    public void setRangeListener(String name) {
        if (name.equals("")) {
            this.frame.getPanel().funSelectRangeListener = null;
        }
        else {
            this.frame.getPanel().funSelectRangeListener = name;
        }
    }

    public void setSelectListener(String name) {
        if (name.equals("")) {
            this.frame.getPanel().funSelectItemListener = null;
        }
        else {
            this.frame.getPanel().funSelectItemListener = name;
        }
    }

    public void setConnectionListener(String name) {
        if (name.equals("")) {
            this.frame.getPanel().funConnectionMoveListener = null;
        }
        else {
            this.frame.getPanel().funConnectionMoveListener = name;
        }
    }

    public void setWaveListener(String name) {
        if (name.equals("")) {
            this.frame.getPanel().funWaveMoveListener = null;
        }
        else {
            this.frame.getPanel().funWaveMoveListener = name;
        }
    }

    public void setClickListener(String name) {
        if (name.equals("")) {
            this.frame.getPanel().funWaveClickListener = null;
        }
        else {
            this.frame.getPanel().funWaveClickListener = name;
        }
    }    

    public void setNameListener(String name) {
        if (name.equals("")) {
            this.frame.getPanel().funWaveNameListener = null;
        }
        else {
            this.frame.getPanel().funWaveNameListener = name;
        }
    }    

    public int addGroup(String name, Number start, Number end, Number colori, boolean enable) {
        return Group.add(name, start.intValue(), end.intValue(), colori.intValue(), enable);
    }

    public void clearGroup() {
        Group.clear();
    }

    public boolean getGroupStatus(int id) {
        return Group.getStatus(id);
    }
    
    public void setWaveOutBorderColor(int id, int color) {
        if (id < Wave.size() ) {
            Wave wave =  Wave.get(id);
            wave.outBorderColor = Util.colorMake(color);
        }
        else if (Util.isDebug) {
            System.out.println("[ERROR][setWaveOutBorderColor] ID:" + id + " LINE ERROR! (>=" + Wave.size() + ")");
        }
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
            this.frame.getPanel().updateTimeLimit(timei);
            return Line.add(name, timei, colori, yi, names);
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
            this.frame.getPanel().updateTimeLimit(timei);
            this.frame.getPanel().updateTimeLimit(timei2);
            return Histogram.add(name, timei, timei2, yi, colori, names, heightMax);
        }

        return -1;
    }

    public void clearWave() {
        Info.clear();
        Connection.clear();
        Group.clear();
        Wave.clear();
    }

    public int addConnection(Number time, Number start, Number end, Number color) {
        if (start.intValue() < Wave.size() && end.intValue() < Wave.size()) {
            Connection.add(time.intValue(), start.intValue(), end.intValue(), color.intValue());
        }
        else if (Util.isDebug){
            System.out.println("[ERROR][addConnection] START:" + start + " END:" + end + " LINE ERROR! (>=" + Wave.size() + ")");
        }
        return -1;
    }

    public void clearConnection() {
        Connection.clear();
    }

    public void setTable(ArrayList<String> names, ArrayList<String> datas, ArrayList<String> datars) {
        this.frame.setTable(names, datas, datars);
    }

    public void setText(String text) {
        this.frame.setText(text);
    }

    public void setInfo(ArrayList<Number> times, ArrayList<Number> lines, 
            ArrayList<String> names, ArrayList<Number> fgcs, ArrayList<Number> bgcs) {
        ArrayList<Integer> ti = Util.an2i(times);
        ArrayList<Integer> li = Util.an2i(lines);
        ArrayList<Integer> fi = Util.an2i(fgcs);
        ArrayList<Integer> bi = Util.an2i(bgcs);
        
        Info.clear();
        for (int id=0; id<ti.size(); id++) {
            Info.add(ti.get(id), li.get(id), names.get(id), fi.get(id), bi.get(id));
        }
    }

    public void debug(Number debug) {
        Util.isDebug = (debug.intValue() != 0);
    }

    public void print(String str) {
        System.out.println(str);
    }
}
