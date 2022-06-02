package com.example.pantauhidroponik.View;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.pantauhidroponik.API.InterfaceAPI;
import com.example.pantauhidroponik.API.RetrofitClientInstance;
import com.example.pantauhidroponik.Model.TableContentStatItem;
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

public class StatisticsFragment extends Fragment implements AdapterView.OnItemSelectedListener, View.OnClickListener {

    // atribut button
    private Button btnLihat;

    // atribut fragment
    protected GrafikStatistics grafikStatistics;
    protected TableStatistics tableStatistics;

    // atribut array
    private final ArrayList<String> listSensor;

    // parameter untuk request Api
    private String namaNode;
    private String paramater;
    private String modeView;
    public String tv_param_text;

    // atribut spinner
    private Spinner parameterSpinner;
    private Spinner spinnerNode;
    private Spinner viewModeSpinner;

    // atribut list class table content stat item
    public List<TableContentStatItem> tableContentItems;


    public StatisticsFragment(ArrayList<String> nodeSensor) {
        this.listSensor = nodeSensor;
    }

    public static StatisticsFragment newInstance(ArrayList<String> nodeSensor) {
        return new StatisticsFragment(nodeSensor);
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_statistics, container, false);

        // inisiasi spinner
        this.parameterSpinner = view.findViewById(R.id.sp_parameter);
        this.viewModeSpinner = view.findViewById(R.id.sp_modeView);
        this.spinnerNode = view.findViewById(R.id.sp_node_statistics);

        // inisiasi button dan set on click listener
        this.btnLihat = view.findViewById(R.id.btn_lihat_statistics);
        this.btnLihat.setOnClickListener(this);

        // inisiasi array list class table content stat item
        this.tableContentItems = new ArrayList();

        // inisiasi adapter untuk spinner
        ArrayAdapter<CharSequence> adapterParameter = ArrayAdapter.createFromResource(getContext(),
                R.array.parameters_array, android.R.layout.simple_spinner_item);
        adapterParameter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        ArrayAdapter<CharSequence> adapterViewMode = ArrayAdapter.createFromResource(getContext(),
                R.array.viewMode_array, android.R.layout.simple_spinner_item);
        adapterViewMode.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // atribut array adapter
        ArrayAdapter<String> adapterSpinnerMenu = new ArrayAdapter<>(getContext(),
                android.R.layout.simple_spinner_dropdown_item, this.listSensor);

        // set spinner adapter
        this.parameterSpinner.setAdapter(adapterParameter);
        this.viewModeSpinner.setAdapter(adapterViewMode);
        this.spinnerNode.setAdapter(adapterSpinnerMenu);

        // set on item selected
        this.parameterSpinner.setOnItemSelectedListener(this);
        this.viewModeSpinner.setOnItemSelectedListener(this);
        this.spinnerNode.setOnItemSelectedListener(this);

        return view;
    }

    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        this.paramater = this.parameterSpinner.getSelectedItem().toString();
        if (!this.paramater.equalsIgnoreCase("Pilih Parameter")) {
            String str = this.paramater;
            this.tv_param_text = str;
            if (str.equalsIgnoreCase("Suhu Udara")) {
                this.paramater = "suhuUdara";
            } else if (this.paramater.equalsIgnoreCase("Suhu Air")) {
                this.paramater = "suhuAir";
            } else if (this.paramater.equalsIgnoreCase("Kelembaban")) {
                this.paramater.toLowerCase();
            }
        }
        this.modeView = this.viewModeSpinner.getSelectedItem().toString();
        this.namaNode = this.spinnerNode.getSelectedItem().toString();
    }

    public void onNothingSelected(AdapterView<?> adapterView) {
        this.modeView = "Tabel";
        this.paramater = "pH";
    }

    public void onClick(View view) {
        if (view.getId() == this.btnLihat.getId()) {
            Log.d("button", "onClick: " + this.paramater + " " + this.modeView);
            if (!this.paramater.equalsIgnoreCase("Pilih Parameter")
                    || !this.modeView.equalsIgnoreCase("Pilih Tampilan")) {
                getStatisticsData(this.namaNode, this.paramater);
            } else {
                Toast.makeText(getContext(), "Pastikan Semua Field Terisi !", Toast.LENGTH_SHORT).show();
            }
        }

    }

    public void getStatisticsData(final String namaNode2, final String paramater2) {
        ExecutorService service = Executors.newSingleThreadExecutor();
        final Handler handler = new Handler(Looper.getMainLooper());
        service.execute(new Runnable() {
            public void run() {
                RetrofitClientInstance.getRetrofitInstance().create(InterfaceAPI.class).getStatistics(namaNode2, paramater2).enqueue(new Callback<ResponseBody>() {
                    public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                        String status;
                        if (response.isSuccessful()) {
                            Calendar calendar = Calendar.getInstance();
                            SimpleDateFormat format = new SimpleDateFormat("EEE, MMM d, yyyy");
                            SimpleDateFormat format2 = new SimpleDateFormat("h:mm a");
                            List<TableContentStatItem> arrayList = new ArrayList<>();
                            try {
                                JSONArray jsonArray = new JSONArray(Objects.requireNonNull(response.body()).string());
                                int i = 0;
                                while (i < jsonArray.length()) {
                                    String title = tv_param_text;
                                    String status2 = jsonArray.getJSONObject(i).getString(NotificationCompat.CATEGORY_STATUS);
                                    String waktu = jsonArray.getJSONObject(i).getString("waktu");
                                    int xLabel = Integer.parseInt(jsonArray.getJSONObject(i).getString("xLabel"));
                                    String value = jsonArray.getJSONObject(i).getString("rata2Value");
                                    String btsAtas = jsonArray.getJSONObject(i).getString("batasAtas");
                                    String btsBawah = jsonArray.getJSONObject(i).getString("batasBawah");
                                    if (status2.equalsIgnoreCase("0")) {
                                        status = "Mati";
                                    } else {
                                        status = "Hidup";
                                    }
                                    calendar.setTime(Objects.requireNonNull(new SimpleDateFormat("yy-MM-dd HH:mm:ss").parse(waktu)));
                                    Date hari = calendar.getTime();

                                    TableContentStatItem tableContentStatItem = new TableContentStatItem(status, title, value,
                                            format.format(hari) + "\n" + format2.format(hari), xLabel);
                                    tableContentStatItem.setWarning(btsAtas, btsBawah, value);
                                    arrayList.add(tableContentStatItem);
                                    i++;
                                }

                                tableContentItems.clear();
                                tableContentItems.addAll(arrayList);
                                handler.post(new Runnable() {
                                    public void run() {
                                        FragmentTransaction ft = getChildFragmentManager().beginTransaction();
                                        if (tableContentItems.size() > 0) {
                                            Fragment fragment = null;
                                            if (modeView.equalsIgnoreCase("Tabel")) {
                                                fragment = TableStatistics.newInstance(StatisticsFragment.this.tableContentItems);
                                            } else if (StatisticsFragment.this.modeView.equalsIgnoreCase("Grafik")) {
                                                fragment = GrafikStatistics.newInstance(StatisticsFragment.this.tableContentItems);
                                            }
                                            ft.replace(R.id.fragment_container_statistics, Objects.requireNonNull(fragment)).commit();
                                        } else {
                                            Toast.makeText(getContext(),
                                                    "Data Statistics selama 24 jam terakhir" +
                                                            " tidak tersedia ", Toast.LENGTH_SHORT).show();
                                        }

                                    }
                                });
                            } catch (JSONException e) {
                                e.printStackTrace();
                            } catch (IOException e) {
                                e.printStackTrace();
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                        } else {
                            Toast.makeText(getContext(), "Coba lagi nanti, Server Error: 500", Toast.LENGTH_SHORT).show();
                        }
                    }

                    public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable t) {
                        Toast.makeText(getContext(), "Koneksi Internet Bermasalah", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }
}
