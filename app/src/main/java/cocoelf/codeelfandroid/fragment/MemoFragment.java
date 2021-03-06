package cocoelf.codeelfandroid.fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.github.ybq.android.spinkit.SpinKitView;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;
import org.androidannotations.rest.spring.annotations.RestService;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import cocoelf.codeelfandroid.R;
import cocoelf.codeelfandroid.adapter.LoadMoreWrapper;
import cocoelf.codeelfandroid.adapter.MemoAdapter;
import cocoelf.codeelfandroid.exception.ResponseException;
import cocoelf.codeelfandroid.json.MemoModel;
import cocoelf.codeelfandroid.listener.EndlessRecyclerOnScrollListener;
import cocoelf.codeelfandroid.service.MemoService;
import cocoelf.codeelfandroid.service.SearchService;

import static android.content.Context.MODE_PRIVATE;

@EFragment(R.layout.fragment_memo)
public class MemoFragment extends Fragment {

    private List<MemoModel> memoModelList = new ArrayList<>();

    private static final String TAG = "MemoFragment->";

    private int PAGE_SIZE = 30;

    private int presentPage = 0;

    private String username;

    @ViewById(R.id.fragment_memo_list)
    RecyclerView memoRecyclerView;

    @ViewById(R.id.spin_kit)
    SpinKitView spinKitView;

    @RestService
    MemoService memoService;

    private LoadMoreWrapper loadMoreWrapper;

    private MemoAdapter memoAdapter;


    @AfterViews
    void init(){
        Log.d(TAG, "init: ");
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("user",MODE_PRIVATE);
//        username = sharedPreferences.getString("username","");
        username = "shea";
        getMemos(presentPage,username);
    }

    @UiThread
    void setMemoModelList(List<MemoModel> resultModelList){
//        memoModelList = new ArrayList<>();
        memoModelList = resultModelList;
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        memoRecyclerView.setLayoutManager(linearLayoutManager);
//        Log.d(TAG, "setMemoModelList: "+memoModelList.size());
        memoAdapter = new MemoAdapter(memoModelList);
        loadMoreWrapper = new LoadMoreWrapper(memoAdapter);
        memoRecyclerView.setAdapter(loadMoreWrapper);
        spinKitView.setVisibility(View.GONE);
        //点击跳转到详情页
        memoAdapter.setOnItemClickListener(new MemoAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Log.i(TAG, "onItemClick: ");
                //处理事件
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                Fragment fragment = new MemoItemDetailFragment_();
                transaction.replace(R.id.fragment_container,fragment);
                Bundle bundle = new Bundle();
                bundle.putInt("memoId",memoModelList.get(position).getMemoId());
                fragment.setArguments(bundle);
                transaction.addToBackStack(null);
                transaction.commit();


            }
        });
        // 设置加载更多监听
//        memoRecyclerView.addOnScrollListener(new EndlessRecyclerOnScrollListener() {
//            @Override
//            public void onLoadMore() {
//                if(loadMoreWrapper.getLoadState()!=loadMoreWrapper.LOADING_END){
//                    loadMoreWrapper.setLoadState(loadMoreWrapper.LOADING);
//                    presentPage ++;
//                    getMemos(presentPage,username);
//                    loadMoreWrapper.setLoadState(loadMoreWrapper.LOADING_COMPLETE);
//                }
//            }
//        });
    }

    @Background
    void getMemos(int pageNum,String username){
        try {
            List<MemoModel> resultModelList = memoService.getMemoList(username,pageNum+"",PAGE_SIZE+"") ;
            Log.d(TAG, "getMemos: "+resultModelList.size());
            setMemoModelList(resultModelList);
        }catch (ResponseException e){
            makeToast(e.getMessage());
        }catch (Exception e){
            makeToast("请检查网络连接");
        }
    }


    @UiThread
    void makeToast(String message){
        Toast.makeText(getActivity(),message,Toast.LENGTH_SHORT).show();
    }



}
