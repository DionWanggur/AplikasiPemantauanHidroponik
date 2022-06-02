package com.example.pantauhidroponik.Adapter;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pantauhidroponik.Model.ContentItem;
import com.example.pantauhidroponik.R;

import java.util.List;

import pl.pawelkleczkowski.customgauge.CustomGauge;

public class ContentSlider extends RecyclerView.Adapter<ContentSlider.ContentViewHolder> {
    private final List<ContentItem> contentItems;

    public ContentSlider(List<ContentItem> contentItems2) {
        this.contentItems = contentItems2;
    }

    @NonNull
    public ContentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ContentViewHolder(
                LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.slider_content, parent, false));
    }

    public void onBindViewHolder(ContentViewHolder holder, int position) {
        holder.setContent(this.contentItems.get(position));
    }

    public int getItemCount() {
        return this.contentItems.size();
    }

    static class ContentViewHolder extends RecyclerView.ViewHolder {
        // atribut pada monitoring content
        private final CustomGauge gauge;
        private final TextView tv_Location;
        private final TextView tv_Title;
        private final TextView tv_Value;
        private final TextView tv_Warning;
        private final TextView tv_lastChecked;

        public ContentViewHolder(View itemView) {
            super(itemView);
            // inisiasi
            this.gauge = itemView.findViewById(R.id.gauge);
            this.tv_Title = itemView.findViewById(R.id.tv_parameterTitle);
            this.tv_Location = itemView.findViewById(R.id.tv_node_loc_title);
            this.tv_Value = itemView.findViewById(R.id.tv_parameterValue);
            this.tv_Warning = itemView.findViewById(R.id.tv_warning);
            this.tv_lastChecked = itemView.findViewById(R.id.tv_lastCheck);
        }

        /* access modifiers changed from: package-private */
        @SuppressLint("SetTextI18n")
        public void setContent(ContentItem contentItem) {
            this.gauge.setPointSize((int) contentItem.getValue());
            this.tv_Title.setText(contentItem.getTitle());
            this.tv_Location.setText("Lokasi: " + contentItem.getLocation());
            this.tv_Value.setText(contentItem.getParameterValue());
            this.tv_lastChecked.setText("Pemeriksaan Terakhir: \n" + contentItem.getLastCheck());
            this.tv_Warning.setText("Peringatan: \n" + contentItem.getWarning());
            if (contentItem.isWarningColor()) {
                this.tv_Warning.setBackgroundResource(R.color.Aman);
            } else {
                this.tv_Warning.setBackgroundResource(R.color.Bahaya);
            }
        }
    }
}
