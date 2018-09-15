package com.n8plus.vhiep.cyberzone.ui.deliveryaddress.loaddeliveryaddress;

import com.n8plus.vhiep.cyberzone.base.BasePresenter;
import com.n8plus.vhiep.cyberzone.data.model.Address;

import java.util.List;

public interface LoadDeliveryAddressContract {
    interface View{
        void setAdapterAddress(List<Address> addressList);
    }
    interface Presenter extends BasePresenter<View>{

    }
}
