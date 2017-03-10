package com.codename1.ui.nowui;


import com.codename1.components.OnOffSwitch;
import com.codename1.ui.Button;
import com.codename1.ui.Calendar;
import com.codename1.ui.Command;
import com.codename1.ui.Container;
import com.codename1.ui.Display;
import com.codename1.ui.Form;
import com.codename1.ui.Graphics;
import com.codename1.ui.Label;
import com.codename1.ui.Tabs;
import com.codename1.ui.TextField;
import com.codename1.ui.Toolbar;
import com.codename1.ui.events.ActionEvent;
import com.codename1.ui.events.ActionListener;
import com.codename1.ui.layouts.BorderLayout;
import com.codename1.ui.layouts.BoxLayout;
import com.codename1.ui.layouts.FlowLayout;
import com.codename1.ui.nowui.NUIFactory.ComponentStyle;
import com.codename1.ui.util.Resources;



public class NUIDemo {

    private Form current;
    private Resources theme;
    
    public void init(Object context) {
        try {
            NUI.install();
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new RuntimeException(ex.getMessage());
        }
        theme = NUI.getTheme();
        
        //theme = UIManager.initFirstTheme("/nowui.css");
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
    
    private void installSideMenu(Form f) {
        NUISideMenu menu = new NUISideMenu(f);
        menu.addMenuItem("BUTTONS", (char)0xf00a, new ActionListener() {

            public void actionPerformed(ActionEvent evt) {
                showButtonsForm();
            }
        });
        
        menu.addMenuItem("TEXT FIELDS", (char)0xf044, new ActionListener() {

            public void actionPerformed(ActionEvent evt) {
                showTextFieldsForm();
            }
            
        });
        
        menu.addMenuItem("ICONS", (char)0xf024, new ActionListener() {

            public void actionPerformed(ActionEvent evt) {
                showFontAwesomeIconsForm();
            }
            
        });
        
        menu.addMenuItem("CALENDAR", (char)0xf073, new ActionListener() {

            public void actionPerformed(ActionEvent evt) {
                showCalendarForm();
            }
            
        });
        
        menu.addMenuItem("WIDGETS", (char)0xf073, new ActionListener() {

            public void actionPerformed(ActionEvent evt) {
                showWidgetsForm();
            }
            
        });
        
        menu.addMenuItem("NATIVE FONTS", (char)0xf031, new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                showNativeFonts();
            }
        });
        
        
        
    }
    
    public void showFontAwesomeIconsForm() {
        Form f = new Form("Icons");
        installSideMenu(f);
        f.setLayout(new BorderLayout());
        
        NUI.apply(f.getContentPane(), ComponentStyle.BackgroundDark);
        
        f.addComponent(BorderLayout.NORTH, NUI.apply(new Label("FONT AWESOME ICONS"), ComponentStyle.HeadingLightLarge));
        
        
        
        
        //f.addComponent(darkPanel);
        //f.addComponent(lightPanel);
        
        Container circleButtons1 = NUI.apply(new Container(new FlowLayout()), ComponentStyle.BackgroundDark);
        circleButtons1.setScrollableY(true);
        for (char c = 0xf000; c < 0xf287; c++) {
            for (ComponentStyle style : ComponentStyle.values()) {
                switch (style) {
                    case CircleButtonFilledLight:
                    case CircleButtonStrokedLight:
                    case CircleButtonTransparentLight: {
                        
                        Button b = new Button();
                        NUI.apply(b, style);
                        NUI.setIcon(b, style, c);
                        circleButtons1.addComponent(b);
                        
                        break;
                    }
                        
                }
            }
        }
        
        //f.addComponent(circleButtons1);
        
        Container circleButtons2 = NUI.apply(new Container(new FlowLayout()), ComponentStyle.BackgroundLight);
        circleButtons2.setScrollableY(true);
        for (char c = 0xf000; c < 0xf287; c++) {
            for (ComponentStyle style : ComponentStyle.values()) {
                switch (style) {
                    case CircleButtonFilledDark:
                    case CircleButtonStrokedDark:
                    case CircleButtonTransparentDark: {
                        
                        Button b = new Button();
                        NUI.apply(b, style);
                        NUI.setIcon(b, style, c);
                        circleButtons2.addComponent(b);
                        
                        break;
                    }
                        
                }
            }
        }
        
        //f.addComponent(circleButtons);
        
        Tabs tabs = new Tabs();
        tabs.addTab("Dark", circleButtons1);
        tabs.addTab("Light", circleButtons2);
        f.addComponent(BorderLayout.CENTER, tabs);
        
        f.show();
    }
    
    
    public void showNativeFonts() {
        Form f = new Form("Native Fonts");
        installSideMenu(f);
        f.setLayout(new BorderLayout());
        Label thin = new Label("Main Thin: The Quick Brown Fox Jumped");
        thin.setUIID("MainThin");
        
        Label regular = new Label("Main Regular: The Quick Brown Fox Jumped");
        regular.setUIID("MainRegular");
        
        Label regular0001 = new Button("Main Regular: The Quick Brown Fox Jumped");
        regular0001.setUIID("MainRegular0001");
        
        Label regular1001 = new Button("Main Regular: The Quick Brown Fox Jumped");
        regular1001.setUIID("MainRegular1001");
        
        Label regular0p5001 = new Label("Main Regular: The Quick Brown Fox Jumped");
        regular0p5001.setUIID("MainRegular0p5001");
        
        
        Label regular2001 = new Label("Main Regular: The Quick Brown Fox Jumped");
        regular2001.setUIID("MainRegular2001");
        
        Label regular5001 = new Label("Main Regular: The Quick Brown Fox Jumped");
        regular5001.setUIID("MainRegular5001");
        
        Label regular0101 = new Label("Main Regular: The Quick Brown Fox Jumped");
        regular0101.setUIID("MainRegular0101");
        
        Label regularm1001 = new Label("Main Regular: The Quick Brown Fox Jumped");
        regularm1001.setUIID("MainRegularm1001");
        
        Label regular0m101 = new Label("Main Regular: The Quick Brown Fox Jumped");
        regular0m101.setUIID("MainRegular0m101");
        
        Container root = BoxLayout.encloseY(thin, regular, 
                new Label("shadowX(0), shadowY(0), blur(0), spread(1):"),
                regular0001,
                new Label("shadowX(1), shadowY(0), blur(0), spread(1):"),
                regular1001,
                new Label("shadowX(2), shadowY(0), blur(0), spread(1):"),
                regular2001,
                new Label("shadowX(0.5), shadowY(0), blur(0), spread(1):"),
                regular0p5001,
                new Label("shadowX(5), shadowY(0), blur(0), spread(1):"),
                regular5001,
                new Label("shadowX(0), shadowY(1), blur(0), spread(1):"),
                regular0101,
                new Label("shadowX(-1), shadowY(0), blur(0), spread(1):"),
                regularm1001,
                new Label("shadowX(0), shadowY(-1), blur(0), spread(1):"),
                regular0m101
                
        );
        root.setScrollableY(true);
        f.add(BorderLayout.CENTER, root
        );
        f.show();
    }
    
    public void showCalendarForm() {
        Form f = new Form("Calendar");
        installSideMenu(f);
        f.setLayout(new BorderLayout());
        
        NUI.apply(f.getContentPane(), ComponentStyle.BackgroundDark);
        
        f.addComponent(BorderLayout.NORTH, NUI.apply(new Label("CALENDAR"), ComponentStyle.HeadingLightLarge));
        
        Container c = NUI.apply(new Container(new BoxLayout(BoxLayout.Y_AXIS)), ComponentStyle.BackgroundLight);
        Calendar cal = new Calendar();
        c.addComponent(cal);
        f.addComponent(BorderLayout.CENTER, c);
        f.show();
    }
    
    public void showWidgetsForm() {
        Form f = new Form("Widgets");
        installSideMenu(f);
        f.setLayout(new BoxLayout(BoxLayout.Y_AXIS));
        NUI.apply(f.getContentPane(), ComponentStyle.BackgroundLight);
        OnOffSwitch onOffSwitch = new OnOffSwitch();
        onOffSwitch.setLabelForComponent(new Label("Foobar"));
        f.addComponent(onOffSwitch);
        f.show();
    }
    
    public void showListsForm() {
        Form f = new Form("Lists");
        installSideMenu(f);
        f.setLayout(new BorderLayout());
        NUI.apply(f.getContentPane(), ComponentStyle.BackgroundDark);
        
        //NUIList commentsList = new NUIList(NUIList.ListType.Comments);
        
    }
    
    public void showTextFieldsForm() {
        Form f = new Form("Text Fields");
        installSideMenu(f);
        f.setLayout(new BorderLayout());
        
        NUI.apply(f.getContentPane(), ComponentStyle.BackgroundDark);
        
        f.addComponent(BorderLayout.NORTH, NUI.apply(new Label("TEXT FIELDS"), ComponentStyle.HeadingLightLarge));
        
        Container darkPanel = NUI.apply(new Container(new BoxLayout(BoxLayout.Y_AXIS)), ComponentStyle.BackgroundDark);
        darkPanel.setScrollableY(true);
        Container lightPanel = NUI.apply(new Container(new BoxLayout(BoxLayout.Y_AXIS)), ComponentStyle.BackgroundLight);
        lightPanel.setScrollableY(true);
        
        for (ComponentStyle style: ComponentStyle.values()) {
            if (style.name().indexOf("TextField") == -1 && style.name().indexOf("CommentsField") == -1 ) {
                continue;
            }
            if (style.name().indexOf("Dark") == -1) {
                continue;
            }
            String text = style.name().indexOf("Circle") != -1 ? "\uf1ba" : style.name();
            if (style.name().indexOf("Icon") != -1) {
                text = "\uf0e6" + text;
            }
            lightPanel.addComponent(NUI.apply(new TextField(text), style));
        }
        
        for (ComponentStyle style: ComponentStyle.values()) {
            if (style.name().indexOf("TextField") == -1 && style.name().indexOf("CommentsField") == -1 ) {
                continue;
            }
            if (style.name().indexOf("Light") == -1) {
                continue;
            }
            String text = style.name().indexOf("Circle") != -1 ? "\uf1ba" : style.name();
            if (style.name().indexOf("Icon") != -1) {
                text = "\uf0e6" + text;
            }
            darkPanel.addComponent(NUI.apply(new TextField(text), style));
            
            
        }
        
        TextField paperClipField = NUI.apply(new TextField("Comments Field"), ComponentStyle.CommentsFieldLight);
        NUI.setIcon(paperClipField, ComponentStyle.CommentsFieldLight, (char)0xf0c6);
        darkPanel.addComponent(paperClipField);
        
        paperClipField = NUI.apply(new TextField("Comments Field"), ComponentStyle.CommentsFieldDark);
        NUI.setIcon(paperClipField, ComponentStyle.CommentsFieldDark, (char)0xf0c6);
        lightPanel.addComponent(paperClipField);
        
        Tabs tabs = new Tabs();
        tabs.addTab("Dark", darkPanel);
        tabs.addTab("Light", lightPanel);
        f.addComponent(BorderLayout.CENTER, tabs);
        
        f.show();
    }
    
    
    
    public void showButtonsForm() {
        Form f = new Form("Buttons");
        installSideMenu(f);
        f.setLayout(new BorderLayout());
        
        NUI.apply(f.getContentPane(), ComponentStyle.BackgroundDark);
        
        f.addComponent(BorderLayout.NORTH, NUI.apply(new Label("BUTTONS"), ComponentStyle.HeadingLightLarge));
        
        
        Container darkPanel = NUI.apply(new Container(new BoxLayout(BoxLayout.Y_AXIS)), ComponentStyle.BackgroundDark);
        darkPanel.setScrollableY(true);
        Container lightPanel = NUI.apply(new Container(new BoxLayout(BoxLayout.Y_AXIS)), ComponentStyle.BackgroundLight);
        lightPanel.setScrollableY(true);
        
        for (ComponentStyle style: ComponentStyle.values()) {
            if (style.name().indexOf("Button") == -1 ) {
                continue;
            }
            if (style.name().indexOf("Dark") == -1) {
                continue;
            }
            String text = style.name().indexOf("Circle") != -1 ? "\uf1ba" : style.name();
            if (style.name().indexOf("Icon") != -1) {
                text = "\uf0e6" + text;
            }
            lightPanel.addComponent(NUI.apply(new Button(text), style));
        }
        
        
        
        for (ComponentStyle style: ComponentStyle.values()) {
            if ((style.name().indexOf("Heading") == -1 && style.name().indexOf("Text") != 0) || style.name().indexOf("Field") != -1) {
                continue;
            }
            if (style.name().indexOf("Dark") == -1) {
                continue;
            }
            String text = style.name().indexOf("Circle") != -1 ? "\uf1ba" : style.name();
            if (style.name().indexOf("Icon") != -1) {
                text = "\uf0e6" + text;
            }
            lightPanel.addComponent(NUI.apply(new Label(text), style));
        }
        
        for (ComponentStyle style: ComponentStyle.values()) {
            if (style.name().indexOf("Button") == -1) {
                continue;
            }
            if (style.name().indexOf("Light") == -1) {
                continue;
            }
            String text = style.name().indexOf("Circle") != -1 ? "\uf1ba" : style.name();
            if (style.name().indexOf("Icon") != -1) {
                text = "\uf0e6" + text;
            }
            darkPanel.addComponent(NUI.apply(new Button(text), style));
        }
        
        
        
        for (ComponentStyle style: ComponentStyle.values()) {
            if ((style.name().indexOf("Heading") == -1 && style.name().indexOf("Text") != 0) || style.name().indexOf("Field") != -1) {
                continue;
            }
            if (style.name().indexOf("Light") == -1) {
                continue;
            }
            String text = style.name().indexOf("Circle") != -1 ? "\uf1ba" : style.name();
            if (style.name().indexOf("Icon") != -1) {
                text = "\uf0e6" + text;
            }
            darkPanel.addComponent(NUI.apply(new Label(text), style));
        }
        
        
        //f.addComponent(circleButtons);
        
        Tabs tabs = new Tabs();
        tabs.addTab("Dark", darkPanel);
        tabs.addTab("Light", lightPanel);

        f.addComponent(BorderLayout.CENTER, tabs);
        
        f.show();
        
    }
    
    public void start() {
        showButtonsForm();
    }

    public void stop() {
        current = Display.getInstance().getCurrent();
    }
    
    public void destroy() {
    }

}
