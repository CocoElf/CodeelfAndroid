package cocoelf.codeelfandroid.fragment;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.nfc.Tag;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.thinkcool.circletextimageview.CircleTextImageView;

import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;

import cocoelf.codeelfandroid.R;
import cocoelf.codeelfandroid.service.TimerService;

@EFragment
public class ClockFragment extends Fragment {
    public static final String TAG="ClockFragment";

    public static final String CLOCK_ACTION = "cocoelf.codeelfandroid.service.TimerService";
    private long time = 0;

    private Bundle taskBundle=new Bundle();
    private CircleTextImageView circleTextImageView;
    String nowtime="00:00:00";
    boolean isStop=false;
    public ClockFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        regReceiver();//注册广播

    }

        @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
            Log.e(TAG,"createView");
        // Inflate the layout for this fragment
        return  inflater.inflate(R.layout.fragment_clock, container, false);
    }

    @Override
    public void onActivityCreated(Bundle bundle) {
        super.onActivityCreated(bundle);
        circleTextImageView=getActivity().findViewById(R.id.show_time);
        circleTextImageView.setText(nowtime);
        if(isStop){
            getActivity().findViewById(R.id.play).setVisibility(View.VISIBLE);
            getActivity().findViewById(R.id.pause).setVisibility(View.GONE);
        }else {
            getActivity().findViewById(R.id.play).setVisibility(View.GONE);
            getActivity().findViewById(R.id.pause).setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        getActivity().unregisterReceiver(clockReceiver);
    }

    public void regReceiver() {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(CLOCK_ACTION);
        getActivity().registerReceiver(clockReceiver, intentFilter);
    }

    /**
     * 广播接受者，接受来自ClockService（计时服务）的广播，ClockService每隔一秒
     * 钟发一次广播
     */

    private BroadcastReceiver clockReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            time=intent.getLongExtra("time",time);
            changeTime(time);//改变TextView中的显示时间
        }
    };

    //通过发送广播，控制计时服务
    //继续计时
    @Click(R.id.play)
    public void restart(View view){
        getActivity().findViewById(R.id.play).setVisibility(View.GONE);
        getActivity().findViewById(R.id.pause).setVisibility(View.VISIBLE);
        Intent intent=new Intent();
        intent.setAction(TimerService.CLOCK_SERVICE_ACTION);
        intent.putExtra("method", "continue");
        getActivity().sendBroadcast(intent);
        isStop=false;
    }

    //通过发送广播，控制计时服务
    //暂停计时
    @Click(R.id.pause)
    public void pause(View view){
        getActivity().findViewById(R.id.pause).setVisibility(View.GONE);
        getActivity().findViewById(R.id.play).setVisibility(View.VISIBLE);
        Intent intent=new Intent();
        intent.setAction(TimerService.CLOCK_SERVICE_ACTION);
        intent.putExtra("method","pause");
        getActivity().sendBroadcast(intent);
        isStop=true;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode != Activity.RESULT_OK){
            return;
        }else{
             taskBundle = data.getExtras();
            String s=taskBundle.getString("s");
            Log.e("work",s);
        }
    }

    //转到任务页面
    @Click(R.id.work_task)
    public void changeToTask(){
        Fragment fragment=new TaskFragment_();
        fragment.setArguments(taskBundle);
        fragment.setTargetFragment(ClockFragment.this, 0);
        getFragmentManager().beginTransaction()
                .addToBackStack(null)  //将当前fragment加入到返回栈中
                .replace(R.id.fragment_container, fragment).commit();
    }


    private void changeTime(long time) {
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
        stime = shour + ":" + sminute + ":" + ssecond;
        circleTextImageView.setText(stime);
        nowtime=stime;
    }


}
