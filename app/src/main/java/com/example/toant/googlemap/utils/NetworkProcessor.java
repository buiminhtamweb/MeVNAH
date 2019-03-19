package com.example.toant.googlemap.utils;

import okhttp3.CacheControl;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.text.LoginFilter;
import android.text.TextUtils;
import android.util.Log;

import com.example.toant.googlemap.R;
import com.example.toant.googlemap.helper.GoogleMapParserHelper;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Headers;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
/**
 * Created by toant on 31/05/2018.
 */

public class NetworkProcessor extends OkHttpClient implements Constants {

    private static final String TAG = NetworkProcessor.class.getSimpleName();
    private static ProcessDialog processDialog;
    private static OkHttpClient client;

    public static void UploadImage(final Context context, File file, final int processId, final DownloadCallback downloadCallback){
        MediaType MEDIA_TYPE_JPEG = MediaType.parse("image/jpeg");

        Log.e(TAG, "UploadImage: File_name->" + file.getName() );
        RequestBody requestBody  = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("file", file.getName()+".png", RequestBody.create(MEDIA_TYPE_JPEG, file))
                .build();
        Request request = new Request.Builder()
                .url(API_INSERT_IMAGE)
                .post(requestBody).build();

        Log.i(TAG, "URL_REQUEST: " + API_INSERT_IMAGE);
        client = new OkHttpClient();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                processDialog.dismiss();
                if(e != null){
                    String message = e.getMessage();
                    if(!TextUtils.isEmpty(message)){
                        if(message.equals("timeout")){
                            showError(context, processId, context.getString(R.string.msg_connection_time_out_please_try_again), downloadCallback);
                            return;
                        }
                    }
                }
                Log.i(TAG, "DOWNLOAD_ONFAILURE: " + e.getMessage());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                processDialog.dismiss();
                if (response.isSuccessful()) {
                    try {
                        final String json = response.body().string();
                        jsonParser(context, json, processId, downloadCallback, null,false);
                        Log.i(TAG, "DOWNLOAD_RESPONSE: " + json);
                    } catch (Exception e) {
                        Log.i(TAG, "DOWNLOAD_PARSER_ERROR: " + e.toString() + " (Dữ liệu trả về không giống với đối tượng truyền vào)");
                    }
                } else {
                    Log.i(TAG, "DOWNLOAD_ONRESPONE: " + response.message() + " (Truyền sai tham số)");
                }
            }
        });
    }


    public static <T> T GET(@NonNull final Context context, @NonNull final int processId, @NonNull String url, final Class<T> type, boolean showDialog, @NonNull final DownloadCallback downloadCallback, final boolean isGoogleMap) {
        try {

            if (!Utils.isConnectionAvailable(context)) {
                showError(context, processId, context.getString(R.string.msg_sorry_no_internet), downloadCallback);
                return null;
            }
            processDialog = new ProcessDialog(context);
            if (showDialog) processDialog.show();
            Log.i(TAG, "URL_REQUEST: " + url);
            client = new OkHttpClient();

            Map<String, String> map = new HashMap<>();
            map.put("Content-Type", "application/json");
            map.put("Accept", "application/json");

            Headers headers = Headers.of(map);

            final Request request = new Request.Builder()
                    .headers(headers)
                    .url(url)
                    .build();
            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    processDialog.dismiss();
                    if(e != null){
                        String message = e.getMessage();
                        if(!TextUtils.isEmpty(message)){
                            if(message.equals("timeout")){
                                showError(context, processId, context.getString(R.string.msg_connection_time_out_please_try_again), downloadCallback);
                                return;
                            }
                        }
                    }
                    showError(context, processId, context.getString(R.string.msg_sorry_an_has_occurred), downloadCallback);

                    Log.i(TAG, "DOWNLOAD_ONFAILURE: " + e.getMessage());
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    processDialog.dismiss();
                    if (response.isSuccessful()) {
                        try {
                            final String json = response.body().string();
                            Log.i(TAG, "DOWNLOAD_RESPONSE: " + json);
                            jsonParser(context, json, processId, downloadCallback, type,isGoogleMap);
                        } catch (Exception e) {
                            Log.i(TAG, "DOWNLOAD_PARSER_ERROR: " + e.toString() + " (Dữ liệu trả về không giống với đối tượng truyền vào)");
                        }
                    } else {
                        showError(context, processId, context.getString(R.string.msg_sorry_an_has_occurred), downloadCallback);
                        Log.i(TAG, "DOWNLOAD_ONRESPONE: " + response.message() + " (Truyền sai tham số)");
                    }
                }
            });
            return null;
        } catch (Exception e) {
            showError(context, processId, context.getString(R.string.msg_sorry_an_has_occurred), downloadCallback);
            e.printStackTrace();
            return null;
        }
    }

    private static void showError(final Context context, final int processId, final String msg, final DownloadCallback downloadCallback) {
        ((Activity) context).runOnUiThread(new Runnable() {
            @Override
            public void run() {
                downloadCallback.downloadError(processId, msg);
            }
        });
        if (processDialog != null) {
            processDialog.dismiss();
        }
    }

    private static <T> T jsonParser(Context context, final String json, final int processId, final DownloadCallback downloadCallback, final Class<T> type, boolean isGoogleMap) {
        try {
            if(isGoogleMap){
                ((Activity) context).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        GoogleMapParserHelper googleMapParserHelper = new GoogleMapParserHelper();
                        ArrayList<String> arrayList = googleMapParserHelper.parseDirections(json);
                        for (String a :
                                arrayList) {
                            Log.i(TAG, "run: " + a.toString());
                        }
                        downloadCallback.downloadSuccess(processId, arrayList);
                    }
                });
            }else {
                final JSONObject mainData = new JSONObject(json);
//                int errorCode = mainData.has(STATUS_CODE) ? mainData.opt(STATUS_CODE) instanceof String ? 1 : mainData.getInt(STATUS_CODE) : -1;
                int errorCode = -1;
                if(mainData.has(STATUS_CODE)){
                    if(mainData.opt(STATUS_CODE) instanceof String){
                        errorCode = 1;
                    }else {
                        errorCode = mainData.getInt(STATUS_CODE);
                    }
                }
//                if(processId == ID_API_GOOGLE_DISTANCEMATRIX){
                    Log.i(TAG, "jsonParser 123213: "+ errorCode);
//                }
                final String message = mainData.has(MESSAGE) ? mainData.getString(MESSAGE) : "";
                final Object data = mainData.isNull(RESULT) ? null : mainData.get(RESULT);
                final String fileName = mainData.has("file_name") ? mainData.getString("file_name") : "";

                if(processId == ID_API_GOOGLE_DISTANCEMATRIX){
                    ((Activity) context).runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            downloadCallback.downloadSuccess(processId, new Gson().fromJson(json.toString(), type));
                            return;
                        }
                    });
                }

                if (errorCode == 1) {//JSON trả về đúng định dạng có message, errorCode, data
                    if (data != null && data instanceof JSONObject && type != null) {
                        ((Activity) context).runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                downloadCallback.downloadSuccess(processId, new Gson().fromJson(data.toString(), type));
                            }
                        });
                    } else if (data != null && data instanceof JSONArray && type != null) {
                        JSONArray array = (JSONArray) data;
                        int lengh = array.length();
                        final ArrayList<Object> listObject = new ArrayList<>();
                        for (int i = 0; i < lengh; i++) {
                            JSONObject object = array.getJSONObject(i);
                            listObject.add(new Gson().fromJson(object.toString(), type));
                        }
                        ((Activity) context).runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                downloadCallback.downloadSuccess(processId, listObject);
                                Log.d(TAG, "List size: " + listObject.size());
                            }
                        });
                    } else if(!fileName.equals("") && type == null){
                        ((Activity) context).runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                downloadCallback.downloadSuccess(processId, fileName);
                            }
                        });
                    } else {
                        ((Activity) context).runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                downloadCallback.downloadSuccess(processId, message);
                            }
                        });
                    }
                } else if (errorCode == -1 && !isGoogleMap) {//JSON trả về không đúng định dạng message, errorCode, data
                    if (mainData != null && mainData instanceof JSONObject && type != null) {
                        ((Activity) context).runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                downloadCallback.downloadSuccess(processId, new Gson().fromJson(mainData.toString(), type));
                            }
                        });
                    } else {
                        showError(context, processId, context.getString(R.string.msg_sorry_an_has_occurred), downloadCallback);
                        Log.i(TAG, "PARSER_JSON: " + "JSON trả về rỗng hoặc không truyền đối tượng để phân tích.");
                    }
                }
                else {
                    showError(context, processId, message, downloadCallback);
                }
            }
        } catch (Exception e) {
            showError(context, processId, context.getString(R.string.msg_sorry_an_has_occurred), downloadCallback);
            e.printStackTrace();
            Log.i(TAG, "PARSER_JSON: " + "JSON trả về không đúng định dạng");
        }
        return null;
    }
}
