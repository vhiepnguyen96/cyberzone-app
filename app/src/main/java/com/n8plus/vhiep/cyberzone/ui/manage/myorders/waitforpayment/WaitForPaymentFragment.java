package com.n8plus.vhiep.cyberzone.ui.manage.myorders.waitforpayment;

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
import com.n8plus.vhiep.cyberzone.data.model.Order;
import com.n8plus.vhiep.cyberzone.ui.manage.myorders.adapter.MyOrderAdapter;
import com.n8plus.vhiep.cyberzone.util.Constant;

import java.util.List;

public class WaitForPaymentFragment extends Fragment implements WaitForPaymentContract.View{
    private ListView mListWaitForPayment;
    private MyOrderAdapter mMyOrderAdapter;
    private WaitForPaymentPresenter mWaitForPaymentPresenter;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.wait_for_payment_frag, container, false);
        mWaitForPaymentPresenter = new WaitForPaymentPresenter(this);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        mListWaitForPayment = (ListView) view.findViewById(R.id.lv_wait_for_payment);

        mWaitForPaymentPresenter.loadOrderWaitToPayment(Constant.customer.getId());

        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void setAdapterWaitForPayment(List<Order> waitForPaymentList) {
        mMyOrderAdapter = new MyOrderAdapter(mListWaitForPayment.getContext(), R.layout.row_my_order, waitForPaymentList);
        mListWaitForPayment.setAdapter(mMyOrderAdapter);
    }

    @Override
    public void setNotifyDataSetChanged() {
        mMyOrderAdapter.notifyDataSetChanged();
    }
}
