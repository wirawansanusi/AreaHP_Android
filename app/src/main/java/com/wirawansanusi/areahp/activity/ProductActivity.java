package com.wirawansanusi.areahp.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewStub;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.squareup.okhttp.Callback;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import com.squareup.picasso.Picasso;
import com.wirawansanusi.areahp.R;
import com.wirawansanusi.areahp.model.ProductDetail;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class ProductActivity extends BaseActivity {

    private String mURLString = "http://areahp.com/api/get_post/?post_id=";

    private int mProductId;
    private ProductDetail mProductDetail;

    private boolean mIsDelete = false;
    private ViewStub mLoadingView;
    private ViewStub mFailureView;
    private ScrollView mProductView;

    // Views Bind
    @Bind(R.id.title) TextView mTitle;
    @Bind(R.id.category) TextView mCategory;
    @Bind(R.id.thumbnail) ImageView mThumbnail;
    @Bind(R.id.weight) TextView mWeight;
    @Bind(R.id.warranty) TextView mWarranty;
    @Bind(R.id.additional) TextView mAdditional;
    @Bind(R.id.price) TextView mPrice;
    @Bind(R.id.favoriteButton) Button mFavoriteButton;

    /*
     *
     * TODO: INIT
     *
     */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product);
        ButterKnife.bind(this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_black_18dp);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //What to do on back clicked
                finish();
            }
        });

        Intent intent = getIntent();
        mProductId = intent.getIntExtra("productID", 0);

        performJSONRequest();
    }

    /*
     *
     * TODO: HANDLING JSON REQUEST
     *
     */

    private void performJSONRequest() {

        hideProductView();
        showLoadingView();

        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                                     .url(mURLString + mProductId)
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
                        mProductDetail = getProductDetailFromJSONData(jsonData);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                hideLoadingView();
                                showProductView(mProductDetail);
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

    private ProductDetail getProductDetailFromJSONData(String jsonData) throws JSONException {

        JSONObject productObject = new JSONObject(jsonData);
        JSONObject postObject = productObject.getJSONObject("post");

        int productID = postObject.getInt("id");
        String title = postObject.getString("title");
        String thumbnailURLString = postObject.getString("thumbnail");

        JSONArray categoriesArray = postObject.getJSONArray("categories");
        JSONObject categoryObject = categoriesArray.getJSONObject(0);
        String category = categoryObject.getString("title");

        JSONArray weightsArray = postObject.getJSONArray("taxonomy_berat");
        String weight;
        if(weightsArray.length() > 0) {
            JSONObject weightObject = weightsArray.getJSONObject(0);
            weight = weightObject.getString("title");
        } else {
            weight = "-";
        }

        JSONArray warrantiesArray = postObject.getJSONArray("taxonomy_garansi");
        String warranty;
        if(warrantiesArray.length() > 0) {
            JSONObject warrantyObject = warrantiesArray.getJSONObject(0);
            warranty = warrantyObject.getString("title");
        } else {
            warranty = "-";
        }

        JSONArray additionalsArray = postObject.getJSONArray("taxonomy_paket-pengiriman");
        String additional;
        if(additionalsArray.length() > 0) {
            additional = "";
            for(int i=0; i<additionalsArray.length(); i++){
                JSONObject additionalObject = additionalsArray.getJSONObject(i);
                if(i != additionalsArray.length() - 1) {
                    additional += additionalObject.getString("title") + "\n";
                } else {
                    additional += additionalObject.getString("title");
                }
            }
        } else {
            additional = "-";
        }

        JSONArray pricesArray = postObject.getJSONArray("tags");
        String price;
        if(pricesArray.length() > 0) {
            JSONObject priceObject = pricesArray.getJSONObject(0);
            price = priceObject.getString("title");
        } else {
            price = "-";
        }

        ProductDetail productDetail = new ProductDetail(productID,
                title, category, thumbnailURLString,
                weight, warranty, additional, price);

        return productDetail;
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
     * TODO: HANDLING PRODUCT DETAIL
     *
     */

    private void showProductView(final ProductDetail productDetail) {

        mProductView.setVisibility(View.VISIBLE);

        mTitle.setText(productDetail.getTitle());
        mCategory.setText(productDetail.getCategory());
        Picasso.with(this).load(Uri.parse(productDetail.getThumbnailURLString()))
                .placeholder(R.drawable.sample_grid_item).fit().centerInside().into(mThumbnail);
        mWeight.setText(productDetail.getWeight());
        mWarranty.setText(productDetail.getWarranty());
        mAdditional.setText(productDetail.getAdditional());
        mPrice.setText(productDetail.getPrice());

        // Check if Product Detail has been added into favorite
        List<ProductDetail> productDetailList = ProductDetail.find(ProductDetail.class, "m_product_id = ?", "" + mProductId);
        if (productDetailList.size() < 1){
            mFavoriteButton.setText(getResources().getString(R.string.favorite_button_label_add));
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                mFavoriteButton.setBackgroundColor(getResources().getColor(R.color.textColorPrimary, null));
            } else {
                mFavoriteButton.setBackgroundColor(getResources().getColor(R.color.textColorPrimary));
            }
        } else {
            mFavoriteButton.setText(getResources().getString(R.string.favorite_button_label_remove));
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                mFavoriteButton.setBackgroundColor(getResources().getColor(R.color.textColorError, null));
            } else {
                mFavoriteButton.setBackgroundColor(getResources().getColor(R.color.textColorError));
            }
            mIsDelete = true;
        }
        mFavoriteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mIsDelete == true) {
                    removeProductFromFavorite();
                } else {
                    addProductIntoFavorite();
                }
            }
        });
    }

    private void hideProductView() {

        if( mProductView == null ) mProductView = (ScrollView) findViewById(R.id.productView);
        mProductView.setVisibility(View.INVISIBLE);
    }

    private void addProductIntoFavorite() {

        ProductDetail productDetail = mProductDetail;
        productDetail.save();
        reloadActivity();
    }

    private void removeProductFromFavorite() {

        List<ProductDetail> productDetailList = ProductDetail.find(ProductDetail.class, "m_product_id = ?", "" + mProductId);
        ProductDetail productDetail = productDetailList.get(0);
        productDetail.delete();
        reloadActivity();
    }

    private void reloadActivity() {

        Intent intent = getIntent();
        finish();
        startActivity(intent);
    }
}
