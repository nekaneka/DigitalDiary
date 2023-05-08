package com.spba.ediary.views;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CalendarView;
import android.widget.ListView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.spba.ediary.R;
import com.spba.ediary.adapters.EntryListAdapter;
import com.spba.ediary.controllers.EntryController;
import com.spba.ediary.model.Entry;

import java.util.ArrayList;
import java.util.List;

public class DisplayPageActivity extends AppCompatActivity {

    private ListView entryListView;
    private EntryListAdapter entryAdapter;
    private CalendarView calendarView;
    private BottomNavigationView bottomNavigationView;

    private List<Entry> displayEntries;

    private EntryController entryController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_page);

        entryController = new EntryController(this);
        calendarView = findViewById(R.id.calendarView);
        entryListView = findViewById(R.id.entryListView);

        updateListView(entryController.getAllEntries());

        setBottomNavigation();
        setOnCalendarClick();
        setGotoEntry();

    }


    /**
     * Sets the onitemClickListener for the current displayed list
     */
    private void setGotoEntry() {
        entryListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                startActivity(new Intent(DisplayPageActivity.this, EntryActivity.class)
                        .putExtra("ENTRY_ID", displayEntries.get(i).getUID()));
            }
        });
    }


    /**
     *  Methode to update the list view based on the passed list
     * @param updatedEntryList
     */

    private void updateListView(List<Entry> updatedEntryList){
        displayEntries = new ArrayList<>();
        displayEntries.addAll(updatedEntryList);

        entryAdapter = new EntryListAdapter(this, R.layout.list_entry_layout, displayEntries);
        entryAdapter.notifyDataSetChanged();
        entryListView.setAdapter(entryAdapter);
    }

    /**
     *  Setup for the  CalendarView and its OnDateChangeListener
     */

    private void setOnCalendarClick() {

        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView calendarView, int year, int month, int day) {

                String date = day + "/" + month + 1 + "/" + year;
                //System.out.println(date);
                updateListView(entryController.getEntriesByDate(day, month + 1, year));
            }
        });
    }


    /**
     * Setup for the BottomNavigationView
     */

    private void setBottomNavigation() {
        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setSelectedItemId(R.id.calendar_nav);

        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.add_nav:
                        startActivity(new Intent(DisplayPageActivity.this, CreateEntryActivity.class));
                        //overridePendingTransition(0, 0);
                        return true;
                    case R.id.calendar_nav:
                        return true;
                    case R.id.list_nav:
                        startActivity(new Intent(DisplayPageActivity.this, AllEntriesActivity.class));
                        //overridePendingTransition(0, 0);
                        return true;
                    case R.id.chart:
                        startActivity(new Intent(DisplayPageActivity.this, ChartActivity.class));
                        return true;

                }
                return false;
            }
        });
    }


    @Override
    protected void onRestart() {
        super.onRestart();
        setBottomNavigation();
        updateListView(entryController.getAllEntries());
    }
}