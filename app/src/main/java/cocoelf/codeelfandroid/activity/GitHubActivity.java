package cocoelf.codeelfandroid.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;


import butterknife.BindView;
import cocoelf.codeelfandroid.R;
import cocoelf.codeelfandroid.view.DailyView;

/**
 * Created by Administrator on 2017/2/3.
 * github提交表
 */

public class GitHubActivity extends AppCompatActivity {


    DailyView github;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_github);
        github=(DailyView)findViewById(R.id.cc_chart) ;
        initView();
    }

    protected void initView() {
        github.setData(2018, 12, 9, 2);
        github.setData(2018, 11, 9, 1);
        github.setData(2018, 10, 5, 10);
        github.setData(2018, 8, 9, 3);
        github.setData(2018, 4, 20, 2);
        github.setData(2018, 12, 13, 3);
        github.setData(2018, 12, 14, 3);
        github.setData(2018, 2, 15, 4);
    }
}
