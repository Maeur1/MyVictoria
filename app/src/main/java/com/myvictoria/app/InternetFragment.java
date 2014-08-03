package com.myvictoria.app;

import android.app.DownloadManager;
import android.app.Fragment;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

public class InternetFragment extends Fragment{

    private String cookie;

    private static String ARG_URL = "https://my.vuw.ac.nz/cp/home/displaylogin";
    public WebView internet;
    private ProgressBar prog;
    public String name, pass;

    public boolean close(){
        String currentUrl = internet.getUrl();
        if(currentUrl.equals("http://my.vuw.ac.nz/render.userLayoutRootNode.uP?uP_root=root") || currentUrl.equals("https://blackboard.vuw.ac.nz/webapps/portal/execute/tabs/tabAction?tab_tab_group_id=_1_1") || currentUrl.equals("https://signups.victoria.ac.nz/index.aspx")){
            return true;
        } else {
            if (internet.getUrl().contains("http://my.vuw.ac.nz/tag.87d85b278372bb7c.render.userLayoutRootNode.uP?uP_root=root")){
                internet.loadUrl("http://my.vuw.ac.nz/tag.87d85b278372bb7c.render.userLayoutRootNode.uP?uP_root=root&uP_sparam=activeTab&activeTab=u11l1s8&uP_tparam=frm&frm=");
            } else {
                internet.goBack();
            }
            return false;
        }
    }

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
        prog = (ProgressBar) view.findViewById(R.id.progressBar);
        internet = (WebView) view.findViewById(R.id.webview);
        internet.getSettings().setLoadsImagesAutomatically(true);
        internet.getSettings().setJavaScriptEnabled(true);
        internet.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
        internet.setWebViewClient(new MyBrowser());
        internet.getSettings().setUseWideViewPort(true);
        internet.getSettings().setLoadWithOverviewMode(true);
        internet.getSettings().setBuiltInZoomControls(true);
        internet.setInitialScale(100);
        internet.setWebChromeClient(new MyChrome());
        internet.loadUrl(ARG_URL);
        return view;
    }

    private class MyChrome extends WebChromeClient {
        @Override
        public void onProgressChanged(WebView view, int newProgress) {
            prog.setVisibility(View.VISIBLE);
            prog.setProgress(newProgress);
            if (newProgress == 100) {
                prog.setProgress(0);
            }
            super.onProgressChanged(view, newProgress);
        }

        @Override
        public boolean onJsAlert(WebView view, String url, String message, JsResult result) {
            Log.d("Java Alert", message);
            return super.onJsAlert(view, url, message, result);
        }
    }

    private class MyBrowser extends WebViewClient {

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            if(url.endsWith(".pdf")) {
                DownloadManager.Request request = new DownloadManager.Request(Uri.parse(url));
                request.allowScanningByMediaScanner();
                request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
                request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, url.substring(url.lastIndexOf('/')+1, url.length()));
                DownloadManager manager = (DownloadManager) getActivity().getSystemService(Context.DOWNLOAD_SERVICE);
                request.addRequestHeader("Cookie", cookie);
                request.addRequestHeader("User-Agent", view.getSettings().getUserAgentString());
                manager.enqueue(request);
            } else {
                view.loadUrl(url);
            }
            return true;
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            cookie = android.webkit.CookieManager.getInstance().getCookie(url);
            if(url.equals("https://my.vuw.ac.nz/cp/home/displaylogin")) {
                view.loadUrl("javascript:document.getElementById('pass').value = '"
                        + pass
                        + "';document.getElementById('user').value= '"
                        + name
                        + "';login();");
            } else if(url.equals("https://blackboard.vuw.ac.nz/webapps/portal/execute/tabs/tabAction?tab_tab_group_id=_1_1")){
                view.loadUrl("javascript:document.getElementById('password').value = '"
                        + pass
                        + "';document.getElementById('user_id').value = '"
                        + name
                        + "';document.getElementsByClassName('submit button-1')[0].click();");
            } else if(url.contains("https://signups.victoria.ac.nz/login.aspx")){
                view.loadUrl("javascript:document.getElementById('ctl00_mainContent_simLogin_Password').value = '"
                        + pass
                        + "';document.getElementById('ctl00_mainContent_simLogin_UserName').value = '"
                        + name
                        + "';document.getElementById('ctl00_mainContent_simLogin_LoginImageButton').click();");
            } else if(url.equals("https://library.victoria.ac.nz/roombooking/edit_entry.php")){
                view.loadUrl("javascript:if(document.getElementById('NewUserName')!=null){document.getElementsByName('NewUserPassword')[0].value = '"
                        + pass
                        + "';document.getElementsByName('NewUserName')[0].value = '"
                        + name
                        + "';document.getElementsByClassName('submit')[0].click();" +
                        "}");
            }
        }
    }

}
