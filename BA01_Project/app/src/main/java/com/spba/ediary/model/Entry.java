package com.spba.ediary.model;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.icu.text.SimpleDateFormat;
import android.util.Base64;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

public class Entry {

    private String UID;
    private String creationDate;
    private String title;
    private boolean isFavorite;
    private String description;
    private List<EntryImage> images;

    private double longitude;
    private double latitude;

    /**
     * User for retrieving data from the database
     * @param UID
     * @param creationDate
     * @param title
     * @param isFavorite
     * @param description
     * @param latitude
     * @param longitude
     */
    public Entry(String UID, String creationDate, String title, boolean isFavorite, String description, double latitude, double longitude) {

        this.UID = UID;
        this.creationDate = creationDate;
        this.title = title;
        this.isFavorite = isFavorite;
        this.description = description;
        this.latitude = latitude;
        this.longitude = longitude;
        this.images = new ArrayList<>();
    }

    /**
     * Constructor only for test data - preferred not to be used
     * @param creationDate
     * @param title
     * @param isFavorite
     * @param description
     * @param latitude
     * @param longitude
     */
    public Entry(String creationDate, String title, boolean isFavorite, String description, double latitude, double longitude) {

        this.UID = UUID.randomUUID().toString();;
        this.creationDate = creationDate;
        this.title = title;
        this.isFavorite = isFavorite;
        this.description = description;
        this.latitude = latitude;
        this.longitude = longitude;
        this.images = new ArrayList<>();
    }


    /**
     * User in the creation process of an entry object in the creationActivity
     *
     * @param title
     * @param description
     * @param latitude
     * @param longitude
     * @param isFavorite
     */
    public Entry(String title, String description, double latitude, double longitude, boolean isFavorite) {

        this.UID = UUID.randomUUID().toString();
        this.creationDate = new SimpleDateFormat("dd/MM/yyyy").format(new Date());
        this.title = title;
        this.isFavorite = isFavorite;
        this.description = description;
        this.latitude = latitude;
        this.longitude = longitude;
        this.images = new ArrayList<>();
    }


    public List<EntryImage> getImages() {
        return images;
    }

    public boolean setImages(List<EntryImage> images) {

        if(images.isEmpty()) return false;

        this.images = new ArrayList<>();
        this.images.addAll(images);

        return true;
    }


    @Override
    public String toString() {
        return "Entry{" +
                "UID='" + UID + '\'' +
                ", creationDate= " + creationDate +
                ", title= '" + title + '\'' +
                //", images=" + images +
                ", isFavorit= '" + isFavorite +
                ", description= " + description + '\'' +
                '}';
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Entry entry = (Entry) o;
        return Objects.equals(UID, entry.UID);
    }

    @Override
    public int hashCode() {
        return Objects.hash(UID, creationDate, title, isFavorite, description, images);
    }

    public String getUID() {
        return UID;
    }

    public void setUID(String UID) {
        this.UID = UID;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }


    public String getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(String creationDate) {
        this.creationDate = creationDate;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Boolean isFavorite() {
        return isFavorite;
    }

    public void setFavorite(boolean favorite) {
        isFavorite = favorite;
    }

    public double getLatitude() {
        return latitude;
    }


    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public String getFirstImage() {
        return images.get(0).getImage();
    }

}

