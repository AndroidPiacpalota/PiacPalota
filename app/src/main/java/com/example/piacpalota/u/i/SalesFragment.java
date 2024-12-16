package com.example.piacpalota.u.i;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.example.piacpalota.R;
import com.example.piacpalota.u.i.buylist.Product;
import com.google.firebase.firestore.FirebaseFirestore;

public class SalesFragment extends Fragment {

    private EditText nameEditText, priceEditText, quantityEditText, locationEditText;
    private Button uploadButton;

    public SalesFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_sales, container, false);

        // Képernyőelemek inicializálása
        nameEditText = view.findViewById(R.id.nameEditText);
        priceEditText = view.findViewById(R.id.priceEditText);
        quantityEditText = view.findViewById(R.id.quantityEditText);
        locationEditText = view.findViewById(R.id.locationEditText);
        uploadButton = view.findViewById(R.id.uploadButton);

        // Feltöltés gomb click listener
        uploadButton.setOnClickListener(v -> uploadProduct(null)); // Alapértelmezett: nincs kép

        return view;
    }

    // Termék feltöltése Firebase Firestore-ba
    private void uploadProduct(Uri imageUri) {
        String name = nameEditText.getText().toString().trim();
        String price = priceEditText.getText().toString().trim();
        String quantity = quantityEditText.getText().toString().trim();
        String location = locationEditText.getText().toString().trim();
        String imageUrl = imageUri != null ? imageUri.toString() : ""; // Kép opcionális

        if (name.isEmpty() || price.isEmpty() || quantity.isEmpty() || location.isEmpty()) {
            Toast.makeText(getActivity(), "Kérlek, töltsd ki az összes mezőt!", Toast.LENGTH_SHORT).show();
            return;
        }

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        Product product = new Product(name, price, quantity, location, imageUrl);
        db.collection("products").add(product)
                .addOnSuccessListener(documentReference -> {
                    Toast.makeText(getActivity(), "Sikeres feltöltés!", Toast.LENGTH_SHORT).show();
                    clearInputs(); // Mezők ürítése a sikeres feltöltés után
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(getActivity(), "Hiba történt a feltöltés során.", Toast.LENGTH_SHORT).show();
                });
    }

    private void clearInputs() {
        nameEditText.setText("");
        priceEditText.setText("");
        quantityEditText.setText("");
        locationEditText.setText("");
    }
}