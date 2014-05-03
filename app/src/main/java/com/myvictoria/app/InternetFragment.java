package com.myvictoria.app;

import android.app.Fragment;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;

/**
 * Created by Mayur on 4/05/2014.
 */
public class InternetFragment extends Fragment{

    public WebView internet;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_internet, container, false);
        internet = (WebView) view.findViewById(R.id.webview);
        internet.getSettings().setLoadsImagesAutomatically(true);
        internet.getSettings().setJavaScriptEnabled(true);
        internet.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
        internet.setWebViewClient(new MyBrowser());
        internet.loadUrl("https://my.vuw.ac.nz/cp/home/displaylogin");
        return view;
    }

    private class MyBrowser extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }
    }
}
