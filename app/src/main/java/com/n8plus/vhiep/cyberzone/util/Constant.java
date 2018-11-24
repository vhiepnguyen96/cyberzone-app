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
//    public static String IP = "http://192.168.10.56";
    public static String IP = "http://172.16.198.84";
    //    public static String IP = "http://172.20.10.4";
//    public static String IP = "http://192.168.43.188";
    public static String URL_HOST = IP + ":3000/";
    public static String URL_TIME = "http://api.geonames.org/timezoneJSON?formatted=true&lat=10.041791&lng=105.747099&username=cyberzone&style=full";

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
