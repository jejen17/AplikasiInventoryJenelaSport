package com.example.kkp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

public class SuratSPKAdapter extends ArrayAdapter<String> {

    public SuratSPKAdapter(@NonNull Context context, List<String> objects) {
        super(context, 0, objects);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_surat_spk, parent, false);
        }

        String namaSurat = getItem(position);

        TextView tvNama = convertView.findViewById(R.id.tvNamaSurat);
        tvNama.setText(namaSurat);

        return convertView;
    }
}