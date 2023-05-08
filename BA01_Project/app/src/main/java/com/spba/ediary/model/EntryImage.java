package com.spba.ediary.model;

import android.graphics.Bitmap;
import android.graphics.Matrix;

import java.io.Serializable;
import java.util.HashMap;

/**
 * Holder object of the analyzed image data
 */
public class EntryImage implements Serializable {

    private final String NO_CATEGORY = "NO CATEGORY";
    private String category;
    private Float percentage;
    private String image;

    public EntryImage(String category, Float percentage, String image){
        this.category = category;
        this.percentage = percentage;
        this.image = image;
    }

    public EntryImage(String image){
        this.category = NO_CATEGORY;
        this.image = image;
        this.percentage = (float) 0.0;
    }

    public Float getPercentage() {
        return percentage;
    }

    public void setPercentage(Float percentage) {
        this.percentage = percentage;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
