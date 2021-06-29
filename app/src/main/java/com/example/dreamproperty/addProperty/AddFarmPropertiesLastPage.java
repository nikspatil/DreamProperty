package com.example.dreamproperty.addProperty;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.OpenableColumns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.dreamproperty.R;
import com.example.dreamproperty.buyProperty.BuyProperty;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AddFarmPropertiesLastPage extends AppCompatActivity {
    private static final int RESULT_LOAD_IMAGE = 1 ;
    Button propertyuploadphoto;
    RecyclerView displayselectedphotoRV;
    List<String> fileNameList;
    List<String> fileDoneList;
    List<Uri> AllImageUri;
    List<String> savedImagesUri;
    UploadListAdapter uploadListAdapter;
    StorageReference mStorage;
    CollectionReference reference;
    ImageView finishpropertybtn;
    int counter;
    Uri fileUri;
    FirebaseAuth mAuth;
    FirebaseUser mUser;
    FirebaseFirestore mstore;
    EditText expectedprice, farmarea, ownermobinum;
    String userId;
    String getPropertytype, getpropertysubtype, getpropertylocation;
    String getpropertyexpectedprice, getownermobinumber, getfarmarea;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_farm_properties);

        Intent intent = getIntent();
        getPropertytype = intent.getStringExtra("Property Type");
        getpropertysubtype = intent.getStringExtra("Property Subtype");
        getpropertylocation = intent.getStringExtra("Property Location");

        propertyuploadphoto = findViewById(R.id.uploadpropertyphotobtn);
        displayselectedphotoRV = findViewById(R.id.uploadphotorv);
        expectedprice = (EditText)findViewById(R.id.pricedetailsed);
        ownermobinum = (EditText)findViewById(R.id.ownermobilenumberedtv);
        farmarea = (EditText)findViewById(R.id.farmareainputedt);
        finishpropertybtn = findViewById(R.id.finishbtn);

        fileNameList = new ArrayList<>();
        fileDoneList = new ArrayList<>();
        AllImageUri = new ArrayList<Uri>();
        savedImagesUri = new ArrayList<>();
        mAuth = FirebaseAuth.getInstance();
        mstore = FirebaseFirestore.getInstance();
        mUser = mAuth.getCurrentUser();
        mStorage = FirebaseStorage.getInstance().getReference();
        reference = mstore.collection("usersProperty");
        uploadListAdapter = new UploadListAdapter(fileNameList, fileDoneList);

        //RecyclerView
        displayselectedphotoRV.setLayoutManager(new LinearLayoutManager(this));
        //displayselectedphotoRV.setHasFixedSize(true);
        //displayselectedphotoRV.setNestedScrollingEnabled(false);
        displayselectedphotoRV.setAdapter(uploadListAdapter);

        propertyuploadphoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent();
                i.setType("image/*");
                i.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
                i.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(i, "Select House Photos"), RESULT_LOAD_IMAGE);
            }
        });
        userId = mAuth.getCurrentUser().getUid();
        finishpropertybtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getpropertyexpectedprice = expectedprice.getText().toString();
                getownermobinumber = ownermobinum.getText().toString();
                getfarmarea = farmarea.getText().toString();
                addpropertuinfo(view);
                //startActivityForResult(Intent.createChooser(i, "Select House Photos"), RESULT_LOAD_IMAGE);
            }
        });
    }

    private void addpropertuinfo(View view) {
        if (AllImageUri.size() != 0) {
            final ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setMessage("Uploaded 0/"+AllImageUri.size());
            progressDialog.setCanceledOnTouchOutside(false); //Remove this line if you want your user to be able to cancel upload
            progressDialog.setCancelable(false);    //Remove this line if you want your user to be able to cancel upload
            progressDialog.show();
            for (int i = 0; i < AllImageUri.size(); i++) {
                final int finalI = i;
                //mStorage.child("userData/").child(fileNameList.get(i)).putFile(AllImageUri.get(i)).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                mStorage.child("usersProperty/"+mAuth.getCurrentUser().getUid()).child(fileNameList.get(i)).putFile(AllImageUri.get(i)).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                        if (task.isSuccessful()){
                            mStorage.child("usersProperty/"+mAuth.getCurrentUser().getUid()).child(fileNameList.get(finalI)).getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                                @Override
                                public void onComplete(@NonNull Task<Uri> task) {
                                    counter++;
                                    progressDialog.setMessage("Uploaded "+counter+"/"+fileNameList.size());
                                    if (task.isSuccessful()){
                                        savedImagesUri.add(task.getResult().toString());
                                        fileDoneList.add(finalI, "Property upload successfully!!");
                                        uploadListAdapter.notifyDataSetChanged();
                                    }else{
                                        mStorage.child("usersProperty/"+mAuth.getCurrentUser().getUid()).child(fileNameList.get(finalI)).delete();
                                        Toast.makeText(AddFarmPropertiesLastPage.this, "Couldn't save "+fileNameList.get(finalI), Toast.LENGTH_SHORT).show();
                                    }
                                    if (counter == fileNameList.size()){
                                        saveImageDataToFirestore(progressDialog);
                                    }
                                }
                            });
                        }else{
                            progressDialog.setMessage("Uploaded "+counter+"/"+fileNameList.size());
                            counter++;
                            Toast.makeText(AddFarmPropertiesLastPage.this, "Couldn't upload "+fileNameList.get(finalI), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        } else {

        }
    }

    private void saveImageDataToFirestore(ProgressDialog progressDialog) {
        progressDialog.setMessage("Saving uploaded images...");
        Map<String, Object> dataMap = new HashMap<>();
        //Below line of code will put your images list as an array in firestore
        if(getPropertytype.equals("Farm")) {
            dataMap.put("propertyType", getPropertytype);
            dataMap.put("propertySubType", getpropertysubtype);
            dataMap.put("propertyLocation", getpropertylocation);
            dataMap.put("propertyexpectedprice", getpropertyexpectedprice);
            dataMap.put("propertyarea",getfarmarea);
            dataMap.put("ownermobilnumer", getownermobinumber);
            dataMap.put("images", savedImagesUri);
        }else if(getPropertytype.equals("Plot")) {
            dataMap.put("propertyType", getPropertytype);
            dataMap.put("propertySubType", getpropertysubtype);
            dataMap.put("propertyLocation", getpropertylocation);
            dataMap.put("propertyexpectedprice", getpropertyexpectedprice);
            dataMap.put("propertyarea",getfarmarea);
            dataMap.put("ownermobilnumer", getownermobinumber);
            dataMap.put("images", savedImagesUri);
        }
        reference.add(dataMap).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
            @Override
            public void onSuccess(DocumentReference documentReference) {
                progressDialog.dismiss();
                Toast.makeText(AddFarmPropertiesLastPage.this, "Your property added successfully!!", Toast.LENGTH_SHORT).show();
                Intent i = new Intent(AddFarmPropertiesLastPage.this , BuyProperty.class);
//                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(i);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                progressDialog.dismiss();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK) {

            if (data.getClipData() != null) {

                int totalItemsSelected = data.getClipData().getItemCount();

                for (int i = 0; i < totalItemsSelected; i++) {

                    fileUri = data.getClipData().getItemAt(i).getUri();
                    AllImageUri.add(fileUri);
                    System.out.println("Images URI");
                    System.out.println(AllImageUri.get(i));
                    String fileName = getFileName(fileUri);

                    fileNameList.add(fileName);
                    fileDoneList.add("uploading");
                    uploadListAdapter.notifyDataSetChanged();
                }

            } else if (data.getData() != null) {

                Toast.makeText(AddFarmPropertiesLastPage.this, "Selected Single File", Toast.LENGTH_SHORT).show();

            }

        }
    }

    public String getFileName(Uri uri) {
        String result = null;
        if (uri.getScheme().equals("content")) {
            Cursor cursor = getContentResolver().query(uri, null, null, null, null);
            try {
                if (cursor != null && cursor.moveToFirst()) {
                    result = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
                }
            } finally {
                cursor.close();
            }
        }
        if (result == null) {
            result = uri.getPath();
            int cut = result.lastIndexOf('/');
            if (cut != -1) {
                result = result.substring(cut + 1);
            }
        }
        return result;
    }
}
