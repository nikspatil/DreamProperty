package com.example.dreamproperty.buyProperty;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.dreamproperty.R;
import com.example.dreamproperty.addProperty.AddpropertiesLastPage;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.smarteist.autoimageslider.SliderView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static android.content.ContentValues.TAG;

public class ShowDetailsOfFarmProperty extends AppCompatActivity {

    List<String> images;
    TextView proptylocation, propertyprice, propertyarea;
    SliderView propertyimgslider;
    ImageButton callowner, msgowner;
    String getpropertyID, getPropertytype, getpropertysubtype, getpropertylocation, getpropertyLatLong;
    String getfarmpropertyarea, getpropertyexpectedprice, getownermobinumber;

    CollectionReference reference;
    FirebaseAuth mAuth;
    FirebaseUser mUser;
    FirebaseFirestore mstore;

    String userID;
    private Menu menu;
    MenuItem favitem;
    boolean isFavorite = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_details_of_farm_property);

        mstore = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();
        userID = mUser.getUid();
        reference = mstore.collection("users");

        propertyimgslider = findViewById(R.id.property_image_slider);
        proptylocation = (TextView)findViewById(R.id.proplocationtv);
        propertyprice = (TextView)findViewById(R.id.proppricetv);
        propertyarea = (TextView)findViewById(R.id.farmproparea);
        callowner = findViewById(R.id.callofficeowner);
        msgowner = findViewById(R.id.msgtoowner);

        ArrayList<String> propetyImageList = (ArrayList<String>) getIntent().getSerializableExtra("PropertyImages");
        Intent intent = getIntent();

        getpropertyID = intent.getStringExtra("propertyID");
        getPropertytype = intent.getStringExtra("Property Type");
        getpropertylocation = intent.getStringExtra("Property Location");
        getpropertyLatLong = intent.getStringExtra("Property LatLong");
        getpropertyexpectedprice = intent.getStringExtra("Farm Property Price");
        getfarmpropertyarea = intent.getStringExtra("Farm Property Area");
        getownermobinumber = intent.getStringExtra("ownermobilnumer");

        getpropertyexpectedprice += " Price (INR)";
        getfarmpropertyarea += "  Sqft (Area)";
        proptylocation.setText(getpropertylocation);
        propertyprice.setText(getpropertyexpectedprice);
        propertyarea.setText(getfarmpropertyarea);
        images = new ArrayList<>();
        for (int i = 0; i < propetyImageList.size(); i++) {
            images.add(propetyImageList.get(i));
        }
        PropertyDetailsAdapter propertyDetailsAdapter = new PropertyDetailsAdapter(images);
        propertyimgslider.setSliderAdapter(propertyDetailsAdapter);


        callowner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Intent.ACTION_DIAL);
                String getnumber ="tel:" +getownermobinumber;
                i.setData(Uri.parse(getnumber));
                startActivity(i);
            }
        });

        msgowner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String messageToSend = "Hi, I am interested in your posted property. Is this property avaialble for sale? can i come to visit?";
                String number = getownermobinumber;
                Intent smsIntent = new Intent(Intent.ACTION_VIEW);
                smsIntent.setData(Uri.parse("smsto:"));
                smsIntent.setType("vnd.android-dir/mms-sms");
                smsIntent.putExtra("address"  , new String (number));
                smsIntent.putExtra("sms_body"  , messageToSend);
                try {
                    startActivity(smsIntent);
                    finish();
                } catch (android.content.ActivityNotFoundException ex) {
                    Toast.makeText(ShowDetailsOfFarmProperty.this,
                            "SMS faild, please try again later.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        this.menu = menu;
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.favourite_menu_item, menu);
        favitem = menu.findItem(R.id.addfavourite);
        return true;

    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.addfavourite:
                if(isFavorite){
                    isFavorite = false;
                    removeProperty();
                    item.setIcon(R.drawable.ic_baseline_favorite_border_24);
                }else{
                    isFavorite = true;
                    item.setIcon(R.drawable.ic_baseline_favorite_24);
                    addProperty();
                }


        }
        return super.onOptionsItemSelected(item);
    }

    private void addProperty() {
        Map<String, Object> dataMap = new HashMap<>();
        System.out.println("Property Type:"+ getPropertytype);
        if(getPropertytype.equals("Plot")) {
            dataMap.put("propertyType", "Plot");
        }else{
            dataMap.put("propertyType", "Farm");
        }
        dataMap.put("propertyId", getpropertyID);
        dataMap.put("propertyLocation", getpropertylocation);
        dataMap.put("propertyLatLong", getpropertyLatLong);
        dataMap.put("propertyexpectedprice", getpropertyexpectedprice);
        dataMap.put("propertyarea", getfarmpropertyarea);
        dataMap.put("ownermobilnumer", getownermobinumber);
        dataMap.put("images", images);
        Map<String, Boolean> dataMap1 = new HashMap<>();
        dataMap1.put(userID, true);


        reference.document(userID).collection("FavouritesList").add(dataMap).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
            @Override
            public void onSuccess(DocumentReference documentReference) {
                Toast.makeText(ShowDetailsOfFarmProperty.this, "Property added to your favourite list!!!", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(ShowDetailsOfFarmProperty.this, "Property is not adding  into your favourite list. Please retry one more time", Toast.LENGTH_SHORT).show();
            }
        });

        if (getpropertyID != null) {
            reference.document(userID).collection("Favourites").document(getpropertyID).set(dataMap1);
        }
    }

    private void removeProperty() {
        if(getpropertyID !=null){
            reference.document(userID).collection("Favourites").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    if(task.isSuccessful()){
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            if(document.getId().equals(getpropertyID)){
                                System.out.println("Document ID"+document.getId());
                                System.out.println("Property ID"+ getpropertyID);
                                reference.document(userID).collection("Favourites").document(getpropertyID).delete();
                                Toast.makeText(getApplicationContext(), "Property deleted from your favourite list!!!",Toast.LENGTH_LONG).show();
                            };
                        }
                    }
                }
            });

            reference.document(userID).collection("FavouritesList").whereEqualTo("propertyId", getpropertyID).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    if(task.isSuccessful()){
                        for(QueryDocumentSnapshot snapshot : task.getResult()){
                            reference.document(userID).collection("FavouritesList").document(snapshot.getId()).delete();

                        }
                    }
                }
            });

        }else{
            Toast.makeText(getApplicationContext(), "Something went wrong!! There was a error while adding property into favourites", Toast.LENGTH_LONG).show();
        }
    }
}