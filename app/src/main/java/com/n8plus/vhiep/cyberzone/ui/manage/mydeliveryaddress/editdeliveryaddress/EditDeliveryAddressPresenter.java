package com.n8plus.vhiep.cyberzone.ui.manage.mydeliveryaddress.editdeliveryaddress;

import android.support.v4.app.Fragment;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.n8plus.vhiep.cyberzone.data.model.Address;
import com.n8plus.vhiep.cyberzone.data.model.Province;
import com.n8plus.vhiep.cyberzone.util.Constant;
import com.n8plus.vhiep.cyberzone.util.MySingleton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.TimeZone;

public class EditDeliveryAddressPresenter implements EditDeliveryAddressContract.Presenter {
    private EditDeliveryAddressContract.View mEditDeliveryAddressView;
    private List<Province> mProvinceList;
    private String URL_ADDRESS = Constant.URL_HOST + "deliveryAddresses";
    private Gson gson;
    private Address mAddress;
    private int selectionProvince = 0;
    private int selectionDistrict = 0;
    private int selectionWard = 0;

    public EditDeliveryAddressPresenter(EditDeliveryAddressContract.View mEditDeliveryAddressView) {
        this.mEditDeliveryAddressView = mEditDeliveryAddressView;
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.setDateFormat("M/d/yy hh:mm a");
        gson = gsonBuilder.create();
    }

    @Override
    public void loadProvice(List<Province> provinceList) {
        mProvinceList = provinceList;
        mEditDeliveryAddressView.setProvince(mProvinceList, selectionProvince);
    }

    @Override
    public void loadDistrict(int locationProvince) {
        mEditDeliveryAddressView.setDistrict(mProvinceList.get(locationProvince).getDistrictList(), selectionDistrict);
    }

    @Override
    public void loadWard(int locationProvince, int locationDistrict) {
        mEditDeliveryAddressView.setWard(mProvinceList.get(locationProvince).getDistrictList().get(locationDistrict).getWardList(), selectionWard);
    }

    @Override
    public void loadAddress(List<Province> provinceList, Address address) {
        mAddress = address;
        mEditDeliveryAddressView.setPresentation(address.getPresentation());
        mEditDeliveryAddressView.setPhoneNumber(address.getPhone());
        String[] addressString = address.getAddress().split(", ");
        for (int i = 0; i < addressString.length; i++) {
            Log.i("Address", i + ": " + addressString[i]);
        }
        for (int i = 0; i < provinceList.size(); i++) {
            if (addressString.length > 3) {
                int last = addressString.length - 1;
                int addressWithoutSelection = last - 2;
                StringBuilder addressCustomer = new StringBuilder();
                for (int count = 0; count < addressWithoutSelection; count++) {
                    if (count != addressWithoutSelection - 1) {
                        addressCustomer.append(addressString[count] + ", ");
                    } else {
                        addressCustomer.append(addressString[count]);
                    }
                }
                mEditDeliveryAddressView.setAddress(addressCustomer.toString());
                // Set selection
                if (addressString[last].equals(provinceList.get(i).getName())) {
                    selectionProvince = i;
                    loadProvice(provinceList);
                    for (int j = 0; j < mProvinceList.get(selectionProvince).getDistrictList().size(); j++) {
                        if (addressString[last - 1].equals(mProvinceList.get(selectionProvince).getDistrictList().get(j).getName())) {
                            selectionDistrict = j;
                            loadDistrict(selectionProvince);
                            for (int z = 0; z < mProvinceList.get(selectionProvince).getDistrictList().get(selectionDistrict).getWardList().size(); z++) {
                                if (addressString[last - 2].equals(mProvinceList.get(selectionProvince).getDistrictList().get(selectionDistrict).getWardList().get(z).getName())) {
                                    selectionWard = z;
                                    loadWard(selectionProvince, selectionDistrict);
                                }
                            }
                        }
                    }
                }
            } else {
                if (addressString[2].equals(mProvinceList.get(i).getName())) {
                    selectionProvince = i;
                    loadProvice(provinceList);
                    for (int j = 0; j < mProvinceList.get(selectionProvince).getDistrictList().size(); j++) {
                        if (addressString[1].equals(mProvinceList.get(selectionProvince).getDistrictList().get(j).getName())) {
                            selectionDistrict = j;
                            loadDistrict(selectionProvince);
                            for (int z = 0; z < mProvinceList.get(selectionProvince).getDistrictList().get(selectionDistrict).getWardList().size(); z++) {
                                if (addressString[0].equals(mProvinceList.get(selectionProvince).getDistrictList().get(selectionDistrict).getWardList().get(z).getName())) {
                                    selectionWard = z;
                                    loadWard(selectionProvince, selectionDistrict);
                                }
                            }
                        }
                    }
                }
            }
        }
        Log.i("Address", selectionProvince + " | " + selectionDistrict + " | " + selectionWard);

    }

    @Override
    public void updateDeliveryAddress(String presentation, String phone, String address) {
        mAddress.setPresentation(presentation);
        mAddress.setPhone(phone);
        mAddress.setAddress(address);
        final JSONObject nameObject = new JSONObject();
        final JSONObject phoneObject = new JSONObject();
        final JSONObject addressObject = new JSONObject();
        try {
            nameObject.put("propName", "presentation");
            nameObject.put("value", mAddress.getPresentation());

            phoneObject.put("propName", "phoneNumber");
            phoneObject.put("value", mAddress.getPhone());

            addressObject.put("propName", "address");
            addressObject.put("value", mAddress.getAddress());
        } catch (JSONException e) {
            Log.e("Error", e.toString());
        }

        JSONArray updateOps = new JSONArray();
        updateOps.put(nameObject);
        updateOps.put(phoneObject);
        updateOps.put(addressObject);

        JsonArrayRequest updateRequest = new JsonArrayRequest(Request.Method.PATCH, URL_ADDRESS + "/" + mAddress.getId(), updateOps,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.i("UpdateProfilePresenter", response.toString());
                        Toast.makeText(((Fragment) mEditDeliveryAddressView).getContext(), "Cập nhật thành công", Toast.LENGTH_SHORT).show();
                        mEditDeliveryAddressView.backLoadDeliveryAddress(mProvinceList);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("UpdateProfilePresenter", error.toString());
                        Toast.makeText(((Fragment) mEditDeliveryAddressView).getContext(), "Cập nhật thất bại", Toast.LENGTH_SHORT).show();
                    }
                });

        MySingleton.getInstance(((Fragment) mEditDeliveryAddressView).getContext().getApplicationContext()).addToRequestQueue(updateRequest);
    }

    @Override
    public void prepareBackLoadDeliveryAddress() {
        mEditDeliveryAddressView.backLoadDeliveryAddress(mProvinceList);
    }
}
