package com.n8plus.vhiep.cyberzone.ui.deliveryaddress.editdeliveryaddress;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
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

import com.n8plus.vhiep.cyberzone.R;
import com.n8plus.vhiep.cyberzone.ui.deliveryaddress.loaddeliveryaddress.LoadDeliveryAddressFragment;

public class EditDeliveryAddressFragment extends Fragment implements EditDeliveryAddressContract.View {
    private ImageButton mCloseEditDelivery;
    private TextInputLayout mInputName, mInputAddress, mInputPhone;
    private EditText mEditTextName, mEditTextAddress, mEditTextPhone;
    private Spinner mChooseProvince, mChooseDistrict, mChooseWard;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.edit_delivery_frag, container, false);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        mCloseEditDelivery = (ImageButton) view.findViewById(R.id.ibn_closeEditDelivery);
        mInputName = (TextInputLayout) view.findViewById(R.id.text_input_edit_name);
        mInputAddress = (TextInputLayout) view.findViewById(R.id.text_input_edit_address);
        mInputPhone = (TextInputLayout) view.findViewById(R.id.text_input_edit_phone);
        mEditTextName = (EditText) view.findViewById(R.id.edt_edit_name);
        mEditTextAddress = (EditText) view.findViewById(R.id.edt_edit_address);
        mEditTextPhone = (EditText) view.findViewById(R.id.edt_edit_phone);
        mChooseProvince = (Spinner) view.findViewById(R.id.spn_choose_edit_province);
        mChooseDistrict = (Spinner) view.findViewById(R.id.spn_choose_edit_district);
        mChooseWard = (Spinner) view.findViewById(R.id.spn_choose_edit_ward);

        customSpinner();

        mCloseEditDelivery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fragmentManager = getFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
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
