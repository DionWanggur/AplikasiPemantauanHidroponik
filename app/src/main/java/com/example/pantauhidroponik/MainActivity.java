package com.example.pantauhidroponik;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.provider.MediaStore;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import com.example.pantauhidroponik.View.HistoryFragment;
import com.example.pantauhidroponik.View.MonitoringFragment;
import com.example.pantauhidroponik.View.StatisticsFragment;
import com.example.pantauhidroponik.API.InterfaceAPI;
import com.example.pantauhidroponik.API.RetrofitClientInstance;
import com.google.android.material.navigation.NavigationView;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import okhttp3.ResponseBody;
import org.json.JSONArray;
import org.json.JSONException;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    // atribut untuk memilih foto profil
    private static final int PICK_IMAGE = 1;

    // atribut drawer layout
    protected DrawerLayout drawer;

    // atribut fragment
    protected HistoryFragment historyFragment;
    protected MonitoringFragment monitoringFragment;
    protected StatisticsFragment statisticsFragment;

    // atribut uri image
    Uri imageUri;

    // atribut Context
    protected Context mContext;

    // atribut array berisis daftar node sensor
    ArrayList<String> nodeSensorList;

    // atribut Image View photo profile
    private ImageView photoProfile;

    // atribut Toolbar
    protected Toolbar toolbar;

    public void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // inisiasi Context
        this.mContext = this;

        // inisiasi Bundle untuk dapat dari activity sebelumnya
        Bundle extra = getIntent().getExtras();

        // inisiasi drawer layout
        this.drawer = this.findViewById(R.id.drawer_layout);
        final NavigationView navigationView = this.findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        View navHeader = navigationView.getHeaderView(0);
        this.photoProfile = navHeader.findViewById(R.id.iv_photo_profile);
        this.photoProfile.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent gallery = new Intent();
                gallery.setType("image/*");
                gallery.setAction("android.intent.action.GET_CONTENT");
                startActivityForResult(Intent.createChooser(gallery, "Pilih Gambar"), 1);
            }
        });
        // inisiasi Text View username pada drawer layout
        TextView username = navHeader.findViewById(R.id.tv_username);
        if (extra != null) {
            username.setText(extra.getString("username"));
        }

        // inisiasi toolbar
        this.toolbar = this.findViewById(R.id.toolbar);
        setSupportActionBar(this.toolbar);
        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(this, this.drawer, this.toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        this.drawer.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();
        // inisiasi array list node sensor
        this.nodeSensorList = new ArrayList<>();

        // panggil class Executor dan Handle untuk melakukan request Api
        ExecutorService service = Executors.newSingleThreadExecutor();
        final Handler handler = new Handler(Looper.getMainLooper());
        service.execute(new Runnable() {
            public void run() {
                RetrofitClientInstance.getRetrofitInstance().create(InterfaceAPI.class).getNode().enqueue(new Callback<ResponseBody>() {
                    public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                        if (response.isSuccessful()) {
                            try {
                                JSONArray jsonArray = new JSONArray(Objects.requireNonNull(response.body()).string());
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    MainActivity.this.nodeSensorList.add(jsonArray.getJSONObject(i).getString("namaNode"));
                                }
                            } catch (IOException | JSONException e) {
                                e.printStackTrace();
                            }
                            handler.post(new Runnable() {
                                public void run() {
                                    MainActivity.this.monitoringFragment = MonitoringFragment.newInstance(MainActivity.this.nodeSensorList);
                                    MainActivity.this.statisticsFragment = StatisticsFragment.newInstance(MainActivity.this.nodeSensorList);
                                    MainActivity.this.historyFragment = HistoryFragment.newInstance(MainActivity.this.nodeSensorList);
                                    if (savedInstanceState == null) {
                                        MainActivity.this.loadFragment(MainActivity.this.monitoringFragment);
                                        navigationView.setCheckedItem(R.id.nav_monitoring);
                                    }
                                }
                            });
                            return;
                        }
                        Toast.makeText(MainActivity.this.mContext, "Coba lagi nanti, Server Error: 500", Toast.LENGTH_SHORT).show();
                    }

                    public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable t) {
                        Toast.makeText(MainActivity.this.mContext, "Koneksi Internet Bermasalah", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }

    public void onBackPressed() {
        if (this.drawer.isDrawerOpen(GravityCompat.START)) {
            this.drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    /* method ini digunakan untuk menampilkan fragment */
    public void loadFragment(Fragment fragment) {
        if (fragment != null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, fragment).commit();
        }
    }

    /* method ini digunakan untuk menampilkan fragment
    * ketika user memilih salah satu dari menu drawer */
    public boolean onNavigationItemSelected(MenuItem item) {
        Fragment fragment = null;
        switch (item.getItemId()) {
            case R.id.nav_exit :
                finish();
                System.exit(0);
                break;
            case R.id.nav_history :
                fragment = HistoryFragment.newInstance(this.nodeSensorList);
                break;
            case R.id.nav_monitoring :
                fragment = MonitoringFragment.newInstance(this.nodeSensorList);
                break;
            case R.id.nav_statistics :
                fragment = StatisticsFragment.newInstance(this.nodeSensorList);
                break;
        }
        loadFragment(fragment);
        this.drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    /* method ini digunakan untuk mengganti foto profil pengguna */
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == -1) {
            this.imageUri = data.getData();
            try {
                this.photoProfile.setImageBitmap(MediaStore.Images.Media.getBitmap(getContentResolver(), this.imageUri));
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e2) {
                e2.printStackTrace();
            }
        }
    }
}
