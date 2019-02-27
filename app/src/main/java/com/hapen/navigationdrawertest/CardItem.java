package com.hapen.navigationdrawertest;

import java.util.Date;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.LinearLayout;

public class CardItem {
    private int organizationLogo;
    private String organizationName;
    private String eventCategory;
    private String eventTitle;
    private String eventDate;
    private String eventDescription;
    private int eventFlyer;
    private boolean zoomOut =  false;

    public CardItem(int orgLogo, String orgName, String category, String title, String date,
                    String description, int flyer) {
        organizationLogo = orgLogo;
        organizationName = orgName;
        eventCategory = category;
        eventTitle = title;
        eventDate = date;
        eventDescription = description;
        eventFlyer = flyer;
    }

    public int getOrganizationLogo() {
        return organizationLogo;
    }

    public String getOrganizationName() {
        return organizationName;
    }

    public String getEventCategory() {
        return eventCategory;
    }

    public String getEventTitle() {
        return eventTitle;
    }

    public String getEventDate() {
        return eventDate;
    }

    public String getEventDescription() {
        return eventDescription;
    }

    public int getEventFlyer() {
        return eventFlyer;
    }
}
