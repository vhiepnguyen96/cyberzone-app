package com.n8plus.vhiep.cyberzone.ui.manage.registersale.addregistersale;

import android.content.Context;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.n8plus.vhiep.cyberzone.data.model.Account;
import com.n8plus.vhiep.cyberzone.data.model.District;
import com.n8plus.vhiep.cyberzone.data.model.Province;
import com.n8plus.vhiep.cyberzone.data.model.RegisterSale;
import com.n8plus.vhiep.cyberzone.data.model.Store;
import com.n8plus.vhiep.cyberzone.util.Constant;
import com.n8plus.vhiep.cyberzone.util.VolleyUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

public class AddRegisterSalePresenter implements AddRegisterSaleContract.Presenter {
    private static final String TAG = "RegisterSalePresenter";
    private Context context;
    private AddRegisterSaleContract.View mAddRegisterSaleView;
    private List<Province> mProvinceList;
    private Gson gson;
    private RegisterSale mRegisterSale;

    public AddRegisterSalePresenter(@NonNull final Context context, @NonNull final AddRegisterSaleContract.View mAddRegisterSaleView) {
        this.context = context;
        this.mAddRegisterSaleView = mAddRegisterSaleView;
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.setDateFormat("M/d/yy hh:mm a");
        gson = gsonBuilder.create();
    }

    @Override
    public void loadData() {
        loadProvince();
        mAddRegisterSaleView.setNameCustomer(Constant.customer.getName());
    }

    @Override
    public void loadProvince() {
        mProvinceList = new ArrayList<>();
        new Handler().post(() -> {
            try {
                JSONObject provinces = new JSONObject(loadJSONFromAsset(((Fragment) mAddRegisterSaleView).getContext(), "data_country.json"));
                for (int i = 0; i < provinces.names().length(); i++) {
                    JSONObject province = provinces.getJSONObject((String) provinces.names().get(i));
                    mProvinceList.add(new Province(province.getString("name_with_type"), province.getInt("code"), new ArrayList<District>()));
                }
                Collections.sort(mProvinceList, (o1, o2) -> {
                    String o1_name = o1.getName().split(o1.getName().contains("Thành phố") ? "Thành phố" : "Tỉnh")[1];
                    String o2_name = o2.getName().split(o2.getName().contains("Thành phố") ? "Thành phố" : "Tỉnh")[1];
                    return o1_name.compareTo(o2_name);
                });
                Log.i(TAG, "provinceList: " + mProvinceList.size());
                mAddRegisterSaleView.setProvince(mProvinceList);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        });
    }

    @Override
    public void saveRegisterSale(String nameStore, String locationStore, String nameCustomer, String phone, String email, String storeAccount, String password) {
        mRegisterSale = new RegisterSale(Constant.customer, nameStore, locationStore, nameCustomer, email, storeAccount, password);
        checkStoreName(mRegisterSale.getStoreName());
    }


    @Override
    public void checkStoreName(String nameStore) {
        JSONObject storeName = new JSONObject();
        try {
            storeName.put("name", nameStore);
            VolleyUtil.POST(context, Constant.URL_STORE + "/findByName", storeName,
                    response -> {
                        Log.i(TAG, response.toString());
                        try {
                            Store store = gson.fromJson(String.valueOf(response.getJSONObject("store")), Store.class);
                            if (store != null) {
                                mAddRegisterSaleView.showAlert("Tên gian hàng đã tồn tại !");
                            } else {
                                checkStoreAccount(mRegisterSale.getUsername());
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    },
                    error -> {
                        Log.e(TAG, error.toString());
                        checkStoreAccount(mRegisterSale.getUsername());
                    });
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void checkStoreAccount(String storeAccount) {
        VolleyUtil.GET(context, Constant.URL_ACCOUNT + "/username/" + storeAccount,
                response -> {
                    Log.i(TAG, response.toString());
                    try {
                        Account account = gson.fromJson(String.valueOf(response.getJSONObject("account")), Account.class);
                        if (account != null) {
                            mAddRegisterSaleView.showAlert("Tên đăng nhập không khả dụng!");
                        } else {
                            sendRegisterSale(mRegisterSale);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                },
                error -> {
                    Log.e(TAG, error.toString());
                    sendRegisterSale(mRegisterSale);
                });
    }

    @Override
    public void sendRegisterSale(RegisterSale registerSale) {
        JSONObject registerSaleObj = new JSONObject();
        try {
            registerSaleObj.put("customerId", Constant.customer.getId());
            registerSaleObj.put("storeName", registerSale.getStoreName());
            registerSaleObj.put("address", registerSale.getAddress());
            registerSaleObj.put("phoneNumber", registerSale.getPhoneNumber());
            registerSaleObj.put("email", registerSale.getEmail());
            registerSaleObj.put("username", registerSale.getUsername());
            registerSaleObj.put("password", registerSale.getPassword());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        VolleyUtil.POST(context, Constant.URL_REGISTER_SALE, registerSaleObj,
                response -> {
                    Log.d(TAG, response.toString());
                    mAddRegisterSaleView.sendRegisterSaleResult(true);
                    mAddRegisterSaleView.backLoadRegisterSale();
                },
                error -> {
                    Log.e(TAG, error.toString());
                    mAddRegisterSaleView.sendRegisterSaleResult(false);
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
