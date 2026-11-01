package com.sakura.strike;

import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.webkit.WebChromeClient;
import android.webkit.ConsoleMessage;
import android.webkit.JsResult;
import android.webkit.JsPromptResult;
import android.content.DialogInterface;
import android.app.AlertDialog;
import android.view.Window;
import android.graphics.Color;
import android.util.Log;
import android.webkit.PermissionRequest;

public class MainActivity extends Activity {

    private WebView webView;

    private static final String DIAG_JS =
            "(function(){try{var r=function(id){var e=document.getElementById(id);" +
            "if(!e)return 'null';var b=e.getBoundingClientRect();var cs=getComputedStyle(e);" +
            "return {x:Math.round(b.x),y:Math.round(b.y),w:Math.round(b.width),h:Math.round(b.height)," +
            "disp:cs.display,vis:cs.visibility,op:cs.opacity,tf:cs.transform,pos:cs.position};};" +
            "return JSON.stringify({iw:innerWidth,ih:innerHeight,dpr:devicePixelRatio," +
            "vw:visualViewport?Math.round(visualViewport.width):-1," +
            "vl:visualViewport?Math.round(visualViewport.offsetLeft):-1," +
            "vs:visualViewport?Math.round(visualViewport.scale*100)/100:-1," +
            "sx:scrollX,sy:scrollY,de:document.documentElement.clientWidth," +
            "dh:document.documentElement.clientHeight,db:document.body.clientWidth," +
            "app:r('app'),title:r('screen-title'),lobby:r('screen-lobby')," +
            "play:r('btn-play'),petals:r('petals')," +
            "active:(document.querySelector('.screen.active')||{}).id});}catch(e){return 'ERR '+e;}})()";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // fullscreen immersive
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        webView = new WebView(this);
        webView.setBackgroundColor(Color.parseColor("#FFF1F7"));

        WebSettings s = webView.getSettings();
        s.setJavaScriptEnabled(true);
        s.setDomStorageEnabled(true);
        s.setDatabaseEnabled(true);
        s.setLoadWithOverviewMode(true);
        s.setUseWideViewPort(false);
        s.setSupportZoom(false);
        s.setBuiltInZoomControls(false);
        s.setMediaPlaybackRequiresUserGesture(false);
        s.setAllowFileAccess(true);
        s.setCacheMode(WebSettings.LOAD_NO_CACHE);

        webView.setWebViewClient(new WebViewClient());
        webView.setWebChromeClient(new WebChromeClient() {
            @Override
            public boolean onConsoleMessage(ConsoleMessage m) {
                Log.d("SakuraStrike", m.message() + " @" + m.lineNumber());
                return true;
            }
            @Override
            public boolean onJsAlert(WebView v, String url, String msg, JsResult r) {
                new AlertDialog.Builder(MainActivity.this)
                        .setMessage(msg).setPositiveButton("OK", (d, w) -> r.confirm()).show();
                return true;
            }
            @Override
            public boolean onJsConfirm(WebView v, String url, String msg, JsResult r) {
                new AlertDialog.Builder(MainActivity.this)
                        .setMessage(msg)
                        .setPositiveButton("OK", (d, w) -> r.confirm())
                        .setNegativeButton("Cancel", (d, w) -> r.cancel())
                        .setOnCancelListener(d -> r.cancel())
                        .show();
                return true;
            }
            @Override
            public void onPermissionRequest(PermissionRequest request) {
                request.deny();
            }
        });

        setContentView(webView);

        final boolean tour = getIntent() != null && getIntent().getBooleanExtra("tour", false);

        // ---- diagnostics: layout ki asli values log me ----
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView v, String url) {
                v.postDelayed(() -> v.evaluateJavascript(DIAG_JS, value ->
                        Log.i("SakuraStrike", "DIAG " + value)), 1200);
            }
        });
        if (tour) {
            webView.setWebViewClient(new WebViewClient() {
                @Override
                public void onPageFinished(WebView v, String url) {
                    v.postDelayed(() -> v.evaluateJavascript(
                            "window.startTour && window.startTour()", null), 600);
                }
            });
        }

        webView.loadUrl("file:///android_asset/index.html");
        hideBars();
    }

    private void hideBars() {
        View decor = getWindow().getDecorView();
        decor.setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus) hideBars();
    }

    @Override
    public void onBackPressed() {
        // keep the player inside the game; back = pause instead of exit
        webView.evaluateJavascript(
                "(function(){var b=document.getElementById('btn-pause');if(b){b.click();return 'ok';}return 'no';})()",
                null);
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (webView != null) webView.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (webView != null) webView.onResume();
        hideBars();
    }

    @Override
    protected void onDestroy() {
        if (webView != null) {
            webView.loadUrl("about:blank");
            webView.destroy();
            webView = null;
        }
        super.onDestroy();
    }
}
