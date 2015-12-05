package com.wirawansanusi.areahp.model;

import com.orm.SugarRecord;

/**
 * Created by wirawansanusi on 11/11/15.
 */
public class ProductDetail extends SugarRecord<ProductDetail> {

    private int mProductId;
    private String mTitle;
    private String mCategory;
    private String mThumbnailURLString;
    private String mWeight;
    private String mWarranty;
    private String mAdditional;
    private String mPrice;

    public ProductDetail() {

    }

    public ProductDetail(int productId, String title, String category, String thumbnailURLString, String weight, String warranty, String additional, String price) {
        mProductId = productId;
        mTitle = title;
        mCategory = category;
        mThumbnailURLString = thumbnailURLString;
        mWeight = weight;
        mWarranty = warranty;
        mAdditional = additional;
        mPrice = price;
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

    public String getThumbnailURLString() {
        return mThumbnailURLString;
    }

    public void setThumbnailURLString(String thumbnailURLString) {
        mThumbnailURLString = thumbnailURLString;
    }

    public String getWeight() {
        return mWeight;
    }

    public void setWeight(String weight) {
        mWeight = weight;
    }

    public String getWarranty() {
        return mWarranty;
    }

    public void setWarranty(String warranty) {
        mWarranty = warranty;
    }

    public String getAdditional() {
        return mAdditional;
    }

    public void setAdditional(String additional) {
        mAdditional = additional;
    }

    public String getPrice() {
        return mPrice;
    }

    public void setPrice(String price) {
        mPrice = price;
    }
}
