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

import org.w3c.dom.Text;

import java.util.ArrayList;

/**
 * Created by violetaria on 6/12/16.
 */
public class ItemsAdapter extends ArrayAdapter<Object> {
    public ItemsAdapter(Context context, ArrayList<Object> items) {
        super(context, 0, items);
    }

    private static final StrikethroughSpan STRIKE_THROUGH_SPAN = new StrikethroughSpan();
    private static final int ITEM_VIEW_TYPE_TODO = 0;
    private static final int ITEM_VIEW_TYPE_SEPARATOR = 1;
    private static final int ITEM_VIEW_TYPE_COUNT = 2;

    @Override
    public int getItemViewType(int position) {
        if (getItem(position) instanceof Item){
            return ITEM_VIEW_TYPE_TODO;
        }else{
            return ITEM_VIEW_TYPE_SEPARATOR;
        }
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        // Get the data item for this position
        Object dataItem = getItem(position);

        if (dataItem instanceof Item){
            Item item = (Item) dataItem;
            // Check if an existing view is being reused, otherwise inflate the view
            if (convertView == null) {
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.item, parent, false);
            }
            // Lookup view for data population
            TextView tvItem = (TextView) convertView.findViewById(R.id.tvItem);

            // Populate the data into the template view using the data object
            tvItem.setText(item.text, TextView.BufferType.SPANNABLE);

            if (item.completed) {
                Spannable spannable = (Spannable) tvItem.getText();
                spannable.setSpan(STRIKE_THROUGH_SPAN, 0, item.text.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
        }
        else {
            SectionHeader sectionHeader = (SectionHeader) dataItem;
            if (convertView == null) {
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.section_header, parent, false);
                TextView tvSectionHeader = (TextView) convertView.findViewById(R.id.tvSectionHeader);

                tvSectionHeader.setText(sectionHeader.header_text);
            }
        }

        // Return the completed view to render on screen
        return convertView;
    }

    @Override
    public int getViewTypeCount() {
        return ITEM_VIEW_TYPE_COUNT;
    }

    @Override
    public boolean isEnabled(int position) {
        // A separator cannot be clicked !
        return getItemViewType(position) != ITEM_VIEW_TYPE_SEPARATOR;
    }

}
