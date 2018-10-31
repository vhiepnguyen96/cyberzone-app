package com.n8plus.vhiep.cyberzone.ui.manage.wishlistfollowedstore.followedstore;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import com.n8plus.vhiep.cyberzone.R;
import com.n8plus.vhiep.cyberzone.data.model.FollowStore;
import com.n8plus.vhiep.cyberzone.data.model.Store;
import com.n8plus.vhiep.cyberzone.ui.manage.wishlistfollowedstore.adapter.FollowStoreAdapter;
import com.n8plus.vhiep.cyberzone.ui.store.StoreActivity;

import java.util.List;

public class FollowedStoreFragment extends Fragment implements FollowedStoreContract.View {
    private ListView mListFollowedStore;
    private FollowStoreAdapter mFollowStoreAdapter;
    private FollowedStorePresenter mFollowedStorePresenter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.followed_store_frag, container, false);
        mFollowedStorePresenter = new FollowedStorePresenter(this);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        mListFollowedStore = (ListView) view.findViewById(R.id.lv_followed_store);

        // Presenter
        mFollowedStorePresenter.loadData();

        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void setAdapterFollowStore(List<FollowStore> followStores) {
        mFollowStoreAdapter = new FollowStoreAdapter(this, R.layout.row_followedstore, followStores);
        mListFollowedStore.setAdapter(mFollowStoreAdapter);
    }

    @Override
    public void setNotifyDataSetChanged() {
        mFollowStoreAdapter.notifyDataSetChanged();
    }

    @Override
    public void actionUnfollowStore(int position) {
        mFollowedStorePresenter.unfollowStore(position);
    }

    @Override
    public void actionGoToStore(int position) {
        mFollowedStorePresenter.goToStore(position);
    }

    @Override
    public void moveToStore(Store store) {
        Intent ACTION_GOSTORE = new Intent(this.getContext(), StoreActivity.class);
        ACTION_GOSTORE.putExtra("store", store);
        startActivity(ACTION_GOSTORE);
    }

    @Override
    public void unfollowStoreAlert(boolean b) {
        Toast.makeText(this.getContext(), b ? "Đã bỏ theo dõi gian hàng!" : "Vui lòng thử lại", Toast.LENGTH_SHORT).show();
    }
}
