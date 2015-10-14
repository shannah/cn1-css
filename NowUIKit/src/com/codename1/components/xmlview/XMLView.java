/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.codename1.components.xmlview;

import com.codename1.io.ConnectionRequest;
import com.codename1.io.NetworkManager;
import com.codename1.ui.Component;
import com.codename1.ui.Container;
import com.codename1.ui.Display;
import com.codename1.ui.EncodedImage;
import com.codename1.ui.Font;
import com.codename1.ui.FontImage;
import com.codename1.ui.Image;
import com.codename1.ui.Label;
import com.codename1.ui.URLImage;
import com.codename1.ui.layouts.BoxLayout;
import com.codename1.ui.util.Resources;
import com.codename1.util.Callback;
import com.codename1.xml.Element;
import com.codename1.xml.XMLParser;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Vector;

/**
 *
 * @author shannah
 */
public class XMLView extends Container{
    
    public static final int ASPECT_SQUARE=1;
    public static final int ASPECT_16X9=2;
    
    private final HashMap<String,ViewFactory> factoryMap = new HashMap<String,ViewFactory>();
    private Resources theme;
    private String baseURL;
    
    private EncodedImage defaultSquarePlaceholderImage;
    private EncodedImage default16x9PlaceholderImage;
    
    private final HashMap<String,Image> icons = new HashMap<String, Image>();
    private final Font iconFont;
    private int defaultIconColor = 0xCCCCCC;
    
    
    public EncodedImage getDefaultSquarePlaceholderImage() {
        if (defaultSquarePlaceholderImage == null || defaultSquarePlaceholderImage.getWidth() != this.getWidth()) {
            defaultSquarePlaceholderImage = EncodedImage.createFromImage(Image.createImage(this.getWidth(), this.getWidth()), false);
        }
        return defaultSquarePlaceholderImage;
    }
    
    
    public EncodedImage getDefault16x9PlaceholderImage() {
        if (default16x9PlaceholderImage == null || default16x9PlaceholderImage.getWidth() != this.getWidth()) {
            double height = ((double)this.getWidth()) * 9.0 / 16.0;
            default16x9PlaceholderImage = EncodedImage.createFromImage(Image.createImage(this.getWidth(), (int)Math.round(height)), false);
        }
        return default16x9PlaceholderImage;
    }
    
    private final Map<String,EncodedImage> placeholders = new HashMap<String,EncodedImage>();
    
    public void setPlaceholderImage(String name, EncodedImage img) {
        placeholders.put(name, img);
    }
    
    public EncodedImage getPlaceholderImage(String name, int aspectPreference) {
        EncodedImage img = placeholders.get(name);
        if (img == null) {
            img = (EncodedImage)theme.getImage(name);
            if (img != null) {
                placeholders.put(name, img);
            }
        }
        if (img == null) {
            switch (aspectPreference) {
                case ASPECT_16X9:
                    img = getDefault16x9PlaceholderImage();
                    break;
                default:
                    img = getDefaultSquarePlaceholderImage();
                    
            }
            if (img != null) {
                placeholders.put(name, img);
            }
        }
        return img;
    }
    
    public Image getImage(String url, String placeholder, int aspectPreference) {
        url = toAbsoluteUrl(url);
        return URLImage.createToStorage(getPlaceholderImage(placeholder, aspectPreference), placeholder + "@"+this.getWidth()+";" + url, url, URLImage.RESIZE_SCALE_TO_FILL);
    }
    
    public Image getIcon(String charCode, int width, int height, int color) {
        String key = charCode+"@"+width+"x"+height+"#"+color;
        Image icn = icons.get(key);
        if (icn == null) {
            icn = FontImage.createFixed(charCode, iconFont, color, width, height);
            icons.put(key, icn);
        }
        return icn;
        
    }
    
    public Image getIcon(String charCode, int width, int height) {
        return getIcon(charCode, width, height, defaultIconColor);
    }
    
    
    
    
    
    
    public static  interface ViewFactory {
        Component createView(Element el, XMLView context);
    }
    
    public XMLView() {
        this(null);
    }
    
    public XMLView(Resources theme) {
        setLayout(new BoxLayout(BoxLayout.Y_AXIS));
        this.theme = theme;
        
        Label l = new Label();
        l.setUIID("XMLViewIcon");
        iconFont = Font.createTrueTypeFont("FontAwesome", "fontawesome-webfont.ttf");
    }
    
    public void setXML(String xml, String base) {
        baseURL = base;
        setXML(xml);
    }
    
    public void setTheme(Resources theme) {
        this.theme = theme;
    }
    
    public void setXML(String xml) {
        try {
            XMLParser parser = new XMLParser();
            Element root = parser.parse(new InputStreamReader(new ByteArrayInputStream(xml.getBytes("UTF-8"))));
            setXML(root);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    
    
    public void setXML(Element root, String base) {
        baseURL = base;
        setXML(root);
    }
    public void setXML(Element root) {
        //XMLParser parser = new XMLParser();
        System.out.println("Setting XML");
        if (root.getAttribute("base") != null) {
            baseURL = root.getAttribute("base");
        }
        if ("doc".equals(root.getTagName())) {
            System.out.println("Root is doc");
            Iterator <Element> it = root.iterator();
            while (it.hasNext()) {
                Element nex = it.next();
                if ("head".equals(nex.getTagName())) {
                    Vector baseTags = nex.getDescendantsByTagName("base");
                    if (!baseTags.isEmpty()) {
                        Element baseTag = (Element)baseTags.get(0);
                        baseURL = baseTag.getAttribute("href");
                    }
                } else if ("body".equals(nex.getTagName())) {
                    root = nex;
                    if (root.getAttribute("base") != null) {
                        baseURL = root.getAttribute("base");
                    }
                }
            }
            
        }
        
        //Element root = parser.parse(new StringReader(xmlstr));
        this.removeAll();
        Iterator<Element> it = root.iterator();
        while (it.hasNext()) {
            System.out.println("Adding element");
            Component c = createView(it.next());
            if (c != null) {
                addComponent(c);
            }
        }
        revalidate();
    }
    
    public Component createView(Element el) {
        String tagName = el.getTagName();
        ViewFactory factory = factoryMap.get(tagName);
        if (factory != null) {
            return factory.createView(el, this);
        }
        return null;
    }
    
    public Resources getTheme() {
        return theme;
    }
    
    public String toAbsoluteUrl(String url) {
        if (url.indexOf("http://") == 0 || url.indexOf("https://") == 0) {
            return url;
        } else if (url.indexOf("/") == 0) {
            int slashPos = baseURL.indexOf('/', 8);
            if (slashPos >= 0) {
                return baseURL.substring(0, slashPos) + url;
            } else {
                return baseURL + url;
            }
            
        } else {
            return baseURL + url;
        }
        
    }
    
    public void registerViewFactory(String tagName, ViewFactory factory) {
        factoryMap.put(tagName, factory);
    }
    
    
    public void load(String url, final Callback<Element> callback) {
        ConnectionRequest req = new ConnectionRequest() {

            @Override
            protected void readResponse(InputStream input) throws IOException {
                XMLParser parser = new XMLParser();
                final Element el = parser.parse(new InputStreamReader(input));
                Display.getInstance().callSerially(new Runnable() {

                    public void run() {
                        setXML(el);
                        callback.onSucess(el);
                    }
                });
            }
            
        };
        req.setUrl(url);
        req.setPost(false);
        req.setHttpMethod("GET");
        NetworkManager.getInstance().addToQueue(req);
        
    }
}
