package com.n8plus.vhiep.cyberzone.ui.login.signup;

import android.content.Context;
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
import com.n8plus.vhiep.cyberzone.data.model.Account;
import com.n8plus.vhiep.cyberzone.data.model.Customer;
import com.n8plus.vhiep.cyberzone.data.model.Role;
import com.n8plus.vhiep.cyberzone.util.Constant;
import com.n8plus.vhiep.cyberzone.util.MySingleton;
import com.n8plus.vhiep.cyberzone.util.TypeLoad;
import com.n8plus.vhiep.cyberzone.util.TypeLogin;
import com.n8plus.vhiep.cyberzone.util.VolleyUtil;

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
    private static final String TAG = "SignupPresenter";
    private Context context;
    private SignupContract.View mSignupView;
    private Gson gson;
    private List<Role> roleList;
    private Role roleCustomer;

    public SignupPresenter(@NonNull final Context context, @NonNull final SignupContract.View mSignupView) {
        this.context = context;
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
        VolleyUtil.GET(context, Constant.URL_ROLE,
                response -> {
                    try {
                        roleList = Arrays.asList(gson.fromJson(String.valueOf(response.getJSONArray("roles")), Role[].class));
                        Log.d("SignupPresenter", "roleList: " + roleList.size());
                        for (int i = 0; i < roleList.size(); i++) {
                            if (roleList.get(i).getRoleName().equals("Khách hàng")) {
                                roleCustomer = roleList.get(i);
                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                },
                error -> Log.e(TAG, error.toString()));

    }

    @Override
    public void signUp(final Customer customer) {
        VolleyUtil.GET(context, Constant.URL_CUSTOMER + "/account/" + customer.getAccount().getId(),
                response -> {
                    try {
                        Log.d(TAG, "checkAccountResponse: " + response.toString());
                        Customer customerResult = gson.fromJson(String.valueOf(response.getJSONObject("customer")), Customer.class);
                        boolean isExists = customerResult != null ? true : false;

                        if (!isExists) {
                            createCustomer(customer);
                        } else {
                            mSignupView.showAlertAccountAlready();
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                },
                error -> {
                    Log.e(TAG, error.toString());
                    createCustomer(customer);
                });
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
        Log.d(TAG, "account: " + accountObj.toString());
        VolleyUtil.POST(context, Constant.URL_ACCOUNT, accountObj,
                response -> {
                    try {
                        Log.d(TAG, response.toString());
                        if (response.getJSONObject("createdAccount") != null) {
                            String accountId = response.getJSONObject("createdAccount").getString("_id");
                            Log.d(TAG, "accountId: " + accountId);
                            if (accountId != null) {
                                account.setId(accountId);
                                customer.setAccount(account);
                                createCustomer(customer);
                            }
                        }
                    } catch (JSONException e) {
                        Log.e(TAG, e.toString());
                    }
                },
                error -> {
                    Log.e(TAG, error.toString());
                    mSignupView.createCustomerResult(false);
                });
//        JsonObjectRequest accountRequest = new JsonObjectRequest(Request.Method.POST, URL_ACCOUNT, accountObj,
//                new Response.Listener<JSONObject>() {
//                    @Override
//                    public void onResponse(JSONObject response) {
//                        try {
//                            Log.d("SignupPresenter", response.toString());
//                            if (response.getJSONObject("createdAccount") != null) {
//                                String accountId = response.getJSONObject("createdAccount").getString("_id");
//                                Log.d("SignupPresenter", "accountId: " + accountId);
//                                if (accountId != null) {
//                                    account.setId(accountId);
//                                    customer.setAccount(account);
//                                    createCustomer(customer);
//                                }
//                            }
//                        } catch (JSONException e) {
//                            Log.e("SignupPresenter", e.toString());
//                        }
//                    }
//                },
//                new Response.ErrorListener() {
//                    @Override
//                    public void onErrorResponse(VolleyError error) {
//                        Log.e("SignupPresenter", error.toString());
//                        mSignupView.createCustomerResult(false);
//                    }
//                });
//
//        MySingleton.getInstance(((Fragment) mSignupView).getContext().getApplicationContext()).addToRequestQueue(accountRequest);
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

        Log.d(TAG, "customerObj: " + customerObj.toString());
        VolleyUtil.POST(context, Constant.URL_CUSTOMER, customerObj,
                response -> {
                    try {
                        Log.e(TAG, response.toString());
                        if (response.getJSONObject("createdCustomer") != null) {
                            customer.setId(response.getJSONObject("createdCustomer").getString("_id"));
                            mSignupView.createCustomerResult(true);
                            if (customer.getAccount().getRole() == null) {
                                mSignupView.moveToConfirmSignup(customer);
                            } else {
                                mSignupView.moveToSignin();
                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                },
                error -> {
                    Log.e(TAG, error.toString());
                    mSignupView.createCustomerResult(false);
                });
//        JsonObjectRequest updateRequest = new JsonObjectRequest(Request.Method.POST, URL_CUSTOMER, customerObj,
//                new Response.Listener<JSONObject>() {
//                    @Override
//                    public void onResponse(JSONObject response) {
//                        try {
//                            Log.e("SignupPresenter", response.toString());
//                            if (response.getJSONObject("createdCustomer") != null) {
//                                customer.setId(response.getJSONObject("createdCustomer").getString("_id"));
//                                mSignupView.createCustomerResult(true);
//                                if (customer.getAccount().getRole() == null) {
//                                    mSignupView.moveToConfirmSignup(customer);
//                                } else {
//                                    mSignupView.moveToSignin();
//                                }
//                            }
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                        }
//                    }
//                },
//                new Response.ErrorListener() {
//                    @Override
//                    public void onErrorResponse(VolleyError error) {
//                        Log.e("SignupPresenter", error.toString());
//                        mSignupView.createCustomerResult(false);
//                    }
//                });
//
//        MySingleton.getInstance(((Fragment) mSignupView).getContext().getApplicationContext()).addToRequestQueue(updateRequest);
    }
}
