package com.n8plus.vhiep.cyberzone.ui.manage.myprofile.updateprofile;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.n8plus.vhiep.cyberzone.R;
import com.n8plus.vhiep.cyberzone.data.model.Customer;
import com.n8plus.vhiep.cyberzone.ui.manage.mainmanage.MainManageFragment;
import com.n8plus.vhiep.cyberzone.ui.manage.myprofile.MyProfileFragment;
import com.n8plus.vhiep.cyberzone.ui.manage.myprofile.MyProfilePresenter;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class UpdateProfileFragment extends Fragment implements UpdateProfileContract.View {
    private TextView mBirthday;
    private EditText mEditName, mEditPhoneNumber, mEditEmail;
    private ImageView mDatePicker;
    private Button mUpdate, mBack;
    private RadioGroup mRadioGroupGender;
    private RadioButton mRadioButtonBoy, mRadioButtonGirl;
    private Toolbar mToolbarProfile;
    private UpdateProfilePresenter mUpdateProfilePresenter;
    private DatePickerDialog mDatePickerDialog;
    private Calendar calendar;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.update_profile_frag, container, false);
        mUpdateProfilePresenter = new UpdateProfilePresenter(this);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        mToolbarProfile = (Toolbar) view.findViewById(R.id.toolbar_update_profile);
        mEditName = (EditText) view.findViewById(R.id.edt_customer_name);
        mEditPhoneNumber = (EditText) view.findViewById(R.id.edt_customer_phone);
        mEditEmail = (EditText) view.findViewById(R.id.edt_customer_email);
        mRadioGroupGender = (RadioGroup) view.findViewById(R.id.rbg_gender);
        mRadioButtonBoy = (RadioButton) view.findViewById(R.id.rbn_boy);
        mRadioButtonGirl = (RadioButton) view.findViewById(R.id.rbn_girl);
        mBirthday = (TextView) view.findViewById(R.id.tv_customer_birthday);
        mDatePicker = (ImageView) view.findViewById(R.id.img_date_picker);
        mUpdate = (Button) view.findViewById(R.id.btn_update);
        mBack = (Button) view.findViewById(R.id.btn_back);

        // Custom
        setHasOptionsMenu(true);
        customActionBar();
        customDatePickerDialog();

        Bundle bundle = getArguments();
        if (bundle.getSerializable("customer") != null) {
            Customer customer = (Customer) bundle.getSerializable("customer");
            mUpdateProfilePresenter.setCustomerProfile(customer);
        }

        mDatePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDatePickerDialog.show();
            }
        });

        mUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mUpdateProfilePresenter.updateCustomer(mEditName.getText().toString(), mRadioButtonBoy.isChecked() ? "Nam" : "Nữ", calendar.getTime(), mEditPhoneNumber.getText().toString(), mEditEmail.getText().toString());
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

    private void customActionBar() {
        ((AppCompatActivity) getActivity()).setSupportActionBar(mToolbarProfile);
        ActionBar actionbar = ((AppCompatActivity) getActivity()).getSupportActionBar();
        actionbar.setDisplayHomeAsUpEnabled(true);
        actionbar.setHomeAsUpIndicator(R.drawable.ic_arrow_back_black_24dp);
        actionbar.setTitle("Cập nhật thông tin");
    }

    private void customDatePickerDialog() {
        mDatePickerDialog = new DatePickerDialog(
                this.getContext(), new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                calendar = Calendar.getInstance();
                calendar.set(year, month, dayOfMonth);
                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                mBirthday.setText(sdf.format(calendar.getTime()));
                mDatePickerDialog.dismiss();
            }
        }, Calendar.getInstance().get(Calendar.YEAR), Calendar.getInstance().get(Calendar.MONTH), Calendar.getInstance().get(Calendar.DATE));
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

    @Override
    public void setNameCustomer(String nameCustomer) {
        mEditName.setText(nameCustomer);
    }

    @Override
    public void setGenderCustomer(String genderCustomer) {
        if (genderCustomer != null) {
            mRadioButtonBoy.setChecked(genderCustomer.contains("Nam") ? true : false);
        }
    }

    @Override
    public void setBirthdayCustomer(Date birthdayCustomer) {
        if (birthdayCustomer != null) {
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
            mBirthday.setText(sdf.format(birthdayCustomer));
            long timeInMillis = birthdayCustomer.getTime();
            calendar = Calendar.getInstance();
            calendar.setTimeInMillis(timeInMillis);
        }
    }

    @Override
    public void setPhoneNumberCustomer(String phoneNumberCustomer) {
        mEditPhoneNumber.setText(phoneNumberCustomer);
    }

    @Override
    public void setEmailCustomer(String emailCustomer) {
        mEditEmail.setText(emailCustomer);
    }
}
