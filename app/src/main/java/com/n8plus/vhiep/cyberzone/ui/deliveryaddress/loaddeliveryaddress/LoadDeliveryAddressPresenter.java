package com.n8plus.vhiep.cyberzone.ui.deliveryaddress.loaddeliveryaddress;

import com.n8plus.vhiep.cyberzone.data.model.Address;
import com.n8plus.vhiep.cyberzone.ui.deliveryaddress.DeliveryAddressContract;

import java.util.ArrayList;
import java.util.List;

public class LoadDeliveryAddressPresenter implements LoadDeliveryAddressContract.Presenter {

    private LoadDeliveryAddressContract.View mLoadDeliveryAddressView;
    private List<Address> addressList;

    public LoadDeliveryAddressPresenter(LoadDeliveryAddressContract.View mLoadDeliveryAddressView) {
        this.mLoadDeliveryAddressView = mLoadDeliveryAddressView;
    }

    @Override
    public void loadData() {
        prepareDataDeliveryAddress();
        mLoadDeliveryAddressView.setAdapterAddress(addressList);
    }

    private void prepareDataDeliveryAddress(){
        addressList = new ArrayList<>();
        addressList.add(new Address("a1", "Nguyễn Văn Hiệp", "01646158456", "Số nhà 100, Hẻm 138, Đường Trần Hưng Đạo, Phường An Nghiệp, Quận Ninh Kiều, TP Cần Thơ", true));
        addressList.add(new Address("a2", "Nguyễn Văn Hiệp", "01646158456","138/100, Đường Trần Hưng Đạo, Phường An Nghiệp, Quận Ninh Kiều, TP Cần Thơ", false));
    }
}
