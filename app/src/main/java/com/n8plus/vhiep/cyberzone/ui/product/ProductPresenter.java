package com.n8plus.vhiep.cyberzone.ui.product;

import android.app.Activity;
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
import com.n8plus.vhiep.cyberzone.data.model.ProductType;
import com.n8plus.vhiep.cyberzone.data.model.ReviewProduct;
import com.n8plus.vhiep.cyberzone.data.model.SaleOff;
import com.n8plus.vhiep.cyberzone.data.model.Specification;
import com.n8plus.vhiep.cyberzone.data.model.Store;
import com.n8plus.vhiep.cyberzone.ui.product.adapter.FilterExpandableAdapter;
import com.n8plus.vhiep.cyberzone.ui.product.adapter.ProductVerticalAdapter;
import com.n8plus.vhiep.cyberzone.util.Constant;
import com.n8plus.vhiep.cyberzone.util.MySingleton;

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
    private ProductContract.View mProductView;
    private List<Product> products;
    private List<Filter> filters;
    private ArrayList<ParentObject> parentObjects;
    private Date mCurentTime;
    private final String URL_PRODUCT = Constant.URL_HOST + "products";
    private final String URL_IMAGE = Constant.URL_HOST + "productImages";
    private final String URL_FILTER = Constant.URL_HOST + "filterTypes";
    private final String URL_REVIEW = Constant.URL_HOST + "reviewProducts";
    private final String URL_TIME = "http://api.geonames.org/timezoneJSON?formatted=true&lat=10.041791&lng=105.747099&username=cyberzone&style=full";
    private Gson gson;

    public ProductPresenter(ProductContract.View mProductView) {
        this.mProductView = mProductView;
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.setDateFormat("M/d/yy hh:mm a");
        gson = gsonBuilder.create();
    }

    @Override
    public void loadProductDefault() {
        fetchFilter();
        fetchProduct();
    }

    @Override
    public void loadProductByList(List<Product> productList) {
        products = productList;
        fetchFilter();
        mProductView.setAdapterProduct(productList, "GridLayout");
    }

    @Override
    public void loadProductByProductType(String productTypeId) {
        fetchFilter();
        fetchProduct(productTypeId);
    }

    @Override
    public void loadProductByKeyWord(String keyword) {
        mProductView.setKeyword(keyword);
        JSONObject object = new JSONObject();
        try {
            object.put("name", keyword);
            fetchFilter();
            fetchProduct(object);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void fetchProduct() {
        products = new ArrayList<>();
        JsonObjectRequest productRequest = new JsonObjectRequest(Request.Method.GET, URL_PRODUCT, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    products = Arrays.asList(gson.fromJson(String.valueOf(response.getJSONArray("products")), Product[].class));
                    Log.i("ProductPresenter", "GET: " + products.size() + " products");
                    if (products.size() > 0) {
                        mProductView.setAdapterProduct(products, "GridLayout");
                        fetchCurrentTime();
                        for (int i = 0; i < products.size(); i++) {
                            fetchImageProduct(i);
                            fetchReviewProduct(i);
                            mProductView.setNotifyDataSetChanged();
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("ProductPresenter", error.toString());
            }
        });
        MySingleton.getInstance(((Activity) mProductView).getApplicationContext()).addToRequestQueue(productRequest);
    }

    public void fetchProduct(String productTypeId) {
        products = new ArrayList<>();
        Log.d("ProductPresenter", "URL: "+URL_PRODUCT + "/productType/" + productTypeId);
        JsonObjectRequest productRequestTypeId = new JsonObjectRequest(Request.Method.GET, URL_PRODUCT + "/productType/" + productTypeId, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    products = Arrays.asList(gson.fromJson(String.valueOf(response.getJSONArray("products")), Product[].class));
                    Log.i("ProductPresenter", "GET: " + products.size() + " products");
                    if (products.size() > 0) {
                        mProductView.setAdapterProduct(products, "GridLayout");
                        fetchCurrentTime();
                        for (int i = 0; i < products.size(); i++) {
                            fetchImageProduct(i);
                            fetchReviewProduct(i);
                            mProductView.setNotifyDataSetChanged();
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("ProductPresenter", error.toString());
            }
        });
        MySingleton.getInstance(((Activity) mProductView).getApplicationContext()).addToRequestQueue(productRequestTypeId);
    }

    public void fetchProduct(JSONObject jsonObject) {
        products = new ArrayList<>();
        JsonObjectRequest productRequestKeyWord = new JsonObjectRequest(Request.Method.POST, URL_PRODUCT + "/findByName/", jsonObject, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    products = Arrays.asList(gson.fromJson(String.valueOf(response.getJSONArray("products")), Product[].class));
                    Log.i("ProductPresenter", "GET: " + products.size() + " products");
                    if (products.size() > 0) {
                        mProductView.setAdapterProduct(products, "GridLayout");
                        fetchCurrentTime();
                        for (int i = 0; i < products.size(); i++) {
                            fetchReviewProduct(i);
                            fetchImageProduct(i);
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("ProductPresenter", error.toString());
            }
        });
        MySingleton.getInstance(((Activity) mProductView).getApplicationContext()).addToRequestQueue(productRequestKeyWord);
    }

    public void fetchImageProduct(final int position) {
        JsonObjectRequest requestImage = new JsonObjectRequest(Request.Method.GET, URL_IMAGE + "/product/" + products.get(position).getProductId(), null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    List<ProductImage> imageList = Arrays.asList(gson.fromJson(String.valueOf(response.getJSONArray("imageList")), ProductImage[].class));
                    Log.d("ProductPresenter", "Product images: " + imageList.size());
                    if (imageList.size() > 0) {
                        products.get(position).setImageList(imageList);
                        mProductView.setNotifyDataSetChanged();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("ProductPresenter", error.toString());
            }
        });
        MySingleton.getInstance(((Activity) mProductView).getApplicationContext()).addToRequestQueue(requestImage);
    }

    public void fetchReviewProduct(final int position) {
        JsonObjectRequest requestRating = new JsonObjectRequest(Request.Method.GET, URL_REVIEW + "/product/" + products.get(position).getProductId(), null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    List<ReviewProduct> reviewProducts = Arrays.asList(gson.fromJson(String.valueOf(response.getJSONArray("reviewProducts")), ReviewProduct[].class));
                    Log.d("ProductDetailPresenter", "Review product: " + reviewProducts.size());
                    if (reviewProducts.size() > 0) {
                        products.get(position).setReviewProducts(reviewProducts);
                        mProductView.setNotifyDataSetChanged();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("ProductDetailPresenter", error.toString());
            }
        });
        MySingleton.getInstance(((Activity) mProductView).getApplicationContext()).addToRequestQueue(requestRating);
    }

    public void fetchFilter() {
        JsonObjectRequest filterRequest = new JsonObjectRequest(Request.Method.GET, URL_FILTER, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    filters = Arrays.asList(gson.fromJson(String.valueOf(response.getJSONArray("filterTypes")), Filter[].class));
                    Log.i("ProductPresenter", "Filter: " + filters.size());
                    if (filters.size() > 0) {
                        mProductView.generateFilters(new ArrayList<ParentObject>(filters));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("ProductPresenter", error.toString());
            }
        });
        MySingleton.getInstance(((Activity) mProductView).getApplicationContext()).addToRequestQueue(filterRequest);
    }

    public void fetchCurrentTime() {
        JsonObjectRequest timeRequest = new JsonObjectRequest(Request.Method.GET, URL_TIME, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
                            sdf.setTimeZone(TimeZone.getTimeZone("Asia/Ho_Chi_Minh"));
                            mCurentTime = sdf.parse(response.getString("time"));
                            Log.d("ProductPresenter", "Current time: " + sdf.format(mCurentTime).toString());
                            for (int i = 0; i < products.size(); i++) {
                                if (products.get(i).getSaleOff() != null) {
                                    if (products.get(i).getSaleOff().getDateStart().getTime() > mCurentTime.getTime() || products.get(i).getSaleOff().getDateEnd().getTime() < mCurentTime.getTime()) {
                                        products.get(i).getSaleOff().setDiscount(0);
                                    }
                                }
                            }
                            mProductView.setNotifyDataSetChanged();
                        } catch (ParseException | JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.w("ProductPresenter", "onErrorResponse: " + error.getMessage());
            }
        });
        MySingleton.getInstance(((Activity) mProductView).getApplicationContext()).addToRequestQueue(timeRequest);
    }

    @Override
    public void changeProductGridLayout() {
        mProductView.setAdapterProduct(products, "GridLayout");
    }

    @Override
    public void changeProductLinearLayout() {
        mProductView.setAdapterProduct(products, "LinearLayout");
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

    private void fakeDataFilter() {
        List<FilterChild> categoryItem = new ArrayList<>();
        categoryItem.add(new FilterChild("1603653", "CPU Intel", "a", true));
        categoryItem.add(new FilterChild("1603653", "CPU Intel i3", "a", false));
        categoryItem.add(new FilterChild("1603653", "CPU Intel i5", "a", false));
        categoryItem.add(new FilterChild("1603653", "CPU Intel i7", "a", false));
        categoryItem.add(new FilterChild("1603653", "CPU Intel i9", "a", false));

        List<FilterChild> brandItem = new ArrayList<>();
        brandItem.add(new FilterChild("1603653", "AMD", "a", false));
        brandItem.add(new FilterChild("1603653", "Intel", "a", true));

        List<FilterChild> priceItem = new ArrayList<>();
        priceItem.add(new FilterChild("1603653", "0đ -> 1.000.000đ", "a", true));
        priceItem.add(new FilterChild("1603653", "0đ -> 1.000.000đ", "a", true));
        priceItem.add(new FilterChild("1603653", "0đ -> 1.000.000đ", "a", true));
        priceItem.add(new FilterChild("1603653", "0đ -> 1.000.000đ", "a", true));
        priceItem.add(new FilterChild("1603653", "0đ -> 1.000.000đ", "a", true));

        parentObjects = new ArrayList<>();
        parentObjects.add(new Filter("1603653", "Danh mục", categoryItem));
        parentObjects.add(new Filter("1603653", "Thương hiệu", brandItem));
        parentObjects.add(new Filter("1603653", "Mức giá", priceItem));
    }
}
