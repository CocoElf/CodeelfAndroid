package cocoelf.codeelfandroid.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.webkit.WebView;
import android.webkit.WebViewClient;

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

    @AfterViews
    void init(){
        Bundle bundle = getArguments();
        if(bundle!=null&&bundle.containsKey("url")){
            webView.getSettings().setJavaScriptEnabled(true);
            webView.setWebViewClient(new WebViewClient());
            webView.loadUrl(bundle.getString("url"));
        }
    }
}
