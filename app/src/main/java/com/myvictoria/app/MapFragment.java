package com.myvictoria.app;

import android.app.Activity;
import android.app.Fragment;
import android.graphics.Point;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

/**
 * Created by Mayur on 4/05/2014.
 */
public class MapFragment extends Fragment implements View.OnClickListener{

    TouchImageView im;
    Button pip, kel, kar;
    ProgressBar pb;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_map, container, false);
        initialise(rootView);
        pb.setVisibility(View.GONE);
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
        pb = (ProgressBar) view.findViewById(R.id.pbImage);
        im = (TouchImageView) view.findViewById(R.id.map);
        pip = (Button) view.findViewById(R.id.bPip);
        kel = (Button) view.findViewById(R.id.bKelburn);
        kar = (Button) view.findViewById(R.id.bKaro);
        pip.setOnClickListener(this);
        kel.setOnClickListener(this);
        kar.setOnClickListener(this);
    }



}
