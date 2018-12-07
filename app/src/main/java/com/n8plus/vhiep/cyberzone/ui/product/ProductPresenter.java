package com.n8plus.vhiep.cyberzone.ui.product;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Header;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.bignerdranch.expandablerecyclerview.Model.ParentObject;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.n8plus.vhiep.cyberzone.R;
import com.n8plus.vhiep.cyberzone.data.model.Filter;
import com.n8plus.vhiep.cyberzone.data.model.FilterChild;
import com.n8plus.vhiep.cyberzone.data.model.Overview;
import com.n8plus.vhiep.cyberzone.data.model.Product;
import com.n8plus.vhiep.cyberzone.data.model.ProductImage;
import com.n8plus.vhiep.cyberzone.data.model.ProductPurchase;
import com.n8plus.vhiep.cyberzone.data.model.ProductType;
import com.n8plus.vhiep.cyberzone.data.model.ReviewProduct;
import com.n8plus.vhiep.cyberzone.data.model.SaleOff;
import com.n8plus.vhiep.cyberzone.data.model.Specification;
import com.n8plus.vhiep.cyberzone.data.model.Store;
import com.n8plus.vhiep.cyberzone.ui.product.adapter.FilterExpandableAdapter;
import com.n8plus.vhiep.cyberzone.ui.product.adapter.ProductVerticalAdapter;
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
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.TimeZone;

public class ProductPresenter implements ProductContract.Presenter {
    private static final String TAG = "ProductPresenter";
    private int GRID_LAYOUT = 1, LINEAR_LAYOUT = 2;
    private Context context;
    private ProductContract.View mProductView;
    private List<ProductPurchase> productPurchases;
    private List<Product> mProductList, mProductListTemp;
    private List<Filter> filters;
    private ArrayList<ParentObject> parentObjects;
    private Date mCurentTime;
    private Gson gson;
    private String mProductTypeId;
    private int mCurrentPage = 1, mPages = 1;

    public ProductPresenter(@NonNull final Context context, @NonNull final ProductContract.View mProductView) {
        this.context = context;
        this.mProductView = mProductView;
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.setDateFormat("M/d/yy hh:mm a");
        gson = gsonBuilder.create();
        mCurentTime = getCurrentTime();
        loadFilter();
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
                    } catch (ParseException | JSONException e) {
                        e.printStackTrace();
                    }
                },
                error -> Log.e(TAG, error.toString()));
    }

    @Override
    public Date getCurrentTime() {
        mCurentTime = new Date();
        VolleyUtil.GET(context, Constant.URL_TIME,
                response -> {
                    try {
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
                        sdf.setTimeZone(TimeZone.getTimeZone("Asia/Ho_Chi_Minh"));
                        mCurentTime = sdf.parse(response.getString("time"));
                        Log.d(TAG, "CurrentTime: " + sdf.format(mCurentTime).toString());
                    } catch (ParseException | JSONException e) {
                        e.printStackTrace();
                    }
                },
                error -> Log.e(TAG, error.toString()));
        return mCurentTime;
    }

    @Override
    public void loadFilter() {
        VolleyUtil.GET(context, Constant.URL_FILTER,
                response -> {
                    try {
                        filters = Arrays.asList(gson.fromJson(String.valueOf(response.getJSONArray("filterTypes")), Filter[].class));
                        Log.i(TAG, "Filter: " + filters.size());
                        if (filters.size() > 0) {
                            mProductView.generateFilters(new ArrayList<ParentObject>(filters));
                            parentObjects = new ArrayList<ParentObject>(filters);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                },
                error -> Log.e(TAG, error.toString()));
    }

    @Override
    public void loadProductBestSeller() {
        productPurchases = new ArrayList<>();
        VolleyUtil.GET(context, Constant.URL_ORDER_ITEM + "/productPurchase",
                response -> {
                    try {
                        productPurchases = Arrays.asList(gson.fromJson(String.valueOf(response.getJSONArray("productPurchases")), ProductPurchase[].class));
                        Log.i(TAG, "GET: " + productPurchases.size() + " productPurchases");

                        if (productPurchases.size() > 0) {
                            mProductList = new ArrayList<>();
                            mProductView.showLinearLoading(false);

                            for (int i = 0; i < productPurchases.size(); i++) {
                                final int index = i;
                                VolleyUtil.GET(context, Constant.URL_PRODUCT + "/" + productPurchases.get(index).getProductId(),
                                        response1 -> {
                                            try {
                                                Product product = gson.fromJson(String.valueOf(response1.getJSONObject("product")), Product.class);
                                                // Add product
                                                mProductList.add(product);
                                                mProductView.setAdapterProduct(mProductList, GRID_LAYOUT);
                                                Log.i(TAG, "productsBestSeller: " + mProductList.size());

                                                if (!checkOnSale(mProductList.get(mProductList.indexOf(product)))) {
                                                    mProductList.get(mProductList.indexOf(product)).getSaleOff().setDiscount(0);
                                                }
                                                loadProductReview(mProductList.indexOf(product));
                                                loadProductImage(mProductList.indexOf(product));


                                            } catch (JSONException e) {
                                                e.printStackTrace();
                                            }
                                        },
                                        error -> Log.e(TAG, error.toString()));
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
        VolleyUtil.GET(context, Constant.URL_PRODUCT + "/onSale/" + mCurrentPage,
                response -> {
                    try {
                        mCurrentPage = Integer.valueOf(response.getString("current"));
                        mPages = Integer.valueOf(response.getString("pages"));
                        mProductList = new ArrayList<>(Arrays.asList(gson.fromJson(String.valueOf(response.getJSONArray("products")), Product[].class)));
                        if (!mProductList.isEmpty()) {
                            Log.d(TAG, "productsOnSaleSize: " + mProductList.size());
                            Iterator<Product> iterator = mProductList.iterator();
                            while (iterator.hasNext()) {
                                Product next = iterator.next();
                                if (!checkOnSale(next)) {
                                    iterator.remove();
                                }
                            }
                            // Load image, review
                            mProductView.showLinearLoading(false);
                            mProductView.setAdapterProduct(mProductList, GRID_LAYOUT);

                            for (int i = 0; i < mProductList.size(); i++) {
                                loadProductImage(i);
                                loadProductReview(i);
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
        mProductList = new ArrayList<>();
        VolleyUtil.GET(context, Constant.URL_PRODUCT + "/page/" + mCurrentPage,
                response -> {
                    try {
                        mCurrentPage = Integer.valueOf(response.getString("current"));
                        mPages = Integer.valueOf(response.getString("pages"));
                        mProductList = new ArrayList<Product>(Arrays.asList(gson.fromJson(String.valueOf(response.getJSONArray("products")), Product[].class)));
                        Log.d(TAG, "SuggestionSize: " + mProductList.size() + ", current: " + mCurrentPage + ", pages: " + mPages);

                        if (mProductList.size() > 0) {
                            mProductView.showLinearLoading(false);
                            mProductView.setAdapterProduct(mProductList, GRID_LAYOUT);

                            for (int i = 0; i < mProductList.size(); i++) {
                                if (!checkOnSale(mProductList.get(i))) {
                                    mProductList.get(i).getSaleOff().setDiscount(0);
                                }
                                loadProductReview(i);
                                loadProductImage(i);
                            }
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                },
                error -> {
                    Log.e(TAG, error.toString());
                    mProductView.showLinearLoading(false);
                    mProductView.showLinearNotFound(true);
                });
    }

    @Override
    public void loadProductByProductType(String productTypeId) {
        mProductTypeId = productTypeId;
        mProductList = new ArrayList<>();

        VolleyUtil.GET(context, Constant.URL_PRODUCT + "/productType/" + mProductTypeId + "/page/" + mCurrentPage,
                response -> {
                    try {
                        mProductList = Arrays.asList(gson.fromJson(String.valueOf(response.getJSONArray("products")), Product[].class));
                        Log.i(TAG, "GET: " + mProductList.size() + " products");

                        if (mProductList.size() > 0) {
                            mProductView.showLinearLoading(false);
                            mProductView.setAdapterProduct(mProductList, GRID_LAYOUT);

                            for (int i = 0; i < mProductList.size(); i++) {
                                if (!checkOnSale(mProductList.get(i))) {
                                    mProductList.get(i).getSaleOff().setDiscount(0);
                                }
                                loadProductReview(i);
                                loadProductImage(i);
                            }
                        } else {
                            new Handler().postDelayed(() -> {
                                mProductView.showLinearLoading(false);
                                mProductView.showLinearNotFound(true);
                            }, 1000);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                },
                error -> {
                    Log.e(TAG, error.toString());
                    mProductView.showLinearLoading(false);
                    mProductView.showLinearNotFound(true);
                });
    }

    @Override
    public void loadProductByKeyWord(String keyword) {
        mProductView.setKeyword(keyword);
        JSONObject object = new JSONObject();
        try {
            object.put("name", keyword);
            mProductList = new ArrayList<>();

            VolleyUtil.POST(context, Constant.URL_PRODUCT + "/findByName/", object,
                    response -> {
                        try {
                            mProductList = Arrays.asList(gson.fromJson(String.valueOf(response.getJSONArray("products")), Product[].class));
                            Log.i(TAG, "GET: " + mProductList.size() + " products");
                            if (mProductList.size() > 0) {
                                mProductView.showLinearLoading(false);
                                mProductView.showLinearNotFound(false);
                                mProductView.setAdapterProduct(mProductList, GRID_LAYOUT);

                                for (int i = 0; i < mProductList.size(); i++) {
                                    if (!checkOnSale(mProductList.get(i))) {
                                        mProductList.get(i).getSaleOff().setDiscount(0);
                                    }
                                    loadProductReview(i);
                                    loadProductImage(i);
                                }
                            } else {
                                new Handler().postDelayed(() -> {
                                    mProductView.setAdapterProduct(mProductList, GRID_LAYOUT);
                                    mProductView.showLinearLoading(false);
                                    mProductView.showLinearNotFound(true);
                                }, 1000);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    },
                    error -> {
                        Log.e(TAG, error.toString());
                        mProductView.showLinearLoading(false);
                        mProductView.showLinearNotFound(true);
                    });
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void loadMoreProduct(TypeLoad typeLoad, int page) {
        if (page >= mCurrentPage) {
            mCurrentPage = page;
        }
        if (mCurrentPage <= mPages) {
            mProductView.startLoadMore();

            mProductListTemp = new ArrayList<>();
            final int sizeOld = mProductList.size();
            String URL_LOAD_MORE = "";

            switch (typeLoad) {
                case PRODUCT_TYPE:
                    URL_LOAD_MORE = Constant.URL_PRODUCT + "/productType/" + mProductTypeId + "/page/" + mCurrentPage;
                    break;
                case ON_SALE:
                    URL_LOAD_MORE = Constant.URL_PRODUCT + "/onSale/" + mCurrentPage;
                    break;
                case SUGGESTION:
                    URL_LOAD_MORE = Constant.URL_PRODUCT + "/page/" + mCurrentPage;
                    break;
            }

            VolleyUtil.GET(context, URL_LOAD_MORE,
                    response -> {
                        try {
                            mProductListTemp = Arrays.asList(gson.fromJson(String.valueOf(response.getJSONArray("products")), Product[].class));

                            if (mProductListTemp.size() > 0) {
                                mProductList.addAll(mProductListTemp);
                                mProductView.addDataListAdapter(mProductListTemp);
                                Log.d(TAG, "SizeOld: " + sizeOld + ", SizeNew: " + mProductList.size());

                                if (mProductList.size() > sizeOld) {
                                    for (int i = sizeOld; i < mProductList.size(); i++) {
                                        if (!checkOnSale(mProductList.get(i))) {
                                            mProductList.get(i).getSaleOff().setDiscount(0);
                                        }
                                        loadProductReview(i);
                                        loadProductImage(i);
                                    }
                                }
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    },
                    error -> {
                        Log.e(TAG, error.toString());
                        mProductView.onLoadMoreFailed();
                    });
        } else {
            Log.d(TAG, "OnReachEnd");
            mProductView.onReachEnd();
        }
    }

    @Override
    public void changeProductGridLayout() {
        mProductView.setAdapterProduct(mProductList, GRID_LAYOUT);
    }

    @Override
    public void changeProductLinearLayout() {
        mProductView.setAdapterProduct(mProductList, LINEAR_LAYOUT);
    }

    @Override
    public void resetFilter() {
        for (int i = 0; i < parentObjects.size(); i++) {
            ParentObject parentObject = parentObjects.get(i);
            for (int j = 0; j < parentObject.getChildObjectList().size(); j++) {
                ((FilterChild) parentObject.getChildObjectList().get(j)).setState(false);
            }
        }
        mProductView.generateFilters(parentObjects);
    }

    public boolean checkOnSale(Product product) {
        boolean isOnSale = true;
        if (product.getSaleOff() != null && (product.getSaleOff().getDateStart().getTime() > mCurentTime.getTime() || product.getSaleOff().getDateEnd().getTime() < mCurentTime.getTime())) {
            isOnSale = false;
        }
        return isOnSale;
    }

    public void loadProductImage(final int position) {
        VolleyUtil.GET(context, Constant.URL_IMAGE + "/product/" + mProductList.get(position).getProductId(),
                response -> {
                    try {
                        List<ProductImage> imageList = Arrays.asList(gson.fromJson(String.valueOf(response.getJSONArray("imageList")), ProductImage[].class));
                        Log.d(TAG, "Product images: " + imageList.size());
                        if (imageList.size() > 0) {
                            mProductList.get(position).setImageList(imageList);
                            mProductView.setNotifyDataSetChanged();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                },
                error ->  Log.e(TAG, error.toString()));
    }

    public void loadProductReview(final int position) {
        VolleyUtil.GET(context, Constant.URL_REVIEW + "/product/" + mProductList.get(position).getProductId(),
                response -> {
                    try {
                        List<ReviewProduct> reviewProducts = Arrays.asList(gson.fromJson(String.valueOf(response.getJSONArray("reviewProducts")), ReviewProduct[].class));
                        Log.d(TAG, "Review product: " + reviewProducts.size());
                        if (reviewProducts.size() > 0) {
                            mProductList.get(position).setReviewProducts(reviewProducts);
                            mProductView.setNotifyDataSetChanged();
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                },
                error ->  Log.e(TAG, error.toString()));
    }
}
