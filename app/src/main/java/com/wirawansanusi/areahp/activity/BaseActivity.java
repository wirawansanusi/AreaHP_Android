package com.wirawansanusi.areahp.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.wirawansanusi.areahp.R;

public class BaseActivity extends AppCompatActivity {

    protected String CURRENTACTIVITY = this.getClass().getSimpleName();
    protected String CATEGORYACTIVITY = CategoryActivity.class.getSimpleName();
    protected String FAVORITEACTIVITY = FavoriteActivity.class.getSimpleName();
    protected String CONTACTACTIVITY = ContactActivity.class.getSimpleName();
    protected String PRODUCTACTIVITY = ProductActivity.class.getSimpleName();
    protected String PRODUCTLISTACTIVITY = ProductListActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    /*
     *
     * TODO: CUSTOMIZING TOOLBAR OPTIONS
     *
     */

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.toolbar_menu, menu);

        if (CURRENTACTIVITY.equalsIgnoreCase(CATEGORYACTIVITY) ||
            CURRENTACTIVITY.equalsIgnoreCase(PRODUCTACTIVITY) ||
            CURRENTACTIVITY.equalsIgnoreCase(PRODUCTLISTACTIVITY)) {

            MenuItem categoryItem = menu.findItem(R.id.action_category);
            categoryItem.setVisible(false);
        } else if (CURRENTACTIVITY.equalsIgnoreCase(FAVORITEACTIVITY)) {

            MenuItem favoriteItem = menu.findItem(R.id.action_favorite);
            favoriteItem.setVisible(false);
        } else if (CURRENTACTIVITY.equalsIgnoreCase(CONTACTACTIVITY)) {

            MenuItem contactItem = menu.findItem(R.id.action_contact);
            contactItem.setVisible(false);
        }

        if (CURRENTACTIVITY.equalsIgnoreCase(PRODUCTLISTACTIVITY) ||
                CURRENTACTIVITY.equalsIgnoreCase(CONTACTACTIVITY)) {

            MenuItem searchItem = menu.findItem(R.id.action_search);
            searchItem.setVisible(false);
        }

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case R.id.action_favorite:

                if (CURRENTACTIVITY.equalsIgnoreCase(CONTACTACTIVITY)) {
                    finish();
                }

                Intent favoriteIntent = new Intent(this, FavoriteActivity.class);
                startActivity(favoriteIntent);
                return true;


            case R.id.action_contact:

                if (CURRENTACTIVITY.equalsIgnoreCase(FAVORITEACTIVITY)) {
                    finish();
                }

                Intent contactIntent = new Intent(this, ContactActivity.class);
                startActivity(contactIntent);
                return true;


            case R.id.action_category:

                finish();
                return true;

            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);

        }
    }
}
