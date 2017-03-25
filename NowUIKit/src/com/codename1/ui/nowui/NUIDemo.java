package com.codename1.ui.nowui;


import com.codename1.components.OnOffSwitch;
import com.codename1.ui.Button;
import com.codename1.ui.Calendar;
import com.codename1.ui.Command;
import com.codename1.ui.Container;
import com.codename1.ui.Display;
import com.codename1.ui.FontImage;
import com.codename1.ui.Form;
import com.codename1.ui.Graphics;
import com.codename1.ui.Image;
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
import com.codename1.ui.plaf.Style;
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
        
        menu.addMenuItem("Radial Gradients", (char)0xf031, new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                showRadialGradients();
            }
        });
        
        menu.addMenuItem("Linear Gradients", (char)0xf031, new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                showLinearGradients();
            }
        });
        
        menu.addMenuItem("Round Borders", (char)0xf031, new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                showRoundBorders();
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
        
        /*
        PlainText0p5mm {
    font-size: 0.5mm;
}

PlainText1mm {
    font-size: 1mm;
}

PlainText2mm {
    font-size: 2mm;
}

PlainText5mm {
    font-size: 5mm;
}

PlainText10mm {
    font-size: 10mm;
}

PlainText50mm {
    font-size: 50mm;
}

PlainTextSmall {
    font-size: small;
}

PlainTextMedium {
    font-size: medium;
}

PlainTextLarge {
    font-size: large;
}

PlainText3pt {
    font-size: 3pt;
}

PlainText6pt {
    font-size: 6pt;
}

PlainText12pt {
    font-size: 12pt;
}

PlainText20pt {
    font-size: 20pt;
}

PlainText36pt {
    font-size: 36pt;
}

BoldText {
    font-weight: bold;
}

BoldText1mm {
    font-weight: bold;
    font-size: 1mm;
}

BoldText2mm {
    font-weight: bold;
    font-size: 2mm;
}

BoldText3mm {
    font-weight: bold;
    font-size: 3mm;
}

BoldText5mm {
    font-weight: bold;
    font-size: 5mm;
}

ItalicText {
    font-style: italic;
}

ItalicText3mm {
    font-style: italic;
    font-size: 3mm;
}

ItalicBoldText {
    font-style: italic;
    font-weight: bold;
}
        */
        String[] styles = new String[]{
            "PlainText0p5mm", "font-size: 0.5mm",
            "PlainText1mm", "font-size: 1mm",
            "PlainText2mm", "font-size: 2mm",
            "PlainText5mm", "font-size: 5mm",
            "PlainText10mm", "font-size: 10mm",
            "PlainText50mm", "font-size: 50mm",
            "PlainTextSmall", "font-size: small",
            "PlainTextMedium", "font-size: medium",
            "PlainTextLarge", "font-size: large",
            "PlainText3pt", "font-size: 3pt",
            "PlainText6pt", "font-size: 6pt",
            "PlainText12pt", "font-size: 12pt",
            "PlainText20pt", "font-size: 20pt",
            "PlainText36pt", "font-size: 36pt",
            "BoldText", "font-weight: bold",
            "BoldText1mm", "font-weight:bold; font-size: 1mm",
            "BoldText2mm", "font-weight:bold; font-size: 2mm",
            "BoldText3mm", "font-weight:bold; font-size: 3mm",
            "BoldText5mm", "font-weight:bold; font-size: 5mm",
            "ItalicText", "font-style:italic",
            "ItalicText3mm", "font-style:italic; font-size:3mm",
            "ItalicBoldText", "font-style:italic; font-weight:bold",
            "PlainTextUnderline", "text-decoration: underline",
            "BoldTextUnderline", "text-decoration:underline; font-weight:bold",
            "ItalicTextUnderline", "text-decoration:underline; font-style:italic",
            "PlainText3d", "text-decoration:cn1-3d",
            "BoldText3d", "text-decoration:cn1-3d; font-weight: bold",
            "ItalicText3d", "text-decoration:cn1-3d; font-style: italic",
            "PlainText3dLowered", "text-decoration:cn1-3d-lowered",
            "BoldText3dLowered", "text-decoration:cn1-3d-lowered; font-weight: bold",
            "ItalicText3dLowered", "text-decoration:cn1-3d-lowered; font-style:italic",
            "PlainText3dShadow", "text-decoration:cn1-3d-shadow-north",
            "BoldText3dShadow", "text-decoration:cn1-3d-shadow-north; font-weight:bold",
            "ItalicText3dShadow", "text-decoration:cn1-3d-shadow-north; font-style:italic"
              
        };
        
        int len = styles.length;
        for (int i=0; i<len; i+=2) {
            root.add(new Label(styles[i+1]));
            Label l = new Label("The Quick Brown Fox Jumped");
            l.setUIID(styles[i]);
            root.add(l);
        }
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
    
    private void showRadialGradients() {
        Form f = new Form("RadialGradients");
        installSideMenu(f);
        
        Tabs t = new Tabs();
        Container c1 = new Container();
        c1.setUIID("RGC100");
        t.addTab("Center 100%", c1);
        
        Container c2 = new Container();
        c2.setUIID("RGC200");
        t.addTab("Center 200%", c2);
        
        Container c3 = new Container();
        c3.setUIID("RGCX0100");
        t.addTab("Center X=0", c3);
        
        Container c4 = new Container();
        c4.setUIID("RGCX1100");
        t.addTab("Center X=1", c4);
        
        f.setLayout(new BorderLayout());
        f.addComponent(BorderLayout.CENTER, t);
        f.show();
    }
    
    private void showLinearGradients() {
        Form f = new Form("Linear Gradients", new BorderLayout());
        installSideMenu(f);
        
        
        /*
        
LG0 {
    background: linear-gradient(0deg, #ccc, #666);
}

LGToTop {
    background: linear-gradient(to top, #ccc, #666);
}

LG90 {
    background: linear-gradient(90deg, #ccc, #666);
}

LGToLeft {
    background: linear-gradient(to left, #ccc, #666);
}

LG45 {
    background: linear-gradient(45deg, #ccc, #666);
}

LGDiffAlpha {
    background: linear-gradient(90deg, rgba(255, 0, 0, 0.5), blue);
}

        *
        */
        String[] styles = {
            "LG0", "linear-gradient(0deg, #ccc, #666)",
            "LGToTop", "linear-gradient(to top, #ccc, #666)",
            "LG90", "linear-gradient(90deg, #ccc, #666)",
            "LGToLeft", "linear-gradient(to left, #ccc, #666)",
            "LG45", "linear-gradient(45deg, #cccccc, #666)",
            "LGDiffAlpha", "linear-gradient(90deg, rgba(255, 0, 0, 0.5), blue)"
        };
        int len = styles.length;
        Container center = new Container(new BoxLayout(BoxLayout.Y_AXIS));
        center.setScrollableY(true);
        f.add(BorderLayout.CENTER, center);
        for (int i=0; i<len; i+=2) {
            Container cnt = new Container();
            cnt.setUIID(styles[i]);
            cnt.setPreferredH(2 * Display.getInstance().getDisplayHeight()/ 3);
            center.add(new Label(styles[i+1]));
            center.add(cnt);
        }
        f.show();
    }
    
    
    private void showRoundBorders() {
        Form f = new Form("Round Borders", new BorderLayout());
        installSideMenu(f);
        /*
        PillBorder {
            border: 1px #3399ff cn1-pill-border;
        }

        RoundBorder {
            border: 1px #ee99ff cn1-round-border;
        }

        BorderRadius {
            border: 1px solid #3399ff;
            border-radius: 3mm;
        }
        */
        
        Style s = new Style();
        s.setFgColor(0x336699);
        s.setBgTransparency(0);
        
        Object[] styles = new Object[]{
            "PillBorder", "border: 1px #3399ff cn1-pill-border", "Hello World",
            "PillBorderFilled", "background: #33099ff cn1-pill-border", "Hello World",
            "RoundBorder", "border: 1px #ee99ff cn1-round-border", FontImage.createMaterial(FontImage.MATERIAL_ALARM, s, 4f),
            "RoundBorderFilled", "background: #ccc cn1-round-border", FontImage.createMaterial(FontImage.MATERIAL_ALARM, s, 4f),
            "BorderRadius", "border: 1px solid #3399ff; border-radius: 3mm", "Hello World",
            "BorderRadiusFilled", "border: 1px solid transparent; border-radius: 3mm; background-color:#ccc", "Hello World"
        };
        
        Container root = new Container(new BoxLayout(BoxLayout.Y_AXIS));
        root.setScrollableY(true);
        int len = styles.length;
        for (int i=0; i<len; i+=3) {
            root.add(new Label(String.valueOf(styles[i+1])));
            Object content = styles[i+2];
            Label l = new Label();
            if (content instanceof Image) {
                l.setIcon((Image)content);
            } else {
                l.setText(String.valueOf(content));
            }
            l.setUIID(String.valueOf(styles[i]));
            root.add(l);
        }
        f.add(BorderLayout.CENTER, root);
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
