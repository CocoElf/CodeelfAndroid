package cocoelf.codeelfandroid.service;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.os.SystemClock;
import android.util.Log;
import android.widget.Toast;

import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.EService;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.rest.spring.annotations.RestService;

import java.util.Date;

import cocoelf.codeelfandroid.fragment.ClockFragment;
import cocoelf.codeelfandroid.receiver.AlarmReceiver;
import cocoelf.codeelfandroid.util.TimingType;

/**
 * APP计时的后台程序，可暂停继续
 * Created by green-cherry on 2018/3/2.
 */
@EService
public class TimerService extends Service {
    public static final String CLOCK_SERVICE_ACTION = "clock_service_action";
    public static final String REST_SERVICE_ACTION="rest_service_action";
    private boolean controllOpt = true;
    String username = "123";
    long time = 0;

    @RestService
    TimingService timingService;

    @Override
    public void onCreate() {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(CLOCK_SERVICE_ACTION);
        //在service中注册广播（serviceController）,接受来自ClockFragment中的广播信息，实现对计时服务的控制（暂停、继续）
        super.registerReceiver(serviceController, intentFilter);

        IntentFilter intentFilter2 = new IntentFilter();
        intentFilter2.addAction(REST_SERVICE_ACTION);
        //在service中注册广播（serviceController）,接受来自TimerActivity中的广播信息，实现对计时服务的控制（继续）
        super.registerReceiver(continueWork, intentFilter2);
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        initAlarm();
        //获取用户名
        SharedPreferences sharedPreferences = getSharedPreferences("user", MODE_PRIVATE);
        username = sharedPreferences.getString("username", "");
        countTime();
        sendTimingInfo(TimingType.START, new Date(), username);
        return Service.START_STICKY;
    }

    //实现计时功能，每隔一秒发送广播
    public void countTime() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(ClockFragment.CLOCK_ACTION);

                while (controllOpt) {
                    try {
                        Thread.sleep(1000);
                        Bundle bundle = new Bundle();
                        time++;

                        bundle.putLong("time", time);
                        intent.putExtras(bundle);
                        sendBroadcast(intent);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }

    //实现提醒休息
    public void initAlarm() {
        AlarmManager manager = (AlarmManager) getSystemService(ALARM_SERVICE);
        //此处是设置每隔两个小时启动一次
        //这是两个小时的毫秒数
        int Minutes = 120*60*1000;

//        int Minutes = 10 * 1000;
        //SystemClock.elapsedRealtime()表示1970年1月1日0点至今所经历的时间
        long triggerAtTime = SystemClock.elapsedRealtime() + Minutes;
        //此处设置开启AlarmReceiver这个Service
        Intent i = new Intent(this, AlarmReceiver.class);
        PendingIntent pi = PendingIntent.getBroadcast(this, 0, i, 0);
        //ELAPSED_REALTIME_WAKEUP表示让定时任务的出发时间从系统开机算起，并且会唤醒CPU。
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Log.i("alarm", "setWindow");
            manager.setExact(AlarmManager.ELAPSED_REALTIME_WAKEUP, triggerAtTime, pi);
        } else {
            Log.i("alarm", "setRepeating");
             manager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, triggerAtTime, pi);
        }

    }

    //广播接受者，接受来自ClockActivity的广播以便暂停、继续、停止广播
    private BroadcastReceiver serviceController = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String method = intent.getStringExtra("method");
            switch (method) {
                case "pause":
                    controllOpt = false;
                    sendTimingInfo(TimingType.PAUSE_START, new Date(), username);
                    break;
                case "continue":
                    controllOpt = true;
                    countTime();
                    sendTimingInfo(TimingType.PAUSE_END, new Date(), username);
                    break;
                case "stop":
                    controllOpt = false;
                    stopSelf();
                    sendTimingInfo(TimingType.END, new Date(), username);
                    break;
            }
        }
    };


    //广播接受者，接受来自TimeActivity的广播以便继续广播
    private BroadcastReceiver continueWork = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String method = intent.getStringExtra("method");
            switch (method) {
                case "pause":
                    controllOpt = false;
                    sendTimingInfo(TimingType.PAUSE_START, new Date(), username);
                    break;
                case "continue":
                    controllOpt = true;
                    countTime();
                    sendTimingInfo(TimingType.PAUSE_END, new Date(), username);
                    break;
            }
        }
    };

    @Background
    public void sendTimingInfo(TimingType timingType, Date date, String username) {
        try {
            switch (timingType) {
                case START:
                    timingService.startAppTiming(timingType.toString(), String.valueOf((new Date()).getTime()), username);
                    break;
                case END:
                    timingService.endAppTiming(timingType.toString(), String.valueOf((new Date()).getTime()), username);
                    break;
                case PAUSE_START:
                    timingService.pause(timingType.toString(), String.valueOf((new Date()).getTime()), username);
                    break;
                case PAUSE_END:
                    timingService.endPause(timingType.toString(), String.valueOf((new Date()).getTime()), username);
                    break;
            }
        } catch (Exception e) {
            Log.e("传输APP计时错误",e.getMessage());
//            showError("网络连接错误！");
        }
    }

    @UiThread
    public void showError(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public IBinder onBind(Intent intent) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onDestroy() {

        super.unregisterReceiver(serviceController);
        super.unregisterReceiver(continueWork);
        //在Service结束后关闭AlarmManager
        AlarmManager manager = (AlarmManager) getSystemService(ALARM_SERVICE);
        Intent i = new Intent(this, AlarmReceiver.class);
        PendingIntent pi = PendingIntent.getBroadcast(this, 0, i, 0);
        manager.cancel(pi);
    }

}
