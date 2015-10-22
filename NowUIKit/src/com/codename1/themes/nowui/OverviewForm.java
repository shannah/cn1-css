/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.codename1.themes.nowui;

import com.codename1.ui.layouts.BorderLayout;

/**
 *
 * @author shannah
 */
public class OverviewForm extends NUIForm {
    OverviewFormTpl tpl;
    public OverviewForm() {
        setUIID("NUIOrangeForm");
        tpl = new OverviewFormTpl(NUI.createTemplateContext());
        setLayout(new BorderLayout());
        addComponent(BorderLayout.CENTER, tpl.getRoot());
        new NUISideMenu().install(getToolbar());
    }
    
}
