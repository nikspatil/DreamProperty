package com.example.dreamproperty.addProperty;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.example.dreamproperty.R;

public class AddPropertiesNext extends AppCompatActivity {
    ImageView lastpropertyPage;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_properties_next);
        lastpropertyPage= findViewById(R.id.lastpagebtn);

        Intent intent = getIntent();
        String getPropertytype = intent.getStringExtra("Property Type");
        String getpropertysubtype = intent.getStringExtra("Property Subtype");
        String getpropertylocation = intent.getStringExtra("Property Location");

        lastpropertyPage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), AddpropertiesLastPage.class);
                intent.putExtra("Property Type",getPropertytype);
                intent.putExtra("Property Subtype", getpropertysubtype);
                intent.putExtra("Property Location", getpropertylocation);
                startActivity(intent);
                //startActivity(new Intent(AddPropertiesNext.this , AddpropertiesLastPage.class));
            }
        });
    }
}