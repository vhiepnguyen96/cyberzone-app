package com.n8plus.vhiep.cyberzone.ui.manage.registersale.loadregistersale;

import com.n8plus.vhiep.cyberzone.base.BasePresenter;
import com.n8plus.vhiep.cyberzone.data.model.RegisterSale;

import java.util.List;

public interface LoadRegisterSaleContract {
    interface View {
        void setLayoutNone(boolean b);
        void setAdapterRegisterSale(List<RegisterSale> registerSales);
    }
    interface Presenter extends BasePresenter<View> {
        void loadData();
        void loadAllRegisterSale(String customerId);
    }
}
