package com.lickling.mymusic.bean;

import com.orm.SugarRecord;
import com.orm.dsl.Table;

@Table(name = "list_item")
public class APIListItem extends SugarRecord {
    private String title;
    private String subtitle;
    APIListItem(){

    }

    public APIListItem(String title, String subtitle) {
        this.title = title;
        this.subtitle = subtitle;
    }

    public String getTitle() {
        return title;
    }

    public String getSubtitle() {
        return subtitle;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setSubtitle(String subtitle) {
        this.subtitle = subtitle;
    }
}