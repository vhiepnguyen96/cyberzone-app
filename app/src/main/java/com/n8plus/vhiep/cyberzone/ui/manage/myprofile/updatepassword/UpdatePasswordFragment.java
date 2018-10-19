package com.n8plus.vhiep.cyberzone.ui.manage.myprofile.updatepassword;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.n8plus.vhiep.cyberzone.R;
import com.n8plus.vhiep.cyberzone.data.model.Customer;
import com.n8plus.vhiep.cyberzone.ui.manage.myprofile.MyProfileFragment;

public class UpdatePasswordFragment extends Fragment implements UpdatePasswordContract.View {
    private Toolbar mToolbarProfile;
    private EditText mOldPassword, mNewPassword, mRetypeNewPassword;
    private Button mUpdate, mBack;
    private UpdatePasswordPresenter mUpdatePasswordPresenter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.update_password_frag, container, false);
        mUpdatePasswordPresenter = new UpdatePasswordPresenter(this);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        mToolbarProfile = (Toolbar) view.findViewById(R.id.toolbar_update_profile);
        mOldPassword = (EditText) view.findViewById(R.id.edt_old_password);
        mNewPassword = (EditText) view.findViewById(R.id.edt_new_password);
        mRetypeNewPassword = (EditText) view.findViewById(R.id.edt_retype_new_password);
        mUpdate = (Button) view.findViewById(R.id.btn_update);
        mBack = (Button) view.findViewById(R.id.btn_back);

        setHasOptionsMenu(true);
        customActionBar();

        Bundle bundle = getArguments();
        if (bundle.getSerializable("customer") != null) {
            Customer customer = (Customer) bundle.getSerializable("customer");
            mUpdatePasswordPresenter.setCustomerProfile(customer);
        }

        mUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mUpdatePasswordPresenter.updatePassword(mOldPassword.getText().toString(), mNewPassword.getText().toString(), mRetypeNewPassword.getText().toString());
            }
        });

        mBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fragmentManager = getFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.frm_manage, new MyProfileFragment());
                fragmentTransaction.commit();
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
                fragmentTransaction.replace(R.id.frm_manage, new MyProfileFragment());
                fragmentTransaction.commit();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void customActionBar() {
        ((AppCompatActivity) getActivity()).setSupportActionBar(mToolbarProfile);
        ActionBar actionbar = ((AppCompatActivity) getActivity()).getSupportActionBar();
        actionbar.setDisplayHomeAsUpEnabled(true);
        actionbar.setHomeAsUpIndicator(R.drawable.ic_arrow_back_black_24dp);
        actionbar.setTitle("Thay đổi mật khẩu");
    }

    @Override
    public void setAlert(String message) {
        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(this.getContext())
                .setTitle("Thông báo")
                .setMessage(message)
                .setCancelable(false)
                .setPositiveButton("Chấp nhận", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        alertDialog.show();
    }

    @Override
    public void resetEditText() {
        mOldPassword.setText("");
        mNewPassword.setText("");
        mRetypeNewPassword.setText("");
    }
}
