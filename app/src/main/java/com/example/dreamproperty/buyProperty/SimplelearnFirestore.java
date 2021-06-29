package com.example.dreamproperty.buyProperty;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.example.dreamproperty.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class SimplelearnFirestore extends AppCompatActivity {

    private EditText editTextTitle;
    private EditText editTextDescription;
    private EditText editTextImages;
    private TextView textViewData;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference notebookRef = db.collection("usersProperty");
    //private DocumentReference noteRef = db.document("Notebook/My First Note");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_simplelearn_firestore);

        editTextTitle = findViewById(R.id.edit_text_title);
        editTextDescription = findViewById(R.id.edit_text_description);
        editTextImages = findViewById(R.id.proimages);
        textViewData = findViewById(R.id.text_view_data);

    }

    @Override
    protected void onStart() {
        super.onStart();
        notebookRef.addSnapshotListener(this, new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(QuerySnapshot queryDocumentSnapshots, FirebaseFirestoreException e) {
//                if (e != null) {
//                    return;
//                }
//                String data = "";
//                for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
//                    Note note = documentSnapshot.toObject(Note.class);
//                    note.setDocumentId(documentSnapshot.getId());
//                    String documentId = note.getDocumentId();
//                    String title = note.getPropertyType();
//                    String description = note.getPropertySubType();
//                    data += "ID: " + documentId
//                            + "\nTitle: " + title + "\nDescription: " + description + "\n\n";
//                }
//                textViewData.setText(data);
            }
        });
    }

    public void loadNotes(View v) {
        notebookRef.whereEqualTo("propertyType", "Home").get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        String data = "";
                        for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                            Note note = documentSnapshot.toObject(Note.class);
                            note.setDocumentId(documentSnapshot.getId());
                            String documentId = note.getDocumentId();
                            String title = note.getPropertyType();
                            String description = note.getPropertySubType();
                            data += "ID: " + documentId
                                    + "\nTitle: " + title + "\nDescription: " + description + "\n\n";
                            for (String propertyimages : note.getImages()){
                                data+= "\n" + propertyimages + "\n";
                            }

                        }
                        textViewData.setText(data);

                    }
                });
    }
}