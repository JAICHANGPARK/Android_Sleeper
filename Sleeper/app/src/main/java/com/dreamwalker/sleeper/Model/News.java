package com.dreamwalker.sleeper.Model;

/**
 * Created by 2E313JCP on 2017-11-17.
 */

public class News {

    String title;
    String originallink;
    String description;
    String pubDate;

    public News() {
    }

    public News(String title, String originallink, String description, String pubDate) {
        this.title = title;
        this.originallink = originallink;
        this.description = description;
        this.pubDate = pubDate;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getOriginallink() {
        return originallink;
    }

    public void setOriginallink(String originallink) {
        this.originallink = originallink;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPubDate() {
        return pubDate;
    }

    public void setPubDate(String pubDate) {
        this.pubDate = pubDate;
    }
}
