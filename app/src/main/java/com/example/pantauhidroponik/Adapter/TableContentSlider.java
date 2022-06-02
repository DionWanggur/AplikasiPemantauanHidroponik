package com.example.pantauhidroponik.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.pantauhidroponik.Model.TableContentItem;
import com.example.pantauhidroponik.R;
import java.util.List;

public class TableContentSlider extends RecyclerView.Adapter<TableContentSlider.ContentViewHolder> {
    private final List<TableContentItem> contentItems;

    public TableContentSlider(List<TableContentItem> tableContentItems) {
        this.contentItems = tableContentItems;
    }

    @NonNull
    public ContentViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ContentViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.table_history_layout, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ContentViewHolder holder, int position) {
        holder.setContent(this.contentItems.get(position));
    }


    public int getItemCount() {
        return this.contentItems.size();
    }

    public static class ContentViewHolder extends RecyclerView.ViewHolder {
        private final TextView tv_TDS_value;
        private final TextView tv_kelembaban_value;
        private final TextView tv_namaNode;
        private final TextView tv_pH_value;
        private final TextView tv_status;
        private final TextView tv_suhuAir_value;
        private final TextView tv_suhuUdara_value;
        private final TextView tv_waktu;

        public ContentViewHolder(View itemView) {
            super(itemView);
            this.tv_namaNode = itemView.findViewById(R.id.tv_namaNode_history);
            this.tv_status = itemView.findViewById(R.id.tv_node_status_history);
            this.tv_waktu = itemView.findViewById(R.id.tv_waktu_history);
            this.tv_suhuUdara_value = itemView.findViewById(R.id.tv_tbl_SU_value);
            this.tv_suhuAir_value = itemView.findViewById(R.id.tv_tbl_SA_value);
            this.tv_pH_value = itemView.findViewById(R.id.tv_tbl_pH_value);
            this.tv_kelembaban_value = itemView.findViewById(R.id.tv_tbl_Kelembaban_value);
            this.tv_TDS_value = itemView.findViewById(R.id.tv_tbl_TDS_value);
        }

        public void setContent(TableContentItem tableContentItem) {
            this.tv_namaNode.setText(tableContentItem.getNamaNode());
            this.tv_status.setText("Status \n" + tableContentItem.getStatus());
            this.tv_waktu.setText("Waktu \n" + tableContentItem.getWaktu());
            this.tv_suhuUdara_value.setText(tableContentItem.getSuhuUdara());
            this.tv_suhuAir_value.setText(tableContentItem.getSuhuAir());
            this.tv_pH_value.setText(tableContentItem.getpH());
            this.tv_kelembaban_value.setText(tableContentItem.getKelembaban());
            this.tv_TDS_value.setText(tableContentItem.getTDS());
        }
    }
}
