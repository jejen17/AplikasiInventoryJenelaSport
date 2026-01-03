package com.example.kkp;

import android.graphics.Typeface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;

public class HistoryFragment extends Fragment {

    TextView tabSemua, tabMasuk, tabKeluar;

    public HistoryFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_history, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        tabSemua = view.findViewById(R.id.tabSemua);
        tabMasuk = view.findViewById(R.id.tabMasuk);
        tabKeluar = view.findViewById(R.id.tabKeluar);

        tabSemua.setOnClickListener(v -> updateFilterUI(tabSemua));
        tabMasuk.setOnClickListener(v -> updateFilterUI(tabMasuk));
        tabKeluar.setOnClickListener(v -> updateFilterUI(tabKeluar));

    }

    private void updateFilterUI(TextView selectedTab) {
        Typeface fontBold = ResourcesCompat.getFont(requireContext(), R.font.opensansbold);
        Typeface fontRegular = ResourcesCompat.getFont(requireContext(), R.font.opensansreguler);

        TextView[] allTabs = {tabSemua, tabMasuk, tabKeluar};

        for (TextView tab : allTabs) {
            if (tab == selectedTab) {
                tab.setBackgroundResource(R.drawable.bg_filter_active);
                tab.setTextColor(ContextCompat.getColor(requireContext(), R.color.white));
                tab.setTypeface(fontBold);
            } else {
                tab.setBackgroundResource(R.drawable.bg_filter_inactive);
                tab.setTextColor(ContextCompat.getColor(requireContext(), R.color.Gray));
                tab.setTypeface(fontRegular);
            }
        }
    }
}