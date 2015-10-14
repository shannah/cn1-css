/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.codename1.themes.nowui;

import com.codename1.ui.Button;
import com.codename1.ui.Component;
import com.codename1.ui.Container;
import com.codename1.ui.layouts.BorderLayout;

/**
 *
 * @author shannah
 */
public class ChannelButton extends Container {
    
    private String buttonUIID;
    private Button button;
    
    public ChannelButton() {
        setLayout(new BorderLayout());
    }
    
    public void setButtonUIID(String uiid) {
        buttonUIID = uiid;
        if (this.button != null) {
            this.button.setUIID(uiid);
        }
    }

    @Override
    public void addComponent(Component cmp) {
        System.out.println("Adding component... uiid is "+cmp.getUIID());
        
        if (cmp instanceof Button) {
            super.addComponent(BorderLayout.SOUTH, cmp);
            this.setLeadComponent(cmp);
            this.button = (Button)cmp;
            if (buttonUIID != null) {
                this.button.setUIID(buttonUIID);
            }
        } 
    }
    
}
