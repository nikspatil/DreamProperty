package com.example.dreamproperty;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.dreamproperty.addProperty.AddProperties;
import com.example.dreamproperty.addProperty.AddofficeProperties;
import com.example.dreamproperty.buyProperty.BuyProperty;
import com.example.dreamproperty.buyProperty.Buyproperties;
import com.example.dreamproperty.sellProperty.SellProperty;
import com.google.android.gms.maps.model.Dash;

public class Dashboard extends AppCompatActivity implements View.OnClickListener {

    Button buy, sell, editprofile;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        buy = findViewById(R.id.buybtn);
        sell = findViewById(R.id.sellbtn);
        editprofile = findViewById(R.id.editprofile);
        buy.setOnClickListener(this);
        sell.setOnClickListener(this);
        editprofile.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch(view.getId()) {
            case R.id.buybtn:
                startActivity(new Intent(Dashboard.this , BuyProperty.class));
                break;
            case R.id.sellbtn:
                startActivity(new Intent(Dashboard.this , SellProperty.class));
                break;
            case R.id.editprofile:
                startActivity(new Intent(Dashboard.this , EditUserProfile.class));
                break;

        }
    }
}