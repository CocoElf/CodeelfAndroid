package cocoelf.codeelfandroid.fragment;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.github.ybq.android.spinkit.SpinKitView;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

import cocoelf.codeelfandroid.R;

/**
 * Created by shea on 2018/2/27.
 */

@EFragment(R.layout.fragment_search_result_item_detail)
public class SearchResultItemDetailFragment extends Fragment {
    @ViewById(R.id.fragment_search_result_item_detail_content)
    WebView webView;



    private ProgressDialog mProgressDialog;

    @AfterViews
    void init(){
        Bundle bundle = getArguments();
        if(bundle!=null&&bundle.containsKey("url")){
            mProgressDialog = ProgressDialog.show(getContext(),"",null);
            webView.getSettings().setJavaScriptEnabled(true);
            webView.loadUrl(bundle.getString("url"));
            webView.setWebViewClient(new WebViewClient(){
                @Override
                public void onPageFinished(WebView view, String url) {
                    super.onPageFinished(view, url);
                    mProgressDialog.dismiss();

                }
            });
        }
    }
}
