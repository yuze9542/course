package com.course.server.dto;


public class LoginUserDto {


    /**
     * 登陆名
     */
    private String loginName;

    /**
     * 昵称
     */
    private String name;


    public String getLoginName() {
        return loginName;
    }

    public void setLoginName(String loginName) {
        this.loginName = loginName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}