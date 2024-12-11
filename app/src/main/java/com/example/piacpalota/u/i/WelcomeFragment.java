package com.example.piacpalota.u.i;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.piacpalota.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

public class WelcomeFragment extends Fragment {

    private TextView welcomeText;
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_welcome, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        welcomeText = view.findViewById(R.id.welcomeText);
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            // Felhasználó ID alapján a név lekérése Firestore-ból
            String userId = currentUser.getUid();
            db.collection("users").document(userId)
                    .get()
                    .addOnSuccessListener(documentSnapshot -> {
                        if (documentSnapshot.exists()) {
                            String name = documentSnapshot.getString("name");
                            if (name != null && !name.isEmpty()) {
                                welcomeText.setText("Üdvözlünk, " + name + "!");
                            } else {
                                welcomeText.setText("Üdvözlünk, Kedves Felhasználó!");
                            }
                        } else {
                            welcomeText.setText("Üdvözlünk, Kedves Felhasználó!");
                        }
                    })
                    .addOnFailureListener(e -> {
                        Log.e("WelcomeFragment", "Hiba történt a név betöltésekor: ", e);
                        Toast.makeText(getActivity(), "Hiba történt a név betöltésekor.", Toast.LENGTH_SHORT).show();
                        welcomeText.setText("Üdvözlünk, Kedves Felhasználó!");
                    });
        } else {
            // Ha nincs bejelentkezett felhasználó
            welcomeText.setText("Üdvözlünk, Kedves Felhasználó!");
        }

        // Gombok inicializálása
        Button buyButton = view.findViewById(R.id.welcBuyButton);
        Button saleButton = view.findViewById(R.id.welcSaleButton);

        // Vásárolok gomb eseménykezelése
        buyButton.setOnClickListener(v -> {
            if (mAuth.getCurrentUser() != null) {
                // FragmentTransaction használata a navigációhoz
                FragmentTransaction transaction = requireActivity().getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.nav_host_fragment, new BuyFragment());  // Replace a buyFragment-tel
                transaction.addToBackStack(null); // opcionálisan, hogy vissza lehessen lépni
                transaction.commit();
            } else {
                Toast.makeText(getActivity(), "Bejelentkezés szükséges a vásárláshoz!", Toast.LENGTH_SHORT).show();
            }
        });

        // Eladok gomb eseménykezelése
        saleButton.setOnClickListener(v -> {
            if (mAuth.getCurrentUser() != null) {
                // FragmentTransaction használata a navigációhoz
                FragmentTransaction transaction = requireActivity().getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.nav_host_fragment, new SalesFragment());  // Replace a salesFragment-tel
                transaction.addToBackStack(null); // opcionálisan, hogy vissza lehessen lépni
                transaction.commit();
            } else {
                Toast.makeText(getActivity(), "Bejelentkezés szükséges az eladáshoz!", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
