package cocoelf.codeelfandroid.receiver;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import cocoelf.codeelfandroid.R;
import cocoelf.codeelfandroid.activity.TimerActivity_;
import cocoelf.codeelfandroid.service.TimerService;

/**
 * Created by green-cherry on 2018/3/3.
 */


public class AlarmReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        Toast.makeText(context,"编码时间过长，休息休息一下。。。",Toast.LENGTH_LONG).show();
        Intent intent2=new  Intent();
        intent2.setClass(context, TimerActivity_.class);
        context.getApplicationContext().startActivity(intent2);
    }
}