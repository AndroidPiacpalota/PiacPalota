package com.example.piacpalota.u.i;

import android.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.piacpalota.R;

import java.util.ArrayList;

public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.ViewHolder> {

    private ArrayList<String> items;
    private OnItemModifyListener modifyListener;
    private OnItemDeleteListener deleteListener;

    public ItemAdapter(ArrayList<String> items, OnItemModifyListener modifyListener, OnItemDeleteListener deleteListener) {
        this.items = items;
        this.modifyListener = modifyListener;
        this.deleteListener = deleteListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_view, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String item = items.get(position);
        holder.textView.setText(item);

        // "Módosítás" gomb eseménykezelője
        holder.modifyButton.setOnClickListener(v -> {
            // Dialog ablak létrehozása
            AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
            builder.setTitle("Termék módosítása");

            // EditText hozzáadása a dialoghoz
            final EditText input = new EditText(v.getContext());
            input.setText(item); // Az aktuális nevet jeleníti meg alapértelmezésként
            builder.setView(input);

            // Gombok beállítása
            builder.setPositiveButton("OK", (dialog, which) -> {
                String newName = input.getText().toString();
                modifyListener.onModify(position, newName); // Frissíti az új névvel
            });
            builder.setNegativeButton("Mégse", (dialog, which) -> dialog.cancel());

            // Dialog megjelenítése
            builder.show();
        });

        // "Törlés" gomb eseménykezelője
        holder.deleteButton.setOnClickListener(v -> {
            // A törlés mostantól a fragmentben történik
            deleteListener.onDelete(position); // Jelzi a fragmentnek, hogy törölni kell
        });
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView textView;
        Button modifyButton;
        Button deleteButton;

        public ViewHolder(View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.item_name);
            modifyButton = itemView.findViewById(R.id.modify_button);
            deleteButton = itemView.findViewById(R.id.delete_button);
        }
    }

    public interface OnItemModifyListener {
        void onModify(int position, String newItem);
    }

    public interface OnItemDeleteListener {
        void onDelete(int position);
    }
}
