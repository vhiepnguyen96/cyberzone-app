package com.n8plus.vhiep.cyberzone.ui.deliveryaddress;

public class DeliveryAddressPresenter implements DeliveryAddressContract.Presenter {
    private DeliveryAddressContract.View mDeliveryAddressView;

    public DeliveryAddressPresenter(DeliveryAddressContract.View mDeliveryAddressView) {
        this.mDeliveryAddressView = mDeliveryAddressView;
    }

    @Override
    public void loadData() {

    }
}
