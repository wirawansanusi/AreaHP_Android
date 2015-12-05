package com.wirawansanusi.areahp.classes;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.wirawansanusi.areahp.R;
import com.wirawansanusi.areahp.model.Categories;

import java.util.ArrayList;


/**
 * Created by wirawansanusi on 11/6/15.
 */
public class CategoriesAdapter extends ArrayAdapter<Categories> {

    private Context mContext;

    public CategoriesAdapter(Context context, ArrayList<Categories> categories) {
        super(context, 0, categories);
        mContext = context;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        Categories category = getItem(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.list_item_category, parent, false);
        }

        TextView title = (TextView) convertView.findViewById(R.id.titleText);
        title.setText(category.getTitle());

        return convertView;
    }

}
