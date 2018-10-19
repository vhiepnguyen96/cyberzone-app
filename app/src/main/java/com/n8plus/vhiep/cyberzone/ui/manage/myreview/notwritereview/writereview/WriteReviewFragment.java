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
import com.n8plus.vhiep.cyberzone.ui.manage.myreview.notwritereview.allpurchase.AllPurchaseFragment;

public class WriteReviewFragment extends Fragment {
    private TextView mChangeName, mBackAllPurchase;
    private EditText mNameReview;
    RatingBar mRatingBar;
    private RadioGroup mRadioGroup;
    private RadioButton mRadioButtonGood, mRadioButtonNormal, mRadioButtonNotGood;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.write_review_frag, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        mBackAllPurchase = (TextView) view.findViewById(R.id.tv_back_all_purchase);
        mChangeName = (TextView) view.findViewById(R.id.tv_change_name_review);
        mNameReview = (EditText) view.findViewById(R.id.edt_name_review);
        mRatingBar = (RatingBar) view.findViewById(R.id.rbr_rate_average);
        mRadioGroup = (RadioGroup) view.findViewById(R.id.rbg_store_review);
        mRadioButtonGood = (RadioButton) view.findViewById(R.id.rbn_good);
        mRadioButtonNormal = (RadioButton) view.findViewById(R.id.rbn_normal);
        mRadioButtonNotGood = (RadioButton) view.findViewById(R.id.rbn_notgood);

        mChangeName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mChangeName.getText().equals("Thay đổi")) {
                    mNameReview.setEnabled(true);
                    mNameReview.setBackgroundResource(R.drawable.border_bottom_no_padding);
                    mChangeName.setText("Chấp nhận");
                } else {
                    mNameReview.setEnabled(false);
                    mNameReview.setBackgroundColor(Color.TRANSPARENT);
                    mChangeName.setText("Thay đổi");
                }
            }
        });

        mRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
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
            }
        });

        mBackAllPurchase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.frm_not_write_review, new AllPurchaseFragment());
                fragmentTransaction.commit();
            }
        });
        super.onViewCreated(view, savedInstanceState);

    }

}
