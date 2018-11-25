package com.example.jmucientes.popularmovies.model;

public class Review
{
    private String id;

    private String content;

    private String author;

    private String url;

    public Review(String id, String content, String author, String url) {
        this.id = id;
        this.content = content;
        this.author = author;
        this.url = url;
    }

    public String getId() {
        return id;
    }

    public String getContent() {
        return content;
    }

    public String getAuthor() {
        return author;
    }

    public String getUrl() {
        return url;
    }

    @Override
    public String toString() {
        return "Review{" +
                "id='" + id + '\'' +
                ", content='" + content + '\'' +
                ", author='" + author + '\'' +
                ", url='" + url + '\'' +
                '}';
    }
}

