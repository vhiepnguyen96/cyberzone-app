package com.n8plus.vhiep.cyberzone.ui.manage.wishlistfollowedstore.wishlist;

import android.app.Activity;
import android.support.v4.app.Fragment;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import com.n8plus.vhiep.cyberzone.R;
import com.n8plus.vhiep.cyberzone.data.model.Overview;
import com.n8plus.vhiep.cyberzone.data.model.Product;
import com.n8plus.vhiep.cyberzone.data.model.ProductImage;
import com.n8plus.vhiep.cyberzone.data.model.ProductType;
import com.n8plus.vhiep.cyberzone.data.model.PurchaseItem;
import com.n8plus.vhiep.cyberzone.data.model.SaleOff;
import com.n8plus.vhiep.cyberzone.data.model.Specification;
import com.n8plus.vhiep.cyberzone.data.model.Store;
import com.n8plus.vhiep.cyberzone.data.model.WishList;
import com.n8plus.vhiep.cyberzone.ui.manage.myorders.MyOrderContract;
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

public class WishListPresenter implements WishListContract.Presenter {
    private WishListContract.View mWishListView;
    private List<WishList> mWishList;
    private Gson gson;
    private final String URL_WISHLIST = Constant.URL_HOST + "wishList";
    private final String URL_IMAGE = Constant.URL_HOST + "productImages";
    private final String URL_PRODUCT = Constant.URL_HOST + "products";
    private Date mCurentTime;

    public WishListPresenter(WishListContract.View mWishListView) {
        this.mWishListView = mWishListView;
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.setDateFormat("M/d/yy hh:mm a");
        gson = gsonBuilder.create();
    }

    @Override
    public void loadData() {
        loadWishList(Constant.customer.getId());
    }

    @Override
    public void loadWishList(String customerId) {
        mWishList = new ArrayList<>();
        JsonObjectRequest wishListRequest = new JsonObjectRequest(Request.Method.GET, URL_WISHLIST + "/customer/" + customerId, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            mWishList = Arrays.asList(gson.fromJson(String.valueOf(response.getJSONArray("wishList")), WishList[].class));
                            Log.d("WishListPresenter", "wishList: " + mWishList.size());
                            if (mWishList.size() > 0) {
                                mWishListView.setAdapterWishList(mWishList);
                                fetchCurrentTime();
                                for (int i = 0; i < mWishList.size(); i++) {
                                    if (mWishList.get(i).getProduct() != null) {
                                        loadProduct(i);
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

                    }
                });
        MySingleton.getInstance(((Fragment) mWishListView).getContext().getApplicationContext()).addToRequestQueue(wishListRequest);
    }

    @Override
    public void loadProduct(final int position) {
        JsonObjectRequest productRequest = new JsonObjectRequest(Request.Method.GET, URL_PRODUCT + "/" + mWishList.get(position).getProduct().getProductId(), null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    Product product = gson.fromJson(String.valueOf(response.getJSONObject("product")), Product.class);
                    if (product != null) {
                        mWishList.get(position).setProduct(product);
                        loadImageProduct(position);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("WishListPresenter", error.toString());
            }
        });
        MySingleton.getInstance(((Fragment) mWishListView).getContext().getApplicationContext()).addToRequestQueue(productRequest);
    }


    @Override
    public void loadImageProduct(final int position) {
        JsonObjectRequest requestImage = new JsonObjectRequest(Request.Method.GET, URL_IMAGE + "/product/" + mWishList.get(position).getProduct().getProductId(), null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    List<ProductImage> imageList = Arrays.asList(gson.fromJson(String.valueOf(response.getJSONArray("imageList")), ProductImage[].class));
                    Log.d("WishListPresenter", "Product images: " + imageList.size());
                    if (imageList.size() > 0) {
                        mWishList.get(position).getProduct().setImageList(imageList);
                        mWishListView.setNotifyDataSetChanged();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("WishListPresenter", error.toString());
            }
        });
        MySingleton.getInstance(((Fragment) mWishListView).getContext().getApplicationContext()).addToRequestQueue(requestImage);
    }

    @Override
    public void prepareDataProductDetail(int position) {
        mWishListView.moveToProductDetail(mWishList.get(position).getProduct());
    }

    public void fetchCurrentTime() {
        JsonObjectRequest timeRequest = new JsonObjectRequest(Request.Method.GET, Constant.URL_TIME, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
                            sdf.setTimeZone(TimeZone.getTimeZone("Asia/Ho_Chi_Minh"));
                            mCurentTime = sdf.parse(response.getString("time"));
                            Log.d("ProductPresenter", "Current time: " + sdf.format(mCurentTime).toString());
                            for (int i = 0; i < mWishList.size(); i++) {
                                if (mWishList.get(i).getProduct().getSaleOff() != null) {
                                    if (mWishList.get(i).getProduct().getSaleOff().getDateStart().getTime() > mCurentTime.getTime() || mWishList.get(i).getProduct().getSaleOff().getDateEnd().getTime() < mCurentTime.getTime()) {
                                        mWishList.get(i).getProduct().getSaleOff().setDiscount(0);
                                    }
                                }
                            }
                            mWishListView.setNotifyDataSetChanged();
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
        MySingleton.getInstance(((Fragment) mWishListView).getContext().getApplicationContext()).addToRequestQueue(timeRequest);
    }

    @Override
    public void addToCart(int position) {
        Log.i("ProductDetailPresenter", "productId: " + mWishList.get(position).getProduct().getProductId());
        int sizeOld = Constant.countProductInCart();
        int sizePurchaseList = Constant.purchaseList.size();
        if (sizePurchaseList > 0) {
            int unduplicated = 0;
            for (int i = 0; i < sizePurchaseList; i++) {
                if (mWishList.get(position).getProduct().getProductId().equals(Constant.purchaseList.get(i).getProduct().getProductId())) {
                    Constant.purchaseList.get(i).setQuantity(Constant.purchaseList.get(i).getQuantity() + 1);
                } else {
                    unduplicated++;
                }
            }
            if (unduplicated == sizePurchaseList) {
                Constant.purchaseList.add(new PurchaseItem(mWishList.get(position).getProduct(), 1));
            }
        } else {
            Constant.purchaseList.add(new PurchaseItem(mWishList.get(position).getProduct(), 1));
        }
        Log.i("ProductDetailPresenter", "purchaseList: " + Constant.purchaseList.get(0).getProduct().getProductId() + " | " + Constant.purchaseList.get(0).getQuantity());
        mWishListView.addToCartAlert(Constant.countProductInCart() > sizeOld ? true : false);
    }

    @Override
    public void removeFromWishList(int position) {
        JsonObjectRequest wishListRequest = new JsonObjectRequest(Request.Method.DELETE, URL_WISHLIST + "/" + Constant.customer.getId() + "/" + mWishList.get(position).getProduct().getProductId(), null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.i("ProductDetailPresenter", response.toString());
                        loadWishList(Constant.customer.getId());
                        mWishListView.removeFromWishListAlert(true);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("ProductDetailPresenter", error.toString());
                        mWishListView.removeFromWishListAlert(false);
                    }
                });
        MySingleton.getInstance(((Fragment) mWishListView).getContext().getApplicationContext()).addToRequestQueue(wishListRequest);
    }
}
