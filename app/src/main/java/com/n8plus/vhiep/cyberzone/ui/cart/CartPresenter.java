package com.n8plus.vhiep.cyberzone.ui.cart;

import android.app.Activity;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.n8plus.vhiep.cyberzone.R;
import com.n8plus.vhiep.cyberzone.data.model.DeliveryPrice;
import com.n8plus.vhiep.cyberzone.data.model.Overview;
import com.n8plus.vhiep.cyberzone.data.model.Product;
import com.n8plus.vhiep.cyberzone.data.model.ProductImage;
import com.n8plus.vhiep.cyberzone.data.model.ProductType;
import com.n8plus.vhiep.cyberzone.data.model.PurchaseItem;
import com.n8plus.vhiep.cyberzone.data.model.SaleOff;
import com.n8plus.vhiep.cyberzone.data.model.Specification;
import com.n8plus.vhiep.cyberzone.data.model.Store;
import com.n8plus.vhiep.cyberzone.util.Constant;
import com.n8plus.vhiep.cyberzone.util.MySingleton;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CartPresenter implements CartContract.Presenter {
    private CartContract.View mCartView;
    private List<DeliveryPrice> deliveryPrices;
    private int deliveryPrice = 0;
    private final String URL_DELIVERY_PRICE = Constant.URL_HOST + "deliveryPrices";
    private Gson gson;
    DecimalFormat df;

    public CartPresenter(CartContract.View mCartView) {
        this.mCartView = mCartView;
    }

    @Override
    public void loadData() {
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.setDateFormat("M/d/yy hh:mm a");
        gson = gsonBuilder.create();
        df = new DecimalFormat("#.000");

        mCartView.setAdapterCart(Constant.purchaseList);
        mCartView.setTempPrice(getTempPrice() >= 1000 ? df.format(Product.convertToPrice(String.valueOf(getTempPrice()))) : String.valueOf(Math.round(getTempPrice())));
        mCartView.setProductCount(String.valueOf(Constant.countProductInCart()));
        fetchDeliveryPrice();
        mCartView.setCartNone(Constant.purchaseList.size() == 0 ? true : false);
    }

    @Override
    public void orderNow() {
        mCartView.moveToCheckOrder(Constant.purchaseList, Math.round(getTempPrice()), deliveryPrice);
    }

    private float getTempPrice() {
        float tempPrice = 0;
        for (PurchaseItem item : Constant.purchaseList) {
            if (item.getProduct().getSaleOff() != null && item.getProduct().getSaleOff().getDiscount() > 0) {
                int basicPrice = Integer.valueOf(item.getProduct().getPrice());
                int discount = item.getProduct().getSaleOff().getDiscount();
                int salePrice = basicPrice - (basicPrice * discount / 100);
                tempPrice = tempPrice + (salePrice * item.getQuantity());
            } else {
                tempPrice = tempPrice + (Integer.valueOf(item.getProduct().getPrice()) * item.getQuantity());
            }
        }
        return tempPrice;
    }


    public void fetchDeliveryPrice() {
        deliveryPrices = new ArrayList<>();
        JsonObjectRequest deliveryPriceRequest = new JsonObjectRequest(Request.Method.GET, URL_DELIVERY_PRICE, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    deliveryPrices = Arrays.asList(gson.fromJson(String.valueOf(response.getJSONArray("deliveryPrices")), DeliveryPrice[].class));
                    Log.i("CartPresenter", "Delivery price: " + deliveryPrices.size());
                    for (int i = 0; i < deliveryPrices.size(); i++) {
                        if (Constant.countProductInCart() == 0) {
                            mCartView.setDeliveryPrice("0");
                            deliveryPrice = 0;
                        } else if (Constant.countProductInCart() == deliveryPrices.get(i).getProductQuantity()) {
                            if (Integer.valueOf(deliveryPrices.get(i).getTransportFee()) >= 1000) {
                                mCartView.setDeliveryPrice(df.format(Product.convertToPrice(deliveryPrices.get(i).getTransportFee())));
                            } else {
                                mCartView.setDeliveryPrice(deliveryPrices.get(i).getTransportFee());
                            }
                            deliveryPrice = Integer.valueOf(deliveryPrices.get(i).getTransportFee());
                        } else if (Constant.countProductInCart() >= deliveryPrices.size()) {
                            if (Integer.valueOf(deliveryPrices.get(i).getTransportFee()) >= 1000) {
                                mCartView.setDeliveryPrice(df.format(Product.convertToPrice(deliveryPrices.get(deliveryPrices.size() - 1).getTransportFee())));
                            } else {
                                mCartView.setDeliveryPrice(deliveryPrices.get(deliveryPrices.size() - 1).getTransportFee());
                            }
                            deliveryPrice = Integer.valueOf(deliveryPrices.get(deliveryPrices.size() - 1).getTransportFee());
                        }
                    }
                    float totalPrice = getTempPrice() + deliveryPrice;
                    mCartView.setTotalPrice(totalPrice >= 1000 ? df.format(Product.convertToPrice(String.valueOf(totalPrice))) : String.valueOf(Math.round(totalPrice)));
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
        MySingleton.getInstance(((Activity) mCartView).getApplicationContext()).addToRequestQueue(deliveryPriceRequest);
    }


//    private void prepareProductData() {
//        List<Specification> specifications = new ArrayList<>();
//        specifications.add(new Specification("5b989eb9a6bce5234c9522ea", "Bảo hành", "36"));
//        specifications.add(new Specification("5b989eb9a6bce5234c9522ea", "Thương hiệu", "Asrock"));
//        specifications.add(new Specification("5b989eb9a6bce5234c9522ea", "Model", "H110M-DVS R2.0"));
//        specifications.add(new Specification("5b989eb9a6bce5234c9522ea", "Loại", "Micro-ATX"));
//        specifications.add(new Specification("5b989eb9a6bce5234c9522ea", "Loại Socket", "LGA 1151"));
//        specifications.add(new Specification("5b989eb9a6bce5234c9522ea", "Chipset", "Intel H110"));
//        specifications.add(new Specification("5b989eb9a6bce5234c9522ea", "Số khe Ram", "2"));
//        specifications.add(new Specification("5b989eb9a6bce5234c9522ea", "Dung lượng Ram tối đa", "32GB"));
//        specifications.add(new Specification("5b989eb9a6bce5234c9522ea", "Loại Ram", "DDR4 2133"));
//        specifications.add(new Specification("5b989eb9a6bce5234c9522ea", "VGA Onboard", "Intel HD Graphics"));
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
//        overviews.add(new Overview("5b989eb9a6bce5234c9522ea", "", "ASRock trang bị cho H110M-DVS R2.0 chuẩn linh kiện Super Alloy bền bỉ - trước đây vốn chỉ xuất hiện trên các bo mạch chủ trung cấp và cao cấp thể hiện trong thông điệp Stable and Reliable - Ổn định và tin cậy."));
//
//        Product product_1603653 = new Product("1603653", productType, store, "Bo mạch chính/ Mainboard Asrock H110M-DVS R2.0", "1.320", specifications, overviews, "New", new SaleOff("1", 5));
//        Product product_1600666 = new Product("1600666", productType, store, "Bo mạch chính/ Mainboard Gigabyte H110M-DS2 DDR4", "1.465", specifications, overviews, "New", new SaleOff("1", 6));
//        Product product_1701299 = new Product("1701299", productType, store, "Bo mạch chính/ Mainboard Gigabyte B250M-Gaming 3", "1.899", specifications, overviews, "New", new SaleOff("1", 0));
//        Product product_1704264 = new Product("1704264", productType, store, "Bo mạch chính/ Mainboard Msi A320M Bazooka", "2.180", specifications, overviews, "New", new SaleOff("1", 0));
//        Product product_1501266 = new Product("1501266", productType, store, "Bo mạch chính/ Mainboard Asus H81M-K", "1.280", specifications, overviews, "New", new SaleOff("1", 0));
//
//        purchaseItemList = new ArrayList<>();
//        purchaseItemList.add(new PurchaseItem(product_1603653, 1));
//        purchaseItemList.add(new PurchaseItem(product_1600666, 1));
//        purchaseItemList.add(new PurchaseItem(product_1701299, 1));
//        purchaseItemList.add(new PurchaseItem(product_1704264, 1));
//        purchaseItemList.add(new PurchaseItem(product_1501266, 1));
//
//    }


}
