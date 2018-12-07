package com.n8plus.vhiep.cyberzone.ui.manage.registersale.addregistersale;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.n8plus.vhiep.cyberzone.R;
import com.n8plus.vhiep.cyberzone.data.model.Province;
import com.n8plus.vhiep.cyberzone.ui.manage.mydeliveryaddress.adapter.SpinnerProvinceAdapter;
import com.n8plus.vhiep.cyberzone.ui.manage.registersale.loadregistersale.LoadRegisterSaleFragment;

import java.util.List;

public class AddRegisterSaleFragment extends Fragment implements AddRegisterSaleContract.View {
    private AddRegisterSalePresenter mAddRegisterSalePresenter;
    private Spinner mChooseProvince;
    private SpinnerProvinceAdapter mProvinceAdapter;
    private EditText mEditNameStore, mEditNameCustomer, mEditEmail, mEditPhone, mEditStoreAccount, mEditPassword, mEditConfirmPassword;
    private Button mButtonSendRegister;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mAddRegisterSalePresenter = new AddRegisterSalePresenter(getContext(),this);
        return inflater.inflate(R.layout.add_register_sale_frag, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        mChooseProvince = (Spinner) view.findViewById(R.id.spn_register_address);
        mEditNameStore = (EditText) view.findViewById(R.id.edt_register_name_store);
        mEditNameCustomer = (EditText) view.findViewById(R.id.edt_register_name_customer);
        mEditEmail = (EditText) view.findViewById(R.id.edt_register_email);
        mEditPhone = (EditText) view.findViewById(R.id.edt_register_phone);
        mEditStoreAccount = (EditText) view.findViewById(R.id.edt_register_store_account);
        mEditPassword = (EditText) view.findViewById(R.id.edt_register_password);
        mEditConfirmPassword = (EditText) view.findViewById(R.id.edt_register_confirm_password);
        mButtonSendRegister = (Button) view.findViewById(R.id.btn_send_register_sale);

        // Custom
        this.getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

        // Presenter
        mAddRegisterSalePresenter.loadData();

        // Listener
        mButtonSendRegister.setOnClickListener(v -> {
            if (checkIsValid()) {
                String nameStore = mEditNameStore.getText().toString();
                String locationStore = ((Province) mChooseProvince.getSelectedItem()).getName();
                String nameCustomer = mEditNameCustomer.getText().toString();
                String phone = mEditPhone.getText().toString();
                String email = mEditEmail.getText().toString();
                String username = mEditStoreAccount.getText().toString();
                String password = mEditPassword.getText().toString();
                mAddRegisterSalePresenter.sendRegisterSale(nameStore, locationStore, nameCustomer, phone, email, username, password);
            }
        });

        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void setProvince(List<Province> provinceList) {
        mProvinceAdapter = new SpinnerProvinceAdapter(mChooseProvince.getContext(), R.layout.row_spinner_custom, provinceList);
        mChooseProvince.setAdapter(mProvinceAdapter);
    }

    @Override
    public void setNameCustomer(String nameCustomer) {
        mEditNameCustomer.setText(nameCustomer);
    }

    @Override
    public boolean checkIsValid() {
        boolean isValid = true;
        if (mEditNameStore.getText().toString().isEmpty()) {
            showAlert("Vui lòng nhập tên gian hàng!");
            isValid = false;
        } else if (((Province) mChooseProvince.getSelectedItem()).getName().isEmpty()) {
            showAlert("Vui lòng chọn địa chỉ kho!");
            isValid = false;
        } else if (mEditNameCustomer.getText().toString().isEmpty()) {
            showAlert("Vui lòng nhập tên người đại diện!");
            isValid = false;
        } else if (mEditPhone.getText().toString().isEmpty()) {
            showAlert("Vui lòng nhập số điện thoại!");
            isValid = false;
        } else if (mEditEmail.getText().toString().isEmpty()) {
            showAlert("Vui lòng nhập địa chỉ email!");
            isValid = false;
        } else if (mEditStoreAccount.getText().toString().isEmpty()) {
            showAlert("Vui lòng nhập tên đăng nhập!");
            isValid = false;
        } else if (mEditPassword.getText().toString().isEmpty() || mEditConfirmPassword.getText().toString().isEmpty()) {
            showAlert("Vui lòng nhập mật khẩu!");
            isValid = false;
        } else if (!mEditPassword.getText().toString().equals(mEditConfirmPassword.getText().toString())) {
            showAlert("Mật khẩu không trùng khớp!");
            isValid = false;
        }
        return isValid;
    }

    @Override
    public void showAlert(String message) {
        Toast.makeText(this.getContext(), message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void sendRegisterSaleResult(boolean b) {
        Toast.makeText(this.getContext(), b ? "Gửi đơn đăng ký thành công!" : "Vui lòng thử lại", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void backLoadRegisterSale() {
        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.frm_register_sale, new LoadRegisterSaleFragment());
        fragmentTransaction.commit();
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Đơn đăng ký bán hàng");
    }
}
