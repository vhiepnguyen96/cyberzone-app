package com.n8plus.vhiep.cyberzone.ui.productdetails.specification;

import com.n8plus.vhiep.cyberzone.data.model.Specification;

import java.util.ArrayList;
import java.util.List;

public class SpecificationPresenter implements SpecificationContract.Presenter {
    private SpecificationContract.View mSpecificationView;
    private List<Specification> mSpecifications;

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

    private void fakeDataSpecification() {
        mSpecifications = new ArrayList<>();
        mSpecifications.add(new Specification("Bảo hành (tháng)", "36"));
        mSpecifications.add(new Specification("Thương hiệu", "Asrock"));
        mSpecifications.add(new Specification("Model", "H110M-DVS R2.0"));
        mSpecifications.add(new Specification("Loại", "Micro-ATX"));
        mSpecifications.add(new Specification("Loại Socket", "LGA 1151"));
        mSpecifications.add(new Specification("Chipset", "Intel H110"));
        mSpecifications.add(new Specification("Số khe Ram", "2"));
        mSpecifications.add(new Specification("Dung lượng Ram tối đa", "32GB"));
        mSpecifications.add(new Specification("Loại Ram", "DDR4 2133"));
        mSpecifications.add(new Specification("Audio", "7.1 Channels"));
        mSpecifications.add(new Specification("Số cổng HDMI", "Không có"));
        mSpecifications.add(new Specification("Số cổng DVI", "Không có"));
    }
}
