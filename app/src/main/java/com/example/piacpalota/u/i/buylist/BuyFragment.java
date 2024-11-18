package com.example.piacpalota.u.i.buylist;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.piacpalota.R;
import com.example.piacpalota.data.BuyManager;
import com.example.piacpalota.domain.Buy;
import androidx.appcompat.widget.SearchView; // Az AndroidX SearchView használata

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class BuyFragment extends Fragment {

    private BuyListAdapter adapter;
    private List<Buy> buyList;

    // Létrehozzuk az interfészt, hogy az Activity megfelelő metódusát hívhassuk
    public interface OnBuySelectedListener {
        void onBuySelected(Buy buy);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_buy, container, false);

        buyList = BuyManager.loadMockData();
        adapter = new BuyListAdapter(buyList, this);

        RecyclerView buyRecyclerView = view.findViewById(R.id.buyRecyclerView);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        buyRecyclerView.setLayoutManager(layoutManager);
        buyRecyclerView.setAdapter(adapter);

        // Keresés beállítása - HASZNÁLJUK AZ ANDROIDX KERESŐT
        SearchView searchView = view.findViewById(R.id.searchView);  // Itt a helyes típus, ami AndroidX-ból jön
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                // Azonnali keresés a szöveg beírása után
                adapter.getFilter().filter(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                // Frissítés minden karakter változásakor
                adapter.getFilter().filter(newText);
                return false;
            }
        });

        // Rendezés gomb beállítása
        Button sortButton = view.findViewById(R.id.sortButton);
        sortButton.setOnClickListener(v -> showSortDialog());

        return view;
    }

    // Az egyszerű rendezési dialógus
    private void showSortDialog() {
        // Az újabb rendezési logika itt történik, például egy egyszerű menü, vagy egy toast üzenet
        // A különböző rendezési módok (pl. termék, kategória, hely) kiválasztása alapján
        // Az alábbiakban egyszerűsített példák:

        // Például: rendezés a "termek" alapján
        Collections.sort(buyList, new Comparator<Buy>() {
            @Override
            public int compare(Buy o1, Buy o2) {
                return o1.getTermek().compareTo(o2.getTermek());
            }
        });

        adapter.notifyDataSetChanged(); // A lista frissítése
    }

    // Az Activity-nek átadjuk az adatokat
    public void showBuyDetails(Buy buy) {
        OnBuySelectedListener listener = (OnBuySelectedListener) getActivity();
        if (listener != null) {
            listener.onBuySelected(buy);  // Meghívjuk az Activity metódusát
        }
    }
}
