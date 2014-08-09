package com.myvictoria.app;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

public class MapFragment extends Fragment implements View.OnClickListener{

    TouchImageView im;
    Button pip, kel, kar;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_map, container, false);
        initialise(rootView);
        return rootView;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bPip:
                im.setImageResource(R.drawable.pipitea_map);
                break;
            case R.id.bKelburn:
                im.setImageResource(R.drawable.kelburn_map);
                break;
            case R.id.bKaro:
                im.setImageResource(R.drawable.karori_map);
                break;
        }
    }

    private void initialise(View view){
        im = (TouchImageView) view.findViewById(R.id.map);
        pip = (Button) view.findViewById(R.id.bPip);
        kel = (Button) view.findViewById(R.id.bKelburn);
        kar = (Button) view.findViewById(R.id.bKaro);
        pip.setOnClickListener(this);
        kel.setOnClickListener(this);
        kar.setOnClickListener(this);
    }



}
