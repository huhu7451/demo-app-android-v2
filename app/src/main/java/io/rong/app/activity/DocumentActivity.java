package io.rong.app.activity;

import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import io.rong.app.R;


/**
 * Created by Administrator on 2015/3/20.
 */
public class DocumentActivity extends BaseActionBarActivity{

    private WebView mWebView = null;

    @Override
    protected int setContentViewResId() {
        return R.layout.de_ac_document;
    }

    @Override
    protected void initView() {

        getSupportActionBar().setTitle(R.string.dv_document);

        mWebView = (WebView) findViewById(R.id.document_webview);
        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.getSettings().setUseWideViewPort(true);
        mWebView.getSettings().setLoadWithOverviewMode(true);
        mWebView.getSettings().setBuiltInZoomControls(true);
        mWebView.getSettings().setSupportZoom(true);

        MyWebViewClient mMyWebViewClient = new MyWebViewClient();
        mMyWebViewClient.onPageFinished(mWebView, "http://docs.rongcloud.cn/api/android/imkit/index.html");
        mMyWebViewClient.shouldOverrideUrlLoading(mWebView, "http://docs.rongcloud.cn/api/android/imkit/index.html");
        mMyWebViewClient.onPageFinished(mWebView, "http://docs.rongcloud.cn/api/android/imkit/index.html");
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
                progressDialog = new ProgressDialog(DocumentActivity.this);
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
