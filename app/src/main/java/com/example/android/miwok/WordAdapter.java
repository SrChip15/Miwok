package com.example.android.miwok;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

class WordAdapter extends ArrayAdapter<Word> {

    /**
     * Layout inflater to inflate custom layout
     */
    private LayoutInflater mInflater;

    /**
     * ArrayList for Word object
     */
    private ArrayList<Word> mList;

    /**
     * Create new adapter
     */
    WordAdapter(Context context, ArrayList<Word> words) {
        // Handle ArrayAdapter (superclass) instantiation
        // Most suitable public constructor - ArrayAdapter(Context context, int resource, List<T> objects)
        super(context, 0, words); // @Param resource: int: The resource ID for a layout file containing a TextView to use when instantiating views.
        // Zero for resource ensures that the Layout inflation is not handled by the ArrayAdapter

        // Store LayoutInflater from the context
        mInflater = LayoutInflater.from(context);
        // Store the ArrayList object
        mList = words;
    }

    @NonNull
    @Override
    // Provide custom view implementation and inflate view
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            // No views in the recycle view pool
            // Inflate view for first time view render
            convertView = mInflater.inflate(R.layout.list_item, parent, false);
        }
        // Set text to miwok_text_view
        TextView miwokTextView = (TextView) convertView.findViewById(R.id.miwok_text_view);
        miwokTextView.setText(mList.get(position).getMiwokTranslation());

        // Set text to english_text_view
        TextView defaultTextView = (TextView) convertView.findViewById(R.id.english_text_view);
        defaultTextView.setText(mList.get(position).getDefaultTranslation());

        return convertView;
    }
}
