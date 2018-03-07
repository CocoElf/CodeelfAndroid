package cocoelf.codeelfandroid.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.FileProvider;
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
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.carlos.voiceline.mylibrary.VoiceLineView;
import com.google.gson.Gson;
import com.longsh.optionframelibrary.OptionBottomDialog;
import com.longsh.optionframelibrary.OptionCenterDialog;
import com.microsoft.cognitiveservices.speechrecognition.ISpeechRecognitionServerEvents;
import com.microsoft.cognitiveservices.speechrecognition.MicrophoneRecognitionClient;
import com.microsoft.cognitiveservices.speechrecognition.RecognitionResult;
import com.microsoft.cognitiveservices.speechrecognition.RecognitionStatus;
import com.microsoft.cognitiveservices.speechrecognition.SpeechRecognitionMode;
import com.microsoft.cognitiveservices.speechrecognition.SpeechRecognitionServiceFactory;
import com.microsoft.projectoxford.vision.VisionServiceClient;
import com.microsoft.projectoxford.vision.VisionServiceRestClient;
import com.microsoft.projectoxford.vision.contract.LanguageCodes;
import com.microsoft.projectoxford.vision.contract.Line;
import com.microsoft.projectoxford.vision.contract.OCR;
import com.microsoft.projectoxford.vision.contract.Region;
import com.microsoft.projectoxford.vision.contract.Word;
import com.microsoft.projectoxford.vision.rest.VisionServiceException;
import com.zyao89.view.zloading.ZLoadingDialog;
import com.zyao89.view.zloading.ZLoadingView;
import com.zyao89.view.zloading.Z_TYPE;

import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.rest.spring.annotations.RestService;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import cocoelf.codeelfandroid.R;
import cocoelf.codeelfandroid.activity.helper.ImageHelper;
import cocoelf.codeelfandroid.adapter.ViewPagerAdapter;
import cocoelf.codeelfandroid.fragment.AdviceFragment;
import cocoelf.codeelfandroid.fragment.AdviceFragment_;
import cocoelf.codeelfandroid.fragment.ClockFragment;
import cocoelf.codeelfandroid.fragment.ClockFragment_;
import cocoelf.codeelfandroid.fragment.MemoFragment;
import cocoelf.codeelfandroid.fragment.MemoFragment_;
import cocoelf.codeelfandroid.fragment.SearchFragment_;
import cocoelf.codeelfandroid.fragment.SearchResultFragment_;
import cocoelf.codeelfandroid.fragment.ShareFragment;
import cocoelf.codeelfandroid.fragment.ShareFragment_;
import cocoelf.codeelfandroid.service.SearchService;
import cocoelf.codeelfandroid.service.TimerService;
import cocoelf.codeelfandroid.service.TimerService_;

@EActivity
public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, ISpeechRecognitionServerEvents {
    MicrophoneRecognitionClient micClient = null;//语音客户端
    private ViewPager viewPager;
    private MenuItem menuItem;
    private BottomNavigationView bottomNavigationView;
    String sPrev = "";
    ZLoadingDialog dialog;

    private VisionServiceClient client;//图像客户端
    private static final int REQUEST_TAKE_PHOTO = 0;
    private static final int REQUEST_SELECT_IMAGE_IN_ALBUM = 1;
    // The URI of photo taken from gallery
    private Uri mUriPhotoTaken;
    // File of the photo taken with camera
    private File mFilePhotoTaken;
    // The URI of the image selected to detect.
    private Uri mImageUri;
    // The image selected to detect.
    private Bitmap mBitmap;

    @RestService
    SearchService searchService;

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

        //加载动画
        dialog = new ZLoadingDialog(MainActivity.this);
        dialog.setLoadingBuilder(Z_TYPE.STAR_LOADING).setHintText("Loading...");

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        initBottomNavigationView(); //底部导航
        initViewpager();
        setupViewPager(viewPager);
        setInitPage();
//        initMicClient();
        initVisionServiceClient();
        initTimeService();
    }

    //APP开始计时
    private void initTimeService() {
        this.startService(new Intent(this, TimerService_.class));//启动计时服务

    }

    //APP结束计时
    @Override
    protected void onDestroy() {
        super.onDestroy();
        Intent intent=new Intent();
        intent.setAction(TimerService.CLOCK_SERVICE_ACTION);
        intent.putExtra("method", "stop");
        sendBroadcast(intent);
    }


    private void initBottomNavigationView() {
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

    private void initViewpager() {
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
     * 初始化图像客户端
     */
    private void initVisionServiceClient() {
        if (client == null) {
            client = new VisionServiceRestClient(getString(R.string.subscription_key), getString(R.string.subscription_apiroot));
        }
    }

    /**
     * 弹出所有的fragment
     */
    private void popAll() {
        int num = getSupportFragmentManager().getBackStackEntryCount();
        for (int i = 0; i < num; i++) {
            getSupportFragmentManager().popBackStack();
        }
    }

    /*
     *设置初始默认页面
     */
    private void setInitPage() {
        bottomNavigationView.setSelectedItemId(bottomNavigationView.getMenu().getItem(2).getItemId());
        viewPager.setCurrentItem(2);
    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new ClockFragment_());
        adapter.addFragment(new AdviceFragment_());
        adapter.addFragment(new SearchFragment_());
        adapter.addFragment(new MemoFragment_());
        adapter.addFragment(new ShareFragment_());
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
        // automatically handle clicks on the Home/Up button_begin, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.photo) {
            final ArrayList<String> list = new ArrayList<>();
            list.add((this.getString(R.string.take_photo)));
            list.add((this.getString(R.string.select_image_in_album)));
            final OptionCenterDialog optionCenterDialog = new OptionCenterDialog();
            optionCenterDialog.show(MainActivity.this, list);
            optionCenterDialog.setItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    if (id == 0) {
                        takePhoto(view);
                    } else if (id == 1) {
                        selectImageInAlbum(view);
                    }
                    optionCenterDialog.dismiss();
                }
            });

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


    /**
     * 以下是语音部分
     */
    @Override
    public void onPartialResponseReceived(String s) {

    }

    @Override
    public void onFinalResponseReceived(RecognitionResult response) {
        boolean isFinalDicationMessage = this.getMode() == SpeechRecognitionMode.LongDictation &&
                (response.RecognitionStatus == RecognitionStatus.EndOfDictation ||
                        response.RecognitionStatus == RecognitionStatus.DictationEndSilenceTimeout);

        int max = 0;

        if (response.Results.length > 0) {
            String s = response.Results[0].DisplayText;

            if (sPrev.equals("小精灵")) {
                sPrev = "";
                viewPager.setCurrentItem(2, false);
                search(s);
            } else if (s.equals("小精灵")) {
                sPrev = s;
            }
        }

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


    /**
     * 以下是图像部分
     */
    // When the button_begin of "Take a Photo with Camera" is pressed.
    public void takePhoto(View view) {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (intent.resolveActivity(getPackageManager()) != null) {
            // Save the photo taken to a temporary file.
            File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
            try {
                mFilePhotoTaken = File.createTempFile(
                        "IMG_",  /* prefix */
                        ".jpg",         /* suffix */
                        storageDir      /* directory */
                );

                // Create the File where the photo should go
                // Continue only if the File was successfully created
                if (mFilePhotoTaken != null) {
                    mUriPhotoTaken = FileProvider.getUriForFile(this,
                            "cocoelf.codeelfandroid.fileprovider",
                            mFilePhotoTaken);
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, mUriPhotoTaken);

                    // Finally start camera activity
                    startActivityForResult(intent, REQUEST_TAKE_PHOTO);
                }
            } catch (IOException e) {
                Toast.makeText(getApplicationContext(), "拍照错误：" + e.getMessage(),
                        Toast.LENGTH_SHORT).show();
            }
        }
    }

    // When the button_begin of "Select a Photo in Album" is pressed.
    public void selectImageInAlbum(View view) {
        Intent intent = new Intent(
                Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        // 打开手机相册,设置请求码
        startActivityForResult(intent, REQUEST_SELECT_IMAGE_IN_ALBUM);
    }

    // Deal with the result of selection of the photos and faces.
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case REQUEST_TAKE_PHOTO:
                if (resultCode == RESULT_OK) {
                    mImageUri = Uri.fromFile(mFilePhotoTaken);
                }
                break;
            case REQUEST_SELECT_IMAGE_IN_ALBUM:
                if (resultCode == RESULT_OK) {
                    if (data == null || data.getData() == null) {
                        mImageUri = mUriPhotoTaken;
                    } else {
                        mImageUri = data.getData();
                    }
                }
                break;
            default:
                break;
        }
        mBitmap = ImageHelper.loadSizeLimitedBitmapFromUri(mImageUri, getContentResolver());
        if (mBitmap != null) {
            // Add detection log.
            Log.d("AnalyzeActivity", "Image: " + mImageUri + " resized to " + mBitmap.getWidth()
                    + "x" + mBitmap.getHeight());

            doRecognize();
        }
    }


    public void doRecognize() {
        try {
            dialog.show();
            process();
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), "识别错误：" + e.getMessage(),
                    Toast.LENGTH_SHORT).show();
            ;
        }
    }

    //图像解析成文字
    @Background
    public void process() {
        String excetionMessage = null;
        String result = "";
        try {

            // Put the image into an input stream for detection.
            ByteArrayOutputStream output = new ByteArrayOutputStream();
            mBitmap.compress(Bitmap.CompressFormat.JPEG, 100, output);
            ByteArrayInputStream inputStream = new ByteArrayInputStream(output.toByteArray());

            //获取解析结果
            OCR ocr;
            ocr = this.client.recognizeText(inputStream, LanguageCodes.ChineseSimplified, true);

            //获取用户名
            SharedPreferences sharedPreferences = getSharedPreferences("user", MODE_PRIVATE);
            String username = sharedPreferences.getString("username", "");
            result = searchService.imgToWord(ocr, username).getKeyword();
            Log.d("result", result);
        } catch (VisionServiceException e) {
            excetionMessage = "识别出错！";
        } catch (IOException e) {
            excetionMessage = "识别出错！";
        } catch (Exception e) {
            excetionMessage = "网络出错！";
            e.printStackTrace();
        }
        Log.d("result", result);
        onPostExecute(result, excetionMessage);
    }

    @UiThread
    public void onPostExecute(String data, String exceptionMessage) {
        // Display based on error existence
        if (exceptionMessage != null) {
            dialog.dismiss();
            Toast.makeText(getApplicationContext(), "错误：" + exceptionMessage,
                    Toast.LENGTH_SHORT).show();
        } else {
            search(data);
            dialog.dismiss();
        }
    }

    //跳到搜索界面
    private void search(String keyword) {
        Fragment fragment = new SearchResultFragment_();
        Bundle bundle = new Bundle();
        bundle.putString("keyword", keyword);
        fragment.setArguments(bundle);
        getSupportFragmentManager().beginTransaction()
                .addToBackStack(null)  //将当前fragment加入到返回栈中
                .replace(R.id.fragment_container, fragment).commit();
    }

}
