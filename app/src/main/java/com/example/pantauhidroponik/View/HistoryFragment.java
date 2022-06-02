package com.example.pantauhidroponik.View;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;
import com.example.pantauhidroponik.API.InterfaceAPI;
import com.example.pantauhidroponik.API.RetrofitClientInstance;
import com.example.pantauhidroponik.Adapter.TableContentSlider;
import com.example.pantauhidroponik.Model.TableContentItem;
import com.example.pantauhidroponik.R;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import okhttp3.ResponseBody;
import org.json.JSONArray;
import org.json.JSONException;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HistoryFragment extends Fragment implements View.OnClickListener, AdapterView.OnItemSelectedListener {

    //  atribut paramter Api request
    private String akhir;
    private String awal;
    private String namaNode;

    // atribut button
    private Button btnAkhir;
    private Button btnAwal;

    // atribut class Table Content
    private TableContentSlider contentSlider;
    private final ArrayList<String> listSensor;

    // atribut progres dialog
    public ProgressDialog progressDialog;

    // atribut spinner
    private Spinner spinnerNode;

    // atribut list yang berisi class table content item
    List<TableContentItem> tableContentItems;

    // atribut data picker
    private DatePickerDialog.OnDateSetListener tglAkhir;
    private DatePickerDialog.OnDateSetListener tglAwal;

    // atribut view pager
    private ViewPager2 viewPager2;

    // atribut Text view
    private TextView tv_hidroponik;

    public HistoryFragment(ArrayList<String> nodeSensor) {
        this.listSensor = nodeSensor;
    }

    public static HistoryFragment newInstance(ArrayList<String> nodeSensor) {
        return new HistoryFragment(nodeSensor);
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @SuppressLint("ResourceType")
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_history, container, false);

        // inisiasi spinner
        this.spinnerNode = view.findViewById(R.id.sp_node_history);

        // inisisasi array adapter untuk spinner
        ArrayAdapter<String> adapterSpinnerMenu = new ArrayAdapter<>(getContext(), 17367049, this.listSensor);
        this.spinnerNode.setAdapter(adapterSpinnerMenu);

        // set spinner on item selected
        this.spinnerNode.setOnItemSelectedListener(this);

        // set dat picker listener
        this.tglAwal = new DatePickerDialog.OnDateSetListener() {
            public void onDateSet(DatePicker datePicker, int tahun, int bulan, int hari) {
                int bulan2 = bulan + 1;
                awal = tahun + "/" + bulan2 + "/" + hari;
                btnAwal.setText(tahun + "/" + bulan2 + "/" + hari);
            }
        };
        this.tglAkhir = new DatePickerDialog.OnDateSetListener() {
            public void onDateSet(DatePicker datePicker, int tahun, int bulan, int hari) {
                int bulan2 = bulan + 1;
                akhir = tahun + "/" + bulan2 + "/" + hari;
                btnAkhir.setText(tahun + "/" + bulan2 + "/" + hari);
            }
        };

        // inisiasi button
        this.btnAwal = view.findViewById(R.id.btn_tglAwal);
        this.btnAkhir = view.findViewById(R.id.btn_tglAkhir);
        Button btnPencarian = view.findViewById(R.id.btn_search_history);

        // set on click listener
        this.btnAwal.setOnClickListener(this);
        this.btnAkhir.setOnClickListener(this);
        btnPencarian.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                HistoryFragment.this.onClick(view);
            }
        });

        // inisiasi array class table content item
        this.tableContentItems = new ArrayList();

        // inisiasi view pager 2
        this.viewPager2 = view.findViewById(R.id.pager_history);

        // inisiasi text view
        this.tv_hidroponik = view.findViewById(R.id.tv_hidroponik_history);
        return view;
    }

    @SuppressLint("ResourceType")
    public void onClick(View view) {
        Calendar calendar = Calendar.getInstance();
        int tahun = calendar.get(1);
        int bulan = calendar.get(2);
        int hari = calendar.get(5);
        if (view.getId() == this.btnAwal.getId()) {
            new DatePickerDialog(getContext(), 16974130, this.tglAwal, tahun, bulan, hari).show();
        } else if (view.getId() == this.btnAkhir.getId()) {
            new DatePickerDialog(getContext(), 16974130, this.tglAkhir, tahun, bulan, hari).show();
        } else {
            this.progressDialog = ProgressDialog.show(getContext(), null, "Tunggu Sebentar...", true, false);
            getHistoryData(this.namaNode, this.awal, this.akhir);
        }
    }

    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        this.tableContentItems.clear();
        this.namaNode = this.spinnerNode.getSelectedItem().toString();
    }

    public void onNothingSelected(AdapterView<?> adapterView) {
        this.tableContentItems.clear();
        this.namaNode = this.listSensor.get(0);
    }

    public void getHistoryData(String namaNode2, String awal2, String akhir2) {
        ExecutorService service = Executors.newSingleThreadExecutor();
        final Handler handler = new Handler(Looper.getMainLooper());
        final String str = awal2;
        final String str2 = akhir2;
        final String str3 = namaNode2;
        service.execute(new Runnable() {
            public void run() {
                RetrofitClientInstance.getRetrofitInstance().create(InterfaceAPI.class).getHistory(str, str2, str3).enqueue(new Callback<ResponseBody>() {
                    public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                        if (response.isSuccessful()) {
                            Calendar calendar = Calendar.getInstance();
                            SimpleDateFormat format = new SimpleDateFormat("EEE, MMM d, yyyy");
                            SimpleDateFormat format2 = new SimpleDateFormat("h:mm a");
                            try {
                                JSONArray jsonArray = new JSONArray(Objects.requireNonNull(response.body()).string());
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    String waktu = jsonArray.getJSONObject(i).getString("waktu");
                                    String status = jsonArray.getJSONObject(i).getString("Status");
                                    String suhuUdara = jsonArray.getJSONObject(i).getString("suhuUdara");
                                    String suhuAir = jsonArray.getJSONObject(i).getString("suhuAir");
                                    String pH = jsonArray.getJSONObject(i).getString("pH");
                                    String kelembaban = jsonArray.getJSONObject(i).getString("kelembaban");
                                    String TDS = jsonArray.getJSONObject(i).getString("TDS");
                                    String hidroponik = jsonArray.getJSONObject(i).getString("namaHidroponik");
                                    String lokasi = hidroponik + "\n" + jsonArray.getJSONObject(i).getString("lokasi");
                                    calendar.setTime(Objects.requireNonNull(new SimpleDateFormat("yy-MM-dd HH:mm:ss").parse(waktu)));
                                    Date hari = calendar.getTime();
                                    String waktu2 = format.format(hari) + "\n" + format2.format(hari);
                                    TableContentItem itemBaru = new TableContentItem(str3, waktu2, status, suhuUdara,
                                            suhuAir, pH, kelembaban, TDS,lokasi);
                                    itemBaru.setStatus(status);
                                    tableContentItems.add(itemBaru);
                                }
                                handler.post(new Runnable() {
                                    public void run() {
                                        if (tableContentItems.size() >0){
                                            tv_hidroponik.setText(tableContentItems.get(0).getLokasi());
                                        }
                                        contentSlider = new TableContentSlider(tableContentItems);
                                        viewPager2.setAdapter(contentSlider);
                                    }
                                });
                            } catch (IOException | ParseException | JSONException e) {
                                e.printStackTrace();
                            }
                            HistoryFragment.this.progressDialog.dismiss();
                            return;
                        }
                        HistoryFragment.this.progressDialog.dismiss();
                        Toast.makeText(HistoryFragment.this.getContext(), "Server Error: 500", Toast.LENGTH_SHORT).show();
                    }


                    public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable t) {
                        HistoryFragment.this.progressDialog.dismiss();
                        Toast.makeText(HistoryFragment.this.getContext(), "Koneksi Internet Bermasalah", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }
}
