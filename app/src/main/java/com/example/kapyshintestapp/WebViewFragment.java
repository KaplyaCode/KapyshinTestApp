package com.example.kapyshintestapp;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;
import android.widget.Toast;

public class WebViewFragment extends Fragment {

    SharedPreferences sPref;

    private WebView webView;
    final String URL = "currentUrl";
    private WebViewFragment webViewFragment = this;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        sPref = this.getActivity().getSharedPreferences("MyPref", Context.MODE_PRIVATE);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (container == null) {
            return null;
        }

        FrameLayout ll = (FrameLayout) inflater.inflate(R.layout.fragment_web_view, container, false);
        WebView webView = (WebView) ll.findViewById(R.id.web);
        WebViewController(webView);

        return ll;
    }

    private void WebViewController(WebView web) {
        webView = web;
        webView.getSettings().setJavaScriptEnabled(true);
        try {
            webView.loadUrl(sPref.getString(URL, "https://www.google.com"));
        }  catch (Exception e){
            webView.loadUrl("https://www.google.com");
        }
        webView.canGoBack();
        WebViewClient webViewClient = new WebViewClient() {
            @SuppressWarnings("deprecation")
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }

            @TargetApi(Build.VERSION_CODES.N) @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                view.loadUrl(request.getUrl().toString());
                return true;
            }
        };
        webView.setWebViewClient(webViewClient);

        webView.setFocusableInTouchMode(true);
        webView.requestFocus();
        webView.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if( keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_UP) {
                    getFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                    webView.goBack();
                    return true;
                }
                return false;
            }
        });
    }

    @Override
    public void onStop() {
        SharedPreferences.Editor ed = sPref.edit();
        ed.putString(URL, webView.getUrl());
        ed.commit();
        super.onStop();
    }
}