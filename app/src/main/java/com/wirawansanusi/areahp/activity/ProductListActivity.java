package com.wirawansanusi.areahp.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewStub;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;

import com.squareup.okhttp.Callback;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import com.wirawansanusi.areahp.R;
import com.wirawansanusi.areahp.classes.ProductsAdapter;
import com.wirawansanusi.areahp.model.Products;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

public class ProductListActivity extends BaseActivity implements SearchView.OnQueryTextListener {

    private String mURLString = "http://www.areahp.com/api/get_category_posts/?id=";

    private ProductsAdapter mAdapter;
    private int mProductID;
    private ArrayList<Products> mProducts = new ArrayList<Products>();
    private ArrayList<Products> mCurrentProducts = new ArrayList<Products>();

    private ViewStub mLoadingView;
    private ViewStub mFailureView;

    // Views Bind
    @Bind(R.id.productList) GridView mProductList;

    /*
     *
     * TODO: INIT
     *
     */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_list);
        ButterKnife.bind(this);

        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_black_18dp);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //What to do on back clicked
                finish();
            }
        });

        mAdapter = new ProductsAdapter(this, mProducts);
        mProductList.setAdapter(mAdapter);
        mProductList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Products product = mCurrentProducts.get(i);
                int productID = product.getProductId();
                showProductDetail(productID);
            }
        });

        Intent intent = getIntent();
        mProductID = intent.getIntExtra("productID", 0);
        performJSONRequest();
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
                .url(mURLString + mProductID)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {
                final String message = e.getMessage();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        showFailureView(message);
                    }
                });
            }

            @Override
            public void onResponse(Response response) throws IOException {
                try {
                    String jsonData = response.body().string();
                    if (response.isSuccessful()) {
                        mCurrentProducts = getProductsFromJSONRequest(jsonData);
                        mProducts = mCurrentProducts;
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                hideLoadingView();
                                populateProductsIntoListView(mProducts);
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

    private ArrayList<Products> getProductsFromJSONRequest(String jsonData) throws JSONException {

        ArrayList<Products> products = new ArrayList<Products>();

        JSONObject json = new JSONObject(jsonData);
        JSONArray productsJSON = json.getJSONArray("posts");
        for(int i=0; i<productsJSON.length(); i++) {
            JSONObject productJSON = productsJSON.getJSONObject(i);

            int productId = productJSON.getInt("id");
            String title = productJSON.getString("title");
            JSONArray categoryArr = productJSON.getJSONArray("categories");
            JSONObject categoryObj = categoryArr.getJSONObject(0);
            String category = categoryObj.getString("title");
            JSONArray priceArr = productJSON.getJSONArray("tags");
            JSONObject priceObj = categoryArr.getJSONObject(0);
            String price = categoryObj.getString("title");
            String thumbnailURLString = productJSON.getString("thumbnail");

            Products product = new Products(productId, title, category, price, thumbnailURLString);
            products.add(product);
        }

        return products;
    }

    /*
     *
     * TODO: HANDLING CATEGORY LIST
     *
     */

    private void populateProductsIntoListView(ArrayList<Products> products) {

        mAdapter.addAll(products);
    }

    private void showProductDetail(int productID) {

        Intent intent = new Intent(this, ProductActivity.class);
        intent.putExtra("productID", productID);
        startActivity(intent);
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
        android.support.v7.widget.SearchView searchView = (android.support.v7.widget.SearchView) MenuItemCompat.getActionView(searchItem);
        searchView.setOnQueryTextListener(this);

        return true;
    }

    /*
     *
     * TODO: HANDLING SEARCH KEYWORDS
     *
     */

    @Override
    public boolean onQueryTextChange(String newText) {

        mCurrentProducts = filter(newText);
        mAdapter.clear();
        mAdapter.addAll(mCurrentProducts);
        mAdapter.setNotifyOnChange(true);

        return false;
    }

    @Override
    public boolean onQueryTextSubmit(String query) {

        return false;
    }

    public ArrayList<Products> filter(String predicate) {
        ArrayList<Products> categories = mProducts;
        ArrayList<Products> result = new ArrayList<Products>();
        for (Products product: categories) {
            if (product.getTitle().toLowerCase().contains(predicate)) {
                result.add(product);
            }
        }
        return result;
    }
}
