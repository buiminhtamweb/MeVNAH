package com.example.toant.googlemap.models;


public class ImageSlider {
    private String name;

    private String urlImage;

    public ImageSlider(String name, String urlImage) {
        this.name = name;
        this.urlImage = urlImage;
    }

    public String getUrlImage() {
        return urlImage;
    }

    public void setUrlImage(String urlImage) {
        this.urlImage = urlImage;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


}
