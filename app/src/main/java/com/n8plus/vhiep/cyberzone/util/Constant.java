package com.n8plus.vhiep.cyberzone.util;

import com.n8plus.vhiep.cyberzone.data.model.Product;
import com.n8plus.vhiep.cyberzone.data.model.PurchaseItem;

import java.util.ArrayList;
import java.util.List;

public class Constant {
//    public static String IP = "http://172.16.238.133";
//    public static String IP = "http://192.168.10.26";
    public static String IP = "http://172.16.198.84";
//    public static String IP = "http://172.20.10.2";
    public static String URL_HOST = IP + ":3000/";
    public static String URL_STRIPE = IP + ":5000/";

    public static List<PurchaseItem> purchaseList = new ArrayList<>();
    public static String customerId = "5bb1c941707cdc2e9c3ef33e";

    public static int countProductInCart() {
        int count = 0;
        for (PurchaseItem item : Constant.purchaseList) {
            count = count + item.getQuantity();
        }
        return count;
    }
}
