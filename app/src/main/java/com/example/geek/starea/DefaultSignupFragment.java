package com.example.geek.starea;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.Toast;

import androidx.appcompat.widget.AppCompatEditText;
import androidx.core.widget.ContentLoadingProgressBar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.firebase.auth.FirebaseAuth;

import butterknife.BindView;
import butterknife.ButterKnife;


public class DefaultSignupFragment extends Fragment {

    public DefaultSignupFragment() {
    }
    @BindView(R.id.signup_input_name)
    AppCompatEditText inputName;
    @BindView(R.id.email)
    AppCompatEditText inputEmail;
    @BindView(R.id.password)
    AppCompatEditText inputPassword;
    @BindView(R.id.signup_input_confirm_password)
    AppCompatEditText inputConfirmPassword;
    @BindView(R.id.student_radio_btn)
    RadioButton studentRadioBtn;
    @BindView(R.id.Instructor_radio_btn)
    RadioButton instructorRadioBtn;
    @BindView(R.id.next_btn)
    Button btnSignUp;
    @BindView(R.id.signupPb)
    ContentLoadingProgressBar signupPb;
    private FirebaseAuth auth;
    String email;
    String password;
    String confirmPassword;
    String name;
    String accountType;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.fragment_default_signup , container , false);
        ButterKnife.bind(this , view);
        //Get Firebase auth instance
        auth = FirebaseAuth.getInstance();


        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                email = inputEmail.getText().toString().trim();
                password = inputPassword.getText().toString().trim();
                confirmPassword = inputConfirmPassword.getText().toString().trim();
                name = inputName.getText().toString().trim();
                if (studentRadioBtn.isChecked()) {
                    accountType = studentRadioBtn.getText().toString();
                } else if (instructorRadioBtn.isChecked()) {
                    accountType = instructorRadioBtn.getText().toString();
                }


                if (TextUtils.isEmpty(email)) {
                    Toast.makeText(getActivity(), "Enter email address!", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (TextUtils.isEmpty(password)) {
                    Toast.makeText(getActivity(), "Enter password!", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (password.length() < 6) {
                    Toast.makeText(getActivity(), "Password too short, enter minimum 6 characters!", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(confirmPassword)) {
                    Toast.makeText(getActivity(), "Password dosen't match !", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (!password.equals(confirmPassword)) {
                    Toast.makeText(getActivity(), "Password dosen't match !", Toast.LENGTH_SHORT).show();
                    return;


                }
                Bundle bundle = new Bundle();
                bundle.putString("name" , name);
                bundle.putString("email" , email);
                bundle.putString("password" , password);
                bundle.putString("accountType" , accountType);
                StudentSignupFragment fragment = new StudentSignupFragment();
                fragment.setArguments(bundle);
                FragmentTransaction ft1 = getActivity().getSupportFragmentManager().beginTransaction();
                ft1.replace(R.id.signup_content, fragment, "");
                ft1.commit();


//                signupPb.setVisibility(View.VISIBLE);
//                //create user
//                auth.createUserWithEmailAndPassword(email, password)
//                        .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
//                            @Override
//                            public void onComplete(@NonNull Task<AuthResult> task) {
//                                Toast.makeText(getActivity(), "User Created Successfully", Toast.LENGTH_LONG).show();
//                                // progressBar.setVisibility(View.GONE);
//                                // If sign in fails, display a message to the user. If sign in succeeds
//                                // the auth state listener will be notified and logic to handle the
//                                // signed in user can be handled in the listener.
//                                if (!task.isSuccessful()) {
//                                    Toast.makeText(getActivity(), "Authentication failed." + task.getException(),
//                                            Toast.LENGTH_SHORT).show();
//                                } else {
//                                    FirebaseUser user = auth.getCurrentUser();
//                                    final String uId = user.getUid();
//                                    auth.getCurrentUser().sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
//                                        @Override
//                                        public void onComplete(@NonNull Task<Void> task) {
//                                            if (task.isSuccessful()){
//                                                HashMap<Object, String> hashMap = new HashMap<>();
//                                                hashMap.put("email", email);
//                                                hashMap.put("uId", uId);
//                                                hashMap.put("name", name);
//                                                hashMap.put("onlineStatus", "online");
//                                                hashMap.put("typingTo", "noOne");
//                                                hashMap.put("phone", "");
//                                                hashMap.put("photo", "");
//                                                hashMap.put("cover", "");
//                                                hashMap.put("accountType", accountType);
//                                                FirebaseDatabase database = FirebaseDatabase.getInstance();
//                                                //path to store user data named "Users"
//                                                DatabaseReference reference = database.getReference("Users");
//                                                reference.child(uId).setValue(hashMap);
//
//
//                                              //  startActivity(new Intent(getActivity(), LoginActivity.class));
//
//
//                                            }
//                                        }
//                                    });
//
//                                }
//                            }
//                        });

            }
        });


        return view;

    }
    @Override
    public void onResume() {
        super.onResume();
        signupPb.setVisibility(View.GONE);
    }
}
