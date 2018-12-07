package com.n8plus.vhiep.cyberzone.ui.manage.mydeliveryaddress.loaddeliveryaddress;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
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
import com.n8plus.vhiep.cyberzone.util.VolleyUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class LoadDeliveryAddressPresenter implements LoadDeliveryAddressContract.Presenter {

    private static final String TAG = "AddressPresenter";
    private Context context;
    private LoadDeliveryAddressContract.View mLoadDeliveryAddressView;
    private List<Address> addressList;
    private List<Province> mProvinceList;
    private Gson gson;

    public LoadDeliveryAddressPresenter(@NonNull final Context context, @NonNull final LoadDeliveryAddressContract.View mLoadDeliveryAddressView) {
        this.context = context;
        this.mLoadDeliveryAddressView = mLoadDeliveryAddressView;
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.setDateFormat("M/d/yy hh:mm a");
        gson = gsonBuilder.create();
    }

    @Override
    public void loadAllDeliveryAddress(String customerId) {
        fetchDeliveryAddresss(customerId);
    }

    @Override
    public void loadProvince() {
        mProvinceList = new ArrayList<>();
        new Handler().post(() -> {
            try {
                JSONObject provinces = new JSONObject(loadJSONFromAsset(((Fragment) mLoadDeliveryAddressView).getContext(), "data_country.json"));
                for (int i = 0; i < provinces.names().length(); i++) {
                    JSONObject province = provinces.getJSONObject((String) provinces.names().get(i));
                    mProvinceList.add(new Province(province.getString("name_with_type"), province.getInt("code"), new ArrayList<District>()));
                    JSONObject districts = province.getJSONObject("quan-huyen");
                    for (int j = 0; j < districts.names().length(); j++) {
                        JSONObject district = districts.getJSONObject((String) districts.names().get(j));
                        mProvinceList.get(i).getDistrictList().add(new District(district.getString("name_with_type"), district.getInt("code"), district.getInt("parent_code"), new ArrayList<Ward>()));
                        JSONObject wards = district.getJSONObject("xa-phuong");
                        for (int z = 0; z < wards.names().length(); z++) {
                            JSONObject ward = wards.getJSONObject((String) wards.names().get(z));
                            mProvinceList.get(i).getDistrictList().get(j).getWardList().add(new Ward(ward.getString("name_with_type"), ward.getInt("code"), ward.getInt("parent_code")));
                        }
                    }
                }

                Collections.sort(mProvinceList, (o1, o2) -> {
                    String o1_name = o1.getName().split(o1.getName().contains("Thành phố") ? "Thành phố" : "Tỉnh")[1];
                    String o2_name = o2.getName().split(o2.getName().contains("Thành phố") ? "Thành phố" : "Tỉnh")[1];
                    return o1_name.compareTo(o2_name);
                });
                Log.i(TAG, "provinceList: " + mProvinceList.size());
            } catch (JSONException e) {
                e.printStackTrace();
            }
        });
    }

    @Override
    public void loadProvinceList(List<Province> provinceList) {
        mProvinceList = provinceList;
    }

    @Override
    public void prepareDataAddAddress() {
        mLoadDeliveryAddressView.moveToAddDeliveryAddress(mProvinceList);
    }

    @Override
    public void prepareDataEditAddress(int position) {
        mLoadDeliveryAddressView.moveToEditDeliveryAddress(mProvinceList, addressList.get(position));
    }

    @Override
    public void deleteDeliveryAddress(int position) {
        VolleyUtil.DELETE(context, Constant.URL_ADDRESS + "/" + addressList.get(position).getId(),
                response -> {
                    Log.e(TAG, response.toString());
                    mLoadDeliveryAddressView.deleteAddressSuccess();
                },
                error -> {
                    Log.e("AddressPresenter", error.toString());
                    mLoadDeliveryAddressView.deleteAddressFailure();
                });
    }

    @Override
    public void prepareDataChooseAddress(int position) {
        mLoadDeliveryAddressView.moveToCheckOrder(addressList.get(position));
    }

    private void fetchDeliveryAddresss(String customerId) {
        mLoadDeliveryAddressView.setLayoutLoading(true);
        VolleyUtil.GET(context, Constant.URL_ADDRESS + "/customer/" + customerId,
                response -> {
                    try {
                        addressList = Arrays.asList(gson.fromJson(String.valueOf(response.getJSONArray("deliveryAddresses")), Address[].class));
                        Log.i(TAG, "GET: " + addressList.size() + " address");

                        mLoadDeliveryAddressView.setLayoutLoading(false);
                        if (!addressList.isEmpty()) {
                            mLoadDeliveryAddressView.setLayoutNone(false);
                            mLoadDeliveryAddressView.setAdapterAddress(addressList);
                        } else {
                            mLoadDeliveryAddressView.setLayoutNone(true);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                },
                error -> {
                    Log.e(TAG, error.toString());
                    mLoadDeliveryAddressView.setLayoutLoading(false);
                    mLoadDeliveryAddressView.setLayoutNone(true);
                });
    }

    public String loadJSONFromAsset(Context context, String name_file) {
        String json = null;
        try {
            InputStream is = context.getAssets().open(name_file);
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return json;
    }
}
