package cocoelf.codeelfandroid.fragment;

import android.content.Context;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.carlos.voiceline.mylibrary.VoiceLineView;
import com.microsoft.cognitiveservices.speechrecognition.ISpeechRecognitionServerEvents;
import com.microsoft.cognitiveservices.speechrecognition.MicrophoneRecognitionClient;
import com.microsoft.cognitiveservices.speechrecognition.RecognitionResult;
import com.microsoft.cognitiveservices.speechrecognition.RecognitionStatus;
import com.microsoft.cognitiveservices.speechrecognition.SpeechRecognitionMode;
import com.microsoft.cognitiveservices.speechrecognition.SpeechRecognitionServiceFactory;

import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.EditorAction;
import org.androidannotations.annotations.ViewById;

import java.util.Random;

import cocoelf.codeelfandroid.R;
import cocoelf.codeelfandroid.activity.DemoActivity;

@EFragment(R.layout.fragment_search)
public class SearchFragment extends Fragment implements ISpeechRecognitionServerEvents {
    MicrophoneRecognitionClient micClient = null;
    @ViewById(R.id.search_input)
    EditText search_input;
    @ViewById(R.id.voiceLine)
    VoiceLineView voiceLineView;

    private void initMicClient() {
        if (this.micClient == null) {
            this.micClient = SpeechRecognitionServiceFactory.createMicrophoneClient(
                    this.getActivity(),
                    SpeechRecognitionMode.LongDictation,
                    this.getString(R.string.localLanguage),
                    this,
                    this.getString(R.string.primaryKey)
            );

            this.micClient.setAuthenticationUri("");
        }
    }


    @Click(R.id.speak)
    public void onclick() {
        initMicClient();
        this.micClient.startMicAndRecognition();
    }

    @EditorAction(R.id.search_input)
    public void onEditorActions(TextView textView, int actionId, KeyEvent keyEvent){
        if (actionId == EditorInfo.IME_ACTION_SEARCH) {
            search();
        }
    }


    /**
     * 跳转页面进行搜索
     */
    private void search(){
        String keyword=search_input.getText().toString();
        Fragment fragment=new SearchResultFragment_();
        Bundle bundle=new Bundle();
        bundle.putString("keyword",keyword);
        fragment.setArguments(bundle);
        getFragmentManager().beginTransaction()
                .addToBackStack(null)  //将当前fragment加入到返回栈中
                .replace(R.id.fragment_container, fragment).commit();
    }

    //下面都是语音功能部分
    @Override
    public void onPartialResponseReceived(String s) {
        //部分输出，将搜索框里的值改掉
        search_input.setText(s);
        search_input.setSelection(s.length());
        showVoice();
    }

    @Override
    public void onFinalResponseReceived(RecognitionResult response) {
        boolean isFinalDicationMessage = this.getMode() == SpeechRecognitionMode.LongDictation &&
                (response.RecognitionStatus == RecognitionStatus.EndOfDictation ||
                        response.RecognitionStatus == RecognitionStatus.DictationEndSilenceTimeout);
        if (null != this.micClient && ((this.getMode() == SpeechRecognitionMode.ShortPhrase) || isFinalDicationMessage)) {
            this.micClient.endMicAndRecognition();
        }

        int max = 0;
        if(response.Results.length>0){
            String s = response.Results[0].DisplayText;
            search_input.setText(s);
            search_input.setSelection(s.length());
        }
        this.micClient.endMicAndRecognition();
        voiceLineView.setVisibility(View.INVISIBLE);
        search();
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
        Toast.makeText(getActivity().getApplicationContext(), "错误信息：" + s,
                Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onAudioEvent(boolean recording) {
        if (recording) {
            //在说话中，声波发生变化
            voiceLineView.setVisibility(View.VISIBLE);
        } else {
            this.micClient.endMicAndRecognition();
        }
    }

    /**
     * 显示声波
     */
    private void showVoice() {
        Random random = new Random();
        double ratio = (double) random.nextInt(8000);
        double db = 0;// 分贝
        //默认的最大音量是100,可以修改，但其实默认的，在测试过程中就有不错的表现
        //你可以传自定义的数字进去，但需要在一定的范围内，比如0-200，就需要在xml文件中配置maxVolume
        //同时，也可以配置灵敏度sensibility
        if (ratio > 1)
            db = 20 * Math.log10(ratio);
        //只要有一个线程，不断调用这个方法，就可以使波形变化
        //主要，这个方法必须在ui线程中调用
        voiceLineView.setVolume((int) (db));
    }


}
