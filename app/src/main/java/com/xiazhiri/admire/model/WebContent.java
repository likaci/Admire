package com.xiazhiri.admire.model;

/**
 * Created by liuwencai on 16/3/10.
 */
public class WebContent {

    private final String title;
    private final String href;
    private final String img;
    private final String desc;

    public WebContent(String title, String href, String img, String desc) {

        this.title = title;
        this.href = href;
        this.img = img;
        this.desc = desc;
    }

    public String getTitle() {
        return title;
    }

    public String getHref() {
        return href;
    }

    public String getImg() {
        return img;
    }

    public String getDesc() {
        return desc;
    }
}
