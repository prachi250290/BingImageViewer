package com.testproject.bingimageviewer.manager;

import com.testproject.bingimageviewer.Constants;

import java.util.HashMap;

/**
 * Created by prachi on 11/02/17.
 */

public class WebServiceManager {

    private static WebServiceManager sharedInstance;

    public static WebServiceManager getSharedInstance() {

        if (sharedInstance == null){
            sharedInstance =  new WebServiceManager();
        }
        return sharedInstance;
    }


    public HashMap<String, String> getHeaders() {
        HashMap<String , String> headers = new HashMap<String, String>();
        headers.put(Constants.HEADER_SUBSCRIPTION_KEY, Constants.HEADER_SUBSCRIPTION_KEY_VALUE);
        headers.put(Constants.HEADER_CONTENT_TYPE, Constants.HEADER_CONTENT_TYPE_MULTIPART_FORM);
        return  headers;
    }
}
