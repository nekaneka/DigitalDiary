package com.spba.ediary.views;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.spba.ediary.R;
import com.spba.ediary.adapters.RecycleGalleryAdapter;
import com.spba.ediary.controllers.EntryController;
import com.spba.ediary.helper.Converter;
import com.spba.ediary.model.Entry;

import java.util.ArrayList;

public class EntryActivity extends AppCompatActivity implements RecycleGalleryAdapter.ItemClickListener {

    private Entry entry;

    private EntryController controller;
    private RecycleGalleryAdapter adapter;


    private ImageButton isFavorite;
    private ImageButton analyzes;
    private ImageButton openGoogleMap;

    private ImageView mainImage;
    private ImageButton deleteEntry;

    private TextView title;
    private TextView description;
    private TextView date;

    private RecyclerView recyclerView;


    //private ImageButton locationButton;
    //private ImageButton analyzeImages;
    //private MediaPlayer mediaPlayer;


    private ArrayList<Bitmap> images;

    Context context;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_entry);
        controller = new EntryController(this);
        entry = controller.getEntryByID(getIntent().getExtras().get("ENTRY_ID").toString());
        context = this;
        images = new ArrayList<>();

        variableInitialization();

        setImages();
        setFavoriteButton();
        setDeleteButton();
        setGalleryRecycler();
        setTextViews();
        showAnalyzes(0);
        setGoogleMap();
        //     setPlayAudio();
    }

    private void variableInitialization() {

        isFavorite = findViewById(R.id.star);
        mainImage = findViewById(R.id.main_image);
        deleteEntry = findViewById(R.id.delete_entry);
        recyclerView = findViewById(R.id.gallery_recycle_view);
        title = findViewById(R.id.entry_activity_title);
        description = findViewById(R.id.entry_activity_description);
        date = findViewById(R.id.entry_activity_date);
        openGoogleMap = findViewById(R.id.map_button);
        analyzes = findViewById(R.id.analyze_button);

    }

    /*
        private void setPlayAudio() {

            startAudioRecording = findViewById(R.id.start_audio_recording);
            stopAudioRecording = findViewById(R.id.stop_audio_recording_button);
        }


     */
    private void setTextViews() {

        title.setText(entry.getTitle());

        description.setMovementMethod(new ScrollingMovementMethod());
        description.setText(entry.getDescription());

        date.setText(entry.getCreationDate());

    }

    /**
     *
     */
    private void setGoogleMap() {

        if (entry.getLongitude() != 0.0 && entry.getLatitude() != 0.0) {
            openGoogleMap.setEnabled(true);
            openGoogleMap.setImageResource(R.drawable.ic_baseline_location_on_24);
            openGoogleMap.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    openGoogleMap(entry.getLatitude(), entry.getLongitude());
                }
            });
        } else {
            openGoogleMap.setEnabled(false);
            openGoogleMap.setImageResource(R.drawable.ic_baseline_location_off);
        }
    }


    /**
     *
     */
    private void setImages() {

        for (String image : controller.getImages(entry)) {
            images.add(Converter.stringToBitmap(image));
        }
        mainImage.setImageBitmap(images.get(0));
    }


    private void showAnalyzes(int position) {

        String[] dialogList = controller.getCategoriesForImage(entry, images, position);

        if (dialogList.length == 0) {

            analyzes.setEnabled(false);

        }else {
            analyzes.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    AlertDialog.Builder builder = new AlertDialog.Builder(EntryActivity.this);

                    builder.setTitle("Image Categorisation");
                    builder.setItems(dialogList, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int item) {
                        }
                    });

                    AlertDialog alert = builder.create();
                    alert.show();
                }
            });
        }
    }

    /**
     *
     */
    private void setDeleteButton() {

        deleteEntry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AlertDialog.Builder builder = new AlertDialog.Builder(EntryActivity.this);
                builder.setMessage("Do you want to delete the Entry?")
                        .setCancelable(false)
                        .setPositiveButton("DELETE", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                controller.deleteEntry(entry.getUID());
                                finish();
                            }
                        })
                        .setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });

                AlertDialog alert = builder.create();
                alert.setTitle("Delete Entry");
                alert.show();
            }
        });
    }


    /**
     *
     */
    private void setGalleryRecycler() {
        LinearLayoutManager horizontalLayoutManger = new LinearLayoutManager(EntryActivity.this, LinearLayoutManager.HORIZONTAL, false);

        recyclerView.setLayoutManager(horizontalLayoutManger);
        adapter = new RecycleGalleryAdapter(this, images);
        adapter.setClickListener(this);
        recyclerView.setAdapter(adapter);
    }


    /**
     * Sets the favorite button onclick listener which regulate the favorite state of the entry
     * After the button is clicked, the methode call itself to change the buttonView
     */
    private void setFavoriteButton() {


        isFavorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                controller.changeFavoriteOfEntry(entry);
                setFavoriteButton();
            }
        });

        if (entry.isFavorite()) {
            isFavorite.setImageDrawable(ContextCompat.getDrawable(this, android.R.drawable.btn_star_big_on));
        } else {
            isFavorite.setImageDrawable(ContextCompat.getDrawable(this, android.R.drawable.btn_star_big_off));
        }
    }


    /**
     *
     * Activates the Google Maps Intents
     * https://developers.google.com/maps/documentation/urls/android-intents?hl=de
     *
     * @param latitude
     * @param longitude
     */
    private void openGoogleMap(double latitude, double longitude) {
        Uri gmmIntentUri = Uri.parse("google.streetview:cbll=" + latitude + "," + longitude);
        // Create an Intent from gmmIntentUri. Set the action to ACTION_VIEW
        Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
        // Make the Intent explicit by setting the Google Maps package
        mapIntent.setPackage("com.google.android.apps.maps");

        // Attempt to start an activity that can handle the Intent
        startActivity(mapIntent);
    }

    @Override
    public void onItemClick(View view, int position) {
        mainImage.setImageBitmap(images.get(position));
        showAnalyzes(position);
    }
}