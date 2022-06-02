package com.example.pantauhidroponik.View;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.pantauhidroponik.Adapter.TableContenStat;
import com.example.pantauhidroponik.Model.TableContentStatItem;
import com.example.pantauhidroponik.R;
import java.util.List;

public class TableStatistics extends Fragment {
    private final List<TableContentStatItem> tableContentItems;

    public TableStatistics(List<TableContentStatItem> tableContentItems2) {
        this.tableContentItems = tableContentItems2;
    }

    public static TableStatistics newInstance(List<TableContentStatItem> tableContentItems2) {
        return new TableStatistics(tableContentItems2);
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_table_statistics, container, false);
        TextView status = view.findViewById(R.id.tv_status_statistics);
        TextView parameter = view.findViewById(R.id.tv_parameter_statistics);
        status.setText("Status: " + this.tableContentItems.get(0).getStatus());
        parameter.setText("Parameter: " + this.tableContentItems.get(0).getParameter());
        RecyclerView recyclerView = view.findViewById(R.id.rv_statistics_tblData);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        TableContenStat tableContenStat = new TableContenStat(getContext(), this.tableContentItems);
        recyclerView.setAdapter(tableContenStat);
        return view;
    }
}
