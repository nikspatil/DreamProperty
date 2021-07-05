package com.example.dreamproperty;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.net.Uri;
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

import com.example.dreamproperty.buyProperty.Note;
import com.example.dreamproperty.buyProperty.PropertyDetailsAdapter;
import com.example.dreamproperty.buyProperty.ShowDetailsOfFarmProperty;
import com.example.dreamproperty.buyProperty.ShowDetailsOfHouseproperty;
import com.example.dreamproperty.buyProperty.ShowDetailsOfOfficeProperty;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.firebase.ui.firestore.SnapshotParser;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.smarteist.autoimageslider.SliderView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

public class ViewFavouritesProperties extends AppCompatActivity {

    @BindView(R.id.progress_bar)
    ProgressBar progressBar;
    RecyclerView propertyList;
    String getPropertytype;
    ArrayList<String> propertyImageList;
    private FirebaseFirestore db;
    private FirestoreRecyclerAdapter adapter;
    LinearLayoutManager linearLayoutManager;

    CollectionReference reference;
    FirebaseAuth mAuth;
    FirebaseUser mUser;
    FirebaseFirestore mstore;
    String userID;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_favourites_properties);

        propertyList = (RecyclerView)findViewById(R.id.fvproperty_list);
        linearLayoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);
        propertyList.setLayoutManager(linearLayoutManager);
        db = FirebaseFirestore.getInstance();

        mstore = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();
        userID = mUser.getUid();
        reference = mstore.collection("users");
        propertyImageList = new ArrayList<String>();

        Intent intent = getIntent();
        getPropertytype = intent.getStringExtra("Property Type");
        getPropertyList();

    }

    private void getPropertyList(){
        Query query = db.collection("users").document(userID).collection("FavouritesList");
        FirestoreRecyclerOptions<Note> response = new FirestoreRecyclerOptions.Builder<Note>()
                  .setQuery(query, new SnapshotParser<Note>() {
                      @NonNull
                      @Override
                      public Note parseSnapshot(@NonNull DocumentSnapshot snapshot) {
                          Note note = snapshot.toObject(Note.class);
                          note.setDocumentId(snapshot.getId());
                          return note;
                      }
                  })
                  .setLifecycleOwner(this)
                  .build();
//                .setQuery(query, Note.class)
//                .build();

        adapter = new FirestoreRecyclerAdapter<Note, ViewFavouritesProperties.PropertyHolder>(response) {
            @Override
            public void onBindViewHolder(ViewFavouritesProperties.PropertyHolder holder, int position, Note model) {

                List<String> images = new ArrayList<>();
                for (String propertyimages : model.getImages()){
                    images.add(propertyimages);
                    System.out.println(images);
                }
                holder.propertyType.setText(model.getPropertyType());
                holder.propertyLocation.setText(model.getPropertyLocation());
                holder.propertyprice.setText(model.getPropertyexpectedprice());
                System.out.println(model.getDocumentId());
                String pArea = model.getPropertyarea();
                if(pArea == null) {
                    holder.propertyarea.setVisibility(View.INVISIBLE);
                    holder.propertyText.setVisibility(View.INVISIBLE);
                }else{
                    holder.propertyarea.setText(model.getPropertyarea());

                }
                String getlatlong = model.getPropertyLatLong();
                String mapURI = "google.navigation:q=" + getlatlong + "&model=d";
                holder.getpropertyDirection.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent i = new Intent(Intent.ACTION_VIEW,
                                Uri.parse(mapURI));
                        i.setPackage("com.google.android.apps.maps");
                        startActivity(i);
                    }
                });

                holder.calltoOwner.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent i = new Intent(Intent.ACTION_DIAL);
                        String getnumber ="tel:" +model.getOwnermobilnumer();
                        i.setData(Uri.parse(getnumber));
                        startActivity(i);
                    }
                });
                PropertyDetailsAdapter propertyDetailsAdapter = new PropertyDetailsAdapter(images);
                holder.fvpropertyimgslider.setSliderAdapter(propertyDetailsAdapter);


                holder.delfvItem.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        DocumentSnapshot snapshot = (DocumentSnapshot) adapter.getSnapshots().getSnapshot(position);
                        System.out.println(snapshot.getId());
                        String getfvitemId = (String) snapshot.get("propertyId");
                        reference.document(userID).collection("FavouritesList").document(snapshot.getId()).delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(getApplicationContext(), "Property deleted from your favourite list!!!",Toast.LENGTH_LONG).show();
                            }
                        });
                        reference.document(userID).collection("Favourites").document(getfvitemId).delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {

                            }
                        });
                        adapter.notifyDataSetChanged();
                    }
                });
            }

            @Override
            public ViewFavouritesProperties.PropertyHolder onCreateViewHolder(ViewGroup group, int i) {
                View view = LayoutInflater.from(group.getContext())
                        .inflate(R.layout.getfavouritesproperties, group, false);
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
        @BindView(R.id.propertyType)
        TextView propertyType;
        @BindView(R.id.propertyLocation)
        TextView propertyLocation;
        @BindView(R.id.propertyprice)
        TextView propertyprice;
        @BindView(R.id.propertyOwnernumber)
        TextView propertyOwnernumber;
        @BindView(R.id.propertyarea)
        TextView propertyarea;
        @BindView(R.id.propertytxt)
        TextView propertyText;
        @BindView(R.id.fvpropertyimages)
        SliderView fvpropertyimgslider;
        @BindView(R.id.getdirectioncardview)
        CardView getpropertyDirection;
        @BindView(R.id.callpropertyownercardview)
        CardView calltoOwner;
        @BindView(R.id.deletefvitem)
        ImageButton delfvItem;
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

    @Override
    public void onBackPressed() {
        finish();
        startActivity(new Intent(ViewFavouritesProperties.this , Dashboard.class));
        super.onBackPressed();
    }
}