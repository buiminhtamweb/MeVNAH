package com.example.toant.googlemap.activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.toant.googlemap.R;
import com.example.toant.googlemap.adapters.LocationAdapter;
import com.example.toant.googlemap.models.City;
import com.example.toant.googlemap.models.District;
import com.example.toant.googlemap.models.MapLocation;
import com.example.toant.googlemap.models.Xa;
import com.example.toant.googlemap.utils.NetworkProcessor;
import com.example.toant.googlemap.utils.Utils;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.util.ArrayList;




public class ListLocationActivity extends BaseActivity implements AdapterView.OnItemSelectedListener {

    private static final String TAG = ListLocationActivity.class.getSimpleName();
    private LocationAdapter locationAdapter;
    private RecyclerView rvLocation;
    private LinearLayout rlFilter;
    private Spinner spnCity,spnDistrict,spnXa;
    private ArrayList<City> listCity = new ArrayList();
    private ArrayList<District> listDistrict = new ArrayList();
    private ArrayList<Xa> listXa = new ArrayList();
    private ArrayList<MapLocation> listMapLocation = new ArrayList<>();
    private ArrayAdapter<String> cityAdapter, districtAdapter,xaAdapter;
    private MarkerOptions currentMarker;
    private  String id_huyen = "";



    @Override
    public void initUI() {
        showHomeButton();
        setUpContent();
    }

    @SuppressLint("ResourceType")
    @Override
    public int getLayoutResourceId() {
        return R.layout.activity_list_location;
    }

    private void setUpContent() {
        Intent intent = getIntent();
        if(intent != null){
            Boolean isShow = intent.getBooleanExtra(ISSHOW,false);
            String jsonMarker = intent.getStringExtra(LOCATION);
            currentMarker = new Gson().fromJson(jsonMarker,MarkerOptions.class);
            byte[] byteArr  = intent.getExtras().getByteArray(DESTINATION);
            String json = null;
            try {
                json = Utils.decompress(byteArr);
            } catch (IOException e) {
                e.printStackTrace();
            }

            listMapLocation = new Gson().fromJson(json,new TypeToken<ArrayList<MapLocation>>() {}.getType());
            rvLocation = findViewById(R.id.rvLocation);
            LinearLayoutManager manager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
            rvLocation.setHasFixedSize(true);
            manager.setItemPrefetchEnabled(false);
            rvLocation.setLayoutManager(manager);
            locationAdapter = new LocationAdapter(this, listMapLocation,this);
            rvLocation.setAdapter(locationAdapter);
            if(isShow){
                rlFilter = findViewById(R.id.rlFilter);
                spnCity = findViewById(R.id.spnCity);
                spnDistrict = findViewById(R.id.spnDistrict);
                spnXa = findViewById(R.id.spnXa);
                spnCity.setOnItemSelectedListener(this);
                spnDistrict.setOnItemSelectedListener(this);
                spnXa.setOnItemSelectedListener(this);
                rlFilter.setVisibility(View.VISIBLE);
                cityAdapter = CreateSpinner(spnCity,getString(R.string.btnCity));
                districtAdapter = CreateSpinner(spnDistrict,getString(R.string.btnDistrict));
                xaAdapter = CreateSpinner(spnXa,"Xã");
                NetworkProcessor.GET(this,ID_API_TINHTHANH, API_TINHTHANH, City.class,false,this,false);
                setTitle("Tìm kiếm");
            }
        }
    }

    private ArrayAdapter<String> CreateSpinner(Spinner spinner, String firstItem){
        ArrayList<String> listData = new ArrayList<>();
        listData.add(0,firstItem);
        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,listData );
        spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(spinnerArrayAdapter);
        return spinnerArrayAdapter;
    }

    private void UpdateSpinner(ArrayList<String> listData,ArrayAdapter<String> arrayAdapter, String firstItem){
        listData.add(0,firstItem);
        arrayAdapter.clear();
        arrayAdapter.addAll(listData);
        arrayAdapter.notifyDataSetChanged();
    }

    @Override
    public void adpaterCallback(Object data, int processId, int position) {
        if(processId == EVENT_CLICK){
            // push list marker option to ShowDirectionActivity:
            MapLocation mapLocation = (MapLocation) data;
            ArrayList<MarkerOptions> arrayList = new ArrayList<>();
            MarkerOptions markerOptions = new MarkerOptions();
            if(Utils.convertStringToDouble(mapLocation.getLatitudeLocation()) != -1){
                double lati = Double.parseDouble(mapLocation.getLatitudeLocation());
                double longti = Double.parseDouble(mapLocation.getLongitudeLocation());
                LatLng latLng = new LatLng(lati,longti);
                markerOptions.position(latLng);
                markerOptions.snippet(mapLocation.getDiaChiLocation());
                markerOptions.title(mapLocation.getTenLocation());
                markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
                arrayList.add(currentMarker);
                arrayList.add(markerOptions);
//                Intent intent = new Intent(this,ShowDirectionActivity.class);
//                intent.putExtra(LOCATION,arrayList);
//                intent.putExtra(DETAIL,new Gson().toJson(mapLocation));
//                startActivity(intent);

                //Intent sang chi tiết
                Intent intent = new Intent(this, DetailActivity.class);
                intent.putExtra(LOCATION,arrayList);
                intent.putExtra(DETAIL,new Gson().toJson(mapLocation));
                startActivity(intent);
            }else {
                Toast.makeText(this, "Không xác định được vị trí", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.search, menu);
        MenuItem item = menu.findItem(R.id.search);
        SearchView searchView = (SearchView) item.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                locationAdapter.search(newText);
                return false;
            }
        });
        return super.onCreateOptionsMenu(menu);
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
        if(processId == ID_API_TINHTHANH){
            listCity = (ArrayList<City>) data;
            ArrayList<String> arrayList = new ArrayList<>();
            for (City city : listCity) {
                arrayList.add(city.getTenTinhThanh());
            }
            UpdateSpinner(arrayList,cityAdapter,getString(R.string.btnCity));
        }
        if(processId == ID_API_QUANHUYEN){
            listDistrict = (ArrayList<District>) data;
            ArrayList<String> arrayList = new ArrayList<>();
            for (District district : listDistrict) {
                arrayList.add(district.getTenQuanHuyen());
            }
            UpdateSpinner(arrayList,districtAdapter,getString(R.string.btnDistrict));
        }
        if(processId == ID_API_GET_XA){
            if(!(data instanceof String)){
                listXa = (ArrayList<Xa>) data;
                ArrayList<String> arrayList = new ArrayList<>();
                for (Xa xa : listXa) {
                    arrayList.add(xa.getTenPhuongXa());
                }
                UpdateSpinner(arrayList,xaAdapter,"Xã");
            }
        }
        super.downloadSuccess(processId, data);
    }

    @Override
    public void downloadError(int processId, String msg) {

        super.downloadError(processId, msg);
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        switch (parent.getId()){
            case R.id.spnCity:
                if(position > 0){
                    NetworkProcessor.GET(this,ID_API_QUANHUYEN, API_QUANHUYEN+listCity.get(position-1).getIdTinhThanh(), District.class,false,this,false);
                    break;
                }else {
                    spnDistrict.setSelection(0);
                    locationAdapter.filterData(null);
                }
            case R.id.spnDistrict:
                if(position > 0){
                    District district = listDistrict.get(position-1);
                    id_huyen = district.getIdQuanHuyen();
                    NetworkProcessor.GET(this,ID_API_GET_XA, API_GET_XA+id_huyen, Xa.class,false,this,false);
                }
//                else {
//                    locationAdapter.filterData(null);
//                }
                break;
            case R.id.spnXa:
                if(position > 0){
                    Xa xa = listXa.get(position - 1);
                    locationAdapter.filterData(xa);
                }else {
                    locationAdapter.filterData(null);
                }
                break;
            default:
                break;
        }

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
