package com.wirawansanusi.areahp.activity;

import android.content.Intent;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewStub;
import android.widget.AdapterView;
import android.widget.GridView;

import com.wirawansanusi.areahp.R;
import com.wirawansanusi.areahp.classes.ProductsAdapter;
import com.wirawansanusi.areahp.model.ProductDetail;
import com.wirawansanusi.areahp.model.Products;

import java.util.ArrayList;
import java.util.Iterator;

import butterknife.Bind;
import butterknife.ButterKnife;

public class FavoriteActivity extends BaseActivity implements SearchView.OnQueryTextListener {

    private final static String TAG = ProductListActivity.class.getSimpleName();

    private ProductsAdapter mAdapter;
    private int mProductID;
    private ArrayList<ProductDetail> mProductDetails = new ArrayList<ProductDetail>();
    private ArrayList<ProductDetail> mCurrentProductDetails = new ArrayList<ProductDetail>();
    private ArrayList<Products> mCurrentProducts = new ArrayList<Products>();

    // Views Bind
    @Bind(R.id.favoriteList) GridView mFavoriteList;
    @Bind(android.R.id.empty) ViewStub mEmptyView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorite);
        ButterKnife.bind(this);

        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mAdapter = new ProductsAdapter(this, mCurrentProducts);
        mFavoriteList.setAdapter(mAdapter);
        mFavoriteList.setEmptyView(mEmptyView);
        mFavoriteList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Products product = mCurrentProducts.get(i);
                int productID = product.getProductId();
                showProductDetail(productID);
            }
        });

        fetchProductFromFavorite();
    }

    private void fetchProductFromFavorite() {

        ArrayList<Products> productList = new ArrayList<Products>();
        ArrayList<ProductDetail> productDetails = new ArrayList<ProductDetail>();
        Iterator<ProductDetail> productDetailList = ProductDetail.findAll(ProductDetail.class);
        while( productDetailList.hasNext() ){
            ProductDetail productDetail = productDetailList.next();
            productDetails.add(productDetail);

            Products product = new Products(productDetail.getProductId(),
                                            productDetail.getTitle(),
                                            productDetail.getCategory(),
                                            productDetail.getPrice(),
                                            productDetail.getThumbnailURLString());
            productList.add(product);
        }
        mCurrentProducts = productList;
        mProductDetails = productDetails;
        mCurrentProductDetails = productDetails;
        validateProductList();
    }

    private void showProductDetail(int productID) {

        Intent intent = new Intent(this, ProductActivity.class);
        intent.putExtra("productID", productID);
        startActivity(intent);
    }

    private void validateProductList() {

        if( mAdapter.getCount() > 0 ) mAdapter.clear();

        if( mCurrentProducts.size() > 0 ) {
            mAdapter.addAll(mCurrentProducts);

        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);

        MenuItem searchItem = menu.findItem(R.id.action_search);
        android.support.v7.widget.SearchView searchView = (android.support.v7.widget.SearchView) MenuItemCompat.getActionView(searchItem);
        searchView.setOnQueryTextListener(this);

        return true;
    }

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
        ArrayList<ProductDetail> productDetails = mProductDetails;
        ArrayList<ProductDetail> currentProductDetails = new ArrayList<ProductDetail>();
        ArrayList<Products> productList = new ArrayList<Products>();
        for (ProductDetail productDetail: productDetails) {
            if (productDetail.getTitle().toLowerCase().contains(predicate)) {
                currentProductDetails.add(productDetail);

                Products product = new Products(productDetail.getProductId(),
                                                productDetail.getTitle(),
                                                productDetail.getCategory(),
                                                productDetail.getPrice(),
                                                productDetail.getThumbnailURLString());
                productList.add(product);
            }
        }

        mCurrentProductDetails = currentProductDetails;
        return productList;
    }

    @Override
    protected void onResume() {
        super.onResume();

        fetchProductFromFavorite();
    }
}
