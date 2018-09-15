package com.n8plus.vhiep.cyberzone.ui.deliveryaddress.loaddeliveryaddress;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;

import com.n8plus.vhiep.cyberzone.ui.checkorder.CheckOrderActivity;
import com.n8plus.vhiep.cyberzone.ui.deliveryaddress.adapter.DeliveryAddressAdapter;
import com.n8plus.vhiep.cyberzone.data.model.Address;
import com.n8plus.vhiep.cyberzone.R;
import com.n8plus.vhiep.cyberzone.ui.deliveryaddress.adddeliveryaddress.AddDeliveryAddressFragment;

import java.util.List;

public class LoadDeliveryAddressFragment extends Fragment implements LoadDeliveryAddressContract.View {
    private ListView mListDeliveryAddress;
    private Button mAddAddress;
    private ImageButton mBackCheckOrder;
    private DeliveryAddressAdapter mDeliveryAddressAdapter;
    private LoadDeliveryAddressPresenter mLoadDeliveryAddressPresenter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.all_delivery_address_frag, container, false);
        mLoadDeliveryAddressPresenter = new LoadDeliveryAddressPresenter(this);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        mListDeliveryAddress = (ListView) view.findViewById(R.id.lsv_delivery_address);
        mAddAddress = (Button) view.findViewById(R.id.btn_add_address);
        mBackCheckOrder = (ImageButton) view.findViewById(R.id.ibn_backCheckOrder);

        // Presenter
        mLoadDeliveryAddressPresenter.loadData();

        // Listener
        mAddAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.frm_delivery_address, new AddDeliveryAddressFragment());
                fragmentTransaction.commit();
            }
        });

        mBackCheckOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), CheckOrderActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent);
            }
        });

        super.onViewCreated(view, savedInstanceState);
    }


    @Override
    public void setAdapterAddress(List<Address> addressList) {
        mDeliveryAddressAdapter = new DeliveryAddressAdapter(mListDeliveryAddress.getContext(), R.layout.row_delivery_address, addressList);
        mListDeliveryAddress.setAdapter(mDeliveryAddressAdapter);
    }
}
