package cocoelf.codeelfandroid.service;

import org.androidannotations.rest.spring.annotations.Part;
import org.androidannotations.rest.spring.annotations.Post;
import org.androidannotations.rest.spring.annotations.Rest;
import org.springframework.http.converter.FormHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;

import java.util.List;

import cocoelf.codeelfandroid.exception.MyResponseErrorHandler;
import cocoelf.codeelfandroid.json.SearchResultModel;
import cocoelf.codeelfandroid.util.RestAPI;

/**
 * Created by shea on 2018/2/28.
 */

@Rest(rootUrl = RestAPI.URL,
        converters = {FormHttpMessageConverter.class, MappingJackson2HttpMessageConverter.class},
        responseErrorHandler = MyResponseErrorHandler.class)
public interface SearchService {
    @Post("/api/search/queryWithWord")
    List<SearchResultModel> queryWithWord(@Part String keyWord, @Part String username);

}
