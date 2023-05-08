package com.spba.ediary.views;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.MenuItem;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.spba.ediary.R;
import com.spba.ediary.controllers.EntryController;

import java.util.ArrayList;
import java.util.Map;

/**
 *
 * Chart Activity which displays the entry categories percentage wise
 * with the help of the MPAndroidChart chart library
 * https://github.com/PhilJay/MPAndroidChart
 */
public class ChartActivity extends AppCompatActivity {

    private PieChart pieChart;
    private EntryController entryController;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chart);

        setBottomNavigation();
        setChartUp();
    }

    private void setChartUp() {

        pieChart = findViewById(R.id.pie_chart);
        entryController = new EntryController(ChartActivity.this);
        setupPieChart();
        loadPieChartData();
    }

    private void setupPieChart() {
        pieChart.setDrawHoleEnabled(true);
        pieChart.setUsePercentValues(true);
        pieChart.setEntryLabelTextSize(12);
        pieChart.setEntryLabelColor(Color.BLACK);
        pieChart.setCenterText("Entry by Categories");
        pieChart.setCenterTextSize(24);
        pieChart.getDescription().setEnabled(false);

        Legend l = pieChart.getLegend();
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT);
        l.setOrientation(Legend.LegendOrientation.VERTICAL);
        l.setDrawInside(false);
        l.setEnabled(true);
    }


    private void loadPieChartData() {

        ArrayList<PieEntry> entries = new ArrayList<>();
        for (Map.Entry<String, Integer> set : entryController.categoryPercentage().entrySet())
            entries.add(new PieEntry(set.getValue(), set.getKey()));

        ArrayList<Integer> colors = new ArrayList<>();

        for (int color : ColorTemplate.MATERIAL_COLORS)
            colors.add(color);

        for (int color : ColorTemplate.VORDIPLOM_COLORS)
            colors.add(color);

        PieDataSet dataSet = new PieDataSet(entries, "Entry by Categories");
        dataSet.setColors(colors);

        PieData data = new PieData(dataSet);

        data.setDrawValues(true);
        data.setValueFormatter(new PercentFormatter(pieChart));
        data.setValueTextSize(12f);
        data.setValueTextColor(Color.BLACK);

        pieChart.setData(data);
        pieChart.invalidate();
        pieChart.animateY(1400, Easing.EaseInOutQuad);
    }


    private void setBottomNavigation() {
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setSelectedItemId(R.id.chart);

        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @SuppressLint("NonConstantResourceId")
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.add_nav:
                        startActivity(new Intent(ChartActivity.this, CreateEntryActivity.class));
                        return true;
                    case R.id.calendar_nav:
                        startActivity(new Intent(ChartActivity.this, DisplayPageActivity.class));
                        return true;
                    case R.id.list_nav:
                        startActivity(new Intent(ChartActivity.this, AllEntriesActivity.class));
                        return true;
                    case R.id.chart:
                        return true;
                }
                return false;
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        setBottomNavigation();
    }
}