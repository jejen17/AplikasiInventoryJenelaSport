package com.example.kkp;

import android.os.Bundle;
import android.widget.ImageView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;

public class MainActivity extends AppCompatActivity {

    ImageView navHome, navHistory, navStok, navProfile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        navHome = findViewById(R.id.navHome);
        navHistory = findViewById(R.id.navHistory);
        navStok = findViewById(R.id.navStok);
        navProfile = findViewById(R.id.navProfile);

        if (savedInstanceState == null) {
            loadFragment(new HomeFragment());
            updateNavUI(navHome);
        }

        navHome.setOnClickListener(view -> {
            loadFragment(new HomeFragment());
            updateNavUI(navHome);
        });

        navHistory.setOnClickListener(view -> {

            loadFragment(new HistoryFragment());
            updateNavUI(navHistory);
        });

        navStok.setOnClickListener(view -> {

            loadFragment(new BarangFragment());
            updateNavUI(navStok);
        });

        navProfile.setOnClickListener(view -> {
            loadFragment(new ProfileFragment());
            updateNavUI(navProfile);
        });


        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
    private void updateNavUI(ImageView selectedNav) {

        ImageView[] allNavs = {navHome, navHistory, navStok, navProfile};

        for (ImageView nav : allNavs) {
            nav.setBackgroundResource(0);
            nav.setColorFilter(ContextCompat.getColor(this, R.color.black));
        }

        selectedNav.setBackgroundResource(R.drawable.bg_nav_selected);
        selectedNav.setColorFilter(ContextCompat.getColor(this, R.color.nav_icon_selected));
    }

    private void loadFragment(Fragment fragment) {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .commit();
    }
}