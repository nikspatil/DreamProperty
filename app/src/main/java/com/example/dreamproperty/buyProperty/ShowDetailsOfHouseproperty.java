package com.example.dreamproperty.buyProperty;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.dreamproperty.R;
import com.smarteist.autoimageslider.SliderView;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

public class ShowDetailsOfHouseproperty extends AppCompatActivity {

    List<String> images;
    TextView proptylocation, propertyprice, NoOfBeds, noOfBaths, propertyarea;
    SliderView propertyimgslider;
    String getPropertytype, getpropertysubtype, getpropertylocation;
    String getbedroomtype;
    String getbathrromtype;
    String gethousepropertyarea, getpropertyexpectedprice, getownermobinumber;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_details_ofproperty);
        propertyimgslider = findViewById(R.id.property_image_slider);
        proptylocation = (TextView)findViewById(R.id.proplocationtv);
        propertyprice = (TextView)findViewById(R.id.proppricetv);
        NoOfBeds = (TextView)findViewById(R.id.NoofBeds);
        noOfBaths = (TextView)findViewById(R.id.NoofBathrooms);
        propertyarea = (TextView)findViewById(R.id.houseproparea);
        ArrayList<String> propetyImageList = (ArrayList<String>) getIntent().getSerializableExtra("PropertyImages");
        Intent intent = getIntent();
//        getPropertytype = intent.getStringExtra("Property Type");
//        getpropertysubtype = intent.getStringExtra("Property Subtype");
        getpropertylocation = intent.getStringExtra("Property Location");
        getbedroomtype = intent.getStringExtra("House Property Bedrooms");
        getbathrromtype = intent.getStringExtra("House Property Bathrooms");
        getpropertyexpectedprice = intent.getStringExtra("House Property Price");
        gethousepropertyarea = intent.getStringExtra("House Property Area");
        getpropertyexpectedprice += " Price (INR)";
        getbathrromtype += " Baths";
        gethousepropertyarea +="sqft";
        System.out.println("Property Details:");
        System.out.println(getbedroomtype);
        System.out.println(getbathrromtype);
        System.out.println(getpropertylocation);
        proptylocation.setText(getpropertylocation);
        propertyprice.setText(getpropertyexpectedprice);
        NoOfBeds.setText(getbedroomtype);
        noOfBaths.setText(getbathrromtype);
        propertyarea.setText(gethousepropertyarea);
        images = new ArrayList<>();
        for (int i = 0; i < propetyImageList.size(); i++) {
            System.out.println(propetyImageList.get(i));
            images.add(propetyImageList.get(i));
        }
        PropertyDetailsAdapter propertyDetailsAdapter = new PropertyDetailsAdapter(images);
        propertyimgslider.setSliderAdapter(propertyDetailsAdapter);

    }
}