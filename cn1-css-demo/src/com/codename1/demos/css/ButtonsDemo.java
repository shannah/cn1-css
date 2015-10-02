/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.codename1.demos.css;

import com.codename1.ui.Button;
import com.codename1.ui.Command;
import com.codename1.ui.Display;
import com.codename1.ui.Form;
import com.codename1.ui.events.ActionEvent;

/**
 *
 * @author shannah
 */
public class ButtonsDemo extends Form {
    public ButtonsDemo(){
        super("Buttons Demo");
        final Form current = Display.getInstance().getCurrent();
        setBackCommand(new Command("Back") {
            public void actionPerformed(ActionEvent evt) {
                current.showBack();
            }
        });
        
        Button blueButton = new Button("A Blue Rounded Button");
        blueButton.setUIID("BlueRoundedButton");
        addComponent(blueButton);
    }
}
