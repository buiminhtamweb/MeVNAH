package com.example.toant.googlemap.models;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by toant on 13/06/2018.
 */

public class ListMapLocation {
    @SerializedName("results")
    public ArrayList<MapLocation> results;
    @SerializedName("status")
    public int status;
    @SerializedName("message")
    public String message;

    public ListMapLocation() {
    }

    public ListMapLocation(ArrayList<MapLocation> results, int status, String message) {
        this.results = results;
        this.status = status;
        this.message = message;
    }

    public ArrayList<MapLocation> getResults() {
        return results;
    }

    public void setResults(ArrayList<MapLocation> results) {
        this.results = results;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}