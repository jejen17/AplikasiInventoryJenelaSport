package com.example.kkp;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import model.SpkResponse; // Import model

import java.util.List;

public class SuratSPKAdapter extends ArrayAdapter<SpkResponse.Spk> {

    public SuratSPKAdapter(@NonNull Context context, List<SpkResponse.Spk> objects) {
        super(context, 0, objects);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_surat_spk, parent, false);
        }

        // Ambil Data SPK per baris
        SpkResponse.Spk spk = getItem(position);

        TextView tvNama = convertView.findViewById(R.id.tvNamaPelanggan);
        TextView tvTotal = convertView.findViewById(R.id.tvTotalItem);

        if (spk != null) {
            // 1. Set Nama Pelanggan
            // Cek null biar gak force close kalau datanya kosong
            String namaPelanggan = (spk.getPelanggan() != null) ? spk.getPelanggan().getNama() : "Tanpa Nama";
            tvNama.setText(namaPelanggan);

            // 2. Set Total Item
            tvTotal.setText("Total Item: " + spk.getTotalItems() + " Barang");

            // 3. EVENT KLIK -> PINDAH KE DETAIL
            convertView.setOnClickListener(v -> {
                // Kita buat Intent ke DetailActivity (yang nanti akan kita buat)
                // Intent intent = new Intent(getContext(), DetailSuratActivity.class);
                // intent.putExtra("SPK_DATA", spk); // Kirim seluruh objek SPK
                // getContext().startActivity(intent);

                // SEMENTARA: Tampilkan Toast dulu sampai DetailActivity dibuat
                Toast.makeText(getContext(), "Membuka detail: " + namaPelanggan, Toast.LENGTH_SHORT).show();
            });
        }

        return convertView;
    }
}