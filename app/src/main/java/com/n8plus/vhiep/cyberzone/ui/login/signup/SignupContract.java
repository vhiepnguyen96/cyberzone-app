package com.n8plus.vhiep.cyberzone.ui.login.signup;

import com.n8plus.vhiep.cyberzone.base.BasePresenter;
import com.n8plus.vhiep.cyberzone.data.model.Account;
import com.n8plus.vhiep.cyberzone.data.model.Customer;
import com.n8plus.vhiep.cyberzone.util.TypeLogin;

public interface SignupContract {
    interface View{
        void createCustomerResult(boolean b);
        void showAlertAccountAlready();
        void moveToConfirmSignup(Customer customer);
        void moveToSignin();
    }
    interface Presenter extends BasePresenter<View> {
        void loadData();
        void loadRoleAccount();
        void signUp(Customer customer);
        void createCustomerDefault(Account account, Customer customer);
        void createCustomer(Customer customer);
    }
}
