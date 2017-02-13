package com.testproject.bingimageviewer;

import android.app.AlertDialog;
import android.content.Context;

/**
 * Created by prachi on 10/02/17.
 */

public class Common {

    //Show alert
    public static void showAlertWithMessage(Context context, String messageTitle, String alertMessage){

        new AlertDialog.Builder(context)
                .setTitle(messageTitle)
                .setMessage(alertMessage)
                .setPositiveButton("OK", null)
                .show();
    }
}
