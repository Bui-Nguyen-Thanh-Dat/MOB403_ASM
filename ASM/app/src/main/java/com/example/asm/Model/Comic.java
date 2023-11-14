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


    public Comic() {
    }

    public Comic(String title, String name, String chapter,String image) {
        this.image=image;
        this.title = title;
        this.name = name;
        this.chapter = chapter;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
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

    @Override
    public String toString() {
        return "Comic{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", name='" + name + '\'' +
                ", image='" + image + '\'' +
                ", chapter=" + chapter +
                '}';
    }
}
