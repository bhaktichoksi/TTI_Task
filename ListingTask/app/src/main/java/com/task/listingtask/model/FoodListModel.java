package com.task.listingtask.model;

import com.google.gson.annotations.SerializedName;

public class FoodListModel {

    @SerializedName("title")
    private String title;

    @SerializedName("image")
    private String image;

    @SerializedName("permalink")
    private String permalink;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getPermalink() {
        return permalink;
    }

    public void setPermalink(String permalink) {
        this.permalink = permalink;
    }

    public FoodListModel(String title, String image, String permalink) {
        this.title = title;
        this.image = image;
        this.permalink = permalink;
    }

    public FoodListModel() {
    }


}
