/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.codename1.themes.nowui;

import java.util.List;

/**
 *
 * @author shannah
 */
public class NUIDemoDataSource extends NUIDataSource {

    NUIXMLDataSource xmlds;
    String url="https://raw.githubusercontent.com/shannah/cn1-css/master/NowUIKit/demo-datasource.xml";
    @Override
    public List<Article> find(ArticleQuery query, Sort sort) {
        if (xmlds == null) {
            NUIRESTDataSource restds = new NUIRESTDataSource(url) {

                @Override
                protected String getArticlesUrl() {
                    return url;
                }

                @Override
                protected String getChannelsUrl() {
                    return url;
                }

                @Override
                protected String getArticleDetailsUrl(Article article) {
                    return url;
                }
                
                
                
            };
            List<Article> articles = restds.find(query, sort);
            xmlds = restds.getLastRequest();
            return articles;
        }
        return xmlds.find(query, sort);
    }

    @Override
    public List<Channel> find(ChannelQuery query, Sort sort) {
        if (xmlds == null) {
            NUIRESTDataSource restds = new NUIRESTDataSource(url) {

                @Override
                protected String getArticlesUrl() {
                    return url;
                }

                @Override
                protected String getChannelsUrl() {
                    return url;
                }

                @Override
                protected String getArticleDetailsUrl(Article article) {
                    return url;
                }
                
                
                
            };
            List<Channel> channels = restds.find(query, sort);
            xmlds = restds.getLastRequest();
            return channels;
        }
        return xmlds.find(query, sort);
    }

    @Override
    public void loadDetails(Article article) {
        xmlds.loadDetails(article);
    }
    
}
