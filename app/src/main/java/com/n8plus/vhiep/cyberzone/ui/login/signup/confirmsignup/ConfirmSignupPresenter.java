package com.n8plus.vhiep.cyberzone.ui.login.signup.confirmsignup;

import android.support.v4.app.Fragment;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.n8plus.vhiep.cyberzone.data.model.Customer;
import com.n8plus.vhiep.cyberzone.util.Constant;
import com.n8plus.vhiep.cyberzone.util.MySingleton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;

public class ConfirmSignupPresenter implements ConfirmSignupContract.Presenter {
    private ConfirmSignupContract.View mConfirmSignupView;
    private Customer mCustomer;
    private String URL_CUSTOMER = Constant.URL_HOST + "customers";
    private Gson gson;

    public ConfirmSignupPresenter(ConfirmSignupContract.View mConfirmSignupView) {
        this.mConfirmSignupView = mConfirmSignupView;
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.setDateFormat("M/d/yy hh:mm a");
        gson = gsonBuilder.create();
    }

    @Override
    public void setDataCustomer(Customer customer) {
        mCustomer = customer;
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        if (mCustomer.getName() != null) mConfirmSignupView.setNameCustomer(mCustomer.getName());
        if (mCustomer.getGender() != null) mConfirmSignupView.setGenderCustomer(mCustomer.getGender());
        if (mCustomer.getBirthday() != null)
            mConfirmSignupView.setBirthdayCustomer(sdf.format(mCustomer.getBirthday()));
        if (mCustomer.getEmail() != null) mConfirmSignupView.setEmailCustomer(mCustomer.getEmail());
        if (mCustomer.getPhoneNumber() != null) mConfirmSignupView.setPhoneCustomer(mCustomer.getPhoneNumber());
    }

    @Override
    public void updateCustomer(Customer customer) {
        JSONArray updateOps = new JSONArray();
        try {
            if (customer.getName() != null) {
                JSONObject nameObject = new JSONObject();
                nameObject.put("propName", "name");
                nameObject.put("value", customer.getName());
                updateOps.put(nameObject);
            }
            if (customer.getGender() != null) {
                JSONObject genderObject = new JSONObject();
                genderObject.put("propName", "gender");
                genderObject.put("value", customer.getGender());
                updateOps.put(genderObject);
            }
            if (customer.getBirthday() != null) {
                JSONObject birthdayObject = new JSONObject();
                birthdayObject.put("propName", "birthday");
                birthdayObject.put("value", customer.getBirthday());
                updateOps.put(birthdayObject);
            }
            if (customer.getEmail() != null) {
                JSONObject emailObject = new JSONObject();
                emailObject.put("propName", "email");
                emailObject.put("value", customer.getEmail());
                updateOps.put(emailObject);
            }
            if (customer.getPhoneNumber() != null) {
                JSONObject phoneObject = new JSONObject();
                phoneObject.put("propName", "phoneNumber");
                phoneObject.put("value", customer.getPhoneNumber());
                updateOps.put(phoneObject);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonArrayRequest updateRequest = new JsonArrayRequest(Request.Method.PATCH, URL_CUSTOMER + "/" + mCustomer.getId(), updateOps,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.i("ConfirmSignupPresenter", response.toString());
                        mConfirmSignupView.updateCustomerResult(true);
                        mConfirmSignupView.backToSignin();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("ConfirmSignupPresenter", error.toString());
                        mConfirmSignupView.updateCustomerResult(false);
                    }
                });

        MySingleton.getInstance(((Fragment) mConfirmSignupView).getContext().getApplicationContext()).addToRequestQueue(updateRequest);
    }
}
