package com.n8plus.vhiep.cyberzone.ui.manage.mydeliveryaddress.adddeliveryaddress;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

import com.n8plus.vhiep.cyberzone.R;
import com.n8plus.vhiep.cyberzone.data.model.Address;
import com.n8plus.vhiep.cyberzone.data.model.District;
import com.n8plus.vhiep.cyberzone.data.model.Province;
import com.n8plus.vhiep.cyberzone.data.model.Ward;
import com.n8plus.vhiep.cyberzone.ui.manage.mydeliveryaddress.adapter.SpinnerDisrictAdapter;
import com.n8plus.vhiep.cyberzone.ui.manage.mydeliveryaddress.adapter.SpinnerProvinceAdapter;
import com.n8plus.vhiep.cyberzone.ui.manage.mydeliveryaddress.adapter.SpinnerWardAdapter;
import com.n8plus.vhiep.cyberzone.ui.manage.mydeliveryaddress.loaddeliveryaddress.LoadDeliveryAddressFragment;
import com.n8plus.vhiep.cyberzone.ui.manage.mydeliveryaddress.MyDeliveryAddressFragment;
import com.n8plus.vhiep.cyberzone.util.Constant;

import java.io.Serializable;
import java.util.List;

public class AddDeliveryAddressFragment extends Fragment implements AddDeliveryAddressContract.View {

    private TextInputLayout mInputName, mInputAddress, mInputPhone;
    private EditText mEditTextName, mEditTextAddress, mEditTextPhone;
    private Button mSaveDeliveryAddress;
    private AddDeliveryAddressPresenter mAddDeliveryAddressPresenter;
    private Spinner mChooseProvince, mChooseDistrict, mChooseWard;
    private SpinnerProvinceAdapter mProvinceAdapter;
    private SpinnerDisrictAdapter mDisrictAdapter;
    private SpinnerWardAdapter mWardAdapter;
    private int screen = 0;
    private String parentTitle = "";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.add_new_delivery_frag, container, false);
        mAddDeliveryAddressPresenter = new AddDeliveryAddressPresenter(this);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        mInputName = (TextInputLayout) view.findViewById(R.id.text_input_name);
        mInputAddress = (TextInputLayout) view.findViewById(R.id.text_input_address);
        mInputPhone = (TextInputLayout) view.findViewById(R.id.text_input_phone);
        mEditTextName = (EditText) view.findViewById(R.id.edt_name);
        mEditTextAddress = (EditText) view.findViewById(R.id.edt_address);
        mEditTextPhone = (EditText) view.findViewById(R.id.edt_phone);
        mChooseProvince = (Spinner) view.findViewById(R.id.spn_choose_province);
        mChooseDistrict = (Spinner) view.findViewById(R.id.spn_choose_district);
        mChooseWard = (Spinner) view.findViewById(R.id.spn_choose_ward);
        mSaveDeliveryAddress = (Button) view.findViewById(R.id.btn_save_delivery_address);

        // Custom
        setHasOptionsMenu(true);
        mChooseProvince.setSelected(true);
        mChooseDistrict.setSelected(false);
        mChooseWard.setSelected(false);

        // Presenter
        Bundle bundle = getArguments();
        if (bundle != null && bundle.getSerializable("provinceList") != null) {
            mAddDeliveryAddressPresenter.loadProvice((List<Province>) bundle.getSerializable("provinceList"));
        }
        if (bundle != null && bundle.getString("title") != null) {
            parentTitle = bundle.getString("title");
        }

        mEditTextName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() == 0) {
                    mInputName.setError("Không để trống trường này!");
                } else {
                    mInputName.setError(null);
                }
            }
        });

        mEditTextAddress.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() == 0) {
                    mInputAddress.setError("Không để trống trường này!");
                } else {
                    mInputAddress.setError(null);
                }
            }
        });

        mEditTextPhone.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() == 0) {
                    mInputPhone.setError("Không để trống trường này!");
                } else {
                    mInputPhone.setError(null);
                }
            }
        });

        mChooseProvince.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                mAddDeliveryAddressPresenter.loadDistrict(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        mChooseDistrict.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                mAddDeliveryAddressPresenter.loadWard(mChooseProvince.getSelectedItemPosition(), position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        mChooseWard.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        mSaveDeliveryAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validateInput()) {
                    String address = mEditTextAddress.getText().toString() + ", " + ((Ward) mChooseWard.getSelectedItem()).getName() + ", " + ((District) mChooseDistrict.getSelectedItem()).getName() + ", " + ((Province) mChooseProvince.getSelectedItem()).getName();
                    mAddDeliveryAddressPresenter.addDeliveryAddress(Constant.customer.getId(), new Address(mEditTextName.getText().toString(), mEditTextPhone.getText().toString(), address));
                }
            }
        });

        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                mAddDeliveryAddressPresenter.prepareBackLoadDeliveryAddress();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void setProvince(List<Province> provinceList) {
        mProvinceAdapter = new SpinnerProvinceAdapter(mChooseProvince.getContext(), R.layout.row_spinner_custom, provinceList);
        mChooseProvince.setAdapter(mProvinceAdapter);
    }

    @Override
    public void setDistrict(List<District> districtList) {
        mDisrictAdapter = new SpinnerDisrictAdapter(mChooseDistrict.getContext(), R.layout.row_spinner_custom, districtList);
        mChooseDistrict.setAdapter(mDisrictAdapter);
        mChooseDistrict.setSelected(true);
    }

    @Override
    public void setWard(List<Ward> wardList) {
        mWardAdapter = new SpinnerWardAdapter(mChooseWard.getContext(), R.layout.row_spinner_custom, wardList);
        mChooseWard.setAdapter(mWardAdapter);
        mChooseWard.setSelected(true);
    }

    @Override
    public boolean validateInput() {
        boolean isValid = true;
        if (mEditTextName.getText().toString().isEmpty()) {
            isValid = false;
            mInputName.setError("Không để trống trường này!");
        }
        if (mEditTextPhone.getText().toString().isEmpty()) {
            isValid = false;
            mInputPhone.setError("Không để trống trường này!");
        }
        if (mEditTextAddress.getText().toString().isEmpty()) {
            isValid = false;
            mInputAddress.setError("Không để trống trường này!");
        }
        return isValid;
    }

    @Override
    public void clearAllData() {
        mEditTextName.setText("");
        mInputName.setError(null);
        mEditTextName.clearFocus();
        mEditTextPhone.setText("");
        mInputPhone.setError(null);
        mEditTextPhone.clearFocus();
        mEditTextAddress.setText("");
        mInputAddress.setError(null);
        mEditTextAddress.clearFocus();
    }

    @Override
    public void backLoadDeliveryAddress(List<Province> provinceList) {
        LoadDeliveryAddressFragment fragment = new LoadDeliveryAddressFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable("provinceList", (Serializable) provinceList);
        fragment.setArguments(bundle);
        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.frm_delivery_address, fragment);
        fragmentTransaction.commit();

        ActionBar actionbar = ((AppCompatActivity) getActivity()).getSupportActionBar();
        actionbar.setTitle(parentTitle.equals("Chọn địa chỉ giao hàng") ? "Chọn địa chỉ giao hàng" : "Quản lý địa chỉ giao hàng");
    }
}
