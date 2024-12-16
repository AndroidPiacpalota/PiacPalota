package com.example.piacpalota.u.i;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.example.piacpalota.MainActivity;
import com.example.piacpalota.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LogInFragment extends Fragment {

    private FirebaseAuth mAuth;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Infláljuk az XML layoutot
        View view = inflater.inflate(R.layout.fragment_log_in, container, false);

        // Firebase Auth inicializálása
        mAuth = FirebaseAuth.getInstance();

        Button loginButton = view.findViewById(R.id.logButton);
        EditText emailEditText = view.findViewById(R.id.emailEditText); // Email cím
        EditText passwordEditText = view.findViewById(R.id.logPasswordTextText); // Jelszó

        // Bejelentkezés gomb kezelése
        loginButton.setOnClickListener(v -> {
            String email = emailEditText.getText().toString();
            String password = passwordEditText.getText().toString();

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(getActivity(), "Email és jelszó nem lehet üres", Toast.LENGTH_SHORT).show();
                return;
            }

            // Email validálás
            if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                Toast.makeText(getActivity(), "Érvényes email címet adj meg!", Toast.LENGTH_SHORT).show();
                return;
            }

            // Bejelentkezés Firebase-be
            mAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(getActivity(), task -> {
                        if (task.isSuccessful()) {
                            // Bejelentkezés sikeres
                            FirebaseUser user = mAuth.getCurrentUser();
                            Toast.makeText(getActivity(), "Sikeres bejelentkezés!", Toast.LENGTH_SHORT).show();

                            // Menü frissítése a MainActivity-ben
                            MainActivity mainActivity = (MainActivity) getActivity();
                            if (mainActivity != null) {
                                mainActivity.replaceFragment(new WelcomeFragment());
                                mainActivity.invalidateOptionsMenu();  // Menü frissítése
                            }

                        } else {
                            // Hiba történt a bejelentkezéskor
                            Toast.makeText(getActivity(), "Hiba történt: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
        });

        return view;
    }
}