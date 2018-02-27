package cocoelf.codeelfandroid.activity;

import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.OptionsItem;
import org.androidannotations.annotations.OptionsMenu;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.rest.spring.annotations.RestService;

import cocoelf.codeelfandroid.R;
import cocoelf.codeelfandroid.json.UserModel;
import cocoelf.codeelfandroid.service.UserService;

@EActivity(R.layout.activity_memo)
@OptionsMenu(R.menu.main)
public class MemoActivity extends AppCompatActivity {
    //    @OptionsMenuItem
//    MenuItem menuItem;
    @RestService
    UserService userService;

    @OptionsItem(R.id.add_item)
    void Add() {
        submit("chen", "123456");

    }

    @OptionsItem(R.id.remove_item)
    void Remove() {
        Toast.makeText(this, "hey", Toast.LENGTH_SHORT).show();
    }

    @Background
    public void submit(String username,String password) {
        UserModel userModel = userService.signUp(username,password);
        showInfo(userModel.toString());

    }

    @UiThread
    public void showInfo(String s) {
        Toast.makeText(this,s,Toast.LENGTH_SHORT).show();
    }
}
