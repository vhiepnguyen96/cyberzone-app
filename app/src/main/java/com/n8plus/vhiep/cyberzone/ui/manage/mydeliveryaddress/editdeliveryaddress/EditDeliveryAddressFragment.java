package com.n8plus.vhiep.cyberzone.ui.manage.mydeliveryaddress.editdeliveryaddress;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
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
import com.n8plus.vhiep.cyberzone.ui.manage.mainmanage.MainManageFragment;
import com.n8plus.vhiep.cyberzone.ui.manage.mydeliveryaddress.adapter.SpinnerDisrictAdapter;
import com.n8plus.vhiep.cyberzone.ui.manage.mydeliveryaddress.adapter.SpinnerProvinceAdapter;
import com.n8plus.vhiep.cyberzone.ui.manage.mydeliveryaddress.adapter.SpinnerWardAdapter;
import com.n8plus.vhiep.cyberzone.ui.manage.mydeliveryaddress.adddeliveryaddress.AddDeliveryAddressFragment;
import com.n8plus.vhiep.cyberzone.ui.manage.mydeliveryaddress.loaddeliveryaddress.LoadDeliveryAddressFragment;
import com.n8plus.vhiep.cyberzone.util.Constant;

import java.io.Serializable;
import java.util.List;

public class EditDeliveryAddressFragment extends Fragment implements EditDeliveryAddressContract.View {
    private TextInputLayout mInputName, mInputAddress, mInputPhone;
    private EditText mEditTextName, mEditTextAddress, mEditTextPhone;
    private Spinner mChooseProvince, mChooseDistrict, mChooseWard;
    private Button mEditAddress;
    private SpinnerProvinceAdapter mProvinceAdapter;
    private SpinnerDisrictAdapter mDisrictAdapter;
    private SpinnerWardAdapter mWardAdapter;
    private EditDeliveryAddressPresenter mEditDeliveryAddressPresenter;
    private String parentTitle = "";

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.edit_delivery_frag, container, false);
        mEditDeliveryAddressPresenter = new EditDeliveryAddressPresenter(this);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        mInputName = (TextInputLayout) view.findViewById(R.id.text_input_edit_name);
        mInputAddress = (TextInputLayout) view.findViewById(R.id.text_input_edit_address);
        mInputPhone = (TextInputLayout) view.findViewById(R.id.text_input_edit_phone);
        mEditTextName = (EditText) view.findViewById(R.id.edt_edit_name);
        mEditTextAddress = (EditText) view.findViewById(R.id.edt_edit_address);
        mEditTextPhone = (EditText) view.findViewById(R.id.edt_edit_phone);
        mChooseProvince = (Spinner) view.findViewById(R.id.spn_choose_edit_province);
        mChooseDistrict = (Spinner) view.findViewById(R.id.spn_choose_edit_district);
        mChooseWard = (Spinner) view.findViewById(R.id.spn_choose_edit_ward);
        mEditAddress = (Button) view.findViewById(R.id.btn_edit_delivery_address);

        // Custom
        setHasOptionsMenu(true);

        // Presenter
        Bundle bundle = getArguments();
        if (bundle != null && bundle.getSerializable("provinceList") != null && bundle.getSerializable("address") != null) {
            mEditDeliveryAddressPresenter.loadAddress((List<Province>) bundle.getSerializable("provinceList"), (Address) bundle.getSerializable("address"));
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
                mEditDeliveryAddressPresenter.loadDistrict(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        mChooseDistrict.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                mEditDeliveryAddressPresenter.loadWard(mChooseProvince.getSelectedItemPosition(), position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        mChooseWard.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                mChooseWard.setSelection(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        mEditAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String address = mEditTextAddress.getText().toString() + ", " + ((Ward) mChooseWard.getSelectedItem()).getName() + ", " + ((District) mChooseDistrict.getSelectedItem()).getName() + ", " + ((Province) mChooseProvince.getSelectedItem()).getName();
                mEditDeliveryAddressPresenter.updateDeliveryAddress(mEditTextName.getText().toString(), mEditTextPhone.getText().toString(), address);
            }
        });

        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                mEditDeliveryAddressPresenter.prepareBackLoadDeliveryAddress();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void setProvince(List<Province> provinceList, int selection) {
        mProvinceAdapter = new SpinnerProvinceAdapter(mChooseProvince.getContext(), R.layout.row_spinner_custom, provinceList);
        mChooseProvince.setAdapter(mProvinceAdapter);
        mChooseProvince.setSelection(selection);
    }

    @Override
    public void setDistrict(List<District> districtList, int selection) {
        mDisrictAdapter = new SpinnerDisrictAdapter(mChooseDistrict.getContext(), R.layout.row_spinner_custom, districtList);
        mChooseDistrict.setAdapter(mDisrictAdapter);
        mChooseDistrict.setSelection(selection);
    }

    @Override
    public void setWard(List<Ward> wardList, int selection) {
        mWardAdapter = new SpinnerWardAdapter(mChooseWard.getContext(), R.layout.row_spinner_custom, wardList);
        mChooseWard.setAdapter(mWardAdapter);
        mChooseWard.setSelection(selection);
    }

    @Override
    public void setPresentation(String presentation) {
        mEditTextName.setText(presentation);
    }

    @Override
    public void setPhoneNumber(String phoneNumber) {
        mEditTextPhone.setText(phoneNumber);
    }

    @Override
    public void setAddress(String address) {
        mEditTextAddress.setText(address);
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
