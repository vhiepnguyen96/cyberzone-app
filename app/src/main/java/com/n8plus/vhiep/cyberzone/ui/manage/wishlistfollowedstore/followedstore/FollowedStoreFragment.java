package com.n8plus.vhiep.cyberzone.ui.manage.wishlistfollowedstore.followedstore;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.n8plus.vhiep.cyberzone.R;
import com.n8plus.vhiep.cyberzone.data.model.Store;
import com.n8plus.vhiep.cyberzone.ui.manage.wishlistfollowedstore.adapter.FollowStoreAdapter;

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
    public void setAdapterFollowStore(List<Store> storeList) {
        mFollowStoreAdapter = new FollowStoreAdapter(mListFollowedStore.getContext(), R.layout.row_followedstore, storeList);
        mListFollowedStore.setAdapter(mFollowStoreAdapter);
    }
}
