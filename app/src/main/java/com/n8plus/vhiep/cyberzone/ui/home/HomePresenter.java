package com.n8plus.vhiep.cyberzone.ui.home;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.bignerdranch.expandablerecyclerview.Model.ParentObject;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.n8plus.vhiep.cyberzone.R;
import com.n8plus.vhiep.cyberzone.data.model.Account;
import com.n8plus.vhiep.cyberzone.data.model.Category;
import com.n8plus.vhiep.cyberzone.data.model.Customer;
import com.n8plus.vhiep.cyberzone.data.model.Filter;
import com.n8plus.vhiep.cyberzone.data.model.Overview;
import com.n8plus.vhiep.cyberzone.data.model.Product;
import com.n8plus.vhiep.cyberzone.data.model.ProductImage;
import com.n8plus.vhiep.cyberzone.data.model.ProductPurchase;
import com.n8plus.vhiep.cyberzone.data.model.ProductType;
import com.n8plus.vhiep.cyberzone.data.model.ReviewProduct;
import com.n8plus.vhiep.cyberzone.data.model.SaleOff;
import com.n8plus.vhiep.cyberzone.data.model.Specification;
import com.n8plus.vhiep.cyberzone.data.model.Store;
import com.n8plus.vhiep.cyberzone.ui.home.adapter.PopularCategoryAdapter;
import com.n8plus.vhiep.cyberzone.ui.home.adapter.ProductHorizontalAdapter;
import com.n8plus.vhiep.cyberzone.util.Constant;
import com.n8plus.vhiep.cyberzone.util.MySingleton;
import com.n8plus.vhiep.cyberzone.util.TypeLoad;
import com.n8plus.vhiep.cyberzone.util.VolleyUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.TimeZone;

public class HomePresenter implements HomeContract.Presenter {
    private final String TAG = "HomePresenter";
    private final int LIST_BEST_SELLER = 1, LIST_ON_SALE = 2, LIST_SUGGESTION = 3;
    private Context context;
    private final HomeContract.View mHomeView;
    private List<ProductPurchase> productPurchases;
    private List<Product> productsBestSeller, productsSuggestion, productsOnSale, mProductListTemp;
    private List<ProductType> productTypes;
    private Date mCurentTime;
    private Gson gson;
    private int fromIndex = 0, toIndex = 9;
    private int mCurrentPage = 1, mPages = 1;

    public HomePresenter(@NonNull final Context context, @NonNull final HomeContract.View mHomeView) {
        this.context = context;
        this.mHomeView = mHomeView;
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.setDateFormat("M/d/yy hh:mm a");
        gson = gsonBuilder.create();
    }

    @Override
    public void loadData() {
        loadCurrentTime();
        loadAllProductType();
    }

    @Override
    public void loadCurrentTime() {
        VolleyUtil.GET(context, Constant.URL_TIME,
                response -> {
                    try {
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
                        sdf.setTimeZone(TimeZone.getTimeZone("Asia/Ho_Chi_Minh"));
                        mCurentTime = sdf.parse(response.getString("time"));
                        Log.d("HomePresenter", "Current time: " + sdf.format(mCurentTime).toString());
                        // Load data
                        loadProductBestSeller();
                        loadProductOnSale();
                        loadProductSuggestion();
                    } catch (ParseException | JSONException e) {
                        e.printStackTrace();
                    }
                },
                error -> Log.e(TAG, error.toString()));
    }

    @Override
    public void loadProductBestSeller() {
        productsBestSeller = new ArrayList<>();
        VolleyUtil.GET(context, Constant.URL_ORDER_ITEM + "/productPurchase",
                response -> {
                    try {
                        productPurchases = Arrays.asList(gson.fromJson(String.valueOf(response.getJSONArray("productPurchases")), ProductPurchase[].class));
                        Log.i("HomePresenter", "GET: " + productPurchases.size() + " productPurchases");

                        if (!productPurchases.isEmpty()) {
                            for (int i = 0; i < productPurchases.size(); i++) {
                                final int index = i;
                                // Load product by id
                                VolleyUtil.GET(context, Constant.URL_PRODUCT + "/" + productPurchases.get(index).getProductId(),
                                        response1 -> {
                                            try {
                                                Product product = gson.fromJson(String.valueOf(response1.getJSONObject("product")), Product.class);

                                                if (product != null) {
                                                    productsBestSeller.add(product);
                                                    mHomeView.setAdapterBestSeller(productsBestSeller);
                                                    Log.i(TAG, "productsBestSeller: " + productsBestSeller.size());

                                                    if (!checkOnSale(productsBestSeller.get(productsBestSeller.indexOf(product)))) {
                                                        productsBestSeller.get(productsBestSeller.indexOf(product)).getSaleOff().setDiscount(0);
                                                    }
                                                    loadProductImage(LIST_BEST_SELLER, productsBestSeller.indexOf(product));
                                                    loadProductReview(LIST_BEST_SELLER, productsBestSeller.indexOf(product));
                                                }
                                            } catch (JSONException e) {
                                                e.printStackTrace();
                                            }
                                        },
                                        error -> {
                                            Log.e(TAG, error.toString());
                                        });
                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                },
                error -> Log.e(TAG, error.toString()));
    }

    @Override
    public void loadProductOnSale() {
        VolleyUtil.GET(context, Constant.URL_PRODUCT + "/onSale",
                jsonObject -> {
                    Log.d(TAG, jsonObject.toString());
                    try {
                        productsOnSale = new ArrayList<>(Arrays.asList(gson.fromJson(String.valueOf(jsonObject.getJSONArray("products")), Product[].class)));
                        if (productsOnSale.size() > 0) {
                            Log.d(TAG, "productsOnSaleSize: " + productsOnSale.size());
                            Iterator<Product> iterator = productsOnSale.iterator();
                            while (iterator.hasNext()) {
                                Product next = iterator.next();
                                if (!checkOnSale(next)) {
                                    iterator.remove();
                                }
                            }
                            // Load image, review
                            mHomeView.setAdapterOnSale(productsOnSale);
                            for (int i = 0; i < productsOnSale.size(); i++) {
                                loadProductImage(LIST_ON_SALE, i);
                                loadProductReview(LIST_ON_SALE, i);
                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                },
                error -> Log.e(TAG, error.toString()));
    }

    @Override
    public void loadProductSuggestion() {
        VolleyUtil.GET(context, Constant.URL_PRODUCT + "/page/" + mCurrentPage,
                response -> {
                    try {
                        mCurrentPage = Integer.valueOf(response.getString("current"));
                        mPages = Integer.valueOf(response.getString("pages"));
                        productsSuggestion = new ArrayList<Product>(Arrays.asList(gson.fromJson(String.valueOf(response.getJSONArray("products")), Product[].class)));
                        Log.d(TAG, "SuggestionSize: " + productsSuggestion.size() + ", current: " + mCurrentPage + ", pages: " + mPages);

                        if (productsSuggestion.size() > 0) {
                            for (int i = 0; i < productsSuggestion.size(); i++) {
                                if (!checkOnSale(productsSuggestion.get(i))) {
                                    productsSuggestion.get(i).setSaleOff(null);
                                }
                                loadProductImage(LIST_SUGGESTION, i);
                                loadProductReview(LIST_SUGGESTION, i);

                                if (i == productsSuggestion.size() - 1) {
                                    mHomeView.setAdapterSuggestion(productsSuggestion);
                                }
                            }
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                },
                error -> Log.e(TAG, error.toString()));
    }

    @Override
    public void loadMoreSuggestion() {
        mCurrentPage++;
        if (mCurrentPage <= mPages) {
            mProductListTemp = new ArrayList<>();
            final int sizeOld = productsSuggestion.size();
            String URL_LOAD_MORE = Constant.URL_PRODUCT + "/page/" + mCurrentPage;
            VolleyUtil.GET(context, URL_LOAD_MORE,
                    response -> {
                        Log.d(TAG, response.toString());
                        try {
                            mProductListTemp = Arrays.asList(gson.fromJson(String.valueOf(response.getJSONArray("products")), Product[].class));

                            if (mProductListTemp.size() > 0) {
                                productsSuggestion.addAll(mProductListTemp);
                                mHomeView.setAdapterSuggestion(productsSuggestion);
                                Log.d(TAG, "SizeOld: " + sizeOld + ", SizeNew: " + productsSuggestion.size());

                                if (productsSuggestion.size() > sizeOld) {
                                    for (int i = sizeOld; i < productsSuggestion.size(); i++) {
                                        if (!checkOnSale(productsSuggestion.get(i))) {
                                            productsSuggestion.get(i).getSaleOff().setDiscount(0);
                                        }
                                        loadProductReview(LIST_SUGGESTION, i);
                                        loadProductImage(LIST_SUGGESTION, i);
                                    }
                                }
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    },
                    error -> Log.e(TAG, error.toString()));
        } else {
            Log.d(TAG, "OnReachEnd");
        }
    }

    @Override
    public void refreshData() {
        loadData();
        mHomeView.setRefreshing(false);
    }


    @Override
    public void loadAllProductType() {
        VolleyUtil.GET(context, Constant.URL_PRODUCT_TYPE,
                response -> {
                    Log.d(TAG, response.toString());
                    try {
                        productTypes = Arrays.asList(gson.fromJson(String.valueOf(response.getJSONArray("productTypes")), ProductType[].class));
                        Log.i("HomePresenter", "Product type: " + productTypes.size());
                        mHomeView.setAdapterPopularCategory(productTypes.subList(fromIndex, toIndex));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                },
                error -> Log.e(TAG, error.toString()));
    }

    @Override
    public void loadDataCustomer(final String accountId) {
        VolleyUtil.GET(context, Constant.URL_CUSTOMER + "/account/" + accountId,
                response -> {
                    try {
                        Customer customer = gson.fromJson(String.valueOf(response.getJSONObject("customer")), Customer.class);
                        Log.i("HomePresenter", "Customer: " + customer.getName());
                        if (customer != null) {
                            customer.setAccount(new Account(accountId));
                            Constant.customer = customer;
                            mHomeView.setNameCustomer(Constant.customer.getName());
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                },
                error -> Log.e(TAG, error.toString()));
    }

    @Override
    public void refreshPopularCategory() {
        if (toIndex == productTypes.size()) {
            fromIndex = 0;
            toIndex = 9;
        } else {
            fromIndex = fromIndex + 9 > productTypes.size() ? productTypes.size() : fromIndex + 9;
            toIndex = toIndex + 9 > productTypes.size() ? productTypes.size() : toIndex + 9;
        }
        mHomeView.setAdapterPopularCategory(productTypes.subList(fromIndex, toIndex));
    }

    @Override
    public void prepareDataSuggestion() {
        mHomeView.moveToProductActivity(TypeLoad.SUGGESTION, null);
    }

    @Override
    public void prepareDataBestSeller() {
        mHomeView.moveToProductActivity(TypeLoad.BEST_SELLER, null);
    }

    @Override
    public void prepareDataOnSale() {
        mHomeView.moveToProductActivity(TypeLoad.ON_SALE, null);
    }

    @Override
    public void prepareDataProductType(String productTypeId) {
        Bundle data = new Bundle();
        data.putString("productType", productTypeId);
        mHomeView.moveToProductActivity(TypeLoad.PRODUCT_TYPE, data);
    }

    @Override
    public void prepareDataKeyword(String keyword) {
        Bundle data = new Bundle();
        data.putString("keyword", keyword);
        mHomeView.moveToProductActivity(TypeLoad.KEYWORD, data);
    }

    @Override
    public void signOut() {

    }

    public boolean checkOnSale(Product product) {
        boolean isOnSale = true;
        if (product.getSaleOff() != null) {
            if (product.getSaleOff().getDateStart().getTime() > mCurentTime.getTime() || product.getSaleOff().getDateEnd().getTime() < mCurentTime.getTime()) {
                isOnSale = false;
            }
        }
        return isOnSale;
    }

    public String getProductIdByList(int productList, int position) {
        String productId = "";
        switch (productList) {
            case LIST_BEST_SELLER:
                productId = productsBestSeller.get(position).getProductId();
                break;
            case LIST_ON_SALE:
                productId = productsOnSale.get(position).getProductId();
                break;
            case LIST_SUGGESTION:
                productId = productsSuggestion.get(position).getProductId();
                break;
        }
        return productId;
    }

    public void loadProductImage(final int productList, final int position) {
        VolleyUtil.GET(context, Constant.URL_IMAGE + "/product/" + getProductIdByList(productList, position),
                response -> {
                    try {
                        List<ProductImage> imageList = Arrays.asList(gson.fromJson(String.valueOf(response.getJSONArray("imageList")), ProductImage[].class));
                        if (imageList.size() > 0) {
                            switch (productList) {
                                case LIST_BEST_SELLER:
                                    productsBestSeller.get(position).setImageList(imageList);
                                    mHomeView.setNotifyItemChanged(LIST_BEST_SELLER, position);
                                    break;
                                case LIST_ON_SALE:
                                    productsOnSale.get(position).setImageList(imageList);
                                    mHomeView.setNotifyItemChanged(LIST_ON_SALE, position);
                                    break;
                                case LIST_SUGGESTION:
                                    productsSuggestion.get(position).setImageList(imageList);
                                    mHomeView.setNotifyDataSetChanged(LIST_SUGGESTION);
                                    break;
                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                },
                error -> Log.e(TAG, error.toString()));
    }

    public void loadProductReview(final int productList, final int position) {
        VolleyUtil.GET(context, Constant.URL_REVIEW + "/product/" + getProductIdByList(productList, position),
                response -> {
                    try {
                        List<ReviewProduct> reviewProducts = Arrays.asList(gson.fromJson(String.valueOf(response.getJSONArray("reviewProducts")), ReviewProduct[].class));
                        if (reviewProducts.size() > 0) {
                            switch (productList) {
                                case LIST_BEST_SELLER:
                                    productsBestSeller.get(position).setReviewProducts(reviewProducts);
                                    mHomeView.setNotifyItemChanged(LIST_BEST_SELLER, position);
                                    break;
                                case LIST_ON_SALE:
                                    productsOnSale.get(position).setReviewProducts(reviewProducts);
                                    mHomeView.setNotifyItemChanged(LIST_ON_SALE, position);
                                    break;
                                case LIST_SUGGESTION:
                                    productsSuggestion.get(position).setReviewProducts(reviewProducts);
                                    mHomeView.setNotifyDataSetChanged(LIST_SUGGESTION);
                                    break;
                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                },
                error -> Log.e(TAG, error.toString()));
    }
}
