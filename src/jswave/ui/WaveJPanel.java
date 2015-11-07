/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jswave.ui;


import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Toolkit;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.util.ArrayList;

import jswave.Util;
import jswave.js.JsEnv;
import jswave.st.*;

/**
 *
 * @author lenovo
 */
public final class WaveJPanel extends javax.swing.JPanel {

    private final ArrayList<Data> waveList = new ArrayList<Data>();
    private final ArrayList<Connection> connectionList = new ArrayList<Connection>();
    private final ArrayList<Info> infoList = new ArrayList<Info>();
    
    private int timeX, timeW;
    private int offsetLine;
    private int rangeX1, rangeX2;

    private int itemSelected;
    private int itemConnectionMoveSelected;

    private int timeLimitX1, timeLimitX2; 

    public String funSelectListener = null;
    public String funRangeListener = null;
    public String funConnectionListener = null;

    private final int offsetX = 100, offsetY = 30;
    private final Font fontArial = new Font("Arial", Font.PLAIN, 12);
    private final FontMetrics fontArialMetrics = Toolkit.getDefaultToolkit().getFontMetrics(fontArial);
    private final int FontHeight = fontArialMetrics.getHeight();
    
    private final Color colorSelect = new Color(255,0,0,30);

    
    public void clearUI() {
        funSelectListener = null;
        funRangeListener = null;
        funConnectionListener = null;
        
        offsetLine = 0;

        rangeX1 = -1; 
        rangeX2 = -1;

        itemSelected = -1;
        itemConnectionMoveSelected = -1;

        timeLimitX1 = Integer.MAX_VALUE;
        timeLimitX2 = 1;
    }
    
    
    public void setTimeRange(int x, int w) {
        timeX = x;
        timeW = w;
        
        int max = (int)((timeLimitX2 - timeLimitX1) * 1.05);
        int min = 800;
        
        if (timeX < timeLimitX1) timeX = timeLimitX1;
        if (timeW <= min) timeW = min;
        if (max < timeW) timeW = max;
        if (timeX + timeW > timeLimitX1 + max) timeX = timeLimitX1 + max - timeW;
        
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
        return getWidth() - offsetX;
    }
    
    public void setLine(int line) {
        offsetLine = line;
    }
    
    public int getSelected() {
        int selected = itemSelected;
        if (selected >=0) {
            selected += offsetLine;
        }
        return selected;
    }

    
    public int getLineY0(int line) {
        if (line - offsetLine >= 0) {
            return waveList.get(line - offsetLine).getY2() - waveList.get(line - offsetLine).getHeightMax();
        }
        else {
            return offsetY;
        }
    }
    
    public int getLineY1(int line) {
        if (line - offsetLine >= 0) {
            return waveList.get(line - offsetLine).getY1();
        }
        else {
            return offsetY;
        }
    }
    
    public int getLineY2(int line) {
        if (line - offsetLine >= 0) {
            return waveList.get(line - offsetLine).getY2();
        }
        else {
            return offsetY;
        }
    }
    
    
    private void updateTimeLimit(ArrayList<Integer> time) {
        int x2 = time.get(time.size()-1);
        int x1 = time.get(0);
        if (timeLimitX1 > x1) {
            timeLimitX1 = x1;
        }
        if (timeLimitX2 < x2) {
            timeLimitX2 = x2;
        }    
    }
    
    public int addLine(String name, ArrayList<Integer> time, ArrayList<Integer> colors, ArrayList<String> names) {
        Line list = Line.newData(time, colors, names);
        list.setName(name);
        updateTimeLimit(time);

        waveList.add(list);
        return waveList.size() - 1;
    }

    public int addDiagram(String name, ArrayList<Integer> time, ArrayList<Integer> y, int heightMax, int color) {
        Diagram list = Diagram.newData(time, y, heightMax, color);
        list.setName(name);
        waveList.add(list);
        return waveList.size() - 1;
    }
    
    public int addHistogram(String name, ArrayList<Integer> time, ArrayList<Integer> time2, 
            ArrayList<Integer> y, ArrayList<Integer> colors, ArrayList<String> names, int heightMax) {
        Histogram list = Histogram.newData(time, time2, y, colors, names, heightMax + FontHeight);
        list.setName(name);
        updateTimeLimit(time);
        updateTimeLimit(time2);
        waveList.add(list);
        return waveList.size() - 1;
    }
    
    public int addConnection(int time, int start, int end, int color) {
        Connection data = Connection.newData(time, start, end, color);
        
        connectionList.add(data);
        return connectionList.size() - 1;
    }
    
    public void setInfo(ArrayList<Integer> xs, ArrayList<Integer> ys, 
            ArrayList<String> names, ArrayList<Integer> fgcs, ArrayList<Integer> bgcs) {
        infoList.clear();
        for (int id=0; id<xs.size(); id++) {
            Info info = Info.newData(xs.get(id), ys.get(id), names.get(id), fgcs.get(id), bgcs.get(id));
            
            infoList.add(info);
        }
    } 
    
    /**
     * Creates new form wavaJPanel
     */
    public WaveJPanel() {
        clearUI();
        initComponents();
        initMouse();
    }
    
    private void drawName(Graphics g, String name, int y, int height) {
        g.setColor(Color.red);
        g.drawString(name, 10, y - height/2);
    }

    public void drawTimeRule(Graphics g, int y) {
        int[] iS = {1,1000,1000000};
        String[] sS = {"us", "ms", "s"};
        
        int w = timeW / 20;
        int ww;
        int s;
        for (ww=1;true;ww*=10){
            if (ww > w) {
                for (s=0; s<iS.length-1; s++) {
                    if (iS[s] * 100 >= ww) {
                        break;
                    }
                }
                break;
            }
        }
        
        drawName(g, "Time", y, 20);
        int d = getX(timeX);
        
        for (int t=timeX/ww*ww; t<timeX+timeW; t+=ww) {
            int x = getX(t);
                g.drawLine(x, y-18, x, y-15);
                g.drawString(t/iS[s] + sS[s], x, y-5);
        }

        g.drawLine(offsetX, y-18, getWidth(), y-18);
    }

    public String trimDownText(String strValue, int maxWidth) {

        String strReturn = "";
        int chr_width;
        if (strValue != null && !strValue.equals("") && maxWidth > 2) {
            for (int i = strValue.length(); i > -1; i--) {
                strReturn = strValue.substring(0, i);
                chr_width = fontArialMetrics.stringWidth(strReturn);
                if (chr_width < maxWidth) {
                    break;
                }
            }
        }
        return strReturn;
    }

    public void drawLine(Graphics g, int y, Line list, double wdt) {
        //System.out.println("Line:" + y);
        ArrayList<Integer> x = list.getX(timeX, offsetX, wdt);

        int timeStart = x.get(0);
        if (timeStart < offsetX) timeStart = offsetX;

        drawName(g, list.getName(), y, list.getHeightMax());
        
        list.setY(y - 3, y);
        
        for (int timeId=1; timeId<x.size(); timeId++) {
            if (x.get(timeId) < offsetX) {timeStart = offsetX; continue;}

            g.setColor(list.listColor.get(timeId));
            int timeEnd = x.get(timeId);
            g.fillRect(timeStart, y-3, timeEnd - timeStart, 3);

            String str = null;
            if (timeId < list.listName.size()) {
                str = list.listName.get(timeId);
            }
            if (str != null && !str.equals("")) {
                str = trimDownText(str, timeEnd - timeStart - 8);
                //g.setColor(Color.BLACK);
                g.drawString(str, timeStart + 4, y - 5);
            }
            
            timeStart = timeEnd;
            
            if (timeStart > getWidth()) break;
        }
    }
/*
    public void drawDiagram(Graphics g, int y, Diagram list, double wdt) {
        //System.out.println("Diagram: " + y);
        ArrayList<Integer> x = list.getX(timeX, offsetX, wdt);
        
        int timeStart = x.get(0);
        int yStart = list.listY.get(0) + y;

        list.setY(y - list.getHeightMax(), y);
        
        drawName(g, list.getName(), y, list.getHeightMax());
        g.setColor(list.color);
        for (int timeId=1; timeId<x.size(); timeId++) {
            if (x.get(timeId) > getWidth()) break;
            if (x.get(timeId)<offsetX) continue;
            
            int timeEnd = x.get(timeId);
            int yEnd =  y - list.listY.get(timeId);
            g.drawLine(timeStart, yStart, timeEnd, yEnd);
            timeStart = timeEnd;
            yStart = yEnd;
        }
    }
*/    
    public void drawHistogram(Graphics g, int y, Histogram list, double wdt) {
        //System.out.println("Histogram:" + y);
        ArrayList<Integer> x = list.getX(timeX, offsetX, wdt);
        ArrayList<Integer> x2 = list.getX2(timeX, offsetX, wdt);

        list.setY(y - list.getHeightMax(), y);
        drawName(g, list.getName(), y, list.getHeightMax());
        for (int timeId=0; timeId<x.size(); timeId++) {
            int xx = x.get(timeId);
            int xx2 = x2.get(timeId);
            
            if (xx > getWidth()) break;
            if (xx2 < offsetX) continue;
            
            if (xx < offsetX) xx = offsetX;
            
            g.setColor(list.listColor.get(timeId));
            
            int w = xx2 - xx;
            if (w <= 0) w = 1;
            g.fillRect(xx, y-list.listY.get(timeId), 
                    w, list.listY.get(timeId));
            
            String str = null;
            if (timeId < list.listName.size()) {
                str = list.listName.get(timeId);
            }            
            if (str != null && !str.equals("")) {
                str = trimDownText(str, w - 8);
                //g.setColor(Color.BLACK);
                g.drawString(str, xx + 4, y - list.listY.get(timeId) - 2);
            }            
        }        
    }
    
    private void drawConnection(Graphics g, double wdt) {
        for (Connection data : connectionList) {
            int x = data.getX(timeX, offsetX, wdt);
            
            if (x < offsetX) continue;
            if (x > getWidth()) continue;
            
            g.setColor(data.color);

            int start = data.start;
            int end = data.end;

            int yStart, yEnd, yO;
            if (start < end) {
                yStart = getLineY2(start);
                yEnd = getLineY1(end);
                yO = yEnd - 4;
            }
            else {
                yStart = getLineY1(start);
                yEnd = getLineY2(end);
                yO = yEnd;
            }
            if (yStart != yEnd) {
                g.drawLine(x, yStart, x, yEnd);
                g.fillOval(x-2, yO, 4, 4);
            }
        }
    }

    private void drawInfo(Graphics g) {
        for (Info info: infoList) {
            int w = fontArialMetrics.stringWidth(info.info) + 4;
            
            g.setColor(info.bgColor);
            g.fillRect(info.x - 2, info.y , w, FontHeight + 4);
            g.setColor(info.fgColor);
            g.drawString(info.info, info.x, info.y + FontHeight);
        }
    }
    
    
    private void drawFrame(Graphics g) {
        g.setColor(Color.black);
        g.drawLine(offsetX - 3, 0, offsetX - 3, getHeight());
        
        int gap = 6;

        //draw range
        if (rangeX1 >= 0 && rangeX2 >= 0 && rangeX1 < rangeX2) {
            g.setColor(colorSelect);
            g.fillRect(rangeX1, 0, rangeX2 - rangeX1, getHeight());
            int t1 = getTime(rangeX1);
            int t2 = getTime(rangeX2);
            
            int t = t2 - t1;

            g.setColor(Color.blue);  
            g.drawString(Util.getTimeString(t) + "  (" + Util.getTimeString(t1) + " - " + Util.getTimeString(t2) + ")", 
                    rangeX1, offsetY);
        
        }

        int y0 = getLineY0(itemSelected);
        int y2 = getLineY2(itemSelected);
        if (itemSelected >= 0 && itemSelected < waveList.size() && y2 != y0) {
            g.setColor(colorSelect);
            g.fillRect(0, y0 - gap/2, getWidth(), y2 - y0 + gap);
        }        

    }
    
    private void drawSplitLine(Graphics g, int y) {
        g.setColor(colorSelect);
        g.drawLine(offsetX, y, getWidth(), y);
    }
    
    @Override
    public void paintComponent(Graphics g){ 
        super.paintComponent(g); 
        
        g.setFont(fontArial);
        
        double wdt = (double)getW() / timeW;
        int gap = 5;

        drawTimeRule(g, 20);
        
        int y = offsetY;

        y += gap;
        for (int id=offsetLine; id<waveList.size(); id ++ ) {
            Data list = waveList.get(id);
            y += list.getHeightMax();

            if (y < getHeight()) {

                if (list.type == Data.LINE) {
                    drawLine(g, y, (Line)list, wdt);
                    
                }
                else if (list.type == Data.DIAGRAM) {
                    //drawDiagram(g, y, (Diagram)list, wdt);
                }
                else if (list.type == Data.HISTOGRAM) {
                    drawHistogram(g, y, (Histogram)list, wdt);
                }
            }
            else {
                list.setY(y + gap, y + gap);
            }
            drawSplitLine(g, y + 2);
            y += gap;
        }
        drawConnection(g, wdt);

       drawInfo(g);
        drawFrame(g);
        
        
    }

    private int getTime(int x){
        return Util.getTime(x, offsetX, getW(), timeX, timeW);
    }
    
    private int getX(int t) {
        return Util.getX(t, offsetX, getW(), timeX, timeW);
    }
    
    private boolean inConnectionRange(int id, int x, int y) {
        Connection c = connectionList.get(id);
        Data d1;
        Data d2;

        if (c.start < c.end) {
            d1 = waveList.get(c.start);
            d2 = waveList.get(c.end);
        }
        else {
            d2 = waveList.get(c.start);
            d1 = waveList.get(c.end);         
        }
        
        int l1 = d1.getY2(); 
        int l2 = d2.getY1();

        int xx = getX(c.time);
        int x1 = xx - 4;
        int x2 = xx + 4;

        return (x > x1 && x < x2 &&  y > l1 && y < l2);
    }
    
    private void initMouse() {
        MouseAdapter mouseListen = new MouseAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                //System.out.println("Drag X:" + e.getX() + " Y:" +e.getY());
                
                rangeX2 = e.getX();
                if (rangeX2 > getWidth()) {
                    rangeX2 = getWidth();
                }
                repaint();
                super.mouseDragged(e);  
            }
            @Override
            public void mouseMoved(MouseEvent e) {
                //System.out.println("Move X:" + e.getX() + " Y:" +e.getY());

                
                int id;
                int size = connectionList.size();
                for (id =0; id<size; id++) {
                    if (inConnectionRange(id, e.getX(), e.getY()) ) {
                        break;
                    }
                }
                if (id == size) id = -1;
                
                if (itemConnectionMoveSelected != id) {
                    itemConnectionMoveSelected = id;
                    if (funConnectionListener != null) {
                        JsEnv.getJsEnv(null).invokeFunction(funConnectionListener, id, e.getX(), e.getY());
                        repaint();
                    }
                }
                    
                    
                    
                super.mouseMoved(e);  
            }

            @Override
            public void mousePressed(MouseEvent e) {
                //System.out.println("Pressed X:" + e.getX() + " Y:" +e.getY());
                if (e.getX() > offsetX) {
                    rangeX1 = e.getX();
                    rangeX2 = -1;
                    repaint();
                }
                super.mousePressed(e);  
            }
            
            @Override
            public void mouseReleased(MouseEvent e) {
                //System.out.println("Release X:" + e.getX() + " Y:" +e.getY());

                if (funRangeListener != null) {
                    if (rangeX1 >= 0 && rangeX2 >= 0 && rangeX1 <= rangeX2) {
                        JsEnv.getJsEnv(null).invokeFunction(funRangeListener, itemSelected, getTime(rangeX1), getTime(rangeX2));
                    }
                }

                //repaint();
                super.mouseReleased(e);              
            }
            
            @Override
            public void mouseClicked(MouseEvent e) {
                //System.out.println("Clicked X:" + e.getX() + " Y:" +e.getY());
                //int x = e.getX();
                int y = e.getY();
                int id;
                for (id=0; id<waveList.size(); id++) {
                    if ( getLineY0(id) < y && y < getLineY2(id)) {
                        break;
                    }
                }
                
                if (id == waveList.size()) {
                    id = -1;
                }
                
                if (itemSelected != id){
                    itemSelected = id;
                    repaint();
                    if (funSelectListener != null) {
                        if (rangeX1 > 0 && rangeX2 > 0) {
                            JsEnv.getJsEnv(null).invokeFunction(funSelectListener, itemSelected, getTime(rangeX1), getTime(rangeX2));
                        }
                        else {
                            JsEnv.getJsEnv(null).invokeFunction(funSelectListener, itemSelected, -1, -1);
                        }
                    }
                }

                super.mouseClicked(e); 
            }
            @Override
            public void mouseWheelMoved(MouseWheelEvent e){
                //System.out.println("Wheel: " + e.getWheelRotation());
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
