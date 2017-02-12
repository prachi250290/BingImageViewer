package com.testproject.bingimageviewer;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

public class ImageInfoDataSource {

    // Database fields
    private SQLiteDatabase database;
    private BingSQLiteHelper dbHelper;
    private String[] allColumns = { BingSQLiteHelper.COLUMN_ID,
            BingSQLiteHelper.COLUMN_URL,
            BingSQLiteHelper.COLUMN_CATEGORY,
            BingSQLiteHelper.COLUMN_BRAND,
            BingSQLiteHelper.COLUMN_PRICE,
            BingSQLiteHelper.COLUMN_IMAGE_ID,
            BingSQLiteHelper.COLUMN_DATE
    };


    public ImageInfoDataSource(Context context) {
        dbHelper = new BingSQLiteHelper(context);
    }

    public void open() throws SQLException {
        database = dbHelper.getWritableDatabase();
    }

    public void close() {
        dbHelper.close();
    }

    public int saveImageInfo(ImageInfo imageInfo) {
        ContentValues values = new ContentValues();
        values.put(BingSQLiteHelper.COLUMN_URL, imageInfo.getContentUrl());
        values.put(BingSQLiteHelper.COLUMN_CATEGORY, imageInfo.getCategory());
        values.put(BingSQLiteHelper.COLUMN_BRAND, imageInfo.getBrand());
        values.put(BingSQLiteHelper.COLUMN_PRICE, imageInfo.getPrice());
        values.put(BingSQLiteHelper.COLUMN_IMAGE_ID, imageInfo.getContentUrl());
        values.put(BingSQLiteHelper.COLUMN_DATE, imageInfo.getImageId());
        long insertId = database.insert(BingSQLiteHelper.TABLE_IMAGE_INFO, null,
                values);
        return insertId;
    }
}