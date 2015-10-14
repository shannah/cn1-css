/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.codename1.components.xmlview;

import com.codename1.components.ImageViewer;
import com.codename1.ui.Component;
import com.codename1.ui.EncodedImage;
import com.codename1.ui.Image;
import com.codename1.ui.URLImage;
import com.codename1.ui.list.DefaultListModel;
import com.codename1.ui.util.Resources;
import com.codename1.xml.Element;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 *
 * @author shannah
 */
public class CarouselView implements XMLView.ViewFactory {

    public Component createView(Element el, XMLView context) {
        ImageViewer iv = new ImageViewer();
        List<Image> images = new ArrayList<Image>();
        Iterator<Element> it = el.iterator();
        String placeholderName = el.getAttribute("placeholder");
        if (placeholderName == null) {
            placeholderName = "image";
        }
        EncodedImage placeholder =  context.getPlaceholderImage(placeholderName, XMLView.ASPECT_SQUARE);
        iv.setSwipePlaceholder(placeholder);
        while (it.hasNext()) {
            Element nex = it.next();
            if ("img".equals(nex.getTagName())) {
                String src = context.toAbsoluteUrl(nex.getAttribute("src"));
                images.add(context.getImage(src, placeholderName, XMLView.ASPECT_SQUARE));
            }
        }
        iv.setImageList(new DefaultListModel<Image>(images));
        
        String uiid = el.getAttribute("uiid");
        if (uiid == null) {
            uiid = "CarouselView";
        }
        iv.setUIID(uiid);
        
        
        return iv;
    }
    
}
