package com.example.pantauhidroponik.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.pantauhidroponik.Model.TableContentStatItem;
import com.example.pantauhidroponik.R;
import java.util.List;

public class TableContenStat extends RecyclerView.Adapter<TableContenStat.ViewHolder> {
    private final List<TableContentStatItem> contentStatItems;
    private final Context context;

    public TableContenStat(Context context2, List<TableContentStatItem> contentStatItems2) {
        this.context = context2;
        this.contentStatItems = contentStatItems2;
    }

    @NonNull
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(this.context).inflate(R.layout.table_statistics_item_layout, parent, false));
    }

    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.setContent(this.contentStatItems.get(position));
    }

    public int getItemCount() {
        return this.contentStatItems.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView value;
        private final TextView waktu;
        private final TextView warning;

        public ViewHolder(View itemView) {
            super(itemView);
            this.value = itemView.findViewById(R.id.tv_value_stat_tbl_item);
            this.warning = itemView.findViewById(R.id.tv_peringatan_stat_tbl_item);
            this.waktu = itemView.findViewById(R.id.tv_waktu_stat_tbl_item);
        }

        public void setContent(TableContentStatItem tableContentStatItem) {
            this.value.setText(tableContentStatItem.getValue());
            this.warning.setText(tableContentStatItem.getWarning());
            this.waktu.setText(tableContentStatItem.getWaktu());
        }
    }
}
