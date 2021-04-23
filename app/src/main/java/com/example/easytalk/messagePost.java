package com.example.easytalk;

import com.example.easytalk.model.message;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface messagePost {
    @Multipart
    @POST("/msgboard/release")
    Call<message> submitMessage(
            @Part() MultipartBody.Part author,
            @Part() MultipartBody.Part content
    );
}
