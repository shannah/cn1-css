/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.codename1.themes.nowui;

import com.codename1.components.Progress;
import com.codename1.ui.Container;
import com.codename1.ui.Label;
import com.codename1.ui.Slider;
import com.codename1.ui.layouts.BorderLayout;

/**
 *
 * @author shannah
 */
public class NUIProgress extends Container {
    private Label nameLabel, percentLabel;
    private Slider progress;
    
    public NUIProgress() {
        setUIID("NUIProgress");
        nameLabel = new Label();
        nameLabel.setUIID("NUIProgressLabel");
        percentLabel = new Label();
        percentLabel.setUIID("NUIProgressPercentLabel");
        progress = new Slider();
        
        
        //progress.setIncrements(100);
        
        setLayout(new BorderLayout());
        addComponent(BorderLayout.WEST, nameLabel);
        addComponent(BorderLayout.EAST, percentLabel);
        addComponent(BorderLayout.SOUTH, progress);
    }
    
    public void setProgress(int percent) {
        percentLabel.setText(percent + "%");
        progress.setProgress(percent);
    }
    
    public void setCategory(String category) {
        nameLabel.setText(category);
    }
}
