/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.codename1.ui.nowui;


import com.codename1.ui.Component;
import com.codename1.ui.Display;
import com.codename1.ui.Font;
import com.codename1.ui.plaf.Style;
import com.codename1.ui.plaf.UIManager;
import com.codename1.ui.util.Resources;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

/**
 *
 * @author shannah
 */
public class NUI {
    private static Resources theme;
    private static NUIFactory factory;
    
    
    public static void install() throws IOException {
        if (theme == null) {
            theme = Resources.openLayered("/nowui.css");
            Hashtable ht = theme.getTheme(theme.getThemeResourceNames()[0]);
            NUIFactory.fixFontSizes(ht);
            UIManager.getInstance().addThemeProps(ht);
            factory = new NUIFactory();
            
        }
    }
    
    public static <T extends Component> T apply(T cmp, NUIFactory.ComponentStyle style) {
        return factory.apply(cmp, style);
    }
    
    public static <T extends Component> T setIcon(T cmp, NUIFactory.ComponentStyle style, char icon) {
        return factory.setIcon(cmp, style, icon);
    }
    
    public static Resources getTheme() {
        return theme;
    }
    
    public static Map createTemplateContext() {
        Map out = new HashMap();
        out.put("res", getTheme());
        out.put("ui", UIManager.getInstance());
        return out;
    }
    
    /**
     * Converts device-independent point (pt) to px.  1pt=1px on medium density.
     * @param pt
     * @return 
     */
    public static int pt2px(int pt) {
        float ratioWidth = 0;
        int multiVal = Display.getInstance().getDeviceDensity();
        switch(multiVal) {
            case com.codename1.ui.Display.DENSITY_MEDIUM:
                //multiVal = com.codename1.ui.Display.DENSITY_MEDIUM;
                ratioWidth = 320;
                break;

            // Generate High Resolution MultiImage
            case com.codename1.ui.Display.DENSITY_HIGH:
                ratioWidth = 480;
                //multiVal = com.codename1.ui.Display.DENSITY_HIGH;
                break;

            // Generate Very High Resolution MultiImage
            case com.codename1.ui.Display.DENSITY_VERY_HIGH:
                ratioWidth = 640;
                //multiVal = com.codename1.ui.Display.DENSITY_VERY_HIGH;
                break;

            // Generate HD Resolution MultiImage
            case com.codename1.ui.Display.DENSITY_HD:
                ratioWidth = 1080;
                //multiVal = com.codename1.ui.Display.DENSITY_HD;
                break;

            // Generate HD560 Resolution MultiImage
            case com.codename1.ui.Display.DENSITY_560:
                ratioWidth = 1500;
                //multiVal = com.codename1.ui.Display.DENSITY_560;
                break;

            // Generate HD2 Resolution MultiImage
            case com.codename1.ui.Display.DENSITY_2HD:
                ratioWidth = 2000;
                //multiVal = com.codename1.ui.Display.DENSITY_2HD;
                break;

            // Generate 4k Resolution MultiImage
            case com.codename1.ui.Display.DENSITY_4K:
                ratioWidth = 2500;
                //multiVal = com.codename1.ui.Display.DENSITY_4K;
                break;
        }
        
        return (int)Math.round(((double)ratioWidth)/320.0);
    }
    
    /**
     * Converts dips to px.
     * @param mm
     * @return 
     */
    public static int mm2px(double mm) {
        Display d = Display.getInstance();
        int i1h = (int)(mm * 100);
        int px1h = d.convertToPixels(i1h, true);
        return (int)(((double)px1h)/100.0);
        
    }
    
    
    public static String formatDateAsAgo(Date date) {
        long diff = System.currentTimeMillis() - date.getTime();
        if (diff < 0) {
            if (diff > -60000) {
                return "in "+(-diff/1000)+" seconds";
            } else if (diff > -3600000) {
                return "in "+(-diff/60000)+" minutes";
            } else if (diff > -3600000 * 24) {
                return "in "+(-diff/3600000)+" hours";
            } else {
                return "in "+(-diff/3600000/24)+" days";
            }
        } else {
            if (diff < 60000) {
                return "in "+(diff/1000)+" seconds";
            } else if (diff < 3600000) {
                return "in "+(diff/60000)+" minutes";
            } else if (diff < 3600000 * 24) {
                return "in "+(diff/3600000)+" hours";
            } else {
                return "in "+(diff/3600000/24)+" days";
            }
        }
    }
    
    
    
    
}
