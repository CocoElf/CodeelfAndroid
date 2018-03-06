package cocoelf.codeelfandroid.json;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by shea on 2018/3/1.
 */

public class MemoModel implements Serializable {
    private String name;
    private String url;
    private String snippet;
    private Date date;
    private List<String> keywords;
    private String type;
    private Integer memoId;

    public MemoModel() {
    }

    public MemoModel(String name, String url, String snippet, Date date, List<String> keywords, String type, Integer memoId) {
        this.name = name;
        this.url = url;
        this.snippet = snippet;
        this.date = date;
        this.keywords = keywords;
        this.type = type;
        this.memoId = memoId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTitle(){
        String temp = "["+type+"]  "+name;
        return temp.length()>48?temp.substring(0,45)+"...":temp;
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

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getFormatDate() {
        return new SimpleDateFormat("yyyy/MM/ss").format(date);
    }

    public List<String> getKeywords() {
        return keywords;
    }

    public void setKeywords(List<String> keywords) {
        this.keywords = keywords;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Integer getMemoId() {
        return memoId;
    }

    public void setMemoId(Integer memoId) {
        this.memoId = memoId;
    }
}
