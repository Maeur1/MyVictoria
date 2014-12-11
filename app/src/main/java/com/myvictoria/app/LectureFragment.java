package com.myvictoria.app;

import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.TextView;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;

public class LectureFragment extends Fragment implements View.OnClickListener{

    Button submit;
    EditText input;
    TextView info, response;
    String room, type, day, start, end;
    Boolean found;
    ArrayList<String> strings = new ArrayList<String>();
    ExpandableListAdapter listAdapter;
    ExpandableListView expListView;
    List<String> listDataHeader;
    HashMap<String, List<String>> listDataChild;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_lecture, container, false);
        initialise(rootView);
        return rootView;
    }

    @Override
    public void onClick(View v) {
        listDataHeader.clear();
        listDataChild.clear();
        if (!input.getText().toString().matches("")) {
            if (v.getId() == R.id.bSubmit) {
                response.setText("");
                found = false;
                strings.clear();
                final ProgressDialog ringProgressDialog= ProgressDialog.show(getActivity(), "Please wait...", "Searching for Lectures...", true);
                AsyncTask<String, Void, ArrayList<String>> task = new AsyncTask<String, Void, ArrayList<String>>() {
                    @Override
                    protected ArrayList<String> doInBackground(String... params) {
                        InputStream inputStream = getResources().openRawResource(R.raw.classdata);
                        Scanner scan = new Scanner(inputStream);
                        String search = params[0];
                        ArrayList<String> strings2 = new ArrayList<String>();
                        while (scan.hasNext()) {
                            String check = scan.next();
                            if (check.contains(search.toUpperCase())) {
                                type = scan.next();
                                day = scan.next();
                                start = scan.next();
                                end = scan.next();
                                room = scan.next();
                                String helper = day;
                                for(int i = 0; i < day.length(); i++) {
                                    char s = day.charAt(i);
                                    switch (s){
                                        case 'M':
                                            helper = "Monday";
                                            break;
                                        case 'T':
                                            helper = "Tuesday";
                                            break;
                                        case 'W':
                                            helper = "Wednesday";
                                            break;
                                        case 'R':
                                            helper = "Thursday";
                                            break;
                                        case 'F':
                                            helper = "Friday";
                                            break;
                                        case 'S':
                                            helper = "Saturday";
                                            break;
                                    }
                                    strings2.add(check + " " + type + " is in " + room + " at " + start + " on " + helper);
                                }
                                found = true;
                            } else {
                                scan.nextLine();
                            }
                        }
                        return strings2;
                    }

                    @Override
                    protected void onPostExecute(ArrayList<String> strings2) {
                        strings.clear();
                        strings.addAll(strings2);
                        for(String s: strings2) {
                            String firstword = s.substring(0, s.indexOf(" "));
                            if(!listDataHeader.contains(firstword)){
                                listDataHeader.add(firstword);
                                listDataChild.put(firstword, new ArrayList<String>());
                            }
                            listDataChild.get(firstword).add(s.substring(firstword.length(), s.length()));
                        }
                        if (!found) {
                            response.setVisibility(View.VISIBLE);
                            response.setText("There were no matches found.");
                        } else {
                            listAdapter = new ExpandedListAdapter(getActivity().getApplicationContext(), listDataHeader, listDataChild);
                            expListView.setAdapter(listAdapter);
                            response.setVisibility(View.GONE);
                            expListView.setVisibility(View.VISIBLE);
                        }
                        ringProgressDialog.dismiss();
                    }
                };
                task.execute(input.getText().toString());
            }
        } else {
            response.setText("Please enter a class.");
            response.setVisibility(View.VISIBLE);
        }
        InputMethodManager imm = (InputMethodManager)getActivity().getSystemService(
                Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(input.getWindowToken(), 0);
    }

    private void initialise(View view){
        submit = (Button) view.findViewById(R.id.bSubmit);
        input = (EditText) view.findViewById(R.id.etInput);
        info = (TextView) view.findViewById(R.id.tvInfo);
        response = (TextView) view.findViewById(R.id.tvResponse);
        response.setVisibility(View.GONE);
        submit.setOnClickListener(this);
        expListView = (ExpandableListView) view.findViewById(R.id.lvExp);
        expListView.setVisibility(View.GONE);
        listDataHeader = new ArrayList<String>();
        listDataChild = new HashMap<String, List<String>>();
    }


}
