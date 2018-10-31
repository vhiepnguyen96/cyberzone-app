package com.n8plus.vhiep.cyberzone.ui.login.signup;

import android.support.v4.app.Fragment;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.n8plus.vhiep.cyberzone.data.model.Account;
import com.n8plus.vhiep.cyberzone.data.model.Customer;
import com.n8plus.vhiep.cyberzone.data.model.Role;
import com.n8plus.vhiep.cyberzone.util.Constant;
import com.n8plus.vhiep.cyberzone.util.MySingleton;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

public class SignupPresenter implements SignupContract.Presenter {
    private SignupContract.View mSignupView;
    private String URL_CUSTOMER = Constant.URL_HOST + "customers";
    private String URL_ROLE = Constant.URL_HOST + "roles";
    private String URL_ACCOUNT = Constant.URL_HOST + "accounts";
    private Gson gson;
    private List<Role> roleList;
    private Role roleCustomer;

    public SignupPresenter(SignupContract.View mSignupView) {
        this.mSignupView = mSignupView;
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.setDateFormat("M/d/yy hh:mm a");
        gson = gsonBuilder.create();
    }

    @Override
    public void loadData() {
        loadRoleAccount();
    }

    @Override
    public void loadRoleAccount() {
        roleList = new ArrayList<>();
        JsonObjectRequest roleRequest = new JsonObjectRequest(Request.Method.GET, URL_ROLE, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("SignupPresenter", response.toString());
                        try {
                            roleList = Arrays.asList(gson.fromJson(String.valueOf(response.getJSONArray("roles")), Role[].class));
                            Log.d("SignupPresenter", "roleList: "+roleList.size());
                            for (int i = 0; i < roleList.size(); i++) {
                                if (roleList.get(i).getRoleName().equals("Khách hàng")) {
                                    roleCustomer = roleList.get(i);
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("SignupPresenter", error.toString());
                    }
                });

        MySingleton.getInstance(((Fragment) mSignupView).getContext().getApplicationContext()).addToRequestQueue(roleRequest);
    }

    @Override
    public void createCustomerDefault(final Account account, final Customer customer) {
        account.setRole(roleCustomer);
        JSONObject accountObj = new JSONObject();
        try {
            accountObj.put("username", account.getUsername());
            accountObj.put("password", account.getPassword());
            accountObj.put("roleId", account.getRole().getId());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.d("SignupPresenter", "account: "+accountObj.toString());
        JsonObjectRequest accountRequest = new JsonObjectRequest(Request.Method.POST, URL_ACCOUNT, accountObj,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            Log.d("SignupPresenter", response.toString());
                            if (response.getJSONObject("createdAccount") != null) {
                                String accountId = response.getJSONObject("createdAccount").getString("_id");
                                Log.d("SignupPresenter", "accountId: "+accountId);
                                if (accountId != null) {
                                    account.setId(accountId);
                                    customer.setAccount(account);
                                    createCustomer(customer);
                                }
                            }
                        } catch (JSONException e) {
                            Log.e("SignupPresenter", e.toString());
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("SignupPresenter", error.toString());
                        mSignupView.createCustomerResult(false);
                    }
                });

        MySingleton.getInstance(((Fragment) mSignupView).getContext().getApplicationContext()).addToRequestQueue(accountRequest);
    }

    @Override
    public void createCustomer(final Customer customer) {
        JSONObject customerObj = new JSONObject();
        try {
            if (customer.getAccount().getId() != null)
                customerObj.put("accountId", customer.getAccount().getId());
            if (customer.getName() != null) customerObj.put("name", customer.getName());
            if (customer.getGender() != null) customerObj.put("gender", customer.getGender());
            if (customer.getBirthday() != null) {
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(customer.getBirthday());
                calendar.add(Calendar.DATE, 1);

                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                sdf.setTimeZone(TimeZone.getTimeZone("UTC"));

                customerObj.put("birthday", sdf.format(calendar.getTime()));
            }
            if (customer.getEmail() != null) customerObj.put("email", customer.getEmail());
            if (customer.getPhoneNumber() != null)
                customerObj.put("phoneNumber", customer.getPhoneNumber());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.e("SignupPresenter", "customerObj: " + customerObj.toString());
        JsonObjectRequest updateRequest = new JsonObjectRequest(Request.Method.POST, URL_CUSTOMER, customerObj,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            Log.e("SignupPresenter", response.toString());
                            if (response.getJSONObject("createdCustomer") != null) {
                                customer.setId(response.getJSONObject("createdCustomer").getString("_id"));
                                mSignupView.createCustomerResult(true);
                                if (customer.getAccount().getRole() == null){
                                    mSignupView.moveToConfirmSignup(customer);
                                } else {
                                    mSignupView.moveToSignin();
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("SignupPresenter", error.toString());
                        mSignupView.createCustomerResult(false);
                    }
                });

        MySingleton.getInstance(((Fragment) mSignupView).getContext().getApplicationContext()).addToRequestQueue(updateRequest);
    }
}
