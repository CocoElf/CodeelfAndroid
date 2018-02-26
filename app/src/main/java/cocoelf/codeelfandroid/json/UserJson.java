package cocoelf.codeelfandroid.json;

import java.io.Serializable;

/**
 * Created by green-cherry on 2018/2/26.
 */

public class UserJson  implements Serializable {

    private String username;

    private String password;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString() {
        return "UserJson{" +
                "username='" + username + '\'' +
                ", password='" + password + '\'' +
                '}';
    }

    public UserJson(String username, String password) {
        this.username = username;
        this.password = password;
    }
    public UserJson(){}
}
