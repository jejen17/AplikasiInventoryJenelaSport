package com.example.kkp;

import android.content.Context; // Import ini
import android.content.Intent;
import android.content.SharedPreferences; // Import ini
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView; // Import ini

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class HomeFragment extends Fragment {

    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // 1. Inisialisasi View
        TextView tvNamaKaryawan = view.findViewById(R.id.tvNamaKaryawan);
        Button btnSuratSPK = view.findViewById(R.id.btnSuratSPK);
        // ... inisialisasi tombol lain jika ada ...

        // 2. AMBIL DATA DARI SHAREDPREFERENCES
        // "UserSession" harus SAMA PERSIS dengan yang kamu tulis di Login.java
        SharedPreferences sharedPref = requireActivity().getSharedPreferences("UserSession", Context.MODE_PRIVATE);

        // Ambil string "nama". Jika kosong, default-nya "Karyawan"
        String nama = sharedPref.getString("nama", "Karyawan");

        // 3. Tampilkan ke Layar
        tvNamaKaryawan.setText(nama);

        // --- Logika Tombol ---
        btnSuratSPK.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), SuratSPK.class);
            startActivity(intent);
        });
    }
}