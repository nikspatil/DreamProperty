package com.example.dreamproperty.addProperty;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.example.dreamproperty.R;

public class AddPropertiesNext extends AppCompatActivity implements View.OnClickListener {
    ImageView lastpropertyPage;
    String getbedroomtype = "";
    String getbathrromtype = "";
    String gethousepropertyarea = "";
    Button bhkbedrrom1, bhkbedrrom2, bhkbedrrom3, bhkbedrrom4, bhkbedrrom5;
    Button bathroom0, bathroom1, bathroom2, bathroom3, bathroom4, bathroom5;

    EditText areaprop;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_properties_next);
        lastpropertyPage= findViewById(R.id.lastpagebtn);
        areaprop = (EditText)findViewById(R.id.areadetailsed);
        bhkbedrrom1 = findViewById(R.id.btn1BHK);
        bhkbedrrom2 = findViewById(R.id.btn2BHK);
        bhkbedrrom3 = findViewById(R.id.btn3BHK);
        bhkbedrrom4 = findViewById(R.id.btn4BHK);
        bhkbedrrom5 = findViewById(R.id.btn5BHK);

        bathroom0 = findViewById(R.id.btn0bathroom);
        bathroom1 = findViewById(R.id.btn1bathroom);
        bathroom2 = findViewById(R.id.btn2bathroom);
        bathroom3 = findViewById(R.id.btn3bathroom);
        bathroom4 = findViewById(R.id.btn4bathroom);
        bathroom5 = findViewById(R.id.btn5bathroom);

        bhkbedrrom1.setOnClickListener(this);
        bhkbedrrom2.setOnClickListener(this);
        bhkbedrrom3.setOnClickListener(this);
        bhkbedrrom4.setOnClickListener(this);
        bhkbedrrom5.setOnClickListener(this);


        bathroom0.setOnClickListener(this);
        bathroom1.setOnClickListener(this);
        bathroom2.setOnClickListener(this);
        bathroom3.setOnClickListener(this);
        bathroom4.setOnClickListener(this);
        bathroom5.setOnClickListener(this);

        Intent intent = getIntent();
        String getPropertytype = intent.getStringExtra("Property Type");
        String getpropertysubtype = intent.getStringExtra("Property Subtype");
        String getpropertylocation = intent.getStringExtra("Property Location");
        String getpropertyLatlong = intent.getStringExtra("Property LatLong");
        System.out.println("Property latlong" + getpropertyLatlong);
        lastpropertyPage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                gethousepropertyarea = areaprop.getText().toString();
                Intent intent = new Intent(getApplicationContext(), AddpropertiesLastPage.class);
                intent.putExtra("Property Type",getPropertytype);
                intent.putExtra("Property Subtype", getpropertysubtype);
                intent.putExtra("Property Location", getpropertylocation);
                intent.putExtra("Property LatLong", getpropertyLatlong);
                intent.putExtra("House Property Bedrooms", getbedroomtype);
                intent.putExtra("House Property Bathrooms", getbathrromtype);
                intent.putExtra("House Property Area", gethousepropertyarea);
                startActivity(intent);

            }
        });
    }

    @Override
    public void onClick(View view) {
        switch(view.getId()) {
            case R.id.btn1BHK:
                getbedroomtype = "1BHK";
                break;
            case R.id.btn2BHK:
                getbedroomtype = "2BHK";
                break;
            case R.id.btn3BHK:
                getbedroomtype = "3BHK";
                break;
            case R.id.btn4BHK:
                getbedroomtype = "4BHK";
                break;
            case R.id.btn5BHK:
                getbedroomtype = "5BHK";
                break;
            case R.id.btn0bathroom:
                getbathrromtype = "0";
                break;
            case R.id.btn1bathroom:
                getbathrromtype = "1";
                break;
            case R.id.btn2bathroom:
                getbathrromtype = "2";
                break;
            case R.id.btn3bathroom:
                getbathrromtype = "3";
                break;
            case R.id.btn4bathroom:
                getbathrromtype = "4";
                break;
            case R.id.btn5bathroom:
                getbathrromtype = "5";
                break;
        }
    }
}