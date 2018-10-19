package com.n8plus.vhiep.cyberzone.ui.manage.myorders.allorder;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.n8plus.vhiep.cyberzone.R;
import com.n8plus.vhiep.cyberzone.data.model.Order;
import com.n8plus.vhiep.cyberzone.ui.manage.myorders.adapter.MyOrderAdapter;

import java.util.List;

public class AllOrderFragment extends Fragment implements AllOrderContract.View {
    private ListView mListAllOrder;
    private MyOrderAdapter mMyOrderAdapter;
    private AllOrderPresenter mAllOrderPresenter;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.all_order_frag, container, false);
        mAllOrderPresenter = new AllOrderPresenter(this);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        mListAllOrder = (ListView) view.findViewById(R.id.lv_all_order);

        mAllOrderPresenter.loadData();

        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void setAdapterAllOrder(List<Order> orderList) {
        mMyOrderAdapter = new MyOrderAdapter(mListAllOrder.getContext(), R.layout.row_my_order, orderList);
        mListAllOrder.setAdapter(mMyOrderAdapter);

    }
}
