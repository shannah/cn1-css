/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.codename1.demos.css;

import com.codename1.components.InteractionDialog;
import com.codename1.components.SpanLabel;
import com.codename1.ui.Button;
import com.codename1.ui.Command;
import com.codename1.ui.Component;
import com.codename1.ui.Container;
import com.codename1.ui.Display;
import com.codename1.ui.Form;
import com.codename1.ui.Label;
import com.codename1.ui.events.ActionEvent;
import com.codename1.ui.events.ActionListener;
import com.codename1.ui.geom.Dimension;
import com.codename1.ui.layouts.BorderLayout;
import com.codename1.ui.layouts.BoxLayout;
import com.codename1.ui.layouts.GridLayout;
import com.codename1.ui.plaf.UIManager;
import com.codename1.ui.util.Resources;
import java.io.IOException;


/**
 *
 * @author shannah
 */
public class SamplesMenu extends Form {
    static Resources res;
    static boolean resourcesLoaded;
    public SamplesMenu() {
        super("CN1 CSS Samples");
        /*UIIDTransformer t = new UIIDTransformer(new String[]{
            "TitleArea", "SamplesMenuTitleArea",
            "Title", "SamplesMenuTitle",
            "TitleCommand", "SamplesMenuTitleCommand",
            "BackCommand", "SamplesMenuBackCommand"
        });
        t.transform(this);*/
        setUIID("SamplesMenu");
        
        
        if (!resourcesLoaded) {
            try {
                res = Resources.openLayered("/SamplesMenu.css");
            } catch (IOException ex) {
                throw new RuntimeException(ex.getMessage());
            }
            //UIManager.getInstance().
            UIManager.getInstance().addThemeProps(res.getTheme("Theme"));
            resourcesLoaded = true;
        }
        setLayout(new GridLayout(2,2));
        
        Button signUpForm = new Button("Sign Up Form", res.getImage("signupform-250.png"));
        signUpForm.setTextPosition(Component.BOTTOM);
        signUpForm.setGap(signUpForm.getStyle().getFont().getHeight()/2);
        signUpForm.setUIID("SamplesMenuButton");
        signUpForm.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent evt) {
                new SignUpForm().show();
            }
            
        });
        
        addComponent(signUpForm);
        
        Button weatherForecast = new Button("Weather Forecast", res.getImage("weatherform-250.png"));
        weatherForecast.setTextPosition(Component.BOTTOM);
        weatherForecast.setGap(signUpForm.getStyle().getFont().getHeight()/2);
        
        weatherForecast.setUIID("SamplesMenuButton");
        weatherForecast.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent evt) {
                new WeatherForecastForm().show();
            }
            
        });
        
        addComponent(weatherForecast);
        
        
        Button buttons = new Button("Buttons");
        buttons.setUIID("SamplesMenuButton");
        buttons.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent evt) {
                new ButtonsDemo().show();
            }
            
        });
        
        addComponent(buttons);
        
        addCommand(new Command("About") {
            public void actionPerformed(ActionEvent evt) {
                showAboutDialog();
            }
        });
        
    }
    
    private void showAboutDialog() {
        final InteractionDialog dlg = new InteractionDialog("About"); 
        
        dlg.setUIID("AboutDialog");
        dlg.setLayout(new BorderLayout()); 
        String desc = "This app was built using Codename One. The "
                + "designs and themes were created using the CN1-CSS plugin.";
        dlg.addComponent(BorderLayout.NORTH, new Label(res.getImage("logo-dark.png"))); 
        
        Container content = new Container(new BoxLayout(BoxLayout.Y_AXIS));
        content.setPreferredW(Display.getInstance().getDisplayWidth()/2);
        content.addComponent(new SpanLabel(desc));
        dlg.addComponent(BorderLayout.CENTER, content);
        
        Button learnMore = new Button("Learn more");
        learnMore.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                Display.getInstance().execute("http://shannah.github.io/cn1-css");
            }
        });
        
        content.addComponent(learnMore);
        
        Button close = new Button("Close"); 
        close.addActionListener(new ActionListener() { 
            public void actionPerformed(ActionEvent evt) { 
                dlg.dispose(); 
            } 
        }); 
        dlg.addComponent(BorderLayout.SOUTH, close); 
        Dimension pre = dlg.getContentPane().getPreferredSize(); 
        dlg.show(0, 0, Display.getInstance().getDisplayWidth() - (pre.getWidth() + pre.getWidth() / 6), 0); 
    }
}
