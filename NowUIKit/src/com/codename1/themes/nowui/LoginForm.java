/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.codename1.themes.nowui;

import com.codename1.ui.Button;
import com.codename1.ui.Component;
import com.codename1.ui.Container;
import com.codename1.ui.Form;
import com.codename1.ui.Graphics;
import com.codename1.ui.Label;
import com.codename1.ui.TextArea;
import com.codename1.ui.TextField;
import com.codename1.ui.geom.Dimension;
import com.codename1.ui.geom.GeneralPath;
import com.codename1.ui.layouts.BorderLayout;
import com.codename1.ui.util.Resources;
import java.util.HashMap;

/**
 *
 * @author shannah
 */
public class LoginForm extends Form {
    LoginFormTpl tpl;
    public LoginForm() {
        super("");
        final Resources theme = NUI.getTheme();
        
        
        tpl = new LoginFormTpl(NUI.createTemplateContext());
        TextField usernameField = tpl.getUsernameField();
        Label usernameHintLabel = usernameField.getHintLabel();
        usernameHintLabel.setUIID("NUITextFieldHint");
        
        usernameField.setHintIcon(theme.getImage("Username-icon.png"));
        usernameHintLabel.setGap(10);
        
        TextField passwordField = tpl.getPasswordField();
        passwordField.setHintIcon(theme.getImage("Password-icon.png"));
        passwordField.getHintLabel().setUIID("NUITextFieldHint");
        passwordField.getHintLabel().setGap(10);
        passwordField.setConstraint(TextArea.PASSWORD);
        setUIID("LoginForm");
        customizeCoreUIIDs(this);
        
        setLayout(new BorderLayout());
        addComponent(BorderLayout.CENTER, tpl.getRoot());
        /*
        tpl.getTestPath().addComponent(new Component() {

            @Override
            protected Dimension calcPreferredSize() {
                return new Dimension(200,200);
            }

            @Override
            public void paint(Graphics g) {
                super.paint(g); 
                g.translate(getX(), getY());
            

            int halfDiagonal = 70;
            int cx = 100;
            int cy = 100;
            int r = 10;

            GeneralPath gp = new GeneralPath();
            gp.moveTo(cx, cy - halfDiagonal);
            gp.lineTo(cx + halfDiagonal, cy);
            gp.lineTo(cx, cy + halfDiagonal);
            gp.lineTo(cx - halfDiagonal, cy);
            gp.lineTo(cx, cy - halfDiagonal);
            gp.arc(cx - r, cy - r, r + r, r + r, 0, 2 * Math.PI);
            gp.closePath();
            g.setColor(0xFFbdc3c7);
            g.setAlpha(80);
            g.fillShape(gp);

            g.setAlpha(255);

            g.translate(-getX(), -getY());
            }
            
            
            
        });
                */
    }
    
    private void customizeCoreUIIDs(Container parent) {
        for (Component c : parent) {
            if ("TitleArea".equals(c.getUIID())) {
                c.setUIID("NUIHiddenTitleArea");
            } else if ("StatusBar".equals(c.getUIID())) {
                c.setUIID("NUIHiddenStatusBar");
            }
            if (c instanceof Container) {
                customizeCoreUIIDs((Container)c);
            }
        }
    }
    
    public TextField getUsernameField() {
        return tpl.getUsernameField();
    }
    
    public TextField getPasswordField() {
        return tpl.getPasswordField();
    }
    
    public Button getSubmitButton() {
        return tpl.getSubmitButton();
    }
    
    public Button getHelpButton() {
        return tpl.getHelpButton();
    }
    
    public Button getSignupButton() {
        return tpl.getSignupButton();
    }
}
