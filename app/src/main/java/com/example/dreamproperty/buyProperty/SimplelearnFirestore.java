package com.example.dreamproperty.buyProperty;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.dreamproperty.R;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

public class SimplelearnFirestore extends AppCompatActivity {

    @BindView(R.id.progress_bar)
    ProgressBar progressBar;
    RecyclerView propertyList;
    String getPropertytype;
    ArrayList<String> propertyImageList;
    private FirebaseFirestore db;
    private FirestoreRecyclerAdapter adapter;
    LinearLayoutManager linearLayoutManager;

    private FirebaseFirestore mstore = FirebaseFirestore.getInstance();
    private CollectionReference favlistRef, favref;
    FirebaseAuth mAuth;
    FirebaseUser mUser;
    String userID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buyproperties);
        ButterKnife.bind(this);
        propertyList = (RecyclerView)findViewById(R.id.property_list);
        linearLayoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);
        propertyList.setLayoutManager(linearLayoutManager);
        db = FirebaseFirestore.getInstance();
        propertyImageList = new ArrayList<String>();

        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();
        userID = mUser.getUid();
        favlistRef = mstore.collection("favouritelist");
        favref = mstore.collection("Favourites");


        Intent intent = getIntent();
        getPropertytype = intent.getStringExtra("Property Type");
        getPropertyList();


    }

    private void getPropertyList(){
        Query query = db.collection("usersProperty").whereEqualTo("propertyType", getPropertytype);

        FirestoreRecyclerOptions<Note> response = new FirestoreRecyclerOptions.Builder<Note>()
                .setQuery(query, Note.class)
                .build();
        System.out.println(response);
        adapter = new FirestoreRecyclerAdapter<Note, PropertyHolder>(response) {
            @Override
            public void onBindViewHolder(PropertyHolder holder, int position, Note model) {
                progressBar.setVisibility(View.GONE);
                holder.propertyName.setText(model.getPropertyType());
                holder.propertySubtype.setText(model.getPropertySubType());
                holder.propertyID.setText(model.getDocumentId());
                String firstpropertyimage = "";
                for (String propertyimages : model.getImages()){
                    firstpropertyimage = propertyimages;
                    break;
                }
                Glide.with(getApplicationContext())
                        .load(firstpropertyimage)
                        .into(holder.imageView);

                holder.itemView.setOnClickListener(v -> {
                    propertyImageList.clear();
                    for (String propertyimages : model.getImages()){
                        propertyImageList.add(propertyimages);
                    }
                    System.out.println(model.getDocumentId());

                    String checkPropertyType = String.valueOf(model.getPropertyType());
                    if(checkPropertyType.equals("Home")) {
                        Intent intent = new Intent(getApplicationContext(), ShowDetailsOfHouseproperty.class);
                        intent.putExtra("Property Location", model.getPropertyLocation());
                        intent.putExtra("House Property Bedrooms", model.getHousepropertybedrooms());
                        intent.putExtra("House Property Bathrooms", model.getHousepropertybathrooms());
                        intent.putExtra("House Property Price", model.getPropertyexpectedprice());
                        intent.putExtra("House Property Area", model.getPropertyarea());
                        intent.putExtra("ownermobilnumer", model.getOwnermobilnumer());
                        intent.putExtra("PropertyImages", propertyImageList);
                        startActivity(intent);
                    }else if(checkPropertyType.equals("Office")){
                        Intent intent = new Intent(getApplicationContext(), ShowDetailsOfOfficeProperty.class);
                        intent.putExtra("Property Location", model.getPropertyLocation());
                        intent.putExtra("Office Property Price", model.getPropertyexpectedprice());
//                        intent.putExtra("Office Property Area", model.getPropertyarea());
                        intent.putExtra("ownermobilnumer", model.getOwnermobilnumer());
                        intent.putExtra("PropertyImages", propertyImageList);
                        startActivity(intent);
                    }else if(checkPropertyType.equals("Farm")){
                        Intent intent = new Intent(getApplicationContext(), ShowDetailsOfFarmProperty.class);
                        intent.putExtra("Property Location", model.getPropertyLocation());
                        intent.putExtra("Property Type", model.getPropertyType());
                        intent.putExtra("Farm Property Price", model.getPropertyexpectedprice());
                        intent.putExtra("Farm Property Area", model.getPropertyarea());
                        intent.putExtra("ownermobilnumer", model.getOwnermobilnumer());
                        intent.putExtra("PropertyImages", propertyImageList);
                        startActivity(intent);
                    }else if(checkPropertyType.equals("Plot")){
                        Intent intent = new Intent(getApplicationContext(), ShowDetailsOfFarmProperty.class);
                        intent.putExtra("Property Location", model.getPropertyLocation());
                        intent.putExtra("Farm Property Price", model.getPropertyexpectedprice());
                        intent.putExtra("Farm Property Area", model.getPropertyarea());
                        intent.putExtra("ownermobilnumer", model.getOwnermobilnumer());
                        intent.putExtra("PropertyImages", propertyImageList);
                        startActivity(intent);
                    }
                });
            }

            @Override
            public PropertyHolder onCreateViewHolder(ViewGroup group, int i) {
                View view = LayoutInflater.from(group.getContext())
                        .inflate(R.layout.get_new_properties, group, false);

                return new PropertyHolder(view);
            }

            @Override
            public void onError(FirebaseFirestoreException e) {
                Log.e("error", e.getMessage());
            }
        };

        adapter.notifyDataSetChanged();
        propertyList.setAdapter(adapter);
    }

    public class PropertyHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.name)
        TextView propertyName;
        @BindView(R.id.image)
        CircleImageView imageView;
        @BindView(R.id.title)
        TextView propertySubtype;
        @BindView(R.id.company)
        TextView propertyID;
        public PropertyHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        adapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        adapter.stopListening();
    }
}