package com.jiawa.wiki.exception;

public enum BusinessExceptionCode {

    USER_LOGIN_NAME_EXIST("Username already exists!"),
    LOGIN_USER_ERROR("Username is incorrect or password!"),
    VOTE_REPEAT("You have repeated vote!"),
    ;

    private String desc;

    BusinessExceptionCode(String desc) {
        this.desc = desc;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}
