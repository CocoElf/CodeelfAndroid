package cocoelf.codeelfandroid.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;

import cocoelf.codeelfandroid.R;

/**
 * A simple {@link Fragment} subclass.
 */
@EFragment(R.layout.fragment_detail)
public class DetailFragment extends Fragment {



    @Click(R.id.back)
    void back(){
        getFragmentManager().popBackStack();
    }

    @Click(R.id.more)
    void more(){

        getFragmentManager().beginTransaction()
                .addToBackStack(null)  //将当前fragment加入到返回栈中
                .replace(R.id.fragment_container, new DetailDetailFragment_()).commit();
    }



}
