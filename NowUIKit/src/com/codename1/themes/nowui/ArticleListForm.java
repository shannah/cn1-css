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
import com.codename1.ui.layouts.BorderLayout;
import com.codename1.ui.plaf.UIManager;
import com.codename1.ui.util.Resources;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author shannah
 */
public class ArticleListForm extends NUIForm {
    public ArticleListForm() {
        ArticleListFormTpl tpl = new ArticleListFormTpl(NUI.createTemplateContext());
        
        setLayout(new BorderLayout());
        addComponent(BorderLayout.CENTER, tpl.getRoot());
        
        new NUISideMenu().install(getToolbar());
        
    }
    
}
