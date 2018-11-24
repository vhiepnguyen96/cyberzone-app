package com.n8plus.vhiep.cyberzone.ui.manage.myprofile.updateprofile;

import android.support.v4.app.Fragment;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Header;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.JsonArray;
import com.n8plus.vhiep.cyberzone.data.model.Customer;
import com.n8plus.vhiep.cyberzone.data.model.ProductImage;
import com.n8plus.vhiep.cyberzone.util.Constant;
import com.n8plus.vhiep.cyberzone.util.MySingleton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.TimeZone;

public class UpdateProfilePresenter implements UpdateProfileContract.Presenter {
    private Customer mCustomer;
    private UpdateProfileContract.View mUpdateProfileView;
    private String URL_CUSTOMER = Constant.URL_HOST + "customers";

    public UpdateProfilePresenter(UpdateProfileContract.View updateProfileView) {
        mUpdateProfileView = updateProfileView;
    }

    @Override
    public void setCustomerProfile(Customer customer) {
        mCustomer = customer;
        mUpdateProfileView.setNameCustomer(mCustomer.getName());
        mUpdateProfileView.setGenderCustomer(mCustomer.getGender());
        mUpdateProfileView.setBirthdayCustomer(mCustomer.getBirthday());
        mUpdateProfileView.setPhoneNumberCustomer(mCustomer.getPhoneNumber());
        mUpdateProfileView.setEmailCustomer(mCustomer.getEmail());
    }

    @Override
    public void updateCustomer(String name, String gender, Date birthday, String phone, String email) {
        mCustomer.setName(name != null ? name : mCustomer.getName());
        mCustomer.setGender(gender != null ? gender : mCustomer.getGender());
        mCustomer.setBirthday(birthday != null ? birthday : mCustomer.getBirthday());
        mCustomer.setPhoneNumber(phone != null ? phone : mCustomer.getPhoneNumber());
        mCustomer.setEmail(email != null ? email : mCustomer.getEmail());

        updateCustomer();
    }

    public void updateCustomer() {
        final JSONObject nameObject = new JSONObject();
        final JSONObject genderObject = new JSONObject();
        final JSONObject birthdayObject = new JSONObject();
        final JSONObject phoneObject = new JSONObject();
        final JSONObject emailObject = new JSONObject();
        try {
            if (mCustomer.getName() != null) {
                nameObject.put("propName", "name");
                nameObject.put("value", mCustomer.getName());
            }

            if (mCustomer.getGender() != null) {
                genderObject.put("propName", "gender");
                genderObject.put("value", mCustomer.getGender());
            }

            if (mCustomer.getBirthday() != null) {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
                birthdayObject.put("propName", "birthday");
                birthdayObject.put("value", sdf.format(mCustomer.getBirthday()));
            }

            if (mCustomer.getPhoneNumber() != null) {
                phoneObject.put("propName", "phoneNumber");
                phoneObject.put("value", mCustomer.getPhoneNumber());
            }

            if (mCustomer.getEmail() != null) {
                emailObject.put("propName", "email");
                emailObject.put("value", mCustomer.getEmail());
            }

        } catch (JSONException e) {
            Log.e("Error", e.toString());
        }

        JSONArray updateOps = new JSONArray();
        updateOps.put(nameObject);
        updateOps.put(genderObject);
        updateOps.put(birthdayObject);
        updateOps.put(phoneObject);
        updateOps.put(emailObject);

        JsonArrayRequest updateRequest = new JsonArrayRequest(Request.Method.PATCH, URL_CUSTOMER + "/" + Constant.customer.getId(), updateOps,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        mUpdateProfileView.updateCustomerResult(true);
                        mUpdateProfileView.actionBackProfile();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("UpdateProfilePresenter", error.toString());
                        mUpdateProfileView.updateCustomerResult(false);
                    }
                });

        MySingleton.getInstance(((Fragment) mUpdateProfileView).getContext().getApplicationContext()).addToRequestQueue(updateRequest);
    }
}
