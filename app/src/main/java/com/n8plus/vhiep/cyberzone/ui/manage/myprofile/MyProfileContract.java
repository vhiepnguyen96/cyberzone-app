package com.n8plus.vhiep.cyberzone.ui.manage.myprofile;

import com.n8plus.vhiep.cyberzone.base.BasePresenter;
import com.n8plus.vhiep.cyberzone.data.model.Customer;

import java.util.Date;

public interface MyProfileContract {
    interface View {
        void setNameCustomer(String nameCustomer);
        void setGenderCustomer(String genderCustomer);
        void setBirthdayCustomer(Date birthdayCustomer);
        void setPhoneNumberCustomer(String phoneNumberCustomer);
        void setEmailCustomer(String emailCustomer);
        void moveToUpdateProfile(Customer customer);
        void moveToUpdatePassword(Customer customer);
    }
    interface Presenter extends BasePresenter<View> {
        void loadProfile(String customerId);
        void prepareDataProfile();
        void prepareDataPassword();
    }
}
