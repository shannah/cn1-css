/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.codename1.themes.nowui;

import com.codename1.ui.Image;
import com.codename1.xml.Element;
import java.util.Date;

/**
 *
 * @author shannah
 */
public class Article {
    
    private MetaData metaData;
    
    private String id;
    private Element content;
    private String source;
    private Channel channel;
    private String title;
    private Date date;
    private Image thumbnail;
    private Image header;

    /**
     * @return the content
     */
    public Element getContent() {
        return content;
    }

    /**
     * @param content the content to set
     */
    public void setContent(Element content) {
        this.content = content;
    }

    /**
     * @return the source
     */
    public String getSource() {
        return source;
    }

    /**
     * @param source the source to set
     */
    public void setSource(String source) {
        this.source = source;
    }

    /**
     * @return the channel
     */
    public Channel getChannel() {
        return channel;
    }

    /**
     * @param channel the channel to set
     */
    public void setChannel(Channel channel) {
        this.channel = channel;
    }

    /**
     * @return the date
     */
    public Date getDate() {
        return date;
    }

    /**
     * @param date the date to set
     */
    public void setDate(Date date) {
        this.date = date;
    }

    /**
     * @return the header
     */
    public Image getHeader() {
        return header;
    }

    /**
     * @param header the header to set
     */
    public void setHeader(Image header) {
        this.header = header;
    }

    /**
     * @return the title
     */
    public String getTitle() {
        return title;
    }

    /**
     * @param title the title to set
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * @return the thumbnail
     */
    public Image getThumbnail() {
        return thumbnail;
    }

    /**
     * @param thumbnail the thumbnail to set
     */
    public void setThumbnail(Image thumbnail) {
        this.thumbnail = thumbnail;
    }

    /**
     * @return the id
     */
    public String getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * @return the metaData
     */
    public MetaData getMetaData() {
        return metaData;
    }

    /**
     * @param metaData the metaData to set
     */
    public void setMetaData(MetaData metaData) {
        this.metaData = metaData;
    }
}
