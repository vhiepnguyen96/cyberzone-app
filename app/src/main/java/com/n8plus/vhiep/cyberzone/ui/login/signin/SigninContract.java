package com.n8plus.vhiep.cyberzone.ui.login.signin;

import com.facebook.Profile;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.n8plus.vhiep.cyberzone.base.BasePresenter;
import com.n8plus.vhiep.cyberzone.data.model.Account;
import com.n8plus.vhiep.cyberzone.data.model.Customer;
import com.n8plus.vhiep.cyberzone.util.TypeLogin;

public interface SigninContract {
    interface View{
        void showAlertAccountNotRegister();
        void onSigninDefaultSuccess(Account account);
        void onSigninDefaultFailed();
        void onSigninSuccess(String accountId);
        void onSigninFailed();
        void moveToHome();
    }
    interface Presenter extends BasePresenter<View> {
        void checkLogin(String username, String password);
        void signIn(String accountId);
    }
}
