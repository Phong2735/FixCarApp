package com.example.fixcarapp;

import com.google.gson.annotations.SerializedName;

public class ScenePhoto {
    @SerializedName("image")
    String image;

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
