package com.example.smartwallet;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.fragment.app.Fragment;


public class PaymentFragment extends Fragment {
    View view;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_payment, container, false);
        // Inflate the layout for this fragment
        ImageView payy = view.findViewById( R.id.GooglePayButton );
        payy.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Uri webpage = Uri.parse( "https://play.google.com/store/apps/details?id=com.google.android.apps.walletnfcrel" );
                Intent Website = new Intent( Intent.ACTION_VIEW, webpage );
                startActivity( Website );
            }
        });
    return view;
    }

}