package com.n8plus.vhiep.cyberzone.ui.manage.wishlistfollowedstore;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.n8plus.vhiep.cyberzone.R;
import com.n8plus.vhiep.cyberzone.ui.manage.mainmanage.MainManageFragment;
import com.n8plus.vhiep.cyberzone.ui.manage.myprofile.MyProfileContract;

public class WishlistFollowedstoreFragment extends Fragment implements WishlistFollowedstoreContract.View{
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.my_wishlist_and_followedstore_frag, container, false);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        TextView mTextView = (TextView) view.findViewById(R.id.tv_wl_fs_back);

        mTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fragmentManager = getFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.frm_manage, new MainManageFragment());
                fragmentTransaction.commit();
            }
        });
        super.onViewCreated(view, savedInstanceState);
    }
}