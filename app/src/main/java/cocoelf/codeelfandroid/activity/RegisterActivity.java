package cocoelf.codeelfandroid.activity;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
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

@EActivity(R.layout.activity_register)
public class RegisterActivity extends AppCompatActivity {
    @RestService
    UserService userService;

    @ViewById(R.id.register_btn)
    Button registerBtn;

    @ViewById(R.id.username_input)
    EditText usernameInput;

    @ViewById(R.id.password_input)
    EditText passwordInput;

    @ViewById(R.id.password_confirm_input)
    EditText passwordConfirmInput;

    @ViewById(R.id.activity_register_hasaccount)
    TextView toLoginTextView;

    @AfterViews
    void init(){
        ActionBar actionBar = getSupportActionBar();
        if(actionBar!=null){
            actionBar.hide();
        }
    }

    @Click(R.id.register_btn)
    void register(){
        String username = usernameInput.getText().toString();
        String password = passwordInput.getText().toString();
        String passwordConfirm = passwordConfirmInput.getText().toString();
        if(username.equals("")||password.equals("")||passwordConfirm.equals("")){
            Toast.makeText(this,"您有未输入的选项",Toast.LENGTH_SHORT).show();
        }else if(!password.equals(passwordConfirm)){
            Toast.makeText(this,"两次密码输入不一致",Toast.LENGTH_SHORT).show();
        }else {
            submit(username,password);
        }
    }

    @Click(R.id.activity_register_hasaccount)
    void toLogin(){
        Intent intent = new Intent(this,LoginActivity_.class);
        startActivity(intent);
    }

    @Background
    void submit(String username,String password){
        try {
            UserModel userModel = userService.signUp(username, password);
            toLogin(userModel.getUsername(),password);
        }catch (ResponseException e){
            makeToast(e.getMessage());
        }catch (Exception e){
            makeToast("请检查网络连接");
        }
    }

    @UiThread
    void toLogin(String username,String password){
        Intent intent = new Intent(this,LoginActivity_.class);
        intent.putExtra("username",username);
        intent.putExtra("password",password);
        startActivity(intent);
    }

    @UiThread
    void makeToast(String message){
        Toast.makeText(this,message,Toast.LENGTH_SHORT).show();
    }

}
