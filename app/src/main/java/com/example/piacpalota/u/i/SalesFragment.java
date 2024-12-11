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

    private EditText nameEditText, priceEditText, quantityEditText, locationEditText, imageUrlEditText;
    private Button uploadButton;
    private static final int PICK_IMAGE_REQUEST = 1; // Kép kiválasztásának kódja

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
        imageUrlEditText = view.findViewById(R.id.imageUrlEditText);
        uploadButton = view.findViewById(R.id.uploadButton);

        // Kép feltöltése gomb click listener
        uploadButton.setOnClickListener(v -> openFileChooser());

        return view;
    }

    // Kép kiválasztásának indítása
    private void openFileChooser() {
        Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setType("image/*");
        startActivityForResult(Intent.createChooser(intent, "Select Product Image"), PICK_IMAGE_REQUEST);
    }

    // Az ActivityResult kezelése a képek kiválasztásához
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == getActivity().RESULT_OK && data != null && data.getData() != null) {
            Uri imageUri = data.getData();
            uploadProduct(imageUri);
        }
    }

    // Termék feltöltése Firebase Firestore-ba
    private void uploadProduct(Uri imageUri) {
        String name = nameEditText.getText().toString();
        String price = priceEditText.getText().toString();
        String quantity = quantityEditText.getText().toString();
        String location = locationEditText.getText().toString();
        String imageUrl = imageUri != null ? imageUri.toString() : imageUrlEditText.getText().toString();

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        Product product = new Product(name, price, quantity, location, imageUrl);
        db.collection("products").add(product)
                .addOnSuccessListener(documentReference -> {
                    Toast.makeText(getActivity(), "Sikeres feltöltés!", Toast.LENGTH_SHORT).show();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(getActivity(), "Hiba történt a feltöltés során.", Toast.LENGTH_SHORT).show();
                });
    }
}
