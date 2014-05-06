package com.myvictoria.app;

import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import org.apache.http.util.EncodingUtils;

import android.webkit.CookieManager;

/**
 * Created by Mayur on 4/05/2014.
 */
public class InternetFragment extends Fragment{

    private static String ARG_URL = "https://my.vuw.ac.nz/cp/home/displaylogin";
    public WebView internet;
    public String name, pass;

    public static InternetFragment newInstance(String url) {
        InternetFragment fragment = new InternetFragment();
        Bundle args = new Bundle();
        args.putString(ARG_URL, url);
        fragment.setArguments(args);
        return fragment;
    }

    public static InternetFragment newInstanceLogin(String url, String n, String p) {
        InternetFragment fragment = new InternetFragment();
        Bundle args = new Bundle();
        args.putString(ARG_URL, url);
        args.putString("name", n);
        args.putString("pass", p);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void setArguments(Bundle args) {
        super.setArguments(args);
        ARG_URL = args.getString(ARG_URL);
        name = args.getString("user");
        pass = args.getString("pass");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_internet, container, false);
        internet = (WebView) view.findViewById(R.id.webview);
        internet.getSettings().setLoadsImagesAutomatically(true);
        internet.getSettings().setJavaScriptEnabled(true);
        internet.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
        internet.setWebViewClient(new MyBrowser());
        if(ARG_URL.contains("https://my.vuw.ac.nz/")) {
            internet.loadUrl(ARG_URL);
            String uuid = getCookie(ARG_URL, "JSESSIONID");
            //Log.d("UUID MESSAGE", uuid);
            String postData = "pass=" + pass + "&user=" + name + "&uuid" + uuid;
            internet.postUrl(ARG_URL, postData.getBytes());
        } else {
            internet.loadUrl(ARG_URL);
        }
        return view;
    }

    public String getCookie(String siteName,String CookieName){
        String CookieValue = null;
        CookieManager cookieManager = CookieManager.getInstance();
        String cookies = cookieManager.getCookie(siteName);
        String[] temp=cookies.split("[;]");
        for (String ar1 : temp ){
            if(ar1.contains(CookieName)){
                String[] temp1=ar1.split("[=]");
                CookieValue = temp1[1];
            }
        }
        return CookieValue;
    }

    private class MyBrowser extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }
    }
}
