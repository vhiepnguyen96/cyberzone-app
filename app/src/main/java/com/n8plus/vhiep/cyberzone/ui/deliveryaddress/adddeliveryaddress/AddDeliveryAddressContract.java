package com.n8plus.vhiep.cyberzone.ui.deliveryaddress.adddeliveryaddress;

import com.n8plus.vhiep.cyberzone.base.BasePresenter;

public interface AddDeliveryAddressContract {
    interface View{
        void setTextLoadJson(String json);
    }
    interface Presenter extends BasePresenter<View>{
        void loadProvice();
        void loadDistrict(String provinceCode);
        void loadWard(String districtCode);
    }
}