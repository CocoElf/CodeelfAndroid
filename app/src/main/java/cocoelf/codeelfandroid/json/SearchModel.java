package cocoelf.codeelfandroid.json;

/**
 * Created by green-cherry on 2018/3/1.
 */

public class SearchModel {
    private String keyword;

    public SearchModel() {

    }

    public SearchModel(String s) {
        this.keyword = s;
    }

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    @Override
    public String toString() {
        return "SearchModel{" + "keyword='" + keyword + '\'' + '}';
    }
}
