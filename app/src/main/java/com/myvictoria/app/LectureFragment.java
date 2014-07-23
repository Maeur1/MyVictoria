package com.myvictoria.app;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Scanner;

/**
 * Created by Mayur on 11/05/2014.
 */
public class LectureFragment extends Fragment implements View.OnClickListener{

    Button submit;
    EditText input;
    TextView info, response;
    String room, type, day, start, end;
    Boolean found;
    Calendar c = Calendar.getInstance();
    ArrayList<String> strings = new ArrayList<String>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_lecture, container, false);
        initialise(rootView);
        return rootView;
    }

    @Override
    public void onClick(View v) {
        if (!input.getText().toString().matches("")) {
            if (v.getId() == R.id.bSubmit) {
                response.setText("");
                found = false;
                String check = "";
                Scanner scan = new Scanner(getResources().openRawResource(R.raw.classdata));
                getActivity().setProgressBarIndeterminateVisibility(true);
                while (scan.hasNext()) {
                    check = scan.next();
                    if (check.contains(input.getText().toString().toUpperCase())) {
                        type = scan.next();
                        day = scan.next();
                        start = scan.next();
                        end = scan.next();
                        room = scan.next();
                        strings.add(check + " " + type + " is in " + room + " at " + start + " on " + day + "\n");
                        response.setVisibility(View.VISIBLE);
                        found = true;
                    }
                }
                for (int i = 0; i < strings.size(); i++)
                    response.append(strings.get(i).toString());

                if (!found) {
                    response.setText("There were no matches found.");
                    response.setVisibility(View.VISIBLE);
                }
                InputMethodManager imm = (InputMethodManager)getActivity().getSystemService(
                        Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(input.getWindowToken(), 0);
                getActivity().setProgressBarIndeterminateVisibility(false);
            }
        } else {
            response.setText("Please enter a class.");
            response.setVisibility(View.VISIBLE);
        }
    }

    private void initialise(View view){
        submit = (Button) view.findViewById(R.id.bSubmit);
        input = (EditText) view.findViewById(R.id.etInput);
        info = (TextView) view.findViewById(R.id.tvInfo);
        response = (TextView) view.findViewById(R.id.tvResponse);
        response.setVisibility(View.GONE);
        submit.setOnClickListener(this);

    }
}
