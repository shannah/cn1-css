package com.codename1.themes.nowui;


import com.codename1.ui.Display;
import com.codename1.ui.Form;
import com.codename1.ui.Label;
import com.codename1.ui.events.ActionEvent;
import com.codename1.ui.events.ActionListener;
import com.codename1.ui.plaf.UIManager;
import com.codename1.ui.util.Resources;
import java.io.IOException;

public class NowUIKitDemo {

    private Form current;
    private Resources theme;

    public void init(Object context) {
        theme = UIManager.initFirstTheme("/nowui.css");
        //Display.getInstance().setFramerate(120);
        // Pro users - uncomment this code to get crash reports sent to you automatically
        /*Display.getInstance().addEdtErrorHandler(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                evt.consume();
                Log.p("Exception in AppName version " + Display.getInstance().getProperty("AppVersion", "Unknown"));
                Log.p("OS " + Display.getInstance().getPlatformName());
                Log.p("Error " + evt.getSource());
                Log.p("Current Form " + Display.getInstance().getCurrent().getName());
                Log.e((Throwable)evt.getSource());
                Log.sendLog();
            }
        });*/
    }
    
    public void start() {
        if(current != null){
            current.show();
            return;
        }
        LoginForm hi = new LoginForm(theme);
        hi.getSignupButton().addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                new SignupForm(theme).show();
                
            }
        });
        
        hi.getSubmitButton().addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent evt) {
                new ArticleForm(theme, "News Article", "http://dev.weblite.ca/demo-xmlview.xml").show();
            }
           
        });
        
        hi.getHelpButton().addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent evt) {
                new ArticleListForm(theme).show();
            }
           
        });
        hi.show();
    }

    public void stop() {
        current = Display.getInstance().getCurrent();
    }
    
    public void destroy() {
    }

}
