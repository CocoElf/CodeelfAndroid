package cocoelf.codeelfandroid.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;
import org.androidannotations.rest.spring.annotations.RestService;

import cocoelf.codeelfandroid.R;
import cocoelf.codeelfandroid.exception.ResponseException;
import cocoelf.codeelfandroid.json.UserModel;
import cocoelf.codeelfandroid.service.UserService;

@EActivity(R.layout.activity_login)
public class LoginActivity extends AppCompatActivity {
    @RestService
    UserService userService;

    @ViewById(R.id.username_input)
    EditText usernameInput;

    @ViewById(R.id.password_input)
    EditText passwordInput;

    private static final String TAG = "LoginActivity";

    @AfterViews
    void init(){
        ActionBar actionBar = getSupportActionBar();
        if(actionBar!=null){
            actionBar.hide();
        }
        Intent intent = getIntent();
        String username = intent.getStringExtra("username");
        String password = intent.getStringExtra("password");
        usernameInput.setText(username);
        passwordInput.setText(password);
    }

    @Click(R.id.login_btn)
    void login(){
        String username = usernameInput.getText().toString();
        String password = passwordInput.getText().toString();
        if(username.equals("")||password.equals("")){
            Toast.makeText(this,"您有未输入的选项",Toast.LENGTH_SHORT).show();
        }else {
            submit(username,password);
        }
    }

    @Background
    void submit(String username,String password){
        try{
            UserModel userModel = userService.login(username,password);
            saveLogin(userModel);
        }catch (ResponseException e){
            Toast.makeText(this,e.getMessage(),Toast.LENGTH_SHORT).show();
        }
    }

    @UiThread
    void saveLogin(UserModel userModel){
        SharedPreferences.Editor editor = getSharedPreferences("user",MODE_PRIVATE).edit();
        editor.putString("username",userModel.getUsername());
        editor.apply();
        Intent intent = new Intent(this,MainActivity.class);
        startActivity(intent);
//        SharedPreferences sharedPreferences = getSharedPreferences("user",MODE_PRIVATE);
//        String username = sharedPreferences.getString("username","");
//        Toast.makeText(this,username,Toast.LENGTH_SHORT).show();
//        Log.d(TAG,username);
    }


}
