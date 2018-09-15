package com.n8plus.vhiep.cyberzone.ui.payment;

public class PaymentPresenter implements PaymentContract.Presenter {
    private PaymentContract.View mPaymentView;
    private int mTotalProduct;
    private float mSubtotalPrice, mShippingFee, mTotalPrice;

    public PaymentPresenter(PaymentContract.View mPaymentView) {
        this.mPaymentView = mPaymentView;
    }

    @Override
    public void loadData() {
        fakeData();
        mPaymentView.setTotalProduct(String.valueOf(mTotalProduct));
        mPaymentView.setSubtotalPrice(String.format("%.3f", mSubtotalPrice));
        mPaymentView.setShippingFee(mShippingFee == 0 ? String.valueOf(0) : String.format("%.3f", mShippingFee));
        mPaymentView.setTotalPrice(String.format("%.3f", mTotalPrice));
    }

    private void fakeData(){
        mTotalProduct = 5;
        mSubtotalPrice = (float) 4.599;
        mShippingFee = 0;
        mTotalPrice = (float) 4.599;
    }
}
