/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.codename1.demos.css;

import com.codename1.ui.Button;
import com.codename1.ui.Command;
import com.codename1.ui.Component;
import com.codename1.ui.Container;
import com.codename1.ui.Display;
import com.codename1.ui.Form;
import com.codename1.ui.Label;
import com.codename1.ui.TextField;
import com.codename1.ui.events.ActionEvent;
import com.codename1.ui.layouts.BorderLayout;
import com.codename1.ui.layouts.BoxLayout;
import com.codename1.ui.layouts.FlowLayout;
import com.codename1.ui.layouts.GridLayout;
import com.codename1.ui.plaf.UIManager;
import com.codename1.ui.util.Resources;
import java.io.IOException;

/**
 *
 * @author shannah
 */
public class SignUpForm extends Form {
    static Resources res;
    
    public static void init() {
        if (res == null) {
            try {
                res = Resources.openLayered("/SignUpForm.css");
            } catch (IOException ex) {
                throw new RuntimeException(ex.getMessage());
            }
            //UIManager.getInstance().
            UIManager.getInstance().addThemeProps(res.getTheme("Theme"));
        }
    }
    
    public SignUpForm() {
        super("Sign Up");
        this.setUIID("SignUpForm");
        
        setLayout(new BorderLayout());
        Container north = new Container(new FlowLayout(Component.CENTER));
        north.setUIID("SignUpNorth");
        
        Button photoButton = new Button(res.getImage("profile-photo-button.png"));
        photoButton.setUIID("PhotoButton");
        north.addComponent(photoButton);
        
        this.addComponent(BorderLayout.NORTH, north);
        
        Container center = new Container(new BoxLayout(BoxLayout.Y_AXIS));
        center.setUIID("SignUpCenter");
        
        Container row1 = new Container(new GridLayout(1,2));
        TextField firstName = new TextField();
        firstName.setUIID("SignUpField");
        firstName.setHint("First Name");
        firstName.getHintLabel().setUIID("SignupFieldHint");
        TextField lastName = new TextField();
        lastName.setUIID("SignUpField");
        lastName.setHint("Last Name");
        lastName.getHintLabel().setUIID("SignupFieldHint");
        row1.addComponent(firstName);
        row1.addComponent(lastName);
        center.addComponent(row1);
        center.setScrollableY(true);
        
        TextField email = new TextField();
        email.setUIID("SignUpField");
        center.addComponent(email);
        email.setHint("Email Address");
        email.getHintLabel().setUIID("SignupFieldHint");
        
        TextField password = new TextField();
        password.setUIID("SignUpField");
        password.setConstraint(TextField.PASSWORD);
        password.setHint("Choose Password");
        password.getHintLabel().setUIID("SignupFieldHint");
        center.addComponent(password);
        
        Container row4 = new Container(new BorderLayout());
        Label code = new Label("+1");
        code.setUIID("SignUpLabel");
        row4.addComponent(BorderLayout.WEST, code);
        
        TextField phoneNumber = new TextField();
        phoneNumber.setUIID("SignUpField");
        phoneNumber.setHint("Phone Number");
        phoneNumber.getHintLabel().setUIID("SignupFieldHint");
        row4.addComponent(BorderLayout.CENTER, phoneNumber);
        
        center.addComponent(row4);
        
        this.addComponent(BorderLayout.CENTER, center);
        
        Button getStarted = new Button("Get Started", res.getImage("right_arrow.png"));
        getStarted.setGap(getStarted.getStyle().getFont().getHeight());
        getStarted.setUIID("SignUpButton");
        getStarted.setTextPosition(Component.LEFT);
        
        this.addComponent(BorderLayout.SOUTH, getStarted);
        this.addCommand(new Command("Done") {

            @Override
            public void actionPerformed(ActionEvent evt) {
                
            }
            
        });
        
        final Form backForm = Display.getInstance().getCurrent();
        
        this.setBackCommand(new Command("", res.getImage("back-arrow.png")) {

            @Override
            public void actionPerformed(ActionEvent evt) {
                backForm.showBack();
            }
            
        });
          
        
    }
}
