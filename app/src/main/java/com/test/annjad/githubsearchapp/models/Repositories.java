package com.test.annjad.githubsearchapp.models;

import com.squareup.moshi.Json;

import java.util.List;

public class Repositories {

    @Json(name = "items")
    private List<Repository> repositories;

    public Repositories(){}

    public Repositories(List<Repository> repositories) {
        this.repositories = repositories;
    }

    public List<Repository> getRepositories() {
        return repositories;
    }

    public void setRepositories(List<Repository> repositories) {
        this.repositories = repositories;
    }
}
