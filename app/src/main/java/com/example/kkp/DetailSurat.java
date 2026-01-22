package com.example.kkp;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import model.SpkResponse;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class DetailSurat extends AppCompatActivity {

    TextView tvPelanggan, tvNoSpk, tvTanggal;
    LinearLayout containerBarang;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_surat);

        tvPelanggan = findViewById(R.id.tvDetailPelanggan);
        tvNoSpk = findViewById(R.id.tvDetailNoSpk);
        tvTanggal = findViewById(R.id.tvDetailTanggal);
        containerBarang = findViewById(R.id.containerBarang);


        SpkResponse.Spk spkData = (SpkResponse.Spk) getIntent().getSerializableExtra("SPK_DATA");

        if (spkData != null) {

            tvPelanggan.setText(spkData.getNamaPelanggan());

            tvNoSpk.setText("#" + spkData.getIdSpk());


            tvTanggal.setText(formatTanggal(spkData.getTargetDate()));

            tampilkanBarang(spkData);
        } else {
            Toast.makeText(this, "Data Error", Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    private void tampilkanBarang(SpkResponse.Spk spk) {
        if (spk.getItems() != null) {
            containerBarang.removeAllViews();

            for (SpkResponse.Item item : spk.getItems()) {
                View itemView = LayoutInflater.from(this).inflate(R.layout.item_detail_produk, containerBarang, false);

                TextView tvNama = itemView.findViewById(R.id.tvNamaProduk);
                TextView tvUkuran = itemView.findViewById(R.id.tvUkuran);
                TextView tvJumlah = itemView.findViewById(R.id.tvJumlah);


                tvNama.setText(item.getNamaProduk());
                tvUkuran.setText("Ukuran: " + item.getNamaUkuran());
                tvJumlah.setText(item.getTarget() + " Pcs");

                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                );
                params.setMargins(0, 0, 0, 24);
                itemView.setLayoutParams(params);

                containerBarang.addView(itemView);
            }
        }
    }

    // Fungsi Format Tanggal (Dari 2026-01-20 jadi 20 Januari 2026)
    private String formatTanggal(String rawDate) {
        if (rawDate == null) return "-";
        try {

            SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.US);

            SimpleDateFormat outputFormat = new SimpleDateFormat("dd MMMM yyyy", new Locale("id", "ID"));

            Date date = inputFormat.parse(rawDate);
            return outputFormat.format(date);
        } catch (Exception e) {
            return rawDate;
        }
    }
}