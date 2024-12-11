package com.example.piacpalota.u.i;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.piacpalota.R;
import com.example.piacpalota.u.i.buylist.BuyAdapter;
import com.example.piacpalota.u.i.buylist.Product;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class BuyFragment extends Fragment {

    private RecyclerView recyclerView;
    private BuyAdapter buyAdapter;
    private List<Product> buyList;
    private FirebaseFirestore db;

    public interface OnBuySelectedListener {
        void onBuySelected(Product product);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_buy, container, false);

        recyclerView = view.findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        db = FirebaseFirestore.getInstance();
        buyList = new ArrayList<>();
        buyAdapter = new BuyAdapter(getContext(), buyList, product -> {
            if (getActivity() instanceof OnBuySelectedListener) {
                ((OnBuySelectedListener) getActivity()).onBuySelected(product);
            }
        });
        recyclerView.setAdapter(buyAdapter);

        fetchProductsFromFirestore();

        return view;
    }

    private void fetchProductsFromFirestore() {
        db.collection("products")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        buyList.clear(); // Kihagyjuk a korábbi adatokat
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            String name = document.getString("name");
                            String price = document.getString("price");
                            String quantity = document.getString("quantity");
                            String location = document.getString("location");
                            String imageUrl = document.getString("imageUrl");

                            if (name != null && price != null && quantity != null && location != null) {
                                buyList.add(new Product(name, price, quantity, location, imageUrl));
                            } else {
                                Log.e("Firestore", "Hibás adat: " + document.getId());
                            }
                        }
                        buyAdapter.notifyDataSetChanged();
                    } else {
                        Toast.makeText(getContext(), "Adatok betöltése sikertelen!", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(e -> {
                    Log.e("Firestore", "Hiba: ", e);
                    Toast.makeText(getContext(), "Nem sikerült az adatok lekérdezése.", Toast.LENGTH_SHORT).show();
                });
    }
}
