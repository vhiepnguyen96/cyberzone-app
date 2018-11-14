package com.n8plus.vhiep.cyberzone.ui.home;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
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

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

public class HomePresenter implements HomeContract.Presenter {
    private final String TAG = "HomePresenter";
    private final int LIST_BEST_SELLER = 1, LIST_ON_SALE = 2, LIST_SUGGESTION = 3;
    private final HomeContract.View mHomeView;
    private List<ProductPurchase> productPurchases;
    private List<Product> productsBestSeller, productsSuggestion, productsOnSale, mProductListTemp;
    private List<ProductType> productTypes;
    private Date mCurentTime;
    private final String URL_PRODUCT = Constant.URL_HOST + "products";
    private final String URL_ORDER_ITEM = Constant.URL_HOST + "orderItems";
    private final String URL_PRODUCT_TYPE = Constant.URL_HOST + "productTypes";
    private final String URL_IMAGE = Constant.URL_HOST + "productImages";
    private final String URL_FILTER = Constant.URL_HOST + "filterTypes";
    private final String URL_REVIEW = Constant.URL_HOST + "reviewProducts";
    private final String URL_CUSTOMER = Constant.URL_HOST + "customers";
    private final String URL_TIME = "http://api.geonames.org/timezoneJSON?formatted=true&lat=10.041791&lng=105.747099&username=cyberzone&style=full";
    private Gson gson;
    private int fromIndex = 0, toIndex = 9;
    private int mCurrentPage = 1, mPages = 1;

    public HomePresenter(HomeContract.View mHomeView) {
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
        JsonObjectRequest timeRequest = new JsonObjectRequest(Request.Method.GET, URL_TIME, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
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
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.w("HomePresenter", "onErrorResponse: " + error.getMessage());
            }
        });
        MySingleton.getInstance(((Activity) mHomeView).getApplicationContext()).addToRequestQueue(timeRequest);
    }

    @Override
    public void loadProductBestSeller() {
        productPurchases = new ArrayList<>();
        JsonObjectRequest productPurchaseRequest = new JsonObjectRequest(Request.Method.GET, URL_ORDER_ITEM + "/productPurchase", null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    productPurchases = Arrays.asList(gson.fromJson(String.valueOf(response.getJSONArray("productPurchases")), ProductPurchase[].class));
                    Log.i("HomePresenter", "GET: " + productPurchases.size() + " productPurchases");

                    if (productPurchases.size() > 0) {
                        productsBestSeller = new ArrayList<>();

                        for (int i = 0; i < productPurchases.size(); i++) {
                            final int index = i;
                            JsonObjectRequest productRequest = new JsonObjectRequest(Request.Method.GET, URL_PRODUCT + "/" + productPurchases.get(index).getProductId(), null, new Response.Listener<JSONObject>() {
                                @Override
                                public void onResponse(JSONObject response) {
                                    try {
                                        Product product = gson.fromJson(String.valueOf(response.getJSONObject("product")), Product.class);
                                        // Add product
                                        if (product != null) {
                                            productsBestSeller.add(product);
                                            mHomeView.setAdapterBestSeller(productsBestSeller);
                                            Log.i("HomePresenter", "productsBestSeller: " + productsBestSeller.size());

                                            if (!checkOnSale(productsBestSeller.get(productsBestSeller.indexOf(product)))) {
                                                productsBestSeller.get(productsBestSeller.indexOf(product)).getSaleOff().setDiscount(0);
                                            }
                                            loadProductImage(LIST_BEST_SELLER, productsBestSeller.indexOf(product));
                                            loadProductReview(LIST_BEST_SELLER, productsBestSeller.indexOf(product));
                                        }

                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }, new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    Log.e("HomePresenter", error.toString());
                                }
                            });
                            MySingleton.getInstance(((Activity) mHomeView).getApplicationContext()).addToRequestQueue(productRequest);
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("HomePresenter", error.toString());
            }
        });
        MySingleton.getInstance(((Activity) mHomeView).getApplicationContext()).addToRequestQueue(productPurchaseRequest);
    }

    @Override
    public void loadProductOnSale() {
        productsOnSale = new ArrayList<>();
        JsonObjectRequest fetchOnSale = new JsonObjectRequest(Request.Method.GET, URL_PRODUCT + "/onSale", null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d(TAG, response.toString());
                        try {
                            productsOnSale = new ArrayList<Product>(Arrays.asList(gson.fromJson(String.valueOf(response.getJSONArray("products")), Product[].class)));

                            if (productsOnSale.size() > 0) {
                                mHomeView.setAdapterOnSale(productsOnSale);
                                for (int i = 0; i < productsOnSale.size(); i++) {
                                    if (!checkOnSale(productsOnSale.get(i))) {
                                        productsOnSale.remove(i);
                                    }
                                    loadProductImage(LIST_ON_SALE, i);
                                    loadProductReview(LIST_ON_SALE, i);
                                }
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e(TAG, error.toString());
                    }
                });
        MySingleton.getInstance(((Activity) mHomeView).getApplicationContext()).addToRequestQueue(fetchOnSale);
    }

    @Override
    public void loadProductSuggestion() {
        productsSuggestion = new ArrayList<>();
        JsonObjectRequest fetchSuggestion = new JsonObjectRequest(Request.Method.GET, URL_PRODUCT + "/page/" + mCurrentPage, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d(TAG, response.toString());
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
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e(TAG, error.toString());
                    }
                });
        MySingleton.getInstance(((Activity) mHomeView).getApplicationContext()).addToRequestQueue(fetchSuggestion);
    }

    @Override
    public void loadMoreSuggestion(int page) {
        mCurrentPage = page;
        if (mCurrentPage <= mPages) {
            mHomeView.startLoadMore();

            mProductListTemp = new ArrayList<>();
            final int sizeOld = productsSuggestion.size();
            String URL_LOAD_MORE = URL_PRODUCT + "/page/" + mCurrentPage;

            JsonObjectRequest fetchMoreProduct = new JsonObjectRequest(Request.Method.GET, URL_LOAD_MORE, null,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            Log.d(TAG, response.toString());
                            try {
                                mProductListTemp = Arrays.asList(gson.fromJson(String.valueOf(response.getJSONArray("products")), Product[].class));

                                if (mProductListTemp.size() > 0) {
                                    productsSuggestion.addAll(mProductListTemp);
                                    mHomeView.addDataListAdapter(mProductListTemp);
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
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Log.e(TAG, error.toString());
                            mHomeView.onLoadMoreFailed();
                        }
                    });
            MySingleton.getInstance(((Activity) mHomeView).getApplicationContext()).addToRequestQueue(fetchMoreProduct);
        } else {
            Log.d(TAG, "OnReachEnd");
            mHomeView.onReachEnd();
        }
    }

    @Override
    public void loadAllProductType() {
        productTypes = new ArrayList<>();
        JsonObjectRequest productTypeRequest = new JsonObjectRequest(Request.Method.GET, URL_PRODUCT_TYPE, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    productTypes = Arrays.asList(gson.fromJson(String.valueOf(response.getJSONArray("productTypes")), ProductType[].class));
                    Log.i("HomePresenter", "Product type: " + productTypes.size());
                    mHomeView.setAdapterPopularCategory(productTypes.subList(fromIndex, toIndex));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("HomePresenter", error.toString());
            }
        });
        MySingleton.getInstance(((Activity) mHomeView).getApplicationContext()).addToRequestQueue(productTypeRequest);
    }

    @Override
    public void loadDataCustomer(final String accountId) {
        JsonObjectRequest customerRequest = new JsonObjectRequest(Request.Method.GET, URL_CUSTOMER + "/account/" + accountId, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
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
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("HomePresenter", error.toString());
            }
        });
        MySingleton.getInstance(((Activity) mHomeView).getApplicationContext()).addToRequestQueue(customerRequest);
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
        if (product.getSaleOff() != null && (product.getSaleOff().getDateStart().getTime() > mCurentTime.getTime() || product.getSaleOff().getDateEnd().getTime() < mCurentTime.getTime())) {
            isOnSale = false;
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
        JsonObjectRequest fetchImage = new JsonObjectRequest(Request.Method.GET, URL_IMAGE + "/product/" + getProductIdByList(productList, position), null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            List<ProductImage> imageList = Arrays.asList(gson.fromJson(String.valueOf(response.getJSONArray("imageList")), ProductImage[].class));
                            Log.d("HomePresenter", "Product images: " + imageList.size());
                            Log.d(TAG, "ImageProduct | productList: " + productList + ", position: " + position);
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
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("HomePresenter", error.toString());
                    }
                });
        MySingleton.getInstance(((Activity) mHomeView).getApplicationContext()).addToRequestQueue(fetchImage);
    }

    public void loadProductReview(final int productList, final int position) {
        JsonObjectRequest fetchReview = new JsonObjectRequest(Request.Method.GET, URL_REVIEW + "/product/" + getProductIdByList(productList, position), null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            List<ReviewProduct> reviewProducts = Arrays.asList(gson.fromJson(String.valueOf(response.getJSONArray("reviewProducts")), ReviewProduct[].class));
                            Log.d("HomePresenter", "Review product: " + reviewProducts.size());
                            Log.d(TAG, "ReviewProduct | productList: " + productList + ", position: " + position);
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
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("HomePresenter", error.toString());
                    }
                });
        MySingleton.getInstance(((Activity) mHomeView).getApplicationContext()).addToRequestQueue(fetchReview);
    }


}
