package cocoelf.codeelfandroid.fragment;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;

import cocoelf.codeelfandroid.R;
@EFragment(R.layout.fragment_search)
public class SearchFragment extends Fragment {
    @Click(R.id.sou)
    void ss(){
        getFragmentManager().beginTransaction()
                .addToBackStack(null)  //将当前fragment加入到返回栈中
                .replace(R.id.fragment_container, new SearchResultFragment_()).commit();
    }
}
