package com.spba.ediary.views;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.spba.ediary.R;
import com.spba.ediary.adapters.EntryListAdapter;
import com.spba.ediary.controllers.EntryController;
import com.spba.ediary.fragment.FilterFragment;
import com.spba.ediary.intefaces.MultipleChoiceListener;
import com.spba.ediary.model.Entry;

import java.util.ArrayList;
import java.util.List;

/**
 * List of all entries which can be updated by filtering and searching
 */
public class AllEntriesActivity extends AppCompatActivity implements MultipleChoiceListener {


    private ListView entryListView;
    private EntryListAdapter entryAdapter;
    private BottomNavigationView bottomNavigationView;
    private TextView filterText;
    private List<Entry> displayEntries;
    private SearchView searchView;

    private EntryController entryController;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_entries);

        entryController = new EntryController(this);

        setBottomNavigation();
        setListView(entryController.getAllEntries());
        setFiltering();
        setSearchView();
    }


    /**
     * Filters all entries over the title with its equality to the entered string
     */
    private void setSearchView(){
        searchView = findViewById(R.id.search_all_entry);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {

                List<Entry> searchedEntries = new ArrayList<>();

                for(Entry entry: entryController.getAllEntries()){
                    if(entry.getTitle().toLowerCase().contains(s.toLowerCase()))
                        searchedEntries.add(entry);
                }

                setListView(searchedEntries);
                return false;
            }
        });
    }


    /**
     * Setting the passed list of entries to the listview
     * @param list
     */
    private void setListView(List<Entry> list) {

        entryListView = findViewById(R.id.all_entry_list);

        displayEntries = new ArrayList<>();
        displayEntries.addAll(list);


        entryAdapter = new EntryListAdapter(this, R.layout.list_entry_layout, displayEntries);
        entryAdapter.notifyDataSetChanged();
        entryListView.setAdapter(entryAdapter);

        entryListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                startActivity(new Intent(AllEntriesActivity.this, EntryActivity.class).putExtra("ENTRY_ID", displayEntries.get(i).getUID()));
            }
        });
    }


    /**
     * Sets all the categories to the popup dialog with the on clickListener
     */
    private void setFiltering(){

        filterText = findViewById(R.id.filter_text);

        filterText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                DialogFragment filteredCategoriesDialog = new FilterFragment(entryController);
                filteredCategoriesDialog.setCancelable(false);
                filteredCategoriesDialog.show(getSupportFragmentManager(), "Entry Categories");

            }
        });
    }



    private void setBottomNavigation() {
        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setSelectedItemId(R.id.list_nav);

        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.add_nav:
                        startActivity(new Intent(AllEntriesActivity.this, CreateEntryActivity.class));
                       // overridePendingTransition(0,0);
                        return true;
                    case R.id.calendar_nav:
                        startActivity(new Intent(AllEntriesActivity.this, DisplayPageActivity.class));
                        return true;
                       // overridePendingTransition(0,0);
                    case R.id.list_nav:
                        return true;
                    case R.id.chart:
                        startActivity(new Intent(AllEntriesActivity.this, ChartActivity.class));
                        return true;
                }
                return false;
            }
        });
    }


    @Override
    public void positiveButtonClicker(String[] list, ArrayList<String> selectedCategories) {
        if(selectedCategories.isEmpty())
            setListView(entryController.getAllEntries());
        else
            setListView(entryController.getEntriesByFilteredCategories(selectedCategories));
    }

    @Override
    public void negativeButtonClicker() {

    }

    @Override
    protected void onStart() {
        super.onStart();
        setListView(entryController.getAllEntries());
        setBottomNavigation();
    }
}