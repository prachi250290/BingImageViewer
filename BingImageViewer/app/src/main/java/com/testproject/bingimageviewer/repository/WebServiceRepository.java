package com.testproject.bingimageviewer.model;

/**
 * Created by prachi on 12/02/17.
 */

public class WebServiceRepository {

    private WebServiceRepository sharedInstance;
    private ImageInfoDataSource datasource;

    public WebServiceRepository getSharedInstance(Context context) {

        datasource = new ImageInfoDataSource(context);
        if(sharedInstance == null) {
            sharedInstance = new WebServiceRepository();
        }

        return sharedInstance;

    }

    public void saveImageInfo(Context context, ImageInfo imageInfo) {
        datasource.open();
        datasource.saveImageInfo(imageInfo);
        datasource.close();
    }


}