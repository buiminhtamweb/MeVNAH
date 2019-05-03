package com.example.toant.googlemap.utils;

import android.content.Context;
import android.content.DialogInterface;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.util.Log;

import com.example.toant.googlemap.R;
import com.google.android.gms.common.util.IOUtils;
import com.google.android.gms.common.util.Strings;
import com.google.android.gms.maps.model.LatLng;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

/**
 * Created by toant on 31/05/2018.
 */

public class Utils implements Constants{



    private static final String DIRECTION_API  = "AIzaSyAmITju60o8HgOrCT61z0qlyz9Dqc1alhQ";

    private static final String TAG = Utils.class.getSimpleName();
    public static int PROCESS = 0;


    // just for image becasue api is too fucking stupid
    public static ArrayList<String> ConvertStringToArrayListImage(String photoListLocation){
        ArrayList<String> arrayList = new ArrayList<>();
        String listStr = photoListLocation.replaceAll("[\\[\\]\"]", "");
        if(listStr.contains("")){
            // có nhiều phần tử
            String strarray[] =listStr.split(",") ;
            arrayList.addAll(Arrays.asList(strarray));
        }else {
            // có 1 phần tử
            arrayList.add(listStr);
        }
        return arrayList;
    }

    public static String getDirectionsUrl(LatLng start, LatLng end) {
        double latitude = start.latitude;
        double longitude = start.longitude;
        double end_latitude = end.latitude;
        double end_longitude = end.longitude;
        StringBuilder googleDirectionsUrl = new StringBuilder(API_GOOGLE_DIRECTION);
        googleDirectionsUrl.append("origin="+latitude+","+longitude);
        googleDirectionsUrl.append("&destination="+end_latitude+","+end_longitude);
//        googleDirectionsUrl.append("sensor=false");
        googleDirectionsUrl.append("&key="+DIRECTION_API);
        return googleDirectionsUrl.toString();
    }

    public static byte[] compress(String data) {
        ByteArrayOutputStream bos = new ByteArrayOutputStream(data.length());
        GZIPOutputStream gzip = null;
        try {
            gzip = new GZIPOutputStream(bos);
            gzip.write(data.getBytes());
            gzip.close();
            byte[] compressed = bos.toByteArray();
            bos.close();
            return compressed;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String decompress(final byte[] compressed) throws IOException {
        ByteArrayInputStream bis = new ByteArrayInputStream(compressed);
        GZIPInputStream gis = new GZIPInputStream(bis);
        byte[] bytes = IOUtils.toByteArray(gis);
        return new String(bytes, "UTF-8");
    }

    public static double convertStringToDouble(String string){
        try {
            return Double.parseDouble(string);
        }catch (Exception e){
            return  - 1;
        }

    }

    public static void showMessageOKCancel(Context context,String message, DialogInterface.OnClickListener okListener) {
        new AlertDialog.Builder(context)
                .setMessage(message)
                .setPositiveButton("OK", okListener)
                .setNegativeButton("Cancel", null)
                .create()
                .show();
    }

    public static void showMessageOK(Context context,String message, DialogInterface.OnClickListener okListener, Boolean cancel) {
        new AlertDialog.Builder(context)
                .setTitle(context.getString(R.string.app_name))
                .setMessage(message)
                .setCancelable(cancel)
                .setPositiveButton("OK",okListener)
                .create()
                .show();
    }

    public static boolean isLocationServiceEnable(Context context){
        LocationManager lm = (LocationManager)context.getSystemService(Context.LOCATION_SERVICE);
        Boolean gps_enabled = false;
        try {
            gps_enabled = lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
        }catch (Exception ignored){
            Log.i(TAG, "EXCETION: " + ignored.getMessage());
        }

        return gps_enabled;

//        if(!gps_enabled){
//            AlertDialog.Builder dialog = new AlertDialog.Builder(this);
//            dialog.setMessage(getResources().getString(R.string.msg_gps_network_not_enabled));
//            dialog.setPositiveButton(getResources().getString(R.string.open_location_settings), new DialogInterface.OnClickListener() {
//                @Override
//                public void onClick(DialogInterface paramDialogInterface, int paramInt) {
//                    Intent myIntent = new Intent( Settings.ACTION_LOCATION_SOURCE_SETTINGS);
//                    startActivity(myIntent);
//                }
//            });
//            dialog.setNegativeButton(getString(R.string.btnCancel), new DialogInterface.OnClickListener() {
//
//                @Override
//                public void onClick(DialogInterface paramDialogInterface, int paramInt) {
//                    // TODO Auto-generated method stub
//
//                }
//            });
//            dialog.show();
//        }
    }

    public static boolean isConnectionAvailable(Context context) {
        try {
            final ConnectivityManager connMgr = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            final NetworkInfo wifi = connMgr.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
            final NetworkInfo mobile = connMgr.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
            if (wifi.isAvailable() && wifi.isConnected()) {
                return true;
            } else return mobile.isAvailable() && mobile.isConnected();

        } catch (Exception e) {
            //e.printStackTrace();
        }
        return false;
    }

    /*Kiểm tra email có đúng định dạng hay không*/
    public static boolean isEmailValid(String email) {
        String expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
        Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }
}
