package com.example.dreamproperty.buyProperty;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.example.dreamproperty.R;
import com.smarteist.autoimageslider.SliderView;

import java.util.ArrayList;
import java.util.List;

public class ShowDetailsOfOfficeProperty extends AppCompatActivity {

    List<String> images;
    TextView proptylocation, propertyprice, propertyarea;
    SliderView propertyimgslider;
    String getPropertytype, getpropertysubtype, getpropertylocation;
    String gethousepropertyarea, getpropertyexpectedprice, getownermobinumber;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_details_of_office_property);

        propertyimgslider = findViewById(R.id.property_image_slider);
        proptylocation = (TextView)findViewById(R.id.proplocationtv);
        propertyprice = (TextView)findViewById(R.id.proppricetv);
        propertyarea = (TextView)findViewById(R.id.houseproparea);

        ArrayList<String> propetyImageList = (ArrayList<String>) getIntent().getSerializableExtra("PropertyImages");
        Intent intent = getIntent();
        getpropertylocation = intent.getStringExtra("Property Location");
        getpropertyexpectedprice = intent.getStringExtra("House Property Price");
        gethousepropertyarea = intent.getStringExtra("House Property Area");
        getpropertyexpectedprice += " Price (INR)";

        proptylocation.setText(getpropertylocation);
        propertyprice.setText(getpropertyexpectedprice);
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