package com.n8plus.vhiep.cyberzone.ui.manage.mydeliveryaddress.loaddeliveryaddress;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import com.n8plus.vhiep.cyberzone.data.model.Province;
import com.n8plus.vhiep.cyberzone.ui.cart.CartActivity;
import com.n8plus.vhiep.cyberzone.ui.checkorder.CheckOrderActivity;
import com.n8plus.vhiep.cyberzone.ui.choosedeliveryaddress.ChooseDeliveryAddressActivity;
import com.n8plus.vhiep.cyberzone.ui.choosedeliveryaddress.adapter.DeliveryAddressAdapter;
import com.n8plus.vhiep.cyberzone.data.model.Address;
import com.n8plus.vhiep.cyberzone.R;
import com.n8plus.vhiep.cyberzone.ui.manage.mydeliveryaddress.adddeliveryaddress.AddDeliveryAddressFragment;
import com.n8plus.vhiep.cyberzone.ui.manage.mydeliveryaddress.editdeliveryaddress.EditDeliveryAddressFragment;
import com.n8plus.vhiep.cyberzone.util.Constant;

import java.io.Serializable;
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

        // Presenter
        mLoadDeliveryAddressPresenter.loadAllDeliveryAddress(Constant.customerId);
        Bundle bundle = getArguments();
        if (bundle != null && bundle.getSerializable("provinceList") != null){
            mLoadDeliveryAddressPresenter.loadProvinceList((List<Province>) bundle.getSerializable("provinceList"));
            Toast.makeText(this.getContext(), "Có provinceList", Toast.LENGTH_SHORT).show();
        } else {
            mLoadDeliveryAddressPresenter.loadProvince();
            Toast.makeText(this.getContext(), "Ko có provinceList", Toast.LENGTH_SHORT).show();
        }

        // Listener
        mAddAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mLoadDeliveryAddressPresenter.prepareDataAddAddress();
            }
        });

        mListDeliveryAddress.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ActionBar actionbar = ((AppCompatActivity) getActivity()).getSupportActionBar();
                String title = (String) actionbar.getTitle();
                if (title.equals("Chọn địa chỉ giao hàng")){
                    mLoadDeliveryAddressPresenter.prepareDataChooseAddress(position);
                }
            }
        });

        super.onViewCreated(view, savedInstanceState);
    }


    @Override
    public void setAdapterAddress(List<Address> addressList) {
        mDeliveryAddressAdapter = new DeliveryAddressAdapter(this, R.layout.row_delivery_address, addressList);
        mListDeliveryAddress.setAdapter(mDeliveryAddressAdapter);
    }

    @Override
    public void moveToAddDeliveryAddress(List<Province> provinceList) {
        AddDeliveryAddressFragment fragment = new AddDeliveryAddressFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable("provinceList", (Serializable) provinceList);
        bundle.putString("title", (String) ((AppCompatActivity) getActivity()).getSupportActionBar().getTitle());
        fragment.setArguments(bundle);
        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.frm_delivery_address, fragment);
        fragmentTransaction.commit();
        // Custom action bar
        ActionBar actionbar = ((AppCompatActivity) getActivity()).getSupportActionBar();
        actionbar.setTitle("Thêm địa chỉ giao hàng");
    }

    @Override
    public void moveToEditDeliveryAddress(List<Province> provinceList, Address address) {
        EditDeliveryAddressFragment fragment = new EditDeliveryAddressFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable("provinceList", (Serializable) provinceList);
        bundle.putSerializable("address", (Serializable) address);
        bundle.putString("title", (String) ((AppCompatActivity) getActivity()).getSupportActionBar().getTitle());
        fragment.setArguments(bundle);
        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.frm_delivery_address, fragment);
        fragmentTransaction.commit();
        // Custom action bar
        ActionBar actionbar = ((AppCompatActivity) getActivity()).getSupportActionBar();
        actionbar.setTitle("Chỉnh sửa địa chỉ giao hàng");
    }

    @Override
    public void deleteAddressSuccess() {
        Toast.makeText(this.getContext(), "Xóa thành công", Toast.LENGTH_SHORT).show();
        mLoadDeliveryAddressPresenter.loadAllDeliveryAddress(Constant.customerId);
    }

    @Override
    public void deleteAddressFailure() {
        Toast.makeText(this.getContext(), "Xóa thất bại", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void moveToCheckOrder(Address address) {
        Intent intent = new Intent(getActivity(), CheckOrderActivity.class);
        intent.putExtra("address", address);
        getActivity().setResult(Activity.RESULT_OK, intent);
        getActivity().finish();
    }

    public void editDeliveryAddress(int position){
        mLoadDeliveryAddressPresenter.prepareDataEditAddress(position);
    }

    public void deleteDeliveryAddress(final int position){
        AlertDialog.Builder builder = new AlertDialog.Builder(this.getContext());
        builder.setTitle("Xác nhận");
        builder.setMessage("Bạn muốn xóa địa chỉ này?");
        builder.setCancelable(false);
        builder.setPositiveButton("Chấp nhận", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                mLoadDeliveryAddressPresenter.deleteDeliveryAddress(position);
            }
        });
        builder.setNegativeButton("Hủy", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }
}
