/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.codename1.ui.nowui;

import com.codename1.components.SpanLabel;
import com.codename1.ui.Button;
import com.codename1.ui.Container;
import com.codename1.ui.Image;
import com.codename1.ui.Label;
import com.codename1.ui.events.DataChangedListener;
import com.codename1.ui.layouts.BorderLayout;
import com.codename1.ui.layouts.BoxLayout;
import com.codename1.ui.list.ListModel;
import com.codename1.ui.nowui.NUIFactory.ComponentStyle;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author shannah
 */
public class NUIList extends Container {
    
    private ComponentStyle getForegroundStyle(ComponentStyle style) {
        
        String uiid = this.getUIID();
        if (uiid.indexOf("Dark") != -1) {
            return style.asLight();
        } else if (uiid.indexOf("Light") != -1) {
            return style.asDark();
        }
        return style;
    }
    
    public static final String CATEGORY="category";
    public static final String SUBJECT="subject";
    public static final String THUMBNAIL="thumbnail";
    public static final String BODY="body";
    
    public static enum ListType {
        
        /**
         * A comments list.
         * 
         * <p><img src="https://cloud.githubusercontent.com/assets/2677562/12468048/2d4eebc0-bf95-11e5-932a-5986c270bde6.png"/></p>
         */
        Comments,
        /**
         * An article list.
         * 
         * <p>With thumbnail:</p>
         * 
         * <p><img src="https://cloud.githubusercontent.com/assets/2677562/12468048/2d4eebc0-bf95-11e5-932a-5986c270bde6.png"/></p>
         * 
         * <p>Without thumbnail:</p>
         * <p><img src="https://cloud.githubusercontent.com/assets/2677562/12468084/71ff2000-bf95-11e5-8dd9-2e8e95ad5492.png"/></p>
         */
        Articles,
        
        /**
         * A list to show a history or timeline.  As shown here:
         * 
         * <p><img src="https://cloud.githubusercontent.com/assets/2677562/12468101/9700ecee-bf95-11e5-918d-0f2835b6fb11.png"/></p>
         */
        History
        
    }
    
    
    private final ListType listType;
    private ListModel<Map> model;
    private boolean modelChanged = true;
    private final DataChangedListener modelListener = new DataChangedListener() {

        public void dataChanged(int type, int index) {
            
        }
        
    };
    
    public NUIList(ListType type, ListModel<Map> data) {
        this.listType = type;
        setModel(data);
        setLayout(new BoxLayout(BoxLayout.Y_AXIS));
        setScrollableY(true);
        
        
    }

    public void setModel(ListModel model) {
        
        this.model = model;
        
    }
    
    @Override
    public void revalidate() {
        /*
        super.removeAll();
        int len = dataSource.getSize();
        for (int i=0; i<len; i++) {
            Map itemData = dataSource.getItemAt(i);
            NUIListItem itemView = new NUIListItem();
            itemView.setModel(itemData);
            add(itemView);
        }
                */
        super.revalidate();
    }
    
    
    
    public class NUIListItem extends Container {
        private ArrayList<Button> buttons = new ArrayList<Button>();
        private Button categoryButton = new Button();
        private Label subject = new Label(), thumbnail = new Label();
        private SpanLabel body = new SpanLabel();
        private final Map model = new HashMap();
        
        private Container buttonsPanel = new Container();
        
        private boolean showCategoryButton,
                showSubject,
                showThumbnail,
                showIcon,
                showBody,
                showCategoryHeading;
        
        
        public NUIListItem() {
            switch (listType) {
                case Comments: {
                    setLayout(new BorderLayout());
                    add(BorderLayout.WEST, thumbnail);
                    add(BorderLayout.NORTH, BorderLayout.west(subject).add(BorderLayout.EAST, buttonsPanel));
                    add(BorderLayout.CENTER, body);
                    break;
                }
                
                case Articles : {
                    setLayout(new BorderLayout());
                    
                    add(BorderLayout.CENTER, thumbnail);
                    add(BorderLayout.NORTH, subject);
                    add(BorderLayout.SOUTH, BorderLayout.east(categoryButton).add(BorderLayout.WEST, buttonsPanel));
                    break;
                }
                
                case History : {
                    setLayout(new BorderLayout());
                    add(BorderLayout.WEST, thumbnail);
                    add(BorderLayout.CENTER, BoxLayout.encloseY(
                            BorderLayout.west(categoryButton).add(BorderLayout.EAST, buttonsPanel),
                            subject,
                            body
                    ));
                    break;
                }
            }
                    
        }
        
        public void setModel(Map model) {
            this.model.clear();
            this.model.putAll(model);
        }
        
        public void getModel(Map model) {
            model.clear();
            model.putAll(this.model);
        }
        
        public Map getModel() {
            Map out = new HashMap();
            getModel(out);
            return out;
        }
        
        public void update() {
            
            if (model.containsKey(CATEGORY)) {
                categoryButton.setText((String)model.get(CATEGORY));
                showCategoryButton = true;
                
            }  else {
                showCategoryButton = false;
            }
            
            if (model.containsKey(SUBJECT)) {
                subject.setText((String)model.get(SUBJECT));
                showSubject = true;
            } else {
                showSubject = false;
            }
            
            if (model.containsKey(THUMBNAIL)) {
                thumbnail.setIcon((Image)model.get(THUMBNAIL));
                showThumbnail = true;
            } else {
                showThumbnail = false;
            }
            
            
            if (model.containsKey(BODY)) {
                body.setText((String)model.get(BODY));
                showBody = true;
            } else {
                showBody = false;
            }
            
            categoryButton.setVisible(showCategoryButton);
            subject.setVisible(showSubject);
            thumbnail.setVisible(showThumbnail);
            body.setVisible(showBody);
        }
    
        
        
    }

    
}
