package com.example.dreamproperty.sellProperty;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.dreamproperty.R;
import com.example.dreamproperty.addProperty.AddProperties;
import com.example.dreamproperty.addProperty.AddofficeProperties;

public class SellProperty extends AppCompatActivity implements View.OnClickListener {

    CardView homeId,plotId,officeId,farmId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sell_property);

        homeId =(CardView)findViewById(R.id.housepropertysellid);
        plotId =(CardView)findViewById(R.id.plotpropertysellId);
        officeId =(CardView)findViewById(R.id.officepropertysellId);
        farmId =(CardView)findViewById(R.id.farmpropertysellId);

        homeId.setOnClickListener(this);
        plotId.setOnClickListener(this);
        officeId.setOnClickListener(this);
        farmId.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
            switch(view.getId()) {
                case R.id.housepropertysellid:
                    startActivity(new Intent(SellProperty.this , AddProperties.class));
                    break;
                case R.id.officepropertysellId:
                    startActivity(new Intent(SellProperty.this , AddofficeProperties.class));
                    break;
            }
    }
}