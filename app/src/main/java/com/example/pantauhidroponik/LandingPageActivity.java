package com.example.pantauhidroponik;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.pantauhidroponik.API.InterfaceAPI;
import com.example.pantauhidroponik.API.RetrofitClientInstance;

import java.io.IOException;
import java.util.Objects;

import okhttp3.ResponseBody;

import org.json.JSONException;
import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class LandingPageActivity extends AppCompatActivity implements View.OnClickListener {
    // atribut progress dialog
    protected ProgressDialog loadingContent;

    // atribut button
    protected Button login;

    // atribut context
    protected Context mContext;

    // atribut edir text
    protected EditText password;
    protected EditText username;

    // atribut parameter atau key untuk request ke Api
    protected String usrname;
    protected String pass;

    /* access modifiers changed from: protected */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_landing_page);
        this.username = this.findViewById(R.id.et_userLGN);
        this.password = this.findViewById(R.id.et_passLGN);
        Button button = findViewById(R.id.btn_login);
        this.login = button;
        this.mContext = this;
        button.setOnClickListener(this);
    }

    /* method ini digunakan untuk berpindah ke halaman utama
    *  sekaligus melakukan pengecekan terhadap user yang akan
    *  login ke dalam aplikasi.
    * */
    public void onClick(View view) {
        this.loadingContent = ProgressDialog.show(this, null, "Tunggu Sebentar...",
                true, false);
        this.usrname = this.username.getText().toString();
        this.pass = this.password.getText().toString();
        checkLoginDetails(this.usrname, this.pass);
    }


    /* method ini digunakan untuk melakukan request ke Api
    *  yang bertujuan untuk melakukan pengecekan terhadap username
    *  dan password dari user.
    * */
    private void checkLoginDetails(final String usrname2, String pass2) {
        Retrofit retrofit = RetrofitClientInstance.getRetrofitInstance();
        InterfaceAPI api = retrofit.create(InterfaceAPI.class);

        Call<ResponseBody> call = api.loginRequest(usrname2, pass2);

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    loadingContent.dismiss();
                    try {
                        JSONObject jsonObject = new JSONObject(Objects.requireNonNull(response.body()).string());
                        if (!jsonObject.isNull("login") && jsonObject.getString("login").equals("true")) {
                            Intent intent = new Intent(mContext, MainActivity.class);
                            intent.putExtra("username", usrname2);
                            startActivity(intent);
                            LandingPageActivity.this.finish();
                        } else if (jsonObject.isNull("login") || !jsonObject.getString("login").equals("false")) {
                            Toast.makeText(mContext, "Silahkan Masukan Username dan Password dengan Benar !", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(mContext, jsonObject.getString("password"), Toast.LENGTH_SHORT).show();
                        }
                        Log.d("login", jsonObject.getString("login"));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    } catch (IOException e2) {
                        e2.printStackTrace();
                    }
                } else {
                    loadingContent.dismiss();
                    Toast.makeText(mContext, "Koneksi Internet Bermasalah", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable t) {
                loadingContent.dismiss();
                Toast.makeText(mContext, "Server Error: 500", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
