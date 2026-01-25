package com.example.kkp;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import api.ApiClient;
import api.ApiInterface;
import model.OptionItem;
import model.ProdukItem;
import model.SizeItem;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BarangFragment extends Fragment {

    // View Components
    private LinearLayout layoutTanggal;
    private TextView tvTanggal, tvQuantity;
    private Spinner spinnerProduk, spinnerUkuran;
    private View btnMinus, btnPlus;
    private Button btnSimpan;
    private EditText etCatatan;

    // Data Variables
    private Calendar calendar = Calendar.getInstance();
    private int quantity = 1;
    private String selectedDateForApi = "";

    public BarangFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_barang, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // 1. Inisialisasi View
        layoutTanggal = view.findViewById(R.id.layoutTanggal);
        tvTanggal = view.findViewById(R.id.tvTanggal);
        tvQuantity = view.findViewById(R.id.tvQuantity);
        spinnerProduk = view.findViewById(R.id.spinnerProduk);
        spinnerUkuran = view.findViewById(R.id.spinnerUkuran);
        btnMinus = view.findViewById(R.id.btnMinus);
        btnPlus = view.findViewById(R.id.btnPlus);
        btnSimpan = view.findViewById(R.id.btnSimpan);

        // Cek ID di XML, pastikan ada android:id="@+id/etCatatan"
        etCatatan = view.findViewById(R.id.catat);

        // 2. Setup Tanggal (Hari ini & Tidak Bisa Diklik)
        updateLabelTanggal();
        // layoutTanggal.setOnClickListener(...) <-- HAPUS/JANGAN DIPASANG agar tidak bisa diklik

        // 3. Setup Counter
        btnPlus.setOnClickListener(v -> {
            quantity++;
            tvQuantity.setText(String.valueOf(quantity));
        });

        btnMinus.setOnClickListener(v -> {
            if (quantity > 1) {
                quantity--;
                tvQuantity.setText(String.valueOf(quantity));
            }
        });

        // 4. Setup Simpan
        btnSimpan.setOnClickListener(v -> simpanData());

        // 5. Setup Listener Spinner Produk
        spinnerProduk.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                OptionItem selectedItem = (OptionItem) parent.getItemAtPosition(position);
                if (selectedItem != null) {
                    loadSize(selectedItem.getId());
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });

        // 6. Load Data Awal
        loadProduk();
    }

    private void updateLabelTanggal() {
        String formatUser = "dd MMMM yyyy";
        SimpleDateFormat sdfUser = new SimpleDateFormat(formatUser, new Locale("id", "ID"));
        tvTanggal.setText(sdfUser.format(calendar.getTime()));

        String formatApi = "yyyy-MM-dd";
        SimpleDateFormat sdfApi = new SimpleDateFormat(formatApi, Locale.US);
        selectedDateForApi = sdfApi.format(calendar.getTime());
    }

    private void loadProduk() {
        if (getActivity() == null) return; // Cek aman

        SharedPreferences sharedPref = getActivity().getSharedPreferences("UserSession", Context.MODE_PRIVATE);
        String token = sharedPref.getString("token", "");

        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        Call<List<ProdukItem>> call = apiInterface.getListProduk("Bearer " + token);

        call.enqueue(new Callback<List<ProdukItem>>() {
            @Override
            public void onResponse(Call<List<ProdukItem>> call, Response<List<ProdukItem>> response) {
                // --- PENGAMAN UTAMA: CEK CONTEXT ---
                if (getContext() == null) return;
                // -----------------------------------

                if (response.isSuccessful() && response.body() != null) {
                    List<ProdukItem> data = response.body();
                    List<OptionItem> listSpinner = new ArrayList<>();

                    for (ProdukItem item : data) {
                        listSpinner.add(new OptionItem(item.getId(), item.getNamaTampil()));
                    }

                    ArrayAdapter<OptionItem> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_dropdown_item, listSpinner);
                    spinnerProduk.setAdapter(adapter);
                } else {
                    Toast.makeText(getContext(), "Gagal memuat produk", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<ProdukItem>> call, Throwable t) {
                if (getContext() != null) {
                    Toast.makeText(getContext(), "Koneksi Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void loadSize(String idProduk) {
        // --- PENGAMAN ---
        if (getContext() == null) return;

        // Kosongkan spinner size dulu
        spinnerUkuran.setAdapter(null);

        if (getActivity() == null) return;
        SharedPreferences sharedPref = getActivity().getSharedPreferences("UserSession", Context.MODE_PRIVATE);
        String token = sharedPref.getString("token", "");

        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        Call<List<SizeItem>> call = apiInterface.getListSize("Bearer " + token, idProduk);

        call.enqueue(new Callback<List<SizeItem>>() {
            @Override
            public void onResponse(Call<List<SizeItem>> call, Response<List<SizeItem>> response) {
                // --- PENGAMAN UTAMA ---
                if (getContext() == null) return;
                // ----------------------

                if (response.isSuccessful() && response.body() != null) {
                    List<SizeItem> data = response.body();
                    List<OptionItem> listSpinner = new ArrayList<>();

                    for (SizeItem item : data) {
                        listSpinner.add(new OptionItem(item.getId(), item.getTipe()));
                    }

                    ArrayAdapter<OptionItem> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_dropdown_item, listSpinner);
                    spinnerUkuran.setAdapter(adapter);

                    if (listSpinner.isEmpty()) {
                        Toast.makeText(getContext(), "Ukuran tidak tersedia", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<List<SizeItem>> call, Throwable t) {
                if (getContext() != null) {
                    Toast.makeText(getContext(), "Koneksi Error", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void simpanData() {
        if (getContext() == null) return;

        OptionItem selectedProduk = (OptionItem) spinnerProduk.getSelectedItem();
        OptionItem selectedSize = (OptionItem) spinnerUkuran.getSelectedItem();

        if (selectedProduk == null || selectedSize == null) {
            Toast.makeText(getContext(), "Pilih Produk & Ukuran dulu", Toast.LENGTH_SHORT).show();
            return;
        }

        if (getActivity() == null) return;
        SharedPreferences sharedPref = getActivity().getSharedPreferences("UserSession", Context.MODE_PRIVATE);
        String token = sharedPref.getString("token", "");

        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        Call<ResponseBody> call = apiInterface.inputBarangMasuk(
                "Bearer " + token,
                selectedProduk.getId(),
                selectedSize.getId(),
                quantity,
                selectedDateForApi
        );

        Toast.makeText(getContext(), "Menyimpan...", Toast.LENGTH_SHORT).show();

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                // --- PENGAMAN UTAMA ---
                if (getContext() == null) return;
                // ----------------------

                if (response.isSuccessful()) {
                    Toast.makeText(getContext(), "Berhasil Disimpan!", Toast.LENGTH_LONG).show();
                    quantity = 1;
                    tvQuantity.setText("1");
                    if (etCatatan != null) etCatatan.setText("");
                } else {
                    Toast.makeText(getContext(), "Gagal Simpan", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                if (getContext() != null) {
                    Toast.makeText(getContext(), "Gagal Terhubung", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}