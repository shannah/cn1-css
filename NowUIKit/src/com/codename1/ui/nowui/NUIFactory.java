/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.codename1.ui.nowui;

import com.codename1.charts.util.ColorUtil;
import com.codename1.ui.Button;
import com.codename1.ui.Component;
import com.codename1.ui.Display;
import com.codename1.ui.Font;
import com.codename1.ui.FontImage;
import com.codename1.ui.Graphics;
import com.codename1.ui.Image;
import com.codename1.ui.Label;
import com.codename1.ui.TextField;
import com.codename1.ui.geom.Dimension;
import com.codename1.ui.geom.Rectangle;
import com.codename1.ui.painter.BackgroundPainter;
import com.codename1.ui.plaf.Border;
import com.codename1.ui.plaf.Style;
import com.codename1.ui.plaf.UIManager;
import com.codename1.util.StringUtil;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

/**
 *
 * @author shannah
 */
public class NUIFactory {
    
    private static Font fontAwesome;
    
    
    public NUIFactory() {
        if (fontAwesome == null) {
            fontAwesome = Font.createTrueTypeFont("FontAwesome", "fontawesome-webfont.ttf");
        }
    }
    
    public static enum ComponentStyle {
        PillButtonFilledLight,
        PillButtonFilledLightLarge,
        PillButtonStrokedLight,
        PillButtonStrokedLightLarge,
        PillButtonFilledDark,
        PillButtonFilledDarkLarge,
        PillButtonStrokedDark,
        PillButtonStrokedDarkLarge,
        PillButtonTransparentLight,
        PillButtonTransparentLightLarge,
        PillButtonTransparentDark,
        PillButtonTransparentDarkLarge,
        CircleButtonFilledLight,
        
        CircleButtonStrokedLight,
        CircleButtonFilledDark,
        CircleButtonStrokedDark,
        CircleButtonTransparentLight,
        CircleButtonTransparentDark,
        
        IconLinkButtonLight,
        IconLinkButtonDark,
        LinkButtonLight,
        LinkButtonDark,
        LinkButtonLightSmall,
        LinkButtonDarkSmall,
        LinkButtonUnderlinedLight,
        LinkButtonUnderlinedDark,
        
        BackgroundDark,
        BackgroundLight,
        
        TextFieldTransparentLight,
        TextFieldTransparentDark,
        CommentsFieldLight,
        CommentsFieldDark,
        
        Tab,
        SideCommand,
        
        HeadingLightLarge,
        HeadingDarkLarge,
        HeadingLightMedium,
        HeadingDarkMedium,
        HeadingLightSmall,
        HeadingDarkSmall,
        SubHeadingLight,
        SubHeadingDark,
        
        TextLightLarge,
        TextDarkLarge,
        TextLightMedium,
        TextDarkMedium,
        TextLightSmall,
        TextDarkSmall;
        
        
        public static ComponentStyle asLight(ComponentStyle style) {
            if (style.name().indexOf("Light") != -1) {
                return style;
            } else if (style.name().indexOf("Dark") != -1) {
                return ComponentStyle.valueOf(StringUtil.replaceAll(style.name(), "Dark", "Light"));
            } else {
                return style;
            }
        }
        
        public ComponentStyle asLight() {
            return asLight(this);
        }
        
        public static ComponentStyle asDark(ComponentStyle style) {
            if (style.name().indexOf("Dark") != -1) {
                return style;
            } else if (style.name().indexOf("Dark") != -1) {
                return ComponentStyle.valueOf(StringUtil.replaceAll(style.name(), "Light", "Dark"));
            } else {
                return style;
            }
        }
        
        public ComponentStyle asDark() {
            return asDark(this);
        }
        
    }
    
    
    
    private static Map<ComponentStyle, Double> componentFontSizes = null;
    
    static Map<ComponentStyle, Double> getComponentFontSizes() {
        if (componentFontSizes == null) {
            componentFontSizes = new HashMap<ComponentStyle, Double>();
            for (ComponentStyle s : ComponentStyle.values()) {
                switch (s) {
                    case CircleButtonFilledDark:
                    case CircleButtonFilledLight:
                    case CircleButtonStrokedDark:
                    case CircleButtonStrokedLight:
                    case CircleButtonTransparentDark:
                    case CircleButtonTransparentLight:
                        componentFontSizes.put(s, 2.8);
                        break;
                    case PillButtonFilledLight:
                    case PillButtonStrokedLight:
                    case PillButtonFilledDark:
                    case PillButtonStrokedDark:
                    case PillButtonTransparentLight:
                    case PillButtonTransparentDark:
                        componentFontSizes.put(s, 1.8);
                        break;
                    case PillButtonFilledLightLarge:
                    case PillButtonStrokedLightLarge:
                    case PillButtonFilledDarkLarge:
                    case PillButtonStrokedDarkLarge:
                    case PillButtonTransparentLightLarge:
                    case PillButtonTransparentDarkLarge:
                        componentFontSizes.put(s, 2.2);
                        break;
                    case TextFieldTransparentDark:
                    case TextFieldTransparentLight:
                    case CommentsFieldDark:
                    case CommentsFieldLight:
                        componentFontSizes.put(s, 2.0);
                        break;
                    case Tab:
                        componentFontSizes.put(s, 1.8);
                        break;
                    case LinkButtonLightSmall: 
                    case LinkButtonDarkSmall:
                        componentFontSizes.put(s, 1.6);
                        break;
                    case LinkButtonLight:
                    case LinkButtonDark:
                    case LinkButtonUnderlinedDark:
                    case LinkButtonUnderlinedLight:
                    case IconLinkButtonDark:
                    case IconLinkButtonLight:
                        componentFontSizes.put(s, 1.8);
                        break;
                        
                    case HeadingDarkLarge:
                    case HeadingLightLarge:
                    case TextLightLarge:
                    case TextDarkLarge:
                        componentFontSizes.put(s, 3.2);
                        break;
                    case HeadingDarkMedium:
                    case HeadingLightMedium:
                    case TextLightMedium:
                    case TextDarkMedium:
                        componentFontSizes.put(s, 2.4);
                        break;
                    case HeadingDarkSmall:
                    case HeadingLightSmall:
                    case TextLightSmall:
                    case TextDarkSmall:
                        componentFontSizes.put(s, 1.8);
                        break;
                        
                    case SideCommand:
                        componentFontSizes.put(s, 2.1);
                        break;
                        
                }
            }
        }
        return componentFontSizes;
    }
    
    public static void fixFontSizes(Hashtable h) {
        Map<ComponentStyle, Double> sizes = getComponentFontSizes();
        for (ComponentStyle style : ComponentStyle.values()) {
            Double size = sizes.get(style);
            if (size != null) {
                String styleName = style.name();
                for (String key : new String[]{styleName+".font", styleName+".sel#font", styleName+".dis#font", styleName+".press#font"}) {
                    Font f = (Font)h.get(key);
                    if (f != null) {
                         f = f.derive(NUI.mm2px(size), f.getStyle());
                         h.put(key, f);
                    }
                }
            }
        }
    }
    
    public <T extends Component> T apply(final T cmp, final ComponentStyle style) {
        cmp.setUIID(style.name());
        //setFont(cmp, style);
        if (style.name().indexOf("Circle") != -1) {
            BackgroundPainter p = new BackgroundPainter(cmp) {

                @Override
                public void paint(Graphics g, Rectangle rect) {
                    boolean antiAliased = g.isAntiAliased();
                    g.setAntiAliased(true);
                    int r = Math.min(rect.getWidth(), rect.getHeight())/2;
                    int x = rect.getX() + rect.getWidth()/2 - r;
                    int y = rect.getY() + rect.getHeight()/2 - r;
                    switch (style) {
                        case CircleButtonStrokedDark:
                        case CircleButtonStrokedLight: {
                            if (cmp.getStyle().getBgTransparency() != 0) {
                                int alpha = cmp.getStyle().getBgTransparency();
                                if (alpha <0) {
                                    alpha = 0xff;
                                }
                                g.setColor(cmp.getStyle().getBgColor());
                                g.setAlpha(alpha);
                                g.fillArc(x, y, 2*r-1, 2*r-1, 0, 360);
                                g.setAlpha(0xff);
                            }
                            g.setColor(cmp.getStyle().getFgColor());
                            
                            g.drawArc(x, y, 2*r-1, 2*r-1, 0, 360);
                            break;
                        }
                        case CircleButtonFilledDark:
                        case CircleButtonFilledLight:
                        case CircleButtonTransparentDark:
                        case CircleButtonTransparentLight: {
                            int alpha = cmp.getStyle().getBgTransparency();
                            if (alpha < 0) {
                                alpha = 0xff;
                            }
                            g.setAlpha(alpha);
                            g.setColor(cmp.getStyle().getBgColor());
                            g.fillArc(x, y, 2*r, 2*r, 0, 360);
                            g.setAlpha(0xff);
                            break;
                        }
                    }
                    
                    g.setAntiAliased(antiAliased);
                }
                
            };
            cmp.getAllStyles().setBgPainter(p);
            
            
        }
        
        switch (style) {
            case IconLinkButtonDark:
            case IconLinkButtonLight:
                if (cmp instanceof Button) {
                    Button btn = (Button)cmp;
                    
                    Font textFont = btn.getUnselectedStyle().getFont();
                    textFont = textFont.derive(NUI.mm2px(1.8), textFont.getStyle());
                    btn.getAllStyles().setFont(textFont);
                    if (btn.getText() != null && btn.getText().length() > 0) {
                        char firstChar = btn.getText().charAt(0);
                        if (firstChar >= 0xf000 && firstChar <= 0xf300) {
                            // The first char is an icon
                            Font f = Font.createTrueTypeFont("FontAwesome", "fontawesome-webfont.ttf");
                            f = f.derive(NUI.mm2px(3.5),f.getStyle());
                            Style s = new Style();
                            s.setFgColor(0xcccccc);
                            s.setBgTransparency(0);
                            FontImage icon = FontImage.create(String.valueOf(firstChar), s, f);
                            btn.setIcon(icon);
                            btn.setText(btn.getText().substring(1));
                            btn.setGap(NUI.mm2px(1.5));
                        }
                    }
                    
                    
                }
                break;
            case TextFieldTransparentDark:
            case TextFieldTransparentLight:

                if (cmp instanceof TextField) {
                    
                    TextField tf = (TextField)cmp;
                    tf.getUnselectedStyle().setBorder(new TextFieldIconBorder(tf, tf.getUnselectedStyle()));
                    tf.getSelectedStyle().setBorder(new TextFieldIconBorder(tf, tf.getSelectedStyle()));
                    tf.getPressedStyle().setBorder(new TextFieldIconBorder(tf, tf.getPressedStyle()));
                    tf.getDisabledStyle().setBorder(new TextFieldIconBorder(tf, tf.getDisabledStyle()));
                }
            break;
                
            
            case CommentsFieldDark:
            case CommentsFieldLight:
                if (cmp instanceof TextField) {
                    
                    TextField tf = (TextField)cmp;
                    tf.getUnselectedStyle().setBorder(new TextFieldIconBorder(tf, tf.getUnselectedStyle(), NUI.mm2px(1), style));
                    tf.getSelectedStyle().setBorder(new TextFieldIconBorder(tf, tf.getSelectedStyle(), NUI.mm2px(1), style));
                    tf.getPressedStyle().setBorder(new TextFieldIconBorder(tf, tf.getPressedStyle(),NUI.mm2px(1), style));
                    tf.getDisabledStyle().setBorder(new TextFieldIconBorder(tf, tf.getDisabledStyle(), NUI.mm2px(1), style));
                }
            break;
            
                
        }
        return cmp;
    }
    
    
    
    
    
    public <T extends Component> T setIcon(T cmp, ComponentStyle style, char charCode) {
        switch (style) {
            case IconLinkButtonDark:
            case IconLinkButtonLight: {
                if (cmp instanceof Label) {
                    Label l = (Label)cmp;
                    Font f = fontAwesome;
                    f = f.derive(NUI.mm2px(3.5),f.getStyle());
                    Style s = new Style();
                    s.setFgColor(0xcccccc);
                    s.setBgTransparency(0);
                    FontImage icon = FontImage.create(String.valueOf(charCode), s, f);
                    l.setIcon(icon);
                    l.setGap(NUI.mm2px(1.5));
                }
                break;
            }
            
            case TextFieldTransparentDark:
            case TextFieldTransparentLight: {
                if (cmp instanceof TextField) {
                    TextField tf = (TextField)cmp;
                    tf.getUnselectedStyle().setBorder(new TextFieldIconBorder(tf, tf.getUnselectedStyle()));
                    tf.getSelectedStyle().setBorder(new TextFieldIconBorder(tf, tf.getSelectedStyle()));
                    tf.getPressedStyle().setBorder(new TextFieldIconBorder(tf, tf.getPressedStyle()));
                    tf.getDisabledStyle().setBorder(new TextFieldIconBorder(tf, tf.getDisabledStyle()));
                    Font f = fontAwesome;
                    f = f.derive(NUI.mm2px(3.5),f.getStyle());
                    Style s = new Style();
                    s.setFgColor(0xffffff);
                    s.setBgTransparency(0);
                    s.setOpacity(88);
                    FontImage icon = FontImage.create(String.valueOf(charCode), s, f);
                    tf.setHintIcon(icon);
                }
                break;
            }
            
            case CommentsFieldDark:
            case CommentsFieldLight: {
                if (cmp instanceof TextField) {
                    TextField tf = (TextField)cmp;
                    System.out.println("CommentsFieldDark");
                    tf.getUnselectedStyle().setBorder(new TextFieldIconBorder(tf, tf.getUnselectedStyle(), NUI.mm2px(1), style));
                    tf.getSelectedStyle().setBorder(new TextFieldIconBorder(tf, tf.getSelectedStyle(), NUI.mm2px(1), style));
                    tf.getPressedStyle().setBorder(new TextFieldIconBorder(tf, tf.getPressedStyle(), NUI.mm2px(1), style));
                    tf.getDisabledStyle().setBorder(new TextFieldIconBorder(tf, tf.getDisabledStyle(), NUI.mm2px(1), style));
                    Font f = fontAwesome;
                    f = f.derive(NUI.mm2px(3.5),f.getStyle());
                    Style s = new Style();
                    if (style.name().indexOf("Light") != -1) {
                        s.setFgColor(0xffffff);
                    } else {
                        s.setFgColor(0x0);
                    }
                    s.setBgTransparency(0);
                    s.setOpacity(88);
                    FontImage icon = FontImage.create(String.valueOf(charCode), s, f);
                    tf.setHintIcon(icon);
                }
                break;
            }
             
                
            case CircleButtonFilledDark:
            case CircleButtonStrokedLight: 
            case CircleButtonFilledLight:
            case CircleButtonStrokedDark:
            
            case CircleButtonTransparentDark:
            case CircleButtonTransparentLight: {
                if (cmp instanceof Label) {
                    Label l = (Label)cmp;
                    
                    Font f = fontAwesome;
                    
                    f = f.derive(NUI.mm2px(3),f.getStyle());
                    Style s = new Style();
                    int color = cmp.getUnselectedStyle().getFgColor();
                    s.setFgColor(color);
                    s.setBgTransparency(0);
                    FontImage icon = FontImage.create(String.valueOf(charCode), s, f);
                    l.setIcon(icon);
                    
                    if (l instanceof Button) {
                        Button btn = (Button)l;
                        s.setFgColor(cmp.getPressedStyle().getFgColor());
                        icon = FontImage.create(String.valueOf(charCode), s, f);
                        btn.setPressedIcon(icon);
                    }
                }
                
                break;
            }
            
            case SideCommand: {
                if (cmp instanceof Button) {
                    Font f = fontAwesome;
                    f = f.derive(NUI.mm2px(3.5), f.getStyle());
                    Button btn = (Button)cmp;
                    btn.setGap(NUI.mm2px(4));
                    Style s = new Style();
                    s.setBgTransparency(0);
                    s.setFgColor(0xffffff);
                    s.setOpacity(88);
                    Image icon = FontImage.create(String.valueOf(charCode), s, f);
                    btn.setIcon(icon);
                }
                break;
            }
                
                
                
        }
        return cmp;
        
    }
    
    private static class TextFieldIconBorder extends Border {
        Border origBorder;
        TextField textField;
        int iconPaddingX;
        ComponentStyle componentStyle;
        Image submitButton;

        TextFieldIconBorder(TextField tf, Style style, int iconPaddingX,  ComponentStyle componentStyle) {
            this.iconPaddingX = iconPaddingX;
            this.componentStyle = componentStyle;
            if (this.componentStyle != null) {
                switch (this.componentStyle) {
                    case CommentsFieldLight:
                        submitButton = UIManager.getInstance().getThemeImageConstant("SubmitIconImage");
                        break;
                    case CommentsFieldDark:
                        submitButton = UIManager.getInstance().getThemeImageConstant("SubmitIconDarkImage");
                        break;
                }
            }
            //style.setPadding(Component.LEFT, iconPaddingX);
            this.textField = tf;
            origBorder = style.getBorder();
            while (origBorder != null && origBorder instanceof TextFieldIconBorder) {
                origBorder = ((TextFieldIconBorder)origBorder).origBorder;
            }
        }
        
        TextFieldIconBorder(TextField tf, Style style) {
            this(tf, style, NUI.mm2px(3.5));
        }
        
        TextFieldIconBorder(TextField tf, Style style, int iconPaddingX) {
            this(tf, style, iconPaddingX, null);
            
        }
        
        @Override
        public boolean isBackgroundPainter() {
            return origBorder.isBackgroundPainter();
        }



        @Override
        public void paint(Graphics g, Component c) {
            origBorder.paint(g, c);


        }

        private void setIconPaddingX(int padding) {
            iconPaddingX = padding;
            //textField.getAllStyles().setPadding(Component.LEFT, iconPaddingX);
        }
        
        @Override
        public void paintBorderBackground(Graphics g, Component c) {
            origBorder.paintBorderBackground(g, c);
            Image hintIcon = textField.getHintIcon();
            if (hintIcon != null && (textField.hasFocus() || textField.getText().length()>0)) {
                g.drawImage(hintIcon, c.getX() + iconPaddingX, c.getY() + c.getHeight()/2 - hintIcon.getHeight()/2);
            }
            if (submitButton != null) {
                g.drawImage(submitButton, c.getX() + c.getWidth() - submitButton.getWidth(), c.getY() + c.getHeight()/2 - submitButton.getHeight()/2);
                
            }
        }
        
        
    }
    
   
}
