package com.example.piacpalota.u.i;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.example.piacpalota.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class AccountFragment extends Fragment {

    private static final int PICK_IMAGE_REQUEST = 1; // Kép kiválasztásának kódja

    private FirebaseUser user;
    private ImageView profileImageView;
    private EditText nameEditText;
    private Button uploadButton;
    private Button updateNameButton;

    private StorageReference storageReference;

    public AccountFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_account, container, false);

        // Initialize Firebase User és Storage Referencia
        user = FirebaseAuth.getInstance().getCurrentUser();
        storageReference = FirebaseStorage.getInstance().getReference("profile_pics");

        // UI komponensek beállítása
        profileImageView = view.findViewById(R.id.profileImageView);
        nameEditText = view.findViewById(R.id.nameEditText);
        uploadButton = view.findViewById(R.id.uploadButton);
        updateNameButton = view.findViewById(R.id.updateNameButton);

        // Ha be van jelentkezve a felhasználó
        if (user != null) {
            // Megjelenítjük a felhasználó nevét
            nameEditText.setText(user.getDisplayName());

            // Megjelenítjük a felhasználó profilképét, ha van
            if (user.getPhotoUrl() != null) {
                // Ha van Firebase-ből kép, betöltjük
                Glide.with(getContext())
                        .load(user.getPhotoUrl())
                        .into(profileImageView);
            } else {
                // Ha nincs Firebase-ből kép, akkor a helyettesítő kép (account.jpg) jelenik meg
                Glide.with(getContext())
                        .load(R.drawable.account) // account.jpg a drawable mappában
                        .into(profileImageView);
            }
        }

        // Feltöltés gomb eseménykezelője
        uploadButton.setOnClickListener(v -> openFileChooser());

        // Név frissítése gomb eseménykezelője
        updateNameButton.setOnClickListener(v -> updateName());

        return view;
    }

    // Kép kiválasztásának indítása
    private void openFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Profile Image"), PICK_IMAGE_REQUEST);
    }

    // Kép kiválasztása után feltöltés Firebase Storage-ba
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == getActivity().RESULT_OK && data != null && data.getData() != null) {
            Uri imageUri = data.getData();
            uploadImageToFirebase(imageUri);
        }
    }

    // Kép feltöltése Firebase Storage-ba
    private void uploadImageToFirebase(Uri imageUri) {
        if (imageUri != null) {
            StorageReference fileReference = storageReference.child(user.getUid() + ".jpg");
            fileReference.putFile(imageUri)
                    .addOnSuccessListener(taskSnapshot -> fileReference.getDownloadUrl()
                            .addOnSuccessListener(uri -> {
                                updateProfileImage(uri);
                            }))
                    .addOnFailureListener(e -> Toast.makeText(getContext(), "Upload failed", Toast.LENGTH_SHORT).show());
        }
    }

    // Frissítjük a profilképet Firebase Authentication-ban
    private void updateProfileImage(Uri uri) {
        if (user != null) {
            user.updateProfile(new com.google.firebase.auth.UserProfileChangeRequest.Builder()
                            .setPhotoUri(uri)
                            .build())
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            // Frissítjük a képet
                            Glide.with(getContext())
                                    .load(uri)
                                    .into(profileImageView);
                            Toast.makeText(getContext(), "Profile image updated", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(getContext(), "Failed to update image", Toast.LENGTH_SHORT).show();
                        }
                    });
        }
    }

    // Frissítjük a felhasználó nevét Firebase Authentication-ban
    private void updateName() {
        String newName = nameEditText.getText().toString().trim();
        if (user != null && !newName.isEmpty()) {
            user.updateProfile(new com.google.firebase.auth.UserProfileChangeRequest.Builder()
                            .setDisplayName(newName)
                            .build())
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            Toast.makeText(getContext(), "Name updated", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(getContext(), "Failed to update name", Toast.LENGTH_SHORT).show();
                        }
                    });
        }
    }
}
