package com.example.pantauhidroponik.View;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import com.example.pantauhidroponik.API.InterfaceAPI;
import com.example.pantauhidroponik.API.RetrofitClientInstance;
import com.example.pantauhidroponik.Adapter.ContentSlider;
import com.example.pantauhidroponik.Model.ContentItem;
import com.example.pantauhidroponik.R;
import com.pusher.client.Pusher;
import com.pusher.client.PusherOptions;
import com.pusher.client.channel.Channel;
import com.pusher.client.connection.ConnectionEventListener;
import com.pusher.client.connection.ConnectionState;
import com.pusher.client.connection.ConnectionStateChange;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import okhttp3.ResponseBody;

import org.json.JSONArray;
import org.json.JSONException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MonitoringFragment extends Fragment {
    // atribut boolean inisiasi view pager
    private boolean adapterInitiate;

    // atribut list class content item
    private ArrayList contentItems;

    // atribut view pager adapter
    private ContentSlider contentSlider;

    // atribut list node sensor
    private final ArrayList<String> listSensor;

    // atribut parameter request api
    private String namaNode;

    // atribut progress dialog
    private ProgressDialog progressDialog;

    // atribut spinner
    private Spinner spinnermenu;

    // atribut String status node
    private String status;

    // atribut Text View
    private TextView tanggal;
    private TextView tv_status;

    // atribut view pager 2
    private ViewPager2 viewPager2;

    public MonitoringFragment(ArrayList<String> listSensor2) {
        this.listSensor = listSensor2;
    }

    public static MonitoringFragment newInstance(ArrayList<String> listSensor2) {
        return new MonitoringFragment(listSensor2);
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_monitoring, container, false);
        this.progressDialog = ProgressDialog.show(getContext(), null,
                "Tunggu Sebentar... \nSedang Menunggu Perintah Aktif dari Base Station", true, true);

        //inisiasi text view
        this.tv_status = view.findViewById(R.id.tv_node_status);
        this.tanggal = view.findViewById(R.id.tv_tgl_Monitoring);

        // inisiasi spinner
        this.spinnermenu = view.findViewById(R.id.sp_sensor_menu);

        // inisiasi adapater view pager 2 (belum diinisiasi)
        this.adapterInitiate = false;

        // inisiasi list class content item
        this.contentItems = new ArrayList();

        // inisiasi view pager
        this.viewPager2 = view.findViewById(R.id.pager);

        // inisiasi pusher
        callPusher();

        // inisiasi spinner adapter
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(getContext(),
                android.R.layout.simple_spinner_dropdown_item, this.listSensor);

        this.spinnermenu.setAdapter(arrayAdapter);
        this.spinnermenu.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                namaNode = spinnermenu.getSelectedItem().toString();
            }

            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });
        return view;
    }

    @SuppressLint("SimpleDateFormat")
    public void getAllNodeData() {
        ExecutorService service = Executors.newSingleThreadExecutor();
        final Handler handler = new Handler(Looper.getMainLooper());
        service.execute(() -> RetrofitClientInstance.getRetrofitInstance().create(InterfaceAPI.class).getMonitoring(MonitoringFragment.this.namaNode).enqueue(new Callback<ResponseBody>() {
            @SuppressLint({"NotifyDataSetChanged", "SetTextI18n"})
            public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    Calendar calendar = Calendar.getInstance();
                     SimpleDateFormat format = new SimpleDateFormat("EEE, MMM d, yyyy");
                     SimpleDateFormat format2 = new SimpleDateFormat("h:mm a");
                    ArrayList arrayList = new ArrayList();
                    try {
                        assert response.body() != null;
                        JSONArray jsonArray = new JSONArray(response.body().string());
                        for (int i = 0; i < jsonArray.length(); i++) {
                            String hidroponik = jsonArray.getJSONObject(i).getString("namaHidroponik");
                            String title = jsonArray.getJSONObject(i).getString("namaSensor");
                            String location = jsonArray.getJSONObject(i).getString("lokasi");
                            String paramaterValue = jsonArray.getJSONObject(i).getString("value");
                            String lastChecked = jsonArray.getJSONObject(i).getString("waktu");
                            String btsAtas = jsonArray.getJSONObject(i).getString("batasAtas");
                            String btsBawah = jsonArray.getJSONObject(i).getString("batasBawah");
                            calendar.setTime(Objects.requireNonNull(new SimpleDateFormat("yy-MM-dd HH:mm:ss").parse(lastChecked)));
                            Date hari = calendar.getTime();
                            ContentItem dataBaru = new ContentItem(hidroponik + " | "+ title, location, paramaterValue, format.format(hari) + "\n" + format2.format(hari));
                            dataBaru.setWarning(btsAtas, btsBawah, paramaterValue);
                            dataBaru.setValue(paramaterValue, title);
                            arrayList.add(dataBaru);
                        }
                        status = jsonArray.getJSONObject(0).getString("Status");
                        contentItems.clear();
                        contentItems.addAll(arrayList);
                        handler.post(() -> {
                            Date today = Calendar.getInstance().getTime();
                            SimpleDateFormat formatter = new SimpleDateFormat("EEE, MMM d, yyyy");
                            SimpleDateFormat formatter2 = new SimpleDateFormat("h:mm a");
                            String date = formatter.format(today);
                            tanggal.setText(date + "\n" + formatter2.format(today));
                            if (status.equalsIgnoreCase("0")) {
                                tv_status.setText("Status: Mati");
                            } else {
                                tv_status.setText("Status: Hidup");
                            }
                            if (adapterInitiate) {
                                contentSlider.notifyDataSetChanged();
                            }
                            else {
                                contentSlider = new ContentSlider(contentItems);
                                viewPager2.setAdapter(contentSlider);
                                adapterInitiate = true;
                                progressDialog.dismiss();
                            }

                        });
                    } catch (JSONException | ParseException | IOException e) {
                        e.printStackTrace();
                    }
                } else {
                    Toast.makeText(MonitoringFragment.this.getContext(), "Coba lagi nanti, Server Error: 500", Toast.LENGTH_SHORT).show();
                }
            }

            public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable t) {
                Toast.makeText(MonitoringFragment.this.getContext(), "Koneksi Internet Bermasalah", Toast.LENGTH_SHORT).show();
            }
        }));
    }

    public void callPusher() {
        PusherOptions options = new PusherOptions();
        options.setCluster("ap1");
        Pusher pusher = new Pusher("a189c31a94dae4644a9f", options);
        pusher.connect(new ConnectionEventListener() {
            public void onConnectionStateChange(ConnectionStateChange change) {
                Log.i("Pusher", "State changed from " + change.getPreviousState() + " to " + change.getCurrentState());
            }

            public void onError(String message, String code, Exception e) {
                Log.i("Pusher", "There was a problem connecting! \ncode: " + code + "\nmessage: " + message + "\nException: " + e);
            }
        }, ConnectionState.ALL);
        Channel subscribe = pusher.subscribe("my-channel");
        subscribe.bind("my-event", event -> {
            getAllNodeData();
            Log.i("Pusher", "Received event with data: " + event.toString());
        });
    }
}
