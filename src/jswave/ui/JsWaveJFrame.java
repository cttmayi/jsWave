/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jswave.ui;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowStateListener;
import java.io.File;
import java.util.ArrayList;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import jswave.Util;
import jswave.js.JsEnv;
import jswave.js.Script;
import jswave.st.Group;

/**
 *
 * @author cttmayi
 */
public class JsWaveJFrame extends javax.swing.JFrame {

    class FKeyListener {
        public String key;
        public String func;
        
        public FKeyListener(String key, String func) {
            this.key = key;
            this.func = func;
        }
    }
    
    private final ArrayList<FKeyListener> keyListeners = new ArrayList<FKeyListener>();
    
    public void addKeyListener(String key, String func) {
        keyListeners.add(new FKeyListener(key, func));
    }

    /**
     * Creates new form jsWaveJFrame
     */
    public JsWaveJFrame() {
        initComponents();
        keyListeners.clear();
        initSciprt();
        initKey();
        initUI();
        
    }

    public WaveJPanel getPanel() {
        return waveJPanel;
    }

    JsEnv jsEnv;

    private void initSciprt() {
        Script script = Script.getScript();
        script.setFrame(this);

        jsEnv = JsEnv.getJsEnv(this);

        String jarPath = Util.getJarPath();
        String defaultJs = jarPath + "default.js";
        String libPath; 

        if (Util.getPara(Util.PARA_LIBS_PATH) != null) {
            libPath = Util.getPara(Util.PARA_LIBS_PATH);
        }
        else {
            libPath = jarPath + "libs";
        }

        File libsDir = new File(libPath);        
        File files[] = libsDir.listFiles();
        if (files != null) {
            for (File file : files) {
                if (file.isDirectory()) {
                } else {
                    jsEnv.loadFile(file.getAbsolutePath());
                }
            }
        }

        if (Util.getPara(Util.PARA_FILE_PATH) != null) {
            if (Util.isFileExist(Util.getPara(Util.PARA_FILE_PATH))) {
                jsEnv.loadFile(Util.getPara(Util.PARA_FILE_PATH));
            }
        }
        else if (Util.isFileExist(defaultJs)){
            jsEnv.loadFile(defaultJs);
        }

        waveJPanel.setTimeRange(Integer.MIN_VALUE,  Integer.MAX_VALUE);
        Group.updateGroupStatus();
    }

    private void initUI() {
        jSplitPaneLR.setDividerLocation(jSplitPaneLR.getWidth() - 240);
        jSplitPaneLR.setOneTouchExpandable(true);
        jSplitPaneLR.setResizeWeight(1.0);

        jSplitPaneUD.setDividerLocation(0.7);
        jSplitPaneUD.setResizeWeight(0.7);

        waveJPanel.setMinimumSize(new Dimension(600,500));
        jScrollPaneU.setMinimumSize(new Dimension(200,100));

        this.addWindowStateListener(new WindowStateListener () {
            @Override 
            public void windowStateChanged(WindowEvent state) {
                //System.out.println("w:" + waveJPanel.getWidth());
            }
        });
    }

    private void initKey() {
        addKeyListener(new KeyListener() { 
            @Override 
            public void keyTyped(KeyEvent e) { 
            } 

            @Override 
            public void keyPressed(KeyEvent e) { 
                
                if (!e.isActionKey()) {
                    String keyText = KeyEvent.getKeyText(e.getKeyCode());
                    if(Util.isDebug) {
                        System.out.println("Input Key: " + keyText);
                    }
                    for (FKeyListener keyListener : keyListeners) {
                        if (keyText.equals(keyListener.key)) {
                            JsEnv.getJsEnv(null).invokeFunction(keyListener.func);
                            repaint();
                        }
                    }
                } 
            } 

            @Override 
            public void keyReleased(KeyEvent e) { 

            } 
        });
    }

    ArrayList<ArrayList<Color>> tableBgColor = null;
    ArrayList<ArrayList<Color>> tableFgColor = null;
    
    public void setTable(ArrayList<ArrayList<String>> datas,
            ArrayList<ArrayList<Color>> fgc, ArrayList<ArrayList<Color>> bgc,
            ArrayList<Integer> width) {
        tableFgColor = fgc;
        tableBgColor = bgc;
        
        DefaultTableModel tableModel = (DefaultTableModel) jTableInfo.getModel();
        tableModel.setColumnCount(0);
        tableModel.setRowCount(0);
        
        for (int i=0; i<datas.get(0).size(); i++) {
            String title = datas.get(0).get(i);
            tableModel.addColumn(title);
        }
        
        DefaultTableCellRenderer tcr = new DefaultTableCellRenderer() { 
            @Override
            public Component getTableCellRendererComponent(JTable table,
                    Object value, boolean isSelected, boolean hasFocus, int row,
                    int column)
            {
                Component comp = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                
                Color bg = new Color(240,240,240);
                
                try {
                    Color color = tableFgColor.get(row).get(column);
                    comp.setForeground(color);
                }
                catch (Exception e) {
                    comp.setForeground(Color.black);
                }

                try {
                    Color color = tableBgColor.get(row).get(column);
                    comp.setBackground(color);
                }
                catch (Exception e) {
                    if (row % 2 == 0){
                        comp.setBackground(Color.white);
                    }
                    else {
                        comp.setBackground(bg);
                    }
                }
                return comp;
            }
        };

        for (int i=0; i<datas.get(0).size(); i++) {
            //String title = datas.get(0).get(i);
            jTableInfo.getColumnModel().getColumn(i).setCellRenderer(tcr);
            //jTableInfo.getColumn(title).setCellRenderer(tcr);
        }

        if (false) {
            jTableInfo.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);

            int col = 0;
            int w = 0;
            for (int i=0; i<width.size(); i++) {
                if (width.get(i) < 0) {
                    col ++;
                }
                else {
                    w += width.get(i);
                }
            }
            for (int i=0; i<width.size(); i++) {
                if (width.get(i) < 0) {
                    width.set(i, (jTableInfo.getWidth() - w)/col);
                }
            }

            for (int i=0; i<datas.get(0).size() && i < width.size(); i++) {
                jTableInfo.getColumnModel().getColumn(i).setPreferredWidth(width.get(i));
            }
        }
        
        for(int i=1; i<datas.size(); i++){
            ArrayList<String> data = datas.get(i);
            
            String[] arr = new String[data.size()];
            
            for (int j=0; j<data.size(); j++) {
                arr[j] = data.get(j);
            }
            tableModel.addRow(arr);
        }

        jTableInfo.invalidate();
    }

    public void setText(String text) {
        jTextAreaInfo.setText(text);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jSplitPaneLR = new javax.swing.JSplitPane();
        waveJPanel = new jswave.ui.WaveJPanel();
        jSplitPaneUD = new javax.swing.JSplitPane();
        jScrollPaneU = new javax.swing.JScrollPane();
        jTableInfo = new javax.swing.JTable();
        jScrollPaneD = new javax.swing.JScrollPane();
        jTextAreaInfo = new javax.swing.JTextArea();
        jMenuBarMain = new javax.swing.JMenuBar();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jSplitPaneLR.setDividerLocation(600);

        javax.swing.GroupLayout waveJPanelLayout = new javax.swing.GroupLayout(waveJPanel);
        waveJPanel.setLayout(waveJPanelLayout);
        waveJPanelLayout.setHorizontalGroup(
            waveJPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 599, Short.MAX_VALUE)
        );
        waveJPanelLayout.setVerticalGroup(
            waveJPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 764, Short.MAX_VALUE)
        );

        jSplitPaneLR.setLeftComponent(waveJPanel);

        jSplitPaneUD.setDividerLocation(450);
        jSplitPaneUD.setOrientation(javax.swing.JSplitPane.VERTICAL_SPLIT);

        jTableInfo.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {

            }
        ));
        jTableInfo.setEnabled(false);
        jTableInfo.setName(""); // NOI18N
        jScrollPaneU.setViewportView(jTableInfo);

        jSplitPaneUD.setTopComponent(jScrollPaneU);

        jTextAreaInfo.setColumns(20);
        jTextAreaInfo.setRows(5);
        jTextAreaInfo.setDisabledTextColor(new java.awt.Color(0, 0, 0));
        jTextAreaInfo.setDoubleBuffered(true);
        jTextAreaInfo.setEnabled(false);
        jScrollPaneD.setViewportView(jTextAreaInfo);

        jSplitPaneUD.setRightComponent(jScrollPaneD);

        jSplitPaneLR.setRightComponent(jSplitPaneUD);

        jMenuBarMain.setFont(jMenuBarMain.getFont());
        setJMenuBar(jMenuBarMain);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jSplitPaneLR, javax.swing.GroupLayout.DEFAULT_SIZE, 1024, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jSplitPaneLR)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JMenuBar jMenuBarMain;
    private javax.swing.JScrollPane jScrollPaneD;
    private javax.swing.JScrollPane jScrollPaneU;
    private javax.swing.JSplitPane jSplitPaneLR;
    private javax.swing.JSplitPane jSplitPaneUD;
    private javax.swing.JTable jTableInfo;
    private javax.swing.JTextArea jTextAreaInfo;
    private jswave.ui.WaveJPanel waveJPanel;
    // End of variables declaration//GEN-END:variables
}
