package com.n8plus.vhiep.cyberzone.ui.manage.myprofile.updateprofile;

import com.n8plus.vhiep.cyberzone.base.BasePresenter;
import com.n8plus.vhiep.cyberzone.data.model.Customer;

import java.util.Date;

public interface UpdateProfileContract {
    interface View {
        void setNameCustomer(String nameCustomer);
        void setGenderCustomer(String genderCustomer);
        void setBirthdayCustomer(Date birthdayCustomer);
        void setPhoneNumberCustomer(String phoneNumberCustomer);
        void setEmailCustomer(String emailCustomer);
    }
    interface Presenter extends BasePresenter<View> {
        void setCustomerProfile(Customer customer);
        void updateCustomer(String name, String gender, Date birthday, String phone, String email);
    }
}
