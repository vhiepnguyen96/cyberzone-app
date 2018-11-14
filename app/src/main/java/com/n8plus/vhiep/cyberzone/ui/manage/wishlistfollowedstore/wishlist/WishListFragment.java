package com.n8plus.vhiep.cyberzone.ui.manage.wishlistfollowedstore.wishlist;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.n8plus.vhiep.cyberzone.R;
import com.n8plus.vhiep.cyberzone.data.model.Product;
import com.n8plus.vhiep.cyberzone.data.model.WishList;
import com.n8plus.vhiep.cyberzone.ui.manage.wishlistfollowedstore.adapter.WishListAdapter;
import com.n8plus.vhiep.cyberzone.ui.productdetails.ProductDetailActivity;

import java.util.List;

public class WishListFragment extends Fragment implements WishListContract.View {
    private ListView mWishlist;
    private WishListAdapter mWishListAdapter;
    private WishListPresenter mWishListPresenter;
    private LinearLayout mLinearNone;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.wishlist_frag, container, false);
        mWishListPresenter = new WishListPresenter(this);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        mWishlist = (ListView) view.findViewById(R.id.lv_wishlist);
        mLinearNone = (LinearLayout) view.findViewById(R.id.lnr_none);

        // Presenter
        mWishListPresenter.loadData();

        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void setLayoutNone(boolean b) {
        mLinearNone.setVisibility(b ? View.VISIBLE : View.GONE);
    }

    @Override
    public void setAdapterWishList(List<WishList> wishList) {
        mWishListAdapter = new WishListAdapter(this, R.layout.row_wishlist, wishList);
        mWishlist.setAdapter(mWishListAdapter);
    }

    @Override
    public void setNotifyDataSetChanged() {
        mWishListAdapter.notifyDataSetChanged();
    }

    @Override
    public void actionMoveToProductDetail(int position) {
        mWishListPresenter.prepareDataProductDetail(position);
    }

    @Override
    public void addToCartAlert(boolean b) {
        Toast.makeText(this.getContext(), b ? "Thêm vào giỏ hàng thành công!" : "Vui lòng kiểm tra lại giỏ hàng!", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void removeFromWishListAlert(boolean b) {
        Toast.makeText(this.getContext(), b ? "Đã xóa khỏi sách yêu thích!" : "Vui lòng thử lại!", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void moveToProductDetail(Product product) {
        Intent productDetailIntent = new Intent(this.getActivity(), ProductDetailActivity.class);
        productDetailIntent.putExtra("product", product);
        startActivity(productDetailIntent);
    }

    @Override
    public void actionRemoveFromWishList(int position) {
        mWishListPresenter.removeFromWishList(position);
    }

    @Override
    public void actionAddToCart(int position) {
        mWishListPresenter.addToCart(position);
    }
}
