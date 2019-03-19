package com.example.toant.googlemap.utils;

/**
 * Created by toant on 31/05/2018.
 */

public interface DownloadCallback {
    void downloadSuccess(int processId, Object data);
    void downloadError(int processId, String msg);
}
