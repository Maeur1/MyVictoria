package com.myvictoria.app;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;

/**
 * Created by Mayur on 4/05/2014.
 */
public class CalendarFragment extends Fragment {

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
        internet.loadUrl("https://csa-public.vuw.ac.nz/pls/webprod/bwskfshd.P_CrseSchd");
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