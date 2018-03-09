package cocoelf.codeelfandroid.fragment;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.github.ybq.android.spinkit.SpinKitView;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;
import org.androidannotations.rest.spring.annotations.RestService;

import java.util.ArrayList;
import java.util.Date;

import cocoelf.codeelfandroid.R;
import cocoelf.codeelfandroid.exception.ResponseException;
import cocoelf.codeelfandroid.json.MemoModel;
import cocoelf.codeelfandroid.json.SearchResultModel;
import cocoelf.codeelfandroid.service.MemoService;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by shea on 2018/2/27.
 */

@EFragment(R.layout.fragment_search_result_item_detail)
public class SearchResultItemDetailFragment extends Fragment {
    @ViewById(R.id.fragment_search_result_item_detail_content)
    WebView webView;

    @RestService
    MemoService memoService;

    private ProgressDialog mProgressDialog;
    private String username;

    private static final String TAG = "SearchResultItemDetailF";

    @AfterViews
    void init(){
        Bundle bundle = getArguments();
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("user",MODE_PRIVATE);
//            username = sharedPreferences.getString("username","");
        username = "shea";
        if(bundle!=null&&bundle.containsKey("url")){
            mProgressDialog = ProgressDialog.show(getContext(),"",null);
            webView.getSettings().setJavaScriptEnabled(true);
            webView.addJavascriptInterface(new InJavaScriptLocalObj(), "local_obj");
            String url = bundle.getString("url");
            Log.i(TAG, url);
            webView.loadUrl(bundle.getString("url"));
            webView.setWebViewClient(new WebViewClient(){
                @Override
                public void onPageFinished(WebView view, String url) {
                    super.onPageFinished(view, url);
                    view.loadUrl("javascript:window.local_obj.showSource('<head>'+" + "document.getElementsByTagName('html')[0].innerHTML+'</head>');");
                    mProgressDialog.dismiss();
                }
            });
        }
    }

    final class InJavaScriptLocalObj {
        @JavascriptInterface
        public void showSource(String html) {
            Log.i(TAG, "showSource: ");
            mProgressDialog.dismiss();
            Bundle bundle = getArguments();
            String name = bundle.getString("name");
            String url = bundle.getString("url");
            String snippet = bundle.getString("snippet");
            String type = bundle.getString("type");
            ArrayList<String> keywords = bundle.getStringArrayList("keywords");
            Date date = new Date(bundle.getLong("date"));
            MemoModel memoModel = new MemoModel(name,url,snippet,date,keywords,type,null,html);
            addMemo(memoModel);

        }
    }

    @Background
    void addMemo(MemoModel memoModel){
        Log.i(TAG, "addMemo: ");
        try {
            memoService.addMemo(memoModel,username);
        }catch (ResponseException e){
            makeToast(e.getMessage());
        }catch (Exception e){
            makeToast("请检查网络连接");
        }
    }

    @UiThread
    void makeToast(String text){
        Toast.makeText(getActivity(),text,Toast.LENGTH_SHORT).show();
    }
}
