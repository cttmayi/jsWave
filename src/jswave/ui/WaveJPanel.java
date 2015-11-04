/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jswave.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.util.ArrayList;
import javax.swing.JFrame;
import jswave.st.*;

/**
 *
 * @author lenovo
 */
public final class WaveJPanel extends javax.swing.JPanel {

    private final ArrayList<Data> waveList = new ArrayList<Data>();
    private final ArrayList<Connection> connectionList = new ArrayList<Connection>();
    
    private final int offsetX = 100;
    private int timeX, timeW;
    private int offsetLine;
    private int rangeX1, rangeX2;
    private final ArrayList<Integer> lineY = new ArrayList<Integer>();
    private int itemSelected;

    private int widthMax; 
    
    public void setTimeRange(int x, int w) {
        timeX = x;
        timeW = w;
        
        int max = (int)(widthMax * 1.05);
        int min = 800;
        
        if (timeX < 0) timeX = 0;
        if (timeW <= min) timeW = min;
        
        if (max < timeW) timeW = max;

        if (timeX > max - timeW) timeX = max - timeW;
        
        repaint();
    }
    
    public void moveLeft() {
        setTimeRange(timeX + timeW/10, timeW);
    }
    public void moveRight() {
        setTimeRange(timeX - timeW/10, timeW);
    }    

    public void scalerDown() {
        int w = timeW * 4 / 3;
        //setTimeRange(timeX + timeW/2 - w/2 , w);
        setTimeRange(timeX , w);
    }
    public void scalerUp() {
        int w = timeW * 3 / 4;
        //setTimeRange(timeX + timeW/2 - w/2 , w);
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
    
    public void addLine(String name, ArrayList<Integer> time, ArrayList<Integer> colors) {
        Line list = Line.newData(time, colors);
        list.setName(name);

        int  width = time.get(time.size()-1);
        if (widthMax < width) {
            widthMax = width;
        }
        waveList.add(list);
    }

    public void addDiagram(String name, ArrayList<Integer> time, ArrayList<Integer> y, int heightMax, int color) {
        Diagram list = Diagram.newData(time, y, heightMax, color);
        list.setName(name);
        waveList.add(list);
    }
    
    public void addHistogram(String name, ArrayList<Integer> time, ArrayList<Integer> y, ArrayList<Integer> colors, int widthItem, int heightMax) {
        Histogram list = Histogram.newData(time, y, colors, widthItem, heightMax);
        list.setName(name);
        waveList.add(list);
    }
    
    public void addConnection(int time, int start, int end, int color) {
        Connection data = Connection.newData(time, start, end, color);
        
        connectionList.add(data);
    }
    
    
    public void clearUI() {
        offsetLine = 0;

        rangeX1 = -1; 
        rangeX2 = -1;

        lineY.clear();
        itemSelected = -1;

        widthMax = 1;
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
        
        int num = 0;
        for (int id=0; num < 20;id ++) {
            int x = (int)((double)(id * ww - timeX) / timeW * getW()) + offsetX;
            
            if (x>getWidth()) break; 
            if (x>=offsetX) {
                g.drawLine(x, y-18, x, y-15);
                g.drawString(ww*id/iS[s] + sS[s], x, y-5);
                num ++;
            }
        }
        g.drawLine(offsetX, y-18, getWidth(), y-18);
    }
    
    
    public void drawLine(Graphics g, int y, Line list, double wdt) {
        //System.out.println("Color:" + y);
        ArrayList<Integer> x = list.getX(timeX, offsetX, wdt);

        int timeStart = x.get(0);
        if (timeStart < offsetX) timeStart = offsetX;

        drawName(g, list.getName(), y, list.getHeightMax());
        //y -= list.getHeightMax()/2;
        for (int timeId=1; timeId<x.size(); timeId++) {
            if (x.get(timeId) < offsetX) {timeStart = offsetX; continue;}

            g.setColor(list.listColor.get(timeId));
            int timeEnd = x.get(timeId);
            g.fillRect(timeStart, y-3, timeEnd - timeStart, 3);
            timeStart = timeEnd;
            
            if (timeStart > getWidth()) break;
        }
    }

    public void drawDiagram(Graphics g, int y, Diagram list, double wdt) {
        //System.out.println("Diagram: " + y);
        ArrayList<Integer> x = list.getX(timeX, offsetX, wdt);
        
        int timeStart = x.get(0);
        int yStart = list.listY.get(0) + y;

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
    
    public void drawHistogram(Graphics g, int y, Histogram list, double wdt) {
        //System.out.println("Histogram:" + y);
        ArrayList<Integer> x = list.getX(timeX, offsetX, wdt);

        drawName(g, list.getName(), y, list.getHeightMax());
        for (int timeId=0; timeId<x.size(); timeId++) {
            if (x.get(timeId) > getWidth()) break;
            if (x.get(timeId)<offsetX) continue;
            
            g.setColor(list.listColor.get(timeId));
            g.fillRect(x.get(timeId), y, list.width, -list.listY.get(timeId));
        }        
    }
    
    private void drawConnection(Graphics g, double wdt) {
        for (Connection data : connectionList) {
            int x = data.getX(timeX, offsetX, wdt);
            
            if (x < offsetX) continue;
            
            g.setColor(data.color);
            int start = data.start+1 - offsetLine;
            int end = data.end+1 - offsetLine;
            if (start < 0) start = 0;
            if (start > lineY.size()-1) start = lineY.size()-1;
            if (end < 0) end = 0;
            if (end > lineY.size()-1) end = lineY.size()-1;
            if (start != end) {
                
                if (start < end) {
                    g.drawLine(x, lineY.get(start), x, lineY.get(end));
                    g.fillOval(x-2, lineY.get(end)-5, 4, 4);
                }
                else {
                    g.drawLine(x, lineY.get(start), x, lineY.get(end));
                    g.fillOval(x-2, lineY.get(end)-2, 4, 4);
                }
            }
        }
    }
    
    private void drawFrame(Graphics g) {
        g.drawLine(offsetX - 3, 0, offsetX - 3, getHeight());
    }
    
    @Override
    public void paintComponent(Graphics g){ 
        super.paintComponent(g); 
        
        double wdt = (double)getW() / timeW;
        int gap = 5;

        drawFrame(g);
        
        drawTimeRule(g, 20);
        
        int y = 20;
        lineY.add(y);
        y += gap;
        for (int id=offsetLine; id<waveList.size(); id ++ ) {
            Data list = waveList.get(id);
            y += list.getHeightMax();
            lineY.add(y);
            if (y > getHeight()) break;
            if (list.type == Data.LINE) {
                drawLine(g, y, (Line)list, wdt);
            }
            else if (list.type == Data.DIAGRAM) {
                drawDiagram(g, y, (Diagram)list, wdt);
            }
            else if (list.type == Data.HISTOGRAM) {
                drawHistogram(g, y, (Histogram)list, wdt);
            }            
            y += gap;
        }
        
        drawConnection(g, wdt);
        
        
        
        g.setColor(new Color(255,0,0,30));   
        if (rangeX1 >= 0 && rangeX2 >= 0) {
            g.fillRect(rangeX1, 0, rangeX2 - rangeX1, getHeight());
        }

        if (itemSelected >= 0 && itemSelected + 1< lineY.size()) {
            g.fillRect(0, lineY.get(itemSelected) + gap/2, getWidth(), lineY.get(itemSelected + 1) - lineY.get(itemSelected));
        }
        //System.out.println("paintComponent");
    }

    private void initMouse() {
        MouseAdapter mouseListen = new MouseAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                //System.out.println("Drag X:" + e.getX() + " Y:" +e.getY());
                rangeX2 = e.getX();
                repaint();
                super.mouseDragged(e);  
            }
            @Override
            public void mouseMoved(MouseEvent e) {
                //System.out.println("Move X:" + e.getX() + " Y:" +e.getY());
                super.mouseMoved(e);  
            }

            @Override
            public void mousePressed(MouseEvent e) {
                //System.out.println("Pressed X:" + e.getX() + " Y:" +e.getY());
                rangeX1 = e.getX();
                rangeX2 = -1;
                repaint();
                super.mousePressed(e);  
            }
            
            @Override
            public void mouseReleased(MouseEvent e) {
                //System.out.println("Release X:" + e.getX() + " Y:" +e.getY());
                //rangeX1 = -1;
                //rangeX2 = -1;
                //repaint();
                super.mouseReleased(e);              
            }
            
            @Override
            public void mouseClicked(MouseEvent e) {
                //System.out.println("Clicked X:" + e.getX() + " Y:" +e.getY());
                for (int line=0; line<lineY.size(); line++) {
                    if (lineY.get(line) > e.getY()) {
                        //System.out.println("Clicked Line: " + (line - 1));
                        itemSelected = line - 1;
                        repaint();
                        break;
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
    
    
	public static void main(String[] args)
	{
            JFrame mainFrame = new JFrame("UI Demo");
            mainFrame.getContentPane().setLayout(new BorderLayout());

            ArrayList<Integer> colors = new ArrayList<Integer>();
            ArrayList<Integer> times = new ArrayList<Integer>();
            ArrayList<Integer> ys = new ArrayList<Integer>();
            
            for (int id=0;id<10000; id++) {
                colors.add(100 * id * id);
                times.add(id * 800);
                ys.add(id % 40);
            }
            
            WaveJPanel panel;
            panel = new WaveJPanel();
            
            for (int id=0; id<10; id++) {
                panel.addLine("Line", times, colors);
                //panel.addDiagram("Diagram",times, ys, 30, 0);
                //panel.addHistogram("Histogram",times, ys, colors, 3, 60);
            }

            for (int id=0;id<100; id++) {
                panel.addConnection(id*100000, id % 5, id % 5 + 2, 0);
            }            
            
            
            javax.swing.GroupLayout layout = new javax.swing.GroupLayout(mainFrame.getContentPane());
            mainFrame.getContentPane().setLayout(layout);
            layout.setHorizontalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(panel, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 815, Short.MAX_VALUE)
            );

            layout.setVerticalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    //.addComponent(panel, javax.swing.GroupLayout.PREFERRED_SIZE, 219, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addComponent(panel, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 815, Short.MAX_VALUE)
            );
            
            //mainFrame.getContentPane().add(myPanel, BorderLayout.NORTH);
            mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);  
            mainFrame.pack();
            mainFrame.setVisible(true);  
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
