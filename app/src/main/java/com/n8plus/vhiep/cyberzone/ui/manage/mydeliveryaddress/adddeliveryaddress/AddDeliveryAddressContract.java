package com.n8plus.vhiep.cyberzone.ui.manage.mydeliveryaddress.adddeliveryaddress;

import com.n8plus.vhiep.cyberzone.base.BasePresenter;
import com.n8plus.vhiep.cyberzone.data.model.Address;
import com.n8plus.vhiep.cyberzone.data.model.District;
import com.n8plus.vhiep.cyberzone.data.model.Province;
import com.n8plus.vhiep.cyberzone.data.model.Ward;

import java.util.List;

public interface AddDeliveryAddressContract {
    interface View{
        void setProvince(List<Province> provinceList);
        void setDistrict(List<District> districtList);
        void setWard(List<Ward> wardList);
        boolean validateInput();
        void clearAllData();
        void backLoadDeliveryAddress(List<Province> provinceList);
    }
    interface Presenter extends BasePresenter<View>{
        void loadProvice(List<Province> provinceList);
        void loadDistrict(int locationProvince);
        void loadWard(int locationProvince, int locationDistrict);
        void addDeliveryAddress(String customerId, Address address);
        void prepareBackLoadDeliveryAddress();
    }
}