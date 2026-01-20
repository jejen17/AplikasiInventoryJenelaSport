package com.example.kkp;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import api.ApiClient;
import api.ApiInterface;
import model.SpkResponse;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SuratSPK extends AppCompatActivity {

    ListView lvSuratSPK;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_surat_spk);

        lvSuratSPK = findViewById(R.id.lvSuratSPK);


        loadDataSpk();
    }

    private void loadDataSpk() {
        // 1. Ambil Token
        SharedPreferences sharedPref = getSharedPreferences("UserSession", MODE_PRIVATE);
        String token = sharedPref.getString("token", "");

        if (token.isEmpty()) {
            Toast.makeText(this, "Token tidak ditemukan, silakan login ulang", Toast.LENGTH_SHORT).show();
            return;
        }


        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        Call<SpkResponse> call = apiInterface.getSpkList("Bearer " + token);

        call.enqueue(new Callback<SpkResponse>() {
            @Override
            public void onResponse(Call<SpkResponse> call, Response<SpkResponse> response) {
                if (response.isSuccessful() && response.body() != null) {

                    List<SpkResponse.Spk> dataList = response.body().getData();

                    if (dataList != null && !dataList.isEmpty()) {

                        SuratSPKAdapter adapter = new SuratSPKAdapter(SuratSPK.this, dataList);
                        lvSuratSPK.setAdapter(adapter);
                    } else {
                        Toast.makeText(SuratSPK.this, "Belum ada data SPK", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(SuratSPK.this, "Gagal mengambil data", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<SpkResponse> call, Throwable t) {
                Toast.makeText(SuratSPK.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}