package cocoelf.codeelfandroid.service;

import org.androidannotations.rest.spring.annotations.Body;
import org.androidannotations.rest.spring.annotations.Part;
import org.androidannotations.rest.spring.annotations.Path;
import org.androidannotations.rest.spring.annotations.Post;
import org.androidannotations.rest.spring.annotations.Rest;
import org.springframework.http.converter.FormHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;

import cocoelf.codeelfandroid.exception.MyResponseErrorHandler;
import cocoelf.codeelfandroid.util.CodeType;
import cocoelf.codeelfandroid.util.RestAPI;

/**
 * 计时模块接口
 * Created by green-cherry on 2018/3/3.
 */
@Rest(rootUrl = RestAPI.URL,
        converters = {FormHttpMessageConverter.class, MappingJackson2HttpMessageConverter.class,},
        responseErrorHandler = MyResponseErrorHandler.class)
public interface TimingService {

    @Post("/api/timing/startTiming/{username}")
    public void startTiming(@Body CodeType codeType, @Path String username);

    @Post("/api/timing/endTiming/{username}")
    public void endTiming(@Body CodeType codeType, @Path String username);

    @Post("/api/timing/startAppTiming")
    public void startAppTiming(@Part String timingType, @Part String date,@Part String username);

    @Post("/api/timing/endAppTiming")
    public void endAppTiming(@Part String timingType, @Part String date,@Part String username);

    @Post("/api/timing/pause")
    public void pause(@Part String timingType, @Part String date,@Part String username);

    @Post("/api/timing/endPause")
    public void endPause(@Part String timingType, @Part String date,@Part String username);
}
