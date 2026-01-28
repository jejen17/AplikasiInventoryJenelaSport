package com.example.kkp;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import api.ApiClient;
import api.ApiInterface;
import model.LowStockResponse;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class StokMenipis extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ImageView btnBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stok_menipis);


        recyclerView = findViewById(R.id.rvStokMenipis);
        btnBack = findViewById(R.id.btnBack);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        btnBack.setOnClickListener(v -> finish());

        loadData();
    }

    private void loadData() {
        SharedPreferences sharedPref = getSharedPreferences("UserSession", MODE_PRIVATE);
        String token = sharedPref.getString("token", "");

        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        Call<LowStockResponse> call = apiInterface.getLowStockList("Bearer " + token);

        Toast.makeText(this, "Memuat data...", Toast.LENGTH_SHORT).show();

        call.enqueue(new Callback<LowStockResponse>() {
            @Override
            public void onResponse(Call<LowStockResponse> call, Response<LowStockResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<LowStockResponse.StockItem> data = response.body().getData();

                    if (data != null && !data.isEmpty()) {
                        LowStockAdapter adapter = new LowStockAdapter(StokMenipis.this, data);
                        recyclerView.setAdapter(adapter);
                    } else {
                        Toast.makeText(StokMenipis.this, "Stok Aman! Tidak ada yang menipis.", Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(StokMenipis.this, "Gagal memuat data", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<LowStockResponse> call, Throwable t) {
                Toast.makeText(StokMenipis.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}