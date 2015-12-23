/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jswave.ui;

import java.awt.Dimension;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowStateListener;
import java.io.File;
import java.util.ArrayList;
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
    
    private ArrayList<FKeyListener> keyListeners = new ArrayList<FKeyListener>();
    
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
        String libPath = jarPath + "libs\\";
        
        File dir = new File(libPath);
        File files[] = dir.listFiles();
        if (files != null) {
            for (File file : files) {
                if (file.isDirectory()) {
                } else {
                    jsEnv.loadFile(file.getAbsolutePath());
                }
            }
        }

        if (Util.getPara(Util.PARA_PATH) != null) {
            if (Util.isFileExist(Util.getPara(Util.PARA_PATH))) {
                jsEnv.loadFile(Util.getPara(Util.PARA_PATH));
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
                String keyText = KeyEvent.getKeyText(e.getKeyCode()); 
                if (!e.isActionKey()) { 
                    if (keyText.equals("W")) {
                        waveJPanel.scalerUp();
                    }
                    else if (keyText.equals("S")) {
                        waveJPanel.scalerDown();
                    }
                    else if (keyText.equals("A")) {
                        waveJPanel.moveLeft();
                    }
                    else if (keyText.equals("D")) {
                        waveJPanel.moveRight();
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
    
    public void setTable(ArrayList<String> names, ArrayList<String> datas, ArrayList<String> datars) {

        DefaultTableModel tableModel = (DefaultTableModel) jTableInfo.getModel();
        tableModel.setColumnCount(0);
        tableModel.setRowCount(0);

        if (0 < names.size()){
            tableModel.addColumn(names.get(0));
        }
        if (0 < datas.size()){
            tableModel.addColumn(datas.get(0));
        }
        if (0 < datars.size()){
            tableModel.addColumn(datars.get(0));
        }
        for(int id=1; id <names.size(); id++){
            String[] arr=new String[3];
            arr[0]=names.get(id);
            if (id < datas.size()){
                arr[1]=datas.get(id);
            }
            if (id < datars.size()){
                arr[2]=datars.get(id);
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
                {"", null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null}
            },
            new String [] {
                "Name", "Data", "Data"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
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
