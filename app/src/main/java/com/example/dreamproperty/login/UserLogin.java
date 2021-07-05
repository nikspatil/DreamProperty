package com.example.dreamproperty.login;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.dreamproperty.Dashboard;
import com.example.dreamproperty.R;
import com.example.dreamproperty.ViewFavouritesProperties;
import com.example.dreamproperty.addProperty.AddProperties;
import com.example.dreamproperty.addProperty.AddPropertiesNext;
import com.example.dreamproperty.addProperty.AddpropertiesLastPage;
import com.example.dreamproperty.buyProperty.BuyProperty;
import com.example.dreamproperty.buyProperty.Buyproperties;
import com.example.dreamproperty.buyProperty.SimplelearnFirestore;
import com.example.dreamproperty.registerNewUser.registerNewUser;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

public class UserLogin extends AppCompatActivity {
    private static final int RC_SIGN_IN = 101 ;
    EditText loginEmail, loginPassword;
    Button loginButton;
    ImageView googleSignIn, facebookSignIn;
    TextView signuphinttv;
    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
    ProgressDialog pd, pd1;
    FirebaseAuth mAuth;
    FirebaseUser mUser;
    GoogleSignInClient mGoogleSignInClient;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_login);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);


        loginEmail= findViewById(R.id.loginemail);
        loginPassword = findViewById(R.id.loginpassword);
        loginButton = findViewById(R.id.loginbutton);
        signuphinttv = findViewById(R.id.signuphint);
        googleSignIn = (ImageView)findViewById(R.id.googlesignbtn);
        facebookSignIn = (ImageView) findViewById(R.id.facebooksigninbtn);

        pd = new ProgressDialog(this);
        pd1 = new ProgressDialog(this);
        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();
        signuphinttv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(UserLogin.this, registerNewUser.class));
            }
        });

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                performLogin();
            }
        });

        //sign in with google

        // Configure Google Sign In
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        googleSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pd1.setMessage("Google sign In...");
                pd1.show();
                Intent signInIntent = mGoogleSignInClient.getSignInIntent();
                startActivityForResult(signInIntent, RC_SIGN_IN);
            }
        });

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuthWithGoogle(account.getIdToken());
            } catch (ApiException e) {

                // Google Sign In failed, update UI appropriately
                Toast.makeText(UserLogin.this, "Google sign in failed", Toast.LENGTH_LONG).show();
                pd1.dismiss();
                finish();
            }
        }
    }
    private void firebaseAuthWithGoogle(String idToken) {
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            pd1.dismiss();
                            Toast.makeText(UserLogin.this,"Welcome to Dream Property!!!", Toast.LENGTH_LONG).show();
                            FirebaseUser user = mAuth.getCurrentUser();
                            updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Toast.makeText(UserLogin.this, "Sign in with google failed",Toast.LENGTH_LONG).show();
                            pd1.dismiss();
                            finish();
                        }
                    }
                });
    }

    private void updateUI(FirebaseUser user) {
        Intent i = new Intent(UserLogin.this, Dashboard.class);
        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(i);
    }

    private void performLogin() {
        String email = loginEmail.getText().toString();
        String password = loginPassword.getText().toString();

        if (!email.matches(emailPattern)) {
            loginEmail.setError("Enter valid email");
        } else if (password.isEmpty() || password.length() < 6) {
            loginPassword.setError("Enter proper password");
        } else {
            pd.setMessage("Please wait while Login...");
            pd.setTitle("Login");
            pd.setCanceledOnTouchOutside(false);
            pd.show();

            mAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful()){
                        pd.dismiss();
                        sendUsertoDashboardActivity();
                        Toast.makeText(UserLogin.this,"Welcome to Dream Property!!!", Toast.LENGTH_LONG).show();
                    }else{
                        pd.dismiss();
                        Toast.makeText(UserLogin.this, ""+task.getException(),Toast.LENGTH_LONG).show();
                    }

                }
            });

        }
    }

    private void sendUsertoDashboardActivity() {
        Intent i = new Intent(UserLogin.this, Dashboard.class);
        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(i);
    }
}