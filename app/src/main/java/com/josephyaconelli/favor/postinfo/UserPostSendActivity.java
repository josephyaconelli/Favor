package com.josephyaconelli.favor.postinfo;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.josephyaconelli.favor.R;
import com.josephyaconelli.favor.favorhome.FavorHome;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class UserPostSendActivity extends AppCompatActivity {

    // request codes
    private static final int REQUEST_CAMERA = 3;
    private static final int SELECT_FILE = 2;
    private static final int PERMISSION_REQUEST_CAMERA = 4;
    private static final int PERMISSION_REQUEST_EXTERNAL_WRITE = 5;

    // favor points
    String mFavorPoints = "0";

    // profile image uri and File
    Uri mCurrentPhotoPath;
    Uri imageHoldUri = null;

    // progress dialog
    ProgressDialog mProgress;


    // layout elements
    TextView favorPointsTextView, publishFavorBtn, addPhotoText;
    EditText favorTitleEditText, favorDescriptionEditText;
    ImageView favorImage;


    // firebase auth
    FirebaseAuth mAuth;
    FirebaseAuth.AuthStateListener mAuthListener;

    // firebase database
    DatabaseReference mUserDatabase, mFavorsDatabase;
    StorageReference mStorageRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_post_send);


        // get references
        favorDescriptionEditText = (EditText) findViewById(R.id.aboutFavorEditText);
        favorPointsTextView = (TextView) findViewById(R.id.favorPointsView);
        favorTitleEditText = (EditText) findViewById(R.id.favorTitleEditText);
        favorDescriptionEditText = (EditText) findViewById(R.id.aboutFavorEditText);
        publishFavorBtn = (TextView) findViewById(R.id.publishFavor);
        favorImage = (ImageView) findViewById(R.id.FavorImageView);
        addPhotoText = (TextView) findViewById(R.id.addPhotoText);


        // listeners
        favorImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                pickFavorPicture();

            }
        });


        publishFavorBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String favorTitle, favorDescription;

                favorTitle = favorTitleEditText.getText().toString().trim();
                favorDescription = favorDescriptionEditText.getText().toString().trim();

                if (!TextUtils.isEmpty(favorTitle) && !TextUtils.isEmpty(favorDescription)) {
                    if(imageHoldUri != null) {

                        mProgress.setTitle("Saving Favor");
                        mProgress.setMessage("Please wait...");
                        mProgress.show();

                        StorageReference mChildStorage = mStorageRef.child("Favors").child(imageHoldUri.getLastPathSegment());
                        String profilePicUrl = imageHoldUri.getLastPathSegment();

                        mChildStorage.putFile(imageHoldUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                                taskSnapshot.getMetadata().getReference().getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                    @Override
                                    public void onSuccess(Uri uri) {
                                        mFavorsDatabase.child("title").setValue(favorTitle);
                                        mFavorsDatabase.child("description").setValue(favorDescription);
                                        mFavorsDatabase.child("user_id").setValue(mAuth.getCurrentUser().getUid());
                                        mFavorsDatabase.child("img_url").setValue(uri.toString());
                                        mFavorsDatabase.child("points").setValue(R.integer.init_points);

                                        mProgress.dismiss();

                                        finish();

                                        Intent moveToHome = new Intent(UserPostSendActivity.this, FavorHome.class);
                                        moveToHome.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                        startActivity(moveToHome);
                                    }
                                });



                            }
                        });

                    } else {
                        Toast.makeText(UserPostSendActivity.this, "Please add a profile picture", Toast.LENGTH_LONG).show();
                    }
                } else {

                    Toast.makeText(UserPostSendActivity.this, "Please enter a display name and about", Toast.LENGTH_LONG).show();

                }


            }
        });

        // setup progress dialog
        mProgress = new ProgressDialog(this);

        // setup firebase auth
        mAuth = FirebaseAuth.getInstance();

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {

                // check if/which user is logged in
                FirebaseUser user = firebaseAuth.getCurrentUser();

                if(user != null){

                    finish();
                    Intent moveToHome  = new Intent(UserPostSendActivity.this, FavorHome.class);
                    moveToHome.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(moveToHome);

                }

            }
        };

        // setup firebase database
        mUserDatabase = FirebaseDatabase.getInstance().getReference().child("users").child(mAuth.getCurrentUser().getUid());
        mFavorsDatabase = FirebaseDatabase.getInstance().getReference().child("favors");
        mStorageRef = FirebaseStorage.getInstance().getReference();

        mUserDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                if(! dataSnapshot.child("points").exists()) {
                    mUserDatabase.child("points").setValue(R.integer.init_points);
                } else {

                    mFavorPoints = String.valueOf(dataSnapshot.child("points").getValue());
                    favorPointsTextView.setText(mFavorPoints);


                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        // set favor points





    }




    private void pickFavorPicture() {

        // display choose photo dialog
        final CharSequence[] items = {getResources().getString(R.string.take_photo),
                getResources().getString(R.string.from_library),
                getResources().getString(R.string.cancel)};
        AlertDialog.Builder builder = new AlertDialog.Builder(UserPostSendActivity.this);
        builder.setTitle(getResources().getString(R.string.add_profile_picture));

        // set items and listeners
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                if(items[which].equals(getResources().getString(R.string.take_photo))){

                    cameraIntent();

                } else if (items[which].equals(getResources().getString(R.string.from_library))) {

                    galleryIntent();

                } else if (items[which].equals(getResources().getString(R.string.cancel))) {

                    dialog.dismiss();

                }
            }
        });

        builder.show();



    }

    /**
     *  Goes to choose photo from Gallery
     */
    private void galleryIntent() {

        Log.d("gola", "entered here");
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");

        startActivityForResult(intent, SELECT_FILE);

    }

    /**
     * Goes to take photo using camera
     */
    private void cameraIntent() {

        Log.d("gola", "entered here");
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        if(ContextCompat.checkSelfPermission(UserPostSendActivity.this, android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(UserPostSendActivity.this,
                    new String[]{android.Manifest.permission.CAMERA},
                    PERMISSION_REQUEST_CAMERA);
        } else {

            if(ContextCompat.checkSelfPermission(UserPostSendActivity.this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(UserPostSendActivity.this,
                        new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        PERMISSION_REQUEST_EXTERNAL_WRITE);
            } else {

                if (intent.resolveActivity(getPackageManager()) != null) {

                    File photoFile = null;

                    try {
                        photoFile = createImageFile();
                    } catch (IOException ex) {
                        Toast.makeText(UserPostSendActivity.this, "Could not save photo...", Toast.LENGTH_LONG).show();
                    }

                    if (photoFile != null) {
                        Uri photoURI = FileProvider.getUriForFile(this,
                                "com.josephyaconelli.favor.profileuser.fileprovider",
                                photoFile);
                        intent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                        mCurrentPhotoPath = photoURI;
                        Log.d("save_photo", mCurrentPhotoPath.toString());
                        startActivityForResult(intent, REQUEST_CAMERA);

                    }
                }

            }
        }

    }

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "Favor_JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = Uri.fromFile(image);
        return image;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);


        // save URI from gallery
        if((requestCode == SELECT_FILE || requestCode == REQUEST_CAMERA) && resultCode == RESULT_OK){
            Uri imageUri = null;
            if(requestCode == REQUEST_CAMERA) {
                imageUri = mCurrentPhotoPath;
            } else {
                imageUri = data.getData();
            }

            CropImage.activity(imageUri)
                    .setGuidelines(CropImageView.Guidelines.ON)
                    .setAspectRatio(1,1)
                    .start(this);

        }

        //image crop library code

        if(requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);

            if(resultCode == RESULT_OK) {
                imageHoldUri = result.getUri();

                favorImage.setImageURI(imageHoldUri);
                addPhotoText.setVisibility(View.INVISIBLE);

            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }

        }

    }

}
