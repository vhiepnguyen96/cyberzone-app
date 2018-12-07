package com.n8plus.vhiep.cyberzone.util;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class VolleyUtil {

    public static void GET(@NonNull final Context context, @NonNull final String URL, @NonNull final Response.Listener<JSONObject> onSuccess, @NonNull final Response.ErrorListener onFailure) {
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, URL, null, onSuccess, onFailure);
        MySingleton.getInstance(context).addToRequestQueue(jsonObjectRequest);
    }

    public static <T> void POST(@NonNull final Context context, @NonNull final String URL, @NonNull final JSONObject object, @NonNull final Response.Listener<JSONObject> onSuccess, @NonNull final Response.ErrorListener onFailure) {
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, URL, object, onSuccess, onFailure);
        MySingleton.getInstance(context).addToRequestQueue(request);
    }

    public static <T> void PATCH(@NonNull final Context context, @NonNull final String URL, @NonNull final JSONArray jsonArray, @NonNull final Response.Listener<JSONArray> onSuccess, @NonNull final Response.ErrorListener onFailure) {
            JsonArrayRequest request = new JsonArrayRequest(Request.Method.PATCH, URL, jsonArray, onSuccess, onFailure);
            MySingleton.getInstance(context).addToRequestQueue(request);
    }

    public static void DELETE(@NonNull final Context context, @NonNull final String URL, @NonNull final Response.Listener<JSONObject> onSuccess, @NonNull final Response.ErrorListener onFailure) {
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.DELETE, URL, null, onSuccess, onFailure);
        MySingleton.getInstance(context).addToRequestQueue(request);
    }
}
