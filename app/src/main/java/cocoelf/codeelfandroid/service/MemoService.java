package cocoelf.codeelfandroid.service;

import org.androidannotations.rest.spring.annotations.Body;
import org.androidannotations.rest.spring.annotations.Part;
import org.androidannotations.rest.spring.annotations.Path;
import org.androidannotations.rest.spring.annotations.Post;
import org.androidannotations.rest.spring.annotations.Rest;
import org.springframework.http.converter.FormHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;

import java.util.List;

import cocoelf.codeelfandroid.exception.MyResponseErrorHandler;
import cocoelf.codeelfandroid.json.MemoModel;
import cocoelf.codeelfandroid.util.RestAPI;

/**
 * Created by shea on 2018/3/1.
 */
@Rest(rootUrl = RestAPI.URL,
        converters = {FormHttpMessageConverter.class, MappingJackson2HttpMessageConverter.class},
        responseErrorHandler = MyResponseErrorHandler.class)
public interface MemoService {

    @Post("/api/memo/memoList")
    List<MemoModel> getMemoList(@Part String username, @Part String pageNum, @Part String pageSize);

    @Post("/api/memo/memoDetail")
    MemoModel getMemoDetail(@Part String memoId, @Part String username);

    @Post("/api/memo/addMemo/{username}")
    MemoModel addMemo(@Body MemoModel memoModel, @Path String username);

    @Post("/api/memo/deleteMemo/{username}")
    Boolean deleteMemo(@Body MemoModel memoModel,@Path String username);
}
