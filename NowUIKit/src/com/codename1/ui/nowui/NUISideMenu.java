/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.codename1.ui.nowui;

import com.codename1.ui.nowui.*;
import com.codename1.ui.Button;
import com.codename1.ui.Command;
import com.codename1.ui.Form;
import com.codename1.ui.Image;
import com.codename1.ui.Toolbar;
import com.codename1.ui.events.ActionEvent;
import com.codename1.ui.events.ActionListener;
import com.codename1.ui.plaf.UIManager;
import com.codename1.ui.util.Resources;

/**
 *
 * @author shannah
 */
public class NUISideMenu {
    final Form form;
    
    public NUISideMenu(Form form) {
        this.form = form;
        Toolbar tb = form.getToolbar();
        if (tb == null) {
            tb = new Toolbar();
            form.setToolBar(tb);
        }
    }
    
    public Button addMenuItem(String label, char icon, final ActionListener l) {
        Button b = NUI.apply(new Button(new Command(label) {

            @Override
            public void actionPerformed(ActionEvent evt) {
                l.actionPerformed(evt);
            }
            
        }), NUIFactory.ComponentStyle.SideCommand);
        NUI.setIcon(b, NUIFactory.ComponentStyle.SideCommand, icon);
        Toolbar tb = this.form.getToolbar();
        if (tb != null) {
            tb.addComponentToSideMenu(b, b.getCommand());
        }
        return b;
        
    }
    /*
    public void install(Toolbar toolbar) {
        final Resources theme = NUI.getTheme();
        Button[] commands =  new Button[]{
            new Button(new Command("NEWS", i("newsIconImage")) {
                public void actionPerformed(ActionEvent evt) {
                    
                }
            }),
            new Button(new Command("CHANNELS", i("channelsIconImage")) {
                @Override
                public void actionPerformed(ActionEvent evt) {
                    
                }
            }),
            new Button(new Command("BOOKMARKS", i("bookmarksIconImage")) {
                public void actionPerformed(ActionEvent evt) {
                    //new ArticleListForm(theme).show();
                }
            }),
            
       
            new Button(new Command("OVERVIEW", i("overviewIconImage")) {
                public void actionPerformed(ActionEvent evt) {
                    
                }
            }),
            
            new Button(new Command("CALENDAR", i("calendarIconImage")) {
                public void actionPerformed(ActionEvent evt) {
                    
                }
            }),
        
            new Button(new Command("PROFILE", i("profileIconImage")) {
                public void actionPerformed(ActionEvent evt) {
                    //new ArticleListForm(theme).show();
                }
            }),
            new Button(new Command("WIDGETS", i("widgetsIconImage")) {
                public void actionPerformed(ActionEvent evt) {
                    //new ArticleListForm(theme).show();
                }

            }),
        
            new Button(new Command("SETTINGS", i("settingsIconImage")) {
                public void actionPerformed(ActionEvent evt) {
                    
                }
            })
        };
        
        
        for (Button btn : commands) {
            btn.setGap(btn.getStyle().getFont().getHeight());
            btn.setUIID("SideCommand");
            toolbar.addComponentToSideMenu(btn, btn.getCommand());
            
            
        }
        
        
        
    }
    
    private Image i(String name) {
        return UIManager.getInstance().getThemeImageConstant(name);
    }
    */
}
