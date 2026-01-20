package com.example.kkp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import api.ApiClient;
import api.ApiInterface;
import model.LoginResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Login extends AppCompatActivity {

    EditText etEmail, etPassword;
    Button btnMasuk;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        btnMasuk = findViewById(R.id.btnMasuk);


        btnMasuk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String username = etEmail.getText().toString();
                String password = etPassword.getText().toString();

                if (username.isEmpty() || password.isEmpty()) {
                    Toast.makeText(Login.this, "Username dan Password harus diisi!", Toast.LENGTH_SHORT).show();
                } else {
                    loginProses(username, password);
                }
            }
        });
    }


    private void loginProses(String username, String password) {
        Toast.makeText(this, "Sedang Login...", Toast.LENGTH_SHORT).show();


        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        Call<LoginResponse> loginCall = apiInterface.loginUser(username, password);

        loginCall.enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                // Cek apakah server merespon (HTTP 200 OK)
                if (response.isSuccessful() && response.body() != null) {

                    LoginResponse loginResponse = response.body();

                    // Cek apakah Login Sukses (success: true)
                    if (loginResponse.isSuccess()) {

                        // AMBIL DATA PENTING
                        String namaUser = loginResponse.getData().getUser().getNama();
                        String token = loginResponse.getData().getToken();


                        SharedPreferences sharedPref = getSharedPreferences("UserSession", MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPref.edit();
                        editor.putString("nama", namaUser);
                        editor.putString("token", token);
                        editor.putBoolean("isLoggedIn", true);
                        editor.apply();

                        // PINDAH KE MENU UTAMA
                        Toast.makeText(Login.this, "Login Berhasil ", Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(Login.this, MainActivity.class);
                        startActivity(intent);
                        finish(); // Tutup halaman login agar tidak bisa back

                    } else {
                        Toast.makeText(Login.this, loginResponse.getMessage(), Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(Login.this, "Gagal terhubung ke server", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                Toast.makeText(Login.this, "Error: " + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }
}