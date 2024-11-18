package com.example.piacpalota.u.i.buydetails;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.piacpalota.MainActivity;
import com.example.piacpalota.R;
import com.example.piacpalota.domain.Buy;


public class BuyDetailsFragment extends Fragment {


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_buy_details, container, false);

        TextView buyDetailsTextView = view.findViewById(R.id.buyDetailsTextView);
        MainActivity mainActivity = (MainActivity) getActivity();
        Buy buy = mainActivity.getSelectedBuy();

        buyDetailsTextView.setText(buy.getTermek());


        return view;
    }
}