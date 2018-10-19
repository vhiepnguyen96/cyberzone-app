package com.n8plus.vhiep.cyberzone.ui.manage.mydeliveryaddress.loaddeliveryaddress;

import com.n8plus.vhiep.cyberzone.base.BasePresenter;
import com.n8plus.vhiep.cyberzone.data.model.Address;
import com.n8plus.vhiep.cyberzone.data.model.Province;

import java.util.List;

public interface LoadDeliveryAddressContract {
    interface View{
        void setAdapterAddress(List<Address> addressList);
        void moveToAddDeliveryAddress(List<Province> provinceList);
        void moveToEditDeliveryAddress(List<Province> provinceList, Address address);
        void deleteAddressSuccess();
        void deleteAddressFailure();
        void moveToCheckOrder(Address address);
    }
    interface Presenter extends BasePresenter<View>{
        void loadAllDeliveryAddress(String customerId);
        void loadProvince();
        void loadProvinceList(List<Province> provinceList);
        void prepareDataAddAddress();
        void prepareDataEditAddress(int position);
        void deleteDeliveryAddress(int position);
        void prepareDataChooseAddress(int position);
    }
}
