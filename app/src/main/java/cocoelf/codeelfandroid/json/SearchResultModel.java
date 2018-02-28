package cocoelf.codeelfandroid.json;

import java.io.Serializable;

/**
 * Created by shea on 2018/2/27.
 */

public class SearchResultModel implements Serializable{
    private String name;
    private String url;
    private String snippet;


    public SearchResultModel() {
    }

    public SearchResultModel(String name, String url, String snippet) {
        this.name = name;
        this.url = url;
        this.snippet = snippet;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getSnippet() {
        return snippet;
    }

    public void setSnippet(String snippet) {
        this.snippet = snippet;
    }
}
