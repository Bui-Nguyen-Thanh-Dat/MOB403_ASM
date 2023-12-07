package com.example.asm.Model;

import com.google.gson.annotations.SerializedName;

import java.util.Arrays;

public class Comic {
    @SerializedName("_id")
    String id;
    @SerializedName("title")
    String title;
    @SerializedName("name")
    String name;
    @SerializedName("chapter")
    String chapter;
    @SerializedName("image")
    String image;
    @SerializedName("status")
    int status;

    public Comic() {
    }

    public Comic(String title, String name, String chapter, String image, int status) {
        this.title = title;
        this.name = name;
        this.chapter = chapter;
        this.image = image;
        this.status = status;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getChapter() {
        return chapter;
    }

    public void setChapter(String chapter) {
        this.chapter = chapter;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "Comic{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", name='" + name + '\'' +
                ", image='" + image + '\'' +
                ", status='" + status + '\'' +
                ", chapter=" + chapter +
                '}';
    }
}
