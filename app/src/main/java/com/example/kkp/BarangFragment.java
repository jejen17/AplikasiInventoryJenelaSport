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

    private LinearLayout layoutTanggal;
    private TextView tvTanggal;

    private EditText tvQuantity;

    private Spinner spinnerProduk, spinnerUkuran;
    private View btnMinus, btnPlus;
    private Button btnSimpan;
    private EditText etCatatan;


    private Calendar calendar = Calendar.getInstance();
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

        layoutTanggal = view.findViewById(R.id.layoutTanggal);
        tvTanggal = view.findViewById(R.id.tvTanggal);


        tvQuantity = view.findViewById(R.id.tvQuantity);

        spinnerProduk = view.findViewById(R.id.spinnerProduk);
        spinnerUkuran = view.findViewById(R.id.spinnerUkuran);
        btnMinus = view.findViewById(R.id.btnMinus);
        btnPlus = view.findViewById(R.id.btnPlus);
        btnSimpan = view.findViewById(R.id.btnSimpan);


        etCatatan = view.findViewById(R.id.catat);


        updateLabelTanggal();


        btnPlus.setOnClickListener(v -> {
            int currentQty = getCurrentQuantity();
            currentQty++;
            tvQuantity.setText(String.valueOf(currentQty));
        });

        btnMinus.setOnClickListener(v -> {
            int currentQty = getCurrentQuantity();
            if (currentQty > 1) {
                currentQty--;
                tvQuantity.setText(String.valueOf(currentQty));
            }
        });


        btnSimpan.setOnClickListener(v -> simpanData());

        // 5. Setup Logic Dropdown
        setupSpinnerLogic();


        loadProduk();
    }

    // --- HELPER BARU: Ambil Angka dari EditText ---
    private int getCurrentQuantity() {
        String qtyStr = tvQuantity.getText().toString();
        if (qtyStr.isEmpty()) {
            return 0; // Default kalau kosong
        }
        try {
            return Integer.parseInt(qtyStr);
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    private void updateLabelTanggal() {
        String formatUser = "dd MMMM yyyy";
        SimpleDateFormat sdfUser = new SimpleDateFormat(formatUser, new Locale("id", "ID"));
        tvTanggal.setText(sdfUser.format(calendar.getTime()));

        String formatApi = "yyyy-MM-dd";
        SimpleDateFormat sdfApi = new SimpleDateFormat(formatApi, Locale.US);
        selectedDateForApi = sdfApi.format(calendar.getTime());
    }

    private void setupSpinnerLogic() {
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
    }

    private void loadProduk() {
        if (getActivity() == null) return;
        SharedPreferences sharedPref = getActivity().getSharedPreferences("UserSession", Context.MODE_PRIVATE);
        String token = sharedPref.getString("token", "");

        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        Call<List<ProdukItem>> call = apiInterface.getListProduk("Bearer " + token);

        call.enqueue(new Callback<List<ProdukItem>>() {
            @Override
            public void onResponse(Call<List<ProdukItem>> call, Response<List<ProdukItem>> response) {
                if (getContext() == null) return;
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
                if (getContext() != null) Toast.makeText(getContext(), "Koneksi Error", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void loadSize(String idProduk) {
        if (getContext() == null) return;
        spinnerUkuran.setAdapter(null);
        SharedPreferences sharedPref = getActivity().getSharedPreferences("UserSession", Context.MODE_PRIVATE);
        String token = sharedPref.getString("token", "");

        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        Call<List<SizeItem>> call = apiInterface.getListSize("Bearer " + token, idProduk);

        call.enqueue(new Callback<List<SizeItem>>() {
            @Override
            public void onResponse(Call<List<SizeItem>> call, Response<List<SizeItem>> response) {
                if (getContext() == null) return;
                if (response.isSuccessful() && response.body() != null) {
                    List<SizeItem> data = response.body();
                    List<OptionItem> listSpinner = new ArrayList<>();
                    for (SizeItem item : data) {
                        listSpinner.add(new OptionItem(item.getId(), item.getTipe()));
                    }
                    ArrayAdapter<OptionItem> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_dropdown_item, listSpinner);
                    spinnerUkuran.setAdapter(adapter);
                }
            }
            @Override
            public void onFailure(Call<List<SizeItem>> call, Throwable t) {
                if (getContext() != null) Toast.makeText(getContext(), "Koneksi Error", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void simpanData() {
        if (getContext() == null) return;

        OptionItem selectedProduk = (OptionItem) spinnerProduk.getSelectedItem();
        OptionItem selectedSize = (OptionItem) spinnerUkuran.getSelectedItem();

        // PERBAIKAN 3: Ambil Quantity Langsung dari EditText saat tombol simpan ditekan
        int finalQuantity = getCurrentQuantity();

        if (selectedProduk == null || selectedSize == null) {
            Toast.makeText(getContext(), "Pilih Produk & Ukuran dulu", Toast.LENGTH_SHORT).show();
            return;
        }

        // Validasi: Jangan kirim kalau 0 atau kosong
        if (finalQuantity <= 0) {
            Toast.makeText(getContext(), "Jumlah harus lebih dari 0", Toast.LENGTH_SHORT).show();
            return;
        }

        SharedPreferences sharedPref = getActivity().getSharedPreferences("UserSession", Context.MODE_PRIVATE);
        String token = sharedPref.getString("token", "");

        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        Call<ResponseBody> call = apiInterface.inputBarangMasuk(
                "Bearer " + token,
                selectedProduk.getId(),
                selectedSize.getId(),
                finalQuantity, // Kirim data yang baru diambil
                selectedDateForApi
        );

        Toast.makeText(getContext(), "Menyimpan...", Toast.LENGTH_SHORT).show();

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (getContext() == null) return;
                if (response.isSuccessful()) {
                    Toast.makeText(getContext(), "Berhasil Disimpan!", Toast.LENGTH_LONG).show();
                    tvQuantity.setText("1"); // Reset ke 1
                    if (etCatatan != null) etCatatan.setText("");
                } else {
                    Toast.makeText(getContext(), "Gagal Simpan", Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                if (getContext() != null) Toast.makeText(getContext(), "Gagal Terhubung", Toast.LENGTH_SHORT).show();
            }
        });
    }
}