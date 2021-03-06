package com.example.dreamproperty;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import com.example.dreamproperty.buyProperty.BuyProperty;
import com.example.dreamproperty.sellProperty.SellProperty;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class Dashboard extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        //Initialize Bottom Navigation View.
        BottomNavigationView navView = findViewById(R.id.bottomNav_view);
        navView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.navigation_profile:
                        Intent intent1 = new Intent(getApplicationContext(), EditUserProfile.class);
                        startActivity(intent1);
                        finish();
                        break;
                    case R.id.navigation_buy:
                        Intent intent2 = new Intent(getApplicationContext(), BuyProperty.class);
                        startActivity(intent2);
                        finish();
                        break;
                    case R.id.navigation_favourites:
                        Intent intent3 = new Intent(getApplicationContext(), ViewFavouritesProperties.class);
                        startActivity(intent3);
                        finish();
                        break;
                    case R.id.navigation_add:
                        Intent intent4 = new Intent(getApplicationContext(), SellProperty.class);
                        startActivity(intent4);
                        finish();
                        break;
                }
                return false;
            }
        });
    }
}