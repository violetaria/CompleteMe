package com.getlosthere.completeme;

import android.content.Context;
import android.text.Spannable;
import android.text.Spanned;
import android.text.style.StrikethroughSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by violetaria on 6/12/16.
 */
public class ItemsAdapter extends ArrayAdapter<Item> {
    public ItemsAdapter(Context context, ArrayList<Item> items) {
        super(context, 0, items);
    }
    private static final StrikethroughSpan STRIKE_THROUGH_SPAN = new StrikethroughSpan();

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        Item item = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item, parent, false);
        }
        // Lookup view for data population
        TextView tvItem = (TextView) convertView.findViewById(R.id.tvItem);

        // Populate the data into the template view using the data object
//        tvItem.setText(item.text);
        tvItem.setText(item.text, TextView.BufferType.SPANNABLE);

        if (item.completed){
            Spannable spannable = (Spannable) tvItem.getText();
            spannable.setSpan(STRIKE_THROUGH_SPAN, 0, item.text.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        }

        // Return the completed view to render on screen
        return convertView;
    }
}
