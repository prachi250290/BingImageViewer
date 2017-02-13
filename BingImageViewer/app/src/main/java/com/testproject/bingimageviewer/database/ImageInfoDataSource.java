package com.testproject.bingimageviewer.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.testproject.bingimageviewer.model.ImageInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by prachi on 11/02/17.
 */

public class ImageInfoDataSource {

    // Database fields
    private SQLiteDatabase database;
    private BingSQLiteHelper dbHelper;
    private String[] allColumns = { BingSQLiteHelper.COLUMN_ID,
            BingSQLiteHelper.COLUMN_NAME_IMAGE_ID,
            BingSQLiteHelper.COLUMN_NAME_IMAGE_URL,
            BingSQLiteHelper.COLUMN_NAME_IMAGE_CATEGORY,
            BingSQLiteHelper.COLUMN_NAME_IMAGE_BRAND,
            BingSQLiteHelper.COLUMN_NAME_IMAGE_PRICE,
            BingSQLiteHelper.COLUMN_NAME_IMAGE_DATE

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

    public long saveImageInfo(ImageInfo imageInfo) {
        ContentValues values = new ContentValues();
        values.put(BingSQLiteHelper.COLUMN_NAME_IMAGE_BRAND, imageInfo.getBrand());
        long insertId = database.insert(BingSQLiteHelper.TABLE_NAME, null,
                values);
        return insertId;
    }


    public List<ImageInfo> getAllImageInfo() {
        List<ImageInfo> imageInfoList = new ArrayList<>();

        Cursor cursor = database.query(BingSQLiteHelper.TABLE_NAME,
                allColumns, null, null, null, null, null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            ImageInfo imageInfo = cursorToImageInfo(cursor);
            imageInfoList.add(imageInfo);
            cursor.moveToNext();
        }
        // make sure to close the cursor
        cursor.close();
        return imageInfoList;
    }

    private ImageInfo cursorToImageInfo(Cursor cursor) {
        ImageInfo imageInfo = new ImageInfo();
        imageInfo.setImageId(cursor.getString(0));
        return imageInfo;
    }

}
