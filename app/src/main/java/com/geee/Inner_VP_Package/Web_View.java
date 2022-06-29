package com.geee.Inner_VP_Package;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.geee.R;


public class Web_View extends Fragment {
    View view;
    Context context;

    ProgressBar progressBar;
    WebView webView;
    String url = "www.google.com";
    String title;
    TextView titleTxt;

    public Web_View() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_web_view, container, false);
        context = getContext();

        Bundle bundle = getArguments();
        if (bundle != null) {
            url = bundle.getString("url");
            title = bundle.getString("title");
        }


        view.findViewById(R.id.iv_back).setOnClickListener(v -> getActivity().onBackPressed());

        titleTxt = view.findViewById(R.id.title_txt);
        titleTxt.setText(title);

        webView = view.findViewById(R.id.webview);
        progressBar = view.findViewById(R.id.progress_bar);
        webView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int progress) {
                if (progress >= 80) {
                    progressBar.setVisibility(View.GONE);
                }
            }
        });


        webView.getSettings().setJavaScriptEnabled(true);

        webView.loadUrl(url);


        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return false;
            }
        });

        return view;
    }
}