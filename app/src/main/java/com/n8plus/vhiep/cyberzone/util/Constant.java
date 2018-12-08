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
    public static String IP_SERVER = "https://api.jsonbin.io/b/5c0b6a8dc10055104ca9e0fe";
    public static String URL_HOST;
    public static String URL_PRODUCT, URL_ORDER_ITEM, URL_PRODUCT_TYPE, URL_IMAGE, URL_FILTER
            , URL_REVIEW, URL_CUSTOMER, URL_DELIVERY_PRICE, URL_ADDRESS, URL_CATEGORY, URL_PAYMENT_METHOD
            , URL_ORDER, URL_ORDER_STATE, URL_ACCOUNT, URL_ROLE, URL_RATING_STAR, URL_RATING_LEVEL
            , URL_REVIEW_PRODUCT, URL_REVIEW_STORE, URL_REGISTER_SALE, URL_FOLLOW_STORE, URL_WISHLIST
            , URL_CHARGE, URL_STORE;

    public static final String URL_TIME = "http://api.geonames.org/timezoneJSON?formatted=true&lat=10.041791&lng=105.747099&username=cyberzone&style=full";
    public static final String URL_CURRENCY_RATE = "https://free.currencyconverterapi.com/api/v5/convert?q=USD_VND&compact=ultra";
    public static final String PUBLISHABLE_KEY = "pk_test_CeyyXLIDl0bfY9IiYwTIYZAU";
    public static final String SECRET_KEY = "sk_test_VWec3gEiwOvZ7wQ6BCWYCHk2";

    public static List<PurchaseItem> purchaseList = new ArrayList<>();
    public static Customer customer = null;

    public static void changeIP(String IP_HOST) {
        URL_PRODUCT = IP_HOST + "products";
        URL_ORDER_ITEM = IP_HOST + "orderItems";
        URL_PRODUCT_TYPE = IP_HOST + "productTypes";
        URL_IMAGE = IP_HOST + "productImages";
        URL_FILTER = IP_HOST + "filterTypes";
        URL_REVIEW = IP_HOST + "reviewProducts";
        URL_CUSTOMER = IP_HOST + "customers";
        URL_DELIVERY_PRICE = IP_HOST + "deliveryPrices";
        URL_ADDRESS = IP_HOST + "deliveryAddresses";
        URL_CATEGORY = IP_HOST + "categories";
        URL_PAYMENT_METHOD = IP_HOST + "paymentMethods";
        URL_ORDER = IP_HOST + "orders";
        URL_ORDER_STATE = IP_HOST + "orderStates";
        URL_ACCOUNT = IP_HOST + "accounts";
        URL_ROLE = IP_HOST + "roles";
        URL_RATING_STAR = IP_HOST + "ratingStars";
        URL_RATING_LEVEL = IP_HOST + "ratingLevels";
        URL_REVIEW_PRODUCT = IP_HOST + "reviewProducts";
        URL_REVIEW_STORE = IP_HOST + "reviewStores";
        URL_REGISTER_SALE = IP_HOST + "registeredSales";
        URL_FOLLOW_STORE = IP_HOST + "followStores";
        URL_WISHLIST = IP_HOST + "wishList";
        URL_CHARGE = IP_HOST + "checkouts";
        URL_STORE = IP_HOST + "stores";
    }

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

    //    public static String URL_PRODUCT = Constant.URL_HOST + "products";
//    public static String URL_ORDER_ITEM = Constant.URL_HOST + "orderItems";
//    public static String URL_PRODUCT_TYPE = Constant.URL_HOST + "productTypes";
//    public static String URL_IMAGE = Constant.URL_HOST + "productImages";
//    public static String URL_FILTER = Constant.URL_HOST + "filterTypes";
//    public static String URL_REVIEW = Constant.URL_HOST + "reviewProducts";
//    public static String URL_CUSTOMER = Constant.URL_HOST + "customers";
//    public static String URL_DELIVERY_PRICE = Constant.URL_HOST + "deliveryPrices";
//    public static String URL_ADDRESS = Constant.URL_HOST + "deliveryAddresses";
//    public static String URL_CATEGORY = Constant.URL_HOST + "categories";
//    public static String URL_PAYMENT_METHOD = Constant.URL_HOST + "paymentMethods";
//    public static String URL_ORDER = Constant.URL_HOST + "orders";
//    public static String URL_ORDER_STATE = Constant.URL_HOST + "orderStates";
//    public static String URL_ACCOUNT = Constant.URL_HOST + "accounts";
//    public static String URL_ROLE = Constant.URL_HOST + "roles";
//    public static String URL_RATING_STAR = Constant.URL_HOST + "ratingStars";
//    public static String URL_RATING_LEVEL = Constant.URL_HOST + "ratingLevels";
//    public static String URL_REVIEW_PRODUCT = Constant.URL_HOST + "reviewProducts";
//    public static String URL_REVIEW_STORE = Constant.URL_HOST + "reviewStores";
//    public static String URL_REGISTER_SALE = Constant.URL_HOST + "registeredSales";
//    public static String URL_FOLLOW_STORE = Constant.URL_HOST + "followStores";
//    public static String URL_WISHLIST = Constant.URL_HOST + "wishList";
//    public static String URL_CHARGE = Constant.URL_HOST + "checkouts";
//    public static String URL_STORE = Constant.URL_HOST + "stores";
}
