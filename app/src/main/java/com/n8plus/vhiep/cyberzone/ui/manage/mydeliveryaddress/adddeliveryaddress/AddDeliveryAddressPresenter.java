package com.n8plus.vhiep.cyberzone.ui.manage.mydeliveryaddress.adddeliveryaddress;

import android.content.Context;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.n8plus.vhiep.cyberzone.data.model.Address;
import com.n8plus.vhiep.cyberzone.data.model.District;
import com.n8plus.vhiep.cyberzone.data.model.Product;
import com.n8plus.vhiep.cyberzone.data.model.Province;
import com.n8plus.vhiep.cyberzone.data.model.Ward;
import com.n8plus.vhiep.cyberzone.util.Constant;
import com.n8plus.vhiep.cyberzone.util.MySingleton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class AddDeliveryAddressPresenter implements AddDeliveryAddressContract.Presenter {
    private AddDeliveryAddressContract.View mAddDeliveryAddressView;
    private List<Province> mProvinceList;
    private String URL_ADDRESS = Constant.URL_HOST + "deliveryAddresses";
    private Gson gson;

    public AddDeliveryAddressPresenter(AddDeliveryAddressContract.View mAddDeliveryAddressView) {
        this.mAddDeliveryAddressView = mAddDeliveryAddressView;
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.setDateFormat("M/d/yy hh:mm a");
        gson = gsonBuilder.create();
    }

    @Override
    public void loadProvice(List<Province> provinceList) {
        mProvinceList = provinceList;
        mAddDeliveryAddressView.setProvince(provinceList);
    }

    @Override
    public void loadDistrict(int locationProvince) {
        mAddDeliveryAddressView.setDistrict(mProvinceList.get(locationProvince).getDistrictList());
    }

    @Override
    public void loadWard(int locationProvince, int locationDistrict) {
        mAddDeliveryAddressView.setWard(mProvinceList.get(locationProvince).getDistrictList().get(locationDistrict).getWardList());
    }

    @Override
    public void addDeliveryAddress(String customerId, Address address) {
        JSONObject addressObj = new JSONObject();
        try {
            addressObj.put("customerId", customerId);
            addressObj.put("presentation", address.getPresentation());
            addressObj.put("phoneNumber", address.getPhone());
            addressObj.put("address", address.getAddress());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest updateRequest = new JsonObjectRequest(Request.Method.POST, URL_ADDRESS, addressObj,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.e("AddressPresenter", response.toString());
                        Toast.makeText(((Fragment) mAddDeliveryAddressView).getContext(), "Thêm thành công!", Toast.LENGTH_SHORT).show();
                        mAddDeliveryAddressView.backLoadDeliveryAddress(mProvinceList);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("AddressPresenter", error.toString());
                        Toast.makeText(((Fragment) mAddDeliveryAddressView).getContext(), "Thêm thất bại!", Toast.LENGTH_SHORT).show();
                    }
                });

        MySingleton.getInstance(((Fragment) mAddDeliveryAddressView).getContext().getApplicationContext()).addToRequestQueue(updateRequest);
    }

    @Override
    public void prepareBackLoadDeliveryAddress() {
        mAddDeliveryAddressView.backLoadDeliveryAddress(mProvinceList);
    }

    private Context getContext(AddDeliveryAddressContract.View addDeliveryAddressView) {
        return ((Fragment) addDeliveryAddressView).getActivity().getApplicationContext();
    }

}
