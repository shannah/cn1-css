/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.codename1.themes.nowui;

import com.codename1.components.xmlview.DefaultXMLViewKit;
import com.codename1.ui.Button;
import com.codename1.ui.Command;
import com.codename1.ui.Dialog;
import com.codename1.ui.Display;
import com.codename1.ui.Form;
import com.codename1.ui.Image;
import com.codename1.ui.Toolbar;
import com.codename1.ui.events.ActionEvent;
import com.codename1.ui.events.ActionListener;
import com.codename1.ui.layouts.BorderLayout;
import com.codename1.ui.plaf.UIManager;
import com.codename1.ui.util.Resources;
import com.codename1.util.Callback;
import com.codename1.xml.Element;
import java.util.HashMap;

/**
 *
 * @author shannah
 */
public class ArticleForm extends Form {
    ArticleFormTpl tpl;
    
    public ArticleForm(Resources theme, String title, String url) {
        super("");
        setUIID("NoPaddingForm");
        HashMap context = new HashMap();
        context.put("res", theme);
        context.put("ui", UIManager.getInstance());
        tpl = new ArticleFormTpl(context);
        tpl.getBody().setTheme(theme);
        tpl.getHeadline().setTextUIID(tpl.getHeadline().getUIID()+"Text");
        DefaultXMLViewKit kit = new DefaultXMLViewKit();
        kit.install(tpl.getBody());
        tpl.getBody().load(url, new Callback<Element>() {

            public void onSucess(Element value) {
                ArticleForm.this.revalidate();
            }

            public void onError(Object sender, Throwable err, int errorCode, String errorMessage) {
                
            }
            
        });
        
        Button sourceButton = tpl.getSourceButton();
        //sourceButton.setIcon(i("articleSourceIconImage"));
        sourceButton.setGap(sourceButton.getStyle().getFont().getHeight()/2);
        
        Button dateButton = tpl.getDateButton();
        dateButton.setIcon(i("articleDateIconImage"));
        dateButton.setGap(sourceButton.getStyle().getFont().getHeight()/2);
        
        tpl.getNextStoryButton().setIcon(i("articleArrowRightImage"));
        
        tpl.getNextStoryButton().setText(" ");
        tpl.getNextStoryTitle().setTextUIID(tpl.getNextStoryTitle().getUIID()+"Text");
        
        setLayout(new BorderLayout());
        tpl.getRoot().setScrollableY(true);
        tpl.getCategoryButton().addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                Dialog.show("Framerate: ", Display.getInstance().getFrameRate()+"", "OK", null);
            }
        });
        addComponent(BorderLayout.CENTER, tpl.getRoot());
        
        final Form current = Display.getInstance().getCurrent();
        Command back = new Command(" ", i("backIconImage")) {
            @Override
            public void actionPerformed(ActionEvent evt) {
                current.showBack();
            }
        };
        setBackCommand(back);
        Toolbar toolbar = new Toolbar();
        this.setToolBar(toolbar);
        toolbar.addCommandToRightBar(new Command(" ", i("articleBookmarkIconImage")));
        toolbar.addCommandToRightBar(new Command(" ", i("articleShareIconImage")));
        
        toolbar.addCommandToLeftBar(back);
        toolbar.addCommandToLeftBar(new Command(" ", i("articleCommentsIconImage")));
        toolbar.addCommandToLeftBar(new Command(" ", i("articleTextIconImage")));
        
        
        
        
    }
    
    private Image i(String name) {
        return UIManager.getInstance().getThemeImageConstant(name);
    }
    
}
