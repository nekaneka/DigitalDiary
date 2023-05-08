package com.spba.ediary;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.spba.ediary.controllers.DatabaseController;
import com.spba.ediary.helper.Converter;
import com.spba.ediary.model.Entry;

import java.util.ArrayList;
import java.util.List;


/**
 * Test Data initialization class
 */
public class TestDataInitialisation {

    private DatabaseController databaseController;

    private String testDescription = "Lorem ipsum dolor sit amet, consetetur sadipscing elitr, sed diam nonumy eirmod tempor" +
            " invidunt ut labore et dolore magna aliquyam erat, sed diam voluptua. At vero eos et accusam et justo duo dolores " +
            "et ea rebum. Stet clita kasd gubergren, no sea takimata sanctus est Lorem ipsum dolor sit amet. Lorem ipsum dolor " +
            "sit amet, consetetur sadipscing elitr, sed diam nonumy eirmod tempor invidunt ut labore et dolore magna aliquyam erat," +
            " sed diam voluptua. At vero eos et accusam et justo duo dolores et ea rebum. Stet clita kasd gubergren, no sea takimata " +
            "sanctus est Lorem ipsum dolor sit amet.";


    Context context;

    public TestDataInitialisation(Context context) {
        this.context = context;
        databaseController = DatabaseController.getDatabaseController();
    }

    public void initialiseData() {


        Entry entry = new Entry("27/01/2023", "Test Entry", true, testDescription, 48.2050491798, 16.3701485194);
        List<Bitmap> galleryImages = new ArrayList<>();
        galleryImages.add(BitmapFactory.decodeResource(context.getResources(),R.drawable.stephansdome));
        galleryImages.add(BitmapFactory.decodeResource(context.getResources(), R.drawable.burger));
        galleryImages.add(BitmapFactory.decodeResource(context.getResources(), R.drawable.biljka));


        databaseController.addNewEntry(entry);

        for (Bitmap image : galleryImages) {

            ImageAnalyzer.runClassification(context, entry.getUID(), Converter.resizeBitmap(image));
            //  ((Runnable) () -> ImageAnalyzerClient.runClassification(context, entry2.getUID(), Converter.resizeBitmap(image2))).run();
        }



        Entry entry2 = new Entry("30/12/2022", "Vienna Food", false, testDescription, 0.0, 0.0);
        List<Bitmap> galleryImages2 = new ArrayList<>();
        galleryImages2.add(BitmapFactory.decodeResource(context.getResources(),R.drawable.croisants));
        galleryImages2.add(BitmapFactory.decodeResource(context.getResources(), R.drawable.burger));


        databaseController.addNewEntry(entry2);

        for (Bitmap image2 : galleryImages2) {

            ImageAnalyzer.runClassification(context, entry2.getUID(), Converter.resizeBitmap(image2));
          //  ((Runnable) () -> ImageAnalyzerClient.runClassification(context, entry2.getUID(), Converter.resizeBitmap(image2))).run();
        }




        Entry entry3 = new Entry("06/02/2023", "Karlsplatz", true, testDescription, 48.192832562, 16.368665192);
        List<Bitmap> galleryImages3 = new ArrayList<>();
        galleryImages3.add(BitmapFactory.decodeResource(context.getResources(),R.drawable.karlsplatz));
        galleryImages3.add(BitmapFactory.decodeResource(context.getResources(), R.drawable.karlsplatz_instrument));


        databaseController.addNewEntry(entry3);

        for (Bitmap image : galleryImages3) {

            ImageAnalyzer.runClassification(context, entry3.getUID(), Converter.resizeBitmap(image));
            //  ((Runnable) () -> ImageAnalyzerClient.runClassification(context, entry2.getUID(), Converter.resizeBitmap(image2))).run();
        }

    }
}
