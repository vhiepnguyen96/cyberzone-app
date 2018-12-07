package com.n8plus.vhiep.cyberzone.ui.manage.myreview.notwritereview.writereview;

import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.n8plus.vhiep.cyberzone.R;
import com.n8plus.vhiep.cyberzone.data.model.Product;
import com.n8plus.vhiep.cyberzone.data.model.PurchaseItem;
import com.n8plus.vhiep.cyberzone.ui.manage.myreview.notwritereview.allpurchase.AllPurchaseFragment;

public class WriteReviewFragment extends Fragment implements WriteReviewContract.View {
    private TextView mBackAllPurchase, mSendReview, mProductName, mStoreName;
    private EditText mReviewProduct, mReviewStore, mNameReview;
    private RatingBar mRatingBar;
    private RadioGroup mRadioGroup;
    private RadioButton mRadioButtonGood, mRadioButtonNormal, mRadioButtonNotGood;
    private WriteReviewPresenter mWriteReviewPresenter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.write_review_frag, container, false);
        mWriteReviewPresenter = new WriteReviewPresenter(getContext(),this);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        mBackAllPurchase = (TextView) view.findViewById(R.id.tv_back_all_purchase);
        mSendReview = (TextView) view.findViewById(R.id.tv_send_review);
        mProductName = (TextView) view.findViewById(R.id.tv_product_name_purchase);
        mStoreName = (TextView) view.findViewById(R.id.tv_store_name);
        mNameReview = (EditText) view.findViewById(R.id.edt_name_review);
        mRatingBar = (RatingBar) view.findViewById(R.id.rbr_rate_average);
        mRadioGroup = (RadioGroup) view.findViewById(R.id.rbg_store_review);
        mRadioButtonGood = (RadioButton) view.findViewById(R.id.rbn_good);
        mRadioButtonNormal = (RadioButton) view.findViewById(R.id.rbn_normal);
        mRadioButtonNotGood = (RadioButton) view.findViewById(R.id.rbn_notgood);
        mReviewProduct = (EditText) view.findViewById(R.id.edt_review_product);
        mReviewStore = (EditText) view.findViewById(R.id.edt_review_store);

        // Presenter
        Bundle bundle = getArguments();
        if (bundle != null && bundle.getSerializable("orderItem") != null) {
            mWriteReviewPresenter.loadDataReview((PurchaseItem) bundle.getSerializable("orderItem"));
        }

        mRadioGroup.setOnCheckedChangeListener((group, checkedId) -> {
            switch (checkedId) {
                case R.id.rbn_good:
                    mRadioButtonGood.setChecked(true);
                    mRadioButtonNormal.setChecked(false);
                    mRadioButtonNotGood.setChecked(false);
                    break;
                case R.id.rbn_normal:
                    mRadioButtonNormal.setChecked(true);
                    mRadioButtonGood.setChecked(false);
                    mRadioButtonNotGood.setChecked(false);
                    break;
                case R.id.rbn_notgood:
                    mRadioButtonNotGood.setChecked(true);
                    mRadioButtonNormal.setChecked(false);
                    mRadioButtonGood.setChecked(false);
                    break;
            }
        });

        mBackAllPurchase.setOnClickListener(v -> {
            backAllPurchase();
        });

        mSendReview.setOnClickListener(v -> {
            if (checkIsValidReview()){
                mWriteReviewPresenter.sendReview(mReviewProduct.getText().toString(), (int) mRatingBar.getRating(), mReviewStore.getText().toString(), getLevelRadioGroup(mRadioGroup.getCheckedRadioButtonId()));
            }
        });

        super.onViewCreated(view, savedInstanceState);

    }

    @Override
    public void setProductName(String productName) {
        mProductName.setText(productName);
    }

    @Override
    public void setStoreName(String storeName) {
        mStoreName.setText(storeName);
    }

    @Override
    public void setCustomerName(String customerName) {
        mNameReview.setText(customerName);
    }

    @Override
    public void sendReviewResult(boolean b) {
        Toast.makeText(this.getContext(), b ? "Gửi nhận xét thành công!" : "Gửi nhận xét thất bại!", Toast.LENGTH_SHORT).show();
    }

    @Override
    public int getLevelRadioGroup(int id) {
        switch (id) {
            case R.id.rbn_good:
                return 1;
            case R.id.rbn_normal:
                return 2;
            case R.id.rbn_notgood:
                return 3;
        }
        return 0;
    }

    @Override
    public boolean checkIsValidReview() {
        boolean isValid = true;
        if (mRatingBar.getRating() == 0) {
            setAlert("Vui lòng chọn số sao cho sản phẩm!");
            isValid = false;
        } else if (mReviewProduct.getText().toString().isEmpty()) {
            setAlert("Vui lòng viết đánh giá cho sản phẩm!");
            isValid = false;
        } else if (mRadioGroup.getCheckedRadioButtonId() == 0) {
            setAlert("Vui lòng chọn mức đánh giá cho gian hàng!");
            isValid = false;
        } else if (mReviewStore.getText().toString().isEmpty()) {
            setAlert("Vui lòng viết đánh giá cho gian hàng!");
            isValid = false;
        }
        return isValid;
    }

    @Override
    public void setAlert(String message) {
        Toast.makeText(this.getContext(), message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void backAllPurchase() {
        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.frm_not_write_review, new AllPurchaseFragment());
        fragmentTransaction.commit();
    }
}
