/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.codename1.themes.nowui;

import com.codename1.ui.Command;
import com.codename1.ui.Display;
import com.codename1.ui.Form;
import com.codename1.ui.Image;
import com.codename1.ui.Toolbar;
import com.codename1.ui.events.ActionEvent;
import com.codename1.ui.plaf.UIManager;

/**
 *
 * @author shannah
 */
public class NUIForm extends Form {
    protected Command back;
    public NUIForm() {
        super("");
        
        setUIID("NoPaddingForm");
        final Form current = Display.getInstance().getCurrent();
        back = new Command(" ", i("backIconImage")) {
            @Override
            public void actionPerformed(ActionEvent evt) {
                current.showBack();
            }
        };
        setBackCommand(back);
        Toolbar toolbar = new Toolbar();
        this.setToolBar(toolbar);
    }
    
    private Image i(String name) {
        return UIManager.getInstance().getThemeImageConstant(name);
    }
    
    
}
