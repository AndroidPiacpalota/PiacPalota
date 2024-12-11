package com.example.piacpalota.u.i;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.fragment.app.Fragment;
import com.example.piacpalota.R;
import com.example.piacpalota.MainActivity;

public class HomeFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        Button loginButton = view.findViewById(R.id.welcBuyButton);
        Button regButton = view.findViewById(R.id.welcSaleButton);

        // Fragment tranzakció a helyes fragmenthez
        loginButton.setOnClickListener(v -> {
            // Használjuk a replaceFragment metódust a MainActivity-ben
            ((MainActivity) requireActivity()).replaceFragment(new LogInFragment());
        });

        regButton.setOnClickListener(v -> {
            // Ugyanez a regisztrációs fragmenthez
            ((MainActivity) requireActivity()).replaceFragment(new SingInFragment());
        });

        return view;
    }
}
