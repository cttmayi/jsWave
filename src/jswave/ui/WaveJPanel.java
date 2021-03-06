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

    private int waveOffsetX;
    private final int waveOffsetY = 42;
    
    private final Color colorSelect = new Color(255,0,0,50);
    private final Color colorSelectLine = new Color(128,0,0);
    private final Color colorFont = Color.white;    

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
        
        waveOffsetX = 100;
        
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

    public void setPanelXOffset(int offset) {
        waveOffsetX = offset;
        repaint();
    }
    
    public int getTimeOffset(){
        return timeX;
    }

    public int getTimeWidth(){
        return timeW;
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

        rangeSelectX1 = -1;
        rangeSelectX2 = -1;
        repaint();
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

    private void drawFrame(Graphics g) {
        g.setColor(colorFont);
        g.drawLine(waveOffsetX, 0, waveOffsetX, getHeight());

        //draw range
        if (rangeSelectX1 >= 0 && rangeSelectX2 >= 0) {
            int  x1 = Math.min(rangeSelectX1, rangeSelectX2);
            int  x2 = Math.max(rangeSelectX1, rangeSelectX2);

            g.setColor(colorSelect);
            g.fillRect(x1, 0, x2 - x1, getHeight());
            g.setColor(colorSelectLine);
            g.drawLine(x1, 0, x1, getHeight());
            g.drawLine(x2, 0, x2, getHeight());
            
            int t1 = getTime(x1);
            int t2 = getTime(x2);
            int t = t2 - t1;
            g.setColor(colorFont);

            x1 += 4;
            if (t1 != t2) {
                g.drawString(Util.getTimeString(t), 
                            x1, waveOffsetY + Util.fontHeight);
            }
            if (t1 != t2) {
                g.drawString(String.format("%s ~ %s",Util.timeFormatS(t1,t), Util.timeFormatS(t2,t)), 
                            x1, waveOffsetY + Util.fontHeight * 2);
            }
            else {
                g.drawString(Util.timeFormatS(t1,t), 
                            x1, waveOffsetY + Util.fontHeight * 2);
            }

            long timeOffset = TimeRuler.getData().getTimeOffset();
            long timeOffset2 = TimeRuler.getData().getTimeOffset2();

            if (timeOffset != 0) {
                if (t1 != t2) {
                    g.drawString(String.format("%s ~ %s", Util.timeFormatHMS(t1+ timeOffset, t), Util.timeFormatHMS(t2 + timeOffset, t)),
                            x1, waveOffsetY + Util.fontHeight * 3);
                }
                else {
                    g.drawString(Util.timeFormatHMS(t1+ timeOffset, t),
                            x1, waveOffsetY + Util.fontHeight * 3);
                }
            }
            if (timeOffset2 != 0) {
                if (t1 != t2) {
                    g.drawString(String.format("%s ~ %s", Util.timeFormatS(t1 + timeOffset2, t), Util.timeFormatS(t2 + timeOffset2, t)),
                            x1, waveOffsetY + Util.fontHeight * 4);
                }
                else {
                    g.drawString(Util.timeFormatS(t1 + timeOffset2, t),
                            x1, waveOffsetY + Util.fontHeight * 4);
                }
            }
        }

        if (itemSelected >= 0 && itemSelected < Wave.size()) {
            int y0 = Wave.getLineY0(itemSelected);
            int y2 = Wave.getLineY2(itemSelected);
            if (y2 != y0) {
                g.setColor(colorSelect);
                g.fillRect(0, y0, getWidth(), y2 - y0);
            }
        }
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
        int gap = 1;
        int g_gap = Util.fontHeight / 2 * 3;

        TimeRuler.show(g, 0);
        
        Wave.show(g, waveOffsetY, wdt, gap, g_gap);
        Connection.show(g, wdt);
        Group.show(g, wdt, g_gap);
        Info.show(g);
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

                if (funWaveNameListener != null) {
                    if (wave.inNameZone(x, y)) {
                        itemNameMoveSelected = i;
                        JsEnv.getJsEnv(null).invokeFunction(funWaveNameListener, getTime(x), i);
                        repaint();
                        return;                    
                    }
                }

                if (funWaveMoveListener != null) {
                    int id = wave.getCallbackId(x, y);
                    if (id >= 0) {
                        itemWaveMoveSelected = id;
                        JsEnv.getJsEnv(null).invokeFunction(funWaveMoveListener, id, getTime(x), i);
                        repaint();
                        return;
                    }
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
                if (rangeSelectX2 <= waveOffsetX) {
                    rangeSelectX2 = waveOffsetX+1;
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
                else {
                    rangeSelectX1 = -1;
                    rangeSelectX2 = -1;
                }
                
                super.mousePressed(e);
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                rangeSelectX2 = e.getX();
                if (rangeSelectX2 > getWidth()) {
                    rangeSelectX2 = getWidth();
                }
                if (rangeSelectX2 <= waveOffsetX) {
                    rangeSelectX2 = waveOffsetX+1;
                }
                //System.out.println(e.getButton());
                if (funSelectRangeListener != null) {
                    if (rangeSelectX1 >= 0 && rangeSelectX2 >= 0) {
                        int  x1 = Math.min(rangeSelectX1, rangeSelectX2);
                        int  x2 = Math.max(rangeSelectX1, rangeSelectX2);
                        JsEnv.getJsEnv(null).invokeFunction(funSelectRangeListener, itemSelected, getTime(x1), getTime(x2), e.getButton());
                    }
                }
                repaint();
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
                            Group.updateGroupStatus();
                            repaint();
                        }
                        else if (!group.enable){
                            group.enable = true;
                            Group.updateGroupStatus();
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

        setBackground(new java.awt.Color(0, 0, 0));

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
