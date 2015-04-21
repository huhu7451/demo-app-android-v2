package io.rong.app.activity;


import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import io.rong.app.R;


/**
 * Created by Administrator on 2015/3/19.
 */
public class UpdateLogActivity extends BaseActionBarActivity {

    private WebView mWebView = null;


    @Override
    protected int setContentViewResId() {
        return R.layout.de_ac_update_log;
    }

    @Override
    protected void initView() {
        getSupportActionBar().setTitle(R.string.update_log);
        mWebView = (WebView) findViewById(R.id.update_log_webview);
        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.getSettings().setUseWideViewPort(true);
        mWebView.getSettings().setLoadWithOverviewMode(true);

        mWebView.getSettings().setBuiltInZoomControls(true);
        mWebView.getSettings().setSupportZoom(true);

        MyWebViewClient mMyWebViewClient = new MyWebViewClient();
        mMyWebViewClient.onPageFinished(mWebView, "http://rongcloud.cn/downloads/history/Android");
        mMyWebViewClient.shouldOverrideUrlLoading(mWebView, "http://rongcloud.cn/downloads/history/Android");
        mMyWebViewClient.onPageFinished(mWebView, "http://rongcloud.cn/downloads/history/Android");
        mWebView.setWebViewClient(mMyWebViewClient);
    }


    @Override
    protected void initData() {

    }

    class MyWebViewClient extends WebViewClient {

        ProgressDialog progressDialog;

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {//网页页面开始加载的时候
            if (progressDialog == null) {
                progressDialog = new ProgressDialog(UpdateLogActivity.this);
                progressDialog.setMessage("Please wait...");
                progressDialog.show();
                mWebView.setEnabled(false);// 当加载网页的时候将网页进行隐藏
            }
            super.onPageStarted(view, url, favicon);
        }

        @Override
        public void onPageFinished(WebView view, String url) {//网页加载结束的时候
            //super.onPageFinished(view, url);
            if (progressDialog != null && progressDialog.isShowing()) {
                progressDialog.dismiss();
                progressDialog = null;
                mWebView.setEnabled(true);
            }
        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) { //网页加载时的连接的网址
            view.loadUrl(url);
            return false;
        }
    }

}
