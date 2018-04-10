/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.codename1.ui.css;



import com.codename1.impl.javase.JavaSEPort;
import com.codename1.ui.Display;
import com.codename1.ui.css.CSSTheme.WebViewProvider;
import java.io.File;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Application;
import static javafx.application.Application.launch;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.scene.web.WebView;
import javafx.stage.Stage;
import javax.swing.JFrame;

/**
 *
 * @author shannah
 */
public class CN1CSSCLI extends Application {
    static Object lock = new Object();
    static WebView web;
    @Override
    public void start(Stage stage) throws Exception {
        System.out.println("Opening JavaFX Webview to render some CSS styles");
        web = new WebView();
        web.getEngine().getLoadWorker().exceptionProperty().addListener(new ChangeListener<Throwable>() {
            @Override
            public void changed(ObservableValue<? extends Throwable> ov, Throwable t, Throwable t1) {
                System.out.println("Received exception: "+t1.getMessage());
            }
        });
        Scene scene = new Scene(web, 400, 800, Color.web("#666670"));
        stage.setScene(scene);
        stage.show();
        synchronized(lock) {
            lock.notify();
        }
        //stage.hide();
        
    }
    
    
    public static void main(String[] args) throws Exception {
        
        String inputPath = "test.css";
        
        if (args.length > 0) {
            inputPath = args[0];
        }
            
        String outputPath = inputPath+".res";
        
        if (args.length > 1) {
            outputPath = args[1];
        }
        
        JavaSEPort.setShowEDTViolationStacks(false);
        JavaSEPort.blockMonitors();
            JavaSEPort.setShowEDTWarnings(false);
            JFrame frm = new JFrame("Placeholder");
            frm.setVisible(false);
            Display.init(frm.getContentPane());
        
        //Thread.sleep(5000);
        File inputFile = new File(inputPath);
        File outputFile = new File(outputPath);
        URL url = inputFile.toURI().toURL();
        //CSSTheme theme = CSSTheme.load(CSSTheme.class.getResource("test.css"));
        CSSTheme theme = CSSTheme.load(url);
        theme.cssFile = inputFile;
        theme.resourceFile = outputFile;
        JavaSEPort.setBaseResourceDir(outputFile.getParentFile());
        WebViewProvider webViewProvider = new WebViewProvider() {

            @Override
            public WebView getWebView() {
                if (web == null) {
                    new Thread(()->{
                        launch(CN1CSSCLI.class, new String[0]);
                    }).start();
                }
                while (web == null) {
                    synchronized(lock) {
                        try {
                            lock.wait();
                        } catch (InterruptedException ex) {
                            Logger.getLogger(CN1CSSCLI.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                }
                return web;
            }
            
        };
        
        try {
                    
                    
            File cacheFile = new File(theme.cssFile.getParentFile(), theme.cssFile.getName()+".checksums");
            if (outputFile.exists() && cacheFile.exists()) {
                theme.loadResourceFile();

                theme.loadSelectorCacheStatus(cacheFile);
            }

            theme.createImageBorders(webViewProvider);
            theme.updateResources();
            theme.save(outputFile);
            theme.saveSelectorChecksums(cacheFile);
            //Platform.runLater(()-> {
            //    web.getEngine().executeScript("$('div').show()");
            //});
            frm.dispose();
            System.exit(0);
        } catch (Throwable ex) {
            Logger.getLogger(CN1CSSCLI.class.getName()).log(Level.SEVERE, null, ex);
            frm.dispose();
            System.exit(1);
        } 
        
    }
}
