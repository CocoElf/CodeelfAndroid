package cocoelf.codeelfandroid.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.SearchView;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import cocoelf.codeelfandroid.R;
import cocoelf.codeelfandroid.adapter.LoadMoreWrapper;
import cocoelf.codeelfandroid.adapter.SearchResultAdapter;
import cocoelf.codeelfandroid.json.SearchResultModel;
import cocoelf.codeelfandroid.listener.EndlessRecyclerOnScrollListener;

/**
 * Created by shea on 2018/2/27.
 */
@EFragment(R.layout.fragment_search_result)
public class SearchResultFragment extends Fragment {

    private List<SearchResultModel> searchResultModelList = new ArrayList<>();

    @ViewById(R.id.fragment_search_result_list)
    RecyclerView searchResultRecyclerView;

    @AfterViews
    void init(){
        getSearchResults();
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        searchResultRecyclerView.setLayoutManager(linearLayoutManager);
        SearchResultAdapter searchResultAdapter = new SearchResultAdapter(searchResultModelList);
        final LoadMoreWrapper loadMoreWrapper = new LoadMoreWrapper(searchResultAdapter);
        searchResultRecyclerView.setAdapter(loadMoreWrapper);
        //点击跳转到详情页
        searchResultAdapter.setOnItemClickListener(new SearchResultAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                //todo 跳转到详情页
//                Bundle bundle = new Bundle();
//                bundle.putString("url",searchResultModelList.get(position).getUrl());
//                Fragment fragment = new SearchResultItemDetailFragment_();
//                fragment.setArguments(bundle);
//                getFragmentManager().beginTransaction()
//                        .addToBackStack(null)  //将当前fragment加入到返回栈中
//                        .replace(R.id.fragment_container, fragment).commit();
            }
        });
        // 设置加载更多监听
        searchResultRecyclerView.addOnScrollListener(new EndlessRecyclerOnScrollListener() {
            @Override
            public void onLoadMore() {
                loadMoreWrapper.setLoadState(loadMoreWrapper.LOADING);

                if (searchResultModelList.size() < 21) {
                    // 模拟获取网络数据，延时1s
                    new Timer().schedule(new TimerTask() {
                        @Override
                        public void run() {
                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    getSearchResults();
                                    loadMoreWrapper.setLoadState(loadMoreWrapper.LOADING_COMPLETE);
                                }
                            });
                        }
                    }, 1000);
                } else {
                    // 显示加载到底的提示
                    loadMoreWrapper.setLoadState(loadMoreWrapper.LOADING_END);
                }
            }
        });
    }

//    @Background
    void getSearchResults(){
        int begin = searchResultModelList.size();
        for(int i=0+begin;i<20+begin;i++){
            SearchResultModel searchResultModel = new SearchResultModel("Android UI组件----ListView列表控件详解"+i,"url"+i,
                    " 运行效果如下：. 二、ListActivity. （1）如果程序的窗口仅仅只需要显示一个列表，" +
                            "则可以让这个activity直接继承ListActivity来实现，此时已经包含了一个ListView组件， 不用再重新写布局文件了。" +
                            "代码举例如下：. 复制代码. 1 package com.smyhvae. smyh005listview; 2 import android.app.ListActivity; 3 import android.os ..."+i);
            searchResultModelList.add(searchResultModel);
        }
    }

}
