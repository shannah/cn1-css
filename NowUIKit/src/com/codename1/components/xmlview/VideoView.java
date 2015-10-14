/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.codename1.components.xmlview;

import com.codename1.components.MediaPlayer;
import com.codename1.media.Media;
import com.codename1.media.MediaManager;
import com.codename1.ui.Button;
import com.codename1.ui.Component;
import com.codename1.ui.Display;
import com.codename1.ui.Graphics;
import com.codename1.ui.Image;
import com.codename1.ui.URLImage;
import com.codename1.ui.events.ActionEvent;
import com.codename1.ui.events.ActionListener;
import com.codename1.ui.events.ScrollListener;
import com.codename1.ui.geom.Dimension;
import com.codename1.xml.Element;

/**
 *
 * @author shannah
 */
public class VideoView implements XMLView.ViewFactory {

    public Component createView(Element el, XMLView context) {
        String posterUrl = context.toAbsoluteUrl(el.getAttribute("poster"));
        String placeholder = el.getAttribute("placeholder");
        if (placeholder == null) {
            placeholder = "video";
        }
        final String src = context.toAbsoluteUrl(el.getAttribute("src"));
        
        Image posterImg = context.getImage(posterUrl, placeholder, XMLView.ASPECT_16X9);
        final Image playIcon = context.getIcon("\uf144", posterImg.getWidth()/9, posterImg.getWidth()/9);
        final Button placeholderBtn = new Button(posterImg) {

            @Override
            public void paint(Graphics g) {
                int x = this.getX();
                int y = this.getY();
                int w = this.getWidth();
                int h = this.getHeight();
                super.paint(g);
                g.drawImage(playIcon, x + w/2 - playIcon.getWidth()/2, y + h/2 - playIcon.getHeight()/2);
                
            }
            
        };
        
        placeholderBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                try {
                    
                    final MediaPlayer mp = new MediaPlayer() {
                        boolean replaced=false;
                        @Override
                        public void paint(Graphics g) {
                            if (!replaced) {
                                int absY = this.getAbsoluteY();
                                int h = this.getHeight();

                                if (absY < 0 || absY + h > Display.getInstance().getDisplayHeight()) {
                                    final MediaPlayer mp = this;
                                    replaced = true;
                                    Display.getInstance().callSerially(new Runnable() {

                                        public void run() {
                                            if (mp.getParent() != null) {

                                                mp.getParent().replace(mp, placeholderBtn, null);
                                                
                                            }
                                        }

                                    });
                                }
                            }
                            super.paint(g); //To change body of generated methods, choose Tools | Templates.
                        }

                        @Override
                        protected Dimension calcPreferredSize() {
                            return new Dimension(placeholderBtn.getWidth(), placeholderBtn.getHeight());
                        }
                        
                        
                         
                    };
                    mp.setAutoplay(true);
                    
                    //mp.setWidth(placeholderBtn.getWidth());
                    //mp.setHeight(placeholderBtn.getHeight());
                    mp.setUIID("MediaPlayer");
                    //mp.setPreferredW(placeholderBtn.getWidth());
                    //mp.setPreferredH(placeholderBtn.getHeight());
                    mp.setDataSource(src);
                    placeholderBtn.getParent().replace(placeholderBtn, mp, null);
                    
                    
                    ScrollListener l = new ScrollListener() {

                        public void scrollChanged(int scrollX, int scrollY, int oldscrollX, int oldscrollY) {
                            mp.getParent().replace(mp, placeholderBtn, null);
                        }
                        
                    };
                    
                    
                    
                    
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });
        
        String uiid = el.getAttribute("uiid");
        if (uiid == null) {
            uiid = "VideoView";
        }
        placeholderBtn.setUIID(uiid);
        
        
        return placeholderBtn;
        
    }
    
}
