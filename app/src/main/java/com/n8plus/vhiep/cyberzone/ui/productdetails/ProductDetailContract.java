package com.n8plus.vhiep.cyberzone.ui.productdetails;

import com.n8plus.vhiep.cyberzone.base.BasePresenter;
import com.n8plus.vhiep.cyberzone.data.model.Overview;
import com.n8plus.vhiep.cyberzone.data.model.Policy;
import com.n8plus.vhiep.cyberzone.data.model.Product;
import com.n8plus.vhiep.cyberzone.data.model.ProductImage;
import com.n8plus.vhiep.cyberzone.data.model.ReviewProduct;
import com.n8plus.vhiep.cyberzone.data.model.Specification;
import com.n8plus.vhiep.cyberzone.data.model.Store;

import java.util.List;

public interface ProductDetailContract {
    interface View {
        void setImageList(List<ProductImage> imageList);
        void setProductName(String name);
        void setProductPrice(String price);
        void setProductPriceSale(String priceSale);
        void setLayoutPriceSale(boolean b);
        void setProductDiscount(int discount);
        void setProductSpecification(List<Specification> specifications);
        void setProductOverviews(List<Overview> overviews);
        void setProductPolicies(List<Policy> policies);
        void setStoreName(String storeName);
        void setStoreLocation(String location);
        void setAdapterRatingProduct(List<ReviewProduct> ratingProductList);
        void setRatingBar(int total, float rating);
        void setRating5star(int max, int count);
        void setRating4star(int max, int count);
        void setRating3star(int max, int count);
        void setRating2star(int max, int count);
        void setRating1star(int max, int count);
        void setLayoutAddToCart(int quantity);
        void setReviewNone(boolean b);
        void setLayoutRatingProduct(boolean b);
        void moveToStore(Store store);
        void moveToCart();
        void addToCartAlert(boolean b);
        void setCartMenuItem();
        void setWishListResult(boolean b);
        void addToWishListResult(boolean b);
        void removeFromWishListResult(boolean b);
        void setAlert(String message);
    }
    interface Presenter extends BasePresenter<View> {
        void loadProductDetail(Product product);
        void loadWishList();
        int getCurrentQuantity();
        void addToWishList();
        void removeFromWishList();
        void buyNow();
        void addToCart();
        void prepareDataStore();
    }
}
