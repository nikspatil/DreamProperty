package com.example.dreamproperty;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.dreamproperty.registerNewUser.registerNewUser;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.Map;

public class EditUserProfile extends AppCompatActivity {

    EditText editFullName, editMobilNumber, editEmailaddress;
    Button updateProfile;
    ImageView editProfileImage;
    FirebaseAuth mAuth;
    FirebaseUser mUser;
    FirebaseFirestore mstore;
    StorageReference storageReference;
    public static final int READ_EXTERNAL_STORAGE = 0;
    private static final int GALLERY_INTENT = 2;
    String userId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_user_profile);

        editFullName = findViewById(R.id.profieFullName);
        editMobilNumber = findViewById(R.id.profileMobilenumber);
        editEmailaddress = findViewById(R.id.profileEmail);
        editProfileImage = findViewById(R.id.profileicon);
        updateProfile = findViewById(R.id.profileupdatebutton);
        mAuth = FirebaseAuth.getInstance();
        mstore = FirebaseFirestore.getInstance();
        mUser = mAuth.getCurrentUser();
        storageReference = FirebaseStorage.getInstance().getReference();

        StorageReference profileRef = storageReference.child("users/"+mAuth.getCurrentUser().getUid()+"/profile.jpg");
        profileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Picasso.get().load(uri).into(editProfileImage);
            }
        });

        userId = mAuth.getCurrentUser().getUid();
        DocumentReference documentReference = mstore.collection("users").document(userId);
        documentReference.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException error) {
                editFullName.setText(documentSnapshot.getString("fullName"));
                editMobilNumber.setText(documentSnapshot.getString("mobileNumber"));
                editEmailaddress.setText(documentSnapshot.getString("email"));
            }
        });
        editProfileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //open gallery
                //Check for Runtime Permission
                if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.READ_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED)
                {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
                    {
                        requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, READ_EXTERNAL_STORAGE);
                    }
                }
                else
                {
                    callgalary();
                }
            }
        });


        updateProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(editFullName.getText().toString().isEmpty() || editMobilNumber.getText().toString().isEmpty() || editEmailaddress.getText().toString().isEmpty()){
                    Toast.makeText(EditUserProfile.this,"one or many fields are empty", Toast.LENGTH_LONG).show();
                }

                final String email = editEmailaddress.getText().toString();
                mUser.updateEmail(email).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        DocumentReference docref = mstore.collection("users").document(mUser.getUid());
                        Map<String, Object> user = new HashMap<>();
                        user.put("fullName", editFullName.getText().toString() );
                        user.put("email", email);
                        user.put("mobileNumber", editMobilNumber.getText().toString());
                        docref.update(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(EditUserProfile.this,"Profiles updated successfully!!!", Toast.LENGTH_LONG).show();

                            }
                        });

                    }
                });

            }
        });
    }

    private void callgalary() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, GALLERY_INTENT);
    }


    //Check for Runtime Permissions for Storage Access
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case READ_EXTERNAL_STORAGE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                    callgalary();
                return;
        }
        Toast.makeText(getApplicationContext(), "...", Toast.LENGTH_SHORT).show();
    }



    //After Selecting image from gallery image will directly uploaded to Firebase Database
    //and Image will Show in Image View
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == GALLERY_INTENT && resultCode == RESULT_OK) {

            Uri mImageUri = data.getData();
//            editProfileImage.setImageURI(mImageUri);
            uploadImageToFireStorage(mImageUri);
        }
    }


    private void uploadImageToFireStorage(Uri imageUri) {
        StorageReference fileref = storageReference.child("users/"+mAuth.getCurrentUser().getUid()+"/profile.jpg");
        fileref.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                fileref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        Picasso.get().load(uri).into(editProfileImage);
                    }
                });
                //Toast.makeText(EditUserProfile.this,"Update new profile image successfully!!!", Toast.LENGTH_LONG).show();
            }
        });
    }
}