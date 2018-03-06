package cocoelf.codeelfandroid.activity;

import android.content.Intent;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.androidannotations.annotations.EActivity;

import cocoelf.codeelfandroid.R;
import cocoelf.codeelfandroid.service.TimerService;

@EActivity()
public class TimerActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        Intent intent=new  Intent();
        intent.putExtra("method","pause");
        intent.setAction(TimerService.REST_SERVICE_ACTION);
        sendBroadcast(intent);

        setContentView(R.layout.activity_timer);
        TextView tv = (TextView)findViewById(R.id.rest);
        Typeface typeface = Typeface.createFromAsset(this.getAssets(), "fonts/tea.ttf");  // mContext为上下文
        tv.setTypeface(typeface );
        initButton(typeface);
    }

    private void initButton(Typeface typeface){
        Button button=(Button)findViewById(R.id.rest_back);
        button.setTypeface(typeface);
        button.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v) {
                Intent intent=new Intent();
                intent.setAction(TimerService.REST_SERVICE_ACTION);
                intent.putExtra("method","continue");
                sendBroadcast(intent);

                Intent in = new Intent();
                setResult( RESULT_OK, in );
                finish();
            }
        });

    }

}
