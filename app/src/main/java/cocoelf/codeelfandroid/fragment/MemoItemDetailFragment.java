package cocoelf.codeelfandroid.fragment;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;
import org.androidannotations.rest.spring.annotations.RestService;

import java.util.List;

import cocoelf.codeelfandroid.R;
import cocoelf.codeelfandroid.exception.ResponseException;
import cocoelf.codeelfandroid.json.MemoModel;
import cocoelf.codeelfandroid.json.SearchResultModel;
import cocoelf.codeelfandroid.service.MemoService;
import jp.wasabeef.richeditor.RichEditor;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by shea on 2018/3/1.
 */
@EFragment(R.layout.fragment_memo_item_detail)
public class MemoItemDetailFragment extends Fragment {
    @ViewById(R.id.fragment_memo_item_detail_editor)
    RichEditor richEditor;
    @ViewById(R.id.action_txt_color)
    ImageButton textColor;
    @ViewById(R.id.action_bg_color)
    ImageButton backgroundColor;

    @RestService
    MemoService memoService;

    private ProgressDialog mProgressDialog;
    private static final String TAG = "MemoItemDetailFragment";

    private String html;
    private String username;
    private MemoModel memoModel;

    @AfterViews
    void init(){
        Log.i(TAG, "init: ");
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("user",MODE_PRIVATE);
//        username = sharedPreferences.getString("username","");
        username = "shea";
        Bundle bundle = getArguments();
        if(bundle!=null&&bundle.containsKey("memoId")){
            mProgressDialog = ProgressDialog.show(getContext(),"",null);

            textColor.setOnClickListener(new View.OnClickListener() {
                private boolean isChanged;
                @Override
                public void onClick(View view) {
                    richEditor.setTextColor(isChanged ? Color.BLACK : Color.RED);
                    isChanged = !isChanged;
                }
            });
            backgroundColor.setOnClickListener(new View.OnClickListener() {
                private boolean isChanged;
                @Override
                public void onClick(View view) {
                    richEditor.setBackgroundColor(isChanged ? Color.TRANSPARENT : Color.YELLOW);
                    isChanged = !isChanged;
                }
            });
            richEditor.setOnTextChangeListener(new RichEditor.OnTextChangeListener() {
                @Override public void onTextChange(String text) {
                    Log.i(TAG, "onTextChange: "+text);
                    html = text;
                   setHtml(text);
                }
            });
            getMemo((Integer)bundle.get("memoId"),username);
        }
    }

    @UiThread
    void setHtml(String html){
        Log.i(TAG, "setHtml: ");
        mProgressDialog.dismiss();
        richEditor.setHtml(html);
    }

    @Click(R.id.action_undo)
    void undo(){
        richEditor.undo();
    }
    @Click(R.id.action_redo)
    void redo(){
        richEditor.redo();
    }
    @Click(R.id.action_bold)
    void setBold(){
        richEditor.setBold();
    }
    @Click(R.id.action_italic)
    void setItalic(){
        richEditor.setItalic();
    }
    @Click(R.id.action_subscript)
    void setSubscript(){
        richEditor.setSubscript();
    }
    @Click(R.id.action_superscript)
    void setSuperscript(){
        richEditor.setSuperscript();
    }
    @Click(R.id.action_strikethrough)
    void setStrikeThrough(){
        richEditor.setStrikeThrough();
    }
    @Click(R.id.action_underline)
    void setUnderline(){
        richEditor.setUnderline();
    }
    @Click(R.id.action_heading1)
    void setHeading1(){
        richEditor.setHeading(1);
    }
    @Click(R.id.action_heading2)
    void setHeading2(){
        richEditor.setHeading(2);
    }
    @Click(R.id.action_heading3)
    void setHeading3(){
        richEditor.setHeading(3);
    }
    @Click(R.id.action_heading4)
    void setHeading4(){
        richEditor.setHeading(4);
    }
    @Click(R.id.action_heading5)
    void setHeading5(){
        richEditor.setHeading(5);
    }
    @Click(R.id.action_heading6)
    void setHeading6(){
        richEditor.setHeading(6);
    }

    @Click(R.id.action_indent)
    void setIndent(){
        richEditor.setIndent();
    }

    @Click(R.id.action_outdent)
    void setOutdent(){
        richEditor.setOutdent();
    }

    @Click(R.id.action_align_left)
    void setAlignLeft(){
        richEditor.setAlignLeft();
    }

    @Click(R.id.action_align_center)
    void setAlignCenter(){
        richEditor.setAlignCenter();
    }

    @Click(R.id.action_align_right)
    void setAlignRight(){
        richEditor.setAlignRight();
    }

    @Click(R.id.action_blockquote)
    void setBlockquote(){
        richEditor.setBlockquote();
    }

    @Click(R.id.action_insert_bullets)
    void setBullets(){
        richEditor.setBullets();
    }

    @Click(R.id.action_insert_numbers)
    void setNumbers(){
        richEditor.setNumbers();
    }

    @Click(R.id.action_insert_image)
    void insertImage(){
        richEditor.insertImage("http://www.1honeywan.com/dachshund/image/7.21/7.21_3_thumb.JPG",
                "dachshund");
    }

    @Click(R.id.action_insert_link)
    void insertLink(){
        richEditor.insertLink("https://github.com/wasabeef", "wasabeef");
    }

    @Click(R.id.action_insert_checkbox)
    void insertTodo(){
        richEditor.insertTodo();
    }


    @Background
    void getMemo(int memoId,String username){
        //根据备忘录的ID获取备忘录
        try {
            memoModel = memoService.getMemoDetail(memoId,username);
            setHtml(memoModel.getContent());
        }catch (ResponseException e){
            makeToast(e.getMessage());
        }catch (Exception e){
            makeToast("请检查网络连接");
        }
    }

    @UiThread
    void makeToast(String text){
        Toast.makeText(getActivity(),text,Toast.LENGTH_SHORT).show();
    }
}
