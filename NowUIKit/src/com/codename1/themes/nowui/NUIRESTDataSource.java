/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.codename1.themes.nowui;

import com.codename1.io.ConnectionRequest;
import com.codename1.io.NetworkManager;
import com.codename1.xml.Element;
import com.codename1.xml.XMLParser;
import java.io.ByteArrayInputStream;
import java.io.InputStreamReader;
import java.util.List;

/**
 *
 * @author shannah
 */
public class NUIRESTDataSource extends NUIDataSource {
    private String url;
    private String argPrefix="";
    
    private NUIXMLDataSource lastRequest;
    public NUIRESTDataSource(String url) {
        this.url = url;
    }
    
    @Override
    public List<Article> find(ArticleQuery query, Sort sort) {
        ConnectionRequest req = new ConnectionRequest();
        setup(req, query, sort);
        NetworkManager.getInstance().addToQueueAndWait(req);
        XMLParser parser = new XMLParser();
        Element el = parser.parse(new InputStreamReader(new ByteArrayInputStream(req.getResponseData())));
        NUIXMLDataSource xmlds = new NUIXMLDataSource(el);
        lastRequest = xmlds;
        return xmlds.find(query, sort);
    }

    @Override
    public List<Channel> find(ChannelQuery query, Sort sort) {
        ConnectionRequest req = new ConnectionRequest();
        setup(req, query, sort);
        NetworkManager.getInstance().addToQueueAndWait(req);
        XMLParser parser = new XMLParser();
        Element el = parser.parse(new InputStreamReader(new ByteArrayInputStream(req.getResponseData())));
        NUIXMLDataSource xmlds = new NUIXMLDataSource(el);
        return xmlds.find(query, sort);
    }

    @Override
    public void loadDetails(Article article) {
        
    }
    
    protected void setup(ConnectionRequest req, ArticleQuery query, Sort sort) {
        req.setUrl(this.getArticlesUrl());
        req.setPost(false);
        req.setHttpMethod("GET");
        req.addRequestHeader("Accept", "application/xml");
        if (query.getChannel() != null) {
            req.addArgument(argPrefix+"channel", query.getChannel());
        }
        if (query.getSource() != null) {
            req.addArgument(argPrefix+"source", query.getSource());
        }
        if (query.getStartDate() != null) {
            req.addArgument(argPrefix+"start", String.valueOf(query.getStartDate().getTime()));
        }
        if (query.getEndDate() != null) {
            req.addArgument(argPrefix+"end", String.valueOf(query.getEndDate().getTime()));
        }
        
        
    }
    
    protected void setup(ConnectionRequest req, ChannelQuery query, Sort sort) {
        req.setUrl(this.getChannelsUrl());
        req.setPost(false);
        req.setHttpMethod("GET");
        req.addRequestHeader("Accept", "application/xml");
        if (query.getKeyword() != null) {
            req.addArgument("keyword", query.getKeyword());
        }
        if (query.isPopular()) {
            req.addArgument("popular", "1");
        }
    }
    
    protected void setupDetailsRequest(ConnectionRequest req, Article article) {
        req.setUrl(getArticleDetailsUrl(article));
        req.setHttpMethod("GET");
        req.addRequestHeader("Accept", "application/xml");
        req.setPost(false);
        
    }
    
    public NUIXMLDataSource getLastRequest() {
        return lastRequest;
    }
    
    protected String getArticlesUrl() {
        return url+"/articles";
    }
    
    protected String getChannelsUrl() {
        return url+"/channels";
    }
    
    protected String getArticleDetailsUrl(Article article) {
        return url+"/articles/"+article.getId();
    }
    
    
    
    
}
