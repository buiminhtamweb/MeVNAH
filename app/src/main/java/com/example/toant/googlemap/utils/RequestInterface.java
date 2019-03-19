package com.example.toant.googlemap.utils;

import android.support.annotation.Keep;

import com.example.toant.googlemap.models.ListMapLocation;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Url;

/**
 * Created by toant on 13/06/2018.
 */

public interface RequestInterface {
    @Keep
    @GET
    Observable<ListMapLocation> register(@Url String address);
}
