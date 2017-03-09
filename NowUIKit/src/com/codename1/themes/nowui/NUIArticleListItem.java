/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.codename1.themes.nowui;

import com.codename1.ui.Component;
import com.codename1.ui.Container;
import com.codename1.ui.Display;
import com.codename1.ui.Font;
import com.codename1.ui.List;
import com.codename1.ui.geom.Dimension;
import com.codename1.ui.layouts.BorderLayout;
import com.codename1.ui.list.ListCellRenderer;

/**
 *
 * @author shannah
 */
public class NUIArticleListItem extends Container implements ListCellRenderer<Article> {
    NUIArticleListItemTpl tpl;
    Article article=new Article();
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
    
    public void setArticle(Article article) {
        this.article = article;
        update();
    }
    
    public Article getArticle() {
        return article;
    }

    public void update() {
        if (article != null) {
            if (article.getChannel() == null) {
                tpl.getCategoryButton().setVisible(false);
            } else {
                tpl.getCategoryButton().setText(article.getChannel().getName());
                tpl.getCategoryButton().setVisible(true);
            }
            if (article.getDate() == null) {
                tpl.getDateButton().setVisible(false);
            } else {
                tpl.getDateButton().setText(NUI.formatDateAsAgo(article.getDate()));
                tpl.getDateButton().setVisible(true);
            }
            if (article.getTitle() == null) {
                tpl.getHeadlineLabel().setVisible(false);
            } else {
                tpl.getHeadlineLabel().setText(article.getTitle());
                tpl.getHeadlineLabel().setVisible(true);
            }
            if (article.getSource() == null) {
                tpl.getSourceButton().setVisible(false);
                
            } else {
                tpl.getSourceButton().setText(article.getSource());
                tpl.getSourceButton().setVisible(true);
            }
            
        } else {
            tpl.getCategoryButton().setVisible(false);
            tpl.getDateButton().setVisible(false);
            tpl.getHeadlineLabel().setVisible(false);
            tpl.getSourceButton().setVisible(false);
        }
        
    }


    
    public Component getListCellRendererComponent(List list, Article value, int index, boolean isSelected) {
        //this.setHeight(Display.getInstance().convertToPixels(28, false));
        setArticle(value);
        return this;
    }

    public Component getListFocusComponent(List list) {
       return this;
    }
    
    
}
