package com.n8plus.vhiep.cyberzone.ui.manage.registersale.addregistersale;

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
import com.n8plus.vhiep.cyberzone.data.model.District;
import com.n8plus.vhiep.cyberzone.data.model.Province;
import com.n8plus.vhiep.cyberzone.data.model.Ward;
import com.n8plus.vhiep.cyberzone.ui.manage.registersale.RegisterSaleContract;
import com.n8plus.vhiep.cyberzone.util.Constant;
import com.n8plus.vhiep.cyberzone.util.MySingleton;
import com.n8plus.vhiep.cyberzone.util.VolleyUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class AddRegisterSalePresenter implements AddRegisterSaleContract.Presenter {
    private static final String TAG = "RegisterSalePresenter";
    private Context context;
    private AddRegisterSaleContract.View mAddRegisterSaleView;
    private List<Province> mProvinceList;
    private Gson gson;

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
    public void sendRegisterSale(String nameStore, String locationStore, String nameCustomer, String phone, String email, String storeAccount, String password) {
        JSONObject registerSaleObj = new JSONObject();
        try {
            registerSaleObj.put("customerId", Constant.customer.getId());
            registerSaleObj.put("storeName", nameStore);
            registerSaleObj.put("address", locationStore);
            registerSaleObj.put("phoneNumber", phone);
            registerSaleObj.put("email", email);
            registerSaleObj.put("username", storeAccount);
            registerSaleObj.put("password", password);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        VolleyUtil.POST(context, Constant.URL_REGISTER_SALE, registerSaleObj,
                response -> {
                    Log.e(TAG, response.toString());
                    mAddRegisterSaleView.sendRegisterSaleResult(true);
                    mAddRegisterSaleView.backLoadRegisterSale();
                },
                error -> {
                    Log.e(TAG, error.toString());
                    mAddRegisterSaleView.sendRegisterSaleResult(false);
                });
//        JsonObjectRequest registerSaleRequest = new JsonObjectRequest(Request.Method.POST, Constant.URL_REGISTER_SALE, registerSaleObj,
//                new Response.Listener<JSONObject>() {
//                    @Override
//                    public void onResponse(JSONObject response) {
//                        Log.e("RegisterSalePresenter", response.toString());
//                        mAddRegisterSaleView.sendRegisterSaleResult(true);
//                        mAddRegisterSaleView.backLoadRegisterSale();
//                    }
//                },
//                new Response.ErrorListener() {
//                    @Override
//                    public void onErrorResponse(VolleyError error) {
//                        Log.e("RegisterSalePresenter", error.toString());
//                        mAddRegisterSaleView.sendRegisterSaleResult(false);
//                    }
//                });
//
//        MySingleton.getInstance(((Fragment) mAddRegisterSaleView).getContext().getApplicationContext()).addToRequestQueue(registerSaleRequest);
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
