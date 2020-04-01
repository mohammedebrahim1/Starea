package com.example.geek.starea.notifications;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface APIService {
    @Headers({
            "Content-Type:application/json",
            "Authorization:key=AAAAlSSOsyQ:APA91bGCCC15PwCQautUDUVLdtV6lMuvsYFUbTnDbnK-W_-ZIReAYMR73kSR73EzJF0M_5z6ul1gppY9xzlMLHDJGq0bI_88mg9jrW7R01HA3iNeaOj1l8S1Gy4H6ZSfaE69-XIgUbxO"
    })
    @POST("fcm/send")
    Call<Response> sendNotifications(@Body Sender body);
}
