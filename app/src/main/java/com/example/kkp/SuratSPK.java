package com.example.kkp;

import android.os.Bundle;
import android.widget.ListView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;

public class SuratSPK extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_surat_spk);

        ListView listView = findViewById(R.id.lvSuratSPK);

        ArrayList<String> dataList = new ArrayList<>();
        dataList.add("Surat SPK pt jambu");
        dataList.add("Surat SPK pt mangga");
        dataList.add("Surat SPK pt durian");
        dataList.add("Surat SPK pt pisang");

        SuratSPKAdapter adapter = new SuratSPKAdapter(this, dataList);
        listView.setAdapter(adapter);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
}