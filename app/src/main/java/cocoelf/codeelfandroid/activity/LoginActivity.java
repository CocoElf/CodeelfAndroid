package cocoelf.codeelfandroid.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
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

    @AfterViews
    void setInput(){
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
        UserModel userModel = userService.login(username,password);
        showInfo(userModel);
    }

    @UiThread
    void showInfo(UserModel userModel){
        Toast.makeText(this,userModel.toString(),Toast.LENGTH_SHORT).show();
    }


}
