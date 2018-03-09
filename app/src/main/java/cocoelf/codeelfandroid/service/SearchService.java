package cocoelf.codeelfandroid.service;

import com.microsoft.projectoxford.vision.contract.OCR;

import org.androidannotations.rest.spring.annotations.Body;
import org.androidannotations.rest.spring.annotations.Part;
import org.androidannotations.rest.spring.annotations.Path;
import org.androidannotations.rest.spring.annotations.Post;
import org.androidannotations.rest.spring.annotations.Rest;
import org.springframework.http.converter.FormHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;

import java.util.List;

import cocoelf.codeelfandroid.exception.MyResponseErrorHandler;
import cocoelf.codeelfandroid.json.SearchModel;
import cocoelf.codeelfandroid.json.SearchResultModel;
import cocoelf.codeelfandroid.util.CustomFormHttpMessageConverter;
import cocoelf.codeelfandroid.util.RestAPI;

/**
 * Created by shea on 2018/2/28.
 */

@Rest(rootUrl = RestAPI.URL,
        converters = {CustomFormHttpMessageConverter.class, FormHttpMessageConverter.class,MappingJackson2HttpMessageConverter.class},
        responseErrorHandler = MyResponseErrorHandler.class)
public interface SearchService {
    @Post("/api/search/queryWithWord/{username}")
    List<SearchResultModel> queryWithWord(@Body SearchModel keyWord, @Path String username);

    @Post("/api/search/imgToWord/{username}")
    SearchModel imgToWord(@Body OCR ocr, @Path String username);
}
