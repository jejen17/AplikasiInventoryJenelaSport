package com.example.kkp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import api.ApiClient;
import api.ApiInterface;
import model.DashboardResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeFragment extends Fragment {

    private TextView tvNamaKaryawan, tvTotalProduk, tvStokMenipis;
    private Button btnSuratSPK;

    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // 1. Initialize Views
        tvNamaKaryawan = view.findViewById(R.id.tvNamaKaryawan);
        tvTotalProduk = view.findViewById(R.id.tvTotalProduk);
        tvStokMenipis = view.findViewById(R.id.tvStokMenipis);
        btnSuratSPK = view.findViewById(R.id.btnSuratSPK);
        LinearLayout layoutStokMenipis = view.findViewById(R.id.layoutStokMenipis);

        // 2. Load User Name (from SharedPreferences)
        SharedPreferences sharedPref = requireActivity().getSharedPreferences("UserSession", Context.MODE_PRIVATE);
        String nama = sharedPref.getString("nama", "Karyawan");
        tvNamaKaryawan.setText(nama);

        // 3. Load Dashboard Data
        loadDashboardData();

        // 4. Setup Button Listener
        btnSuratSPK.setOnClickListener(v -> {
            // Navigate to SPK Activity/Fragment (Adjust as needed)
            Intent intent = new Intent(getContext(), SuratSPK.class);
            startActivity(intent);
        });

        layoutStokMenipis.setOnClickListener(v -> {
            Intent intent = new Intent(getContext(), StokMenipis.class);
            startActivity(intent);
        });
    }

    private void loadDashboardData() {
        if (getContext() == null) return;

        SharedPreferences sharedPref = requireActivity().getSharedPreferences("UserSession", Context.MODE_PRIVATE);
        String token = sharedPref.getString("token", "");

        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        Call<DashboardResponse> call = apiInterface.getDashboardSummary("Bearer " + token);

        call.enqueue(new Callback<DashboardResponse>() {
            @Override
            public void onResponse(Call<DashboardResponse> call, Response<DashboardResponse> response) {
                if (getContext() == null) return;

                if (response.isSuccessful() && response.body() != null) {
                    DashboardResponse.Data data = response.body().getData();

                    // Update UI with data from server
                    tvTotalProduk.setText(String.valueOf(data.getTotalProduk()));
                    tvStokMenipis.setText(String.valueOf(data.getStokMenipis()));

                } else {
                    Toast.makeText(getContext(), "Gagal memuat data dashboard", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<DashboardResponse> call, Throwable t) {
                if (getContext() != null) {
                    Toast.makeText(getContext(), "Koneksi Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });


    }

    @Override
    public void onResume() {
        super.onResume();
        // Reload data when returning to this fragment (e.g., after inputting stock)
        loadDashboardData();
    }
}