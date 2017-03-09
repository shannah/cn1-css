/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.codename1.themes.nowui;

import com.codename1.ui.Toolbar;
import com.codename1.ui.layouts.BorderLayout;
import com.codename1.ui.util.Resources;

/**
 *
 * @author shannah
 */
public class CalendarForm extends NUIForm {
    CalendarFormTpl tpl;
    public CalendarForm(Resources theme) {
        
        tpl = new CalendarFormTpl(NUI.createTemplateContext());
        setLayout(new BorderLayout());
        addComponent(BorderLayout.CENTER, tpl.getRoot());
        new NUISideMenu().install(getToolbar());

    }
    
}
