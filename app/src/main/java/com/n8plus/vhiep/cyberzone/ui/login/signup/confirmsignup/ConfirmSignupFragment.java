package com.n8plus.vhiep.cyberzone.ui.login.signup.confirmsignup;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.n8plus.vhiep.cyberzone.R;
import com.n8plus.vhiep.cyberzone.data.model.Customer;
import com.n8plus.vhiep.cyberzone.ui.login.signin.SigninFragment;
import com.n8plus.vhiep.cyberzone.ui.login.signup.SignupFragment;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class ConfirmSignupFragment extends Fragment implements ConfirmSignupContract.View, View.OnClickListener {
    private EditText mEditName, mEditEmail, mEditPhone;
    private TextView mTextGender, mTextBirthday, mUpdateLater;
    private RadioGroup mGroupGender;
    private RadioButton mButtonBoy, mButtonGirl;
    private ImageView mDatePicker;
    private Button mUpdateCustomer;
    private DatePickerDialog mDatePickerDialog;
    private Calendar calendar;
    private ConfirmSignupPresenter mConfirmSignupPresenter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mConfirmSignupPresenter = new ConfirmSignupPresenter(this);
        return inflater.inflate(R.layout.confirm_signup_frag, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        mEditName = (EditText) view.findViewById(R.id.edt_name);
        mEditEmail = (EditText) view.findViewById(R.id.edt_email);
        mEditPhone = (EditText) view.findViewById(R.id.edt_phone);
        mGroupGender = (RadioGroup) view.findViewById(R.id.rbg_gender);
        mButtonBoy = (RadioButton) view.findViewById(R.id.rbn_boy);
        mButtonGirl = (RadioButton) view.findViewById(R.id.rbn_girl);
        mTextGender = (TextView) view.findViewById(R.id.tv_gender);
        mTextBirthday = (TextView) view.findViewById(R.id.tv_birthday);
        mUpdateLater = (TextView) view.findViewById(R.id.tv_update_later);
        mDatePicker = (ImageView) view.findViewById(R.id.img_date_picker);
        mUpdateCustomer = (Button) view.findViewById(R.id.btn_update);

        Bundle signupBundle = getArguments();
        if (signupBundle != null) {
            Customer customer = (Customer) signupBundle.getSerializable("customer");
            mConfirmSignupPresenter.setDataCustomer(customer);
        }

        customDatePickerDialog();

        mDatePicker.setOnClickListener(this);
        mUpdateCustomer.setOnClickListener(this);
        mUpdateLater.setOnClickListener(this);

        super.onViewCreated(view, savedInstanceState);
    }

    private void customDatePickerDialog() {
        mDatePickerDialog = new DatePickerDialog(
                this.getContext(), new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                calendar = Calendar.getInstance();
                calendar.set(year, month, dayOfMonth);
                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                mTextBirthday.setText(sdf.format(calendar.getTime()));
                mDatePickerDialog.dismiss();
            }
        }, Calendar.getInstance().get(Calendar.YEAR), Calendar.getInstance().get(Calendar.MONTH), Calendar.getInstance().get(Calendar.DATE));
    }

    @Override
    public void setNameCustomer(String nameCustomer) {
        mEditName.setText(nameCustomer);
    }

    @Override
    public void setGenderCustomer(String genderCustomer) {
        if (genderCustomer != null) {
            if (genderCustomer.equals("Nam")) {
                mButtonBoy.setChecked(true);
            } else {
                mButtonGirl.setChecked(false);
            }
        }
    }

    @Override
    public void setBirthdayCustomer(String birthdayCustomer) {
        mTextBirthday.setText(birthdayCustomer);
    }

    @Override
    public void setEmailCustomer(String emailCustomer) {
        mEditEmail.setText(emailCustomer);
    }

    @Override
    public void setPhoneCustomer(String phoneCustomer) {
        mEditPhone.setText(phoneCustomer);
    }

    @Override
    public void updateCustomerResult(boolean b) {
        Toast.makeText(this.getContext(), b ? "Cập nhật thành công!" : "Cập nhật thất bại!", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void backToSignin() {
        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.frm_login, new SigninFragment());
        fragmentTransaction.commit();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.img_date_picker:
                mDatePickerDialog.show();
                break;
            case R.id.btn_update:
                updateCustomer();
                break;
            case R.id.tv_update_later:
                backToSignin();
                break;
        }
    }

    private void updateCustomer() {
        if (validate()) {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
            try {
                String name = mEditName.getText().toString();
                String gender = mGroupGender.getCheckedRadioButtonId() == mButtonBoy.getId() ? "Nam" : "Nữ";
                Date birthday = simpleDateFormat.parse(mTextBirthday.getText().toString());
                String email = mEditEmail.getText().toString();
                String phone = mEditPhone.getText().toString();
                mConfirmSignupPresenter.updateCustomer(new Customer(name, gender, birthday, email, phone));
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
    }

    private boolean validate() {
        boolean valid = true;

        if (mEditName.getText().toString().isEmpty()) {
            valid = false;
            mEditName.setError("Không được để trống");
        }

        if (!mButtonBoy.isChecked() && !mButtonGirl.isChecked()) {
            valid = false;
            showAlert("Vui lòng chọn giới tính");
        }

        if (mEditEmail.getText().toString().isEmpty()) {
            valid = false;
            mEditEmail.setError("Không được để trống");
        }

        if (mEditPhone.getText().toString().isEmpty()) {
            valid = false;
            mEditPhone.setError("Không được để trống");
        }

        return valid;
    }

    private void showAlert(String message) {
        Toast.makeText(this.getContext(), message, Toast.LENGTH_SHORT).show();
    }
}
