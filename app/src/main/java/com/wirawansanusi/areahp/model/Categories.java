package com.wirawansanusi.areahp.model;

import java.io.Serializable;

/**
 * Created by wirawansanusi on 11/6/15.
 */
public class Categories implements Serializable {
    private int mId;
    private int mParentId;
    private String mTitle;
    private String mSlug;

    public Categories(int id, int parentId, String title, String slug) {
        mId = id;
        mParentId = parentId;
        mTitle = title;
        mSlug = slug;
    }

    public int getId() {
        return mId;
    }

    public void setId(int id) {
        mId = id;
    }

    public int getParentId() {
        return mParentId;
    }

    public void setParentId(int parentId) {
        mParentId = parentId;
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String title) {
        mTitle = title;
    }

    public String getSlug() {
        return mSlug;
    }

    public void setSlug(String slug) {
        mSlug = slug;
    }
}
