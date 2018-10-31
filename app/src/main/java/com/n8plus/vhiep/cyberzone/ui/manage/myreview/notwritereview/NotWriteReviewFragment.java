package com.n8plus.vhiep.cyberzone.ui.manage.myreview.notwritereview;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.n8plus.vhiep.cyberzone.R;
import com.n8plus.vhiep.cyberzone.ui.manage.myreview.notwritereview.allpurchase.AllPurchaseFragment;

public class NotWriteReviewFragment extends Fragment implements NotWriteReviewContract.View {
    FragmentTransaction fragmentTransaction;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.not_write_review_frag, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        fragmentTransaction = getFragmentManager().beginTransaction();
        fragmentTransaction.add(R.id.frm_not_write_review, new AllPurchaseFragment());
        fragmentTransaction.commit();
        super.onViewCreated(view, savedInstanceState);
    }
}