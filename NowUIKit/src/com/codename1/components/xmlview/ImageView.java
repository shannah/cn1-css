/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.codename1.components.xmlview;

import com.codename1.ui.Component;
import com.codename1.ui.EncodedImage;
import com.codename1.ui.Image;
import com.codename1.ui.Label;
import com.codename1.ui.URLImage;
import com.codename1.ui.util.Resources;
import com.codename1.xml.Element;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author shannah
 */
public class ImageView implements XMLView.ViewFactory {

    public Component createView(Element el, XMLView context) {
        String placeholder = el.getAttribute("placeholder");
        if (placeholder == null) {
            placeholder = "image";
        }
        String src = context.toAbsoluteUrl(el.getAttribute("src"));
        
        Label l = new Label(context.getImage(src, placeholder, XMLView.ASPECT_SQUARE));
        
        String uiid = el.getAttribute("uiid");
        if (uiid == null) {
            uiid = "ImageView";
        }
        l.setUIID(uiid);
        return l;
    }
    
}
