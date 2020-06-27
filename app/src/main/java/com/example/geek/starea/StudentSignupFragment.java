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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.blogspot.atifsoftwares.circularimageview.CircularImageView;
import com.example.geek.starea.Auth.LoginActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;

import static android.app.Activity.RESULT_OK;
import static com.google.firebase.storage.FirebaseStorage.getInstance;


/**
 * A simple {@link Fragment} subclass.
 */
public class StudentSignupFragment extends Fragment {
    FirebaseAuth auth;
    FirebaseUser user;
    DatabaseReference databaseReference;
    FirebaseDatabase firebaseDatabase;
    StorageReference storageReference;
    // path
    String storagePath = "Users_Profile_Cover_Imgs/";
    private static final int CAMERA_REQUEST_CODE = 100;
    private static final int STORAGE_REQUEST_CODE = 200;
    private static final int IMAGE_PICK_GALLERY_CODE = 300;
    private static final int IMAGE_PICK_CAMERA_CODE = 400;
    // arrays of permissions to be requested
    String cameraPermissons[];
    String storagePermissons[];
    String uid;
    // uri of picked image
    Uri image_uri;
    //for chick profile or cover
    String profieOrCover = "photo";
    ProgressDialog pd;
    @BindView(R.id.profileIv)
    CircularImageView profileIv;
    @BindView(R.id.signup_input_university)
    AppCompatEditText universityInput;
    @BindView(R.id.faculty_input)
    AppCompatEditText facultyInput;
    @BindView(R.id.department_input)
    AppCompatEditText departmentInput;
    @BindView(R.id.sCode_input)
    AppCompatEditText sCodeInput;
    @BindView(R.id.gpa_input)
    AppCompatEditText gpaInput;
    @BindView(R.id.one_radio_btn)
    RadioButton oneRadioBtn;
    @BindView(R.id.two_radio_btn)
    RadioButton twoRadioBtn;
    @BindView(R.id.three_radio_btn)
    RadioButton threeRadioBtn;
    @BindView(R.id.four_radio_btn)
    RadioButton fourRadioBtn;
    @BindView(R.id.sign_up_btn)
    Button signUpBtn;
    String name;
    String email;
    String password;
    String accountType;
    String university;
    String faculty;
    String department;
    String sCode;
    String gpa;
    String level;

    public StudentSignupFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_student_signup, container, false);
        ButterKnife.bind(this, view);
        auth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("Users");
        storageReference = getInstance().getReference();
        // init arrays of permissions
        cameraPermissons = new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE};
        storagePermissons = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE};
        pd = new ProgressDialog(getActivity());
        final Bundle bundle = this.getArguments();
        name = bundle.getString("name");
        email = bundle.getString("email");
        password = bundle.getString("password");
        accountType = bundle.getString("accountType");
        profileIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showImagePicDialog();
            }
        });

        signUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                inputData();
            }
        });

        //create user
//                auth.createUserWithEmailAndPassword(bundle.getString("email"), bundle.getString("password"))
//                        .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
//                            @Override
//                            public void onComplete(@NonNull Task<AuthResult> task) {
//                                // Toast.makeText(getActivity(), "User Created Successfully", Toast.LENGTH_LONG).show();
//                                //     Toast.makeText(getActivity(), ""+bundle.getString("email") , Toast.LENGTH_LONG).show();
//
//                                // progressBar.setVisibility(View.GONE);
//                                // If sign in fails, display a message to the user. If sign in succeeds
//                                // the auth state listener will be notified and logic to handle the
//                                // signed in user can be handled in the listener.
//                                if (!task.isSuccessful()) {
//                                    Toast.makeText(getActivity(), "Authentication failed." + task.getException(),
//                                            Toast.LENGTH_SHORT).show();
//                                } else {
//                                    Toast.makeText(getActivity(), "User Created Successfully", Toast.LENGTH_LONG).show();
//                                     user = auth.getCurrentUser();
//                                     uid = user.getUid();
//                                    auth.getCurrentUser().sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
//                                        @Override
//                                        public void onComplete(@NonNull Task<Void> task) {
//                                            if (task.isSuccessful()) {
//                                                HashMap<Object, String> hashMap = new HashMap<>();
//                                                hashMap.put("email", bundle.getString("email"));
//                                                hashMap.put("uId", uid);
//                                                hashMap.put("name", bundle.getString("name"));
//                                                hashMap.put("onlineStatus", "online");
//                                                hashMap.put("typingTo", "noOne");
//                                                hashMap.put("phone", "");
//                                                hashMap.put("photo", "");
//                                                hashMap.put("cover", "");
//                                                hashMap.put("accountType", bundle.getString("accountType"));
//                                                FirebaseDatabase database = FirebaseDatabase.getInstance();
//                                                //path to store user data named "Users"
//                                                DatabaseReference reference = database.getReference("Users");
//                                                reference.child(uid).setValue(hashMap);
//
//
//                                                //  startActivity(new Intent(getActivity(), LoginActivity.class));
//
//
//                                            }
//                                        }
//                                    });
//
//                                }
//                            }
//                        });
//
//            }
//        });


        return view;
    }

    private void inputData() {
        university = universityInput.getText().toString().trim();
        faculty = facultyInput.getText().toString().trim();
        department = departmentInput.getText().toString().trim();
        sCode = sCodeInput.getText().toString().trim();
        gpa = gpaInput.getText().toString().trim();
        if (oneRadioBtn.isChecked()) {
            level = oneRadioBtn.getText().toString();
        } else if (twoRadioBtn.isChecked()) {
            level = twoRadioBtn.getText().toString();
        } else if (threeRadioBtn.isChecked()) {
            level = threeRadioBtn.getText().toString();
        } else if (fourRadioBtn.isChecked()) {
            level = fourRadioBtn.getText().toString();
        }


        if (TextUtils.isEmpty(university)) {
            Toast.makeText(getActivity(), "Enter your University!", Toast.LENGTH_SHORT).show();
            return;
        }

        if (TextUtils.isEmpty(faculty)) {
            Toast.makeText(getActivity(), "Enter your Faculty!", Toast.LENGTH_SHORT).show();
            return;
        }

        if (TextUtils.isEmpty(department)) {
            Toast.makeText(getActivity(), "Enter your Department!", Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(sCode)) {
            Toast.makeText(getActivity(), "Enter your Code!", Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(gpa)) {
            Toast.makeText(getActivity(), "Enter your GPA!", Toast.LENGTH_SHORT).show();
            return;
        }
        createAccount();

    }

    private void createAccount() {


        auth.createUserWithEmailAndPassword(email, password).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
            @Override
            public void onSuccess(AuthResult authResult) {
                saveFirebaseData();

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getActivity(), "Authentication failed.",
                        Toast.LENGTH_SHORT).show();

            }
        });


    }

//TODO: da ana ely hbwazak
    private void saveFirebaseData() {
//        user = auth.getCurrentUser();
        uid = auth.getUid();
        auth.getCurrentUser().sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    if (image_uri == null) {
                        HashMap<String, Object> hashMap = new HashMap<>();
                        hashMap.put("email", email);
                        hashMap.put("uId", uid);
                        hashMap.put("name", name);
                        hashMap.put("onlineStatus", "online");
                        hashMap.put("typingTo", "noOne");
                        hashMap.put("university", university);
                        hashMap.put("faculty", faculty);
                        hashMap.put("department", department);
                        hashMap.put("sCode", sCode);
                        hashMap.put("gpa", gpa);
                        hashMap.put("level", level);
                        hashMap.put("photo", "");
                        hashMap.put("cover", "");
                        hashMap.put("accountType", accountType);

                        //path to store user data named "Users"
                        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users");
                        reference.child(uid).setValue(hashMap).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(getActivity(), "User Created Successfully", Toast.LENGTH_LONG).show();
                                startActivity(new Intent(getActivity(), LoginActivity.class));
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {

                            }
                        });
                    } else uploadProfileCoverPhoto(image_uri);
                }
            }
        });
    }


    private boolean chickStoragePermission() {
        // chick storage permission enable or not
        boolean result = ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE) == (PackageManager.PERMISSION_GRANTED);
        return result;
    }

    private void requestStoragePermission() {
        //request runtime Storage permission
        requestPermissions(storagePermissons, STORAGE_REQUEST_CODE);
    }

    private boolean chickCameraPermission() {
        // chick storage permission enable or not

        boolean result1 = ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE) == (PackageManager.PERMISSION_GRANTED);
        boolean result = ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.CAMERA) == (PackageManager.PERMISSION_GRANTED);

        return result && result1;
    }

    private void requestCameraPermission() {
        //request runtime Storage permission
        requestPermissions(cameraPermissons, CAMERA_REQUEST_CODE);
    }

    private void showImagePicDialog() {
        // show dialog containing gallery and camera to pick a photo
        // option to show in dialog
        String options[] = {" Camera ", "Gallery"};
        //Alert dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        //set Title
        builder.setTitle("Pick Photo From");
        // set items to dialog
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // handle dialog items click
                if (which == 0) {
                    // Camera click
                    if (!chickCameraPermission()) {
                        requestCameraPermission();
                    } else {
                        pickFromCamera();
                    }

                } else if (which == 1) {
                    // Gallery click
                    if (!chickStoragePermission()) {
                        requestStoragePermission();
                    } else {
                        pickFromGallery();
                    }
                }


            }
        });
        // create and show dialog
        builder.create().show();

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        // handle permissions cases allow or deny
        switch (requestCode) {
            case CAMERA_REQUEST_CODE: {
                if (grantResults.length > 0) {
                    boolean cameraAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    boolean writeStorageAccepted = grantResults[1] == PackageManager.PERMISSION_GRANTED;
                    if (cameraAccepted && writeStorageAccepted) {
                        // permission allowed
                        pickFromCamera();
                    } else {
                        Toast.makeText(getActivity(), "Please enable Camera and Storage Permissions ", Toast.LENGTH_LONG).show();
                    }
                }

            }
            break;
            case STORAGE_REQUEST_CODE: {
                if (grantResults.length > 0) {
                    boolean writeStorageAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    if (writeStorageAccepted) {
                        // permission allowed
                        pickFromGallery();
                    } else {
                        Toast.makeText(getActivity(), "Please enable Storage Permissions ", Toast.LENGTH_LONG).show();
                    }
                }

            }
            break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        // called after picking image from camera or gallery
        if (resultCode == RESULT_OK) {
            if (requestCode == IMAGE_PICK_GALLERY_CODE) {
                //image is picked from gallery get uri
                image_uri = data.getData();
                profileIv.setImageURI(image_uri);
                // uploadProfileCoverPhoto(image_uri);

            }
            if (requestCode == IMAGE_PICK_CAMERA_CODE) {
                // image is picked from cam get uri
                //  uploadProfileCoverPhoto(image_uri);
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void uploadProfileCoverPhoto(Uri uri) {
        // show progress
        pd.show();
        user = auth.getCurrentUser();
        String filePathAndName = storagePath + "" + profieOrCover + "_" + user.getUid();
        StorageReference storageReference1 = storageReference.child(filePathAndName);
        storageReference1.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();
                while (!uriTask.isSuccessful()) ;
                final Uri downloadUri = uriTask.getResult();
                //check image is uploaded or not
                if (uriTask.isSuccessful()) {
                    HashMap<String, Object> hashMap = new HashMap<>();
                    hashMap.put("email", email);
                    hashMap.put("uId", uid);
                    hashMap.put("name", name);
                    hashMap.put("onlineStatus", "online");
                    hashMap.put("typingTo", "noOne");
                    hashMap.put("university", university);
                    hashMap.put("faculty", faculty);
                    hashMap.put("department", department);
                    hashMap.put("sCode", sCode);
                    hashMap.put("gpa", gpa);
                    hashMap.put("level", level);
                    hashMap.put("photo", "" + downloadUri);
                    hashMap.put("cover", "");
                    hashMap.put("accountType", accountType);
                    FirebaseDatabase database = FirebaseDatabase.getInstance();
                    //path to store user data named "Users"
                    DatabaseReference reference = database.getReference("Users");
                    reference.child(uid).setValue(hashMap).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            pd.dismiss();
                            Toast.makeText(getActivity(), "User Created Successfully", Toast.LENGTH_LONG).show();
                            startActivity(new Intent(getActivity(), LoginActivity.class));
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            pd.dismiss();
                            Toast.makeText(getActivity(), "Error  Updating Image ..", Toast.LENGTH_LONG).show();
                        }
                    });
                    // if user change hiss name also change it from his posts
//                    if (profieOrCover.equals("photo")) {
//                        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Posts");
//                        Query query = ref.orderByChild("uid").equalTo(uid);
//                        query.addValueEventListener(new ValueEventListener() {
//                            @Override
//                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                                for (DataSnapshot ds : dataSnapshot.getChildren()) {
//                                    String child = ds.getKey();
//                                    dataSnapshot.getRef().child(child).child("uDp").setValue(downloadUri.toString());
//                                }
//
//                            }
//
//                            @Override
//                            public void onCancelled(@NonNull DatabaseError databaseError) {
//
//                            }
//                        });
//                        // if photo changed in comments
//                        ref.addListenerForSingleValueEvent(new ValueEventListener() {
//                            @Override
//                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                                for (DataSnapshot ds : dataSnapshot.getChildren()) {
//                                    String child = ds.getKey();
//                                    if (dataSnapshot.child(child).hasChild("Comments")) {
//                                        String child1 = dataSnapshot.child(child).getKey();
//                                        Query child2 = FirebaseDatabase.getInstance().getReference("Posts").child(child1)
//                                                .child("Comments").orderByChild("uid").equalTo(uid);
//                                        child2.addValueEventListener(new ValueEventListener() {
//                                            @Override
//                                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                                                for (DataSnapshot ds : dataSnapshot.getChildren()) {
//                                                    String child = ds.getKey();
//                                                    dataSnapshot.getRef().child(child).child("uDp").setValue(downloadUri.toString());
//                                                }
//
//                                            }
//
//                                            @Override
//                                            public void onCancelled(@NonNull DatabaseError databaseError) {
//
//                                            }
//                                        });
//                                    }
//                                }
//
//                            }
//
//                            @Override
//                            public void onCancelled(@NonNull DatabaseError databaseError) {
//
//                            }
//                        });
//
//                    }
                } else {
                    // not uploaded
                    pd.dismiss();
                    Toast.makeText(getActivity(), "Error occurred ", Toast.LENGTH_LONG).show();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                pd.dismiss();
                Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_LONG).show();

            }
        });


    }

    private void pickFromGallery() {
        // pick from gallery
        Intent galleryIntent = new Intent(Intent.ACTION_PICK);
        galleryIntent.setType("image/*");
        startActivityForResult(galleryIntent, IMAGE_PICK_GALLERY_CODE);

    }

    private void pickFromCamera() {
        // intent of picking image from camera
        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.TITLE, "temp pic ");
        values.put(MediaStore.Images.Media.DESCRIPTION, "temp desc");
        // put image uri
        image_uri = getActivity().getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
        // intint to start camera
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, image_uri);
        startActivityForResult(cameraIntent, IMAGE_PICK_CAMERA_CODE);


    }
}
