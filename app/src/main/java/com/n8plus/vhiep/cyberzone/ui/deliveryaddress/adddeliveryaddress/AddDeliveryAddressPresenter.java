package com.n8plus.vhiep.cyberzone.ui.deliveryaddress.adddeliveryaddress;

import android.app.Fragment;
import android.content.Context;
import android.view.View;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.n8plus.vhiep.cyberzone.util.MySingleton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class AddDeliveryAddressPresenter implements AddDeliveryAddressContract.Presenter {
    private AddDeliveryAddressContract.View mAddDeliveryAddressView;

    public AddDeliveryAddressPresenter(AddDeliveryAddressContract.View mAddDeliveryAddressView) {
        this.mAddDeliveryAddressView = mAddDeliveryAddressView;
    }

    @Override
    public void loadData() {

    }

    @Override
    public void loadProvice() {
        String url = "http://mr80.net/files/2015/09/vietnam_provinces_cities.json";
        final ArrayList<JSONObject> provinceList = new ArrayList<>();
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONObject hanoi = response.getJSONObject("HANOI");
                            mAddDeliveryAddressView.setTextLoadJson((String) hanoi.get("name"));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                });
        MySingleton.getInstance(this.getContext(mAddDeliveryAddressView)).addToRequestQueue(jsonObjectRequest);
    }

    @Override
    public void loadDistrict(String provinceCode) {

    }

    @Override
    public void loadWard(String districtCode) {

    }

    private Context getContext(AddDeliveryAddressContract.View addDeliveryAddressView) {
        return ((Fragment) addDeliveryAddressView).getActivity().getApplicationContext();
    }
}
