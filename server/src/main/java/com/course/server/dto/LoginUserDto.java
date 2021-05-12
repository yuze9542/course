package com.course.server.dto;


import java.util.HashSet;
import java.util.List;

public class LoginUserDto {
    private String id;

    /**
     * 登陆名
     */
    private String loginName;

    /**
     * 昵称
     */
    private String name;

    /**
      * 用户标识
     */
    private String token;

    /**
     * 获取所有登录用户的资源
     * 用于前端洁面控制
     * @return
     */
    private List<ResourceDto> resources;

    /**
     * 所有资源的请求 用于后端接口拦截
     * @return
     */
    private HashSet<String > requests;

    public List<ResourceDto> getResources() {
        return resources;
    }

    public void setResources(List<ResourceDto> resources) {
        this.resources = resources;
    }

    public HashSet<String> getRequests() {
        return requests;
    }

    public void setRequests(HashSet<String> requsets) {
        this.requests = requsets;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

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