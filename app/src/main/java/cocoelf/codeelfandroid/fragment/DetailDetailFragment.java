package cocoelf.codeelfandroid.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;

import cocoelf.codeelfandroid.R;

/**
 * A simple {@link Fragment} subclass.
 */
@EFragment(R.layout.fragment_detail_detail)
public class DetailDetailFragment extends Fragment {



    @Click(R.id.back2)
    void back(){
        getFragmentManager().popBackStack();
    }




}
