package cocoelf.codeelfandroid.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.LinearLayout;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.github.ybq.android.spinkit.SpinKitView;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;
import org.androidannotations.rest.spring.annotations.RestService;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import cocoelf.codeelfandroid.R;
import cocoelf.codeelfandroid.adapter.LoadMoreWrapper;
import cocoelf.codeelfandroid.adapter.SearchResultAdapter;
import cocoelf.codeelfandroid.exception.ResponseException;
import cocoelf.codeelfandroid.json.SearchResultModel;
import cocoelf.codeelfandroid.listener.EndlessRecyclerOnScrollListener;
import cocoelf.codeelfandroid.service.SearchService;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by shea on 2018/2/27.
 */
@EFragment(R.layout.fragment_search_result)
public class SearchResultFragment extends Fragment {

    private List<SearchResultModel> searchResultModelList;

    private static final String TAG = "SearchResultFragment->";

    @ViewById(R.id.fragment_search_result_list)
    RecyclerView searchResultRecyclerView;

    @ViewById(R.id.fragment_search_result_search)
    cocoelf.codeelfandroid.component.SearchView searchView;

    @ViewById(R.id.fragment_search_result_root_layout)
    LinearLayout linearLayout;

    @ViewById(R.id.spin_kit)
    SpinKitView spinKitView;

    @RestService
    SearchService searchService;

    @AfterViews
    void init(){
        Log.d(TAG, "init: ");
        final Bundle bundle = getArguments();
//        indicator.stop();
        if(bundle!=null&&bundle.containsKey("keyword")){
            String keyword = bundle.getString("keyword");
            SharedPreferences sharedPreferences = getActivity().getSharedPreferences("user",MODE_PRIVATE);
            String username = sharedPreferences.getString("username","");
            getSearchResults(keyword,"shea");
            searchView.setText(keyword);
        }else {
            setSearchResultModelList(new ArrayList<SearchResultModel>());
        }
    }

    @UiThread
    void setSearchResultModelList(List<SearchResultModel> resultModelList){
        searchResultModelList = new ArrayList<>();
        searchResultModelList.addAll(resultModelList);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        searchResultRecyclerView.setLayoutManager(linearLayoutManager);
        Log.d(TAG, "setSearchResultModelList: "+searchResultModelList.size());
        final SearchResultAdapter searchResultAdapter = new SearchResultAdapter(searchResultModelList);
        final LoadMoreWrapper loadMoreWrapper = new LoadMoreWrapper(searchResultAdapter);
        searchResultRecyclerView.setAdapter(loadMoreWrapper);
        spinKitView.setVisibility(View.GONE);
        //点击跳转到详情页
        searchResultAdapter.setOnItemClickListener(new SearchResultAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                //todo 跳转到详情页
                Bundle bundle = new Bundle();
                bundle.putString("url",searchResultModelList.get(position).getUrl());
                Fragment fragment = new SearchResultItemDetailFragment_();
                fragment.setArguments(bundle);
                getFragmentManager().beginTransaction()
                        .addToBackStack(null)  //将当前fragment加入到返回栈中
                        .replace(R.id.fragment_container, fragment).commit();
            }
        });
//        // 设置加载更多监听
//        searchResultRecyclerView.addOnScrollListener(new EndlessRecyclerOnScrollListener() {
//            @Override
//            public void onLoadMore() {
//                loadMoreWrapper.setLoadState(loadMoreWrapper.LOADING);
//
//                if (searchResultModelList.size() < 21) {
//                    // 模拟获取网络数据，延时1s
//                    new Timer().schedule(new TimerTask() {
//                        @Override
//                        public void run() {
////                            getActivity().runOnUiThread(new Runnable() {
////                                @Override
////                                public void run() {
////                                    getSearchResults();
////                                    loadMoreWrapper.setLoadState(loadMoreWrapper.LOADING_COMPLETE);
////                                }
////                            });
//                        }
//                    }, 1000);
//                } else {
//                    // 显示加载到底的提示
//                    loadMoreWrapper.setLoadState(loadMoreWrapper.LOADING_END);
//                }
//            }
//        });
        searchView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int actionId, KeyEvent event) {
                //当actionId == XX_SEND 或者 XX_DONE时都触发
                //或者event.getKeyCode == ENTER 且 event.getAction == ACTION_DOWN时也触发
                //注意，这是一定要判断event != null。因为在某些输入法上会返回null。
                if (actionId == EditorInfo.IME_ACTION_SEND
                        || actionId == EditorInfo.IME_ACTION_DONE
                        || (event != null && KeyEvent.KEYCODE_ENTER == event.getKeyCode() && KeyEvent.ACTION_DOWN == event.getAction())) {
//                    //处理事件
                    FragmentTransaction transaction = getFragmentManager().beginTransaction();
                    Fragment fragment = new SearchResultFragment_();
                    transaction.replace(R.id.fragment_container,fragment);
                    String keyword = textView.getText().toString();
                    Bundle newBundle = new Bundle();
                    newBundle.putString("keyword",keyword);
                    fragment.setArguments(newBundle);
                    Log.d("enterinput-------",textView.getText().toString());
                    transaction.addToBackStack(null);
                    transaction.commit();
                }
                return false;
            }
        });
    }

    @Background
    void getSearchResults(String keyword,String username){

        try {
            List<SearchResultModel> resultModelList = searchService.queryWithWord(keyword,username);
            Log.d(TAG, "getSearchResults: "+resultModelList.size());
            setSearchResultModelList(resultModelList);
        }catch (ResponseException e){
            Toast.makeText(getActivity(),e.getMessage(),Toast.LENGTH_SHORT).show();
        }catch (Exception e){
            Toast.makeText(getActivity(),"请检查网络连接",Toast.LENGTH_SHORT).show();
        }
    }

}
