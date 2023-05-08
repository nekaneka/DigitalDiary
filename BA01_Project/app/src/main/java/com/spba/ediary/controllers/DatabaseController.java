package com.spba.ediary.controllers;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import com.spba.ediary.exceptions.NoUserRegisteredException;
import com.spba.ediary.model.Entry;
import com.spba.ediary.model.EntryImage;
import com.spba.ediary.model.User;

import java.util.ArrayList;
import java.util.List;

public class DatabaseController extends SQLiteOpenHelper {

    private static DatabaseController databaseController = null;

    private static final String DB_NAME = "diaryDB";

    private static final int DB_VERSION = 1;

    private static final String USER_TABLE = "user";
    private static final String ENTRY_TABLE = "entry";
    private static final String CATEGORY_TABLE = "category";

    private static final String UID = "UID";
    private static final String DESCRIPTION = "DESCRIPTION";
    private static final String DATE = "DATE";
    private static final String TITLE = "TITLE";
    private static final String FAVORITE = "FAVORITE";
    private static final String LATITUDE = "LATITUDE";
    private static final String LONGITUDE = "LONGITUDE";


    private static final String ENTRY_ID = "ENTRY_ID";
    private static final String CATEGORY = "CATEGORY";
    private static final String CONFIDENCE = "CONFIDENCE";
    private static final String IMAGE = "IMAGE";

    private SQLiteDatabase DATABASE;


    public DatabaseController(@Nullable Context context) {
        super(context, DB_NAME, null, DB_VERSION);
        databaseController = this;
    }

    public static DatabaseController getDatabaseController() {
        return databaseController;
    }

    public static String getDbName() {
        return DB_NAME;
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        sqLiteDatabase.execSQL(createUserTableQuery());
        sqLiteDatabase.execSQL(createEntryTableQuery());
        sqLiteDatabase.execSQL(createCategoryTableQuery());

    }


    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + USER_TABLE);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + ENTRY_TABLE);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + CATEGORY_TABLE);
        onCreate(sqLiteDatabase);

    }

    public boolean checkForTestData(){
        return getAllEntries().isEmpty();
    }

    public void registerNewUser(String name, String password) {

        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put("NAME", name);
        values.put("PASSWORD", password);

        database.insert(USER_TABLE, null, values);

        database.close();

    }

    public boolean checkIfUserAlreadyExist() {

        DATABASE = this.getReadableDatabase();

        Cursor cursorCourses = DATABASE.rawQuery("SELECT * FROM " + "user", null);

        boolean value = cursorCourses.moveToFirst();
        cursorCourses.close();

        return value;
    }

    public void addNewEntry(Entry entry) {

        DATABASE = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(UID, entry.getUID());
        values.put(DATE, entry.getCreationDate().toString());
        values.put(TITLE, entry.getTitle());
        values.put(FAVORITE, entry.isFavorite());
        values.put(DESCRIPTION, entry.getDescription());
        values.put(LATITUDE, Double.toString(entry.getLatitude()));
        values.put(LONGITUDE, Double.toString(entry.getLongitude()));
        // values.put("AUDIO_PATH", entry.getAudioPath());

        DATABASE.insert(ENTRY_TABLE, null, values);
        DATABASE.close();
    }

    public synchronized void addCategories(String entryId, EntryImage entryImage) {
        DATABASE = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(ENTRY_ID, entryId);
        values.put(CATEGORY, entryImage.getCategory());
        values.put(CONFIDENCE, entryImage.getPercentage().toString());
        values.put(IMAGE, entryImage.getImage());

        DATABASE.insert(CATEGORY_TABLE, null, values);
        DATABASE.close();
    }




    public void updateFavorite(Entry entry){
        DATABASE = this.getWritableDatabase();

        System.out.println("Update Favorite" + entry.toString());
        ContentValues values = new ContentValues();
        values.put(UID, entry.getUID());
        values.put(DATE, entry.getCreationDate());
        values.put(TITLE, entry.getTitle());
        values.put(FAVORITE, entry.isFavorite());
        values.put(DESCRIPTION, entry.getDescription());
        values.put(LATITUDE, Double.toString(entry.getLatitude()));
        values.put(LONGITUDE, Double.toString(entry.getLongitude()));


        DATABASE.update(ENTRY_TABLE, values, UID + " = ?", new String[]{entry.getUID()});
    }




    public User getUser() throws NoUserRegisteredException {

        DATABASE = this.getReadableDatabase();

        Cursor users = DATABASE.rawQuery("SELECT * FROM " + "user", null);

        List<User> userList = new ArrayList<>();

        if (users.moveToFirst()) {
            do {
                userList.add(User.getUser(users.getString(1), users.getString(2)));
            } while (users.moveToNext());
        }
        users.close();

        if (userList.isEmpty()) throw new NoUserRegisteredException("Something went wrong. Please try again.");

        return userList.get(0);
    }


    public List<Entry> getAllEntries() {
        DATABASE = this.getReadableDatabase();

        Cursor entries = DATABASE.rawQuery("SELECT * FROM " + ENTRY_TABLE, null);

        List<Entry> allEntries = new ArrayList<>();

        if (entries.moveToFirst()) {
            do {
                Entry databaseEntry = new Entry(entries.getString(0), entries.getString(1),
                        entries.getString(2), entries.getString(3).equals("1"),
                        entries.getString(4), Double.parseDouble(entries.getString(5)),
                        Double.parseDouble(entries.getString(6)));
                databaseEntry.setImages(getImagesByEntryIs(databaseEntry.getUID()));
                allEntries.add(databaseEntry);
            } while (entries.moveToNext());
        }
        entries.close();
        DATABASE.close();

        return allEntries;
    }

    private List<EntryImage> getImagesByEntryIs(String uid) {

        Cursor entries = DATABASE.rawQuery("SELECT * FROM " + CATEGORY_TABLE + " WHERE ENTRY_ID ='" + uid + "'", null);

        List<EntryImage> entryImages = new ArrayList<>();

        if (entries.moveToFirst()) {
            do {

                EntryImage entryImage = new EntryImage(entries.getString(2), Float.valueOf(entries.getString(3)), entries.getString(4));
                entryImages.add(entryImage);
            } while (entries.moveToNext());
        }
        entries.close();

        return entryImages;
    }

    public List<String> getAllEntryCategories() {
        DATABASE = this.getReadableDatabase();

        Cursor entries = DATABASE.rawQuery("SELECT * FROM " + CATEGORY_TABLE, null);

        List<String> categoryList = new ArrayList<>();

        if (entries.moveToFirst()) {
            do {
                if (!categoryList.contains(entries.getString(2)))
                    categoryList.add(entries.getString(2));
            } while (entries.moveToNext());
        }
        entries.close();

        DATABASE.close();

        return categoryList;
    }

    public void deleteEntryByID(String uid) {

        DATABASE = this.getWritableDatabase();
        DATABASE.delete(ENTRY_TABLE, UID + "=?", new String[]{uid});
        DATABASE.delete(CATEGORY_TABLE, "ENTRY_ID=?", new String[]{uid});
        DATABASE.close();
    }


    //
    private String createUserTableQuery() {
        return "CREATE TABLE " + USER_TABLE + " ("
                + "ID" + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + "NAME" + " TEXT,"
                + "PASSWORD" + " TEXT)";
    }

    private String createEntryTableQuery() {

        return "CREATE TABLE " + ENTRY_TABLE + " ("
                + UID + " STRING PRIMARY KEY, "
                + DATE + " TEXT, "
                + TITLE + " TEXT, "
                + FAVORITE + " INTEGER DEFAULT 0, "
                + DESCRIPTION + " TEXT, "
                + LATITUDE + " TEXT, "
                + LONGITUDE + " TEXT)";
    }

    private String createCategoryTableQuery() {

        return "CREATE TABLE " + CATEGORY_TABLE + " ("
                + "ID" + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + ENTRY_ID + " TEXT, "
                + CATEGORY + " TEXT, "
                + CONFIDENCE + " TEXT, "
                + IMAGE + " TEXT)";
    }

}
