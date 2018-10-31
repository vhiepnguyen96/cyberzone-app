package com.n8plus.vhiep.cyberzone.ui.login.signin;

import com.facebook.Profile;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.n8plus.vhiep.cyberzone.base.BasePresenter;
import com.n8plus.vhiep.cyberzone.data.model.Account;

public interface SigninContract {
    interface View{
        void onSigninDefaultSuccess(Account account);
        void onSigninDefaultFailed();
        void onSigninGoogleSuccess(GoogleSignInAccount googleAccount);
        void onSigninGoogleFailed();
        void onSigninFacebookSuccess(Profile profile);
        void onSigninFacebookFailed();
        void moveToHome();
    }
    interface Presenter extends BasePresenter<View> {
        void checkLogin(String username, String password);
    }
}
