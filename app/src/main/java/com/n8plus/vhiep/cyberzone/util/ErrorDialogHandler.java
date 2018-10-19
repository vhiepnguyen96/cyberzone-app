package com.n8plus.vhiep.cyberzone.util;

import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;

import com.n8plus.vhiep.cyberzone.R;

public class ErrorDialogHandler {

    FragmentManager mFragmentManager;

    public ErrorDialogHandler(FragmentManager fragmentManager) {
        mFragmentManager = fragmentManager;
    }

    public void showError(String errorMessage) {
        DialogFragment fragment = ErrorDialogFragment.newInstance(
                R.string.validationErrors, errorMessage);
        fragment.show(mFragmentManager, "error");
    }
}