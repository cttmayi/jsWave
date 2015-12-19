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

    private final ArrayList<Data> waveList = new ArrayList<Data>();
    private final ArrayList<Connection> connectionList = new ArrayList<Connection>();
    private final ArrayList<Info> infoList = new ArrayList<Info>();
    
    private final ArrayList<Group> groups = new ArrayList<Group>();
    
    private long timeOffset, timeOffset2;
    private int timePoint;
    
    private int timeX, timeW;
    private int offsetLine;
    private int rangeX1, rangeX2;

    private int itemSelected;
    private int itemConnectionMoveSelected;
    private int itemWaveMoveSelected;

    private int timeLimitX1, timeLimitX2; 

    public String funSelectListener;
    public String funRangeListener;
    public String funConnectionListener;
    public String funWaveListener;
    public String funClickListener;

    private final int offsetX = 100, offsetY = 40;
    
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
        timeOffset = 0;
        timeOffset2 = 0;
        timePoint = 0;
        
        funSelectListener = null;
        funRangeListener = null;
        funConnectionListener = null;
        funWaveListener = null;
        funClickListener = null;
        
        offsetLine = 0;

        rangeX1 = -1; 
        rangeX2 = -1;

        itemSelected = -1;
        itemConnectionMoveSelected = -1;
        itemWaveMoveSelected = -1;

        timeLimitX1 = Integer.MAX_VALUE;
        timeLimitX2 = Integer.MIN_VALUE;
    }

    public void setTimeRuler(long us, long us2) {
        timeOffset = us;
        timeOffset2 = us2;
    }
    
    public void setTimePoint(int us) {
        timePoint = us;
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
        return getWidth() - offsetX;
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
        for (int i=0; i<waveList.size(); i++) {
            Data wave = waveList.get(i);
            if (wave.getY2() > y) {
               l = i;
               break;
            }
        }
        return l;
    }
    
    public int getLineY0(int line) {
        if (line - offsetLine >= 0) {
            //return waveList.get(line).getY2() - waveList.get(line).getHeightMax();
            return waveList.get(line).getY0();
        }
        else {
            return offsetY;
        }
    }
    
    public int getLineY1(int line) {
        if (line - offsetLine >= 0) {
            return waveList.get(line).getY1();
        }
        else {
            return offsetY;
        }
    }
    
    public int getLineY2(int line) {
        if (line - offsetLine >= 0) {
            return waveList.get(line).getY2();
        }
        else {
            return offsetY;
        }
    }

    public int getLineM(int line) {
        return (getLineY0(line) + getLineY2(line))/2;
    }

    private int getLineGroup(int line) {
        for (int id=0; id<groups.size(); id++) {
            if (groups.get(id).isInGroup(line)) {
                return id;
            }
        }
        return -1;
    }
    
    
    private void updateGroupStatus() {
        for (int id=0; id<waveList.size(); id ++ ) {
            Data list = waveList.get(id);
            list.group = getLineGroup(id);
            
            if (list.group >= 0) {
                list.enable = groups.get(list.group).enable;
            }
            else {
                list.enable = true;
            }
        }
        
        for (int id=0; id<connectionList.size(); id ++ ) {
            Connection c = connectionList.get(id);
            if (waveList.get(c.start).enable && waveList.get(c.end).enable) {
                c.enable = true;
            }
            else {
                c.enable = false;
            }
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
    
    public int addGroup(int start, int end, int colori, boolean enable) {
        Group group = new Group();
        group.start = start;
        group.end = end;
        group.color = Util.colorMake(colori);
        group.enable = enable;
        
        groups.add(group);
        return groups.size() - 1;
    }
    
    public int addLine(String name, ArrayList<Integer> time, ArrayList<Integer> colors, ArrayList<Integer> ys, ArrayList<String> names) {
        Line list = Line.newData(time, colors, ys, names);
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
        Histogram list = Histogram.newData(time, time2, y, colors, names, heightMax);
        list.setName(name);
        updateTimeLimit(time);
        updateTimeLimit(time2);
        waveList.add(list);
        return waveList.size() - 1;
    }
    
    public int addConnection(int time, int start, int end, int color) {
        
        if (start < waveList.size() && end < waveList.size()) {
            Connection data = Connection.newData(time, start, end, color);
            connectionList.add(data);
            return connectionList.size() - 1; 
        }
        else if (Util.isDebug){
            System.out.println("[ERROR] START:" + start + " END:" + end + " LINE ERROR! (>=" + waveList.size() + ")");
        }
        return -1;
    }
    
    public void setInfo(ArrayList<Integer> xs, ArrayList<Integer> ys, 
            ArrayList<String> names, ArrayList<Integer> fgcs, ArrayList<Integer> bgcs) {
        infoList.clear();
        for (int id=0; id<xs.size(); id++) {
            Info info = Info.newData(xs.get(id), ys.get(id), names.get(id), fgcs.get(id), bgcs.get(id));
            
            infoList.add(info);
        }
    }
    
    private void drawName(Graphics g, String name, int y, int height) {

        Widget.drawString(g, name, 10, y, offsetX - 10, height, 
                    true, colorFont, null);

    }

    private String i2t(long t, int w) {
        t = t + timeOffset;
        
        long h = t /60/60/Util.S;
        long m = (t - h *60 * 60* Util.S)/60/Util.S;
        
        float s = (float)((t - h * 60 * 60 * Util.S- m * 60 * Util.S))/Util.S;
        
        if (w >= Util.S) {
            return String.format("%02d:%02d:%02.03f", h % 24, m, s);
        }
        else if ((w >= Util.MS)){
            return String.format("%02d:%02d:%02.03f", h % 24, m, s);
        }
        else {
            return String.format("%02d:%02d:%02.04f", h % 24, m, s);
        }
    }
    private String i2t2(long t) {
        if (t % Util.S == 0) {
            return String.format("%ds",((t))/Util.S);
        }
        else if (t % Util.MS == 0) {
            return String.format("%.03fs",((float)(t))/Util.S);
        }
        else {
            return String.format("%.06fs",((float)(t))/Util.S);
        }
    }
    
    public void drawTimeRuler(Graphics g, int y) {
        y = y + 30;
        drawName(g, "Time", y, 20);

        int[] iS = {1,1000,1000000};
        String[] sS = {"us", "ms", "s"};

        int w = timeW / 10;
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
        
        for (int t=timeX/ww*ww; t<timeX+timeW; t+=ww) {
            int x = getX(t);
            if (x >= offsetX) {
                

                g.drawLine(x, y-18, x, y-15);
                if (timeOffset2 != 0) {
                    g.drawString(i2t2(t + timeOffset2), x, y-5); 
                }
                else {
                    g.drawString((t)/iS[s] + sS[s], x, y-5); 
                }
                if (timeOffset != 0) {
                    g.drawString(i2t(t, ww), x, y-20);
                }
            }
        }

        

        g.drawLine(offsetX, y-18, getWidth(), y-18);
        g.setColor(Color.RED);
        g.fillOval(getX(timePoint)-4, y-18-4, 8, 8);
    }

    private void drawConnection(Graphics g, double wdt) {
        for (Connection data : connectionList) {
            if (data.enable) {
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
    }



    private void drawInfo(Graphics g) {
        for (Info info: infoList) {
            Widget.drawString(g, info.info, getX(info.x), getLineM(info.y), Integer.MAX_VALUE, Integer.MAX_VALUE, 
                    false, info.fgColor, info.bgColor);
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
            if (timeOffset == 0) {
                g.drawString(Util.getTimeString(t) + "  (" + Util.getTimeString(t1) + " - " + Util.getTimeString(t2) + ")", 
                        rangeX1, offsetY);
            }
            else {
                g.drawString(String.format("%s (%s - %s)", Util.getTimeString(t), i2t(t1, Util.MS), i2t(t2, Util.MS)),
                        rangeX1, offsetY);
            }
        }

        int y0 = getLineY0(itemSelected);
        int y2 = getLineY2(itemSelected);
        if (itemSelected >= 0 && itemSelected < waveList.size() && y2 != y0) {
            g.setColor(colorSelect);
            g.fillRect(0, y0, getWidth(), y2 - y0);
        }
    }
    
    private void drawSplitLine(Graphics g, int y, int h, Color color) {
        g.setColor(color);
        g.fillRect(0, y, getWidth(), h);
        //g.drawLine(0, y, getWidth(), y);
    }
    
    @Override
    public void paintComponent(Graphics g){ 
        super.paintComponent(g); 
        
        Widget.timeX = timeX;
        Widget.timeW = timeW;
        Widget.rangeX1 = rangeX1;
        Widget.rangeX2 = rangeX2;
        Widget.offsetLine = offsetLine;
        
        Widget.offsetX = offsetX;
        Widget.offsetY = offsetY;
        
        Widget.screenW = getWidth();
        Widget.screenH = getHeight();

        Widget.colorFont = colorFont;

        g.setFont(Util.FontArial);
        
        double wdt = (double)getW() / timeW;
        int gap = 5;
        int g_gap = 15;

        drawTimeRuler(g, 0);

        updateGroupStatus();
        
        int y = offsetY;

        y += gap;
        
        int groupId = -1; 
        for (int id=offsetLine; id<waveList.size(); id ++ ) {
            Data list = waveList.get(id);
            if (groupId != list.group) {
                y += g_gap;
                groupId = list.group;
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
                y += gap;
                
            }
            else {
                list.setY(y, y);
                //y += gap;
            }

        }
        drawConnection(g, wdt);

        for (int id=0; id<groups.size(); id++) {
            Group group = groups.get(id);
            group.y0 = getLineY0(group.start) - g_gap/3;
            group.y2 = getLineY2(group.end) + g_gap/3;
            if(group.start >= offsetLine) {
                drawSplitLine(g, group.y0, 3, group.color);
            }
            if (group.end >= offsetLine) {
                drawSplitLine(g, group.y2, 3, group.color);
            }
        }
        
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
    
    void doMoveCallback(int x, int y) {
        if (funConnectionListener != null) {
            int id;
            int size = connectionList.size();
            for (id =0; id<size; id++) {
                if (inConnectionRange(id, x, y) ) {
                    break;
                }
            }
            if (id == size) id = -1;
            if (itemConnectionMoveSelected != id) {
                itemConnectionMoveSelected = id;
                if (id > 0) {
                    JsEnv.getJsEnv(null).invokeFunction(funConnectionListener, id, connectionList.get(id).getTime(), getLine(y));
                    repaint();
                    return;
                }
                else {
                    JsEnv.getJsEnv(null).invokeFunction(funConnectionListener, id, getTime(x), getLine(y));
                    repaint();
                }
            }
        }
        
        if (funWaveListener != null) {
            int i;
            int size = waveList.size();
            for (i=0; i<size; i++) {
                Data wave = waveList.get(i);
                int id = wave.getCallbackId(x, y);
                if (id >= 0) {
                    itemWaveMoveSelected = id;
                    JsEnv.getJsEnv(null).invokeFunction(funWaveListener, id, getTime(x), i);
                    repaint();
                    return;
                }
            }
            if (itemWaveMoveSelected != -1) {
                itemWaveMoveSelected = -1;
                JsEnv.getJsEnv(null).invokeFunction(funWaveListener, -1, getTime(x), -1);
                repaint();
            }
        }
        
    }

    void doClickCallback(int line, int x, int y) {
        if (funClickListener != null) {
            Data wave = waveList.get(line);
            int id = wave.getCallbackId(x, y);
            //System.out.println(id);
            if (id > 0) {
                JsEnv.getJsEnv(null).invokeFunction(funClickListener, id, getTime(x), line);
                repaint();
            }
        }
    }
    
    
    private void initMouse() {
        MouseAdapter mouseListen = new MouseAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                rangeX2 = e.getX();
                if (rangeX2 > getWidth()) {
                    rangeX2 = getWidth();
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
                if (funRangeListener != null) {
                    if (rangeX1 >= 0 && rangeX2 >= 0 && rangeX1 <= rangeX2) {
                        JsEnv.getJsEnv(null).invokeFunction(funRangeListener, itemSelected, getTime(rangeX1), getTime(rangeX2));
                    }
                }
                super.mouseReleased(e);              
            }
            
            @Override
            public void mouseClicked(MouseEvent e) {
                int y = e.getY();
                
                if (e.getClickCount() == 1)
                {
                    int id;
                    for (id=0; id<waveList.size(); id++) {
                        if ( getLineY0(id) < y && y < getLineY2(id)) {
                            break;
                        }
                    }
                    if (id != waveList.size()) {
                        if (itemSelected != id){
                            itemSelected = id;
                            repaint();
                            if (funSelectListener != null) {
                                if (rangeX1 > 0 && rangeX2 > 0) {
                                    JsEnv.getJsEnv(null).invokeFunction(funSelectListener, itemSelected, getTime(rangeX1), getTime(rangeX2));
                                }
                            }
                        }
                        else {
                            doClickCallback(id, e.getX(), e.getY());
                        }
                    }
                }

                for (int id=0; id<groups.size(); id++) {
                    Group group = groups.get(id);
                    if ( group.y0 < y && y < group.y2) {
                        if (group.enable && e.getClickCount() == 2){
                            group.enable = false;
                            repaint();
                        }
                        else if (!group.enable){
                            group.enable = true;
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
