package com.dmi.books.booksshop;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class AppUtils {
    private static final String TAG = "AppUtils";

    private static final String APP_PREF_NAME = "book_pref_utils";

    private static final String USER_NAME = "user_name";
    private static final String USER_PASSWORD = "user_password";
    private static final String NETWORK_HOST = "network_host";

    public static final boolean DEBUG = true;

    public static String getUserName(Context context) {
        SharedPreferences pref = context.getSharedPreferences(
                APP_PREF_NAME, 0);
        return pref.getString(USER_NAME, "usertest");
    }

    public static void setUserName(Context context, String userName) {
        SharedPreferences pref = context.getSharedPreferences(
                APP_PREF_NAME, 0);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString(USER_NAME, userName).commit();
    }


    public static String getUserPassword(Context context) {
        SharedPreferences pref = context.getSharedPreferences(
                APP_PREF_NAME, 0);
        return pref.getString(USER_PASSWORD, "secret");
    }

    public static void setUserPassword(Context context, String userPassword) {
        SharedPreferences pref = context.getSharedPreferences(
                APP_PREF_NAME, 0);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString(USER_PASSWORD, userPassword).commit();
    }


    public static String getNetworkHost(Context context) {
        SharedPreferences pref = context.getSharedPreferences(
                APP_PREF_NAME, 0);
        return pref.getString(NETWORK_HOST, "");
    }


    /*
 *
 *
 *
 */
    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = cm.getActiveNetworkInfo();
        // if no network is available networkInfo will be null, otherwise check
        // if we are connected
        if (networkInfo != null && networkInfo.isConnected()) {
            return true;
        }
        return false;
    }

    public static boolean serviceErrorMessage(final Context context, final String message, final boolean finish) {
        AlertDialog alertDialog = new AlertDialog.Builder(context).create();
        alertDialog.setTitle("Error");
        alertDialog.setMessage(message);
        // alertDialog.setIcon(R.drawable.tick);
        alertDialog.setButton(
                context.getResources().getString(android.R.string.ok),
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                        if(finish)
                            ((Activity)context).finish();
                    }
                });
        alertDialog.show();
        return false;
    }

}
