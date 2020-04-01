package com.example.geek.starea;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;

import com.example.geek.starea.Auth.LoginActivity;
import com.example.geek.starea.Chat.ChatListFragment;
import com.example.geek.starea.Fragments.ClassRoomsFragment;
import com.example.geek.starea.Fragments.HomeFragment;
import com.example.geek.starea.Fragments.ProfileFragment;
import com.example.geek.starea.notifications.Token;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentTransaction;

public class MainActivity extends AppCompatActivity {


    private BottomNavigationView bottomNavigationBar;
    FirebaseUser user ;
    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    String mUID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_main);
        bottomNavigationBar = findViewById(R.id.bottom_nav);
        bottomNavigationBar.setOnNavigationItemSelectedListener(selectedListener);
        Toolbar toolbar = findViewById(R.id.main_toolbar);
        setSupportActionBar(toolbar);

        // default on start
        HomeFragment fragment1 = new HomeFragment();
        FragmentTransaction ft1 = getSupportFragmentManager().beginTransaction();
        ft1.replace(R.id.content , fragment1 , "");
        ft1.commit();
        checkUserStatus();
        // update token
        updateToken(FirebaseInstanceId.getInstance().getToken());
    }
    public void updateToken (String token){
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Tokens");
        Token mToken = new Token(token);
        reference.child(mUID).setValue(mToken);

    }


    private BottomNavigationView.OnNavigationItemSelectedListener selectedListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                    // handle item click
                    switch (menuItem.getItemId()){
                        case R.id.nav_home :
                            // handle home fragment transaction
                            HomeFragment fragment1 = new HomeFragment();
                            FragmentTransaction ft1 = getSupportFragmentManager().beginTransaction();
                            ft1.replace(R.id.content , fragment1 , "");
                            ft1.commit();
                        return true;

                        case R.id.nav_user :
                            // handle Profile fragment transaction
                            ProfileFragment fragment2 = new ProfileFragment();
                            FragmentTransaction ft2 = getSupportFragmentManager().beginTransaction();
                            ft2.replace(R.id.content , fragment2 , "");
                            ft2.commit();
                            return true;

                        case R.id.nav_classrooms :
                            // handle ClassRooms fragment transaction
                            ClassRoomsFragment fragment3 = new ClassRoomsFragment();
                            FragmentTransaction ft3 = getSupportFragmentManager().beginTransaction();
                            ft3.replace(R.id.content, fragment3 , "");
                            ft3.commit();
                            return true;

                        case R.id.nav_chat :
                            // handle Chat fragment transaction
                            ChatListFragment fragment4 = new ChatListFragment();
                            FragmentTransaction ft4 = getSupportFragmentManager().beginTransaction();
                            ft4.replace(R.id.content , fragment4 , "");
                            ft4.commit();
                            return true;

                    }
                    return false;
                }
            };
    private void checkUserStatus(){
        user = firebaseAuth.getCurrentUser();
        if (user != null){
            mUID = user.getUid();
            // save currentuser id in shared
            SharedPreferences sp = getSharedPreferences("SP_USER" , MODE_PRIVATE);
            SharedPreferences.Editor editor = sp.edit();
            editor.putString("Current_USERID" , mUID);
            editor.apply();

        }
        else {
            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    @Override
    protected void onStart() {
        super.onStart();
        checkUserStatus();
    }

    @Override
    protected void onResume() {
        super.onResume();
        checkUserStatus();
    }
}
