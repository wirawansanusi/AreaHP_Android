package com.wirawansanusi.areahp.classes;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.wirawansanusi.areahp.R;
import com.wirawansanusi.areahp.model.Products;

import java.util.ArrayList;

/**
 * Created by wirawansanusi on 11/9/15.
 */
public class ProductsAdapter extends ArrayAdapter<Products> {

    private Context mContext;

    public ProductsAdapter(Context context, ArrayList<Products> products) {
        super(context, 0, products);
        mContext = context;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        Products product = getItem(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.grid_item_product, parent, false);
        }

        TextView title = (TextView) convertView.findViewById(R.id.productTitle);
        TextView price = (TextView) convertView.findViewById(R.id.productPrice);
        ImageView thumbnailImage = (ImageView) convertView.findViewById(R.id.productThumbnail);

        title.setText(product.getTitle());
        price.setText(product.getPrice());
        Picasso.with(getContext()).setLoggingEnabled(true);
        Picasso.with(getContext()).load(Uri.parse(product.getURLString()))
                .placeholder(R.drawable.sample_grid_item)
                .fit().centerInside()
                .into(thumbnailImage);

        return convertView;
    }
}