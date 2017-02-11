package com.testproject.bingimageviewer.webservice;


import com.testproject.bingimageviewer.model.SearchResult;

import java.util.Map;

import retrofit2.Call;
import retrofit2.http.POST;
import retrofit2.http.QueryMap;

/**
 * Created by prachi on 11/02/17.
 */
public interface ApiInterface {

    @POST("/bing/v5.0/images/search")
    Call<SearchResult> searchImages(@QueryMap Map<String, String> options);

}
