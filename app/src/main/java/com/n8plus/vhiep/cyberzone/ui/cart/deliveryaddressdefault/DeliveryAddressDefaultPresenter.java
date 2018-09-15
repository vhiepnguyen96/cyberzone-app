package com.n8plus.vhiep.cyberzone.ui.cart.deliveryaddressdefault;

import com.n8plus.vhiep.cyberzone.data.model.Address;

public class DeliveryAddressDefaultPresenter implements DeliveryAddressDefaultContract.Presenter{
    private DeliveryAddressDefaultContract.View mDeliveryAddressDefaultView;
    private Address mDeliveryAddress;

    public DeliveryAddressDefaultPresenter(DeliveryAddressDefaultContract.View mDeliveryAddressDefaultView) {
        this.mDeliveryAddressDefaultView = mDeliveryAddressDefaultView;
    }

    @Override
    public void loadData() {
        fakeData();
        mDeliveryAddressDefaultView.setNameCustomer(mDeliveryAddress.getNameCustomer());
        mDeliveryAddressDefaultView.setPhoneCustomer(mDeliveryAddress.getPhone());
        mDeliveryAddressDefaultView.setAddressCustomer(mDeliveryAddress.getAddress());
    }

    private void fakeData(){
        mDeliveryAddress = new Address("1", "Nguyễn Văn A", "01646158456","Số nhà 100, Đường Trần Hưng Đạo, Phường An Nghiệp, Quận Ninh Kiều, TP Cần Thơ", true);
    }
}
