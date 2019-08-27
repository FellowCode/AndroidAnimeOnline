package com.fellowcode.animewatcher.Api;


import android.content.Context;
import android.support.annotation.Nullable;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class Api implements Serializable {

    RequestQueue queue;

    public Api(Context context) {
        queue = Volley.newRequestQueue(context);
    }

    public void Request(String url, Response.Listener<String> respList) {
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                respList,
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("req-error", error.toString());
                    }
                }){
            @Override
            public Map getHeaders() throws AuthFailureError {
                HashMap headers = new HashMap();
                headers.put("User-Agent", "Anime online");
                return headers;
            }
        };
        stringRequest.setTag("Api");
        queue.add(stringRequest);
    }
}
