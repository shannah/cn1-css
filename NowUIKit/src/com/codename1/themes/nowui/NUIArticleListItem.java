/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.codename1.themes.nowui;

import com.codename1.ui.Component;
import com.codename1.ui.Container;
import com.codename1.ui.Font;
import com.codename1.ui.Label;
import com.codename1.ui.layouts.BorderLayout;
import com.codename1.ui.plaf.Style;
import java.util.Date;

/**
 *
 * @author shannah
 */
public class NUIArticleListItem extends Container {
    NUIArticleListItemTpl tpl;
    public NUIArticleListItem() {
        setUIID("NoPadding");
        tpl = new NUIArticleListItemTpl(NUI.createTemplateContext());
        Font font = tpl.getCategoryButton().getUnselectedStyle().getFont();
        font = font.derive(NUI.mm2px(1.5), font.getStyle());
        for (Component c : new Component[]{tpl.getCategoryButton(), tpl.getDateButton(), tpl.getSourceButton()}) {
            c.getAllStyles().setFont(font);
        }
        
        setLayout(new BorderLayout());
        addComponent(BorderLayout.CENTER, tpl.getRoot());
    }
    
    public void setHeadline(String headline) {
        tpl.getHeadlineLabel().setText(headline);
    }
    
    public void setCategory(String category) {
        tpl.getCategoryButton().setText(category);
    }
    
    public void setDate(Date date) {
        
    }
        
    
    public void setSource(String source) {
        tpl.getSourceButton().setText(source);
    }
}
