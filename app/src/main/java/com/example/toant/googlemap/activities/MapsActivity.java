package com.example.toant.googlemap.activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.example.toant.googlemap.R;
import com.example.toant.googlemap.models.CityDistrict;
import com.example.toant.googlemap.models.EventBusInfo;
import com.example.toant.googlemap.models.ListMapLocation;
import com.example.toant.googlemap.models.MapLocation;
import com.example.toant.googlemap.utils.NetworkProcessor;
import com.example.toant.googlemap.utils.ProcessDialog;
import com.example.toant.googlemap.utils.RequestInterface;
import com.example.toant.googlemap.utils.Utils;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.Gson;
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by toant on 31/05/2018.
 */

public class MapsActivity extends BaseMapActivity {

    private static final String TAG = "MapsActivity";
    private static DecimalFormat format1 = new DecimalFormat("#.###");
    //test time - perfomance
    long tStart = 0, tEnd = 0;
    private GoogleMap mGoogleMap;
    private SupportMapFragment mapFrag;
    private LocationRequest mLocationRequest;
    private GoogleApiClient mGoogleApiClient;
    private Location mLastLocation;
    private MarkerOptions mCurrLocationMarker;
    private Button btnNearbyPlaces;
    private ArrayList<MapLocation> mapLocations;
    private ArrayList<MapLocation> mapLocationsNearby;
    private LocationCallback mLocationCallback;
    private FusedLocationProviderClient mFusedLocationClient;
    private ProcessDialog processDialog;
    private CompositeDisposable mCompositeDisposable;

    @Override
    public void initUI() {
        setUp();
        mapLocations = new ArrayList<>();
        mapLocationsNearby = new ArrayList<>();
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        processDialog = new ProcessDialog(this);
        mLocationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                if (locationResult == null) {
                    Log.i(TAG, "onLocationResult: NULL");
                    return;
                }
                for (Location location : locationResult.getLocations()) {
                    // Update UI with location data
                    Log.i(TAG, "onLocationResult: " + location);

                    onMapReadyClient(location);
                }
                mFusedLocationClient.removeLocationUpdates(mLocationCallback);
                processDialog.hide();
            }
        };
        Log.i(TAG, "initUI: ");
    }

    @SuppressLint("ResourceType")
    @Override
    public int getLayoutResourceId() {
        return R.layout.activity_maps;
    }

    @Override
    public int getMapId() {
        return R.id.mapHome;
    }

    @Override
    public void onMapReadyClient(Location location) {
        Log.i(TAG, "onMapReadyClient: " + location);
        LatLng currentPosition = new LatLng(location.getLatitude(), location.getLongitude());
        SetPosition(mGoogleMap, currentPosition);
        mLastLocation = location;
        tStart = System.currentTimeMillis();
//        NetworkProcessor.GET(this, ID_API_LOCATION, API_LOCATION, MapLocation.class, true, this, false);
        loadJSON();
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.i(TAG, "onPause: ");
        if (mFusedLocationClient != null) {
            mFusedLocationClient.removeLocationUpdates(mLocationCallback);
        }
    }

    @SuppressLint("MissingPermission")
    @Override
    protected void onResume() {
        super.onResume();
        Log.i(TAG, "onResume: ");
        if (mGoogleApiClient != null && mFusedLocationClient != null && mapLocations.size() == 0) {
            Log.i(TAG, "onResume: IF");
//            processDialog.show();
            mFusedLocationClient.requestLocationUpdates(mLocationRequest, mLocationCallback, null);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mCompositeDisposable.clear();
    }


    @Override
    public void initMapElement(GoogleMap googleMap, LocationRequest locationRequest, GoogleApiClient googleApiClient) {
        Log.i(TAG, "initMapElement: " + googleMap + "----" + locationRequest);
        this.mGoogleMap = googleMap;
        this.mLocationRequest = locationRequest;
        this.mGoogleApiClient = googleApiClient;
    }

    private void setUp() {
        Toolbar t = findViewById(R.id.tToolbar);
        btnNearbyPlaces = findViewById(R.id.btnNearbyPlaces);
        btnNearbyPlaces.setOnClickListener(this);
        t.setNavigationIcon(R.drawable.ic_plus);
        setSupportActionBar(t);
        getSupportActionBar().setTitle(getString(R.string.str_visit));
        t.setTitleTextColor(Color.WHITE);
        t.setOnClickListener(this);
        mCompositeDisposable = new CompositeDisposable();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_ico_search, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // TODO Auto-generated method stub
        switch (item.getItemId()) {
            case R.id.log_out:
                Utils.showMessageOKCancel(this, getString(R.string.msg_logout), (dialog, which) -> startActivity(new Intent(MapsActivity.this, LoginActivity.class)));
                return true;
            case android.R.id.home:
                addVisit();
                return true;
            case R.id.search_click:
                CreateBundle(true, mapLocations);
                return true;
            case R.id.tintuc:
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://mevietnamanhhung.vn/phu-nu-viet-nam-cm-1500350704"));
                startActivity(browserIntent);
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void addVisit() {
        Log.i(TAG, "addVisit: " + mLastLocation);
        Intent intent = new Intent(this, AddNewActivity.class);
        intent.putExtra(LOCATION, new Gson().toJson(mLastLocation));
        startActivity(intent);
    }

    @Override
    public void onEvent(EventBusInfo eventBusInfo) {
        super.onEvent(eventBusInfo);
        if (eventBusInfo.getProcessId() == ID_EVENTBUS) {
            Log.i(TAG, "onEvent: " + eventBusInfo.getData());
//            MarkerOptions markerOptions = (MarkerOptions) eventBusInfo.getData();
//            mGoogleMap.addMarker(markerOptions);
            loadJSON();
        }
    }

    @Override
    public void adpaterCallback(Object data, int processId, int position) {
        Log.i(TAG, "adpaterCallback: ");
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        ArrayList<MarkerOptions> listLocation = new ArrayList();
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.title(marker.getTitle());
        markerOptions.snippet(marker.getSnippet());
        markerOptions.position(marker.getPosition());
        listLocation.add(mCurrLocationMarker);
        listLocation.add(markerOptions);
        int index = (int) marker.getTag();
        MapLocation mapLocation = mapLocations.get(index);
        Intent intent = new Intent(this, ShowDirectionActivity.class);
        intent.putExtra(LOCATION, listLocation);
        intent.putExtra(DETAIL, new Gson().toJson(mapLocation));
        startActivity(intent);
        return false;
    }

    private void SetPosition(GoogleMap googleMap, LatLng pos) {
        mCurrLocationMarker = new MarkerOptions();
        mCurrLocationMarker.position(pos);
        mCurrLocationMarker.title(getString(R.string.str_current_position));
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(pos, 15));
    }

    private void downLoadData(String id) {
        NetworkProcessor.GET(this, ID_API_GET_CITY, API_GET_CITY + id, CityDistrict.class, false, this, false);
    }

    private void SetMakerNearby(GoogleMap googleMap, ArrayList<MapLocation> pos) {
        Log.i(TAG, "SetMakerNearby: " + mLastLocation);
        for (int i = 0; i < pos.size(); i++) {
            MarkerOptions markerOptions = new MarkerOptions();
            if (Utils.convertStringToDouble(pos.get(i).latitudeLocation) != -1) {
                Location location2 = new Location("target");
                location2.setLatitude(Double.parseDouble(pos.get(i).getLatitudeLocation()));
                location2.setLongitude(Double.parseDouble(pos.get(i).getLongitudeLocation()));
                pos.get(i).setKmLocation(mLastLocation.distanceTo(location2) / 1000);
                mapLocationsNearby.add(pos.get(i));
                if (mLastLocation.distanceTo(location2) / 1000 <= 10) {
//                    downLoadData(pos.get(i).idQuanHuyen);
//                    Log.i(TAG, "NEARBY " + (mLastLocation.distanceTo(location2)/1000));
//                    pos.get(i).setKmLocation((format1.format(mLastLocation.distanceTo(location2)/1000)));


                    double lati = Double.parseDouble(pos.get(i).latitudeLocation);
                    double longti = Double.parseDouble(pos.get(i).longitudeLocation);
                    LatLng latLng = new LatLng(lati, longti);
                    markerOptions.position(latLng);
                    markerOptions.snippet(pos.get(i).diaChiLocation);
                    markerOptions.title(pos.get(i).tenLocation);
                    markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
                    Marker marker = googleMap.addMarker(markerOptions);
                    marker.setTag(i);

                }
//                mapLocationsNearby.sort(Comparator.comparing(a -> a.attr));

            }
        }
        Collections.sort(mapLocationsNearby, (z1, z2) -> {
            if (z1.getKmLocation() > z2.getKmLocation())
                return 1;
            if (z1.getKmLocation() < z2.getKmLocation())
                return -1;
            return 0;
        });
    }


    private void CreateBundle(Boolean isShow, ArrayList<MapLocation> arrayList) {
        Intent intent = new Intent(this, ListLocationActivity.class);
        Bundle bundle = new Bundle();
        String json = new Gson().toJson(arrayList);
        bundle.putByteArray(DESTINATION, Utils.compress(json));
        intent.putExtras(bundle);
        intent.putExtra(ISSHOW, isShow);
        intent.putExtra(LOCATION, new Gson().toJson(mCurrLocationMarker));
        startActivity(intent);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnNearbyPlaces:
                CreateBundle(false, mapLocationsNearby);
                break;
            case R.id.tToolbar:
//                Utils.showMessageOKCancel(this, getString(R.string.msg_logout), (dialog, which) -> startActivity(new Intent(MapsActivity.this,LoginActivity.class)));
                break;
            default:
                break;
        }
    }

    public void loadJSON() {
        RequestInterface requestInterface = new Retrofit.Builder()
                .baseUrl(URL_SERVER)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build().create(RequestInterface.class);

        Disposable disposable = requestInterface.register("getLocations.php/")
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(this::handleResponse, this::handleError, this::handleSuccess);

        mCompositeDisposable.add(disposable);
    }

    private void handleResponse(ListMapLocation androidList) {
        mapLocations.clear();
        mapLocationsNearby.clear();
        tEnd = System.currentTimeMillis();
        long tDelta = tEnd - tStart;
        double elapsedSeconds = tDelta / 1000.0;
        Log.i(TAG, "handleResponse: TIME EXCUTE: " + elapsedSeconds);
        Log.i(TAG, "handleResponse: " + androidList.getResults().size());
        ArrayList<MapLocation> arrayList = androidList.getResults();

        mapLocations.addAll(arrayList);
        SetMakerNearby(mGoogleMap, mapLocations);
    }

    private void handleError(Throwable error) {
        Log.i(TAG, "handleError: " + error.getMessage());
    }

    private void handleSuccess() {
        Log.i(TAG, "handleSuccess: HERE");
    }


    @Override
    public void downloadSuccess(int processId, Object data) {
        if (processId == ID_API_LOCATION) {
            ArrayList<MapLocation> arrayList = (ArrayList<MapLocation>) data;
            mapLocations.addAll(arrayList);
            Log.i(TAG, "downloadSuccess: " + arrayList.size());
            tEnd = System.currentTimeMillis();
            long tDelta = tEnd - tStart;
            double elapsedSeconds = tDelta / 1000.0;
            Log.i(TAG, "downloadSuccess: TIME EXCUTE: " + elapsedSeconds);
            SetMakerNearby(mGoogleMap, arrayList);
        }
    }

    @Override
    public void downloadError(int processId, String msg) {

    }
}
