package com.example.pantauhidroponik.API;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface InterfaceAPI {
    @GET("history")
    Call<ResponseBody> getHistory(@Query("awal") String str, @Query("akhir") String str2, @Query("namaNode") String str3);

    @GET("monitoring")
    Call<ResponseBody> getMonitoring(@Query("namaNode") String str);

    @GET("nodes")
    Call<ResponseBody> getNode();

    @GET("statistics")
    Call<ResponseBody> getStatistics(@Query("namaNode") String str, @Query("parameter") String str2);

    @FormUrlEncoded
    @POST("authenticate")
    Call<ResponseBody> loginRequest(@Field("username") String str, @Field("password") String str2);
}
