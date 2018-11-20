package com.josephyaconelli.favor;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.josephyaconelli.favor.favorhome.FavorHome;

import org.w3c.dom.Text;

public class MainActivity extends AppCompatActivity {

    // Fields for views and widgets
    EditText userEmailEditText, userPasswordEditText;
    TextView loginLayoutBtn, createAccountLayoutBtn;

    // Firebase authentication
    FirebaseAuth mAuth;
    FirebaseAuth.AuthStateListener mAuthListener;

    // progress dialog
    ProgressDialog mProgressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //assign IDs
        userEmailEditText    = (EditText) findViewById(R.id.loginEmailEditText);
        userPasswordEditText = (EditText) findViewById(R.id.loginPasswordEditText);

        loginLayoutBtn = (TextView) findViewById(R.id.loginButton);
        createAccountLayoutBtn = (TextView) findViewById(R.id.createAccountButton);

        //assign progress dialog

        mProgressDialog = new ProgressDialog(this);

        // set firebase auth
        mAuth = FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {

                // checking user presence
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if( user != null )
                {
                    Intent moveToHome = new Intent(MainActivity.this, FavorHome.class);
                    moveToHome.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(moveToHome);

                }

            }
        };

        mAuth.addAuthStateListener(mAuthListener);

        // listeners
        createAccountLayoutBtn.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, RegisterUserActivity.class));
            }
        });

        loginLayoutBtn.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                mProgressDialog.setTitle("Logging In");
                mProgressDialog.setMessage("Please wait...");
                mProgressDialog.show();
                loginUser();
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

    // login user using entered credentials
    private void loginUser(){
        String userEmail, userPassword;

        userEmail    = userEmailEditText.getText().toString().trim();
        userPassword = userPasswordEditText.getText().toString().trim();

        if( !TextUtils.isEmpty(userEmail) && !TextUtils.isEmpty(userPassword)){
            mAuth.signInWithEmailAndPassword(userEmail, userPassword).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    mProgressDialog.dismiss();
                    if(task.isSuccessful()){
                        Intent moveToHome = new Intent(MainActivity.this, FavorHome.class);
                        moveToHome.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(moveToHome);
                    } else {
                        Toast.makeText(MainActivity.this, task.getException().getLocalizedMessage(), Toast.LENGTH_LONG).show();
                    }

                }
            });
        } else {
            mProgressDialog.dismiss();
            Toast.makeText(MainActivity.this, "Please enter email and password", Toast.LENGTH_LONG).show();

        }

    }

}
