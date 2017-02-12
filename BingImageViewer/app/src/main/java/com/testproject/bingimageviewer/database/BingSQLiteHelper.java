package com.testproject.bingimageviewer;

import android.app.AlertDialog;
import android.content.Context;

/**
 * Created by prachi on 12/02/17.
 */

public class  BingSQLiteHelper extends SQLiteOpenHelper {

    public static final String TABLE_IMAGE_INFO = "imageinfo";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_URL = "url";
    public static final String COLUMN_IMAGE_ID = "imageId";
    public static final String COLUMN_CATEGORY = "caegory";
    public static final String COLUMN_BRAND = "brand";
    public static final String COLUMN_PRICE = "price";
    public static final String COLUMN_DATE = "date";


    private static final String DATABASE_NAME = "imageinfo.db";
    private static final int DATABASE_VERSION = 1;

    // Database creation sql statement
    private static final String DATABASE_CREATE = "create table "
            + TABLE_IMAGE_INFO + "( "
            + COLUMN_ID + " integer primary key autoincrement, "
            + COLUMN_URL + " text not null"
            + COLUMN_IMAGE_ID + " text not null"
            + COLUMN_CATEGORY + " text not null"
            + COLUMN_BRAND + " text not null"
            + COLUMN_PRICE + " real not null"
            + COLUMN_DATE + " integer not null"
            +");";

    public MySQLiteHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase database) {
        database.execSQL(DATABASE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_IMAGE_INFO);
        onCreate(db);
    }

}
