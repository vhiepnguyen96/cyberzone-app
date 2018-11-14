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
        void setLayoutUpdatePassword(boolean b);
        void setEmailCustomer(String emailCustomer);
        void moveToUpdateProfile(Customer customer);
        void moveToUpdatePassword(Customer customer);
    }
    interface Presenter extends BasePresenter<View> {
        void loadProfile(Customer customer);
        void checkAccount(String accountId);
        void prepareDataProfile();
        void prepareDataPassword();
    }
}
