package model;

/**
 * User
 * Created by jaycee on 2017/6/23.
 */
public class User {

    private long   id;
    private String token;
    private String imageUrl;
    private String nickname;

    public long getId() {
        return id;
    }

    public String getToken() {
        return token;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public String getNickname() {
        return nickname;
    }
}
