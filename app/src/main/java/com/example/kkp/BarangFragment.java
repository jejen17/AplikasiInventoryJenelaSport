package com.example.kkp;

import android.app.DatePickerDialog;
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
import androidx.fragment.app.Fragment;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class BarangFragment extends Fragment {

    TextView tvQuantity, tvTanggal;
    LinearLayout layoutTanggal;
    Button btnSimpan, btnPlus, btnMinus;
    Spinner spinnerProduk, spinnerUkuran;

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


        tvQuantity = view.findViewById(R.id.tvQuantity);
        btnPlus = view.findViewById(R.id.btnPlus);
        btnMinus = view.findViewById(R.id.btnMinus);
        btnSimpan = view.findViewById(R.id.btnSimpan);
        spinnerProduk = view.findViewById(R.id.spinnerProduk);
        spinnerUkuran = view.findViewById(R.id.spinnerUkuran);
        layoutTanggal = view.findViewById(R.id.layoutTanggal);
        tvTanggal = view.findViewById(R.id.tvTanggal);


        updateLabelTanggal();


        setupSpinners();


        layoutTanggal.setOnClickListener(v -> showDatePicker());


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
            String barang = spinnerProduk.getSelectedItem().toString();
            String ukuran = spinnerUkuran.getSelectedItem().toString();
            String tanggal = tvTanggal.getText().toString();

            if (barang.equals("Pilih Barang")) {
                Toast.makeText(getContext(), "Pilih barang dulu!", Toast.LENGTH_SHORT).show();
            } else if (ukuran.equals("Pilih Ukuran")) {
                Toast.makeText(getContext(), "Pilih ukuran dulu!", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getContext(), "Data Tersimpan", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setupSpinners() {

        String[] daftarBarang = {
                "Pilih Barang",
                "Baju A",
                "Baju B",
                "Baju C",
                "Baju E"};
        ArrayAdapter<String> adapterProduk = new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_item, daftarBarang);
        adapterProduk.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerProduk.setAdapter(adapterProduk);


        String[] daftarUkuran = {
                "Pilih Ukuran",
                "5",
                "7",
                "8",
                "S",
                "M",
                "L",
                "XL"};
        ArrayAdapter<String> adapterUkuran = new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_item, daftarUkuran);
        adapterUkuran.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerUkuran.setAdapter(adapterUkuran);
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
}