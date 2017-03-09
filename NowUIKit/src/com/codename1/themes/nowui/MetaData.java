/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.codename1.themes.nowui;

import java.util.Date;

/**
 *
 * @author shannah
 */
public abstract class MetaData {
    private boolean loaded;
    private Date dateLoaded;
    public abstract void load();

    /**
     * @return the loaded
     */
    public boolean isLoaded() {
        return loaded;
    }

    /**
     * @param loaded the loaded to set
     */
    public void setLoaded(boolean loaded) {
        this.loaded = loaded;
    }

    /**
     * @return the dateLoaded
     */
    public Date getDateLoaded() {
        return dateLoaded;
    }

    /**
     * @param dateLoaded the dateLoaded to set
     */
    public void setDateLoaded(Date dateLoaded) {
        this.dateLoaded = dateLoaded;
    }
    
}
