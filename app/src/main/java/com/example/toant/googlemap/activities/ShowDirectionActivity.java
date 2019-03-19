package com.example.toant.googlemap.activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.location.Location;
import android.support.annotation.Nullable;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.toant.googlemap.R;
import com.example.toant.googlemap.models.CityDistrict;
import com.example.toant.googlemap.models.MapLocation;
import com.example.toant.googlemap.models.TimeKm;
import com.example.toant.googlemap.utils.Constants;
import com.example.toant.googlemap.utils.NetworkProcessor;
import com.example.toant.googlemap.utils.Utils;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.JointType;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.gson.Gson;
import com.google.maps.android.PolyUtil;


import java.util.ArrayList;
import java.util.List;

public class ShowDirectionActivity extends BaseMapActivity {

    private static final String TAG = ShowDirectionActivity.class.getSimpleName();

    private ArrayList<MarkerOptions> listLocation = new ArrayList<>();
    private GoogleMap mGoogleMap;
    private SupportMapFragment mapFrag;
    private LocationRequest mLocationRequest;
    private GoogleApiClient mGoogleApiClient;
    private Button btnDetails;
    private TextView tvTime;
    private MapLocation clickedLocation = null;
    private String url;

    @Override
    public void initUI() {
        btnDetails = findViewById(R.id.btnDetails);
        btnDetails.setOnClickListener(this);
        Intent intent = getIntent();
        if(intent != null){
            ArrayList<MarkerOptions> listLocation = intent.getParcelableArrayListExtra(LOCATION);
            this.listLocation = listLocation;
            String json = intent.getStringExtra(DETAIL);
            clickedLocation = new Gson().fromJson(json,MapLocation.class);
        }
        showHomeButton();
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.btnDetails){
            if(clickedLocation != null){
                Log.i(TAG, "onClick: " + clickedLocation.getDiaChiLocation());
                Intent intent = new Intent(this,DetailActivity.class);
                intent.putExtra(DETAIL,new Gson().toJson(clickedLocation));
                startActivity(intent);
            }
        }
        super.onClick(v);
    }

    @Override
    public void initMapElement(GoogleMap googleMap, LocationRequest locationRequest, GoogleApiClient googleApiClient) {
        this.mGoogleMap = googleMap;
        this.mLocationRequest = locationRequest;
        this.mGoogleApiClient = googleApiClient;
    }

    @SuppressLint("ResourceType")
    @Override
    public int getLayoutResourceId() {
        return R.layout.activity_show_direction;
    }

    @Override
    public int getMapId() {
        return R.id.mapDirection;
    }

    @Override
    public void onMapReadyClient(Location location) {
        if(listLocation.size() > 0){
            LatLng latLng = new LatLng(location.getLatitude(),location.getLongitude());
            String id="";
            id=location.getLatitude()+","+location.getLongitude()+"&destinations="+listLocation.get(1).getPosition().latitude+","+listLocation.get(1).getPosition().longitude;
            downLoadData1(id);
            Log.i("them",""+location.getLatitude()+","+location.getLongitude()+"&destinations="+listLocation.get(1).getPosition().latitude+","+listLocation.get(1).getPosition().longitude);
            NetworkProcessor.GET(this,ID_API_GOOGLE_DIRECTION, Utils.getDirectionsUrl(latLng,listLocation.get(1).getPosition()),String.class,true,this,true);
        }else {
            Toast.makeText(this, "Không xác định được vị trí", Toast.LENGTH_SHORT).show();
        }
    }

    private void downLoadData1(String id) {
        NetworkProcessor.GET(this, Constants.ID_API_GOOGLE_DISTANCEMATRIX,Constants.API_GOOGLE_DISTANCEMATRIX+id, TimeKm.class,false,this,false);
    }



    @Override
    public void onConnected(@Nullable Bundle bundle) {
//        Toast.makeText(this, "CONNECTED", Toast.LENGTH_SHORT).show();
        super.onConnected(bundle);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // TODO Auto-generated method stub
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void downloadSuccess(int processId, Object data) {
        if(processId == ID_API_GOOGLE_DIRECTION){

            ArrayList<String> list = (ArrayList<String>) data;
            PolylineOptions options = new PolylineOptions().color(getResources().getColor(R.color.blue_direction)).width(25).geodesic(true).jointType(JointType.BEVEL);
            ArrayList<List<LatLng>> listLoca = new ArrayList<>();
            for(int i = 0;i<list.size();i++)
            {
                listLoca.add(PolyUtil.decode(list.get(i)));
                options.addAll(PolyUtil.decode(list.get(i)));
            }
            mGoogleMap.addPolyline(options);
            LatLngBounds.Builder builder = new LatLngBounds.Builder();
            builder.include(listLocation.get(0).getPosition());
            builder.include(listLocation.get(1).getPosition());
            LatLngBounds bounds = builder.build();
            int padding = 200; // padding around start and end marker
            CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, padding);
            mGoogleMap.animateCamera(cu);
            for(int i = 0; i < listLocation.size(); i++){
                MarkerOptions markerOptions = new MarkerOptions();
                markerOptions.position(listLocation.get(i).getPosition());
//                String title = i == 0 ? "Current Position" : "Destination";
                markerOptions.title(listLocation.get(i).getTitle());
                markerOptions.snippet(listLocation.get(i).getSnippet());
                markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
                mGoogleMap.addMarker(markerOptions);
            }

        }
        if(processId == ID_API_GOOGLE_DISTANCEMATRIX){
//            String str = (String) data;
            Log.i(TAG, "downloadSuccess: "+ data.getClass());
            if(data instanceof TimeKm){
                TimeKm timeKm = (TimeKm) data;
//                setTitle("Đường đi");
//                TextView customView = (TextView) LayoutInflater.from(this).inflate(R.layout.actionbar_custom_title_view_centered,null);
//                ActionBar.LayoutParams params = new ActionBar.LayoutParams(ActionBar.LayoutParams.MATCH_PARENT, ActionBar.LayoutParams.MATCH_PARENT, Gravity.CENTER);
//
//                customView.setText("Your centered text");
                getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
                getSupportActionBar().setCustomView(R.layout.actionbar_custom_title_view_centered);
                TextView textView = getSupportActionBar().getCustomView().findViewById(R.id.cusotmTitle);
                getSupportActionBar().getCustomView().findViewById(R.id.btnBack).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        finish();
                    }
                });
                try {
                    textView.setText(timeKm.getRows().get(0).elements.get(0).distance.text +" - "+timeKm.getRows().get(0).elements.get(0).duration.text);
                }catch (Exception e){
                    textView.setText("Không xác định");
                }
//                tvTime.setText(timeKm.getRows().get(0).elements.get(0).distance.text +" - "+timeKm.getRows().get(0).elements.get(0).duration.text);
            }
        }
    }

    @Override
    public void downloadError(int processId, String msg) {

    }
}
