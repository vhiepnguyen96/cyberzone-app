package com.n8plus.vhiep.cyberzone.ui.manage.mydeliveryaddress.deliveryaddress;

import android.app.Fragment;
import android.content.Context;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.n8plus.vhiep.cyberzone.ui.manage.mydeliveryaddress.adddeliveryaddress.AddDeliveryAddressContract;
import com.n8plus.vhiep.cyberzone.util.MySingleton;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class DeliveryAddressPresenter implements DeliveryAddressContract.Presenter {
    private AddDeliveryAddressContract.View mAddDeliveryAddressView;

    public DeliveryAddressPresenter(AddDeliveryAddressContract.View mAddDeliveryAddressView) {
        this.mAddDeliveryAddressView = mAddDeliveryAddressView;
    }

    private Context getContext(AddDeliveryAddressContract.View addDeliveryAddressView) {
        return ((Fragment) addDeliveryAddressView).getActivity().getApplicationContext();
    }
}
