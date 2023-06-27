package com.lickling.mymusic.ui.local;

public class ListItem {
    private String title;
    private String subtitle;
    private String subtitle2;
    private String subtitle3;

    public ListItem(String title, String subtitle,String subtitle2,String subtitle3) {
        this.title = title;
        this.subtitle = subtitle;
        this.subtitle2 = subtitle2;
        this.subtitle3 = subtitle3;
    }

    public String getTitle() {
        return title;
    }

    public String getSubtitle() {
        return subtitle;
    }
    public String getSubtitle2() {
        return subtitle2;
    }
    public String getSubtitle3() {
        return subtitle3;
    }
}
