package com.example.geek.starea.Chat;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.format.DateFormat;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.geek.starea.Adapters.AdapterChat;
import com.example.geek.starea.Auth.LoginActivity;
import com.example.geek.starea.Models.ModelChat;
import com.example.geek.starea.Models.ModelUser;
import com.example.geek.starea.R;
import com.example.geek.starea.notifications.APIService;
import com.example.geek.starea.notifications.Client;
import com.example.geek.starea.notifications.Data;
import com.example.geek.starea.notifications.Response;
import com.example.geek.starea.notifications.Sender;
import com.example.geek.starea.notifications.Token;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.sql.Time;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.zip.Inflater;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import retrofit2.Call;
import retrofit2.Callback;

public class ChatActivity extends AppCompatActivity {
    // init views
    Toolbar toolbar;
    RecyclerView recyclerView;
    ImageView profileIv;
    TextView nameTv , userStatusTv;
    EditText messageEt;
    ImageButton sendBtn;
    FirebaseAuth firebaseAuth;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference usersDbRefrence;
    String hisUid;
    String myUid;
    String hisImage;
    // for check if user see the message
    ValueEventListener seenListener;
    DatabaseReference userRefForSeen;
    List<ModelChat> chatList;
    AdapterChat adapterChat;
    // for Notifications
    APIService apiService;
    boolean notify = false;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        toolbar = findViewById(R.id.tool_bar);
        setSupportActionBar(toolbar);
        toolbar.setTitle("");
        recyclerView = findViewById(R.id.chat_recycler_view);
        profileIv = findViewById(R.id.profileIv);
        nameTv = findViewById(R.id.name_tv);
        userStatusTv = findViewById(R.id.user_status);
        messageEt = findViewById(R.id.messageEt);
        sendBtn = findViewById(R.id.send_btn);
        // layout for recycler view
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setStackFromEnd(true);
        // recycler prop
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(linearLayoutManager);
        // create api service
        apiService = Client.getRetrofit("https://fcm.googleapis.com/").create(APIService.class);
        // get user id to retrive his profile image
        Intent intent = getIntent();
        hisUid = intent.getStringExtra("hisUid");
        // init firebase Auth
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        usersDbRefrence = firebaseDatabase.getReference("Users");
        chickUserStatus();
        // Search User info
        Query userQuery  = usersDbRefrence.orderByChild("uId").equalTo(hisUid);
        // get user name and photo
        userQuery.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                // search until data received
                for (DataSnapshot ds : dataSnapshot.getChildren()){
                    // get data
                    String name = "" + ds.child("name").getValue();
                    hisImage = "" + ds.child("photo").getValue();
                    String typingStatus = "" + ds.child("typingTo").getValue();
                    String onlineStatus = "" + ds.child("onlineStatus").getValue();
                    // set data
                    nameTv.setText(name);
                    if (onlineStatus.equals("online")) {
                        userStatusTv.setText(onlineStatus);
                    }
                    // convert timestamp to get user last seen
                    else {
                        Calendar calendar = Calendar.getInstance(Locale.ENGLISH);
                        calendar.setTimeInMillis(Long.parseLong(onlineStatus));
                        String dateTime = DateFormat.format("dd/MM/yyyy hh:mm aa" , calendar).toString();
                        userStatusTv.setText("Last seen : "+dateTime);
                    }
                    // chick value of  typing
                    if (typingStatus.equals(myUid)){
                        userStatusTv.setText("typing...");
                    }
                    else {
                        if (onlineStatus.equals("online")) {
                            userStatusTv.setText(onlineStatus);
                        }
                        // convert timestamp to get user last seen
                        else {
                            Calendar calendar = Calendar.getInstance(Locale.ENGLISH);
                            calendar.setTimeInMillis(Long.parseLong(onlineStatus));
                            String dateTime = DateFormat.format("dd/MM/yyyy hh:mm aa" , calendar).toString();
                            userStatusTv.setText("Last seen : "+dateTime);
                        }

                    }
                    try {
                        Picasso.get().load(hisImage).placeholder(R.drawable.ic_default_image).into(profileIv);
                    } catch (Exception e) {
                        Picasso.get().load(R.drawable.ic_default_image).into(profileIv);

                    }
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        // click btn to send message
        sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                notify = true;
                // get message fro edit text
                String message = messageEt.getText().toString().trim();
                // chick if empty
                if (TextUtils.isEmpty(message)){
                    Toast.makeText(ChatActivity.this, "Can't send empty message !" , Toast.LENGTH_LONG).show();
                }
                else {
                    sendMessage(message);

                }
                //Reset edit text after send message
                messageEt.setText("");
            }
        });
        // chick edit text change listener
        messageEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.toString().trim().length() == 0){
                    chickTypingStatus("noOne");
                }
                else {
                    chickTypingStatus(hisUid);
                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        readMessage();
        seenMessage();

    }

    private void seenMessage() {
        userRefForSeen = FirebaseDatabase.getInstance().getReference("Chat");
        seenListener = userRefForSeen.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()){
                    ModelChat modelChat = ds.getValue(ModelChat.class);
                    if (modelChat.getReceiver().equals(myUid) && modelChat.getSender().equals(hisUid)){
                        HashMap<String , Object> hasSeenHashMap  = new HashMap<>();
                        hasSeenHashMap.put("gotIt" , true);
                        ds.getRef().updateChildren(hasSeenHashMap);
                    }
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void readMessage() {
        chatList = new ArrayList<>();
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Chat");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                chatList.clear();
                for (DataSnapshot ds : dataSnapshot.getChildren()){
                    ModelChat modelChat = ds.getValue(ModelChat.class);
                    if (modelChat.getReceiver().equals(myUid) && modelChat.getSender().equals(hisUid)
                        || modelChat.getReceiver().equals(hisUid) && modelChat.getSender().equals(myUid)){
                        chatList.add(modelChat);
                    }
                    adapterChat = new AdapterChat(ChatActivity.this , chatList , hisImage);
                    adapterChat.notifyDataSetChanged();
                    // set adapter to rec
                    recyclerView.setAdapter(adapterChat);

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private void sendMessage(final String message) {
        String timestamp = String.valueOf(System.currentTimeMillis());

        HashMap<String , Object> hashMap = new HashMap<>();

        hashMap.put("sender" , myUid);
        hashMap.put("receiver" , hisUid);
        hashMap.put("message" , message);
        hashMap.put("timestamp" , timestamp);
        hashMap.put("Seen" , false);
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();

        databaseReference.child("Chat").push().setValue(hashMap);

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Users").child(myUid);
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                ModelUser modelUser = dataSnapshot.getValue(ModelUser.class);
                if (notify){
                    sendNotification(hisUid , modelUser.getName() , message);
                }
                notify = false;
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }

    private void sendNotification(final String hisUid, final String name, final String message) {
        DatabaseReference allTokens = FirebaseDatabase.getInstance().getReference("Tokens");
        Query query = allTokens.orderByKey().equalTo(hisUid);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()){
                    Token token = ds.getValue(Token.class);
                    Data data = new Data(myUid , name + ":" + message , "New Message" , hisUid , R.drawable.ic_default_image);
                    Sender sender = new Sender(data , token.getToken());
                    apiService.sendNotifications(sender)
                            .enqueue(new Callback<Response>() {
                                @Override
                                public void onResponse(Call<Response> call, retrofit2.Response<Response> response) {
                                    Toast.makeText(ChatActivity.this , "" + response.message() , Toast.LENGTH_LONG).show();

                                }

                                @Override
                                public void onFailure(Call<Response> call, Throwable t) {

                                }
                            });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private void chickUserStatus(){
        //get current user
        FirebaseUser user = firebaseAuth.getCurrentUser();
        if (user != null){
            myUid = user.getUid(); // current user id

        }
        else {
            startActivity(new Intent(this, LoginActivity.class));
            finish();
        }
    }
    private void  chickOnlineStatus(String status){
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Users").child(myUid);
        HashMap<String , Object> hashMap = new HashMap<>();
        hashMap.put("onlineStatus" , status);
        databaseReference.updateChildren(hashMap);
    }
    private void  chickTypingStatus(String typing){
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Users").child(myUid);
        HashMap<String , Object> hashMap = new HashMap<>();
        hashMap.put("typingTo" , typing);
        databaseReference.updateChildren(hashMap);
    }

    @Override
    protected void onStart() {
        super.onStart();
        // make status online
        chickOnlineStatus("online");
    }

    @Override
    protected void onPause() {
        super.onPause();
        // get time
        String timestamp = String.valueOf(System.currentTimeMillis());
        // make user offline with last seen timestamp
        chickOnlineStatus(timestamp);
        userRefForSeen.removeEventListener(seenListener);
        chickTypingStatus("noOne");
    }

    @Override
    protected void onResume() {
        super.onResume();
        chickOnlineStatus("online");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.action_bar , menu);
        // hide search and addpost view
        menu.findItem(R.id.search_action).setVisible(false);
        menu.findItem(R.id.add_post).setVisible(false);
        return super.onCreateOptionsMenu(menu);
    }
}
