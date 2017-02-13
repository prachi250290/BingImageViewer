package com.testproject.bingimageviewer.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by prachi on 11/02/17.
 */

public class BingSQLiteHelper extends SQLiteOpenHelper{

    public static final String TABLE_NAME = "ImageTable";
    public static final String COLUMN_NAME_IMAGE_ID = "imageid";
    public static final String COLUMN_NAME_IMAGE_URL = "imageurl";
    public static final String COLUMN_NAME_IMAGE_CATEGORY = "category";
    public static final String COLUMN_NAME_IMAGE_BRAND = "brand";
    public static final String COLUMN_NAME_IMAGE_PRICE = "price";
    public static final String COLUMN_NAME_IMAGE_DATE = "date";
    public static final String COLUMN_ID = "_id";


    private static final String DATABASE_NAME = "imagedatabase.db";
    private static final int DATABASE_VERSION = 1;

    // Database creation sql statement
    private static final String DATABASE_CREATE = "create table "
            + TABLE_NAME + "( " + COLUMN_ID
            + " integer primary key autoincrement, "
            + COLUMN_NAME_IMAGE_ID + " text not null"
            + COLUMN_NAME_IMAGE_URL + " text not null"
            + COLUMN_NAME_IMAGE_CATEGORY + " text not null"
            + COLUMN_NAME_IMAGE_BRAND + " text not null"
            + COLUMN_NAME_IMAGE_PRICE + " real not null"
            + COLUMN_NAME_IMAGE_DATE + " integer not null"
            +");";

    public BingSQLiteHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase database) {
        database.execSQL(DATABASE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }


}
