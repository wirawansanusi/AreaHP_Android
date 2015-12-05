package com.wirawansanusi.areahp.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewStub;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.squareup.okhttp.Callback;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import com.wirawansanusi.areahp.R;
import com.wirawansanusi.areahp.classes.CategoriesAdapter;
import com.wirawansanusi.areahp.model.Categories;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

public class CategoryActivity extends BaseActivity implements SearchView.OnQueryTextListener {

    private final static String TAG = CategoryActivity.class.getSimpleName();

    private String mURL = "http://areahp.com/api/get_category_index/";

    private CategoriesAdapter mAdapter;
    private ArrayList<Categories> mCategories = new ArrayList<Categories>();
    private ArrayList<Categories> mCurrentCategories = new ArrayList<Categories>();

    private ViewStub mLoadingView;
    private ViewStub mFailureView;

    // Views Bind
    @Bind(R.id.categoryList) ListView mCategoryList;

    /*
     *
     * TODO: INIT
     *
     */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mAdapter = new CategoriesAdapter(this, mCategories);
        mCategoryList.setAdapter(mAdapter);
        mCategoryList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Categories category = mCurrentCategories.get(i);
                int parentId = category.getId();
                showSubCategory(parentId);
            }
        });

        if(getIntent().getSerializableExtra("categoriesArrayList") == null) {
            performJSONRequest();

        } else {

            toolbar.setNavigationIcon(R.drawable.ic_arrow_back_black_18dp);
            toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //What to do on back clicked
                    finish();
                }
            });
            Intent intent = getIntent();
            mCategories = (ArrayList<Categories>)intent.getSerializableExtra("categoriesArrayList");
            ArrayList<Categories> categoriesArrayList = (ArrayList<Categories>)intent.getSerializableExtra("currentCategoriesArrayList");
            populateCategoriesIntoListView(categoriesArrayList, true);
        }
    }

    /*
     *
     * TODO: HANDLING JSON REQUEST
     *
     */

    private void performJSONRequest() {

        showLoadingView();

        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(mURL)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {
                final String message = e.getMessage();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        hideLoadingView();
                        showFailureView(message);
                    }
                });
            }

            @Override
            public void onResponse(Response response) throws IOException {
                try {
                    String jsonData = response.body().string();
                    if (response.isSuccessful()) {
                        mCategories = getCategoriesFromJSONRequest(jsonData);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                hideLoadingView();
                                populateCategoriesIntoListView(mCategories, false);
                            }
                        });
                    }

                } catch (IOException e) {
                    final String message = e.getMessage();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            showFailureView(message);
                        }
                    });

                } catch (JSONException e) {
                    final String message = e.getMessage();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            showFailureView(message);
                        }
                    });
                }
            }
        });
    }

    private ArrayList<Categories> getCategoriesFromJSONRequest(String jsonData) throws JSONException {

        ArrayList<Categories> categories = new ArrayList<Categories>();

        JSONObject json = new JSONObject(jsonData);
        JSONArray categoriesJSON = json.getJSONArray("categories");
        for(int i=0; i<categoriesJSON.length(); i++) {
            JSONObject categoryJSON = categoriesJSON.getJSONObject(i);

            int id = categoryJSON.getInt("id");
            int parentId = categoryJSON.getInt("parent");
            String title = categoryJSON.getString("title");
            String slug = categoryJSON.getString("slug");

            Categories category = new Categories(id, parentId, title, slug);
            categories.add(category);
        }

        return categories;
    }

    /*
     *
     * TODO: HANDLING CATEGORY LIST
     *
     */

    protected void populateCategoriesIntoListView(ArrayList<Categories> categories, boolean isSubCategory) {

        Categories[] categoriesArray = new Categories[categories.size()];
        categoriesArray = categories.toArray(categoriesArray);
        for(int i=0; i<categoriesArray.length; i++){
            if(categoriesArray[i].getParentId() == 0 || isSubCategory == true) {
                Categories category = categoriesArray[i];
                mCurrentCategories.add(category);
            }
        }
        mAdapter.addAll(mCurrentCategories);
    }

    protected void showSubCategory(int parentID) {

        ArrayList<Categories> categoriesArrayList = new ArrayList<Categories>();
        Categories[] categories = new Categories[mCategories.size()];
        categories = mCategories.toArray(categories);
        for (int i = 0; i < categories.length; i++) {
            if (categories[i].getParentId() == parentID) {
                Categories category = categories[i];
                categoriesArrayList.add(category);
            }
        }

        if( categoriesArrayList.size() > 0) {
            Intent intent = new Intent(this, CategoryActivity.class);
            intent.putExtra("categoriesArrayList", mCategories);
            intent.putExtra("currentCategoriesArrayList", categoriesArrayList);
            startActivity(intent);

        } else {
            Intent intent = new Intent(this, ProductListActivity.class);
            intent.putExtra("productID", parentID);
            startActivity(intent);
        }
    }

    /*
     *
     * TODO: HANDLING ERROR & LOADING VIEWS
     *
     */

    private void showLoadingView() {

        if (mLoadingView == null) mLoadingView = (ViewStub) findViewById(R.id.loadingView);
        if (mLoadingView.getParent() != null) {
            mLoadingView.inflate();

        } else {
            mLoadingView.setVisibility(View.VISIBLE);
        }
    }

    private void hideLoadingView() {

        mLoadingView.setVisibility(View.INVISIBLE);
    }

    private void showFailureView(String message) {

        hideLoadingView();

        if (mFailureView == null) mFailureView = (ViewStub) findViewById(R.id.failureView);
        if (mFailureView.getParent() != null) {
            mFailureView.inflate();

        } else {
            mFailureView.setVisibility(View.VISIBLE);
        }

        TextView failureTitle = (TextView) findViewById(R.id.failureView_title);
        failureTitle.setText(message);

        Button failureButton = (Button) findViewById(R.id.failureView_action);
        failureButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                hideFailureView();
                showLoadingView();
                performJSONRequest();
            }
        });
    }

    private void hideFailureView() {

        mFailureView.setVisibility(View.INVISIBLE);
    }

    /*
     *
     * TODO: CUSTOMIZING TOOLBAR OPTIONS
     *
     */

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);

        MenuItem searchItem = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) searchItem.getActionView();
        searchView.setOnQueryTextListener(this);
        MenuItemCompat.setOnActionExpandListener(searchItem, new MenuItemCompat.OnActionExpandListener() {

            // Called when search view will begin to take input
            @Override
            public boolean onMenuItemActionExpand(MenuItem menuItem) {
                return true;
            }

            // Called when search view resign its first responder
            @Override
            public boolean onMenuItemActionCollapse(MenuItem menuItem) {
                mCurrentCategories.clear();
                mAdapter.clear();
                populateCategoriesIntoListView(mCategories, false);
                mAdapter.setNotifyOnChange(true);
                return true;
            }
        });

        return true;
    }

    /*
     *
     * TODO: HANDLING SEARCH KEYWORDS
     *
     */

    @Override
    public boolean onQueryTextChange(String newText) {

        mCurrentCategories = filter(newText);
        mAdapter.clear();
        mAdapter.addAll(mCurrentCategories);
        mAdapter.setNotifyOnChange(true);

        return false;
    }

    @Override
    public boolean onQueryTextSubmit(String query) {

        return false;
    }

    public ArrayList<Categories> filter(String predicate) {
        ArrayList<Categories> categories = mCategories;
        ArrayList<Categories> result = new ArrayList<Categories>();
        for (Categories category: categories) {
            if (category.getTitle().toLowerCase().contains(predicate)) {
                result.add(category);
            }
        }
        return result;
    }
}
