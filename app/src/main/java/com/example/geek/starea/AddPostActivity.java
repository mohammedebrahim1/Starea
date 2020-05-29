package com.example.geek.starea;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.geek.starea.Auth.LoginActivity;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;

public class AddPostActivity extends AppCompatActivity {
    FirebaseAuth firebaseAuth;
    DatabaseReference userDbRef ;
    TextView userTv;
    EditText postEt ;
    ImageView userIv , postIv ;
    FloatingActionButton addPhotoBt;
    Toolbar toolbar;
    // path
    String storagePath = "Posts/" + "post_";
    ProgressDialog pd;
    //permissions constants
    private static final int CAMERA_REQUEST_CODE = 100;
    private static final int STORAGE_REQUEST_CODE = 200;
    private static final int IMAGE_PICK_GALLERY_CODE = 400;
    private static final int IMAGE_PICK_CAMERA_CODE = 300;
    // arrays of permissions to be requested
    String cameraPermissons [] ;
    String storagePermissons [] ;
    String name , email , uid , dp;
    // uri of picked image
    Uri image_uri = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_post);
        toolbar = findViewById(R.id.post_toolbar);
       setSupportActionBar(toolbar);
       pd = new ProgressDialog(this);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        // Instance
        firebaseAuth = FirebaseAuth.getInstance();
        chickUserStatus();
        // get user data
        userDbRef = FirebaseDatabase.getInstance().getReference("Users");
        Query query = userDbRef.orderByChild("email").equalTo(email);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()){
                    name = "" + ds.child("name").getValue();
                    email = "" + ds.child("email").getValue();
                    dp = "" + ds.child("photo").getValue();
                }
                // set views
                userTv.setText(name);
                try {
                    Picasso.get().load(dp).placeholder(R.drawable.post_home_deafault_img).into(userIv);

                } catch (Exception e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        //init views
        userTv = findViewById(R.id.userNameTv);
        userIv = findViewById(R.id.post_home_user_img);
        postIv = findViewById(R.id.pImageIv);
        postEt = findViewById(R.id.postContentEt);
        addPhotoBt = findViewById(R.id.postBt);

        addPhotoBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showImagePickDialog();

            }
        });

    }

    private void showImagePickDialog() {
        // show dialog containing gallery and camera to pick a photo
        // option to show in dialog
        String options [] = {" Camera " , "Gallery"};
        //Alert dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        //set Title
        builder.setTitle("Pick Photo From");
        // set items to dialog
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // handle dialog items click
                if (which == 0){
                    // Camera click
                    if (!chickCameraPermission()){
                        requestCameraPermission();
                    }
                    else {
                        pickFromCamera();
                    }

                }
                else if (which == 1){
                    // Gallery click
                    if (!chickStoragePermission()){
                        requestStoragePermission();
                    }
                    else {
                        pickFromGallery();
                    }
                }


            }
        });
        // create and show dialog
        builder.create().show();


    }
    private  boolean chickStoragePermission(){
        // chick storage permission enable or not
        boolean result = ContextCompat.checkSelfPermission(this , Manifest.permission.WRITE_EXTERNAL_STORAGE) == (PackageManager.PERMISSION_GRANTED);
        return result;
    }
    private void requestStoragePermission(){
        //request runtime Storage permission
        requestPermissions( storagePermissons , STORAGE_REQUEST_CODE);
    }
    private  boolean chickCameraPermission(){
        // chick storage permission enable or not

        boolean result1 = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == (PackageManager.PERMISSION_GRANTED);
        boolean result = ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == (PackageManager.PERMISSION_GRANTED);

        return result && result1;
    }
    private void requestCameraPermission(){
        //request runtime Storage permission
        requestPermissions(cameraPermissons , CAMERA_REQUEST_CODE);
    }
    private void pickFromGallery() {
        // pick from gallery
        Intent galleryIntent = new Intent(Intent.ACTION_PICK);
        galleryIntent.setType("image/*");
        startActivityForResult(galleryIntent , IMAGE_PICK_GALLERY_CODE);

    }

    private void pickFromCamera() {
        // intent of picking image from camera
        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.TITLE , "temp pic ");
        values.put(MediaStore.Images.Media.DESCRIPTION , "temp desc");
        // put image uri
        image_uri = this.getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI , values);
        // intint to start camera
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT , image_uri);
        startActivityForResult(cameraIntent , IMAGE_PICK_CAMERA_CODE);


    }
    private void uploadData(final String postContent, String uri) {
        pd.setMessage("Publishing Post...");
        pd.show();
        // data
        final String timestamp = String.valueOf(System.currentTimeMillis());
        String filePathAndName = storagePath + timestamp;

        if (!uri.equals("no Image")){
            // post with photo
            StorageReference storageReference = FirebaseStorage.getInstance().getReference().child(filePathAndName);
            storageReference.putFile(Uri.parse(uri)).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Task<Uri> uriTask  = taskSnapshot.getStorage().getDownloadUrl();
                    while (!uriTask.isSuccessful());
                    String downloadUri = uriTask.getResult().toString();
                    //check image is uploaded or not
                    if (uriTask.isSuccessful()){
                        // image uploaded
                        // update url in user's database
                        HashMap<String , Object> results = new HashMap<>();
                        results.put("uid" ,uid );
                        results.put("uName" ,name );
                        results.put("uEmail" , email );
                        results.put("uDp" ,dp );
                        results.put("pId" ,timestamp );
                        results.put("pContent" ,postContent );
                        results.put("pImage" , downloadUri.toString());
                        results.put("pTime" , timestamp);
                        results.put("pRates" , "0");
                        // path to store data in firebase
                        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Posts");
                        reference.child(timestamp).setValue(results).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                pd.dismiss();
                                Toast.makeText(AddPostActivity.this , "Published" , Toast.LENGTH_LONG).show();
                                // reset view
                                postEt.setText("");
                                postIv.setImageURI(null);
                                postIv.setVisibility(View.GONE);
                                image_uri = null;


                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                pd.dismiss();
                                Toast.makeText(AddPostActivity.this , "Error  Updating Image .." , Toast.LENGTH_LONG).show();

                            }
                        });

                    }


                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    pd.dismiss();

                }
            });


        }
        else {
            HashMap<String , Object> results = new HashMap<>();
            results.put("uid" ,uid );
            results.put("uName" ,name );
            results.put("uEmail" , email );
            results.put("uDp" ,dp );
            results.put("pId" ,timestamp );
            results.put("pContent" ,postContent );
            results.put("pImage" , "no Image" );
            results.put("pTime" , timestamp);
            results.put("pRates" , "0");
            // path to store data in firebase
            DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Posts");
            reference.child(timestamp).setValue(results).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    pd.dismiss();
                    Toast.makeText(AddPostActivity.this , "Published" , Toast.LENGTH_LONG).show();
                    // reset view
                    postEt.setText("");
                    postIv.setImageURI(null);
                    postIv.setVisibility(View.GONE);
                    image_uri = null;


                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    pd.dismiss();
                    Toast.makeText(AddPostActivity.this , "Error  Updating Image .." , Toast.LENGTH_LONG).show();

                }
            });


        }


    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        // handle permissions cases allow or deny
        switch (requestCode){
            case CAMERA_REQUEST_CODE:{
                if (grantResults.length > 0){
                    boolean cameraAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    boolean writeStorageAccepted = grantResults[1] == PackageManager.PERMISSION_GRANTED;
                    if (cameraAccepted && writeStorageAccepted){
                        // permission allowed
                        pickFromCamera();
                    }
                    else {
                        Toast.makeText(this , "Please enable Camera and Storage Permissions " , Toast.LENGTH_LONG).show();
                    }
                }

            }
            break;
            case  STORAGE_REQUEST_CODE: {
                if (grantResults.length > 0){
                    boolean writeStorageAccepted = grantResults[1] == PackageManager.PERMISSION_GRANTED;
                    if (writeStorageAccepted){
                        // permission allowed
                        pickFromGallery();
                    }
                    else {
                        Toast.makeText(this  , "Please enable Storage Permissions " , Toast.LENGTH_LONG).show();
                    }
                }

            }
            break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        // called after picking image from camera or gallery
        if (resultCode == RESULT_OK){
            if (requestCode == IMAGE_PICK_GALLERY_CODE){
                //image is picked from gallery get uri
                image_uri = data.getData();
                //  set
                postIv.setVisibility(View.VISIBLE);
                postIv.setImageURI(image_uri);


            }
            if (requestCode == IMAGE_PICK_CAMERA_CODE){
                // image is picked from cam get uri
                postIv.setVisibility(View.VISIBLE);
                postIv.setImageURI(image_uri);
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }



    private void chickUserStatus(){
        //get current user
        FirebaseUser user = firebaseAuth.getCurrentUser();
        if (user != null){
            email = user.getEmail();
            uid = user.getUid();

        }
        else {
            startActivity(new Intent(this, LoginActivity.class));
            finish();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        chickUserStatus();
    }

    @Override
    protected void onResume() {
        super.onResume();
        chickUserStatus();
    }

    @Override
    public boolean onNavigateUp() {
        onBackPressed();
        return super.onNavigateUp();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.add_post_actionbar , menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.post_action){
            //get data
            String postContent = postEt.getText().toString().trim();
            if (TextUtils.isEmpty(postContent)){
                Toast.makeText(AddPostActivity.this , "Type Some Thing ... " , Toast.LENGTH_LONG).show();
            }
            if (image_uri == null){
                // post without photo
                uploadData(postContent , "no Image");
            }
            else {
                // post with photo
                uploadData(postContent , String.valueOf(image_uri));


            }
        }
        return super.onOptionsItemSelected(item);
    }


}
