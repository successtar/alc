package com.example.myapplication;

import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.net.http.SslError;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.SslErrorHandler;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

public class MainActivity extends AppCompatActivity {

    String ShowOrHideWebViewInitialUse = "show";

    private WebView webview ;

    private ProgressBar spinner;

    String location = "home";

    String myurl = "https://andela.com/alc/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
    }


    // This allows for a splash screen
    // (and hide elements once the page loads)
    private class CustomWebViewClient extends WebViewClient {

        // Handle SSL issue
        @Override
        public void onReceivedSslError(WebView view, final SslErrorHandler handler, SslError error) {

            final AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);

            builder.setMessage(R.string.notification_error_ssl_cert_invalid);

            builder.setPositiveButton("continue", new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialog, int which) {

                    handler.proceed();
                }
            });

            builder.setNegativeButton("cancel", new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialog, int which) {

                    handler.cancel();
                }
            });

            final AlertDialog dialog = builder.create();

            dialog.show();
        }

        public void onPageStarted(WebView webview, String url, Bitmap favicon) {

            // only make it invisible the FIRST time the app is run
            if (ShowOrHideWebViewInitialUse.equals("show")) {

                webview.setVisibility(webview.INVISIBLE);
            }
        }

        /* Remove Spinner and make webview visible when  page load   completed */

        @Override
        public void onPageFinished(WebView view, String url) {

            ShowOrHideWebViewInitialUse = "hide";

            spinner.setVisibility(View.GONE);

            view.setVisibility(webview.VISIBLE);

            super.onPageFinished(view, url);

        }

        /*  Load error view when webview have issues  with loading page */

        @Override
        public void onReceivedError(WebView view, int errorCode,
                                    String description, String failingUrl) {

            myurl = view.getUrl();

            setContentView(R.layout.error);

            super.onReceivedError(view, errorCode, description, failingUrl);
        }
    }

    // Handle back Button click on  webview and  App Home
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (event.getAction() == KeyEvent.ACTION_DOWN) {

            switch (keyCode) {

                case KeyEvent.KEYCODE_BACK:

                    if (location == "about") {

                        if (webview.canGoBack()) {

                            webview.goBack();

                        }
                        else {

                            location = "home";

                            setContentView(R.layout.activity_main);

                        }
                    }
                    else if (location == "home"){

                        final AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);

                        builder.setMessage(R.string.exit_app);

                        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                finish();
                            }
                        });

                        builder.setNegativeButton("No", new DialogInterface.OnClickListener(){
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                // Do nothing
                            }
                        });

                        final AlertDialog dialog = builder.create();

                        dialog.show();
                    }
                    else {

                        location = "home";

                        setContentView(R.layout.activity_main);

                    }

                    return true;
            }

        }
        return super.onKeyDown(keyCode, event);
    }

    /* Back to App Home */

    public void home(View v){

        location = "home";

        setContentView(R.layout.activity_main);
    }

    /* Load About WebView */

    public void about(View v){

        setContentView(R.layout.about);

        location = "about";

        webview =(WebView)findViewById(R.id.webView);

        spinner = (ProgressBar)findViewById(R.id.progressBar1);

        webview.setWebViewClient(new CustomWebViewClient());

        webview.getSettings().setJavaScriptEnabled(true);

        webview.getSettings().setDomStorageEnabled(true);

        webview.setOverScrollMode(WebView.OVER_SCROLL_NEVER);

        webview.loadUrl(myurl);
    }

    /* Load Profile */

    public void profile(View v){

        location = "profile";

        setContentView(R.layout.profile);
    }
}
