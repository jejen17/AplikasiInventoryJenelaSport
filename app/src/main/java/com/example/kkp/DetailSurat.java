package com.example.kkp;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import android.app.AlertDialog;
import android.widget.EditText;
import android.widget.Button;
import api.ApiClient;
import api.ApiInterface;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import android.content.SharedPreferences;

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


                tvJumlah.setText(item.getSisaLabel());

                if (item.getSisaQty() == 0) {
                    tvJumlah.setTextColor(getResources().getColor(android.R.color.holo_green_dark));
                }

                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                );
                params.setMargins(0, 0, 0, 24);
                itemView.setLayoutParams(params);

                itemView.setClickable(true);
                itemView.setFocusable(true);

                itemView.setOnClickListener(v -> {
                    if (item.getSisaQty() <= 0) {
                        Toast.makeText(this, "Item ini sudah selesai!", Toast.LENGTH_SHORT).show();
                    } else {
                        showDialogUpdate(item, tvJumlah);
                    }
                });

                containerBarang.addView(itemView);
            }
        }
    }

    private void showDialogUpdate(SpkResponse.Item item, TextView tvJumlahDiLayar) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_update_progress, null);
        builder.setView(dialogView);

        AlertDialog dialog = builder.create();
        dialog.setCancelable(false);

        TextView tvInfoSisa = dialogView.findViewById(R.id.tvInfoSisa);
        EditText etInput = dialogView.findViewById(R.id.etInputJumlah);
        TextView btnBatal = dialogView.findViewById(R.id.btnBatal);
        Button btnProses = dialogView.findViewById(R.id.btnProses);


        int sisaTarget = item.getSisaQty();
        tvInfoSisa.setText("Sisa yang harus dikerjakan: " + sisaTarget + " Pcs");

        btnBatal.setOnClickListener(v -> dialog.dismiss());

        btnProses.setOnClickListener(v -> {
            String inputStr = etInput.getText().toString();

            if (inputStr.isEmpty()) {
                Toast.makeText(this, "Masukkan jumlah!", Toast.LENGTH_SHORT).show();
                return;
            }

            int inputJumlah = Integer.parseInt(inputStr);


            if (inputJumlah > sisaTarget) {
                Toast.makeText(this, "Input melebihi sisa (" + sisaTarget + ")!", Toast.LENGTH_LONG).show();
            } else if (inputJumlah <= 0) {
                Toast.makeText(this, "Jumlah harus lebih dari 0", Toast.LENGTH_SHORT).show();
            } else {
                kirimProgressKeServer(item, inputJumlah, sisaTarget, dialog, tvJumlahDiLayar);
            }
        });

        dialog.show();
    }

    private void kirimProgressKeServer(SpkResponse.Item item, int jumlahYgDikerjakan, int sisaLama, AlertDialog dialog, TextView tvUI) {
        SharedPreferences sharedPref = getSharedPreferences("UserSession", MODE_PRIVATE);
        String token = sharedPref.getString("token", "");

        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);

        Call<ResponseBody> call = apiInterface.storeProgress("Bearer " + token, item.getIdDetail(), jumlahYgDikerjakan);

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {

                    Toast.makeText(DetailSurat.this, "Laporan Terkirim! Menunggu Verifikasi.", Toast.LENGTH_LONG).show();


                    int sisaBaru = sisaLama - jumlahYgDikerjakan;
                    item.setSisaQty(sisaBaru);

                    if (sisaBaru == 0) {
                        String label = "Selesai";
                        item.setSisaLabel(label);
                        tvUI.setText(label);
                        tvUI.setTextColor(getResources().getColor(android.R.color.holo_orange_dark));
                    } else {
                        String label = sisaBaru + " Pcs";
                        item.setSisaLabel(label);
                        tvUI.setText(label);
                    }

                    dialog.dismiss();
                } else {
                    try {
                        String errorBody = response.errorBody().string();


                        android.util.Log.e("ERROR_API", "Pesan Error: " + errorBody);


                        Toast.makeText(DetailSurat.this, "Server Menolak: " + errorBody, Toast.LENGTH_LONG).show();

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(DetailSurat.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }


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