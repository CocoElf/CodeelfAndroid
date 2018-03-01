package cocoelf.codeelfandroid.activity;

import android.media.MediaRecorder;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.carlos.voiceline.mylibrary.VoiceLineView;
import com.microsoft.cognitiveservices.speechrecognition.ISpeechRecognitionServerEvents;
import com.microsoft.cognitiveservices.speechrecognition.MicrophoneRecognitionClient;
import com.microsoft.cognitiveservices.speechrecognition.RecognitionResult;
import com.microsoft.cognitiveservices.speechrecognition.RecognitionStatus;
import com.microsoft.cognitiveservices.speechrecognition.SpeechRecognitionMode;
import com.microsoft.cognitiveservices.speechrecognition.SpeechRecognitionServiceFactory;

import cocoelf.codeelfandroid.R;
import cocoelf.codeelfandroid.adapter.ViewPagerAdapter;
import cocoelf.codeelfandroid.fragment.AdviceFragment;
import cocoelf.codeelfandroid.fragment.ClockFragment;
import cocoelf.codeelfandroid.fragment.MemoFragment;
import cocoelf.codeelfandroid.fragment.MemoFragment_;
import cocoelf.codeelfandroid.fragment.SearchFragment;
import cocoelf.codeelfandroid.fragment.SearchFragment_;
import cocoelf.codeelfandroid.fragment.SearchResultFragment_;
import cocoelf.codeelfandroid.fragment.ShareFragment;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener ,ISpeechRecognitionServerEvents {
    MicrophoneRecognitionClient micClient = null;
    private ViewPager viewPager;
    private MenuItem menuItem;
    private BottomNavigationView bottomNavigationView;
    String sPrev="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        initBottomNavigationView();
        initViewpager();
        setupViewPager(viewPager);
        setInitPage();
//        initMicClient();

    }

    private void initBottomNavigationView(){
        bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottom_navigation);
        //默认 >3 的选中效果会影响ViewPager的滑动切换时的效果，故利用反射去掉
        BottomNavigationViewHelper.disableShiftMode(bottomNavigationView);

        bottomNavigationView.setOnNavigationItemSelectedListener(
                new BottomNavigationView.OnNavigationItemSelectedListener() {
                    private int previousPosition = -1;
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        int position = 0;
                        switch (item.getItemId()) {
                            case R.id.item_clock:
                                position = 0;
                                break;
                            case R.id.item_advice:
                                position = 1;
                                break;
                            case R.id.menu_empty:
                                position = 2;
                                break;
                            case R.id.item_memo:
                                position = 3;
                                break;
                            case R.id.item_share:
                                position = 4;
                                break;
                        }
                        popAll();
                        if (previousPosition != position) {
                            viewPager.setCurrentItem(position, false);
                            previousPosition = position;
                        }
                        return true;
                    }
                });
//        bottomNavigationView.setSelectedItemId(bottomNavigationView.getMenu().getItem(2).getItemId());

    }

    private void initViewpager(){
        viewPager = (ViewPager) findViewById(R.id.viewpager);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (menuItem != null) {
                    menuItem.setChecked(false);
                } else {
                    bottomNavigationView.getMenu().getItem(0).setChecked(false);
                }

                menuItem = bottomNavigationView.getMenu().getItem(position);
                menuItem.setChecked(true);

            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
    }

    private void initMicClient() {
        if (this.micClient == null) {
            this.micClient = SpeechRecognitionServiceFactory.createMicrophoneClient(
                    this,
                    SpeechRecognitionMode.LongDictation,
                    this.getString(R.string.localLanguage),
                    this,
                    this.getString(R.string.primaryKey)
            );

            this.micClient.setAuthenticationUri("");
        }
        micClient.startMicAndRecognition();
    }
    /**
     * 弹出所有的fragment
     */
    private void popAll(){
        int num = getSupportFragmentManager().getBackStackEntryCount();
        for (int i = 0; i < num; i++) {
            getSupportFragmentManager().popBackStack();
        }
    }

    /*
     *设置初始默认页面
     */
    private void setInitPage(){
        bottomNavigationView.setSelectedItemId(bottomNavigationView.getMenu().getItem(2).getItemId());
        viewPager.setCurrentItem(2);
    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new ClockFragment());
        adapter.addFragment(new AdviceFragment());
        adapter.addFragment(new SearchFragment_());
        adapter.addFragment(new MemoFragment_());
        adapter.addFragment(new ShareFragment());
        viewPager.setAdapter(adapter);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.photo) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_userinfo) {
            // Handle the camera action
        } else if (id == R.id.nav_clock) {

        } else if (id == R.id.nav_memo) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_exit) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    //以下是语音部分
    @Override
    public void onPartialResponseReceived(String s) {

    }

    @Override
    public void onFinalResponseReceived(RecognitionResult response) {
        boolean isFinalDicationMessage = this.getMode() == SpeechRecognitionMode.LongDictation &&
                (response.RecognitionStatus == RecognitionStatus.EndOfDictation ||
                        response.RecognitionStatus == RecognitionStatus.DictationEndSilenceTimeout);

        int max = 0;

        if(response.Results.length>0){
            String s = response.Results[0].DisplayText;

            if(sPrev.equals("小精灵")){
                sPrev="";
                viewPager.setCurrentItem(2, false);
                search(s);
            } else if (s.equals("小精灵")){
                sPrev=s;
            }
        }

    }

    private void search(String keyword){
        Fragment fragment=new SearchResultFragment_();
        Bundle bundle=new Bundle();
        bundle.putString("keyword",keyword);
        fragment.setArguments(bundle);
        getSupportFragmentManager().beginTransaction()
                .addToBackStack(null)  //将当前fragment加入到返回栈中
                .replace(R.id.fragment_container, fragment).commit();
    }

    /**
     * Gets the current speech recognition mode.
     *
     * @return The speech recognition mode.
     */
    private SpeechRecognitionMode getMode() {
        return SpeechRecognitionMode.LongDictation;

    }

    @Override
    public void onIntentReceived(String s) {
        //没用到
    }

    @Override
    public void onError(int i, String s) {
        Toast.makeText(getApplicationContext(), "错误信息：" + s,
                Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onAudioEvent(boolean recording) {
    }

}
