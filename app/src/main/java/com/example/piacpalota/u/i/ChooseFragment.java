package com.example.piacpalota.u.i;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.piacpalota.R;

import java.util.ArrayList;

public class ChooseFragment extends Fragment {

    private ArrayList<String> itemList;  // Lista az elemekhez
    private ItemAdapter itemAdapter;     // Adapter a RecyclerView-hoz
    private EditText addItemEditText;    // EditText az új elem hozzáadásához

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_choose, container, false);

        // RecyclerView inicializálása
        RecyclerView recyclerView = view.findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        // EditText és Button inicializálása
        addItemEditText = view.findViewById(R.id.add_termek);
        Button addButton = view.findViewById(R.id.add_hozzaadas);

        // Lista és adapter inicializálása
        itemList = new ArrayList<>();
        itemAdapter = new ItemAdapter(itemList, this::modifyItem, this::deleteItem);
        recyclerView.setAdapter(itemAdapter);

        // Hozzáadás gomb eseménykezelője
        addButton.setOnClickListener(v -> addItem());

        return view;
    }

    // Új elem hozzáadása a listához
    private void addItem() {
        String newItem = addItemEditText.getText().toString();
        if (!TextUtils.isEmpty(newItem)) {
            itemList.add(newItem);
            itemAdapter.notifyItemInserted(itemList.size() - 1);
            addItemEditText.setText("");  // Szövegmező törlése
        } else {
            Toast.makeText(getContext(), "Adjon meg egy terméket!", Toast.LENGTH_SHORT).show();
        }
    }

    // Meglévő elem módosítása
    private void modifyItem(int position, String newItem) {
        if (position >= 0 && position < itemList.size()) {
            itemList.set(position, newItem);
            itemAdapter.notifyItemChanged(position);
        }
    }

    // Elem törlése a listából
    private void deleteItem(int position) {
        if (position >= 0 && position < itemList.size()) {
            itemList.remove(position);
            itemAdapter.notifyItemRemoved(position); // Csak az adott elem törlődik
            itemAdapter.notifyItemRangeChanged(position, itemList.size()); // Az elemek indexei frissülnek
        }
    }
}
