/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.codename1.themes.nowui;

import com.codename1.ui.Component;
import com.codename1.ui.Container;
import com.codename1.ui.Form;
import com.codename1.ui.TextField;
import com.codename1.ui.layouts.BorderLayout;
import com.codename1.ui.util.Resources;
import java.util.HashMap;

/**
 *
 * @author shannah
 */
public class SignupForm extends Form {
    SignupFormTpl tpl;
    public SignupForm() {
        super("");
        final Resources theme = NUI.getTheme();
        customizeCoreUIIDs(this);
        setUIID("NUISignupForm");
        
        tpl = new SignupFormTpl(NUI.createTemplateContext());
        
        TextField nameField = tpl.getNameField();
        nameField.setHintIcon(theme.getImage("Name-icon.png"));
        
        TextField emailField = tpl.getEmailField();
        emailField.setHintIcon(theme.getImage("Email-icon.png"));
        
        
        TextField passwordField = tpl.getPasswordField();
        passwordField.setHintIcon(theme.getImage("Password-icon.png"));
        
        for (TextField tf : new TextField[]{nameField, emailField, passwordField}) {
            tf.getHintLabel().setUIID("NUITextFieldHint");
            tf.getHintLabel().setGap(10);
        }
        
        setLayout(new BorderLayout());
        addComponent(BorderLayout.CENTER, tpl.getRoot());
        
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
}
