package com.n8plus.vhiep.cyberzone.ui.productdetails.specification;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.n8plus.vhiep.cyberzone.ui.productdetails.adapter.SpecificationAdapter;
import com.n8plus.vhiep.cyberzone.data.model.Specification;
import com.n8plus.vhiep.cyberzone.R;

import java.util.ArrayList;
import java.util.List;

public class SpecificationFragment extends BottomSheetDialogFragment implements SpecificationContract.View {
    private ImageView mCloseBottomSheet;
    private RecyclerView mRecyclerSpecifications;
    private SpecificationAdapter mSpecificationAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private SpecificationPresenter mSpecificationPresenter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.specification_dialog, container, false);
        mSpecificationPresenter = new SpecificationPresenter(this);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        mRecyclerSpecifications = (RecyclerView) view.findViewById(R.id.rcv_specifications);
        mCloseBottomSheet = (ImageView) view.findViewById(R.id.img_closeBottomSheet);

//        mSpecificationPresenter.loadData();
        Bundle bundle = getArguments();
        if (bundle != null){
            List<Specification> specifications = (List<Specification>) bundle.get("arrSpecifications");
            mSpecificationPresenter.loadSpecification(specifications);
        }

        mCloseBottomSheet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getDialog().dismiss();
            }
        });

        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void setAdapterSpecification(List<Specification> specifications) {
        mSpecificationAdapter = new SpecificationAdapter(specifications);
        mLayoutManager = new LinearLayoutManager(mRecyclerSpecifications.getContext(), LinearLayoutManager.VERTICAL, false);
        mRecyclerSpecifications.setLayoutManager(mLayoutManager);
        mRecyclerSpecifications.setAdapter(mSpecificationAdapter);
    }
}
