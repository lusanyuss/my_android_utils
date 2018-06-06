package com.yuliu.base.results;

public class TokenResult {

    public int      code;
    public String   message;
    public DataBean data;

    public static class DataBean {
        public String mapbox_access_token;
    }
}
