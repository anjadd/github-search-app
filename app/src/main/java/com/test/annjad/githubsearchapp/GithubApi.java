package com.test.annjad.githubsearchapp;

import com.test.annjad.githubsearchapp.models.Repositories;

import java.util.Map;

import io.reactivex.Single;
import retrofit2.http.GET;
import retrofit2.http.QueryMap;

public interface GithubApi {

    @GET("repositories")
    Single<Repositories> getGithubSearchRepositories(@QueryMap Map<String, String> filters);
}