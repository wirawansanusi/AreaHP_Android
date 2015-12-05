package com.wirawansanusi.areahp.model;

import android.graphics.Bitmap;

import java.io.Serializable;

/**
 * Created by wirawansanusi on 11/9/15.
 */
public class Products implements Serializable {

    private int mProductId;
    private String mTitle;
    private String mCategory;
    private String mPrice;
    private String mURLString;
    private Bitmap mThumbnailImage;

    public Products(int productId, String title, String category, String price, String URLString) {
        mProductId = productId;
        mTitle = title;
        mCategory = category;
        mPrice = price;
        mURLString = URLString;
    }

    public int getProductId() {
        return mProductId;
    }

    public void setProductId(int productId) {
        mProductId = productId;
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String title) {
        mTitle = title;
    }

    public String getCategory() {
        return mCategory;
    }

    public void setCategory(String category) {
        mCategory = category;
    }

    public String getPrice() {
        return mPrice;
    }

    public void setPrice(String price) {
        mPrice = price;
    }

    public String getURLString() {
        return mURLString;
    }

    public void setURLString(String URLString) {
        mURLString = URLString;
    }

    public Bitmap getThumbnailImage() {
        return mThumbnailImage;
    }

    public void setThumbnailImage(Bitmap thumbnailImage) {
        mThumbnailImage = thumbnailImage;
    }
}
