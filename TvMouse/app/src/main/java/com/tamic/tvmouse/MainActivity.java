package com.tamic.tvmouse;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.SuppressLint;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {


    private TcMouseManager mMouseManager;

    public static ViewGroup contentView;
    private WebView webView;
    private View mLoginStatusView;
    private TextView mLoaddingMessageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LayoutInflater inflater = getLayoutInflater();
        contentView = (ViewGroup) inflater.inflate(R.layout.activity_main, null);
        setContentView(contentView);
        init();
        //初始化
        initMouse();
        showMouse();

    }

    private void init() {

        webView = (WebView) contentView.findViewById(R.id.web);
        mLoginStatusView = this.findViewById(R.id.login_status);
        mLoaddingMessageView = (TextView) this
                .findViewById(R.id.login_status_message);
        Button button = (Button) contentView.findViewById(R.id.btn_onclick);
        button.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this, "onclicked ", 1).show();
                showProgress(true);
                webView.setVisibility(View.VISIBLE);

                webView.loadUrl("https://www.baidu.com/");
                WebSettings settings = webView.getSettings();
                settings.setJavaScriptEnabled(true);
                webView.setWebViewClient(new WebViewClient() {
                    @Override
                    public boolean shouldOverrideUrlLoading(WebView view,
                                                            String url) {
                        // 返回值是true的时候控制去WebView打开，为false调用系统浏览器或第三方浏览器
                        view.loadUrl(url);
                        return true;
                    }

                    @Override
                    public void onPageFinished(WebView view, String url) {

                        super.onPageFinished(view, url);
                    }

                    @Override
                    public void onReceivedError(WebView view, int errorCode,
                                                String description, String failingUrl) {
                        Toast.makeText(MainActivity.this, "加载失败 ",
                                Toast.LENGTH_LONG).show();
                        super.onReceivedError(view, errorCode, description,
                                failingUrl);
                    }

                });

                webView.setWebChromeClient(new WebChromeClient() {
                    @Override
                    public void onProgressChanged(WebView view, int newProgress) {
                        // TODO Auto-generated method stub
                        if (newProgress == 100) {
                            showProgress(false);

                        }
                    }

                });

            }
        });

    }

    @SuppressLint("NewApi")
    private void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(
                    android.R.integer.config_shortAnimTime);

            mLoginStatusView.setVisibility(View.VISIBLE);
            mLoginStatusView.animate().setDuration(shortAnimTime)
                    .alpha(show ? 1 : 0)
                    .setListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            webView.setVisibility(show ? View.VISIBLE
                                    : View.GONE);
                        }
                    });

            webView.setVisibility(View.VISIBLE);
            webView.animate().setDuration(shortAnimTime).alpha(show ? 0 : 1)
                    .setListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            webView.setVisibility(show ? View.GONE
                                    : View.VISIBLE);
                        }
                    });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            mLoginStatusView.setVisibility(show ? View.VISIBLE : View.GONE);
            webView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }

    private void showMouse() {

        mMouseManager.showMouseView();

    }

    public void initMouse() {
        initMouseMrg();
    }

    public void initMouseMrg() {

        mMouseManager = new TcMouseManager();
        mMouseManager.init(contentView, TcMouseManager.MOUSE_TYPE);
        mMouseManager.setShowMouse(true);

    }

}
