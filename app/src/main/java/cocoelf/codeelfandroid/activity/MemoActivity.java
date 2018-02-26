package cocoelf.codeelfandroid.activity;

import android.app.Activity;
import android.provider.DocumentsContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.OptionsItem;
import org.androidannotations.annotations.OptionsMenu;
import org.androidannotations.annotations.OptionsMenuItem;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.rest.spring.annotations.RestService;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import cocoelf.codeelfandroid.R;
import cocoelf.codeelfandroid.exception.ResponseException;
import cocoelf.codeelfandroid.json.UserJson;
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
        UserJson json = new UserJson("chen", "123456");
        submit(json);

    }

    @OptionsItem(R.id.remove_item)
    void Remove() {
        Toast.makeText(this, "hey", Toast.LENGTH_SHORT).show();
    }

    @Background
    public void submit(UserJson json) {
        json = userService.signUp(new UserJson("chen", "123456"));
        showInfo(json.toString());

    }

    @UiThread
    public void showInfo(String s) {
        Toast.makeText(this,s,Toast.LENGTH_SHORT).show();
    }
}
