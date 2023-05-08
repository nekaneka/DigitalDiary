package com.spba.ediary;

import android.content.Context;
import android.graphics.Bitmap;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.mlkit.vision.common.InputImage;
import com.google.mlkit.vision.label.ImageLabel;
import com.google.mlkit.vision.label.ImageLabeler;
import com.google.mlkit.vision.label.ImageLabeling;
import com.google.mlkit.vision.label.defaults.ImageLabelerOptions;
import com.spba.ediary.controllers.DatabaseController;
import com.spba.ediary.helper.Converter;
import com.spba.ediary.model.EntryImage;

import java.util.List;

/**
 * Class used to run the Google ML Kit for image classification and labelling
 * Code taken from https://github.com/iago001/MLSeriesDemonstrator/
 *
 */

public class ImageAnalyzer {

    public static void runClassification(Context context, String entryId, Bitmap image) {

        InputImage inputImage = InputImage.fromBitmap(image, 0);

        ImageLabeler imageLabeler = ImageLabeling.getClient((new ImageLabelerOptions.Builder().setConfidenceThreshold(0.7f).build()));

        imageLabeler.process(inputImage).addOnSuccessListener(new OnSuccessListener<List<ImageLabel>>() {
            @Override
            public void onSuccess(List<ImageLabel> imageLabels) {
                if (imageLabels.size() > 0) {
                    for (ImageLabel label : imageLabels) {

                        new DatabaseController(context).addCategories(entryId,
                                new EntryImage(label.getText(), label.getConfidence(), Converter.getConverter().bitMapToString(image)));
                    }
                } else {
                    new DatabaseController(context).addCategories(entryId,
                            new EntryImage(Converter.getConverter().bitMapToString(image)));
                    System.out.println("Could not Classify Image");
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

                e.printStackTrace();
            }
        });
    }
}
