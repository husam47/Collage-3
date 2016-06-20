package com.edu.nikita.collage;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.Display;
import android.view.ViewGroup;
import android.view.Window;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.edu.nikita.collage.Pojo.TokenResponse;
import com.edu.nikita.collage.Retrofit.ApiFactory;
import com.edu.nikita.collage.Retrofit.GetAccessToken;

import java.lang.ref.WeakReference;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * Created by Nikita on 05.06.2016.
 */
public class AuthDialog extends Dialog {

    private static final String TAG = AuthDialog.class.getSimpleName();
    private WebView mWebView;
    private LinearLayout mContent;
    private ProgressDialog mSpinner;
    private java.lang.String mUrl = "";
    private Callback<TokenResponse> callback;
    private ChoseUsernameFragment fragment;
    // "https://api.instagram.com/oauth/authorize/?client_id=" + Constants.CLIENT_ID + "&redirect_uri=" + Constants.REDIRECT_URI + "&response_type=code&display=touch&scope=public_content";
    static final float[] DIMENSIONS_LANDSCAPE = {460, 260};
    static final float[] DIMENSIONS_PORTRAIT = {280, 420};

    public AuthDialog(Context context,ChoseUsernameFragment callback_in) {
        super(context);
        callback = callback_in;
        fragment = callback_in;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mUrl = "https://api.instagram.com/oauth/authorize/?client_id=" + getContext().getString(R.string.Client_id) + "&redirect_uri=" + getContext().getString(R.string.Redirect_uri) + "&response_type=code&scope=public_content";
        mSpinner = new ProgressDialog(getContext());
        mSpinner.requestWindowFeature(Window.FEATURE_NO_TITLE);
        mSpinner.setMessage("Loading...");

        mContent = new LinearLayout(getContext());
        mContent.setOrientation(LinearLayout.VERTICAL);

        requestWindowFeature(Window.FEATURE_NO_TITLE);

        setUpWebView();

        Display display = getWindow().getWindowManager().getDefaultDisplay();
        final float scale = getContext().getResources().getDisplayMetrics().density;
        float[] dimensions = (display.getWidth() < display.getHeight()) ? DIMENSIONS_PORTRAIT
                : DIMENSIONS_LANDSCAPE;

        addContentView(mContent, new FrameLayout.LayoutParams(
                (int) (dimensions[0] * scale + 0.5f), (int) (dimensions[1]
                * scale + 0.5f)));

        /*CookieSyncManager.createInstance(getContext());
        CookieManager cookieManager = CookieManager.getInstance();
        cookieManager.removeAllCookie();*/
    }

    private void setUpWebView() {
        mWebView = new WebView(getContext());
        mWebView.setVerticalScrollBarEnabled(false);
        mWebView.setHorizontalScrollBarEnabled(false);
        mWebView.setWebViewClient(new OAuthWebViewClient());
        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.loadUrl(mUrl);
        mWebView.setLayoutParams(new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        mContent.addView(mWebView);
    }

    private class OAuthWebViewClient extends WebViewClient {



        @Override
        public boolean shouldOverrideUrlLoading(final WebView view, String url) {
            Log.d(TAG, "Redirecting URL " + url);

            if (url.startsWith(view.getContext().getString(R.string.Redirect_uri))) {
                String code = url.substring(getContext().getString(R.string.Redirect_uri).length()+"?code=".length());
                GetAccessToken request = ApiFactory.getAccessToken();
                Call<TokenResponse> tockenResponse = request.getAccessToken(getContext().getString(R.string.Client_id),
                        getContext().getString(R.string.Client_secret),getContext().getString(R.string.Redirect_uri),
                        "authorization_code",code);
                try {
                    tockenResponse.enqueue(callback);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
                return true;
            }
            return false;
        }


        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            Log.d(TAG, "Loading URL: " + url);

            super.onPageStarted(view, url, favicon);
            mSpinner.show();
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            Log.d(TAG, "onPageFinished URL: " + url);
            mSpinner.dismiss();
            //closeDialog();
        }

    }

    public void closeDialog()
    {
        this.dismiss();
    }

    @Override
    public void dismiss() {
        super.dismiss();
        fragment.authDialogDismis();
    }
}
