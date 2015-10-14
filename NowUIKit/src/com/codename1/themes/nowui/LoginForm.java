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
import com.codename1.ui.Label;
import com.codename1.ui.TextArea;
import com.codename1.ui.TextField;
import com.codename1.ui.layouts.BorderLayout;
import com.codename1.ui.util.Resources;
import java.util.HashMap;

/**
 *
 * @author shannah
 */
public class LoginForm extends Form {
    LoginFormTpl tpl;
    public LoginForm(Resources theme) {
        super("");
        HashMap context = new HashMap();
        context.put("res", theme);
        tpl = new LoginFormTpl(context);
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
