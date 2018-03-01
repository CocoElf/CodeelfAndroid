package cocoelf.codeelfandroid.json;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by shea on 2018/3/1.
 */

public class MemoModel implements Serializable {
    private String name;

    private String snippet;

    private String url;

    private String keyWord;

    private Date date;

    private Integer memoId;

    public MemoModel() {
    }

    public MemoModel(String name, String snippet, String url, String keyWord, Date date, Integer memoId) {
        this.name = name;
        this.snippet = snippet;
        this.url = url;
        this.keyWord = keyWord;
        this.date = date;
        this.memoId = memoId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSnippet() {
        return snippet;
    }

    public void setSnippet(String snippet) {
        this.snippet = snippet;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getKeyWord() {
        return keyWord;
    }

    public void setKeyWord(String keyWord) {
        this.keyWord = keyWord;
    }

    public Date getDate() {
        return date;
    }

    public String getFormatDate() {
        return new SimpleDateFormat("yyyy-MM-ss").format(date);
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Integer getMemoId() {
        return memoId;
    }

    public void setMemoId(Integer memoId) {
        this.memoId = memoId;
    }

    @Override
    public String toString() {
        return "MemoModel{" + "name='" + name + '\'' + ", snippet='" + snippet + '\'' + ", url='" + url + '\'' + ", " +
                "keyWord='" + keyWord + '\'' + ", date=" + date + ", memoId=" + memoId + '}';
    }
}
