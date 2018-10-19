package com.n8plus.vhiep.cyberzone.ui.manage.myprofile.updatepassword;

import com.n8plus.vhiep.cyberzone.base.BasePresenter;
import com.n8plus.vhiep.cyberzone.data.model.Customer;

public interface UpdatePasswordContract {
    interface View {
        void setAlert(String message);
        void resetEditText();
    }

    interface Presenter extends BasePresenter<View> {
        void setCustomerProfile(Customer customer);
        void updatePassword(String oldPass, String newPass, String retypeNewPass);
    }
}
