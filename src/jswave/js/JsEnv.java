/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jswave.js;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import jswave.ui.JsWaveJFrame;

/**
 *
 * @author cttmayi
 */
public class JsEnv {
    ScriptEngine engine;

    static JsEnv jsEnv;
    public static JsEnv getJsEnv(JsWaveJFrame frame) {
        if (jsEnv == null) {
            jsEnv = new JsEnv();
            jsEnv.frame = frame;
        }
        return jsEnv;
    }
    
    public JsWaveJFrame frame;    

    public JsEnv(){
        ScriptEngineManager mgr = new ScriptEngineManager();    
        engine = mgr.getEngineByExtension("js");
    }
    
    public void loadFile(String filename) {
        //if (Util.isDebug) {
        System.out.println("LoadFile: " + filename);
        //}
        try {
            engine.eval(loadAFileToString(filename));
        } catch (ScriptException ex) {
            Logger.getLogger(JsEnv.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void loadString(String soucre) {

        try {
            engine.eval(soucre);
        } catch (ScriptException ex) {
            Logger.getLogger(JsEnv.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void defineFuntion(String function) {
        loadString("function " + function + "(){}");
    }
    
    public boolean invokeFunction(String functionname, Object ... args) {
        Invocable inv = (Invocable) engine;
        try {
            inv.invokeFunction(functionname, args);
        } catch (ScriptException ex) {
            Logger.getLogger(JsEnv.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        } catch (NoSuchMethodException ex) {
            Logger.getLogger(JsEnv.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
        return true;
    }
    
    
    private String loadAFileToString(String filename) {
        File f = new File(filename);
        BufferedReader br = null;
        String ret = null;
        try {
            br =  new BufferedReader(new FileReader(f));
            String line;
            StringBuilder sb = new StringBuilder((int)f.length());
            while( (line = br.readLine()) != null ) {
                sb.append(line).append("\n");
            }
            ret = sb.toString();
        }
        catch(Exception e) { 
        } finally {
            if(br!=null) {try{br.close();} catch(Exception e){} }
        }
        return ret;        
    }
}
