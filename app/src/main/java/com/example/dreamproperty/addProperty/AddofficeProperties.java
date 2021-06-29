package com.example.dreamproperty.addProperty;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.dreamproperty.R;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode;

import java.util.Arrays;
import java.util.List;

public class AddofficeProperties extends AppCompatActivity {

    TextView choosetype, choosepropertysubtype, whereisproperty;
    EditText searchpropertyloc;
    String choosTypeStr="";
    CardView homeId,plotId,officeId,farmId, searchlocationcardId;
    Button residentialsubtype, commercialsubtype;
    ImageView nextpropertyPage;
    String propertyType="";
    String propertysubtype= "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_properties);

        Places.initialize(getApplicationContext(), getString(R.string.google_maps_key));

        homeId =(CardView)findViewById(R.id.housepropertyid);
        plotId =(CardView)findViewById(R.id.plotpropertyId);
        officeId =(CardView)findViewById(R.id.officepropertyId);
        farmId =(CardView)findViewById(R.id.farmpropertyId);
        choosetype = (TextView)findViewById(R.id.choosetypetext);


        choosepropertysubtype = (TextView)findViewById(R.id.propertysubtype);
        choosepropertysubtype.setVisibility(View.INVISIBLE);
        residentialsubtype = (Button)findViewById(R.id.typeresidential);
        commercialsubtype = (Button)findViewById(R.id.typecommerical);
        residentialsubtype.setVisibility(View.INVISIBLE);
        commercialsubtype.setVisibility(View.INVISIBLE);
        nextpropertyPage = findViewById(R.id.nextpagebtn);

        whereisproperty = (TextView)findViewById(R.id.propertylocation);
        whereisproperty.setVisibility(View.INVISIBLE);
        searchpropertyloc = (EditText)findViewById(R.id.edtvsearchpropertylocation);
        searchpropertyloc.setVisibility(View.INVISIBLE);
        searchlocationcardId = findViewById(R.id.searchlocation_card);
        searchlocationcardId.setVisibility(View.INVISIBLE);

        officeId.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                propertyType = "Office";
                homeId.setVisibility(View.GONE);
                plotId.setVisibility(View.GONE);
                officeId.setVisibility(View.VISIBLE);
                farmId.setVisibility(View.GONE);
                choosepropertysubtype.setVisibility(View.VISIBLE);
                residentialsubtype.setVisibility(View.VISIBLE);
                commercialsubtype.setVisibility(View.VISIBLE);
                choosTypeStr = "Choose Property Again";
                choosetype.setText(choosTypeStr);
            }
        });

        residentialsubtype.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                propertysubtype = "Residential";
                commercialsubtype.setVisibility(View.INVISIBLE);
                callSearchproprtylocation();
            }
        });

        // get property location
        searchpropertyloc.setFocusable(false);
        searchpropertyloc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                List<Place.Field> fields = Arrays.asList(Place.Field.ADDRESS);
                Intent intent = new Autocomplete.IntentBuilder(AutocompleteActivityMode.OVERLAY, fields).build(
                        AddofficeProperties.this);
                startActivityForResult(intent, 100);
            }
        });

        nextpropertyPage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String getSearchedLocation = searchpropertyloc.getText().toString().trim();
                Intent intent = new Intent(getApplicationContext(), AddpropertiesLastPage.class);
                intent.putExtra("Property Type",propertyType);
                intent.putExtra("Property Subtype", propertysubtype);
                intent.putExtra("Property Location", getSearchedLocation);
                startActivity(intent);
            }
        });
    }

    private void callSearchproprtylocation() {
        whereisproperty.setVisibility(View.VISIBLE);
        searchlocationcardId.setVisibility(View.VISIBLE);
        searchpropertyloc.setVisibility(View.VISIBLE);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100 && resultCode == RESULT_OK) {
            Place place = Autocomplete.getPlaceFromIntent(data);
            searchpropertyloc.setText(place.getAddress());
        }

    }
}