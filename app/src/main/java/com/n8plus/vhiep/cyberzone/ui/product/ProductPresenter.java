package com.n8plus.vhiep.cyberzone.ui.product;

import android.app.Activity;
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

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

public class ProductPresenter implements ProductContract.Presenter {
    private static final String TAG = "ProductPresenter";
    private int GRID_LAYOUT = 1, LINEAR_LAYOUT = 2;
    private ProductContract.View mProductView;
    private List<ProductPurchase> productPurchases;
    private List<Product> mProductList, mProductListTemp;
    private List<Filter> filters;
    private ArrayList<ParentObject> parentObjects;
    private Date mCurentTime;
    private final String URL_PRODUCT = Constant.URL_HOST + "products";
    private final String URL_IMAGE = Constant.URL_HOST + "productImages";
    private final String URL_FILTER = Constant.URL_HOST + "filterTypes";
    private final String URL_REVIEW = Constant.URL_HOST + "reviewProducts";
    private final String URL_ORDER_ITEM = Constant.URL_HOST + "orderItems";
    private final String URL_TIME = "http://api.geonames.org/timezoneJSON?formatted=true&lat=10.041791&lng=105.747099&username=cyberzone&style=full";
    private Gson gson;
    private String mProductTypeId;
    private int mCurrentPage = 1, mPages = 1;

    public ProductPresenter(ProductContract.View mProductView) {
        this.mProductView = mProductView;
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.setDateFormat("M/d/yy hh:mm a");
        gson = gsonBuilder.create();
        mCurentTime = getCurrentTime();
        loadFilter();
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
        MySingleton.getInstance(((Activity) mProductView).getApplicationContext()).addToRequestQueue(timeRequest);
    }

    @Override
    public Date getCurrentTime() {
        mCurentTime = new Date();
        JsonObjectRequest timeRequest = new JsonObjectRequest(Request.Method.GET, URL_TIME, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
                            sdf.setTimeZone(TimeZone.getTimeZone("Asia/Ho_Chi_Minh"));
                            mCurentTime = sdf.parse(response.getString("time"));
                            Log.d(TAG, "CurrentTime: " + sdf.format(mCurentTime).toString());
                            // Load data
                        } catch (ParseException | JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.w(TAG, "onErrorResponse: " + error.getMessage());
            }
        });
        MySingleton.getInstance(((Activity) mProductView).getApplicationContext()).addToRequestQueue(timeRequest);
        return mCurentTime;
    }

    @Override
    public void loadFilter() {
        JsonObjectRequest filterRequest = new JsonObjectRequest(Request.Method.GET, URL_FILTER, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
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
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, error.toString());
            }
        });
        MySingleton.getInstance(((Activity) mProductView).getApplicationContext()).addToRequestQueue(filterRequest);
    }

    @Override
    public void loadProductBestSeller() {
        productPurchases = new ArrayList<>();
        JsonObjectRequest productPurchaseRequest = new JsonObjectRequest(Request.Method.GET, URL_ORDER_ITEM + "/productPurchase", null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    productPurchases = Arrays.asList(gson.fromJson(String.valueOf(response.getJSONArray("productPurchases")), ProductPurchase[].class));
                    Log.i(TAG, "GET: " + productPurchases.size() + " productPurchases");

                    if (productPurchases.size() > 0) {
                        mProductList = new ArrayList<>();
                        mProductView.showLinearLoading(false);

                        for (int i = 0; i < productPurchases.size(); i++) {
                            final int index = i;
                            JsonObjectRequest productRequest = new JsonObjectRequest(Request.Method.GET, URL_PRODUCT + "/" + productPurchases.get(index).getProductId(), null, new Response.Listener<JSONObject>() {
                                @Override
                                public void onResponse(JSONObject response) {
                                    try {
                                        Product product = gson.fromJson(String.valueOf(response.getJSONObject("product")), Product.class);
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
                                }
                            }, new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    Log.e(TAG, error.toString());
                                }
                            });
                            MySingleton.getInstance(((Activity) mProductView).getApplicationContext()).addToRequestQueue(productRequest);
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, error.toString());
                mProductView.showLinearLoading(false);
                mProductView.showLinearNotFound(true);
            }
        });
        MySingleton.getInstance(((Activity) mProductView).getApplicationContext()).addToRequestQueue(productPurchaseRequest);
    }

    @Override
    public void loadProductOnSale() {
        mProductList = new ArrayList<>();
        JsonObjectRequest fetchOnSale = new JsonObjectRequest(Request.Method.GET, URL_PRODUCT + "/onSale/" + mCurrentPage, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d(TAG, response.toString());
                        try {
                            mCurrentPage = Integer.valueOf(response.getString("current"));
                            mPages = Integer.valueOf(response.getString("pages"));
                            mProductList = new ArrayList<Product>(Arrays.asList(gson.fromJson(String.valueOf(response.getJSONArray("products")), Product[].class)));
                            Log.i(TAG, "productsOnSale: " + mProductList.size());

                            if (mProductList.size() > 0) {
                                mProductView.showLinearLoading(false);
                                mProductView.setAdapterProduct(mProductList, GRID_LAYOUT);

                                for (int i = 0; i < mProductList.size(); i++) {
                                    if (!checkOnSale(mProductList.get(i))) {
                                        mProductList.remove(i);
                                        mProductView.setNotifyItemRemoved(i);
                                    }
                                    loadProductReview(i);
                                    loadProductImage(i);
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
                        mProductView.showLinearLoading(false);
                        mProductView.showLinearNotFound(true);
                    }
                });
        MySingleton.getInstance(((Activity) mProductView).getApplicationContext()).addToRequestQueue(fetchOnSale);
    }

    @Override
    public void loadProductSuggestion() {
        mProductList = new ArrayList<>();
        JsonObjectRequest fetchSuggestion = new JsonObjectRequest(Request.Method.GET, URL_PRODUCT + "/page/" + mCurrentPage, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d(TAG, response.toString());
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
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e(TAG, error.toString());
                        mProductView.showLinearLoading(false);
                        mProductView.showLinearNotFound(true);
                    }
                });
        MySingleton.getInstance(((Activity) mProductView).getApplicationContext()).addToRequestQueue(fetchSuggestion);
    }

    @Override
    public void loadProductByProductType(String productTypeId) {
        mProductTypeId = productTypeId;
        mProductList = new ArrayList<>();
        JsonObjectRequest productRequestTypeId = new JsonObjectRequest(Request.Method.GET, URL_PRODUCT + "/productType/" + mProductTypeId + "/page/" + mCurrentPage, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
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
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                mProductView.showLinearLoading(false);
                                mProductView.showLinearNotFound(true);
                            }
                        }, 1000);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, error.toString());
                mProductView.showLinearLoading(false);
                mProductView.showLinearNotFound(true);
            }
        });
        MySingleton.getInstance(((Activity) mProductView).getApplicationContext()).addToRequestQueue(productRequestTypeId);
    }

    @Override
    public void loadProductByKeyWord(String keyword) {
        mProductView.setKeyword(keyword);
        JSONObject object = new JSONObject();
        try {
            object.put("name", keyword);

            mProductList = new ArrayList<>();
            JsonObjectRequest productRequestKeyWord = new JsonObjectRequest(Request.Method.POST, URL_PRODUCT + "/findByName/", object, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
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
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    mProductView.showLinearLoading(false);
                                    mProductView.showLinearNotFound(true);
                                }
                            }, 1000);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.e(TAG, error.toString());
                    mProductView.showLinearLoading(false);
                    mProductView.showLinearNotFound(true);
                }
            });
            MySingleton.getInstance(((Activity) mProductView).getApplicationContext()).addToRequestQueue(productRequestKeyWord);

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
                    URL_LOAD_MORE = URL_PRODUCT + "/productType/" + mProductTypeId + "/page/" + mCurrentPage;
                    break;
                case ON_SALE:
                    URL_LOAD_MORE = URL_PRODUCT + "/onSale/" + mCurrentPage;
                    break;
                case SUGGESTION:
                    URL_LOAD_MORE = URL_PRODUCT + "/page/" + mCurrentPage;
                    break;
            }
            JsonObjectRequest fetchMoreProduct = new JsonObjectRequest(Request.Method.GET, URL_LOAD_MORE, null,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            Log.d(TAG, response.toString());
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
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Log.e(TAG, error.toString());
                            mProductView.onLoadMoreFailed();
                        }
                    });
            MySingleton.getInstance(((Activity) mProductView).getApplicationContext()).addToRequestQueue(fetchMoreProduct);
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
        JsonObjectRequest fetchImage = new JsonObjectRequest(Request.Method.GET, URL_IMAGE + "/product/" + mProductList.get(position).getProductId(), null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            List<ProductImage> imageList = Arrays.asList(gson.fromJson(String.valueOf(response.getJSONArray("imageList")), ProductImage[].class));
                            Log.d(TAG, "Product images: " + imageList.size());
                            if (imageList.size() > 0) {
                                mProductList.get(position).setImageList(imageList);
//                                mProductView.setNotifyItemChanged(position);
                                mProductView.setNotifyDataSetChanged();
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
        MySingleton.getInstance(((Activity) mProductView).getApplicationContext()).addToRequestQueue(fetchImage);
    }

    public void loadProductReview(final int position) {
        JsonObjectRequest fetchReview = new JsonObjectRequest(Request.Method.GET, URL_REVIEW + "/product/" + mProductList.get(position).getProductId(), null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            List<ReviewProduct> reviewProducts = Arrays.asList(gson.fromJson(String.valueOf(response.getJSONArray("reviewProducts")), ReviewProduct[].class));
                            Log.d(TAG, "Review product: " + reviewProducts.size());
                            if (reviewProducts.size() > 0) {
                                mProductList.get(position).setReviewProducts(reviewProducts);
//                                mProductView.setNotifyItemChanged(position);
                                mProductView.setNotifyDataSetChanged();
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
        MySingleton.getInstance(((Activity) mProductView).getApplicationContext()).addToRequestQueue(fetchReview);
    }

//    private void fakeDataFilter() {
//        List<FilterChild> categoryItem = new ArrayList<>();
//        categoryItem.add(new FilterChild("1603653", "CPU Intel", "a", true));
//        categoryItem.add(new FilterChild("1603653", "CPU Intel i3", "a", false));
//        categoryItem.add(new FilterChild("1603653", "CPU Intel i5", "a", false));
//        categoryItem.add(new FilterChild("1603653", "CPU Intel i7", "a", false));
//        categoryItem.add(new FilterChild("1603653", "CPU Intel i9", "a", false));
//
//        List<FilterChild> brandItem = new ArrayList<>();
//        brandItem.add(new FilterChild("1603653", "AMD", "a", false));
//        brandItem.add(new FilterChild("1603653", "Intel", "a", true));
//
//        List<FilterChild> priceItem = new ArrayList<>();
//        priceItem.add(new FilterChild("1603653", "0đ -> 1.000.000đ", "a", true));
//        priceItem.add(new FilterChild("1603653", "0đ -> 1.000.000đ", "a", true));
//        priceItem.add(new FilterChild("1603653", "0đ -> 1.000.000đ", "a", true));
//        priceItem.add(new FilterChild("1603653", "0đ -> 1.000.000đ", "a", true));
//        priceItem.add(new FilterChild("1603653", "0đ -> 1.000.000đ", "a", true));
//
//        parentObjects = new ArrayList<>();
//        parentObjects.add(new Filter("1603653", "Danh mục", categoryItem));
//        parentObjects.add(new Filter("1603653", "Thương hiệu", brandItem));
//        parentObjects.add(new Filter("1603653", "Mức giá", priceItem));
//    }
}
