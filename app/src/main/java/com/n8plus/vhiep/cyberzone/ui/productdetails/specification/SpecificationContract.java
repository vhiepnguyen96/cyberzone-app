package com.n8plus.vhiep.cyberzone.ui.productdetails.specification;

import com.n8plus.vhiep.cyberzone.base.BasePresenter;
import com.n8plus.vhiep.cyberzone.data.model.Specification;

import java.util.List;

public interface SpecificationContract {
    interface View {
        void setAdapterSpecification(List<Specification> specifications);
    }
    interface Presenter extends BasePresenter<View>{
        @Override
        void loadData();
    }
}
