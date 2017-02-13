package com.testproject.bingimageviewer.repository;

import android.content.Context;

import com.testproject.bingimageviewer.database.ImageInfoDataSource;
import com.testproject.bingimageviewer.model.ImageInfo;

import java.util.List;

/**
 * Created by prachi on 11/02/17.
 */

public class ImageInfoRepository {

    private Context context;
    private ImageInfoDataSource datasource;

    public ImageInfoRepository(Context context) {

        this.context = context;
        datasource = new ImageInfoDataSource(context);

    }

    public List<ImageInfo> getImageInfo() {
        datasource.open();
        List<ImageInfo> imageInfoList = datasource.getAllImageInfo();
        datasource.close();
        return imageInfoList;
    }

    public long saveImageInfo (ImageInfo info) {
        datasource.open();
        long insertID = datasource.saveImageInfo(info);
        datasource.close();
        return insertID;
    }


}
