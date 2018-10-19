package com.n8plus.vhiep.cyberzone.ui.product;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.BaseAdapter;
import android.widget.ListView;

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
        fetchFilter();
        mProductView.setAdapterProduct(productList);
    }

    @Override
    public void loadProductByProductType(String productTypeId) {
        fetchProduct(productTypeId);
    }

    @Override
    public void loadProductByKeyWord(String keyword) {
        Header header = new Header("name", keyword);
        fetchProduct(header);
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
                        mProductView.setAdapterProduct(products);
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
                        mProductView.setAdapterProduct(products);
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

    public void fetchProduct(Header header) {
        products = new ArrayList<>();
        JsonObjectRequest productRequestKeyWord = new JsonObjectRequest(Request.Method.POST, URL_PRODUCT + "/findByName/" + header.getValue(), null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    products = Arrays.asList(gson.fromJson(String.valueOf(response.getJSONArray("products")), Product[].class));
                    Log.i("ProductPresenter", "GET: " + products.size() + " products");
                    if (products.size() > 0) {
                        mProductView.setAdapterProduct(products);
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
//                        mProductView.setNotifyDataSetChanged();
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
//                        mProductView.setNotifyDataSetChanged();
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

//    public void fakeDataProduct() {
//        products = new ArrayList<>();
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
//        List<Overview> overviews = new ArrayList<>();
//        overviews.add(new Overview("", "ASRock trang bị cho H110M-DVS R2.0 chuẩn linh kiện Super Alloy bền bỉ - trước đây vốn chỉ xuất hiện trên các bo mạch chủ trung cấp và cao cấp thể hiện trong thông điệp Stable and Reliable - Ổn định và tin cậy."));
//        overviews.add(new Overview("", "Phụ kiện đi kèm cơ bản nhưng đầy đủ: gồm sách hướng dẫn, đĩa cài driver, I/O ( miếng chặn main) và 2 cáp SATA3 6Gb/s."));
//        overviews.add(new Overview("", "H110M-DVS R2.0 có size M-ATX thường thấy của phân khúc mainboard giá thấp. Với cái nhìn đầu tiên, chiếc mainboard khá đẹp mắt với tone mạch màu đen bóng, có thêm sắc cam từ miếng tản nhiệt chipset."));
//        overviews.add(new Overview("", "ASRock sử dụng công nghệ Super Alloy cho các sản phẩm mainboard của mình, PCB của bo mạch chủ được xây dựng từ sợi thủy tinh mật độ cao (High Density Glass Fabric PCB) giúp cho sản phẩm giảm thiểu hư hỏng hoặc rò rỉ điện khi gặp môi trường có độ ẩm cao - đặc biệt thật sự cần thiết cho thị trường Việt Nam, một quốc gia với khí hậu nóng ẩm."));
//
//        ProductType productType = new ProductType("5b98a6a6fe67871b2068add0", "Bo mạch chủ");
//        Store store = new Store("5b989eb9a6bce5234c9522ea", "Máy tính Phong Vũ");
//
//        Product product_1603653 = new Product("1603653", productType, store, "Bo mạch chính/ Mainboard Asrock H110M-DVS R2.0", "1.320", specifications, overviews, "New", new SaleOff("1", 5));
//        Product product_1600666 = new Product("1600666", productType, store, "Bo mạch chính/ Mainboard Gigabyte H110M-DS2 DDR4", "1.465", specifications, overviews, "New", new SaleOff("1", 6));
//        Product product_1701299 = new Product("1701299", productType, store, "Bo mạch chính/ Mainboard Gigabyte B250M-Gaming 3", "1.899", specifications, overviews, "New", new SaleOff("1", 0));
//        Product product_1704264 = new Product("1704264", productType, store, "Bo mạch chính/ Mainboard Msi A320M Bazooka", "2.180", specifications, overviews, "New", new SaleOff("1", 0));
//        Product product_1501266 = new Product("1501266", productType, store, "Bo mạch chính/ Mainboard Asus H81M-K", "1.280", specifications, overviews, "New", new SaleOff("1", 0));
//
//        products.add(product_1603653);
//        products.add(product_1600666);
//        products.add(product_1701299);
//        products.add(product_1704264);
//        products.add(product_1501266);
//
//        products.add(product_1603653);
//        products.add(product_1600666);
//        products.add(product_1701299);
//        products.add(product_1704264);
//        products.add(product_1501266);
//    }
}
