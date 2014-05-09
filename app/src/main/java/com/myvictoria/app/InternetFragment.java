package com.myvictoria.app;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.JavascriptInterface;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import org.apache.http.util.EncodingUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;

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

    public static InternetFragment newInstanceLogin(String url, String na, String pa) {
        InternetFragment fragment = new InternetFragment();
        Bundle args = new Bundle();
        args.putString(ARG_URL, url);
        args.putString("pass", pa);
        args.putString("user", na);
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
        internet.loadUrl(ARG_URL);
        return view;
    }

    private class MyBrowser extends WebViewClient {

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            if(url.equals("https://my.vuw.ac.nz/cp/home/displaylogin")) {
                view.loadUrl("javascript:document.getElementById('pass').value = '" + pass + "';document.getElementById('user').value='" + name + "';login(); return false;");
            }/* else if(url.equals("https://blackboard.vuw.ac.nz/webapps/portal/frameset.jsp")){
                view.loadUrl("javascript:document.getElementById('password').value = '" + pass + "';document.getElementById('user_id').value='" + name + "';document.getElementByName('login').click(); return false;");
            } else if(url.equals("https://signups.victoria.ac.nz/login.aspx?ReturnUrl=%2findex.aspx")){
                view.loadUrl("javascript:document.getElementByName('ctl00$mainContent$simLogin$Password').value = '" + pass + "';document.getElementByName('ctl00$mainContent$simLogin$UserName').value='" + name + "'document.getElementByName('ctl00$mainContent$simLogin$LoginImageButton').click(); return false;");
            }*/
        }
    }

}
