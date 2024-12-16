package com.example.piacpalota.u.i;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.example.piacpalota.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

public class SingInFragment extends Fragment {

    private FirebaseAuth mAuth;
    private FirebaseFirestore db;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_sing_in, container, false);

        // Firebase Auth és Firestore inicializálása
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        Button registerButton = view.findViewById(R.id.registButton);
        EditText emailEditText = view.findViewById(R.id.emailEditText);
        EditText passwordEditText = view.findViewById(R.id.passwordEditText);
        EditText nameEditText = view.findViewById(R.id.nameEditText); // Név mező

        // Regisztráció gomb kattintása
        registerButton.setOnClickListener(v -> {
            String name = nameEditText.getText().toString();
            String email = emailEditText.getText().toString();
            String password = passwordEditText.getText().toString();

            if (name.isEmpty() || email.isEmpty() || password.isEmpty()) {
                Toast.makeText(getActivity(), "Minden mező kitöltése kötelező!", Toast.LENGTH_SHORT).show();
                return;
            }

            // Felhasználó regisztrálása
            mAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(getActivity(), task -> {
                        if (task.isSuccessful()) {
                            // Regisztráció sikeres
                            FirebaseUser user = mAuth.getCurrentUser();

                            // Felhasználó név mentése Firestore-ba
                            if (user != null) {
                                db.collection("users").document(user.getUid())
                                        .set(new User(name, email))  // Felhasználó adatok mentése
                                        .addOnSuccessListener(aVoid -> {
                                            Toast.makeText(getActivity(), "Sikeres regisztráció és név mentés!", Toast.LENGTH_SHORT).show();
                                        })
                                        .addOnFailureListener(e -> {
                                            Toast.makeText(getActivity(), "Név mentése nem sikerült: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                        });
                            }

                            // Navigálj a következő fragment-be, ha szükséges
                        } else {
                            // Hiba történt a regisztráció során
                            Toast.makeText(getActivity(), "Hiba történt: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
        });

        return view;
    }
}