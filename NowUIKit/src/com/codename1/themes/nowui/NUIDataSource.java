/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.codename1.themes.nowui;

import java.util.Date;
import java.util.List;

/**
 *
 * @author shannah
 */
public abstract class NUIDataSource {
    
    public static enum Sort {
        NEWEST,
        OLDEST,
        POPULAR
    }
    
    public static class ArticleQuery {
        private String channel;
        private String source;
        private Date startDate;
        private Date endDate;

        /**
         * @return the channel
         */
        public String getChannel() {
            return channel;
        }

        /**
         * @param channel the channel to set
         */
        public void setChannel(String channel) {
            this.channel = channel;
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
         * @return the startDate
         */
        public Date getStartDate() {
            return startDate;
        }

        /**
         * @param startDate the startDate to set
         */
        public void setStartDate(Date startDate) {
            this.startDate = startDate;
        }

        /**
         * @return the endDate
         */
        public Date getEndDate() {
            return endDate;
        }

        /**
         * @param endDate the endDate to set
         */
        public void setEndDate(Date endDate) {
            this.endDate = endDate;
        }
        
    }
    
    public static class ChannelQuery {
        private boolean popular;
        private String keyword;

        /**
         * @return the popular
         */
        public boolean isPopular() {
            return popular;
        }

        /**
         * @param popular the popular to set
         */
        public void setPopular(boolean popular) {
            this.popular = popular;
        }

        /**
         * @return the keyword
         */
        public String getKeyword() {
            return keyword;
        }

        /**
         * @param keyword the keyword to set
         */
        public void setKeyword(String keyword) {
            this.keyword = keyword;
        }
    }
    
    public abstract List<Article> find(ArticleQuery query, Sort sort);
    
    public abstract List<Channel> find(ChannelQuery query, Sort sort);
    
    public abstract void loadDetails(Article article);
    
}
