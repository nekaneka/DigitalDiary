package com.spba.ediary.fragment;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.spba.ediary.controllers.EntryController;
import com.spba.ediary.intefaces.MultipleChoiceListener;
import java.util.ArrayList;

public class FilterFragment extends DialogFragment {

    private EntryController controller;

    MultipleChoiceListener listener;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            listener = (MultipleChoiceListener) context;
        } catch (Exception e) {
            throw new ClassCastException(getActivity().toString() + " onMultipleChoiceListener must be implemented");
        }
    }

    public FilterFragment(EntryController entryController) {
        this.controller = entryController;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {

        ArrayList<String> selectedCategories = new ArrayList<>();
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        String [] filterList = controller.getAllCategories();

        builder.setTitle("Select Filters")
                .setMultiChoiceItems(filterList, null, new DialogInterface.OnMultiChoiceClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i, boolean b) {
                        if(b){
                            selectedCategories.add(filterList[i]);
                        }else{
                            selectedCategories.remove(filterList[i]);
                        }
                    }
                })
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        listener.positiveButtonClicker(filterList, selectedCategories);
                    }
                }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        listener.negativeButtonClicker();
                    }
                });
        return builder.create();
    }
}


