/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.codename1.themes.nowui;

import com.codename1.ui.Container;
import com.codename1.ui.layouts.BorderLayout;
import com.codename1.ui.list.DefaultListModel;
import java.util.List;

/**
 *
 * @author shannah
 */
public class ArticleListForm extends NUIForm {
    ArticleListFormTpl tpl;
    public ArticleListForm(List<Article> articles) {
        tpl = new ArticleListFormTpl(NUI.createTemplateContext());
        setLayout(new BorderLayout());
        addComponent(BorderLayout.CENTER, tpl.getRoot());
        
        new NUISideMenu().install(getToolbar());
        setArticles(articles);
        
    }
    
    public void setArticles(List<Article> articles) {
        Container articleList = tpl.getArticleList();
        for (Article article : articles) {
            NUIArticleListItem item = new NUIArticleListItem();
            item.setArticle(article);
            articleList.addComponent(item);
        }
    }
    
}
