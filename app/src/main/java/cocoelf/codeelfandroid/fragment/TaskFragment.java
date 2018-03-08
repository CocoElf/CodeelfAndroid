package cocoelf.codeelfandroid.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.TextView;
import android.widget.Toast;

import com.dou361.dialogui.DialogUIUtils;
import com.dou361.dialogui.listener.DialogUIItemListener;

import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import cocoelf.codeelfandroid.R;
import cocoelf.codeelfandroid.adapter.ExpandablelistAdapter;
import cocoelf.codeelfandroid.view.DailyView;

@EFragment
public class TaskFragment extends Fragment {
    private static final String TAG = "TaskFragment";
    List<String> parents=new ArrayList<>();
    List<String>children=new ArrayList<>();
    ExpandableListView listView;
    String s;
    Bundle taskBundle;
    private Timer mTimer;
    private TimerTask mTimerTask;
    private static int count = 0;
    private Handler mHandler = null;
    private static int delay = 1000; //1s
    private static int period = 1000; //1s
    View childView;
    View parentView;
    DailyView dailyView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_task,container,false);
        taskBundle=getArguments();
        if(taskBundle.getString("s")!=null){
           s=taskBundle.getString("s");
            Log.e("task",s);
        }
        listView = (ExpandableListView) view.findViewById(R.id.task_list);
        initHandler();
        initExpandableListView();
        dailyView=(DailyView)view.findViewById(R.id.cc_chart) ;
        initDailyView();
        return view;
    }


    protected void initDailyView() {
        dailyView.setData(2018, 12, 9, 2);
        dailyView.setData(2018, 11, 9, 1);
        dailyView.setData(2018, 10, 5, 10);
        dailyView.setData(2018, 8, 9, 3);
        dailyView.setData(2018, 4, 20, 2);
        dailyView.setData(2018, 12, 13, 3);
        dailyView.setData(2018, 12, 14, 3);
        dailyView.setData(2018, 2, 15, 4);
    }

    public void initHandler(){
        this.mHandler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                TextView textView=childView.findViewById(R.id.timerView);
                textView.setText(changeTime(count));
                TextView textView2=parentView.findViewById(R.id.code_time);
                textView2.setText(changeTime(count));
            }
        };
    }


    private String  changeTime(long time) {
        String stime = "";


        int hour = (int) (time / (60 * 60));
        int minute = (int) (time % (60 * 60) / (60));
        int second = (int) ((time % (60 * 60)) % 60);
        String shour = "" + hour, sminute = "" + minute, ssecond = "" + second;
        if (hour <= 9) {
            shour = "0" + hour;
        }
        if (minute <= 9) {
            sminute = "0" + minute;
        }
        if (second <= 9) {
            ssecond = "0" + second;
        }
        stime = '\t'+shour + ":" + sminute + ":" + ssecond;
        return stime;
    }

    public void  initExpandableListView(){
        parents.add("编码");parents.add("测试");parents.add("调试");
        children.add("00:00:00");children.add("00:00:00");children.add("00:00:00");
        final ExpandablelistAdapter adapter = new ExpandablelistAdapter(parents,children);
        listView.setAdapter(adapter);

        // 清除默认的 Indicator
        listView.setGroupIndicator(null);

        //  设置分组项的点击监听事件
        listView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
                Log.d(TAG, "onGroupClick: groupPosition:" + groupPosition + ", id:" + id);
                boolean groupExpanded = parent.isGroupExpanded(groupPosition);
                parentView=v;
                return false;
            }
        });

        //  设置子选项点击监听事件
        listView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
                Toast.makeText(getContext(), children.get(groupPosition).toString(), Toast.LENGTH_SHORT).show();
                ExpandablelistAdapter.ChildViewHolder childViewHolder= (ExpandablelistAdapter.ChildViewHolder) v.getTag();
                childView=v;
                begin_click(childViewHolder.begin);
                end_click(childViewHolder.end);
                return true;
            }
        });
    }

    @Click
    public void chooseCodeType(){
        final String[] words = new String[]{"编码", "测试", "调试"};
        DialogUIUtils.showSingleChoose(getActivity(), "单选", 0, words, new DialogUIItemListener() {
            @Override
            public void onItemClick(CharSequence text, int position) {
                parents.add(text.toString());
                children.add("00:00:00");
                ExpandablelistAdapter adapter = new ExpandablelistAdapter(parents,children);
                listView.setAdapter(adapter);
                dailyView.requestLayout();

            }
        }).show();
    }

    @Click(R.id.task_work)
    public void changeToWork(){
        taskBundle.putString("s","return");
        if(getTargetFragment() == null){
            return;
        }else{
            Intent i = new Intent();
            i.putExtras(taskBundle);
            getTargetFragment().onActivityResult(getTargetRequestCode(), Activity.RESULT_OK,i);
        }
        getFragmentManager().popBackStack();
    }



    public void begin_click(Button button){
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startTimer();
            }
        });
    }

    public void end_click(Button button){
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                stopTimer();
            }
        });
    }


    private void startTimer(){
        if (mTimer == null) {
            mTimer = new Timer();
        }

        if (mTimerTask == null) {
            mTimerTask = new TimerTask() {
                @Override
                public void run() {
                    sendMessage();
                    try {
                            Thread.sleep(1000);
                    } catch (InterruptedException e) {
                    }
                    count ++;
                }
            };
        }

        if(mTimer != null && mTimerTask != null )
            mTimer.schedule(mTimerTask, delay, period);

    }

    private void stopTimer(){
        if (mTimer != null) {
            mTimer.cancel();
            mTimer = null;
        }
        if (mTimerTask != null) {
            mTimerTask.cancel();
            mTimerTask = null;
        }
        count = 0;
    }

    public void sendMessage(){
        if (mHandler != null) {
            Message message = Message.obtain(mHandler);
            mHandler.sendMessage(message);
        }
    }

}
