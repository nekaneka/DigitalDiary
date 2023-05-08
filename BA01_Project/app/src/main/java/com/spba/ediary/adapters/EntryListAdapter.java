package com.spba.ediary.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.spba.ediary.R;
import com.spba.ediary.helper.Converter;
import com.spba.ediary.model.Entry;

import java.util.List;


/**
 * Adapter user for listing of Entries in listViews
 * Sets data to one item in the list of entries
 */
public class EntryListAdapter extends ArrayAdapter<Entry> {

    private Context context;
    private int resource;


    public EntryListAdapter(@NonNull Context context, int resource, List<Entry> entries) {
        super(context, resource, entries);

        this.context = context;
        this.resource = resource;

    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        String title = getItem(position).getTitle();
        String image = getItem(position).getFirstImage();
        String description = getItem(position).getDescription();
        String date = getItem(position).getCreationDate();

        LayoutInflater layoutInflater = LayoutInflater.from(context);
        convertView = layoutInflater.inflate(resource, parent, false);

        TextView titleTextView = (TextView) convertView.findViewById(R.id.textView);
        ImageView imageView = (ImageView) convertView.findViewById(R.id.imageView);
        TextView descriptionTextView = (TextView) convertView.findViewById(R.id.textView2);
        TextView dateView = convertView.findViewById(R.id.list_date);

        titleTextView.setText(title);
        imageView.setImageBitmap(Converter.stringToBitmap(image));
        descriptionTextView.setText(description);
        dateView.setText(date);

        return convertView;


    }

 }

