package com.example.dreamproperty.buyProperty;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.dreamproperty.R;
import com.example.dreamproperty.addProperty.AddProperties;
import com.example.dreamproperty.addProperty.AddPropertiesNext;
import com.example.dreamproperty.addProperty.AddofficeProperties;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode;

import java.util.Arrays;
import java.util.List;

public class BuyProperty extends FragmentActivity implements OnMapReadyCallback, View.OnClickListener {

    private static final int REQUEST_CODE = 101 ;
    private GoogleMap mMap;
    Location currentLocation;
    SupportMapFragment mapFragment;
    String propertylocationString;
    TextView getPropertylocationInput;
    String propertypecard;
    FusedLocationProviderClient fusedLocationProviderClient;
    CardView homeId,plotId,officeId,farmId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buy_property);
        getPropertylocationInput = (TextView) findViewById(R.id.searchpropcitytv);

        homeId =(CardView)findViewById(R.id.homepropcard);
        plotId =(CardView)findViewById(R.id.plotpropertycard);
        officeId =(CardView)findViewById(R.id.officepropcard);
        farmId =(CardView)findViewById(R.id.farmpropcard);

        homeId.setOnClickListener(this);
        plotId.setOnClickListener(this);
        officeId.setOnClickListener(this);
        farmId.setOnClickListener(this);

        Places.initialize(getApplicationContext(), getString(R.string.google_maps_key));
        getPropertylocationInput.setFocusable(false);
        getPropertylocationInput.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                List<Place.Field> fields = Arrays.asList(Place.Field.NAME);
                Intent intent = new Autocomplete.IntentBuilder(AutocompleteActivityMode.OVERLAY, fields).build(
                        BuyProperty.this);
                startActivityForResult(intent, 100);
            }
        });


        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        fetchLocation();
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    private void fetchLocation() {
        if (ActivityCompat.checkSelfPermission(
                this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_CODE);
            return;
        }
        Task<Location> task = fusedLocationProviderClient.getLastLocation();
        task.addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if (location != null) {
                    currentLocation = location;
                    Toast.makeText(getApplicationContext(), currentLocation.getLatitude() + "" + currentLocation.getLongitude(), Toast.LENGTH_SHORT).show();
                    LatLng latlong = new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude());
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latlong, 10));
                    assert mapFragment != null;
                    mapFragment.getMapAsync(BuyProperty.this);
                }
            }
        });
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        mMap.setMyLocationEnabled(true);
        googleMap.setMyLocationEnabled(true);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case REQUEST_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    fetchLocation();
                }
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100 && resultCode == RESULT_OK) {
            Place place = Autocomplete.getPlaceFromIntent(data);
            getPropertylocationInput.setText(place.getName());
        }

    }

    @Override
    public void onClick(View view) {
        switch(view.getId()) {
            case R.id.homepropcard:
                //startActivity(new Intent(BuyProperty.this , Buyproperties.class));
                propertypecard="Home";
                Intent intent = new Intent(getApplicationContext(), Buyproperties.class);
                intent.putExtra("Property Type",propertypecard);
                startActivity(intent);
                break;
            case R.id.officepropcard:
                propertypecard="Office";
                Intent intent1 = new Intent(getApplicationContext(), Buyproperties.class);
                intent1.putExtra("Property Type",propertypecard);
                startActivity(intent1);
                break;
            case R.id.farmpropcard:
                propertypecard="Farm";
                Intent intent2 = new Intent(getApplicationContext(), Buyproperties.class);
                intent2.putExtra("Property Type",propertypecard);
                startActivity(intent2);
                break;
            case R.id.plotpropertycard:
                propertypecard="Plot";
                Intent intent3 = new Intent(getApplicationContext(), Buyproperties.class);
                intent3.putExtra("Property Type",propertypecard);
                startActivity(intent3);
                break;


        }
    }
}