package com.n8plus.vhiep.cyberzone.ui.manage.wishlistfollowedstore.wishlist;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
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
import com.n8plus.vhiep.cyberzone.util.VolleyUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import static com.n8plus.vhiep.cyberzone.util.Constant.URL_WISHLIST;

public class WishListPresenter implements WishListContract.Presenter {
    private static final String TAG = "WishListPresenter";
    private Context context;
    private WishListContract.View mWishListView;
    private List<WishList> mWishList;
    private Gson gson;
    private Date mCurentTime;

    public WishListPresenter(@NonNull final Context context, @NonNull final WishListContract.View mWishListView) {
        this.context = context;
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
        mWishListView.setLayoutLoading(true);
        VolleyUtil.GET(context, Constant.URL_WISHLIST + "/customer/" + customerId,
                response -> {
                    try {
                        mWishList = Arrays.asList(gson.fromJson(String.valueOf(response.getJSONArray("wishList")), WishList[].class));
                        Log.d(TAG, "wishList: " + mWishList.size());
                        mWishListView.setLayoutLoading(false);

                        if (mWishList.size() > 0) {
                            mWishListView.setLayoutNone(false);
                            mWishListView.setAdapterWishList(mWishList);
                            fetchCurrentTime();
                            for (int i = 0; i < mWishList.size(); i++) {
                                if (mWishList.get(i).getProduct() != null) {
                                    loadProduct(i);
                                }
                            }
                        } else {
                            mWishListView.setLayoutNone(true);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                },
                error -> {
                    mWishListView.setLayoutNone(true);
                    mWishListView.setLayoutLoading(false);
                });
    }

    @Override
    public void loadProduct(final int position) {
        VolleyUtil.GET(context, Constant.URL_PRODUCT + "/" + mWishList.get(position).getProduct().getProductId(),
                response -> {
                    try {
                        Product product = gson.fromJson(String.valueOf(response.getJSONObject("product")), Product.class);
                        if (product != null) {
                            mWishList.get(position).setProduct(product);
                            loadImageProduct(position);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                },
                error -> Log.e(TAG, error.toString()));
    }


    @Override
    public void loadImageProduct(final int position) {
        VolleyUtil.GET(context, Constant.URL_IMAGE + "/product/" + mWishList.get(position).getProduct().getProductId(),
                response -> {
                    try {
                        List<ProductImage> imageList = Arrays.asList(gson.fromJson(String.valueOf(response.getJSONArray("imageList")), ProductImage[].class));
                        Log.d(TAG, "Product images: " + imageList.size());
                        if (imageList.size() > 0) {
                            mWishList.get(position).getProduct().setImageList(imageList);
                            mWishListView.setNotifyDataSetChanged();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                },
                error -> Log.e(TAG, error.toString()));
    }

    @Override
    public void prepareDataProductDetail(int position) {
        mWishListView.moveToProductDetail(mWishList.get(position).getProduct());
    }

    public void fetchCurrentTime() {
        VolleyUtil.GET(context, Constant.URL_TIME,
                response -> {
                    try {
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
                        sdf.setTimeZone(TimeZone.getTimeZone("Asia/Ho_Chi_Minh"));
                        mCurentTime = sdf.parse(response.getString("time"));
                        Log.d(TAG, "Current time: " + sdf.format(mCurentTime).toString());
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
                },
                error -> Log.e(TAG, error.toString()));
    }

    @Override
    public void addToCart(int position) {
        Log.i(TAG, "productId: " + mWishList.get(position).getProduct().getProductId());
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
        Log.i(TAG, "purchaseList: " + Constant.purchaseList.get(0).getProduct().getProductId() + " | " + Constant.purchaseList.get(0).getQuantity());
        mWishListView.addToCartAlert(Constant.countProductInCart() > sizeOld ? true : false);
    }

    @Override
    public void removeFromWishList(int position) {
        VolleyUtil.DELETE(context, Constant.URL_WISHLIST + "/" + Constant.customer.getId() + "/" + mWishList.get(position).getProduct().getProductId(),
                response -> {
                    Log.i(TAG, response.toString());
                    loadWishList(Constant.customer.getId());
                    mWishListView.removeFromWishListAlert(true);
                },
                error -> {
                    Log.e(TAG, error.toString());
                    mWishListView.removeFromWishListAlert(false);
                });
    }
}
