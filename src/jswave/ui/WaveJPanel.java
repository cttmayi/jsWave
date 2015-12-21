/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jswave.ui;


import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.util.ArrayList;

import jswave.Util;
import jswave.js.JsEnv;
import jswave.st.*;

/**
 *
 * @author cttmayi
 */
public final class WaveJPanel extends javax.swing.JPanel {

    private int timeX, timeW;
    private int offsetLine;
    private int rangeSelectX1, rangeSelectX2;

    private int itemSelected;
    private int itemConnectionMoveSelected;
    private int itemWaveMoveSelected;
    private int itemNameMoveSelected;

    private int timeLimitX1, timeLimitX2; 

    public String funSelectItemListener;
    public String funSelectRangeListener;
    public String funConnectionMoveListener;
    public String funWaveMoveListener;
    public String funWaveClickListener;
    public String funWaveNameListener;

    private final int waveOffsetX = 100, waveOffsetY = 42;
    
    private final Color colorSelect = new Color(255,0,0,30);
    private final Color colorFont = Color.black;    

    /**
     * Creates new form wavaJPanel
     */
    public WaveJPanel() {
        clearUI();
        initComponents();
        initMouse();
    }
    
    public void clearUI() {
        Wave.clear();
        Info.clear();
        Connection.clear();
        Group.clear();
        
        funSelectItemListener = null;
        funSelectRangeListener = null;
        funConnectionMoveListener = null;
        funWaveMoveListener = null;
        funWaveClickListener = null;
        funWaveNameListener = null;
        
        offsetLine = 0;

        rangeSelectX1 = -1; 
        rangeSelectX2 = -1;

        itemSelected = -1;
        itemConnectionMoveSelected = -1;
        itemWaveMoveSelected = -1;
        itemNameMoveSelected = -1;

        timeLimitX1 = Integer.MAX_VALUE;
        timeLimitX2 = Integer.MIN_VALUE;
    }

    public void setTimeRange(int x, int w) {
        timeX = x;
        timeW = w;

        int lx1 = timeLimitX1 - (int)((timeLimitX2 - timeLimitX1) * 0.1);
        int lx2 = timeLimitX2 + (int)((timeLimitX2 - timeLimitX1) * 0.1);

        int max = (int)(lx2 - lx1);
        int min = 800;

        if (timeX < lx1) timeX = lx1;
        if (timeW <= min) timeW = min;
        if (max < timeW) timeW = max;
        if (timeX + timeW > lx1 + max) timeX = lx1 + max - timeW;

        repaint();
    }

    public void moveLeft() {
        setTimeRange(timeX - timeW/10, timeW);
    }

    public void moveRight() {
        setTimeRange(timeX + timeW/10, timeW);
    }    

    public void scalerDown() {
        int w = timeW * 4 / 3;
        setTimeRange(timeX , w);
    }

    public void scalerUp() {
        int w = timeW * 3 / 4;
        setTimeRange(timeX , w);
    }   

    private int getW() {
        return getWidth() - waveOffsetX;
    }

    public void setOffsetLine(int line) {
        offsetLine = line;
    }

    public int getSelected() {
        int selected = itemSelected;
        if (selected >=0) {
            selected += offsetLine;
        }
        return selected;
    }

    private int getLine(int y) {
        int l = -1;
        for (int i=0; i<Wave.size(); i++) {
            Wave wave = Wave.get(i);
            if (wave.getY2() > y) {
               l = i;
               break;
            }
        }
        return l;
    }

    private int getLineGroup(int line) {
        for (int id=0; id<Group.size(); id++) {
            if (Group.get(id).isInGroup(line)) {
                return id;
            }
        }
        return -1;
    }

    public void updateGroupStatus() {
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

    public void updateTimeLimit(ArrayList<Integer> time) {
        int x2 = time.get(time.size()-1);
        int x1 = time.get(0);
        if (timeLimitX1 > x1) {
            timeLimitX1 = x1;
        }
        if (timeLimitX2 < x2) {
            timeLimitX2 = x2;
        }
    }

    public void setInfo(ArrayList<Integer> xs, ArrayList<Integer> ys, 
            ArrayList<String> names, ArrayList<Integer> fgcs, ArrayList<Integer> bgcs) {
        Info.clear();
        for (int id=0; id<xs.size(); id++) {
            Info.add(xs.get(id), ys.get(id), names.get(id), fgcs.get(id), bgcs.get(id));

        }
    }

    public void drawTimeRuler(Graphics g, int y) {
        TimeRuler.getData().draw(g, y);
    }

    private void drawConnection(Graphics g, double wdt) {
        for (Connection data : Connection.getArray()) {
            data.draw(g, wdt);
        }
    }

    private void drawGroup(Graphics g, double wdt, int g_gap) {
        for (Group group : Group.getArray()) {
            group.draw(g, wdt, g_gap);
        }
    }

    private void drawInfo(Graphics g) {
        for (Info info: Info.getArray()) {
            Widget.drawString(g, info.info, getX(info.x), Wave.getLineM(info.y), Integer.MAX_VALUE, Integer.MAX_VALUE, 
                    false, info.fgColor, info.bgColor);
        }
    }

    private void drawFrame(Graphics g) {
        g.setColor(Color.black);
        g.drawLine(waveOffsetX - 3, 0, waveOffsetX - 3, getHeight());

        //draw range
        if (rangeSelectX1 >= 0 && rangeSelectX2 >= 0) {
            int  x1 = Math.min(rangeSelectX1, rangeSelectX2);
            int  x2 = Math.max(rangeSelectX1, rangeSelectX2);

            g.setColor(colorSelect);
            g.fillRect(x1, 0, x2 - x1, getHeight());
            int t1 = getTime(x1);
            int t2 = getTime(x2);
            int t = t2 - t1;
            g.setColor(Color.blue);

            g.drawString(Util.getTimeString(t), 
                        x1, waveOffsetY + Util.fontHeight);            
            g.drawString(String.format("%s ~ %s",Util.timeFormatS(t1,t), Util.timeFormatS(t2,t)), 
                        x1, waveOffsetY + Util.fontHeight * 2);

            long timeOffset = TimeRuler.getData().getTimeOffset();
            long timeOffset2 = TimeRuler.getData().getTimeOffset2();

            if (timeOffset != 0) {
                g.drawString(String.format("%s ~ %s", Util.timeFormatHMS(t1+ timeOffset, t), Util.timeFormatHMS(t2 + timeOffset, t)),
                        x1, waveOffsetY + Util.fontHeight * 3);
            }
            if (timeOffset2 != 0) {
                g.drawString(String.format("%s ~ %s", Util.timeFormatS(t1 + timeOffset2, t), Util.timeFormatS(t2 + timeOffset2, t)),
                        x1, waveOffsetY + Util.fontHeight * 4);
            }
        }

        int y0 = Wave.getLineY0(itemSelected);
        int y2 = Wave.getLineY2(itemSelected);
        if (itemSelected >= 0 && itemSelected < Wave.size() && y2 != y0) {
            g.setColor(colorSelect);
            g.fillRect(0, y0, getWidth(), y2 - y0);
        }
    }
    
    private void drawSplitLine(Graphics g, int y, int h, Color color) {
        g.setColor(color);
        g.fillRect(0, y, getWidth(), h);
    }

    @Override
    public void paintComponent(Graphics g){ 
        super.paintComponent(g); 

        Widget.timeX = timeX;
        Widget.timeW = timeW;
        Widget.offsetLine = offsetLine;

        Widget.offsetX = waveOffsetX;
        Widget.offsetY = waveOffsetY;

        Widget.screenW = getWidth();
        Widget.screenH = getHeight();

        Widget.colorFont = colorFont;

        g.setFont(Util.fontArial);

        double wdt = (double)getW() / timeW;
        int gap = 5;
        int g_gap = Util.fontHeight / 2 * 3;

        drawTimeRuler(g, 0);

        int y = waveOffsetY;
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
                drawSplitLine(g, y, 1, colorSelect);
                if (y < getHeight()) {
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
        drawConnection(g, wdt);
        drawGroup(g, wdt, g_gap);

        drawInfo(g);
        drawFrame(g);
    }

    private int getTime(int x){
        return Util.getTime(x, waveOffsetX, getW(), timeX, timeW);
    }

    private int getX(int t) {
        return Util.getX(t, waveOffsetX, getW(), timeX, timeW);
    }

    private boolean inConnectionRange(int id, int x, int y) {
        Connection c = Connection.getArray().get(id);
        Wave d1;
        Wave d2;

        if (c.start < c.end) {
            d1 = Wave.get(c.start);
            d2 = Wave.get(c.end);
        }
        else {
            d2 = Wave.get(c.start);
            d1 = Wave.get(c.end);         
        }

        int l1 = d1.getY2(); 
        int l2 = d2.getY1();

        int xx = getX(c.time);
        int x1 = xx - 4;
        int x2 = xx + 4;

        return (x > x1 && x < x2 &&  y > l1 && y < l2);
    }
    
    void doMoveCallback(int x, int y) {
        if (funConnectionMoveListener != null) {
            int id;
            int size = Connection.size();
            for (id =0; id<size; id++) {
                if (inConnectionRange(id, x, y) ) {
                    break;
                }
            }
            if (id == size) id = -1;
            if (itemConnectionMoveSelected != id) {
                itemConnectionMoveSelected = id;
                if (id > 0) {
                    JsEnv.getJsEnv(null).invokeFunction(funConnectionMoveListener, id, Connection.get(id).getTime(), getLine(y));
                    repaint();
                    return;
                }
                else {
                    JsEnv.getJsEnv(null).invokeFunction(funConnectionMoveListener, id, getTime(x), getLine(y));
                    repaint();
                }
            }
        }

        if (funWaveMoveListener != null || funWaveNameListener != null) {
            int i;
            int size = Wave.size();
            for (i=0; i<size; i++) {
                Wave wave = Wave.get(i);

                if (wave.inNameZone(x, y)) {
                    itemNameMoveSelected = i;
                    JsEnv.getJsEnv(null).invokeFunction(funWaveNameListener, getTime(x), i);
                    repaint();
                    return;                    
                }

                int id = wave.getCallbackId(x, y);
                if (id >= 0) {
                    itemWaveMoveSelected = id;
                    JsEnv.getJsEnv(null).invokeFunction(funWaveMoveListener, id, getTime(x), i);
                    repaint();
                    return;
                }
            }
            if (itemWaveMoveSelected != -1) {
                itemWaveMoveSelected = -1;
                JsEnv.getJsEnv(null).invokeFunction(funWaveMoveListener, -1, getTime(x), -1);
                repaint();
            }
            if (itemNameMoveSelected != -1) {
                itemNameMoveSelected = -1;
                JsEnv.getJsEnv(null).invokeFunction(funWaveNameListener, getTime(x), -1);
                repaint();
            }
        }
    }

    private boolean doClickCallback(int line, int x, int y) {
        if (funWaveClickListener != null) {
            Wave wave = Wave.get(line);
            int id = wave.getCallbackId(x, y);
            if (id > 0) {
                JsEnv.getJsEnv(null).invokeFunction(funWaveClickListener, id, getTime(x), line);
                repaint();
                return true;
            }
        }
        return false;
    }
    
    
    private void initMouse() {
        MouseAdapter mouseListen = new MouseAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                rangeSelectX2 = e.getX();
                if (rangeSelectX2 > getWidth()) {
                    rangeSelectX2 = getWidth();
                }
                if (rangeSelectX2 < waveOffsetX) {
                    rangeSelectX2 = waveOffsetX;
                }
                repaint();
                super.mouseDragged(e);  
            }

            @Override
            public void mouseMoved(MouseEvent e) {
                doMoveCallback(e.getX(), e.getY());

                super.mouseMoved(e);  
            }

            @Override
            public void mousePressed(MouseEvent e) {
                if (e.getX() > waveOffsetX) {
                    rangeSelectX1 = e.getX();
                    rangeSelectX2 = -1;
                    repaint();
                }
                super.mousePressed(e);  
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                if (funSelectRangeListener != null) {
                    if (rangeSelectX1 >= 0 && rangeSelectX2 >= 0) {
                        int  x1 = Math.min(rangeSelectX1, rangeSelectX2);
                        int  x2 = Math.max(rangeSelectX1, rangeSelectX2);
                        JsEnv.getJsEnv(null).invokeFunction(funSelectRangeListener, itemSelected, getTime(x1), getTime(x2));
                        rangeSelectX1 = -1;
                        rangeSelectX2 = -1;
                    }
                }
                super.mouseReleased(e);              
            }

            @Override
            public void mouseClicked(MouseEvent e) {
                int y = e.getY();
                boolean clicked = false;

                {
                    int id;
                    for (id=0; id<Wave.size(); id++) {
                        if ( Wave.getLineY0(id) < y && y < Wave.getLineY2(id)) {
                            break;
                        }
                    }
                    if (id != Wave.size()) {
                        clicked = true;
                        if (itemSelected != id){
                            itemSelected = id;
                            repaint();
                            if (funSelectItemListener != null) {
                                if (rangeSelectX1 > 0 && rangeSelectX2 > 0) {
                                    JsEnv.getJsEnv(null).invokeFunction(funSelectItemListener, itemSelected, getTime(rangeSelectX1), getTime(rangeSelectX2));
                                }
                            }
                        }
                        else {
                            doClickCallback(id, e.getX(), e.getY());
                        }
                    }
                }

                for (Group group : Group.getArray()) {
                    if ( group.y0 < y && y < group.y2) {
                        if (group.enable && (clicked == false) ){
                            group.enable = false;
                            updateGroupStatus();
                            repaint();
                        }
                        else if (!group.enable){
                            group.enable = true;
                            updateGroupStatus();
                            repaint();
                        }
                    }
                }
                super.mouseClicked(e); 
            }
            @Override
            public void mouseWheelMoved(MouseWheelEvent e){
                if (offsetLine + e.getWheelRotation() >= 0) {
                    offsetLine += e.getWheelRotation();
                    repaint();
                }
                
                super.mouseWheelMoved(e);
            }
        };
        this.addMouseMotionListener(mouseListen);
        this.addMouseListener(mouseListen);
        this.addMouseWheelListener(mouseListen);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 800, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 600, Short.MAX_VALUE)
        );
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables

}
