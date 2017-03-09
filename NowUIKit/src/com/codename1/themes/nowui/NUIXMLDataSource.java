/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.codename1.themes.nowui;

import com.codename1.io.Log;
import com.codename1.l10n.DateFormat;

import com.codename1.l10n.ParseException;
import com.codename1.l10n.SimpleDateFormat;
import com.codename1.xml.Element;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;




/**
 *
 * @author shannah
 */
public class NUIXMLDataSource extends NUIDataSource  {
    private Element root;
    private List<DateFormat> dateFormats=new ArrayList<DateFormat>();
    
    private Map<String,Channel> channelCache = new HashMap<String,Channel>();
    private Map<String,Article> articleCache = new HashMap<String,Article>();
    

    public NUIXMLDataSource(Element root) {
        this.root = root;
        this.dateFormats.add(new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"));
        //this.dateFormats.add(new SimpleDateFormatExt("yyyy-MM-dd'T'HH:mm:ssXXX"));
        this.dateFormats.add(new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssz"));
        this.dateFormats.add(new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ"));
        
        this.dateFormats.add(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));
        this.dateFormats.add(new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss z"));
        this.dateFormats.add(new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss Z"));
        this.dateFormats.add(new SimpleDateFormat("MM/dd/yyyy"));
        this.dateFormats.add(new SimpleDateFormat("yyyy-MM-dd"));
        
    }
    
    public void addDateFormat(DateFormat fmt) {
        dateFormats.add(fmt);
    }
    
    public Date parseDate(String date) {
        for (DateFormat fmt : dateFormats) {
            try {
                return fmt.parse(date);
            } catch (Exception ex) {
                
            }
        }
        return null;
    }
    
    @Override
    public List<Article> find(ArticleQuery query, Sort sort) {
        long startTime = query.getStartDate() == null ? 0 : query.getStartDate().getTime();
        long endTime = query.getEndDate() == null ? 0 : query.getEndDate().getTime();
        List<Article> out = new ArrayList<Article>();
        List<Element> channels = root.getChildrenByTagName("channel");
        for (Element channel : channels) {
            if (query.getChannel() != null && !query.getChannel().equals(channel.getAttribute("id"))) {
                continue;
            } 
            List<Element> articles = channel.getChildrenByTagName("article");
            if (articles != null) {
                for (Element article : articles) {
                    
                    Date date = parseDate(article.getAttribute("date"));
                    
                    if (date != null && startTime != 0 && date.getTime() < startTime) {
                        continue;
                    }
                    if (date != null && endTime != 0 && date.getTime() > endTime) {
                        continue;
                    }
                    
                    out.add(getArticle(article, channel));
                }
            }
        }
        
        return out;
    }

    @Override
    public List<Channel> find(ChannelQuery query, Sort sort) {
        List<Channel> out = new ArrayList<Channel>();
        for (Element channel : (List<Element>)root.getChildrenByTagName("channel")) {
            out.add(getChannel(channel));
        }
        return out;
    }
    
    
    Article getArticle(Element articleEl, Element channelEl) {
        if (articleCache.containsKey(articleEl.getAttribute("id"))) {
            return articleCache.get(articleEl.getAttribute("id"));
        } else {
            Article article = createArticle(articleEl, channelEl);
            articleCache.put(articleEl.getAttribute("id"), article);
            return article;
        }
    }
    
    Article createArticle(Element articleEl, Element channelEl) {
        final Article out = new Article();
        out.setChannel(getChannel(channelEl));
        out.setDate(parseDate(articleEl.getAttribute("date")));
        
        out.setId(articleEl.getAttribute("id"));
        out.setSource(articleEl.getAttribute("source"));
        out.setTitle(articleEl.getAttribute("title"));
        out.setMetaData(new MetaData() {

            @Override
            public void load() {
                if (!isLoaded()) {
                    loadDetails(out);
                    setLoaded(true);
                }
            }
            
        });
        System.out.println("Creating article with source "+out.getSource());
        return out;
    }
    
    Channel getChannel(Element channelEl) {
        if (channelCache.containsKey(channelEl.getAttribute("id"))) {
            return channelCache.get(channelEl.getAttribute("id"));
        } else {
            Channel channel = createChannel(channelEl);
            channelCache.put(channelEl.getAttribute("id"), channel);
            return channel;
                    
        }
    }
    
    Channel createChannel(Element channelEl) {
        Channel out = new Channel();
        out.setId(channelEl.getAttribute("id"));
        out.setName(channelEl.getAttribute("name"));
        out.setThumbnail(null);
        out.setFollowers(channelEl.getAttributeAsInt("followers", 0));
        MetaData md = new MetaData() {

            @Override
            public void load() {
                
            }
            
        };
        
        md.setLoaded(true);
        md.setDateLoaded(new Date());
        
        return out;
    }

    @Override
    public void loadDetails(Article article) {
        Element foundEl = null;
        for (Element channel : (List<Element>)root.getChildrenByTagName("channel")) {
            for (Element articleEl : (List<Element>)channel.getChildrenByTagName("article")) {
                if (article.getId().equals(articleEl.getAttribute("id"))) {
                    foundEl = articleEl;
                    break;
                }
            }
        }
        
        if (foundEl != null) {
            article.setContent(foundEl);
        }
    }
    
    
    
}
