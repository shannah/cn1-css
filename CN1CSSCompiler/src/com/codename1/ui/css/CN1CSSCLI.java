/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.codename1.ui.css;



import com.codename1.impl.javase.JavaSEPort;
import com.codename1.ui.Display;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Application;
import javafx.application.Platform;
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

    static WebView web;
    @Override
    public void start(Stage stage) throws Exception {
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
            JavaSEPort.setShowEDTWarnings(false);
            JFrame frm = new JFrame("Placeholder");
            frm.setVisible(false);
            Display.init(frm.getContentPane());
        new Thread(() -> {
            launch(CN1CSSCLI.class, "Nothing here");
        }).start();
        //Thread.sleep(5000);
        File inputFile = new File(inputPath);
        File outputFile = new File(outputPath);
        URL url = inputFile.toURI().toURL();
        //CSSTheme theme = CSSTheme.load(CSSTheme.class.getResource("test.css"));
        CSSTheme theme = CSSTheme.load(url);
        theme.cssFile = inputFile;
        theme.resourceFile = outputFile;
       
        Platform.runLater(() -> {
            new Thread(()-> {
                try {
                    
                    
                    File cacheFile = new File(theme.cssFile.getParentFile(), theme.cssFile.getName()+".checksums");
                    if (outputFile.exists() && cacheFile.exists()) {
                        theme.loadResourceFile();
                    
                        theme.loadSelectorCacheStatus(cacheFile);
                    }
                    
                    theme.createImageBorders(web);
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
            }).start();
            //System.out.println(theme.getHtmlPreview());
            //web.getEngine().loadContent(theme.getHtmlPreview());
        });
    }
}
