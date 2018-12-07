package com.n8plus.vhiep.cyberzone.util;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.n8plus.vhiep.cyberzone.data.model.Customer;
import com.n8plus.vhiep.cyberzone.data.model.Product;
import com.n8plus.vhiep.cyberzone.data.model.PurchaseItem;

import java.util.ArrayList;
import java.util.List;

public class Constant {
    //    public static String IP = "http://172.16.238.133";
//    public static String IP = "http://192.168.10.29";
//    public static String IP = "http://192.168.10.9";
//    public static String IP = "http://172.16.198.84";
    //    public static String IP = "http://172.20.10.4";
    public static String IP = "http://192.168.43.240";
    public static String URL_HOST = IP + ":3000/";
    public static final String URL_PRODUCT = Constant.URL_HOST + "products";
    public static final String URL_ORDER_ITEM = Constant.URL_HOST + "orderItems";
    public static final String URL_PRODUCT_TYPE = Constant.URL_HOST + "productTypes";
    public static final String URL_IMAGE = Constant.URL_HOST + "productImages";
    public static final String URL_FILTER = Constant.URL_HOST + "filterTypes";
    public static final String URL_REVIEW = Constant.URL_HOST + "reviewProducts";
    public static final String URL_CUSTOMER = Constant.URL_HOST + "customers";
    public static final String URL_DELIVERY_PRICE = Constant.URL_HOST + "deliveryPrices";
    public static final String URL_ADDRESS = Constant.URL_HOST + "deliveryAddresses";
    public static final String URL_CATEGORY = Constant.URL_HOST + "categories";
    public static final String URL_PAYMENT_METHOD = Constant.URL_HOST + "paymentMethods";
    public static final String URL_ORDER = Constant.URL_HOST + "orders";
    public static final String URL_ORDER_STATE = Constant.URL_HOST + "orderStates";
    public static final String URL_ACCOUNT = Constant.URL_HOST + "accounts";
    public static final String URL_ROLE = Constant.URL_HOST + "roles";
    public static final String URL_RATING_STAR = Constant.URL_HOST + "ratingStars";
    public static final String URL_RATING_LEVEL = Constant.URL_HOST + "ratingLevels";
    public static final String URL_REVIEW_PRODUCT = Constant.URL_HOST + "reviewProducts";
    public static final String URL_REVIEW_STORE = Constant.URL_HOST + "reviewStores";
    public static final String URL_REGISTER_SALE = Constant.URL_HOST + "registeredSales";
    public static final String URL_FOLLOW_STORE = Constant.URL_HOST + "followStores";
    public static final String URL_WISHLIST = Constant.URL_HOST + "wishList";
    public static final String URL_CHARGE = Constant.URL_HOST + "checkouts";
    public static final String URL_STORE = Constant.URL_HOST + "stores";

    public static String URL_TIME = "http://api.geonames.org/timezoneJSON?formatted=true&lat=10.041791&lng=105.747099&username=cyberzone&style=full";
    public static String URL_CURRENCY_RATE = "https://free.currencyconverterapi.com/api/v5/convert?q=USD_VND&compact=ultra";

    public static final String PUBLISHABLE_KEY = "pk_test_CeyyXLIDl0bfY9IiYwTIYZAU";
    public static final String SECRET_KEY = "sk_test_VWec3gEiwOvZ7wQ6BCWYCHk2";
    public static List<PurchaseItem> purchaseList = new ArrayList<>();
    public static Customer customer = null;

    public static int countProductInCart() {
        int count = 0;
        for (PurchaseItem item : Constant.purchaseList) {
            count = count + item.getQuantity();
        }
        return count;
    }

    public static int countProductQuantity(Product product) {
        int count = 0;
        for (PurchaseItem item : Constant.purchaseList) {
            if (item.getProduct().getProductId().equals(product.getProductId())) {
                count = item.getQuantity();
            }
        }
        return count;
    }
}
