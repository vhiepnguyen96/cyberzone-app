package com.n8plus.vhiep.cyberzone.ui.manage.registersale.addregistersale;

import com.n8plus.vhiep.cyberzone.base.BasePresenter;
import com.n8plus.vhiep.cyberzone.data.model.Province;

import java.util.List;

public interface AddRegisterSaleContract {
    interface View {
        void setProvince(List<Province> provinceList);
        void setNameCustomer(String nameCustomer);
        boolean checkIsValid();
        void showAlert(String message);
        void sendRegisterSaleResult(boolean b);
        void backLoadRegisterSale();
    }
    interface Presenter extends BasePresenter<View> {
        void loadData();
        void loadProvince();
        void sendRegisterSale(String nameStore, String locationStore, String nameCustomer, String phone, String email, String storeAccount, String password);
    }
}
