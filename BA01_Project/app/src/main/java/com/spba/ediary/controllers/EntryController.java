package com.spba.ediary.controllers;

import android.content.Context;
import android.graphics.Bitmap;

import com.spba.ediary.ImageAnalyzer;
import com.spba.ediary.helper.Converter;
import com.spba.ediary.model.Entry;
import com.spba.ediary.model.EntryImage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EntryController {

    Context context;
    private DatabaseController databaseController;

    public EntryController(Context context) {
        this.context = context;
        this.databaseController = DatabaseController.getDatabaseController();
    }


    public void saveEntry(String title, String description, double latitude, double longitude, boolean isFavorite, List<Bitmap> galleryImages) {

        Entry entry = new Entry(title, description, latitude, longitude, isFavorite);

        for (Bitmap image : galleryImages) {
            ((Runnable) () -> ImageAnalyzer.runClassification(context, entry.getUID(), Converter.resizeBitmap(image))).run();
        }

        databaseController.addNewEntry(entry);

    }


    public List<Entry> getEntriesByDate(int day, int month, int year) {
        String date;

        String monthString = month < 10 ? "0" + month : String.valueOf(month);
        String dayString = day < 10 ? "0" + day :String.valueOf(day);

        date = dayString + "/" + monthString + "/" + year;

        List<Entry> dateEntries = new ArrayList<>();

        for (Entry entry : databaseController.getAllEntries()) {

            if (entry.getCreationDate().equals(date)) {
                dateEntries.add(entry);
            }
        }

        return dateEntries;
    }


    public Entry getEntryByID(String id) {
        for (Entry entry : new DatabaseController(context).getAllEntries())
            if (entry.getUID().equals(id)) return entry;

        return null;
    }

    public List<Entry> getAllEntries() {
        return databaseController.getAllEntries();
    }

    public String[] getAllCategories() {

        List<String> categoryLIst = new ArrayList<>();
        categoryLIst.add("Favorite");
        categoryLIst.addAll(databaseController.getAllEntryCategories());

        String[] str = new String[categoryLIst.size()];

        for (int i = 0; i < categoryLIst.size(); i++) {
            str[i] = categoryLIst.get(i);
        }

        return str;
    }


    public HashMap<String, Integer> categoryPercentage() {
        HashMap<String, Integer> percentage = new HashMap<>();

        String[] str = getAllCategories();

        for (int i = 0; i < str.length; i++) {
            percentage.put(str[i], 0);
        }

        for (Entry entry : getAllEntries()) {
            for (EntryImage image : entry.getImages())
                percentage.put(image.getCategory(), percentage.get(image.getCategory()) + 1);
        }


        return percentage;
    }

    public List<Entry> getEntriesByFilteredCategories(ArrayList<String> selectedCategories) {

        List<Entry> filteredEntries = new ArrayList<>();

        for (Entry entry : getAllEntries()) {
            for (EntryImage image : entry.getImages()) {
                if (selectedCategories.contains(image.getCategory())
                        || (selectedCategories.contains("Favorite") && entry.isFavorite())) {

                    filteredEntries.add(entry);
                    break;
                }
            }

        }

        return filteredEntries;
    }


    public ArrayList<String> getImages(Entry entry) {

        ArrayList<String> images = new ArrayList();

        for (EntryImage image : entry.getImages()) {
            if (!images.contains(image.getImage()))
                images.add(image.getImage());
        }

        return images;

    }

    public void deleteEntry(String uid) {

        databaseController.deleteEntryByID(uid);

    }

    public void changeFavoriteOfEntry(Entry entry) {
        entry.setFavorite(!entry.isFavorite());
        databaseController.updateFavorite(entry);

    }


    public String[] getCategoriesForImage(Entry entry, ArrayList<Bitmap> images, int position) {

        HashMap<String, Float> categorization = new HashMap<>();

        for (EntryImage image : entry.getImages()) {

            if (Converter.getConverter().bitMapToString(images.get(position)).equals(image.getImage())
                    && !image.getCategory().equals("NO CATEGORY") && image.getPercentage() != 0.0) {
                categorization.put(image.getCategory(), image.getPercentage());
            }
        }

        String[] dialogList = new String[categorization.size()];

        int i = 0;
        for (Map.Entry<String, Float> categorySet : categorization.entrySet()) {
            dialogList[i] = categorySet.getKey() + " : " + categorySet.getValue();
            i++;
        }

        return dialogList;
    }

}
