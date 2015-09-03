package kien.activities;

import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import kien.SQLHelper.DataBaseHelper;
import kien.adapter.AdapterLicense;
import kien.license.R;
import kien.objects.LatLong;
import kien.objects.License;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by KIEN on 6/16/2015.
 */
public class DistrictAcitivity extends FragmentActivity implements AdapterLicense.OnClickNumber, OnMapReadyCallback{
    DataBaseHelper dataBaseHelper;
    List<License> listLicense;
    AdapterLicense adapterLicense;
    MapFragment mMapFragment;
    LatLong latLong;
    GoogleMap googleMap;
    private final static LatLng HANOI = new LatLng(21.0277644,105.8341598);
    private LatLngBounds AUS = new LatLngBounds(new LatLng(-44,113), new LatLng(-10,154));
    String localAddress;
    LatLng latlongAdress;
    ImageView ivBack;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_viewdistrict);
        TextView tvPopulation = (TextView) findViewById(R.id.tvPopulation);
        TextView tvArea = (TextView) findViewById(R.id.tvArea);
        GridView gvDistrict = (GridView) findViewById(R.id.gvDistrict);
        ivBack = (ImageView) findViewById(R.id.ivBack);
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        TextView tvNameProvince = (TextView) findViewById(R.id.tvNameProvince);

        Intent myIntent = getIntent();
        int id = myIntent.getIntExtra("id", 0);

        dataBaseHelper = new DataBaseHelper(this);
        try {
            dataBaseHelper.isCreatedDatabase();
        }
        catch (IOException e)
        {

        }

        int population = dataBaseHelper.getPopulationProvince(id);
        float area = dataBaseHelper.getAreaProvince(id);
        NumberFormat numberFormat = NumberFormat.getNumberInstance();
        numberFormat.format(population);
        Log.e("NUMBER", area + "");
        tvPopulation.setText(numberFormat.format(population) + " người");
        tvArea.setText(area + "  km2");

        listLicense = new ArrayList<License>();
        listLicense = dataBaseHelper.getListDistrict(id);
        adapterLicense = new AdapterLicense(this, R.layout.fragment_license, listLicense, this);
        gvDistrict.setAdapter(adapterLicense);
        localAddress = dataBaseHelper.getAddressLocal(id);
        tvNameProvince.setText(localAddress + "");
        //add Map;
//        mMapFragment = MapFragment.newInstance();
//        FragmentTransaction fragmentTransaction =
//                getFragmentManager().beginTransaction();
//        fragmentTransaction.add(R.id.map, mMapFragment);
//        fragmentTransaction.commit();
//        mMapFragment.getMapAsync(this);
//        GoogleMapOptions googleMapOptions = new GoogleMapOptions();
//        googleMapOptions.mapType(GoogleMap.MAP_TYPE_HYBRID).compassEnabled(true).zoomControlsEnabled(false);

        //googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(HANOI, 10));
        //googleMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);



        latlongAdress = dataBaseHelper.getLatLong(id);

        MapFragment mapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        googleMap = ((MapFragment) getFragmentManager().findFragmentById(R.id.map)).getMap();
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latlongAdress, 9));
        Log.e("TEST", latlongAdress + " address");
        Log.e("TEST", googleMap + "google");
        //GoogleMap googleMap = ((MapFragment) getFragmentManager().findFragmentById(R.id.map)).getMap();
        googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        googleMap.getUiSettings().setZoomControlsEnabled(true);
        googleMap.setMyLocationEnabled(true);


    }

    @Override
    public void changeToPage(int id) {
//        Toast.makeText(this, id + "", Toast.LENGTH_SHORT).show();
//        Intent myIntent = new Intent(DistrictAcitivity.this, ViewInfoDistrictActivity.class);
//        myIntent.putExtra("id", id);
//        startActivity(myIntent);
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        googleMap.addMarker(new MarkerOptions().position(latlongAdress).title(localAddress + ", Việt Nam"));
    }

    public LatLng getLocationFromAdress(String strAddress) {
        Geocoder geocoder = new Geocoder(this);
        List<Address> addresses;
        LatLng lng = new LatLng(0,0);
        Log.e("LATLONG", "hello");
        try {

            addresses = geocoder.getFromLocationName(strAddress, 5);
            Log.e("LATLONG", strAddress + "");
            if (addresses == null){
                return null;
            }
            Address location = addresses.get(0);
            location.getLatitude();
            location.getLongitude();
            Log.e("LATLONG", location.getLatitude() + " " + location.getLongitude());
            lng = new LatLng(location.getLatitude(), location.getLongitude());
        } catch (IOException e) {
            Log.e("LATLONG", strAddress + " error");
            e.printStackTrace();
        }

        return lng;
    }
}
