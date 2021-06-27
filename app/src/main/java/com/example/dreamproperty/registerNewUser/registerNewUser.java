package com.example.dreamproperty.registerNewUser;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.dreamproperty.R;
import com.example.dreamproperty.login.UserLogin;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class registerNewUser extends AppCompatActivity {

    EditText inputFullName, inputEmail, inputPassword, inputConfirmPassword, inputmobilenumber;
    Button signUpok;
    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
    ProgressDialog pd;
    FirebaseAuth mAuth;
    FirebaseUser mUser;
    FirebaseFirestore mstore;
    String userID;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_new_user);

        inputEmail = (EditText)findViewById(R.id.signupemail);
        inputPassword = (EditText)findViewById(R.id.signupassword);
        inputConfirmPassword = (EditText)findViewById(R.id.signupconfirmpass);
        inputFullName = (EditText)findViewById(R.id.signupfullname);
        inputmobilenumber = (EditText)findViewById(R.id.signupmobileNumber);

        pd = new ProgressDialog(this);
        mAuth = FirebaseAuth.getInstance();
        mstore = FirebaseFirestore.getInstance();
        mUser = mAuth.getCurrentUser();
        signUpok = (Button)findViewById(R.id.signupbtn);

        signUpok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PerformAuth();
            }
        });
    }

    private void PerformAuth() {
            String fullname = inputFullName.getText().toString().trim();
            String email = inputEmail.getText().toString();
            String password = inputPassword.getText().toString();
            String confirmpassword = inputConfirmPassword.getText().toString();
            String mobileNumber = inputmobilenumber.getText().toString().trim();

            if(!email.matches(emailPattern)){
                inputEmail.setError("Enter valid email");
            }else if(password.isEmpty() || password.length() < 6){
                inputPassword.setError("Enter proper password");
            }else if(!password.equals(confirmpassword)){
                inputConfirmPassword.setError("Password not matched");
            }else if(fullname.isEmpty()){
                inputFullName.setError("Please enter full name");
            }else if(mobileNumber.isEmpty() && mobileNumber.length() == 10){
                inputmobilenumber.setError("Please enter mobile number in 10 digits");
            }else{
                pd.setMessage("Please wait while Registration");
                pd.setTitle("Registration");
                pd.setCanceledOnTouchOutside(false);
                pd.show();

                mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            pd.dismiss();
                            userID = mAuth.getCurrentUser().getUid();
                            DocumentReference documentReference = mstore.collection("users").document(userID);
                            Map<String, Object> user = new HashMap<>();
                            user.put("fullName", fullname);
                            user.put("email", email);
                            user.put("mobileNumber", mobileNumber);
                            documentReference.set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {

                                }
                            });
                            sendUsertoLoginActivity();
                            Toast.makeText(registerNewUser.this,"Registration Completed!!!", Toast.LENGTH_LONG).show();
                        }else{
                            pd.dismiss();
                            Toast.makeText(registerNewUser.this, ""+task.getException(),Toast.LENGTH_LONG).show();
                        }
                    }
                });
            }
    }

    private void sendUsertoLoginActivity() {
        Intent i = new Intent(registerNewUser.this, UserLogin.class);
        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(i);
    }

}