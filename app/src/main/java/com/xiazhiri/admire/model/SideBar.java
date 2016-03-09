package com.xiazhiri.admire.model;

/**
 * Created by liuwencai on 16/3/9.
 */
public class SideBar {

    private String title;
    private String href;
    private String level;

    public SideBar(String title, String href, String level) {
        this.title = title;
        this.href = href;
        this.level = level;
    }

    public String getTitle() {
        return title;
    }

    public String getHref() {
        return href;
    }

    public String getLevel() {
        return level;
    }

}
