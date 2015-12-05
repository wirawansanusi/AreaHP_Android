package com.wirawansanusi.areahp.model;

/**
 * Created by wirawansanusi on 12/4/15.
 */
public class Contacts {

    private int mIcon;
    private String mTitle;
    private String mType;
    private String mValue;

    public Contacts(int icon, String title, String type, String value) {
        mIcon = icon;
        mTitle = title;
        mType = type;
        mValue = value;
    }

    public int getIcon() {
        return mIcon;
    }

    public String getTitle() {
        return mTitle;
    }

    public String getType() {
        return mType;
    }

    public String getValue() {
        return mValue;
    }
}