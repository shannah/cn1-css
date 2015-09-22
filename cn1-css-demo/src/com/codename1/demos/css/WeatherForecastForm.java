/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.codename1.demos.css;

import com.codename1.ui.Button;
import com.codename1.ui.Command;
import com.codename1.ui.Container;
import com.codename1.ui.Display;
import com.codename1.ui.FontImage;
import com.codename1.ui.Form;
import com.codename1.ui.Label;
import com.codename1.ui.Tabs;
import com.codename1.ui.events.ActionEvent;
import com.codename1.ui.events.ActionListener;
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
public class WeatherForecastForm extends Form{
    static Resources res;
    static boolean resourcesLoaded;
    
    public static void init() {
        if (!resourcesLoaded) {
            try {
                res = Resources.openLayered("/WeatherForecastForm.css");
            } catch (IOException ex) {
                throw new RuntimeException(ex.getMessage());
            }
            //UIManager.getInstance().
            UIManager.getInstance().addThemeProps(res.getTheme("Theme"));
            resourcesLoaded = true;
        }
    }
    
    public WeatherForecastForm() {
        super("Forecast");
        init();
        /*UIIDTransformer t = new UIIDTransformer(new String[]{
            "TitleArea", "WeatherForecastFormTitleArea",
            "Title", "WeatherForecastFormTitle",
            "TitleCommand", "WeatherForecastFormTitleCommand",
            "BackCommand", "WeatherForecastFormBackCommand"
        });
        t.transform(this);
                */
        
        setUIID("WeatherForecastForm");
        
        
        
        setLayout(new BorderLayout());
        
        
        Container south = new Container();
        south.setUIID("WeatherForecastFormSouth");
        south.setLayout(new BoxLayout(BoxLayout.Y_AXIS));
        
        Label cityLabel = new Label("San Francisco");
        cityLabel.setUIID("WeatherForecastCityLabel");
        Label stateLabel = new Label("California");
        stateLabel.setUIID("WeatherForecastStateLabel");
        
        south.addComponent(cityLabel);
        south.addComponent(stateLabel);
        addComponent(BorderLayout.SOUTH, south);
        
        
        Container center = new Container();
        center.setLayout(new BorderLayout());
        center.setUIID("WeatherForecastFormCenter");
        
        
        
        Container centerCenter = new Container();
        centerCenter.setLayout(new BoxLayout(BoxLayout.Y_AXIS));
        centerCenter.setUIID("WeatherForecastFormCenterCenter");
        
        Label degrees = new Label(" 24°");
        degrees.setUIID("WeatherForecastDegrees");
        centerCenter.addComponent(degrees);
        Label icon = new Label("\uf1b2");
        icon.setUIID("WeatherForecastIcon");
        //
        //centerCenter.addComponent(icon);
        Label clear = new Label("Clear" );
        clear.setIcon(FontImage.create("\uf0c2", icon.getStyle()));
        
        clear.setUIID("WeatherForecastClear");
        centerCenter.addComponent(clear);
        
        center.addComponent(BorderLayout.CENTER, centerCenter);
        
        addComponent(BorderLayout.CENTER, center);
        
        final Form backForm = Display.getInstance().getCurrent();
        this.setBackCommand(new Command("Back", FontImage.create("\uf060", icon.getStyle())) {
            public void actionPerformed(ActionEvent evt) {
                backForm.showBack();
            }
        });
        
        
        
        
       
        
    }
}
