package com.testproject.bingimageviewer.model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by prachi on 11/02/17.
 */

public class SearchResult {

    @SerializedName("value")
    private ArrayList<ImageDetail> imageList;

    public ArrayList<ImageDetail> getImageList() {
        return imageList;
    }

    public void setImageList(ArrayList<ImageDetail> imageList) {
        this.imageList = imageList;
    }
}
