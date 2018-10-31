package com.n8plus.vhiep.cyberzone.ui.login.signup.confirmsignup;

import com.n8plus.vhiep.cyberzone.base.BasePresenter;
import com.n8plus.vhiep.cyberzone.data.model.Customer;
import com.n8plus.vhiep.cyberzone.ui.login.signup.SignupContract;

public interface ConfirmSignupContract {
    interface View {
        void setNameCustomer(String nameCustomer);
        void setGenderCustomer(String genderCustomer);
        void setBirthdayCustomer(String birthdayCustomer);
        void setEmailCustomer(String emailCustomer);
        void setPhoneCustomer(String phoneCustomer);
        void updateCustomerResult(boolean b);
        void backToSignin();
    }

    interface Presenter extends BasePresenter<View> {
        void setDataCustomer(Customer customer);
        void updateCustomer(Customer customer);
    }
}
