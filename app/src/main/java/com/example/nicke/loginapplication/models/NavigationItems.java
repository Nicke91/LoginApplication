package com.example.nicke.loginapplication.models;

/**
 * Created by Nicke on 7/21/2017.
 */

public class NavigationItems {
    String title;
    String subtitle;
    Integer icon;

    public NavigationItems(String title, String subtitle, Integer icon) {
        this.title = title;
        this.subtitle = subtitle;
        this.icon = icon;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSubtitle() {
        return subtitle;
    }

    public void setSubtitle(String subtitle) {
        this.subtitle = subtitle;
    }

    public Integer getIcon() {
        return icon;
    }

    public void setIcon(Integer icon) {
        this.icon = icon;
    }
}
