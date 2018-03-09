package cocoelf.codeelfandroid.util;

import org.springframework.http.converter.FormHttpMessageConverter;

import java.nio.charset.Charset;

/**
 * Created by shea on 2018/3/9.
 */

public class CustomFormHttpMessageConverter extends FormHttpMessageConverter {

    public CustomFormHttpMessageConverter() {
        setCharset(Charset.forName("UTF-8"));
    }
}
