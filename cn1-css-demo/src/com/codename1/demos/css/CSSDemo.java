package com.codename1.demos.css;


import com.codename1.ui.Component;
import com.codename1.ui.Container;
import com.codename1.ui.Display;
import com.codename1.ui.Form;
import com.codename1.ui.Label;
import com.codename1.ui.TextField;
import com.codename1.ui.layouts.BoxLayout;
import com.codename1.ui.layouts.FlowLayout;
import com.codename1.ui.plaf.UIManager;
import com.codename1.ui.util.Resources;
import java.io.IOException;
import java.util.Hashtable;

public class CSSDemo {

    private Form current;
    private Resources theme;
    private Resources css;

    public void init(Object context) {
        try {
            theme = Resources.openLayered("/theme");
            css = Resources.openLayered("/theme.css");
            Hashtable vals = theme.getTheme(theme.getThemeResourceNames()[0]);
            vals.putAll(css.getTheme(css.getThemeResourceNames()[0]));
            UIManager.getInstance().setThemeProps(vals);
            Display.getInstance().setCommandBehavior(Display.COMMAND_BEHAVIOR_DEFAULT);
            //UIManager.getInstance().setThemeProps(css.getTheme(css.getThemeResourceNames()[0]));
        } catch(IOException e){
            e.printStackTrace();
        }
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
        if (true) {
            Form sf = new SignUpForm(css);
            sf.show();
            return;
        }
        Form hi = new Form("Hi World");
        hi.setUIID("GradientForm");
        hi.setLayout(new BoxLayout(BoxLayout.Y_AXIS));
        Label logo = new Label(css.getImage("Logo.png"));
        
        //logo.setAlignment(Component.CENTER);
        Container logoWrapper = new Container();
        logoWrapper.setLayout(new FlowLayout(Component.CENTER));
        logoWrapper.setUIID("Center");
        logoWrapper.addComponent(logo);
        
        hi.addComponent(logoWrapper);
        
        TextField username = new TextField();
        username.setHint("Email");
        username.setUIID("UsernameField");
        hi.addComponent(username);
        
        TextField password = new TextField();
        password.setHint("Password");
        password.setConstraint(TextField.PASSWORD);
        password.setUIID("PasswordField");
        hi.addComponent(password);
        hi.show();
    }

    public void stop() {
        current = Display.getInstance().getCurrent();
    }
    
    public void destroy() {
    }

}
