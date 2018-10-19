package com.n8plus.vhiep.cyberzone.ui.manage.myprofile;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.n8plus.vhiep.cyberzone.R;
import com.n8plus.vhiep.cyberzone.data.model.Customer;
import com.n8plus.vhiep.cyberzone.ui.cart.CartActivity;
import com.n8plus.vhiep.cyberzone.ui.home.HomeActivity;
import com.n8plus.vhiep.cyberzone.ui.manage.mainmanage.MainManageFragment;
import com.n8plus.vhiep.cyberzone.ui.manage.myprofile.updatepassword.UpdatePasswordFragment;
import com.n8plus.vhiep.cyberzone.ui.manage.myprofile.updateprofile.UpdateProfileFragment;
import com.n8plus.vhiep.cyberzone.util.Constant;

import java.text.SimpleDateFormat;
import java.util.Date;

public class MyProfileFragment extends Fragment implements MyProfileContract.View {
    private Toolbar mToolbarProfile;
    private TextView mName, mGender, mBirthday, mPhoneNumber, mEmail;
    private Button mUpdateProfile, mChangePassword;
    private MyProfilePresenter myProfilePresenter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.my_profile_frag, container, false);
        myProfilePresenter = new MyProfilePresenter(this);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        mToolbarProfile = (Toolbar) view.findViewById(R.id.toolbar_profile);
        mName = (TextView) view.findViewById(R.id.tv_customer_name);
        mGender = (TextView) view.findViewById(R.id.tv_customer_gender);
        mBirthday = (TextView) view.findViewById(R.id.tv_customer_birthday);
        mPhoneNumber = (TextView) view.findViewById(R.id.tv_customer_phone);
        mEmail = (TextView) view.findViewById(R.id.tv_customer_email);
        mUpdateProfile = (Button) view.findViewById(R.id.btn_update_customer);
        mChangePassword = (Button) view.findViewById(R.id.btn_change_password);
        setHasOptionsMenu(true);

        // Custom action bar
        ((AppCompatActivity) getActivity()).setSupportActionBar(mToolbarProfile);
        ActionBar actionbar = ((AppCompatActivity) getActivity()).getSupportActionBar();
        actionbar.setDisplayHomeAsUpEnabled(true);
        actionbar.setHomeAsUpIndicator(R.drawable.ic_arrow_back_black_24dp);
        actionbar.setTitle("Quản lý tài khoản");

        myProfilePresenter.loadProfile(Constant.customerId);

        mUpdateProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myProfilePresenter.prepareDataProfile();
            }
        });

        mChangePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myProfilePresenter.prepareDataPassword();
            }
        });

        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                FragmentManager fragmentManager = getFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.frm_manage, new MainManageFragment());
                fragmentTransaction.commit();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void setNameCustomer(String nameCustomer) {
        mName.setText(nameCustomer);
    }

    @Override
    public void setGenderCustomer(String genderCustomer) {
        mGender.setText(genderCustomer);
    }

    @Override
    public void setBirthdayCustomer(Date birthdayCustomer) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        if (birthdayCustomer != null){
            mBirthday.setText(sdf.format(birthdayCustomer.getTime()));
        }
    }

    @Override
    public void setPhoneNumberCustomer(String phoneNumberCustomer) {
        mPhoneNumber.setText(phoneNumberCustomer);
    }

    @Override
    public void setEmailCustomer(String emailCustomer) {
        mEmail.setText(emailCustomer);
    }

    @Override
    public void moveToUpdateProfile(Customer customer) {
        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        UpdateProfileFragment updateProfileFragment = new UpdateProfileFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable("customer", customer);
        updateProfileFragment.setArguments(bundle);
        fragmentTransaction.replace(R.id.frm_manage, updateProfileFragment);
        fragmentTransaction.commit();
    }

    @Override
    public void moveToUpdatePassword(Customer customer) {
        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        UpdatePasswordFragment updatePasswordFragment = new UpdatePasswordFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable("customer", customer);
        updatePasswordFragment.setArguments(bundle);
        fragmentTransaction.replace(R.id.frm_manage, updatePasswordFragment);
        fragmentTransaction.commit();
    }

}
