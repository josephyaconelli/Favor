package com.josephyaconelli.favor;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.josephyaconelli.favor.favorhome.FavorHome;

public class RegisterUserActivity extends AppCompatActivity {

    // Views and Widgets
    EditText passwordEditText, emailEditText, usernameEditText;
    TextView createAccountBtn;

    // firebase authentication
    FirebaseAuth mAuth;
    FirebaseAuth.AuthStateListener mAuthListener;

    // progress dialog
    ProgressDialog mProgressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_user);

        // set views and widgets
        passwordEditText = (EditText) findViewById(R.id.passwordRegisterEditText);
        emailEditText    = (EditText) findViewById(R.id.emailRegisterEditText);
        usernameEditText = (EditText) findViewById(R.id.usernameRegisterEditText);

        createAccountBtn = (TextView) findViewById(R.id.createAccountRegisterButton);

        // set progress dialog
        mProgressDialog = new ProgressDialog(this);

        // Firebase instance
        mAuth = FirebaseAuth.getInstance();

        // Firebase listener
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {

                // create user account
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if(user != null)
                {
                    Intent moveToHome = new Intent(RegisterUserActivity.this, FavorHome.class);
                    moveToHome.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(moveToHome);
                }

            }
        };


        // listeners
        createAccountBtn.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {

                // show progress dialog
                mProgressDialog.setTitle("Create Account");
                mProgressDialog.setMessage("Account being created...");
                mProgressDialog.show();

                createAccount();
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();

        mAuth.addAuthStateListener(mAuthListener);
    }


    @Override
    protected void onStop() {
        super.onStop();

        mAuth.removeAuthStateListener(mAuthListener);
    }

    // create user account
    private void createAccount(){

        String userEmail, userPassword, userName;

        userEmail    = emailEditText.getText().toString().trim();
        userPassword = passwordEditText.getText().toString().trim();
        userName     = usernameEditText.getText().toString().trim();

        if( !TextUtils.isEmpty(userEmail)
                && !TextUtils.isEmpty(userPassword)
                && !TextUtils.isEmpty(userName))
        {
            mAuth.createUserWithEmailAndPassword(userEmail, userPassword).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful()){
                        Toast.makeText(RegisterUserActivity.this, "Account created sucessfully!", Toast.LENGTH_LONG).show();
                        mProgressDialog.dismiss();

                        Intent moveToHome = new Intent(RegisterUserActivity.this, FavorHome.class);
                        moveToHome.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(moveToHome);

                    } else {
                        Toast.makeText(RegisterUserActivity.this, task.getException().getMessage(), Toast.LENGTH_LONG).show();
                        mProgressDialog.dismiss();
                    }
                }
            });
        }


    }
}
