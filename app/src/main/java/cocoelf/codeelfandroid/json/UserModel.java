package cocoelf.codeelfandroid.json;

import java.io.Serializable;

/**
 * Created by green-cherry on 2018/2/26.
 */

public class UserModel implements Serializable {

    private String username;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }


    public UserModel(String username) {
        this.username = username;
    }

    public UserModel(){}

    @Override
    public String toString() {
        return "UserModel{" +
                "username='" + username + '\'' +
                '}';
    }
}
