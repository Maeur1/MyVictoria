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
import android.widget.ProgressBar;
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
    private ProgressBar prog;
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
        prog = (ProgressBar) view.findViewById(R.id.progressBar);
        internet.getSettings().setLoadsImagesAutomatically(true);
        internet.getSettings().setJavaScriptEnabled(true);
        internet.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
        internet.setWebViewClient(new MyBrowser());
        internet.setWebChromeClient(new MyChrome());
        internet.loadUrl(ARG_URL);
        return view;
    }

    private class MyChrome extends WebChromeClient {
        @Override
        public void onProgressChanged(WebView view, int newProgress) {
            super.onProgressChanged(view, newProgress);
            prog.setVisibility(View.VISIBLE);
            prog.setProgress(newProgress);
            if (newProgress == 100) {
                prog.setVisibility(View.GONE);
            }
        }

        @Override
        public boolean onJsAlert(WebView view, String url, String message, JsResult result) {
            Log.d("Java Alert", message);
            return super.onJsAlert(view, url, message, result);
        }
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
                view.loadUrl("javascript:document.getElementById('pass').value = '"
                        + pass
                        + "';document.getElementById('user').value= '"
                        + name
                        + "';login();");
            } else if(url.equals("https://blackboard.vuw.ac.nz/webapps/portal/frameset.jsp")){
                view.loadUrl("javascript:document.getElementById('contentFrame').contentDocument.getElementById('password').value = '"
                        + pass
                        + "';document.getElementById('contentFrame').contentDocument.getElementById('user_id').value = '"
                        + name
                        + "';document.getElementById('contentFrame').contentDocument.getElementsByClassName('submit button-1')[0].click();");
            } else if(url.contains("https://signups.victoria.ac.nz/login.aspx")){
                view.loadUrl("javascript:document.getElementById('ctl00_mainContent_simLogin_Password').value = '"
                        + pass
                        + "';document.getElementById('ctl00_mainContent_simLogin_UserName').value = '"
                        + name
                        + "';document.getElementById('ctl00_mainContent_simLogin_LoginImageButton').click();");
            }
        }
    }

}
