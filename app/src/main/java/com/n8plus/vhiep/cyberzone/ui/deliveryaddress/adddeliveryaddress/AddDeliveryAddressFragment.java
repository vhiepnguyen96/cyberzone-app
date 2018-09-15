package com.n8plus.vhiep.cyberzone.ui.deliveryaddress.adddeliveryaddress;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;

import com.n8plus.vhiep.cyberzone.R;
import com.n8plus.vhiep.cyberzone.ui.deliveryaddress.loaddeliveryaddress.LoadDeliveryAddressFragment;

public class AddDeliveryAddressFragment extends Fragment implements AddDeliveryAddressContract.View{

    private ImageButton mCloseAddDelivery;
    private TextInputLayout mInputName, mInputAddress, mInputPhone;
    private EditText mEditTextName, mEditTextAddress, mEditTextPhone;
    private AddDeliveryAddressPresenter mAddDeliveryAddressPresenter;
    private Spinner mChooseProvince, mChooseDistrict, mChooseWard;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.add_new_delivery_frag, container, false);
        mAddDeliveryAddressPresenter = new AddDeliveryAddressPresenter(this);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        mCloseAddDelivery = (ImageButton) view.findViewById(R.id.ibn_closeAddDelivery);
        mInputName = (TextInputLayout) view.findViewById(R.id.text_input_name);
        mInputAddress = (TextInputLayout) view.findViewById(R.id.text_input_address);
        mInputPhone = (TextInputLayout) view.findViewById(R.id.text_input_phone);
        mEditTextName = (EditText) view.findViewById(R.id.edt_name);
        mEditTextAddress = (EditText) view.findViewById(R.id.edt_address);
        mEditTextPhone = (EditText) view.findViewById(R.id.edt_phone);
        mChooseProvince = (Spinner) view.findViewById(R.id.spn_choose_province);
        mChooseDistrict = (Spinner) view.findViewById(R.id.spn_choose_district);
        mChooseWard = (Spinner) view.findViewById(R.id.spn_choose_ward);

        // Custom
        customSpinner();

        // Presenter
        mAddDeliveryAddressPresenter.loadData();
        mAddDeliveryAddressPresenter.loadProvice();

        // Listener
        mCloseAddDelivery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.frm_delivery_address, new LoadDeliveryAddressFragment());
                fragmentTransaction.commit();
            }
        });

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
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void setTextLoadJson(String json) {

    }

    private void customSpinner(){
        ArrayAdapter<CharSequence> provinceAdapter = ArrayAdapter.createFromResource(mChooseProvince.getContext(),
                R.array.province_data, R.layout.simple_spinner_item_custom);
        provinceAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mChooseProvince.setAdapter(provinceAdapter);

        ArrayAdapter<CharSequence> districtAdapter = ArrayAdapter.createFromResource(mChooseProvince.getContext(),
                R.array.district_data, R.layout.simple_spinner_item_custom);
        districtAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mChooseDistrict.setAdapter(districtAdapter);

        ArrayAdapter<CharSequence> wardAdapter = ArrayAdapter.createFromResource(mChooseProvince.getContext(),
                R.array.ward_data, R.layout.simple_spinner_item_custom);
        wardAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mChooseWard.setAdapter(wardAdapter);
    }
}
