/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.codename1.components.xmlview;

/**
 *
 * @author shannah
 */
public class DefaultXMLViewKit {
    public void install(XMLView view) {
        view.registerViewFactory("img", new ImageView());
        view.registerViewFactory("p", new ParagraphView());
        view.registerViewFactory("video", new VideoView());
        view.registerViewFactory("carousel", new CarouselView());
    }
}
