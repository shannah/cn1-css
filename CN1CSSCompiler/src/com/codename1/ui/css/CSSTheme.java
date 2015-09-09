/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.codename1.ui.css;

import com.codename1.io.Util;
import com.codename1.ui.Component;
import com.codename1.ui.EditorTTFFont;
import com.codename1.ui.EncodedImage;
import com.codename1.ui.Font;
import com.codename1.ui.Image;
import com.codename1.ui.plaf.Border;
import com.codename1.ui.plaf.Style;
import com.codename1.ui.util.EditableResources;
import java.io.ByteArrayInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.scene.paint.Color;
import javafx.scene.web.WebView;
import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.metadata.IIOMetadata;
import javax.imageio.metadata.IIOMetadataNode;
import javax.imageio.stream.ImageInputStream;
import org.w3c.css.sac.AttributeCondition;
import org.w3c.css.sac.CSSException;
import org.w3c.css.sac.Condition;
import org.w3c.css.sac.ConditionalSelector;
import org.w3c.css.sac.DocumentHandler;
import org.w3c.css.sac.ElementSelector;
import org.w3c.css.sac.InputSource;
import org.w3c.css.sac.LexicalUnit;
import org.w3c.css.sac.Parser;
import org.w3c.css.sac.SACMediaList;
import org.w3c.css.sac.Selector;
import org.w3c.css.sac.SelectorList;
import org.w3c.css.sac.SimpleSelector;
import org.w3c.css.sac.helpers.ParserFactory;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 *
 * @author shannah
 */
public class CSSTheme {
    
    URL baseURL;
    File cssFile = new File("test.css");
    Element anyNodeStyle = new Element();
    Map<String,Element> elements = new HashMap<String,Element>();
    EditableResources res;
    private String themeName = "Theme";
    private List<FontFace> fontFaces = new ArrayList<FontFace>();
    public static final int DEFAULT_TARGET_DENSITY = com.codename1.ui.Display.DENSITY_VERY_HIGH;
    
    private int targetDensity = DEFAULT_TARGET_DENSITY;
    
    
    double px(double val) {
        switch (targetDensity) {
            
            case com.codename1.ui.Display.DENSITY_MEDIUM:
                return val;

            // Generate High Resolution MultiImage
            case com.codename1.ui.Display.DENSITY_HIGH:
                return val*480.0/320.0;

            // Generate Very High Resolution MultiImage
            case com.codename1.ui.Display.DENSITY_VERY_HIGH:
                return val*2.0;

            // Generate HD Resolution MultiImage
            case com.codename1.ui.Display.DENSITY_HD:
                return val*1080.0/320.0;

            // Generate HD560 Resolution MultiImage
            case com.codename1.ui.Display.DENSITY_560:
                return val*1500.0/320.0;

            // Generate HD2 Resolution MultiImage
            case com.codename1.ui.Display.DENSITY_2HD:
                return val*2000.0/320.0;

            // Generate 4k Resolution MultiImage
            case com.codename1.ui.Display.DENSITY_4K:
                return val*2500.0/320.0;
                
        }
        throw new RuntimeException("Unsupported density");
    }
    
    public class FontFace {
        File fontFile;
        LexicalUnit fontFamily;
        LexicalUnit src;
        LexicalUnit fontWeight;
        LexicalUnit fontStretch;
        LexicalUnit fontStyle;
        
        URL getURL() {
            try {
                if (src == null) {
                    return null;
                }

                switch (src.getLexicalUnitType()) {
                    case LexicalUnit.SAC_URI:
                        String url = src.getStringValue();
                        if (url.startsWith("http://") || url.startsWith("https://")) {
                            return new URL(url);
                        } else {
                            return new URL(baseURL, url);
                        }
                }
                return null;
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }
        }
        
        File getFontFile() {
            
            if (fontFile == null) {
                try {
                    URL url = getURL();
                    if (url == null) {
                        return null;
                    }
                    //File cssFile = new File(url.toURI());
                    File parentDir = cssFile.getParentFile();
                    if (url.getProtocol().startsWith("http")) {
                        // If it is remote, check so see if we've already downloaded
                        // the font to the current directory.
                        String fontName = url.getPath();
                        if (fontName.indexOf("/") != -1) {
                            fontName = fontName.substring(fontName.lastIndexOf("/")+1);
                        }
                        File tmpFontFile = new File(parentDir, fontName);
                        if (tmpFontFile.exists()) {
                            fontFile = tmpFontFile;
                        } else {
                            InputStream is = url.openStream();
                            FileOutputStream fos = new FileOutputStream(tmpFontFile);
                            Util.copy(is, fos);
                            Util.cleanup(is);
                            Util.cleanup(fos);
                            fontFile = tmpFontFile;
                        }
                    }
                } catch (Exception ex) {
                    Logger.getLogger(CSSTheme.class.getName()).log(Level.SEVERE, null, ex);
                    throw new RuntimeException(ex);
                }
            }
            return fontFile;
        }
    }
    
    public FontFace createFontFace() {
        FontFace f = new FontFace();
        fontFaces.add(f);
        return f;
    }
    
    private class PixelUnit implements LexicalUnit {

        float val;
        
        PixelUnit(float val) {
            this.val = val;
        }
        
        @Override
        public short getLexicalUnitType() {
            return LexicalUnit.SAC_PIXEL;
        }

        @Override
        public LexicalUnit getNextLexicalUnit() {
            return null;
        }

        @Override
        public LexicalUnit getPreviousLexicalUnit() {
            return null;
        }

        @Override
        public int getIntegerValue() {
            return (int)val;
        }

        @Override
        public float getFloatValue() {
            return val;
        }

        @Override
        public String getDimensionUnitText() {
            return "px";
        }

        @Override
        public String getFunctionName() {
            return null;
        }

        @Override
        public LexicalUnit getParameters() {
            return null;
        }

        @Override
        public String getStringValue() {
            return null;
        }

        @Override
        public LexicalUnit getSubValues() {
            return null;
        }
        
    }
    
    
    String renderAsCSSString(LexicalUnit lu) {
        if (lu == null) {
            return "";
        }
        switch (lu.getLexicalUnitType()) {
            case LexicalUnit.SAC_MILLIMETER:
            case LexicalUnit.SAC_CENTIMETER:
            case LexicalUnit.SAC_PIXEL:
            case LexicalUnit.SAC_POINT:
            
                return px(lu.getFloatValue()) + lu.getDimensionUnitText();
            case LexicalUnit.SAC_PERCENTAGE:
            case LexicalUnit.SAC_DEGREE:
                return lu.getFloatValue() + lu.getDimensionUnitText();
            case LexicalUnit.SAC_URI:
                return "url("+lu.getStringValue()+")";
                
            case LexicalUnit.SAC_FUNCTION: {
                StringBuilder sb = new StringBuilder();
                sb.append(lu.getFunctionName()).append("(");
                LexicalUnit val = lu.getParameters();
                //sb.append(String.valueOf(val));
                boolean empty = true;
                while (val != null) {
                    empty = false;
                    sb.append(renderAsCSSString(val)).append(" ");
                    val = val.getNextLexicalUnit();
                }
                if (!empty) {
                    sb.setLength(sb.length()-1);
                }
                sb.append(")");
                return sb.toString();
            }
            
            case LexicalUnit.SAC_OPERATOR_COMMA:
                return ",";
                
            case LexicalUnit.SAC_OPERATOR_SLASH:
                return "/";
                
            case LexicalUnit.SAC_IDENT:
                return lu.getStringValue();
                
            case LexicalUnit.SAC_STRING_VALUE:
                return lu.getStringValue();
                
            case LexicalUnit.SAC_RGBCOLOR:
                StringBuilder sb = new StringBuilder();
                sb.append("rgb(");
                LexicalUnit val = lu.getParameters();
                while (val != null) {
                    
                    sb.append(renderAsCSSString(val));
                    val = val.getNextLexicalUnit();
                }
                sb.append(")");
                return sb.toString();
            case LexicalUnit.SAC_INTEGER:
                return String.valueOf(lu.getIntegerValue());
            case LexicalUnit.SAC_REAL:
                return String.valueOf(lu.getFloatValue());
                
            
            
                
        }
        throw new RuntimeException("Unsupported lex unit type "+lu.getLexicalUnitType());
        
    }
    
    String renderCSSProperty(String property, Map<String, LexicalUnit> styles) {
        if (property.contains("padding") || property.contains("margin")) {
            return "";
        }
        if (property.startsWith("cn1-")) {
            switch (property) {
                case "cn1-border-bottom-left-radius-x":
                    return "border-radius: "+renderAsCSSString(styles.get("cn1-border-top-left-radius-x")) + " " +
                            renderAsCSSString(styles.get("cn1-border-top-right-radius-x")) + " " +
                            renderAsCSSString(styles.get("cn1-border-bottom-right-radius-x")) + " " +
                            renderAsCSSString(styles.get("cn1-border-bottom-left-radius-x")) + " / " +
                            renderAsCSSString(styles.get("cn1-border-top-left-radius-y")) + " " +
                            renderAsCSSString(styles.get("cn1-border-top-right-radius-y")) + " " +
                            renderAsCSSString(styles.get("cn1-border-bottom-right-radius-y")) + " " +
                            renderAsCSSString(styles.get("cn1-border-bottom-left-radius-y"));
                           
                case "cn1-box-shadow-h" :
                    LexicalUnit h = styles.get("cn1-box-shadow-h");
                    if (h == null) {
                        return "";
                    }
                    if ("none".equals(h.getStringValue())) {
                        return "box-shadow: none";
                    } else {
                        return "box-shadow: " +
                                renderAsCSSString(styles.get("cn1-box-shadow-h")) + " " +
                                renderAsCSSString(styles.get("cn1-box-shadow-v")) + " " +
                                (((h = styles.get("cn1-box-shadow-blur")) != null) ? (renderAsCSSString(h) + " ") : "") +
                                (((h = styles.get("cn1-box-shadow-spread")) != null) ? (renderAsCSSString(h) + " ") : "") +
                                (((h = styles.get("cn1-box-shadow-color")) != null) ? (renderAsCSSString(h) + " ") : "") +
                                (((h = styles.get("cn1-box-shadow-inset")) != null && "inset".equals(h.getStringValue())) ? (renderAsCSSString(h) + " ") : "");
                                
                                
                    }
            }
            return "";
        } else {
            
            switch (property) {
                case "width": {
                    LexicalUnit value = styles.get(property);
                    switch (value.getLexicalUnitType()) {
                        case LexicalUnit.SAC_PERCENTAGE:
                            return property + ":"+ px((int)(value.getFloatValue() / 100f * 320f)) + "px";
                    }
                }
                case "height": {
                    LexicalUnit value = styles.get(property);
                    switch (value.getLexicalUnitType()) {
                        case LexicalUnit.SAC_PERCENTAGE:
                            return property + ":"+ px((int)(value.getFloatValue() / 100f * 480f)) + "px";
                    }
                }
            }
            
            return property + ":"+renderAsCSSString(styles.get(property));
        }
    }
    
    public String getHtmlPreview() {
        StringBuilder sb = new StringBuilder();
        sb.append("<!doctype html>\n<html><body>");
        for (String name : elements.keySet()) {
            Element el = (Element)elements.get(name);
            sb.append("<h1>").append(name).append("</h1>")
                    .append(el.getHtmlPreview())
                    .append("<h2>::Unselected</h2>")
                    .append(el.getUnselected().getHtmlPreview())
                    .append("<h2>::Selected</h2>")
                    .append(el.getSelected().getHtmlPreview())
                    .append("<h2>::Pressed</h2>")
                    .append(el.getPressed().getHtmlPreview())
                    .append("<h2>::Disabled</h2>")
                    .append(el.getDisabled().getHtmlPreview())
                    .append("<hr/>");
        }
        sb.append("</body></html>");
        return sb.toString();
    }
    
    public String generateCaptureHtml() {
        StringBuilder sb = new StringBuilder();
        sb.append("<!doctype html>\n<html><base href=\""+baseURL.toExternalForm()+"\"/> <head><style type=\"text/css\">body {padding:0; margin:0} div.element {margin: 0 !important; padding: 0 !important; box-sizing:border-box;}</style></head><body>");
        for (String name : elements.keySet()) {
            Element el = (Element)elements.get(name);
            sb.append(el.getUnselected().getEmptyHtmlWithId(name))
                    .append(el.getSelected().getEmptyHtmlWithId(name+".sel"))
                    .append(el.getPressed().getEmptyHtmlWithId(name+".press"))
                    .append(el.getDisabled().getEmptyHtmlWithId(name+".dis"));
                    
        }
        sb.append("</body></html>");
        return sb.toString();
    }
    
    public void updateResources() {
        for (String id : elements.keySet()) {
            Element el = elements.get(id);
            Map<String,LexicalUnit> unselectedStyles = el.getUnselected().getFlattenedStyle();
            Map<String,LexicalUnit> selectedStyles = el.getSelected().getFlattenedStyle();
            Map<String,LexicalUnit> pressedStyles  = el.getPressed().getFlattenedStyle();
            Map<String,LexicalUnit> disabledStyles = el.getDisabled().getFlattenedStyle();
            
            Element selected = el.getSelected();
            String selId = id+".sel";
            String unselId = id;
            String pressedId = id+".press";
            String disabledId = id+".dis";
            
            
            res.setThemeProperty(themeName, unselId+".padding", el.getThemePadding(unselectedStyles));
            res.setThemeProperty(themeName, unselId+".padUnit", el.getThemePaddingUnit(unselectedStyles));
            res.setThemeProperty(themeName, selId+"#padding", el.getThemePadding(selectedStyles));
            res.setThemeProperty(themeName, selId+"#padUnit", el.getThemePaddingUnit(selectedStyles));
            res.setThemeProperty(themeName, pressedId+"#padding", el.getThemePadding(pressedStyles));
            res.setThemeProperty(themeName, pressedId+"#padUnit", el.getThemePaddingUnit(pressedStyles));
            res.setThemeProperty(themeName, disabledId+"#padding", el.getThemePadding(disabledStyles));
            res.setThemeProperty(themeName, disabledId+"#padUnit", el.getThemePaddingUnit(disabledStyles));
            
            res.setThemeProperty(themeName, unselId+".margin", el.getThemeMargin(unselectedStyles));
            res.setThemeProperty(themeName, unselId+".marUnit", el.getThemeMarginUnit(unselectedStyles));
            res.setThemeProperty(themeName, selId+"#margin", el.getThemeMargin(selectedStyles));
            res.setThemeProperty(themeName, selId+"#marUnit", el.getThemeMarginUnit(selectedStyles));
            res.setThemeProperty(themeName, pressedId+"#margin", el.getThemeMargin(pressedStyles));
            res.setThemeProperty(themeName, pressedId+"#marUnit", el.getThemeMarginUnit(pressedStyles));
            res.setThemeProperty(themeName, disabledId+"#margin", el.getThemeMargin(disabledStyles));
            res.setThemeProperty(themeName, disabledId+"#marUnit", el.getThemeMarginUnit(disabledStyles));
            
            
            res.setThemeProperty(themeName, unselId+".fgColor", el.getThemeFgColor(unselectedStyles));
            res.setThemeProperty(themeName, selId+"#fgColor", el.getThemeFgColor(selectedStyles));
            res.setThemeProperty(themeName, pressedId+"#fgColor", el.getThemeFgColor(pressedStyles));
            res.setThemeProperty(themeName, disabledId+"#fgColor", el.getThemeFgColor(disabledStyles));
            res.setThemeProperty(themeName, unselId+".bgColor", el.getThemeBgColor(unselectedStyles));
            res.setThemeProperty(themeName, selId+"#bgColor", el.getThemeBgColor(selectedStyles));
            res.setThemeProperty(themeName, pressedId+"#bgColor", el.getThemeBgColor(pressedStyles));
            res.setThemeProperty(themeName, disabledId+"#bgColor", el.getThemeBgColor(disabledStyles));
            
            res.setThemeProperty(themeName, unselId+".transparency", el.getThemeTransparency(unselectedStyles));
            res.setThemeProperty(themeName, selId+"#transparency", el.getThemeTransparency(selectedStyles));
            res.setThemeProperty(themeName, pressedId+"#transparency", el.getThemeTransparency(pressedStyles));
            res.setThemeProperty(themeName, disabledId+"#transparency", el.getThemeTransparency(disabledStyles));
            
            res.setThemeProperty(themeName, unselId+".align", el.getThemeAlignment(unselectedStyles));
            res.setThemeProperty(themeName, selId+"#align", el.getThemeAlignment(selectedStyles));
            res.setThemeProperty(themeName, pressedId+"#align", el.getThemeAlignment(pressedStyles));
            res.setThemeProperty(themeName, disabledId+"#align", el.getThemeAlignment(disabledStyles));
            
            res.setThemeProperty(themeName, unselId+".font", el.getThemeFont(unselectedStyles));
            res.setThemeProperty(themeName, selId+"#font", el.getThemeFont(selectedStyles));
            res.setThemeProperty(themeName, pressedId+"#font", el.getThemeFont(pressedStyles));
            res.setThemeProperty(themeName, disabledId+"#font", el.getThemeFont(disabledStyles));
            
            res.setThemeProperty(themeName, unselId+".textDecoration", el.getThemeTextDecoration(unselectedStyles));
            res.setThemeProperty(themeName, selId+"#textDecoration", el.getThemeTextDecoration(selectedStyles));
            res.setThemeProperty(themeName, pressedId+"#textDecoration", el.getThemeTextDecoration(pressedStyles));
            res.setThemeProperty(themeName, disabledId+"#textDecoration", el.getThemeTextDecoration(disabledStyles));
            
            res.setThemeProperty(themeName, unselId+".bgType", el.getThemeBgType(unselectedStyles));
            res.setThemeProperty(themeName, selId+"#bgType", el.getThemeBgType(selectedStyles));
            res.setThemeProperty(themeName, pressedId+"#bgType", el.getThemeBgType(pressedStyles));
            res.setThemeProperty(themeName, disabledId+"#bgType", el.getThemeBgType(disabledStyles));
            
            res.setThemeProperty(themeName, unselId+".derive", el.getThemeDerive(unselectedStyles, ""));
            res.setThemeProperty(themeName, selId+"#derive", el.getThemeDerive(selectedStyles, ".sel"));
            res.setThemeProperty(themeName, pressedId+"#derive", el.getThemeDerive(pressedStyles, ".press"));
            res.setThemeProperty(themeName, disabledId+"#derive", el.getThemeDerive(disabledStyles, ".dis"));
            
           System.out.println("Checking if background image is here for "+unselectedStyles);
           if (el.hasBackgroundImage(unselectedStyles) && !el.requiresBackgroundImageGeneration(unselectedStyles) && !el.requiresImageBorder(unselectedStyles)) {
               System.out.println("Getting background image... it is here"); 
               Image imageId = getBackgroundImage(unselectedStyles);
                if (imageId != null) {
                    
                    res.setThemeProperty(themeName, unselId+".bgImage", imageId);
                }
            }
            if (el.hasBackgroundImage(selectedStyles) && !el.requiresBackgroundImageGeneration(selectedStyles) && !el.requiresImageBorder(selectedStyles)) {
                Image imageId = getBackgroundImage(selectedStyles);
                if (imageId != null) {
                    
                    res.setThemeProperty(themeName, selId+"#bgImage", imageId);
                }
            }
            
            if (el.hasBackgroundImage(pressedStyles) && !el.requiresBackgroundImageGeneration(pressedStyles) && !el.requiresImageBorder(pressedStyles)) {
                Image imageId = getBackgroundImage(pressedStyles);
                if (imageId != null) {
                    
                    res.setThemeProperty(themeName, pressedId+"#bgImage", imageId);
                }
            }
            if (el.hasBackgroundImage(disabledStyles) && !el.requiresBackgroundImageGeneration(disabledStyles) && !el.requiresImageBorder(disabledStyles)) {
                Image imageId = getBackgroundImage(disabledStyles);
                if (imageId != null) {
                    
                    res.setThemeProperty(themeName, disabledId+"#bgImage", imageId);
                }
            }
            
            if (!el.requiresImageBorder(unselectedStyles) && !el.requiresBackgroundImageGeneration(unselectedStyles)) {
                res.setThemeProperty(themeName, unselId+".border", el.getThemeBorder(unselectedStyles));
            }
            if (!el.requiresImageBorder(selectedStyles) && !el.requiresBackgroundImageGeneration(selectedStyles)) {
                res.setThemeProperty(themeName, selId+"#border", el.getThemeBorder(selectedStyles));
            }
            if (!el.requiresImageBorder(pressedStyles) && !el.requiresBackgroundImageGeneration(pressedStyles)) {
                res.setThemeProperty(themeName, pressedId+"#border", el.getThemeBorder(pressedStyles));
            }
            if (!el.requiresImageBorder(disabledStyles) && !el.requiresBackgroundImageGeneration(disabledStyles)) {
                res.setThemeProperty(themeName, disabledId+"#border", el.getThemeBorder(disabledStyles));
            }
            
            
        }
    }
    
    private Map<String,Image> loadedImages = new HashMap<String,Image>();
    
    
    public int[] getDpi(EncodedImage im) throws IOException {
        ImageInputStream iis = ImageIO.createImageInputStream(new ByteArrayInputStream(im.getImageData()));
        Iterator it = ImageIO.getImageReaders(iis);
        if (!it.hasNext()) {
            System.err.println("No reader for this format");
            return null;
        }
        ImageReader reader = (ImageReader) it.next();
        reader.setInput(iis);

        IIOMetadata meta = reader.getImageMetadata(0);
        IIOMetadataNode root = (IIOMetadataNode) meta.getAsTree("javax_imageio_1.0");
        NodeList nodes = root.getElementsByTagName("HorizontalPixelSize");
        int xDPI = -1;
        int yDPI = -1;
        if (nodes.getLength() > 0) {
            IIOMetadataNode dpcWidth = (IIOMetadataNode) nodes.item(0);
            NamedNodeMap nnm = dpcWidth.getAttributes();
            Node item = nnm.item(0);
            xDPI = Math.round(25.4f / Float.parseFloat(item.getNodeValue()));
            System.out.println("xDPI: " + xDPI);
        } else {
            System.out.println("xDPI: -");
        }
        if (nodes.getLength() > 0) {
            nodes = root.getElementsByTagName("VerticalPixelSize");
            IIOMetadataNode dpcHeight = (IIOMetadataNode) nodes.item(0);
            NamedNodeMap nnm = dpcHeight.getAttributes();
            Node item = nnm.item(0);
            yDPI = Math.round(25.4f / Float.parseFloat(item.getNodeValue()));
            System.out.println("yDPI: " + yDPI);
        } else {
            System.out.println("yDPI: -");
        }

        return new int[]{xDPI, yDPI};
    }
    
    public int getImageDensity(EncodedImage im) {
        try {
            int[] dpis = getDpi(im);
            int maxDpi = 72;
            if (dpis != null && dpis[0] > 0 && dpis[0] > maxDpi) {
                maxDpi = dpis[0];
            }
            if (dpis != null && dpis[1] > 0 && dpis[1] > maxDpi) {
                maxDpi = dpis[1];
            }
            
            if (maxDpi <= 72) {
                return com.codename1.ui.Display.DENSITY_MEDIUM;
            } else if (maxDpi <= 144) {
                return com.codename1.ui.Display.DENSITY_VERY_HIGH;
            } else if (maxDpi <= 200) {
                return com.codename1.ui.Display.DENSITY_560;
            } else if (maxDpi <= 300) {
                return com.codename1.ui.Display.DENSITY_HD;
            } else {
                return com.codename1.ui.Display.DENSITY_2HD;
            }
            
        } catch (Exception ex) {
            return com.codename1.ui.Display.DENSITY_MEDIUM;
        }
    }
    
    
    public Image getBackgroundImage(Map<String,LexicalUnit> styles)  {
        try {
            LexicalUnit bgImage = styles.get("background-image");
            if (bgImage == null) {
                return null;
            }
            String url = bgImage.getStringValue();
            String fileName = url;
            if (fileName.indexOf("/") != -1) {
                fileName = fileName.substring(fileName.lastIndexOf("/")+1);
            }
            
            if (loadedImages.containsKey(url)) {
                return loadedImages.get(url);
            }
            
            LexicalUnit imageId = styles.get("cn1-image-id");
            String imageIdStr = url;
            if (imageId != null) {
                imageIdStr = imageId.getStringValue();
            }
            
            URL imgURL = null;
            if (url.startsWith("http://") || url.startsWith("https://")) {
                imgURL = new URL(url);
                
            } else {
                imgURL = new URL(baseURL, url);
            }
            
            
            InputStream is = imgURL.openStream();
            EncodedImage encImg = EncodedImage.create(is);
            is.close();
            
            
            ResourcesMutator resm = new ResourcesMutator(res);
            resm.targetDensity = getImageDensity(encImg);
            System.out.println("Loading image from "+url+" with density "+resm.targetDensity);
            Image im = resm.storeImage(encImg, imageIdStr, false);
            //System.out.println("Storing image "+url+" at id "+imageIdStr);
            loadedImages.put(url, im);
            
            
            return im;
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }
    
    public void createImageBorders(WebView web) {
        if (res == null) {
            res = new EditableResources();
        }
        ArrayList<Border> borders = new ArrayList<Border>();
        
        ResourcesMutator resm = new ResourcesMutator(res);
        resm.targetDensity = targetDensity;
        
        List<Runnable> onComplete = new ArrayList<Runnable>();
        for (String id : elements.keySet()) {
            Element e = (Element)elements.get(id);
            
            Element unselected = e.getUnselected();
            Map<String,LexicalUnit> unselectedStyles = (Map<String,LexicalUnit>)unselected.getFlattenedStyle();
            Border b = unselected.createBorder(unselectedStyles);
            Border unselectedBorder = b;
            if (e.requiresImageBorder(unselectedStyles)) {
                if (!borders.contains(b)) {
                    borders.add(b);
                    resm.addImageProcessor(id, (img) -> {
                        
                        Insets insets = unselected.getImageBorderInsets(unselectedStyles, img.getWidth(), img.getHeight());
                        System.out.println("Creating 9 piece for image "+img.getWidth()+", "+img.getHeight()+" with insets "+insets.top+", "+insets.right+","+insets.bottom+","+insets.left);
                        com.codename1.ui.plaf.Border border = resm.create9PieceBorder(img, id, insets.top, insets.right, insets.bottom, insets.left);
                        resm.put(id+".border", border);
                        unselectedBorder.border = border;
                    });
                } else {
                    onComplete.add(()->{
                        resm.put(id+".border", borders.get(borders.indexOf(unselectedBorder)).border);
                    });
                    
                }
            } else if (e.requiresBackgroundImageGeneration(unselectedStyles)) {
                if (!borders.contains(b)) {
                    borders.add(b);
                    resm.addImageProcessor(id, (img) -> {
                        int i = 1;
                        while(res.containsResource(id + "_" + i + ".png")) {
                            i++;
                        }
                        String prefix = id + "_" + i + ".png";
                        System.out.println("Generating image "+prefix);
                        Image im = resm.storeImage(EncodedImage.create(ResourcesMutator.toPng(img)), prefix, false);
                        unselectedBorder.image = im;
                        resm.put(id+".bgImage", im);
                        //resm.put(id+".press#bgType", Style.B)
                    });
                } else {
                    onComplete.add(()->{
                        resm.put(id+".bgImage", unselectedBorder.image);
                    });
                }
            }
            
            Element selected = e.getSelected();
            Map<String,LexicalUnit> selectedStyles = (Map<String,LexicalUnit>)selected.getFlattenedStyle();
            b = selected.createBorder(selectedStyles);
            Border selectedBorder = b;
            if (e.requiresImageBorder(selectedStyles)) {
                if (!borders.contains(b)) {
                    borders.add(b);
                    resm.addImageProcessor(id+".sel", (img) -> {
                        Insets insets = selected.getImageBorderInsets(selectedStyles, img.getWidth(), img.getHeight());
            
                        com.codename1.ui.plaf.Border border = resm.create9PieceBorder(img, id, insets.top, insets.right, insets.bottom, insets.left);
                        resm.put(id+".sel#border", border);
                        selectedBorder.border = border;
                    });
                } else {
                    onComplete.add(()-> {
                        resm.put(id+".sel#border", borders.get(borders.indexOf(selectedBorder)).border);
                    });
                    
                }
            } else if (e.requiresBackgroundImageGeneration(selectedStyles)) {
                if (!borders.contains(b)) {
                    borders.add(b);
                    resm.addImageProcessor(id+".sel", (img) -> {
                        int i = 1;
                        while(res.containsResource(id + "_" + i + ".png")) {
                            i++;
                        }
                        String prefix = id + "_" + i + ".png";
                        System.out.println("Generating image "+prefix);
                        Image im = resm.storeImage(EncodedImage.create(ResourcesMutator.toPng(img)), prefix, false);
                        selectedBorder.image = im;
                        resm.put(id+".sel#bgImage", im);
                        //resm.put(id+".press#bgType", Style.B)
                    });
                } else {
                    onComplete.add(()->{
                        resm.put(id+".sel#bgImage", selectedBorder.image);
                    });
                }
            }
            
            Element pressed = e.getPressed();
            Map<String,LexicalUnit> pressedStyles = (Map<String,LexicalUnit>)pressed.getFlattenedStyle();
            System.out.println("Pressed styles "+pressedStyles);
            b = pressed.createBorder(pressedStyles);
            Border pressedBorder = b;
            if (e.requiresImageBorder(pressedStyles)) {
                if (!borders.contains(b)) {
                    borders.add(b);
                    resm.addImageProcessor(id+".press", (img) -> {
                        Insets insets = pressed.getImageBorderInsets(pressedStyles, img.getWidth(), img.getHeight());
                        System.out.println("Getting pressed images with insets "+insets);
                        com.codename1.ui.plaf.Border border = resm.create9PieceBorder(img, id, insets.top, insets.right, insets.bottom, insets.left);
                        
                        resm.put(id+".press#border", border);
                        pressedBorder.border = border;
                    });
                } else {
                    onComplete.add(()-> {
                        resm.put(id+".press#border", borders.get(borders.indexOf(pressedBorder)).border);
                    });
                    
                }
            } else if (e.requiresBackgroundImageGeneration(pressedStyles)) {
                if (!borders.contains(b)) {
                    borders.add(b);
                    resm.addImageProcessor(id+".press", (img) -> {
                        int i = 1;
                        while(res.containsResource(id + "_" + i + ".png")) {
                            i++;
                        }
                        String prefix = id + "_" + i + ".png";
                        Image im = resm.storeImage(EncodedImage.create(ResourcesMutator.toPng(img)), prefix, false);
                        pressedBorder.imageId = prefix;
                        resm.put(id+".press#bgImage", im/*res.findId(im, true)*/);
                        //resm.put(id+".press#bgType", Style.B)
                    });
                } else {
                    onComplete.add(()->{
                        resm.put(id+".press#bgImage", res.findId(pressedBorder.imageId, true));
                    });
                }
            }
            
            Element disabled = e.getDisabled();
            Map<String,LexicalUnit> disabledStyles = (Map<String,LexicalUnit>)disabled.getFlattenedStyle();
            System.out.println(id+" disabled "+disabledStyles);
            b = disabled.createBorder(disabledStyles);
            Border disabledBorder = b;
            if (e.requiresImageBorder(disabledStyles)) {
                if (!borders.contains(b)) {
                    
                    borders.add(b);
                    resm.addImageProcessor(id+".dis", (img) -> {
                        Insets disabledInsets = disabled.getImageBorderInsets(disabledStyles, img.getWidth(), img.getHeight());
            
                        com.codename1.ui.plaf.Border border = resm.create9PieceBorder(img, id, disabledInsets.top, disabledInsets.right, disabledInsets.bottom, disabledInsets.left);
                        disabledBorder.border = border;
                        resm.put(id+".dis#border", border);
                    });
                } else {
                    onComplete.add(()-> {
                        resm.put(id+".dis#border", borders.get(borders.indexOf(disabledBorder)).border);
                    });
                    
                }
            } else if (e.requiresBackgroundImageGeneration(pressedStyles)) {
                if (!borders.contains(b)) {
                    borders.add(b);
                    resm.addImageProcessor(id+".dis", (img) -> {
                        int i = 1;
                        while(res.containsResource(id + "_" + i + ".png")) {
                            i++;
                        }
                        String prefix = id + "_" + i + ".png";
                        Image im = resm.storeImage(EncodedImage.create(ResourcesMutator.toPng(img)), prefix, false);
                        disabledBorder.image = im;
                        resm.put(id+".dis#bgImage", im);
                        //resm.put(id+".press#bgType", Style.B)
                    });
                } else {
                    onComplete.add(()->{
                        resm.put(id+".dis#bgImage", disabledBorder.image);
                    });
                }
            }
            
        }
        System.out.println(generateCaptureHtml());
        resm.createScreenshots(web, generateCaptureHtml(), this.baseURL.toExternalForm());
        for (Runnable r : onComplete) {
            r.run();
        }
        System.out.println(res.getTheme("Theme"));
    }
    
    public void save(File outputFile) throws IOException {
         DataOutputStream resFile = new DataOutputStream(new FileOutputStream(outputFile));
         res.save(resFile);
         resFile.close();
    }
    
    
    
    private class Border {
        com.codename1.ui.plaf.Border border;
        String imageId;
        Image image;
        
        String thicknessTop,
                thicknessRight,
                thicknessBottom,
                thicknessLeft,
                styleTop,
                styleRight,
                styleBottom,
                styleLeft;
                
        
        String backgroundImageUrl,
                backgroundRepeat,
                borderRadius,
                boxShadow,
                gradient;
        
        String backgroundColor,
                borderColorTop,
                borderColorRight,
                borderColorBottom,
                borderColorLeft;
                
        
        public boolean equals(Object o) {
            Border b = (Border)o;
            return eq(borderRadius, b.borderRadius)
                    && eq(thicknessTop, b.thicknessTop)
                    && eq(thicknessRight, b.thicknessRight)
                    && eq(thicknessBottom, b.thicknessBottom)
                    && eq(thicknessLeft, b.thicknessLeft)
                    && eq(styleTop, b.styleTop)
                    && eq(styleRight, b.styleRight)
                    && eq(styleBottom, b.styleBottom)
                    && eq(styleLeft, b.styleLeft)
                    && eq(gradient, b.gradient)
                    && eq(boxShadow, b.boxShadow)
                    && (backgroundImageUrl == null ? b.backgroundImageUrl == null : backgroundImageUrl.equals(b.backgroundImageUrl))
                    && (backgroundRepeat == null ? b.backgroundRepeat == null : backgroundRepeat.equals(b.backgroundRepeat))
                    && eq(backgroundColor, b.backgroundColor)
                    && eq(borderColorTop, b.borderColorTop)
                    && eq(borderColorRight, b.borderColorRight)
                    && eq(borderColorBottom, b.borderColorBottom)
                    && eq(borderColorLeft, b.borderColorLeft);
            
        }
        /*
        public boolean requiresImageBorder() {
            return !eq(borderColorTop, borderColorRight)
                    || !eq(borderColorTop, borderColorBottom)
                    || !eq(borderColorTop, borderColorLeft)
                    || !isNone(gradient)
                    || !isNone(boxShadow)
                    || !isZero(borderRadius)
                    || !eq(thicknessTop, thicknessRight)
                    || !eq(thicknessTop, thicknessBottom)
                    || !eq(thicknessTop, thicknessLeft)
                    || !eq(styleTop, styleRight)
                    || !eq(styleTop, styleBottom)
                    || !eq(styleTop, styleLeft);
        }
        */
        public String toString() {
            return borderColorTop + " " +borderColorRight + borderColorBottom + borderColorLeft +
                    gradient + boxShadow + borderRadius + thicknessTop + thicknessRight + thicknessBottom + thicknessLeft
                    + styleTop + styleRight + styleBottom + styleLeft;
        }
        
        public boolean hasBorderRadius() {
            return !isZero(borderRadius);
        }
        
        public boolean hasGradient() {
            return !isNone(gradient);
        }
        
        public boolean hasUnequalBorders() {
            return !eq(thicknessTop, thicknessRight)
                    || !eq(thicknessTop, thicknessBottom)
                    || !eq(thicknessTop, thicknessLeft)
                    || !eq(styleTop, styleRight)
                    || !eq(styleTop, styleBottom)
                    || !eq(styleTop, styleLeft)
                    || !eq(borderColorTop, borderColorRight)
                    || !eq(borderColorTop, borderColorBottom)
                    || !eq(borderColorTop, borderColorLeft);
        }
        
        public boolean hasBoxShadow() {
            return !isNone(boxShadow);
        }
    }
    
    
    
    private static boolean eq(Object o1, Object o2) {
        return o1 == null ? o1 == null : o1.equals(o2);
    }
    
    private static boolean isNone(Object o) {
        return o == null || "".equals(o) || "none".equals(o);
    }
    
    private static boolean isZero(String o) {
        if (o == null || "".equals(o)) {
            return true;
        }
        
        o = o.replaceAll("[^0-9]", "");
        return o.matches("^0*$");
    }
    
    public class Element {
        Element parent = anyNodeStyle;
        Map properties = new HashMap();
        
        Element unselected;
        Element selected;
        Element pressed;
        Element disabled;
        
        String generateStyleCSS() {
            StringBuilder sb = new StringBuilder();
            Map styles = new HashMap();
            styles.putAll(getFlattenedStyle());
            
            if (this.requiresImageBorder(styles)) {
                if (styles.get("min-height") != null) {
                    styles.put("height", styles.get("min-height"));
                }
                if (styles.get("min-width") != null) {
                    styles.put("width", styles.get("min-width"));
                }
            }
            
            if (styles.get("height") == null) {
                styles.put("height", new PixelUnit(100));
            }
            for (Object key : styles.keySet()) {
                String property = (String)key;
                LexicalUnit value = (LexicalUnit)styles.get(key);
                String prop = renderCSSProperty(property, styles);
                if (!prop.isEmpty()) {
                    sb.append(prop).append(";");
                }
                
            }
            //sb.append("border-top-right-radius: 10px / 20px;");
            return sb.toString();
        }
        
        void setParent(String name) {
            Element parentEl = getElementByName(name);
            Element self = this;
            if (this.isSelectedStyle() || this.isDisabledStyle() || this.isDisabledStyle() || this.isUnselectedStyle()) {
                self = this.parent;
            }
            System.out.println("Setting parent of "+self+" to "+name);
            self.parent = parentEl;
        }
        
        String getHtmlPreview() {
            StringBuilder sb = new StringBuilder();
            sb.append("<div style=\"").append(generateStyleCSS()).append("\">Lorem Ipsum</div>");
            return sb.toString();
        }
        
        public String getEmptyHtmlWithId(String id) {
            StringBuilder sb = new StringBuilder();
            sb.append("<div id=\""+id+"\" class=\"element\" style=\"").append(generateStyleCSS()).append("\"></div>");
            return sb.toString();
        }
        
        Map getFlattenedSelectedStyle() {
            Map out = new HashMap();
            
            LinkedList<Map> stack = new LinkedList<Map>();
            Element el = this;
            if (!el.isSelectedStyle()) {
                el = el.getSelected();
            }
            while (el != null) {
                stack.push(el.style);
                
                el = el.parent.parent;
                if (el != null) {
                    el = el.getSelected();
                }
            }
            
            while (!stack.isEmpty()) {
                out.putAll(stack.pop());
            }
            return out;
        }
        
        Map getFlattenedPressedStyle() {
            Map out = new HashMap();
            
            LinkedList<Map> stack = new LinkedList<Map>();
            Element el = this;
            if (!el.isPressedStyle()) {
                el = el.getPressed();
            }
            while (el != null) {
                stack.push(el.style);
                
                el = el.parent.parent;
                if (el != null) {
                    el = el.getPressed();
                }
            }
            
            while (!stack.isEmpty()) {
                out.putAll(stack.pop());
            }
            return out;
        }
        
        Map getFlattenedUnselectedStyle() {
            Map out = new HashMap();
            
            LinkedList<Map> stack = new LinkedList<Map>();
            Element el = this;
            if (!el.isUnselectedStyle()) {
                el = el.getUnselected();
            }
            while (el != null) {
                stack.push(el.style);
                
                el = el.parent.parent;
                if (el != null) {
                    el = el.getUnselected();
                }
            }
            
            while (!stack.isEmpty()) {
                out.putAll(stack.pop());
            }
            return out;
        }
        
        Map getFlattenedDisabledStyle() {
            Map out = new HashMap();
            
            LinkedList<Map> stack = new LinkedList<Map>();
            Element el = this;
            if (!el.isDisabledStyle()) {
                el = el.getDisabled();
            }
            while (el != null) {
                stack.push(el.style);
                
                el = el.parent.parent;
                if (el != null) {
                    el = el.getDisabled();
                }
            }
            
            while (!stack.isEmpty()) {
                out.putAll(stack.pop());
            }
            return out;
        }
        
        Map getFlattenedStyle() {
            Map out = new HashMap();
            if (this.isSelectedStyle()) {
                if (parent != null) {
                    out.putAll(parent.getFlattenedStyle());
                }
                out.putAll(getFlattenedSelectedStyle());
                
            } else if (this.isUnselectedStyle()) {
                if (parent != null) {
                    out.putAll(parent.getFlattenedStyle());
                }
                out.putAll(getFlattenedUnselectedStyle());
            } else if (this.isDisabledStyle()) {
                if (parent != null) {
                    out.putAll(parent.getFlattenedStyle());
                }
                out.putAll(getFlattenedDisabledStyle());
            } else if (this.isPressedStyle()) {
                if (parent != null) {
                    out.putAll(parent.getFlattenedStyle());
                }
                out.putAll(getFlattenedPressedStyle());
            } else {
                if (parent != null) {
                    out.putAll(parent.getFlattenedStyle());
                }
                out.putAll(style);
            }
            return Collections.unmodifiableMap(out);
        }
        
        
        
        Element getUnselected() {
            if (unselected == null) {
                unselected = new Element();
                unselected.parent = this;
            }
            return unselected;
        }
        
        Element getSelected() {
            if (selected == null) {
                selected = new Element();
                selected.parent = this;
                //selected.style.putAll(getUnmodifiableStyle());
                
            }
            return selected;
        }
        
        
        
        Element getPressed() {
            if (pressed == null) {
                pressed = new Element();
                pressed.parent = this;
                //pressed.style.putAll(getUnmodifiableStyle());
                
            }
            return pressed;
        }
        
        Element getDisabled() {
            if (disabled == null) {
                disabled = new Element();
                disabled.parent = this;
                //disabled.style.putAll(style);
            }
            return disabled;
        }
        
        void put(String key, Object value) {
            style.put(key, value);
        }
        
        Object get(String key) {
            Object res = style.get(key);
            if (res == null || parent != null) {
                return parent.get(key);
            }
            return null;
        }
        
        Object getUnselectedValue(String key) {
            return getUnselectedValue(key, false);
        }
        
        Object getUnselectedValue(String key, boolean includeDefault) {
            if (isUnselectedStyle()) {
                Object res = style.get(key);
                if (res != null) {
                    return res;
                }
            }
            if (unselected != null) {
                Object res = unselected.style.get(key);
                if (res != null) {
                    return res;
                }
            }
            if (parent != null) {
                return parent.getUnselectedValue(key);
            }
            if (includeDefault) {
                return get(key);
            }
            return null;
        }
        
        
        Object getSelectedValue(String key) {
            return getSelectedValue(key, false);
        }
        
        Object getSelectedValue(String key, boolean includeDefault) {
            if (isSelectedStyle()) {
                Object res = style.get(key);
                if (res != null) {
                    return res;
                }
            }
            if (selected != null) {
                Object res = selected.style.get(key);
                if (res != null) {
                    return res;
                }
            }
            if (parent != null) {
                return parent.getSelectedValue(key);
            }
            if (includeDefault) {
                return get(key);
            }
            return null;
        }
        
        Object getPressedValue(String key) {
            return getPressedValue(key, false);
        }
        
        /**
         * Gets the pressed value for a property.  Will check the parent's 
         * disabled value if this element has none for the specified prop.
         * 
         * @param key
         * @return 
         */
        Object getPressedValue(String key, boolean includeDefault) {
            if (isPressedStyle()) {
                Object res = style.get(key);
                if (res != null) {
                    return res;
                }
            }
            if (pressed != null) {
                Object res = pressed.style.get(key);
                if (res != null) {
                    return res;
                }
            }
            if (parent != null) {
                return parent.getPressedValue(key);
            }
            
            if (includeDefault) {
                return get(key);
            }
            return null;
        }
        
        Object getDisabledValue(String key) {
            return getDisabledValue(key, false);
        }
        
        Object getDisabledValue(String key, boolean includeDefault) {
            if (isDisabledStyle()) {
                Object res = style.get(key);
                if (res != null) {
                    return res;
                }
            }
            if (disabled != null) {
                Object res = disabled.style.get(key);
                if (res != null) {
                    return res;
                }
            }
            if (parent != null) {
                return parent.getDisabledValue(key);
            }
            if (includeDefault) {
                return get(key);
            }
            
            return null;
        }
        
        boolean isUnselectedStyle() {
            return parent != null && parent.unselected == this;
        }
        
        boolean isSelectedStyle() {
            return parent != null && parent.selected == this;
        }
        
        boolean isPressedStyle() {
            return parent != null && parent.pressed == this;
            
        }
        
        boolean isDisabledStyle() {
            return parent != null && parent.disabled == this;
        }
        
        
        
        Map style = new HashMap();
        
        Border createBorder(Map<String,LexicalUnit> styles) {
            Border b = new Border();
            
            b.borderColorTop = renderAsCSSString("border-top-color", styles);
            b.borderColorRight = renderAsCSSString("border-right-color", styles);
            b.borderColorBottom = renderAsCSSString("border-bottom-color", styles);
            b.borderColorLeft = renderAsCSSString("border-left-color", styles);
            b.styleBottom = renderAsCSSString("border-bottom-style", styles);
            b.styleLeft = renderAsCSSString("border-left-style", styles);
            b.styleTop = renderAsCSSString("border-top-style", styles);
            b.styleRight = renderAsCSSString("border-right-style", styles);
            b.thicknessTop = renderAsCSSString("border-top-width", styles);
            b.thicknessRight = renderAsCSSString("border-right-width", styles);
            b.thicknessBottom = renderAsCSSString("border-bottom-width", styles);
            b.thicknessLeft = renderAsCSSString("border-left-width", styles);
            b.backgroundColor = renderAsCSSString("background-color", styles);
            b.backgroundImageUrl = renderAsCSSString("background-image", styles);
            b.backgroundRepeat = renderAsCSSString("background-repeat", styles);
            b.borderRadius = renderCSSProperty("cn1-border-bottom-left-radius-x", styles);
            b.boxShadow = renderCSSProperty("cn1-box-shadow-h", styles);
            
            LexicalUnit background = styles.get("background");
            while (background != null) {
                if (background.getFunctionName() != null && background.getFunctionName().contains("gradient")) {
                    b.gradient = renderAsCSSString(background);
                }
                background = background.getNextLexicalUnit();
            }
            return b;
        }
        
        Insets getImageBorderInsets(Map<String,LexicalUnit> styles, int width, int height) {
            // Case 1:  Solid background color
            LexicalUnit boxShadowH = styles.get("cn1-box-shadow-h");
            LexicalUnit borderRadiusBottomLeftX = styles.get("cn1-border-bottom-left-radius-x");
            LexicalUnit borderRadiusBottomLeftY = styles.get("cn1-border-bottom-left-radius-y");
            LexicalUnit borderRadiusBottomRightY = styles.get("cn1-border-bottom-right-radius-y");
            LexicalUnit borderRadiusBottomRightX = styles.get("cn1-border-bottom-right-radius-x");
            LexicalUnit borderRadiusTopRightX = styles.get("cn1-border-top-right-radius-x");
            LexicalUnit borderRadiusTopRightY = styles.get("cn1-border-top-right-radius-y");
            LexicalUnit borderRadiusTopLeftY = styles.get("cn1-border-top-left-radius-y");
            LexicalUnit borderRadiusTopLeftX = styles.get("cn1-border-top-left-radius-x");
            
            Insets i = new Insets();
            
            LexicalUnit background = styles.get("background");
            if (isNone(background)) {
                i.bottom = Math.max(getPixelValue(borderRadiusBottomLeftY), getPixelValue(borderRadiusBottomRightY)) + 5;
                i.top = Math.max(getPixelValue(borderRadiusTopLeftY), getPixelValue(borderRadiusTopRightY)) + 5;
                i.left = Math.max(getPixelValue(borderRadiusTopLeftX), getPixelValue(borderRadiusBottomLeftX)) + 5;
                i.right = Math.max(getPixelValue(borderRadiusTopRightX), getPixelValue(borderRadiusBottomRightX)) + 5;
            } else {
                i.bottom = height / 2-1;
                i.top = i.bottom;
                i.left = Math.max(getPixelValue(borderRadiusTopLeftX), getPixelValue(borderRadiusBottomLeftX)) + 5;
                i.right = Math.max(getPixelValue(borderRadiusTopRightX), getPixelValue(borderRadiusBottomRightX)) + 5;
                
            }
            
            return i;
        }
        
        public String getThemeFgColor(Map<String,LexicalUnit> style) {
            LexicalUnit color = style.get("color");
            if (color == null) {
                return null;
            }
            switch (color.getLexicalUnitType()) {
                case LexicalUnit.SAC_IDENT:
                case LexicalUnit.SAC_RGBCOLOR: {
                    System.out.println("Lex type "+color.getLexicalUnitType());
                    System.out.println("Color: "+color.getStringValue());
                    System.out.println(color);
                    String colorStr = color.getStringValue();
                    if (colorStr == null) {
                        colorStr = ""+color;
                        colorStr = colorStr.replace("color", "rgb");
                    }
                    Color c = Color.web(colorStr);
                    return String.format( "%02X%02X%02X",
                    (int)( c.getRed() * 255 ),
                    (int)( c.getGreen() * 255 ),
                    (int)( c.getBlue() * 255 ) );
                    
                }
                case LexicalUnit.SAC_FUNCTION: {
                    if ("rgba".equals(color.getFunctionName())) {
                        LexicalUnit param = color.getParameters();
                        float r = param.getFloatValue();
                        param = param.getNextLexicalUnit();
                        float g = param.getFloatValue();
                        param = param.getNextLexicalUnit();
                        float b = param.getFloatValue();
                        param = param.getNextLexicalUnit();
                        float a = param.getFloatValue();
                        
                        Color c = Color.rgb((int)r, (int)g, (int)b);
                        return String.format( "%02X%02X%02X",
                            (int)( c.getRed() * 255 ),
                            (int)( c.getGreen() * 255 ),
                            (int)( c.getBlue() * 255 ) );
                    }
                }
                default: 
                    throw new RuntimeException("Unsupported color type "+color.getLexicalUnitType());
            }
        }
        
        public String getThemeBgColor(Map<String,LexicalUnit> style) {
            LexicalUnit color = style.get("background-color");
            if (color == null) {
                return null;
            }
            switch (color.getLexicalUnitType()) {
                case LexicalUnit.SAC_IDENT:
                case LexicalUnit.SAC_RGBCOLOR: {
                    
                    System.out.println("Lex type "+color.getLexicalUnitType());
                    System.out.println("Color: "+color.getStringValue());
                    System.out.println(color);
                    String colorStr = color.getStringValue();
                    if (colorStr == null) {
                        colorStr = ""+color;
                        colorStr = colorStr.replace("color", "rgb");
                    }
                    if ("none".equals(colorStr)) {
                        return null;
                    }
                    Color c = Color.web(colorStr);
                    return String.format( "%02X%02X%02X",
                    (int)( c.getRed() * 255 ),
                    (int)( c.getGreen() * 255 ),
                    (int)( c.getBlue() * 255 ) );
                    
                }
                case LexicalUnit.SAC_FUNCTION: {
                    if ("rgba".equals(color.getFunctionName())) {
                        LexicalUnit param = color.getParameters();
                        float r = param.getFloatValue();
                        param = param.getNextLexicalUnit();
                        float g = param.getFloatValue();
                        param = param.getNextLexicalUnit();
                        float b = param.getFloatValue();
                        param = param.getNextLexicalUnit();
                        float a = param.getFloatValue();
                        
                        Color c = Color.rgb((int)r, (int)g, (int)b);
                        return String.format( "%02X%02X%02X",
                            (int)( c.getRed() * 255 ),
                            (int)( c.getGreen() * 255 ),
                            (int)( c.getBlue() * 255 ) );
                    }
                }
                default: 
                    throw new RuntimeException("Unsupported color type "+color.getLexicalUnitType());
            }
        }
        
        public boolean hasFilter(Map<String,LexicalUnit> style) {
            
            return false;
        }
        
        public boolean requiresBackgroundImageGeneration(Map<String,LexicalUnit> style) {
            
            LexicalUnit backgroundType = style.get("cn1-background-type");
            if (backgroundType != null) {
                if ( backgroundType.getStringValue().startsWith("cn1-image") && !backgroundType.getStringValue().endsWith("border")) {
                    
                    
                    return true;
                }
            }
            
            Border b = createBorder(style);
            
            if (b.hasBorderRadius() || b.hasGradient() || b.hasBoxShadow() || hasFilter(style) || b.hasUnequalBorders()) {
                // We might need to generate a background image
                // We first need to determine if this can be done with a 9-piece border
                // or if we'll need to stretch it.
                // Heuristics:
                // If the width or height is specified in percent
                // then this looks like a case for a generated image.
                LexicalUnit width = style.get("width");
                LexicalUnit height = style.get("height");
                
                if (width != null && width.getLexicalUnitType() == LexicalUnit.SAC_PERCENTAGE) {
                    return true;
                }
                if (height != null && height.getLexicalUnitType() == LexicalUnit.SAC_PERCENTAGE) {
                    return true;
                }
            }
            
            return false;
            
        }
        
        
        
        public boolean requiresImageBorder(Map<String,LexicalUnit> style) {
            LexicalUnit backgroundType = style.get("cn1-background-type");
            if (backgroundType != null && "cn1-image-border".equals(backgroundType.getStringValue())) {
                return true;
            }
            Border b = this.createBorder(style);
            if (b.hasBorderRadius() || b.hasGradient() || b.hasBoxShadow() || hasFilter(style) || b.hasUnequalBorders()) {
                LexicalUnit width = style.get("width");
                LexicalUnit height = style.get("height");
                
                if (width != null && width.getLexicalUnitType() == LexicalUnit.SAC_PERCENTAGE) {
                    return false;
                }
                if (height != null && height.getLexicalUnitType() == LexicalUnit.SAC_PERCENTAGE) {
                    return false;
                }
                return true;
            }
            return false;
        }
        
        public boolean hasBackgroundImage(Map<String,LexicalUnit> style) {
            LexicalUnit bgImage = style.get("background-image");
            return bgImage != null && bgImage.getLexicalUnitType() == LexicalUnit.SAC_URI;
        }
        
        public Byte getThemeBgType(Map<String,LexicalUnit> style) {
            LexicalUnit value = style.get("cn1-background-type");
            if (value == null) {
                // Not explicitly specified so we must use some heuristics here
                if (!requiresBackgroundImageGeneration(style) &&  hasBackgroundImage(style)) {
                    LexicalUnit repeat = style.get("background-repeat");
                    if (repeat != null) {
                        switch (repeat.getStringValue()) {
                            case "repeat" :
                                return Style.BACKGROUND_IMAGE_TILE_BOTH;
                            case "repeat-x" :
                                return Style.BACKGROUND_IMAGE_TILE_HORIZONTAL;
                            case "repeat-y" :
                                return Style.BACKGROUND_IMAGE_TILE_VERTICAL;
                            case "no-repeat" : 
                            default : {
                                LexicalUnit bgSize = style.get("background-size");
                                if (bgSize != null) {
                                    switch (bgSize.getLexicalUnitType()) {
                                        case LexicalUnit.SAC_PERCENTAGE: {
                                            LexicalUnit width = bgSize;
                                            LexicalUnit height = bgSize.getNextLexicalUnit();
                                            if (width.getFloatValue() > 99 && (height == null || height.getFloatValue() > 99 || "auto".equals(height.getStringValue()))) {
                                                // This is clearly asking us to stretch the 
                                                // image
                                                return Style.BACKGROUND_IMAGE_SCALED;
                                            }
                                            break;
                                        }
                                        
                                        case LexicalUnit.SAC_IDENT :
                                            switch (bgSize.getStringValue()) {
                                                case "cover" :
                                                    return Style.BACKGROUND_IMAGE_SCALED_FILL;
                                                case "contains" :
                                                    return Style.BACKGROUND_IMAGE_SCALED_FIT;
                                            }
                                            
                                        break;
                                            
                                    }
                                }
                                
                                LexicalUnit bgPosition1 = style.get("background-position");
                                
                                if (bgPosition1 != null) {
                                    LexicalUnit bgPosition2 = bgPosition1.getNextLexicalUnit();
                                    
                                    String val1 = bgPosition1.getStringValue();
                                    String val2 = bgPosition2 == null ? "auto" : bgPosition2.getStringValue();
                                    
                                    switch (val1) {
                                        case "left" :
                                            switch (val2) {
                                                case "top" :
                                                    return Style.BACKGROUND_IMAGE_ALIGNED_TOP_LEFT;
                                                
                                                case "bottom" :
                                                    return Style.BACKGROUND_IMAGE_ALIGNED_BOTTOM_LEFT;
                                                default:
                                                    return Style.BACKGROUND_IMAGE_ALIGNED_LEFT;
                                            }
                                        
                                        
                                        case "right" : 
                                            switch (val2) {
                                                case "top" :
                                                    return Style.BACKGROUND_IMAGE_ALIGNED_TOP_RIGHT;
                                                case "bottom" :
                                                    return Style.BACKGROUND_IMAGE_ALIGNED_BOTTOM_RIGHT;
                                                default :
                                                    return Style.BACKGROUND_IMAGE_ALIGNED_RIGHT;
                                            }
                                        
                                        default :
                                            switch (val2) {
                                                case "top" :
                                                    return Style.BACKGROUND_IMAGE_ALIGNED_TOP;
                                                case "bottom" :
                                                    return Style.BACKGROUND_IMAGE_ALIGNED_BOTTOM;
                                                default:
                                                    return Style.BACKGROUND_IMAGE_ALIGNED_CENTER;
                                            }
                                    }
                                }
                                
                                
                            }
                                
                                    
                        }
                    } else {
                        return Style.BACKGROUND_IMAGE_TILE_BOTH;
                    }
                } else if (requiresBackgroundImageGeneration(style)) {
                    LexicalUnit bgSize = style.get("background-size");
                    if (bgSize != null) {
                        switch (bgSize.getLexicalUnitType()) {
                            case LexicalUnit.SAC_PERCENTAGE: {
                                LexicalUnit width = bgSize;
                                LexicalUnit height = bgSize.getNextLexicalUnit();
                                if (width.getFloatValue() > 99 && (height == null || height.getFloatValue() > 99 || "auto".equals(height.getStringValue()))) {
                                    // This is clearly asking us to stretch the 
                                    // image
                                    return Style.BACKGROUND_IMAGE_SCALED;
                                }
                                break;
                            }

                            case LexicalUnit.SAC_IDENT :
                                switch (bgSize.getStringValue()) {
                                    case "cover" :
                                        return Style.BACKGROUND_IMAGE_SCALED_FILL;
                                    case "contains" :
                                        return Style.BACKGROUND_IMAGE_SCALED_FIT;
                                }

                            break;

                        }
                    } else {
                        return Style.BACKGROUND_IMAGE_SCALED;
                    }
                } 
                return null;
                
            }
            
            switch (value.getStringValue()) {
                case "cn1-image-scaled":
                    return Style.BACKGROUND_IMAGE_SCALED;
                case "cn1-image-scaled-fill":
                    return Style.BACKGROUND_IMAGE_SCALED_FILL;
                case "cn1-image-scaled-fit":
                    return Style.BACKGROUND_IMAGE_SCALED_FIT;
                case "cn1-image-tile-both" :
                    return Style.BACKGROUND_IMAGE_TILE_BOTH;
                case "cn1-image-tile-valign-left" :
                    return Style.BACKGROUND_IMAGE_TILE_VERTICAL_ALIGN_LEFT;
                case "cn1-image-tile-valign-center" :
                    return Style.BACKGROUND_IMAGE_TILE_VERTICAL_ALIGN_CENTER;
                case "cn1-image-tile-valign-right" :
                    return Style.BACKGROUND_IMAGE_TILE_VERTICAL_ALIGN_RIGHT;
                case "cn1-image-tile-halign-top" :
                    return Style.BACKGROUND_IMAGE_TILE_HORIZONTAL_ALIGN_TOP;
                case "cn1-image-tile-halign-center" :
                    return Style.BACKGROUND_IMAGE_TILE_HORIZONTAL_ALIGN_CENTER;
                case "cn1-image-tile-halign-bottom" :
                    return Style.BACKGROUND_IMAGE_TILE_HORIZONTAL_ALIGN_BOTTOM;
                case "cn1-image-align-bottom" :
                    return Style.BACKGROUND_IMAGE_ALIGNED_BOTTOM;
                case "cn1-image-align-right" :
                    return Style.BACKGROUND_IMAGE_ALIGNED_RIGHT;
                case "cn1-image-align-center" :
                    return Style.BACKGROUND_IMAGE_ALIGNED_CENTER;
                case "cn1-image-align-top-left" :
                    return Style.BACKGROUND_IMAGE_ALIGNED_TOP_LEFT;
                case "cn1-image-align-top-right" :
                    return Style.BACKGROUND_IMAGE_ALIGNED_TOP_RIGHT;
                case "cn1-image-align-bottom-left" :
                    return Style.BACKGROUND_IMAGE_ALIGNED_BOTTOM_LEFT;
                case "cn1-image-align-bottom-right" :
                    return Style.BACKGROUND_IMAGE_ALIGNED_BOTTOM_RIGHT;
                case "cn1-image-border":
                case "cn1-none" :
                case "none":
                    return Style.BACKGROUND_NONE;
                default:
                    throw new RuntimeException("Unsupported bg type "+value);
            }
        }
        public String getThemePadding(Map<String,LexicalUnit> style) {
            if (!style.containsKey("padding-left") && !style.containsKey("padding-top") && !style.containsKey("padding-bottom") && !style.containsKey("padding-right")) {
                return null;
            }
            Insets i = getInsets("padding", style);
            return i.top + ","+i.bottom+","+i.left+","+i.right;
        }
        
        public byte[] getThemePaddingUnit(Map<String,LexicalUnit> style) {
            if (!style.containsKey("padding-left") && !style.containsKey("padding-top") && !style.containsKey("padding-bottom") && !style.containsKey("padding-right")) {
                return null;
            }
            Insets i = getInsets("padding", style);
            return new byte[]{i.topUnit, i.rightUnit, i.bottomUnit, i.leftUnit};
        }
        
        public String getThemeMargin(Map<String,LexicalUnit> style) {
            if (!style.containsKey("margin-left") && !style.containsKey("margin-top") && !style.containsKey("margin-bottom") && !style.containsKey("margin-right")) {
                return null;
            }
            Insets i = getInsets("margin", style);
            return i.top + ","+i.bottom+","+i.left+","+i.right;
        }
        
        public byte[] getThemeMarginUnit(Map<String,LexicalUnit> style) {
            if (!style.containsKey("margin-left") && !style.containsKey("margin-top") && !style.containsKey("margin-bottom") && !style.containsKey("margin-right")) {
                return null;
            }
            Insets i = getInsets("margin", style);
            return new byte[]{i.topUnit, i.rightUnit, i.bottomUnit, i.leftUnit};
        }
        
        public Integer getThemeAlignment(Map<String,LexicalUnit> style) {
            LexicalUnit value = style.get("text-align");
            if (value == null) {
                return null;
            }
            
            switch (value.getLexicalUnitType()) {
                case LexicalUnit.SAC_IDENT:
                    switch (value.getStringValue()) {
                        case "center" :
                            return Component.CENTER;
                        case "left" :
                            return Component.LEFT;
                        case "right":
                            return Component.RIGHT;
                        default :
                            throw new RuntimeException("Unsupported alignment "+value);
                            
                    }
                
                default:
                    throw new RuntimeException("Unsupported lexical unit type for text-align "+value.getLexicalUnitType());
            }
            
            
        }
        
        FontFace findFontFace(String family) {
            for (FontFace f : fontFaces) {
                if (f.fontFamily != null && family.equals(f.fontFamily.getStringValue())) {
                    return f;
                }
            }
            return null;
        }
        
        
        
        public com.codename1.ui.Font getThemeFont(Map<String,LexicalUnit> styles) {
            LexicalUnit fontFamily = styles.get("font-family");
            LexicalUnit fontSize = styles.get("font-size");
            LexicalUnit fontStyle = styles.get("font-style");
            LexicalUnit fontWeight = styles.get("font-weight");
            
            if (fontFamily == null && fontSize == null && fontStyle == null && fontWeight == null) {
                return null;
            }
            
            int iFontFace = Font.FACE_SYSTEM;
            int iFontStyle = Font.STYLE_PLAIN;
            int iFontSizeType = Font.SIZE_MEDIUM;
            
            EditorTTFFont ttfFont = null;
            
            
            loop : while (fontSize != null) {
                switch (fontSize.getLexicalUnitType()) {
                    case LexicalUnit.SAC_IDENT: {
                        switch (fontSize.getStringValue().toLowerCase()) {
                            case "small":
                            case "x-small":
                            case "xx-small":
                                iFontSizeType = Font.SIZE_SMALL;
                                break loop;
                            case "large":
                            case "x-large":
                            case "xx-large":
                                iFontSizeType = Font.SIZE_LARGE;
                                break loop;
                                
                        }
                    }
                        
                }
                fontSize = fontSize.getNextLexicalUnit();
            }
            
            loop : while (fontStyle != null) {
                switch (fontStyle.getStringValue()) {
                    case "normal" :
                        iFontStyle = Font.STYLE_PLAIN;
                        break loop;
                    case "italic" :
                    case "oblique" :
                        iFontStyle = Font.STYLE_ITALIC;
                        break loop;
                        
                }
                fontStyle = fontStyle.getNextLexicalUnit();
            }
            
            loop : while (fontWeight != null) {
                switch (fontWeight.getStringValue()) {
                    case "bold" :
                        iFontStyle = Font.STYLE_BOLD;
                        break loop;
                        
                }
                fontWeight = fontWeight.getNextLexicalUnit();
            }
            
            loop : while (fontFamily != null) {
                if (fontFamily.getStringValue() != null) {
                    switch (fontFamily.getStringValue().toLowerCase()) {
                        case "sans-serif" :
                        case "serif" :
                        case "times" :
                        case "courier" :
                        case "arial" :
                        case "cursive" :
                        case "fantasy" :
                            iFontFace = Font.FACE_SYSTEM;
                            break loop;
                        case "monospace" :
                            iFontFace = Font.FACE_MONOSPACE;
                            break loop;
                        default :
                            FontFace face = findFontFace(fontFamily.getStringValue());
                            if (face != null) {
                                File fontFile = face.getFontFile();
                                if (fontFile != null) {
                                    
                                    int ttfFontSize = 1; // medium
                                    float actualSize = 14f;
                                    switch (iFontSizeType) {
                                        case Font.SIZE_SMALL:
                                            ttfFontSize = 0;
                                            actualSize = 11f;
                                            break;
                                        case Font.SIZE_LARGE:
                                            ttfFontSize = 2;
                                            actualSize = 20f;
                                            break;
                                    }
                                    
                                    // Check for a more specific font size
                                    if (fontSize != null) {
                                        switch (fontSize.getLexicalUnitType()) {
                                            case LexicalUnit.SAC_MILLIMETER:
                                                ttfFontSize = 4;
                                                actualSize = fontSize.getFloatValue();
                                                break;
                                            case LexicalUnit.SAC_PIXEL:
                                            case LexicalUnit.SAC_POINT:
                                                ttfFontSize = 3;
                                                actualSize = fontSize.getFloatValue();
                                                break;
                                                
                                            case LexicalUnit.SAC_CENTIMETER:
                                                ttfFontSize = 4;
                                                actualSize = fontSize.getFloatValue()*10f;
                                                break;
                                            case LexicalUnit.SAC_INCH:
                                                ttfFontSize = 4;
                                                actualSize = fontSize.getFloatValue()*25f;
                                                break;
                                            case LexicalUnit.SAC_EM:
                                                ttfFontSize = 3;
                                                actualSize = fontSize.getFloatValue()* 14f;
                                                break;
                                            case LexicalUnit.SAC_PERCENTAGE:
                                                ttfFontSize = 3;
                                                actualSize = fontSize.getFloatValue() /100f * 14f;
                                                break;
                                                
                                        }
                                    }
                                    Font sys = Font.createSystemFont(iFontFace,iFontStyle, iFontSizeType);
                           
                                    ttfFont = new EditorTTFFont(fontFile, ttfFontSize, actualSize, sys);
                                    break loop;
                                }
                            }
                            
                            
                    }
                }
                fontFamily = fontFamily.getNextLexicalUnit();
            }
            
            if (ttfFont != null) {
                return ttfFont;
            } else {
                return Font.createSystemFont(iFontFace,iFontStyle, iFontSizeType);
            }
            
        }
        
        
        
        public String getThemeTransparency(Map<String,LexicalUnit> styles) {
            
            LexicalUnit cn1BgType = styles.get("cn1-background-type");
            if (cn1BgType != null && "none".equals(cn1BgType.getStringValue())) {
                return "0";
            }
        LexicalUnit bgColor = styles.get("background-color");
        if (bgColor == null) {
            return null;
        }
        while (bgColor != null) {
            if ("transparent".equals(bgColor.getStringValue())) {
                return "0";
            }
            if ("rgba".equals(bgColor.getFunctionName())) {
                LexicalUnit params = bgColor.getParameters();
                LexicalUnit lastParam = params;
                while (params != null) {
                    lastParam = params;
                    params = params.getNextLexicalUnit();
                }
                
                switch (lastParam.getLexicalUnitType()) {
                    case LexicalUnit.SAC_REAL :
                        return String.valueOf((int)lastParam.getFloatValue());
                    case LexicalUnit.SAC_INTEGER :
                        return String.valueOf(lastParam.getIntegerValue()); 
                    default:
                        throw new RuntimeException("Unsupported alpha value type in rgba func "+lastParam.getLexicalUnitType());
                }
            } else {
                return "255";
            }
            //bgColor = bgColor.getNextLexicalUnit();
        }
        
        return null;
    }
        public com.codename1.ui.plaf.Border getThemeBorder(Map<String,LexicalUnit> styles) {
            Border b = this.createBorder(styles);
            
            if (b.hasUnequalBorders()) {
                return com.codename1.ui.plaf.Border.createCompoundBorder(
                        getThemeBorder(styles, "top"),
                        getThemeBorder(styles, "bottom"),
                        getThemeBorder(styles, "left"),
                        getThemeBorder(styles, "right")
                );
            } else {
                return getThemeBorder(styles, "top");
            }
        }
        
        
        public String getThemeDerive(Map<String,LexicalUnit> styles, String suffix) {
            
            LexicalUnit derive = styles.get("cn1-derive");
            System.out.println("Cn1 derive "+derive);
            if (derive != null) {
                return derive.getStringValue()+suffix;
            }
            return null;
        }
        
        public com.codename1.ui.plaf.Border getThemeBorder(Map<String,LexicalUnit> styles, String side) {
            
            LexicalUnit topColor = styles.get("border-"+side+"-color");
            LexicalUnit topStyle = styles.get("border-"+side+"-style");
            LexicalUnit topWidth = styles.get("border-"+side+"-width");
            
            if (topStyle == null) {
                return null;
            }
            
            int color = 0;
            
            if (topColor != null) {
                color = getColorInt(topColor);
            }
            
            int thickness = 1;
            if (topWidth != null) {
                switch (topWidth.getLexicalUnitType()) {
                    case LexicalUnit.SAC_PIXEL:
                    case LexicalUnit.SAC_POINT:
                        thickness = (int)topWidth.getFloatValue();
                        break;
                    case LexicalUnit.SAC_INTEGER:
                        thickness = (int)topWidth.getIntegerValue();
                        break;
                    case LexicalUnit.SAC_MILLIMETER:
                        thickness = (int)(topWidth.getFloatValue()*10f);
                        break;
                    case LexicalUnit.SAC_PERCENTAGE:
                        thickness = (int)(topWidth.getFloatValue()*320f/100f);
                        break;
                }
            }
            //com.codename1.ui.plaf.Border out;
            switch (topStyle.getStringValue()) {
                case "none" :
                case "hidden" :
                case "inherit":
                case "initial":
                    return com.codename1.ui.plaf.Border.createEmpty();
                case "dotted" :
                    return com.codename1.ui.plaf.Border.createDottedBorder(thickness, color);
                case "dashed" :
                    return com.codename1.ui.plaf.Border.createDashedBorder(thickness, color);
                case "solid" :
                    return com.codename1.ui.plaf.Border.createLineBorder(thickness, color);
                case "double":
                    return com.codename1.ui.plaf.Border.createDoubleBorder(thickness, color);
                case "groove":
                    return com.codename1.ui.plaf.Border.createGrooveBorder(thickness, color);
                case "ridge":
                    return com.codename1.ui.plaf.Border.createRidgeBorder(thickness, color);
                case "inset":
                    return com.codename1.ui.plaf.Border.createInsetBorder(thickness, color);
                case "outset":
                    return com.codename1.ui.plaf.Border.createOutsetBorder(thickness, color);
                default:
                    throw new RuntimeException("Unsupported border type "+topStyle+" for side "+side);
            }
        }
        
        public Byte getThemeTextDecoration(Map<String,LexicalUnit> style) {
            LexicalUnit value = style.get("text-decoration");
            if (value == null) {
                return null;
            }
            
            switch (value.getLexicalUnitType()) {
                case LexicalUnit.SAC_IDENT :
                    switch (value.getStringValue()) {
                        case "underline" :
                            return Style.TEXT_DECORATION_UNDERLINE;
                        case "overline" :
                            return Style.TEXT_DECORATION_OVERLINE;
                        case "line-through" :
                            return Style.TEXT_DECORATION_STRIKETHRU;
                        case "none" :
                            return Style.TEXT_DECORATION_NONE;
                        case "cn1-3d" :
                            return Style.TEXT_DECORATION_3D;
                        case "cn1-3d-lowered" :
                            return Style.TEXT_DECORATION_3D_LOWERED;
                        case "cn1-3d-shadow-north" :
                            return Style.TEXT_DECORATION_3D_SHADOW_NORTH;
                        default :
                            throw new RuntimeException("Unsupported text decoration value "+value);
                    }
                default :
                    throw new RuntimeException("Unsupported lexical unit type for text-decoration "+value.getLexicalUnitType());
            }
        }
    }
    
    int getPixelValue(LexicalUnit value) {
        if (isNone(value)){
            return 0;
        }
        switch (value.getLexicalUnitType()) {
            case LexicalUnit.SAC_PIXEL:
                return (int)px(value.getFloatValue());
            case LexicalUnit.SAC_INTEGER:
                return (int)value.getIntegerValue();
            default:
                throw new RuntimeException("Cannot get pixel value for lexical type "+value.getLexicalUnitType());
        }
    }
    
    
    private static boolean isNone(LexicalUnit value) {
        if (value == null || "none".equals(value.getStringValue())) {
            return true;
        }
        return false;
    }
    
    String renderAsCSSString(String property, Map<String,LexicalUnit> styles) {
        return renderAsCSSString(styles.get(property));
    }
    
    
    public void apply(Element style, String property, LexicalUnit value) {
        System.out.println("Applying property "+property);
        switch (property) {
            
            case "opacity" : {
                style.put("opacity", value);
                break;
            }
                
            
            case "cn1-derive" : {
                System.out.println("In cn1-derive");
                String parentName = value.getStringValue();
                style.setParent(value.getStringValue());
                style.put("cn1-derive", value);
                break;
            }
                
            
            case "font" : {
                boolean doneFontStyle = false;
                boolean doneFontFamily = false;
                boolean doneFontSize = false;
                while (value != null) {
                    if (value.getStringValue() != null) {
                        switch (value.getStringValue().toLowerCase()) {
                            case "italic":
                            case "normal":
                            case "oblique" :
                                if (!doneFontStyle) {
                                    apply(style, "font-style", value);
                                    doneFontStyle = true;
                                }
                                break;
                            case "serif" :
                            case "sans-serif" :
                            case "monospace" :
                                if (!doneFontFamily) {
                                    apply(style, "font-family", value);
                                    doneFontFamily = true;
                                }
                                break;
                            case "medium" :
                            case "xx-small" :
                            case "x-small" :
                            case "small" :
                            case "large" :
                            case "x-large" :
                            case "xx-large" :
                            case "smaller" :
                            case "larger" :
                                if (!doneFontSize) {
                                    apply(style, "font-size", value);
                                    doneFontSize = true;
                                }
                                break;
                                
                        }
                        
                    } else {
                        switch (value.getLexicalUnitType()) {
                            case LexicalUnit.SAC_PIXEL:
                            case LexicalUnit.SAC_POINT:
                            case LexicalUnit.SAC_MILLIMETER:
                            case LexicalUnit.SAC_INTEGER :
                            case LexicalUnit.SAC_PERCENTAGE:
                            case LexicalUnit.SAC_EM:
                            case LexicalUnit.SAC_REAL:
                            case LexicalUnit.SAC_INCH:
                            case LexicalUnit.SAC_CENTIMETER:
                                if (!doneFontSize) {
                                    apply(style, "font-size", value);
                                    doneFontSize = true;
                                }
                                break;
                            case LexicalUnit.SAC_STRING_VALUE:
                                if (!doneFontFamily) {
                                    apply(style, "font-family", value);
                                    doneFontFamily = true;
                                }
                            break;
                        }
                    }
                    value = value.getNextLexicalUnit();
                }
                break;
            }
            case "font-family" :
                style.put("font-family", value);
                break;
            case "font-size" :
                style.put("font-size", value);
                break;
                
            case "font-style" :
                style.put("font-style", value);
                break;
                
            case "font-weight" :
                style.put("font-weight", value);
                break;
            
            case "margin-top" :
                style.put("margin-top", value);
                break;
                
            case "margin-left" :
                style.put("margin-left", value);
                break;
            case "margin-right" :
                style.put("margin-right", value);
                break;
            case "margin-bottom" :
                style.put("margin-bottom", value);
                break;
            
            case "margin" : {
                List<LexicalUnit> units = new ArrayList<LexicalUnit>();
                while (value != null) {
                    units.add(value);
                    value = value.getNextLexicalUnit();
                }
                switch (units.size()) {
                    case 1 :
                        apply(style, "margin-top", units.get(0));
                        apply(style, "margin-right", units.get(0));
                        apply(style, "margin-bottom", units.get(0));
                        apply(style, "margin-left", units.get(0));
                        break;
                    case 2 :
                        apply(style, "margin-top", units.get(0));
                        apply(style, "margin-bottom", units.get(0));
                        apply(style, "margin-right", units.get(1));
                        apply(style, "margin-left", units.get(1));
                        break;
                    case 3 :
                        apply(style, "margin-top", units.get(0));
                        apply(style, "margin-right", units.get(1));
                        apply(style, "margin-left", units.get(1));
                        apply(style, "margin-bottom", units.get(2));
                        break;
                    case 4 :
                        apply(style, "margin-top", units.get(0));
                        apply(style, "margin-right", units.get(1));
                        apply(style, "margin-bottom", units.get(2));
                        apply(style, "margin-left", units.get(3));
                        break;
                    default :
                        throw new RuntimeException("Unsupported number of units in margin property "+units.size());
                                
                }
                break;
                /*
                Insets i = getInsets(value);
                style.put("margin",i.top + "," + i.bottom + ","+i.left+","+i.right);
                style.put("marUnit", new byte[]{i.topUnit, i.bottomUnit, i.leftUnit, i.rightUnit});
                break;
                */
            }
            
            case "padding-top" :
                style.put("padding-top", value);
                break;
                
            case "padding-left" :
                style.put("padding-left", value);
                break;
            case "padding-right" :
                style.put("padding-right", value);
                break;
            case "padding-bottom" :
                style.put("padding-bottom", value);
                break;
            
            case "padding" : {
                List<LexicalUnit> units = new ArrayList<LexicalUnit>();
                while (value != null) {
                    units.add(value);
                    value = value.getNextLexicalUnit();
                }
                switch (units.size()) {
                    case 1 :
                        apply(style, "padding-top", units.get(0));
                        apply(style, "padding-right", units.get(0));
                        apply(style, "padding-bottom", units.get(0));
                        apply(style, "padding-left", units.get(0));
                        break;
                    case 2 :
                        apply(style, "padding-top", units.get(0));
                        apply(style, "padding-bottom", units.get(0));
                        apply(style, "padding-right", units.get(1));
                        apply(style, "padding-left", units.get(1));
                        break;
                    case 3 :
                        apply(style, "padding-top", units.get(0));
                        apply(style, "padding-right", units.get(1));
                        apply(style, "padding-left", units.get(1));
                        apply(style, "padding-bottom", units.get(2));
                        break;
                    case 4 :
                        apply(style, "padding-top", units.get(0));
                        apply(style, "padding-right", units.get(1));
                        apply(style, "padding-bottom", units.get(2));
                        apply(style, "padding-left", units.get(3));
                        break;
                    default :
                        throw new RuntimeException("Unsupported number of units in padding property "+units.size());
                                
                }
                break;
                /*
                Insets i = getInsets(value);
                style.put("margin",i.top + "," + i.bottom + ","+i.left+","+i.right);
                style.put("marUnit", new byte[]{i.topUnit, i.bottomUnit, i.leftUnit, i.rightUnit});
                break;
                */
            }
            /*
            case "padding" : {
                Insets i = getInsets(value);
                style.put("padding", i.top+","+i.bottom+","+i.left+","+i.right);
                style.put("padUnit", new byte[]{i.topUnit, i.bottomUnit, i.leftUnit, i.rightUnit});
                break;
            }
            */
            
            case "color" : {
                /*
                Color c= getColor(value);
                style.put("fgColor",String.format( "#%02X%02X%02X",
                    (int)( c.getRed() * 255 ),
                    (int)( c.getGreen() * 255 ),
                    (int)( c.getBlue() * 255 ) ));
                break;
                */
                style.put("color", value);
                break;
            }
            
            case "background-image" : {
                style.put("background-image", value);
                break;
            }
            
            case "background-color" : {
                style.put("background-color", value);
                break;
            }
            
            case "background-repeat" : {
                style.put("background-repeat", value);
                break;
            }
            
            case "width" : {
                style.put("width", value);
                break;
            }
            
            case "height" : {
                style.put("height", value);
                break;
            }
            
            case "cn1-image-id" : {
                style.put("cn1-image-id", value);
                break;
            }
            
            case "min-width" : {
                style.put("min-width", value);
                break;
            }
            
            case "min-height" : {
                style.put("min-height", value);
                break;
            }
            
            case "background-size" : {
                style.put("background-size", value);
                break;
            }
            
            case "text-align" : {
                style.put("text-align", value);
                break;
            }
            
            case "text-decoration" : {
                style.put("text-decoration", value);
                break;
            }
            
            case "cn1-background-type" : {
                style.put("cn1-background-type", value);
                break;
            }
            
            case "background" : {
                System.out.println("Setting background");
                
                while (value != null) {
                    System.out.println(value);
                    System.out.println(value.getLexicalUnitType());
                    switch (value.getLexicalUnitType()) {
                        case LexicalUnit.SAC_IDENT:
                            switch (value.getStringValue()) {
                                //{ "IMAGE_SCALED", "IMAGE_SCALED_FILL", "IMAGE_SCALED_FIT", "IMAGE_TILE_BOTH", "IMAGE_TILE_VERTICAL_ALIGN_LEFT", "IMAGE_TILE_VERTICAL_ALIGN_CENTER", "IMAGE_TILE_VERTICAL_ALIGN_RIGHT", "IMAGE_TILE_HORIZONTAL_ALIGN_TOP", "IMAGE_TILE_HORIZONTAL_ALIGN_CENTER", "IMAGE_TILE_HORIZONTAL_ALIGN_BOTTOM", "IMAGE_ALIGNED_TOP", "IMAGE_ALIGNED_BOTTOM", "IMAGE_ALIGNED_LEFT", "IMAGE_ALIGNED_RIGHT", "IMAGE_ALIGNED_TOP_LEFT", "IMAGE_ALIGNED_TOP_RIGHT", "IMAGE_ALIGNED_BOTTOM_LEFT", "IMAGE_ALIGNED_BOTTOM_RIGHT", "IMAGE_ALIGNED_CENTER", "GRADIENT_LINEAR_HORIZONTAL", "GRADIENT_LINEAR_VERTICAL", "GRADIENT_RADIAL", "NONE" }));
                                case "cn1-image-scaled":
                                case "cn1-image-scaled-fill":
                                case "cn1-image-scaled-fit":
                                case "cn1-image-tile-both" :
                                case "cn1-image-tile-valign-left" :
                                case "cn1-image-tile-valign-center" :
                                case "cn1-image-tile-valign-right" :
                                case "cn1-image-tile-halign-top" :
                                case "cn1-image-tile-halign-center" :
                                case "cn1-image-tile-halign-bottom" :
                                case "cn1-image-align-bottom" :
                                case "cn1-image-align-left" :
                                case "cn1-image-align-right" :
                                case "cn1-image-align-center" :
                                case "cn1-image-align-top-left" :
                                case "cn1-image-align-top-right" :
                                case "cn1-image-align-bottom-left" :
                                case "cn1-image-align-bottom-right" :
                                case "cn1-image-border":
                                case "cn1-none" :
                                case "none" :
                                    apply(style, "cn1-background-type", value);
                                    break; 
                                
                            }
                        
                        // no break here because ident could be a color too 
                        // so we let proceed to next (RGB_COLOR).
                        case LexicalUnit.SAC_RGBCOLOR :
                            System.out.println("Setting background color "+value);
                            apply(style, "background-color", value);
                            break;
                        case LexicalUnit.SAC_URI :
                            apply(style, "background-image", value);
                            break;
                        case LexicalUnit.SAC_FUNCTION :
                            switch (value.getFunctionName()) {
                                case "linear-gradient" :
                                case "radial-gradient" :
                                    style.put("background", value);
                                    break;
                                default:
                                    throw new RuntimeException("Unsupported function in background property");
                                    
                            }
                        break;
                        
                        default :
                            throw new RuntimeException("Unsupported lexical type for background "+value.getLexicalUnitType());
                    }
                    value = value.getNextLexicalUnit();
                }
                break;
            }
            case "border-style-top":
            case "border-top-style" : {
                style.put("border-top-style", value);
                break;
            }
            case "border-style-right":
            case "border-right-style" : {
                style.put("border-right-style", value);
                break;
            }
            case "border-style-bottom":
            case "border-bottom-style" : {
                style.put("border-bottom-style", value);
                break;
            }
            case "border-style-left":
            case "border-left-style" : {
                style.put("border-left-style", value);
                break;
            }
            case "border-width-top":
            case "border-top-width" : {
                style.put("border-top-width", value);
                break;
            }
            case "border-width-right":
            case "border-right-width" : {
                style.put("border-right-width", value);
                break;
            }
            case "border-width-bottom":
            case "border-bottom-width" : {
                style.put("border-bottom-width", value);
                break;
            }
            case "border-width-left":
            case "border-left-width" : {
                style.put("border-left-width", value);
                break;
            }
            case "border-color-top":
            case "border-top-color" : {
                style.put("border-top-color", value);
                break;
            }
            case "border-color-right":
            case "border-right-color" : {
                style.put("border-right-color", value);
                break;
            }
            case "border-color-bottom":
            case "border-bottom-color" : {
                style.put("border-bottom-color", value);
                break;
            }
            case "border-color-left":
            case "border-left-color" : {
                style.put("border-left-color", value);
                break;
            }
            
            case "border-style" : {
                List<LexicalUnit> units = new ArrayList<LexicalUnit>();
                while (value != null) {
                    units.add(value);
                    value = value.getNextLexicalUnit();
                }
                switch (units.size()) {
                    case 1 :
                        apply(style, "border-style-top", units.get(0));
                        apply(style, "border-style-left", units.get(0));
                        apply(style, "border-style-bottom", units.get(0));
                        apply(style, "border-style-right", units.get(0));
                        break;
                    case 2 :
                        apply(style, "border-style-top", units.get(0));
                        apply(style, "border-style-bottom", units.get(0));
                        apply(style, "border-style-right", units.get(1));
                        apply(style, "border-style-left", units.get(1));
                        break;
                    case 3 :
                        apply(style, "border-style-top", units.get(0));
                        apply(style, "border-style-right", units.get(1));
                        apply(style, "border-style-left", units.get(1));
                        apply(style, "border-style-bottom", units.get(2));
                        break;
                    case 4 :
                        apply(style, "border-style-top", units.get(0));
                        apply(style, "border-style-left", units.get(3));
                        apply(style, "border-style-bottom", units.get(2));
                        apply(style, "border-style-right", units.get(1));
                        break;
                    default:
                        throw new RuntimeException("Unsupported number of units for border-style "+units.size());
                }
                break;
            }
            
            case "border-width" : {
                List<LexicalUnit> units = new ArrayList<LexicalUnit>();
                while (value != null) {
                    units.add(value);
                    value = value.getNextLexicalUnit();
                }
                switch (units.size()) {
                    case 1 :
                        apply(style, "border-width-top", units.get(0));
                        apply(style, "border-width-left", units.get(0));
                        apply(style, "border-width-bottom", units.get(0));
                        apply(style, "border-width-right", units.get(0));
                        break;
                    case 2 :
                        apply(style, "border-width-top", units.get(0));
                        apply(style, "border-width-bottom", units.get(0));
                        apply(style, "border-width-right", units.get(1));
                        apply(style, "border-width-left", units.get(1));
                        break;
                    case 3 :
                        apply(style, "border-width-top", units.get(0));
                        apply(style, "border-width-right", units.get(1));
                        apply(style, "border-width-left", units.get(1));
                        apply(style, "border-width-bottom", units.get(2));
                        break;
                    case 4 :
                        apply(style, "border-width-top", units.get(0));
                        apply(style, "border-width-left", units.get(3));
                        apply(style, "border-width-bottom", units.get(2));
                        apply(style, "border-width-right", units.get(1));
                        break;
                    default:
                        throw new RuntimeException("Unsupported number of units for border-width "+units.size());
                }
                break;
            }
            
            case "border-color" : {
                List<LexicalUnit> units = new ArrayList<LexicalUnit>();
                while (value != null) {
                    units.add(value);
                    value = value.getNextLexicalUnit();
                }
                switch (units.size()) {
                    case 1 :
                        apply(style, "border-color-top", units.get(0));
                        apply(style, "border-color-left", units.get(0));
                        apply(style, "border-color-bottom", units.get(0));
                        apply(style, "border-color-right", units.get(0));
                        break;
                    case 2 :
                        apply(style, "border-color-top", units.get(0));
                        apply(style, "border-color-bottom", units.get(0));
                        apply(style, "border-color-right", units.get(1));
                        apply(style, "border-color-left", units.get(1));
                        break;
                    case 3 :
                        apply(style, "border-color-top", units.get(0));
                        apply(style, "border-color-right", units.get(1));
                        apply(style, "border-color-left", units.get(1));
                        apply(style, "border-color-bottom", units.get(2));
                        break;
                    case 4 :
                        apply(style, "border-color-top", units.get(0));
                        apply(style, "border-color-left", units.get(3));
                        apply(style, "border-color-bottom", units.get(2));
                        apply(style, "border-color-right", units.get(1));
                        break;
                    default:
                        throw new RuntimeException("Unsupported number of units for border-style "+units.size());
                }
                break;
            }
            
            case "border-top" : {
                while (value != null) {
                    switch (value.getLexicalUnitType()) {
                        case LexicalUnit.SAC_FUNCTION:
                        case LexicalUnit.SAC_RGBCOLOR :
                            apply(style, "border-color-top", value);
                            break;
                        case LexicalUnit.SAC_CENTIMETER :
                        case LexicalUnit.SAC_MILLIMETER :
                        case LexicalUnit.SAC_PIXEL :
                        case LexicalUnit.SAC_POINT :
                            apply(style, "border-width-top", value);
                            break;
                        case LexicalUnit.SAC_IDENT :
                            try {
                                Color.web(value.getStringValue());
                                apply(style, "border-color-top", value);
                            } catch (IllegalArgumentException ex) {
                                apply(style, "border-style-top", value);
                            }
                            break;
                        default :
                            throw new RuntimeException("Unsupported lexical unit in border-top: "+value.getLexicalUnitType());
                    }
                    value = value.getNextLexicalUnit();
                }
                break;
            }
            
            case "border-right" : {
                while (value != null) {
                    switch (value.getLexicalUnitType()) {
                        case LexicalUnit.SAC_FUNCTION:
                        case LexicalUnit.SAC_RGBCOLOR :
                            apply(style, "border-color-right", value);
                            break;
                        case LexicalUnit.SAC_CENTIMETER :
                        case LexicalUnit.SAC_MILLIMETER :
                        case LexicalUnit.SAC_PIXEL :
                        case LexicalUnit.SAC_POINT :
                            apply(style, "border-width-right", value);
                            break;
                        case LexicalUnit.SAC_IDENT :
                            try {
                                Color.web(value.getStringValue());
                                apply(style, "border-color-right", value);
                            } catch (IllegalArgumentException ex) {
                                apply(style, "border-style-right", value);
                            }
                            break;
                        default :
                            throw new RuntimeException("Unsupported lexical unit in border-right: "+value.getLexicalUnitType());
                    }
                    value = value.getNextLexicalUnit();
                }
                break;
            }
            
            case "border-bottom" : {
                while (value != null) {
                    switch (value.getLexicalUnitType()) {
                        case LexicalUnit.SAC_FUNCTION:
                        case LexicalUnit.SAC_RGBCOLOR :
                            apply(style, "border-color-bottom", value);
                            break;
                        case LexicalUnit.SAC_CENTIMETER :
                        case LexicalUnit.SAC_MILLIMETER :
                        case LexicalUnit.SAC_PIXEL :
                        case LexicalUnit.SAC_POINT :
                            apply(style, "border-width-bottom", value);
                            break;
                        case LexicalUnit.SAC_IDENT :
                            try {
                                Color.web(value.getStringValue());
                                apply(style, "border-color-bottom", value);
                            } catch (IllegalArgumentException ex) {
                                apply(style, "border-style-bottom", value);
                            }
                        break;
                        default :
                            throw new RuntimeException("Unsupported lexical unit in border-bottom: "+value.getLexicalUnitType());
                    }
                    value = value.getNextLexicalUnit();
                }
                break;
            }
            
            case "border-left" : {
                while (value != null) {
                    switch (value.getLexicalUnitType()) {
                        case LexicalUnit.SAC_FUNCTION:
                        case LexicalUnit.SAC_RGBCOLOR :
                            apply(style, "border-color-left", value);
                            break;
                        case LexicalUnit.SAC_CENTIMETER :
                        case LexicalUnit.SAC_MILLIMETER :
                        case LexicalUnit.SAC_PIXEL :
                        case LexicalUnit.SAC_POINT :
                            apply(style, "border-width-left", value);
                            break;
                        case LexicalUnit.SAC_IDENT :
                            try {
                                Color.web(value.getStringValue());
                                apply(style, "border-color-left", value);
                            } catch (IllegalArgumentException ex) {
                                apply(style, "border-style-left", value);
                            }
                            break;
                        default :
                            throw new RuntimeException("Unsupported lexical unit in border-left: "+value.getLexicalUnitType());
                    }
                    value = value.getNextLexicalUnit();
                }
                break;
            }
                
            case "border" : {
                apply(style, "border-top", value);
                apply(style, "border-right", value);
                apply(style, "border-bottom", value);
                apply(style, "border-left", value);
                break;
            }
            
            case "cn1-border-top-left-radius-x" :
                style.put("cn1-border-top-left-radius-x", value);
                break;
            case "cn1-border-bottom-left-radius-x" :
                style.put("cn1-border-bottom-left-radius-x", value);
                break;
            case "cn1-border-top-right-radius-x" :
                style.put("cn1-border-top-right-radius-x", value);
                break;
            case "cn1-border-bottom-right-radius-x" : 
                style.put("cn1-border-bottom-right-radius-x", value);
                break;
            case "cn1-border-top-left-radius-y" :
                style.put("cn1-border-top-left-radius-y", value);
                break;
            case "cn1-border-bottom-left-radius-y" :
                style.put("cn1-border-bottom-left-radius-y", value);
                break;
            case "cn1-border-top-right-radius-y" :
                style.put("cn1-border-top-right-radius-y", value);
                break;
            case "cn1-border-bottom-right-radius-y" : 
                style.put("cn1-border-bottom-right-radius-y", value);
                break;
            case "border-top-left-radius" :
                apply(style, "cn1-border-top-left-radius-x", value);
                apply(style, "cn1-border-top-left-radius-y", value);
                break;
            case "border-bottom-left-radius" :
                apply(style, "cn1-border-bottom-left-radius-x", value);
                apply(style, "cn1-border-bottom-left-radius-y", value);
                break;
            case "border-top-right-radius" :
                apply(style, "cn1-border-top-right-radius-x", value);
                apply(style, "cn1-border-top-right-radius-y", value);
                break;
            case "border-bottom-right-radius" : 
                apply(style, "cn1-border-bottom-right-radius-x", value);
                apply(style, "cn1-border-bottom-right-radius-y", value);
                break;
                
            case "border-radius" : {
                List<LexicalUnit> xUnits = new ArrayList<LexicalUnit>();
                List<LexicalUnit> yUnits = new ArrayList<LexicalUnit>();
                List<LexicalUnit> tmpUnits = xUnits;
                while (value != null) {
                    if (value.getLexicalUnitType() == LexicalUnit.SAC_OPERATOR_SLASH) {
                        tmpUnits = yUnits;
                        value = value.getNextLexicalUnit();
                        continue;
                    }
                    tmpUnits.add(value);
                    value = value.getNextLexicalUnit();
                }
                switch (xUnits.size()) {
                    case 1 :
                        apply(style, "cn1-border-top-left-radius-x", xUnits.get(0));
                        apply(style, "cn1-border-top-right-radius-x", xUnits.get(0));
                        apply(style, "cn1-border-bottom-left-radius-x", xUnits.get(0));
                        apply(style, "cn1-border-bottom-right-radius-x", xUnits.get(0));
                        break;
                    case 2 :
                        apply(style, "cn1-border-top-left-radius-x", xUnits.get(0));
                        apply(style, "cn1-border-top-right-radius-x", xUnits.get(1));
                        apply(style, "cn1-border-bottom-left-radius-x", xUnits.get(1));
                        apply(style, "cn1-border-bottom-right-radius-x", xUnits.get(0));
                        break;
                    case 3 :
                        apply(style, "cn1-border-top-left-radius-x", xUnits.get(0));
                        apply(style, "cn1-border-top-right-radius-x", xUnits.get(1));
                        apply(style, "cn1-border-bottom-left-radius-x", xUnits.get(1));
                        apply(style, "cn1-border-bottom-right-radius-x", xUnits.get(2));
                        break;
                        
                    case 4 :
                        apply(style, "cn1-border-top-left-radius-x", xUnits.get(0));
                        apply(style, "cn1-border-top-right-radius-x", xUnits.get(1));
                        apply(style, "cn1-border-bottom-left-radius-x", xUnits.get(3));
                        apply(style, "cn1-border-bottom-right-radius-x", xUnits.get(2));
                        break;
                        
                    default:
                        throw new RuntimeException("Unsupported number of x units for border radius : "+xUnits.size());
                        
                }
                
                if (yUnits.isEmpty()) {
                    yUnits = xUnits;
                }
                
                switch (yUnits.size()) {
                    case 1 :
                        apply(style, "cn1-border-top-left-radius-y", yUnits.get(0));
                        apply(style, "cn1-border-top-right-radius-y", yUnits.get(0));
                        apply(style, "cn1-border-bottom-left-radius-y", yUnits.get(0));
                        apply(style, "cn1-border-bottom-right-radius-y", yUnits.get(0));
                        break;
                    case 2 :
                        apply(style, "cn1-border-top-left-radius-y", yUnits.get(0));
                        apply(style, "cn1-border-top-right-radius-y", yUnits.get(1));
                        apply(style, "cn1-border-bottom-left-radius-y", yUnits.get(1));
                        apply(style, "cn1-border-bottom-right-radius-y", yUnits.get(0));
                        break;
                    case 3 :
                        apply(style, "cn1-border-top-left-radius-y", yUnits.get(0));
                        apply(style, "cn1-border-top-right-radius-y", yUnits.get(1));
                        apply(style, "cn1-border-bottom-left-radius-y", yUnits.get(1));
                        apply(style, "cn1-border-bottom-right-radius-y", yUnits.get(2));
                        break;
                        
                    case 4 :
                        apply(style, "cn1-border-top-left-radius-y", yUnits.get(0));
                        apply(style, "cn1-border-top-right-radius-y", yUnits.get(1));
                        apply(style, "cn1-border-bottom-left-radius-y", yUnits.get(3));
                        apply(style, "cn1-border-bottom-right-radius-y", yUnits.get(2));
                        break;
                        
                    default:
                        throw new RuntimeException("Unsupported number of y units for border radius : "+yUnits.size());
                }
            }
            break;
            
                
            case "cn1-box-shadow-h" : {
                style.put("cn1-box-shadow-h", value);
                break;
            }
            
            case "cn1-box-shadow-v" : {
                style.put("cn1-box-shadow-v", value);
                break;
            }
            
            case "cn1-box-shadow-blur" : {
                style.put("cn1-box-shadow-blur", value);
                break;
            }
            
            case "cn1-box-shadow-spread" : {
                style.put("cn1-box-shadow-spread", value);
                break;
            }
            
            case "cn1-box-shadow-color" : {
                style.put("cn1-box-shadow-color", value);
                break;
            }
            
            case "cn1-box-shadow-inset" : {
                style.put("cn1-box-shadow-inset", value);
                break;
            }
            
            case "box-shadow" : {
                
                if ("none".equals(value.getStringValue())) {
                    apply(style, "cn1-box-shadow-inset", value);
                    apply(style, "cn1-box-shadow-color", value);
                    apply(style, "cn1-box-shadow-spread", value);
                    apply(style, "cn1-box-shadow-blur", value);
                    apply(style, "cn1-box-shadow-h", value);
                    apply(style, "cn1-box-shadow-v", value);
                    break;
                }
                
                int i = 0;
                String[] params = {"cn1-box-shadow-h", "cn1-box-shadow-v", "cn1-box-shadow-blur", "cn1-box-shadow-spread"};
                boolean colorSet = false;
                while (value != null) {
                    switch (value.getLexicalUnitType()) {
                        case LexicalUnit.SAC_IDENT :
                            if ("inset".equals(value.getStringValue())) {
                                apply(style, "cn1-box-shadow-inset", value);
                            } else {
                                apply(style, "cn1-box-shadow-color", value);
                                colorSet = true;
                            }
                        break;
                            
                        case LexicalUnit.SAC_PIXEL:
                        case LexicalUnit.SAC_MILLIMETER:
                        case LexicalUnit.SAC_PERCENTAGE:
                        case LexicalUnit.SAC_CENTIMETER:
                        case LexicalUnit.SAC_EM:
                        case LexicalUnit.SAC_POINT:
                        case LexicalUnit.SAC_INTEGER:
                            apply(style, params[i++], value);
                            break;
                            
                        case LexicalUnit.SAC_RGBCOLOR:
                            apply(style, "cn1-box-shadow-color", value);
                            colorSet = true;
                            break;
                        case LexicalUnit.SAC_FUNCTION:
                            if ("rgba".equals(value.getFunctionName())) {
                                apply(style, "cn1-box-shadow-color", value);
                            } else {
                                throw new RuntimeException("Unrecognized function when parsing box-shadow "+value.getFunctionName());
                            }
                        break;
                        default:
                            throw new RuntimeException("Unsupported lexical unit type in box shadow "+value.getLexicalUnitType());
                        
                    }
                    value = value.getNextLexicalUnit();
                }
                
                break;
            }
            
            default :
                throw new RuntimeException("Unsupported CSS property "+property);
            
                
        }
    }
    
    Element getElementByName(String name) {
        if (!elements.containsKey(name)) {
            Element el = new Element();
            el.parent = anyNodeStyle;
            elements.put(name, el);
        }
        return (Element)elements.get(name);
    }
    
    Element getElementForSelector(Selector sel) {
        switch (sel.getSelectorType()) {
            case Selector.SAC_ANY_NODE_SELECTOR :
                return anyNodeStyle;
            case Selector.SAC_ELEMENT_NODE_SELECTOR : {
                ElementSelector esel = (ElementSelector)sel;
                return getElementByName(esel.getLocalName());
            }
            case Selector.SAC_CONDITIONAL_SELECTOR : {
                ConditionalSelector csel = (ConditionalSelector)sel;
                SimpleSelector simple = csel.getSimpleSelector();
                switch (simple.getSelectorType()) {
                    case Selector.SAC_ANY_NODE_SELECTOR : {
                        Element parent = anyNodeStyle;
                        switch (csel.getCondition().getConditionType()) {
                            case Condition.SAC_CLASS_CONDITION :
                                AttributeCondition clsCond = (AttributeCondition)csel.getCondition();
                                switch (clsCond.getLocalName()) {
                                    case "selected" :
                                        return parent.getSelected();
                                    case "unselected" :
                                        return parent.getUnselected();
                                    case "pressed" :
                                        return parent.getPressed();
                                    case "disabled" :
                                        return parent.getDisabled();
                                    default :
                                        throw new RuntimeException("Unsupported style class "+clsCond.getLocalName());
                                }
                            default :
                                throw new RuntimeException("Unsupported CSS condition type "+csel.getCondition().getConditionType());
                        }
                    }
                    case Selector.SAC_ELEMENT_NODE_SELECTOR : {
                        ElementSelector esel = (ElementSelector)simple;
                        Element parent = getElementForSelector(esel);
                        switch (csel.getCondition().getConditionType()) {
                            case Condition.SAC_CLASS_CONDITION :
                                AttributeCondition clsCond = (AttributeCondition)csel.getCondition();
                                
                                switch (clsCond.getValue()) {
                                    case "selected" :
                                        return parent.getSelected();
                                    case "unselected" :
                                        return parent.getUnselected();
                                    case "pressed" :
                                        return parent.getPressed();
                                    case "disabled" :
                                        return parent.getDisabled();
                                    default :
                                        throw new RuntimeException("Unsupported style class "+clsCond.getValue());
                                }
                            default :
                                throw new RuntimeException("Unsupported CSS condition type "+csel.getCondition().getConditionType());
                        }
                    }
                    default :
                        throw new RuntimeException("Unsupported selector type "+simple.getSelectorType());
                }
                
                
            }
            default :
                throw new RuntimeException("Unsupported selector type "+sel.getSelectorType());
        }
    }
    
    public void apply(Selector sel, String property, LexicalUnit value) {
        Element el = getElementForSelector(sel);
        apply(el, property, value);
        
    }
    
    private class Insets {
        static final int TOP=0;
        static final int RIGHT=1;
        static final int BOTTOM=2;
        static final int LEFT=3;
        
        int top, right, left, bottom;
        byte topUnit, rightUnit, leftUnit, bottomUnit;
        
        void set(int index, int value, byte unit) {
            switch (index) {
                case TOP:
                    top = value;
                    topUnit = unit;
                    break;
                case RIGHT:
                    right = value;
                    rightUnit = unit;
                    break;
                case BOTTOM:
                    bottom = value;
                    bottomUnit = unit;
                    break;
                case LEFT:
                    left = value;
                    leftUnit = unit;
                    break;
                default:
                    throw new RuntimeException("Invalid index "+index);
            }
        }
        
        public String toString() {
            return top+","+right+","+bottom+","+left;
                    
        }
    }
    
    
    
    private class IntValue {
        int value;
        byte unit;
    }
    
    private IntValue getIntValue(LexicalUnit value) {
        
        IntValue out = new IntValue();
        
        if (value == null) {
            return out;
        }
        
        int pixelValue = 0;
        byte unit = 0;
        switch (value.getLexicalUnitType()) {
            case LexicalUnit.SAC_PIXEL :
            case LexicalUnit.SAC_POINT :
            case LexicalUnit.SAC_INTEGER:
                unit = (byte)0;
                pixelValue = Math.round(value.getFloatValue());
                break;
            case LexicalUnit.SAC_MILLIMETER :
                unit = (byte)2;
                pixelValue = Math.round(value.getFloatValue());
                break;
            case LexicalUnit.SAC_CENTIMETER :
                unit = (byte) 2;
                pixelValue = Math.round(value.getFloatValue() * 10);
                break;
            default :
                throw new RuntimeException("Unsupported unit for inset "+value.getDimensionUnitText());
        }
        
        out.value = pixelValue;
        out.unit = unit;
        return out;
            
    }
    
    private Insets getInsets(String key, Map<String,LexicalUnit> style) {
        Insets i = new Insets();
        
        LexicalUnit value = style.get(key+"-top");
        IntValue top = getIntValue(value);
        IntValue right = getIntValue(style.get(key+"-right"));
        IntValue bottom = getIntValue(style.get(key+"-bottom"));
        IntValue left = getIntValue(style.get(key+"-left"));
        
        i.top = top.value;
        i.topUnit = top.unit;
        i.right = right.value;
        i.rightUnit = right.unit;
        i.bottom = bottom.value;
        i.bottomUnit = bottom.unit;
        i.left = left.value;
        i.leftUnit = left.unit;
        return i;
            
    }
    
    private Insets getInsets(LexicalUnit value) {
        Insets i = new Insets();
        int index = Insets.TOP;
        while (value != null) {
            int pixelValue = 0;
            byte unit = 0;
            switch (value.getLexicalUnitType()) {
                case LexicalUnit.SAC_PIXEL :
                case LexicalUnit.SAC_POINT :
                    unit = (byte)0;
                    pixelValue = (int)px(Math.round(value.getFloatValue()));
                    break;
                case LexicalUnit.SAC_MILLIMETER :
                    unit = (byte)2;
                    pixelValue = Math.round(value.getFloatValue());
                    break;
                case LexicalUnit.SAC_CENTIMETER :
                    unit = (byte) 2;
                    pixelValue = Math.round(value.getFloatValue() * 10);
                    break;
                default :
                    throw new RuntimeException("Unsupported unit for inset "+value.getDimensionUnitText());
            }
            
            switch (index) {
                case Insets.TOP :
                    i.set(Insets.TOP, pixelValue, unit);
                    i.set(Insets.RIGHT, pixelValue, unit);
                    i.set(Insets.BOTTOM, pixelValue, unit);
                    i.set(Insets.LEFT, pixelValue, unit);
                    break;
                    
                case Insets.RIGHT :
                    i.set(Insets.LEFT, pixelValue, unit);
                    i.set(Insets.RIGHT, pixelValue, unit);
                    break;
                case Insets.BOTTOM :
                    i.set(Insets.BOTTOM, pixelValue, unit);
                    break;
                case Insets.LEFT :
                    i.set(Insets.LEFT, pixelValue, unit);
                    
            }
            
            index++;
            value = value.getNextLexicalUnit();
        }
        return i;
    }
    
    /*
    Border getBorder(LexicalUnit lu) {
        int width = 1;
        
        while (lu != null) {
            switch (lu.getLexicalUnitType()) {
                case LexicalUnit.SAC_PIXEL :
                    
            }
        }
    }
    */
    
    Color getColor(LexicalUnit color) {
        String str =getColorString(color);
        return Color.web("#"+str);
        
    }
    
    int getColorInt(LexicalUnit color) {
        String str = getColorString(color);
        return Integer.valueOf(str, 16);
    }
    
    String getColorString(LexicalUnit color) {
        switch (color.getLexicalUnitType()) {
                case LexicalUnit.SAC_IDENT:
                case LexicalUnit.SAC_RGBCOLOR: {
                    System.out.println("Lex type "+color.getLexicalUnitType());
                    System.out.println("Color: "+color.getStringValue());
                    System.out.println(color);
                    String colorStr = color.getStringValue();
                    if (colorStr == null) {
                        colorStr = ""+color;
                        colorStr = colorStr.replace("color", "rgb");
                    }
                    Color c = Color.web(colorStr);
                    return String.format( "%02X%02X%02X",
                    (int)( c.getRed() * 255 ),
                    (int)( c.getGreen() * 255 ),
                    (int)( c.getBlue() * 255 ) );
                }
                case LexicalUnit.SAC_FUNCTION: {
                    if ("rgba".equals(color.getFunctionName())) {
                        LexicalUnit param = color.getParameters();
                        float r = param.getFloatValue();
                        param = param.getNextLexicalUnit();
                        float g = param.getFloatValue();
                        param = param.getNextLexicalUnit();
                        float b = param.getFloatValue();
                        param = param.getNextLexicalUnit();
                        float a = param.getFloatValue();
                        
                        Color c = Color.rgb((int)r, (int)g, (int)b);
                        return String.format( "%02X%02X%02X",
                            (int)( c.getRed() * 255 ),
                            (int)( c.getGreen() * 255 ),
                            (int)( c.getBlue() * 255 ) );
                    }
                }
                default: 
                    throw new RuntimeException("Unsupported color type "+color.getLexicalUnitType());
            }
    }
    
    
    
    public static CSSTheme load(URL uri) throws IOException {
        try {
            System.setProperty("org.w3c.css.sac.parser", "org.w3c.flute.parser.Parser");
            InputSource source = new InputSource();
            InputStream stream = uri.openStream();
            source.setByteStream(stream);
            source.setURI(uri.toString());
            ParserFactory parserFactory = new ParserFactory();
            Parser parser = parserFactory.makeParser();
            final CSSTheme theme = new CSSTheme();
            theme.baseURL = uri;
            parser.setDocumentHandler(new DocumentHandler() {
                
                SelectorList currSelectors;
                FontFace currFontFace;
                
                @Override
                public void startDocument(InputSource is) throws CSSException {
                    //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
                }
                
                @Override
                public void endDocument(InputSource is) throws CSSException {
                    //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
                }
                
                @Override
                public void comment(String string) throws CSSException {
                    //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
                }
                
                @Override
                public void ignorableAtRule(String string) throws CSSException {
                    //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
                }
                
                @Override
                public void namespaceDeclaration(String string, String string1) throws CSSException {
                    //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
                }
                
                @Override
                public void importStyle(String string, SACMediaList sacml, String string1) throws CSSException {
                    //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
                }
                
                @Override
                public void startMedia(SACMediaList sacml) throws CSSException {
                    //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
                }
                
                @Override
                public void endMedia(SACMediaList sacml) throws CSSException {
                    //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
                }
                
                @Override
                public void startPage(String string, String string1) throws CSSException {
                    //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
                }
                
                @Override
                public void endPage(String string, String string1) throws CSSException {
                    //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
                }
                
                @Override
                public void startFontFace() throws CSSException {
                    currFontFace = theme.createFontFace();
                }
                
                @Override
                public void endFontFace() throws CSSException {
                    currFontFace = null;
                }
                
                @Override
                public void startSelector(SelectorList sl) throws CSSException {
                    //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
                    currSelectors = sl;
                    
                }
                
                @Override
                public void endSelector(SelectorList sl) throws CSSException {
                    currSelectors = null;
                }
                
                @Override
                public void property(String string, LexicalUnit lu, boolean bln) throws CSSException {
                    
                    if (currFontFace != null) {
                        switch (string) {
                            case "font-family" :
                                currFontFace.fontFamily = lu;
                                break;
                            case "font-style" :
                                currFontFace.fontStyle = lu;
                                break;
                            case "font-stretch" :
                                currFontFace.fontStretch = lu;
                                break;
                            case "src" :
                                currFontFace.src = lu;
                                break;
                            case "font-weight" :
                                currFontFace.fontWeight = lu;
                                break;
                        }
                    } else {

                        int len = currSelectors.getLength();
                        for (int i=0; i<len; i++) {
                            Selector sel = currSelectors.item(i);
                            theme.apply(sel, string, lu);

                        }
                    }
                }
                
            });
            parser.parseStyleSheet(source);
            stream.close();
            return theme;
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(CSSTheme.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            Logger.getLogger(CSSTheme.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            Logger.getLogger(CSSTheme.class.getName()).log(Level.SEVERE, null, ex);
        } catch (NullPointerException ex) {
            Logger.getLogger(CSSTheme.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassCastException ex) {
            Logger.getLogger(CSSTheme.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
    
    public static void main(String[] args) throws Exception {
        CSSTheme theme = CSSTheme.load(CSSTheme.class.getResource("test.css"));
        
    }
    
    
    
}
