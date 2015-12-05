package com.wirawansanusi.areahp.classes;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.wirawansanusi.areahp.R;
import com.wirawansanusi.areahp.model.ContactIntent;
import com.wirawansanusi.areahp.model.Contacts;

/**
 * Created by wirawansanusi on 12/4/15.
 */
public class ContactsAdapter extends BaseAdapter implements View.OnClickListener {

    private Context mContext;
    private Contacts[] mContacts;

    public ContactsAdapter(Context context, Contacts[] contacts) {
        mContext = context;
        mContacts = contacts;
    }

    @Override
    public int getCount() {
        return mContacts.length;
    }

    @Override
    public Object getItem(int i) {
        return mContacts[i];
    }

    @Override
    public long getItemId(int i) {
        return 0; // We're not using this method
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder viewHolder;

        if (view == null) {
            view = LayoutInflater.from(mContext).inflate(R.layout.list_contact, null);
            viewHolder = new ViewHolder();
            viewHolder.mIcon = (ImageView) view.findViewById(R.id.contactIcon);
            viewHolder.mTitle = (TextView) view.findViewById(R.id.title);

            view.setTag(viewHolder);
            view.setId(i);

        } else {
            viewHolder = (ViewHolder) view.getTag();
        }

        final Contacts contact = mContacts[i];

        viewHolder.mIcon.setImageResource(contact.getIcon());
        viewHolder.mTitle.setText(contact.getTitle());
        view.setOnClickListener(this);

        return view;
    }

    private static class ViewHolder {
        ImageView mIcon;
        TextView mTitle;
    }

    public void onClick(View view) {
        int i = view.getId();
        Contacts contact = mContacts[i];

        switch (contact.getType()) {
            case "maps":
                ContactIntent.showDirection(mContext, contact.getValue());
                break;
            case "phone":
                ContactIntent.callNumber(mContext, contact.getValue());
                break;
            case "bbm":
                ContactIntent.openBBM(mContext, contact.getValue());
                break;
            case "email":
                ContactIntent.composeEmail(mContext, contact.getValue(), "Ask AREAHP");
                break;
            default:
                Toast.makeText(mContext, "Unrecognized Selector Type", Toast.LENGTH_LONG).show();
                break;
        }
    }
}
