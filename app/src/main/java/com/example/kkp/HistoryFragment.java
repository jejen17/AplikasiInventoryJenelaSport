package com.example.kkp;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView; // <--- PENTING: Gunakan TextView, bukan Button
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat; // Untuk ambil warna/drawable dengan aman
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import api.ApiClient;
import api.ApiInterface;
import model.HistoryResponse;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HistoryFragment extends Fragment {

    private RecyclerView rvHistory;
    private HistoryAdapter adapter;
    private List<HistoryResponse.HistoryItem> fullList = new ArrayList<>();

    // --- PERBAIKAN: Gunakan TextView sesuai XML ---
    private TextView tabSemua, tabMasuk, tabKeluar;

    public HistoryFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_history, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // 1. Inisialisasi View
        rvHistory = view.findViewById(R.id.rvHistory);
        tabSemua = view.findViewById(R.id.tabSemua);
        tabMasuk = view.findViewById(R.id.tabMasuk);
        tabKeluar = view.findViewById(R.id.tabKeluar);

        rvHistory.setLayoutManager(new LinearLayoutManager(getContext()));

        // --- PERBAIKAN DI SINI ---
        // Pasang Adapter LANGSUNG dengan list kosong dulu.
        // Ini mencegah error "No adapter attached"
        adapter = new HistoryAdapter(getContext(), new ArrayList<>());
        rvHistory.setAdapter(adapter);
        // -------------------------

        // 2. Setup Listener Filter
        tabSemua.setOnClickListener(v -> filterList("ALL"));
        tabMasuk.setOnClickListener(v -> filterList("Masuk"));
        tabKeluar.setOnClickListener(v -> filterList("Keluar"));

        // 3. Load Data API
        loadHistory();
    }

    private void loadHistory() {
        SharedPreferences sharedPref = requireActivity().getSharedPreferences("UserSession", Context.MODE_PRIVATE);
        String token = sharedPref.getString("token", "");

        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        Call<HistoryResponse> call = apiInterface.getRiwayat("Bearer " + token);

        // Opsional: Tampilkan loading
        Toast.makeText(getContext(), "Memuat data...", Toast.LENGTH_SHORT).show();

        call.enqueue(new Callback<HistoryResponse>() {
            @Override
            public void onResponse(Call<HistoryResponse> call, Response<HistoryResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    fullList = response.body().getData();

                    // --- PERBAIKAN DI SINI ---
                    // Karena adapter sudah dipasang di atas, kita tinggal UPDATE datanya
                    if (fullList != null && !fullList.isEmpty()) {
                        adapter.updateList(fullList);
                        updateTabStyle(tabSemua);
                    } else {
                        Toast.makeText(getContext(), "Riwayat masih kosong", Toast.LENGTH_SHORT).show();
                    }
                    // -------------------------
                } else {
                    try {
                        // Ambil pesan error asli dari server
                        String errorBody = response.errorBody().string();
                        // Tampilkan di Logcat (Copy log ini kalau masih error)
                        android.util.Log.e("ERROR_RIWAYAT", errorBody);

                        Toast.makeText(getContext(), "Server Error: " + response.code(), Toast.LENGTH_SHORT).show();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<HistoryResponse> call, Throwable t) {
                // Tampilkan pesan error yang lebih jelas
                Toast.makeText(getContext(), "Koneksi Bermasalah: " + t.getMessage(), Toast.LENGTH_LONG).show();
                t.printStackTrace(); // Cek Logcat untuk detail error
            }
        });
    }

    private void filterList(String type) {
        List<HistoryResponse.HistoryItem> filtered = new ArrayList<>();

        if (type.equals("ALL")) {
            filtered.addAll(fullList);
            updateTabStyle(tabSemua);
        } else {
            for (HistoryResponse.HistoryItem item : fullList) {
                // Ignore case biar aman (masuk == Masuk)
                if (item.getType() != null && item.getType().equalsIgnoreCase(type)) {
                    filtered.add(item);
                }
            }
            // Update Tab Style
            if (type.equals("Masuk")) updateTabStyle(tabMasuk);
            else updateTabStyle(tabKeluar);
        }

        // Update Adapter
        if (adapter != null) {
            adapter.updateList(filtered);
        }
    }

    // Fungsi untuk mengubah tampilan Tab (Aktif vs Tidak Aktif)
    private void updateTabStyle(TextView activeTab) {
        // 1. Reset semua ke style INACTIVE (Abu-abu, Font Regular)
        resetTab(tabSemua);
        resetTab(tabMasuk);
        resetTab(tabKeluar);

        // 2. Set tab yang dipilih ke style ACTIVE (Warna Primary, Font Bold, Teks Putih)
        activeTab.setBackgroundResource(R.drawable.bg_filter_active);
        activeTab.setTextColor(Color.WHITE);
        // Jika kamu punya font bold custom, bisa set disini. Kalau default:
        activeTab.setTypeface(activeTab.getTypeface(), android.graphics.Typeface.BOLD);
    }

    private void resetTab(TextView tab) {
        tab.setBackgroundResource(R.drawable.bg_filter_inactive);
        tab.setTextColor(Color.parseColor("#808080")); // Abu-abu
        tab.setTypeface(null, android.graphics.Typeface.NORMAL);
    }
}