package com.example.pantauhidroponik.View;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.example.pantauhidroponik.Model.TableContentStatItem;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.example.pantauhidroponik.R;


import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class GrafikStatistics extends Fragment {
    private final List<TableContentStatItem> tableContentItems;

    public GrafikStatistics(List<TableContentStatItem> tableContentStatItems) {
        this.tableContentItems = tableContentStatItems;
    }

    public static GrafikStatistics newInstance(List<TableContentStatItem> tableContentStatItems) {
        return new GrafikStatistics(tableContentStatItems);
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_grafik_statistics, container, false);
        LineChart lineChart = view.findViewById(R.id.lc_statistics);
        lineChart.setScaleEnabled(true);

        ArrayList<Entry> arrayList = new ArrayList<>();
        Collections.sort(this.tableContentItems);
        for (int i = 0; i < this.tableContentItems.size(); i++) {
            arrayList.add(new Entry((float) this.tableContentItems.get(i).getxLabel(),
                    Float.parseFloat(this.tableContentItems.get(i).getValue())));
        }

        LineDataSet lineDataSet = new LineDataSet(arrayList, "Nilai Rata-Rata Parameter " + this.tableContentItems.get(0).getParameter());
        lineDataSet.setFillAlpha(210);

        ArrayList<ILineDataSet> dataSets = new ArrayList<>();
        dataSets.add(lineDataSet);

        lineChart.setData(new LineData(dataSets));
        return view;
    }
}
