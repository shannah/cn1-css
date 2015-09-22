/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.codename1.demos.css;

import com.codename1.ui.Component;
import com.codename1.ui.Container;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author shannah
 */
public class UIIDTransformer {
    private final Map<String,String> map = new HashMap<String,String>();
    
    public UIIDTransformer(String[] map) {
        int len = map.length;
        for (int i=0; i<len; i+=2) {
           this.map.put(map[i], map[i+1]);
        }
    }
    
    public void transform(Component c) {
        String uiid = c.getUIID();
        if (map.containsKey(uiid)) {
            c.setUIID(map.get(uiid));
        }
        if (c instanceof Container) {
            for (Component child : ((Container)c)){
                transform(child);
            }
        }
    }
}
