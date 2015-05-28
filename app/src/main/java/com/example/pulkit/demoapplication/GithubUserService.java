package com.example.pulkit.demoapplication;

import java.util.List;

import retrofit.http.GET;

public interface GithubUserService {
    @GET("/users")
    public List<User> getUsers();

}
