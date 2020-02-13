package com.test.annjad.githubsearchapp.models;

import com.squareup.moshi.Json;

public class Repository {

    private String name;

    private String description;

    @Json(name = "html_url")
    private String htmlUrl;

    private String language;

    private double score;

    public Repository() {
    }

    public Repository(String name, String description, String htmlUrl, String language, double score) {
        this.name = name;
        this.description = description;
        this.htmlUrl = htmlUrl;
        this.language = language;
        this.score = score;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getHtmlUrl() {
        return htmlUrl;
    }

    public void setHtmlUrl(String htmlUrl) {
        this.htmlUrl = htmlUrl;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public double getScore() {
        return score;
    }

    public void setScore(double score) {
        this.score = score;
    }
}
