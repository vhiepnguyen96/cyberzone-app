package com.n8plus.vhiep.cyberzone.ui.home;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.bignerdranch.expandablerecyclerview.Model.ParentObject;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.n8plus.vhiep.cyberzone.R;
import com.n8plus.vhiep.cyberzone.data.model.Category;
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

    private final HomeContract.View mHomeView;
    private List<ProductPurchase> productPurchases;
    private List<Product> productsSuggestion;
    private List<Product> productsBestSeller;
    private List<Product> productsOnSale;
    private List<ProductType> productTypes;
    private Date mCurentTime;
    private final String URL_PRODUCT = Constant.URL_HOST + "products";
    private final String URL_ORDER_ITEM = Constant.URL_HOST + "orderItems";
    private final String URL_PRODUCT_TYPE = Constant.URL_HOST + "productTypes";
    private final String URL_IMAGE = Constant.URL_HOST + "productImages";
    private final String URL_FILTER = Constant.URL_HOST + "filterTypes";
    private final String URL_REVIEW = Constant.URL_HOST + "reviewProducts";
    private final String URL_TIME = "http://api.geonames.org/timezoneJSON?formatted=true&lat=10.041791&lng=105.747099&username=cyberzone&style=full";
    private Gson gson;
    private int fromIndex = 0;
    private int toIndex = 9;

    public HomePresenter(HomeContract.View mHomeView) {
        this.mHomeView = mHomeView;
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.setDateFormat("M/d/yy hh:mm a");
        gson = gsonBuilder.create();
    }

    @Override
    public void loadData() {
        fetchBestSeller();
        fetchAllProduct();
    }

    @Override
    public void loadAllProductType() {
        fetchProductType();
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
        mHomeView.moveToProductActivity(productsSuggestion);
    }

    @Override
    public void prepareDataBestSeller() {
        mHomeView.moveToProductActivity(productsBestSeller);
    }

    @Override
    public void prepareDataOnSale() {
        mHomeView.moveToProductActivity(productsOnSale);
    }

    @Override
    public void prepareDataProductType(String productTypeId) {
        mHomeView.moveToProductActivity(productTypeId);
    }

    private void fetchProductType() {
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

    private void fetchBestSeller() {
        productPurchases = new ArrayList<>();
        productsBestSeller = new ArrayList<>();
        JsonObjectRequest productPurchaseRequest = new JsonObjectRequest(Request.Method.GET, URL_ORDER_ITEM + "/productPurchase", null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    productPurchases = Arrays.asList(gson.fromJson(String.valueOf(response.getJSONArray("productPurchases")), ProductPurchase[].class));
                    Log.i("HomePresenter", "GET: " + productPurchases.get(0).getCount() + " count");
                    if (productPurchases.size() > 0) {
                        for (int i = 0; i < productPurchases.size(); i++) {
                            JsonObjectRequest productRequest = new JsonObjectRequest(Request.Method.GET, URL_PRODUCT + "/" + productPurchases.get(i).getId(), null, new Response.Listener<JSONObject>() {
                                @Override
                                public void onResponse(JSONObject response) {
                                    try {
                                        final Product product = gson.fromJson(String.valueOf(response.getJSONObject("product")), Product.class);
                                        // Add product
                                        productsBestSeller.add(product);
                                        mHomeView.setAdapterBestSeller(productsBestSeller);

                                        // fetchSaleOff
                                        JsonObjectRequest timeRequest = new JsonObjectRequest(Request.Method.GET, URL_TIME, null,
                                                new Response.Listener<JSONObject>() {
                                                    @Override
                                                    public void onResponse(JSONObject response) {
                                                        try {
                                                            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
                                                            sdf.setTimeZone(TimeZone.getTimeZone("Asia/Ho_Chi_Minh"));
                                                            mCurentTime = sdf.parse(response.getString("time"));
                                                            Log.d("HomePresenter", "Current time: " + sdf.format(mCurentTime).toString());
                                                            if (product.getSaleOff() != null && (product.getSaleOff().getDateStart().getTime() > mCurentTime.getTime() || product.getSaleOff().getDateEnd().getTime() < mCurentTime.getTime())) {
                                                                product.getSaleOff().setDiscount(0);
                                                                mHomeView.setNotifyDataSetChanged("BestSeller");
                                                            }
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

                                        // fetchImage
                                        JsonObjectRequest requestImage = new JsonObjectRequest(Request.Method.GET, URL_IMAGE + "/product/" + product.getProductId(), null, new Response.Listener<JSONObject>() {
                                            @Override
                                            public void onResponse(JSONObject response) {
                                                try {
                                                    List<ProductImage> productImages = Arrays.asList(gson.fromJson(String.valueOf(response.getJSONArray("imageList")), ProductImage[].class));
                                                    Log.d("HomePresenter", "Product images: " + productImages.size());
                                                    if (productImages.size() > 0) {
                                                        product.setImageList(productImages);
                                                        mHomeView.setNotifyDataSetChanged("BestSeller");
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
                                        MySingleton.getInstance(((Activity) mHomeView).getApplicationContext()).addToRequestQueue(requestImage);

                                        // fetchReviewProduct
                                        JsonObjectRequest requestRating = new JsonObjectRequest(Request.Method.GET, URL_REVIEW + "/product/" + product.getProductId(), null, new Response.Listener<JSONObject>() {
                                            @Override
                                            public void onResponse(JSONObject response) {
                                                try {
                                                    List<ReviewProduct> reviewProducts = Arrays.asList(gson.fromJson(String.valueOf(response.getJSONArray("reviewProducts")), ReviewProduct[].class));
                                                    Log.d("HomePresenter", "Review product: " + reviewProducts.size());
                                                    if (reviewProducts.size() > 0) {
                                                        product.setReviewProducts(reviewProducts);
                                                        mHomeView.setNotifyDataSetChanged("BestSeller");
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
                                        MySingleton.getInstance(((Activity) mHomeView).getApplicationContext()).addToRequestQueue(requestRating);

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

    private void fetchAllProduct() {
        productsSuggestion = new ArrayList<>();
        JsonObjectRequest productOnSaleRequest = new JsonObjectRequest(Request.Method.GET, URL_PRODUCT, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    productsSuggestion = Arrays.asList(gson.fromJson(String.valueOf(response.getJSONArray("products")), Product[].class));
                    if (productsSuggestion.size() > 0) {
                        fetchSaleOff();
                        for (int i = 0; i < productsSuggestion.size(); i++) {
                            fetchImageProduct(i);
                            fetchReviewProduct(i);
                        }
                        mHomeView.setAdapterSuggestion(productsSuggestion);
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
        MySingleton.getInstance(((Activity) mHomeView).getApplicationContext()).addToRequestQueue(productOnSaleRequest);
    }

    public void fetchImageProduct(final int position) {
        JsonObjectRequest requestImage = new JsonObjectRequest(Request.Method.GET, URL_IMAGE + "/product/" + productsSuggestion.get(position).getProductId(), null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    List<ProductImage> productImages = Arrays.asList(gson.fromJson(String.valueOf(response.getJSONArray("imageList")), ProductImage[].class));
                    Log.d("HomePresenter", "Product images: " + productImages.size());
                    if (productImages.size() > 0) {
                        productsSuggestion.get(position).setImageList(productImages);
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
        MySingleton.getInstance(((Activity) mHomeView).getApplicationContext()).addToRequestQueue(requestImage);
    }

    public void fetchReviewProduct(final int position) {
        JsonObjectRequest requestRating = new JsonObjectRequest(Request.Method.GET, URL_REVIEW + "/product/" + productsSuggestion.get(position).getProductId(), null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    List<ReviewProduct> reviewProducts = Arrays.asList(gson.fromJson(String.valueOf(response.getJSONArray("reviewProducts")), ReviewProduct[].class));
                    Log.d("HomePresenter", "Review product: " + reviewProducts.size());
                    if (reviewProducts.size() > 0) {
                        productsSuggestion.get(position).setReviewProducts(reviewProducts);
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
        MySingleton.getInstance(((Activity) mHomeView).getApplicationContext()).addToRequestQueue(requestRating);
    }

    public void fetchSaleOff() {
        productsOnSale = new ArrayList<>();
        JsonObjectRequest timeRequest = new JsonObjectRequest(Request.Method.GET, URL_TIME, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
                            sdf.setTimeZone(TimeZone.getTimeZone("Asia/Ho_Chi_Minh"));
                            mCurentTime = sdf.parse(response.getString("time"));
                            Log.d("HomePresenter", "Current time: " + sdf.format(mCurentTime).toString());
                            for (int i = 0; i < productsSuggestion.size(); i++) {
                                if (productsSuggestion.get(i).getSaleOff() != null) {
                                    if (productsSuggestion.get(i).getSaleOff().getDateStart().getTime() > mCurentTime.getTime() || productsSuggestion.get(i).getSaleOff().getDateEnd().getTime() < mCurentTime.getTime()) {
                                        productsSuggestion.get(i).getSaleOff().setDiscount(0);
                                    }
                                    if (productsSuggestion.get(i).getSaleOff().getDiscount() > 0) {
                                        productsOnSale.add(productsSuggestion.get(i));
                                    }
                                }
                            }
                            mHomeView.setAdapterOnSale(productsOnSale);
                            mHomeView.setNotifyDataSetChanged("OnSale");
                            mHomeView.setNotifyDataSetChanged("Suggestion");
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


//    private void fakeData() {
//        productsSuggestion = new ArrayList<>();
//        List<Specification> specifications = new ArrayList<>();
//        specifications.add(new Specification("Bảo hành", "36"));
//        specifications.add(new Specification("Thương hiệu", "Asrock"));
//        specifications.add(new Specification("Model", "H110M-DVS R2.0"));
//        specifications.add(new Specification("Loại", "Micro-ATX"));
//        specifications.add(new Specification("Loại Socket", "LGA 1151"));
//        specifications.add(new Specification("Chipset", "Intel H110"));
//        specifications.add(new Specification("Số khe Ram", "2"));
//        specifications.add(new Specification("Dung lượng Ram tối đa", "32GB"));
//        specifications.add(new Specification("Loại Ram", "DDR4 2133"));
//        specifications.add(new Specification("VGA Onboard", "Intel HD Graphics"));
//
//        ProductType productType = new ProductType("5b98a6a6fe67871b2068add0", "Bo mạch chủ");
//        Store store = new Store("5b989eb9a6bce5234c9522ea", "Máy tính Phong Vũ");
//
////        List<ProductImage> imageList_1603653 = new ArrayList<>();
////        imageList_1603653.add(new ProductImage("5b98a6a6fe67871b2068add0", R.drawable.img_1603653_1));
////
////        List<ProductImage> imageList_1600666 = new ArrayList<>();
////        imageList_1600666.add(new ProductImage("5b98a6a6fe67871b2068add0", R.drawable.img_1600666));
////
////        List<ProductImage> imageList_1701299 = new ArrayList<>();
////        imageList_1701299.add(new ProductImage("5b98a6a6fe67871b2068add0", R.drawable.img_1701299));
////
////        List<ProductImage> imageList_1704264 = new ArrayList<>();
////        imageList_1704264.add(new ProductImage("5b98a6a6fe67871b2068add0", R.drawable.img_1704264));
////
////        List<ProductImage> imageList_1501266 = new ArrayList<>();
////        imageList_1501266.add(new ProductImage("5b98a6a6fe67871b2068add0", R.drawable.img_1501266));
//
//        List<Overview> overviews = new ArrayList<>();
//        overviews.add(new Overview("", "ASRock trang bị cho H110M-DVS R2.0 chuẩn linh kiện Super Alloy bền bỉ - trước đây vốn chỉ xuất hiện trên các bo mạch chủ trung cấp và cao cấp thể hiện trong thông điệp Stable and Reliable - Ổn định và tin cậy."));
//
//        Product product_1603653 = new Product("1603653", productType, store, "Bo mạch chính/ Mainboard Asrock H110M-DVS R2.0", "1320", specifications, overviews, "New", new SaleOff("1", 5));
//        Product product_1600666 = new Product("1600666", productType, store, "Bo mạch chính/ Mainboard Gigabyte H110M-DS2 DDR4", "1465", specifications, overviews, "New", new SaleOff("1", 6));
//        Product product_1701299 = new Product("1701299", productType, store, "Bo mạch chính/ Mainboard Gigabyte B250M-Gaming 3", "1899", specifications, overviews, "New", new SaleOff("1", 0));
//        Product product_1704264 = new Product("1704264", productType, store, "Bo mạch chính/ Mainboard Msi A320M Bazooka", "2180", specifications, overviews, "New", new SaleOff("1", 0));
//        Product product_1501266 = new Product("1501266", productType, store, "Bo mạch chính/ Mainboard Asus H81M-K", "1280", specifications, overviews, "New", new SaleOff("1", 0));
//
//        productsSuggestion.add(product_1603653);
//        productsSuggestion.add(product_1600666);
//        productsSuggestion.add(product_1701299);
//        productsSuggestion.add(product_1704264);
//        productsSuggestion.add(product_1501266);
//
////        productTypes = new ArrayList<>();
////        productTypes.add(new ProductType("5b98a6a6fe67871b2068add0", "Bộ xử lý - CPU", R.drawable.processor));
////        productTypes.add(new ProductType("5b98a6a6fe67871b2068add0", "Bo mạch chủ", R.drawable.motherboard));
////        productTypes.add(new ProductType("5b98a6a6fe67871b2068add0", "Card màn hình", R.drawable.vga_card));
////        productTypes.add(new ProductType("5b98a6a6fe67871b2068add0", "Nguồn - PSU", R.drawable.psu));
////        productTypes.add(new ProductType("5b98a6a6fe67871b2068add0", "Thùng máy - Case", R.drawable.case_pc));
////        productTypes.add(new ProductType("5b98a6a6fe67871b2068add0", "Tản nhiệt", R.drawable.radial_fan));
////        productTypes.add(new ProductType("5b98a6a6fe67871b2068add0", "Màn hình", R.drawable.monitor));
////        productTypes.add(new ProductType("5b98a6a6fe67871b2068add0", "RAM", R.drawable.memory));
////        productTypes.add(new ProductType("5b98a6a6fe67871b2068add0", "SSD & HDD", R.drawable.ssd_hdd));
////        productTypes.add(new ProductType("5b98a6a6fe67871b2068add0", "Chuột & Bàn phím", R.drawable.mouse));
////        productTypes.add(new ProductType("5b98a6a6fe67871b2068add0", "Webcam", R.drawable.webcam));
//    }
}
