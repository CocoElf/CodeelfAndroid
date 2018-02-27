package cocoelf.codeelfandroid.service;

import org.androidannotations.rest.spring.annotations.Body;
import org.androidannotations.rest.spring.annotations.Part;
import org.androidannotations.rest.spring.annotations.Post;
import org.androidannotations.rest.spring.annotations.Rest;
import org.springframework.http.converter.FormHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;

import cocoelf.codeelfandroid.exception.MyResponseErrorHandler;
import cocoelf.codeelfandroid.json.UserModel;
import cocoelf.codeelfandroid.util.RestAPI;

/**
 * 注册和登录
 * Created by green-cherry on 2018/2/26.
 */

@Rest(rootUrl =RestAPI.URL,
        converters = {FormHttpMessageConverter.class, MappingJackson2HttpMessageConverter.class},
        responseErrorHandler = MyResponseErrorHandler.class)
public interface UserService {

    @Post("/api/user/signUp")
    UserModel signUp(@Part String username,@Part String password);

    @Post("/api/user/login")
    UserModel login(@Part String username,@Part String password);

    @Post("/api/user/userDetail")
    UserModel getUserDetail(@Part String username);

    @Post("/api/user/ModifyUser")
    UserModel modifyUser(@Body UserModel userModel);


}
