package com.n8plus.vhiep.cyberzone.ui.productdetails.specification;

import com.n8plus.vhiep.cyberzone.data.model.Specification;

import java.util.ArrayList;
import java.util.List;

public class SpecificationPresenter implements SpecificationContract.Presenter {
    private SpecificationContract.View mSpecificationView;

    public SpecificationPresenter(SpecificationContract.View mSpecificationView) {
        this.mSpecificationView = mSpecificationView;
    }

    @Override
    public void loadData() {

    }

    @Override
    public void loadSpecification(List<Specification> specifications) {
        mSpecificationView.setAdapterSpecification(specifications);
    }

}
