package com.example.kkp;

import android.app.DatePickerDialog;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class BarangFragment extends Fragment {


    TextView btnTabMasuk, btnTabKeluar, tvQuantity, tvTanggal;
    LinearLayout layoutTanggal;
    Button btnSimpan, btnPlus, btnMinus;
    Spinner spinnerProduk;

    boolean isMasuk = true;
    int quantity = 1;


    Calendar calendar = Calendar.getInstance();

    public BarangFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_barang, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        btnTabMasuk = view.findViewById(R.id.btnTabMasuk);
        btnTabKeluar = view.findViewById(R.id.btnTabKeluar);
        tvQuantity = view.findViewById(R.id.tvQuantity);
        btnPlus = view.findViewById(R.id.btnPlus);
        btnMinus = view.findViewById(R.id.btnMinus);
        btnSimpan = view.findViewById(R.id.btnSimpan);
        spinnerProduk = view.findViewById(R.id.spinnerProduk);


        layoutTanggal = view.findViewById(R.id.layoutTanggal);
        tvTanggal = view.findViewById(R.id.tvTanggal);


        updateLabelTanggal();


        layoutTanggal.setOnClickListener(v -> showDatePicker());


        setupSpinner();

        btnTabMasuk.setOnClickListener(v -> switchTab(true));
        btnTabKeluar.setOnClickListener(v -> switchTab(false));

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

        btnSimpan.setOnClickListener(v -> {
            String tipe = isMasuk ? "Masuk" : "Keluar";
            String barang = spinnerProduk.getSelectedItem().toString();

            if (barang.equals("Pilih Barang")) {
                Toast.makeText(getContext(), "Pilih barang dulu", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getContext(), "Data "  + tipe + " Tersimpan", Toast.LENGTH_SHORT).show();
            }
        });
    }


    private void showDatePicker() {
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                requireContext(),
                (view, year, month, dayOfMonth) -> {

                    calendar.set(Calendar.YEAR, year);
                    calendar.set(Calendar.MONTH, month);
                    calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                    updateLabelTanggal();
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
        );

        datePickerDialog.show();
    }


    private void updateLabelTanggal() {
        String myFormat = "dd/MM/yyyy";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.getDefault());
        tvTanggal.setText(sdf.format(calendar.getTime()));
    }


    private void setupSpinner() {
        String[] daftarBarang = {
                "Pilih Barang",
                "Kain A",
                "Kain B",
                "Kain C",
                "Kain D"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_item, daftarBarang);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerProduk.setAdapter(adapter);
    }

    private void switchTab(boolean statusMasuk) {
        isMasuk = statusMasuk;
        Typeface fontBold = ResourcesCompat.getFont(requireContext(), R.font.opensansbold);
        Typeface fontRegular = ResourcesCompat.getFont(requireContext(), R.font.opensansreguler);

        if (isMasuk) {
            btnTabMasuk.setBackgroundResource(R.drawable.bg_toggle_active);
            btnTabMasuk.setTextColor(ContextCompat.getColor(requireContext(), R.color.black));
            btnTabMasuk.setTypeface(fontBold);
            btnTabKeluar.setBackgroundResource(0);
            btnTabKeluar.setTextColor(ContextCompat.getColor(requireContext(), android.R.color.darker_gray));
            btnTabKeluar.setTypeface(fontRegular);
            btnSimpan.setText("SIMPAN CATATAN MASUK");
        } else {
            btnTabMasuk.setBackgroundResource(0);
            btnTabMasuk.setTextColor(ContextCompat.getColor(requireContext(), android.R.color.darker_gray));
            btnTabMasuk.setTypeface(fontRegular);
            btnTabKeluar.setBackgroundResource(R.drawable.bg_toggle_active);
            btnTabKeluar.setTextColor(ContextCompat.getColor(requireContext(), R.color.black));
            btnTabKeluar.setTypeface(fontBold);
            btnSimpan.setText("SIMPAN CATATAN KELUAR");
        }
    }
}