package com.spba.ediary.views;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.ImageDecoder;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.Settings;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;


import com.spba.ediary.R;
import com.spba.ediary.adapters.RecycleGalleryAdapter;
import com.spba.ediary.controllers.EntryController;
import com.spba.ediary.helper.Converter;
import com.spba.ediary.model.Entry;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;


public class CreateEntryActivity extends AppCompatActivity implements RecycleGalleryAdapter.ItemClickListener {

    private static final int GalleryRequest = 1;
    private static final int CameraRequest = 2;

    private ImageView imageView;
    private ImageButton locationsButton;
    private EditText title;
    private EditText description;
    private RecycleGalleryAdapter adapter;

    private double latitude = 0;
    private double longitude = 0;
    private boolean isFavorite = false;

    private LocationManager manager;
    private boolean gpsEnabled;

    private List<Bitmap> galleryImages;

    private boolean locationAllowed;
    private boolean locationDenied = false;

    Context context;

    private EntryController entryController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_entry);

        context = this;
        entryController = new EntryController(this);

        initializeAndRefreshData();

    }

    /**
     * Is called after the onCreate and save  methods to initialize and clear all views and variables
     *
     */
    private void initializeAndRefreshData() {

        title = findViewById(R.id.entry_title);
        description = findViewById(R.id.entry_description);
        locationsButton = findViewById(R.id.location_button);

        title.setText("");
        description.setText("");

        initializeLocation();
        setImageGalleryRecycler();
        setFavoriteButton();

    }

    /**
     * Main methode that controls the location initialization and management
     * by checking the localization permissions and the gps availability
     * from other methods.
     *
     * It also initializes the onClickListener of the locationsButton
     */
    private void initializeLocation(){
        locationDenied = true;
        locationAllowed = true;

        manager = (LocationManager) getApplicationContext().getSystemService(Context.LOCATION_SERVICE);
        gpsEnabled = manager.isProviderEnabled(LocationManager.GPS_PROVIDER);

        if(!gpsEnabled || !checkLocationPermission()) {
            locationsButton.setImageResource(R.drawable.ic_baseline_location_off);
            locationAllowed = false;
        }

        locationsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(locationAllowed){
                    Toast.makeText(CreateEntryActivity.this, "Location is already on", Toast.LENGTH_SHORT).show();
                }
                else{
                    gpsLocation();
                }
            }
        });
    }


    /**
     * Sets up the gallery recycler view and the main image display
     */
    private void setImageGalleryRecycler() {
        galleryImages = new ArrayList<>();

        imageView = findViewById(R.id.selected_image);
        imageView.setImageResource(android.R.color.transparent);
        setGalleryRecycler();
    }


    /**
     * Starts the Gallery Intent and the results are processed in the onActivityResult methode
     * Is called directly from the xml View with the onClick="openGallery"
     * @param view
     */
    public void openGallery(View view) {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, GalleryRequest);
    }


    /**
     * Starts the Camera Intent and the results are processed in the onActivityResult methode
     * Is called directly from the xml View with the onClick="openCamera"
     * @param view
     */
    public void openCamera(View view) {
        Intent intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, CameraRequest);
    }


    /**
     *
     * Closes the current activity and returns to the previous active activity from the stack
     * Is called directly from the xml View with the onClick="cancel"
     *
     * @param view
     * @throws ParseException
     * @throws IOException
     */
    public void cancel(View view) throws ParseException, IOException {
        finish();
    }



    /**
     *
     * Checks if all the fields are entered correctly.
     * Initiates the creating process of an entry by passing all the data to the entry controller methode saveEntry()
     * Is called directly from the xml View with the onClick="cancel"
     *
     * @param view
     * @throws ParseException
     * @throws IOException
     */
    public void save(View view) throws ParseException, IOException {

        if (title.getText().toString().equals("") || title.getText().length() <= 0
                || description.getText().toString().equals("") || description.getText().length() <= 0
                || galleryImages.isEmpty()) {
            Toast.makeText(this, "Please fill up the fields and at least one picture picture.", Toast.LENGTH_SHORT).show();
            return;
        }

        entryController.saveEntry(title.getText().toString(), description.getText().toString(), latitude, longitude, isFavorite, galleryImages);

        Toast.makeText(this, "NEW ENTRY CREATED", Toast.LENGTH_SHORT).show();

        initializeAndRefreshData();

    }


    /**
     *
     *
     */
    private void setFavoriteButton() {
        ImageButton ButtonStar = (ImageButton) findViewById(R.id.star);

        ButtonStar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (!isFavorite) {
                    ButtonStar.setImageDrawable(ContextCompat.getDrawable(context.getApplicationContext(), android.R.drawable.btn_star_big_on));
                    isFavorite = true;

                } else {
                    ButtonStar.setImageDrawable(ContextCompat.getDrawable(context.getApplicationContext(), android.R.drawable.btn_star_big_off));
                    isFavorite = false;
                }
            }
        });
    }


    /**
     * Function that initiates the location activation
     * Allows an app to access precise location
     */
    private boolean checkLocationPermission() {
        return ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED;

    }



    /**
     * Callback for the result from getCurrentLocation()
     */
    @SuppressLint("SuspiciousIndentation")
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[], @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                locationDenied = false;
                gpsLocation();
            }else {
                locationsButton.setImageResource(R.drawable.ic_baseline_location_off);
                locationDenied = true;
            }
        }
    }



    /**
     * Check if approximate and precise permissions are allowed
     * Get Longitude and Latitude
     */
    @SuppressWarnings("ResourceType")
    public void gpsLocation() {

        if (!checkLocationPermission()) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        } else if (checkIfGPSIsOn()){

            locationAllowed = true;

            if (manager != null && gpsEnabled) {
                for (String provider : manager.getAllProviders()) {
                    if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                            && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                        return;
                    }
                    if (manager.getLastKnownLocation(provider) != null) {
                        latitude = manager.getLastKnownLocation(provider).getLatitude();
                        longitude = manager.getLastKnownLocation(provider).getLongitude();
                    }
                }
                locationsButton.setImageResource(R.drawable.ic_baseline_location_on_24);
            }
        }
    }

    /**
     * Checks if Location is active
     * If Locations is not active, opens settings to activate current location
     */

    private boolean checkIfGPSIsOn() {
        gpsEnabled = manager.isProviderEnabled(LocationManager.GPS_PROVIDER);

        if (!gpsEnabled) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("Your GPS seems to be disabled, do you want to enable it?")
                    .setCancelable(false)
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        public void onClick(final DialogInterface dialog, final int id) {
                            locationDenied = false;
                            Intent settingsIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                            startActivity(settingsIntent);

                        }
                    })
                    .setNegativeButton("No", new DialogInterface.OnClickListener() {
                        public void onClick(final DialogInterface dialog, final int id) {
                            locationsButton.setImageResource(R.drawable.ic_baseline_location_off);
                            locationAllowed = false;
                            locationDenied = true;
                            dialog.cancel();
                        }
                    });
            final AlertDialog alert = builder.create();
            alert.show();
        }

        return gpsEnabled;
    }

    /**
     * Handles the different approaches for image capturing and adding to gallery
     * @param requestCode
     * @param resultCode
     * @param data
     */

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == GalleryRequest && resultCode == RESULT_OK) {

            Uri selectImage = data.getData();
            Bitmap image = Converter.getConverter().convertUriToBitmap(CreateEntryActivity.this, selectImage);

            InputStream imageStream = null;
            try {
                imageStream = getContentResolver().openInputStream(selectImage);
                imageView.setImageBitmap(BitmapFactory.decodeStream(imageStream));
                imageStream.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            imageView.setImageBitmap(image);
            galleryImages.add(image);
            setGalleryRecycler();
        }
        if (requestCode == CameraRequest && resultCode == RESULT_OK) {
            Bundle bundle = data.getExtras();
            Bitmap bitmap = (Bitmap) bundle.get("data");

            imageView.setImageBitmap(bitmap);

            galleryImages.add(bitmap);
            setGalleryRecycler();
        }
    }


    private void setGalleryRecycler() {
        RecyclerView recyclerView = findViewById(R.id.gallery_recycle_view);
        LinearLayoutManager horizontalLayoutManger = new LinearLayoutManager(CreateEntryActivity.this, LinearLayoutManager.HORIZONTAL, false);

        recyclerView.setLayoutManager(horizontalLayoutManger);
        adapter = new RecycleGalleryAdapter(this, galleryImages);
        adapter.setClickListener(this);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onItemClick(View view, int position) {
        imageView.setImageBitmap(galleryImages.get(position));
    }



    @Override
    protected void onRestart() {
        super.onRestart();
        if(!locationDenied)
            gpsLocation();
    }


}