package com.hapen.navigationdrawertest;

import java.util.Date;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.LinearLayout;

public class CardItem {
    String organizationLogo;
   String organizationName;
     String eventCategory;
    String eventTitle;
    String eventDate;
    String eventDescription;
    String eventFlyer;



    String category;
    String date;
    String description;
    String organization;
    String title;
    String url;
    boolean zoomOut =  false;/**
    private String organizationLogo;
    private String organizationName;
    private String eventCategory;
    private String eventTitle;
    private String eventDate;
    private String eventDescription;
    private String eventFlyer;
    private boolean zoomOut =  false;
    **/

    public CardItem(){}

    public CardItem(String orgLogo, String orgName, String category, String title, String date,
                    String description, String flyer) {
        this.organizationLogo = orgLogo;
        this.organization = orgName;
        this.eventCategory = category;
        this.eventTitle = title;
        this.eventDate = date;
        this.eventDescription = description;
        this.eventFlyer = flyer;
    }

    public void setOrganizationLogo(String organizationLogo) {
        this.organizationLogo = organizationLogo;
    }

    public void setOrganizationName(String organization) {
        this.organization = organization;
    }

    public void setEventCategory(String eventCategory) {
        this.eventCategory = eventCategory;
    }

    public void setEventTitle(String eventTitle) {
        this.eventTitle = eventTitle;
    }

    public void setEventDate(String eventDate) {
        this.eventDate = eventDate;
    }

    public void setEventDescription(String eventDescription) {
        this.eventDescription = eventDescription;
    }

    public void setEventFlyer(String eventFlyer) {
        this.eventFlyer = eventFlyer;
    }

    public String getOrganizationLogo() {
        return organizationLogo;
    }

    public String getOrganizationName() {
        return organization;
    }

    public String getEventCategory() {
        return category;
    }

    public String getEventTitle() {
        return title;
    }

    public String getEventDate() {
        return date;
    }

    public String getEventDescription() {
        return description;
    }

    public String getEventFlyer() {
        return url;
    }
}
