package com.n8plus.vhiep.cyberzone.ui.manage.mydeliveryaddress.editdeliveryaddress;

import com.n8plus.vhiep.cyberzone.base.BasePresenter;
import com.n8plus.vhiep.cyberzone.data.model.Address;
import com.n8plus.vhiep.cyberzone.data.model.District;
import com.n8plus.vhiep.cyberzone.data.model.Province;
import com.n8plus.vhiep.cyberzone.data.model.Ward;

import java.util.List;

public interface EditDeliveryAddressContract {
    interface View{
        void setProvince(List<Province> provinceList, int selection);
        void setDistrict(List<District> districtList, int selection);
        void setWard(List<Ward> wardList, int selection);
        void setPresentation(String presentation);
        void setPhoneNumber(String phoneNumber);
        void setAddress(String address);
        void backLoadDeliveryAddress(List<Province> provinceList);
        void updateDeliveryAddressResult(boolean b);
    }
    interface Presenter extends BasePresenter<View>{
        void loadProvice(List<Province> provinceList);
        void loadDistrict(int locationProvince);
        void loadWard(int locationProvince, int locationDistrict);
        void loadAddress(List<Province> provinceList, Address address);
        void updateDeliveryAddress(String presentation, String phone, String address);
        void prepareBackLoadDeliveryAddress();
    }
}
