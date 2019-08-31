package com.fellowcode.animewatcher.Api;


import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.fellowcode.animewatcher.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class Api implements Serializable {

    RequestQueue queue;
    Context context;

    public static String CLIENT_ID = "d5432576ed6467adcc15df423c84cb3ce14498e34ec9a6545cbc6f46c5ebbf54";
    static String CLIENT_SECRET = "bb95386ff0d2afaf30ba6fd5bad16fee9a8ee6c8840e2145f2f715e6678b0b5b";
    public static String REDIRECT_URI = "urn:ietf:wg:oauth:2.0:oob";


    //oauth
    String accessToken;
    String refreshToken;
    int endDateTime;
    int userId;

    public Api(Context context) {
        queue = Volley.newRequestQueue(context);
        this.context = context;
        loadAuthTokens();
    }

    public static String getAuthURI(){
        return Link.shikiUrl + "/oauth/authorize?client_id=" + Api.CLIENT_ID + "&redirect_uri=" + Api.REDIRECT_URI + "&response_type=code&scope=";
    }

    public void Request(final String url, final Response.Listener<String> respList) {
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                respList,
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("req-error", error.toString());
                        Toast.makeText(context, R.string.load_error, Toast.LENGTH_LONG).show();
                        Request(url, respList);
                    }
                }) {
            @Override
            public Map getHeaders() throws AuthFailureError {
                HashMap headers = new HashMap();
                headers.put("User-Agent", "AniWatch");
                return headers;
            }
        };
        stringRequest.setTag("Api");
        queue.add(stringRequest);
    }

    public void ShikiProtectReq(final String url, final Response.Listener<String> respList) {
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                respList,
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("req-error", error.toString());
                        Toast.makeText(context, R.string.load_error, Toast.LENGTH_LONG).show();
                    }
                }) {
            @Override
            public Map getHeaders() throws AuthFailureError {
                HashMap headers = new HashMap();
                headers.put("User-Agent", "AniWatch");
                headers.put("Authorization", "Bearer " + accessToken);
                return headers;
            }
        };
        stringRequest.setTag("Api");
        queue.add(stringRequest);
    }

    void saveShikiAuthTokens(){
        SharedPreferences authShiki = context.getSharedPreferences("authShiki", Context.MODE_PRIVATE);
        authShiki.edit()
                .putString("accessToken",  accessToken)
                .putString("refreshToken", refreshToken)
                .putInt("endDateTime", endDateTime)
                .apply();
    }

    Api loadAuthTokens() {
        SharedPreferences authShiki = context.getSharedPreferences("authShiki", Context.MODE_PRIVATE);
        accessToken = authShiki.getString("accessToken", null);
        refreshToken = authShiki.getString("refreshToken", null);
        endDateTime = authShiki.getInt("endDateTime", 0);
        return this;
    }

    public void authInShiki(String authCode, Auth listener){
        getShikiAuthTokens(false, authCode, listener);

    }

    public void updateShikiTokens(){
        getShikiAuthTokens(true, null, null);
    }

    private void getShikiAuthTokens(final boolean isRefresh, final String authCode, final Auth listener) {
        Log.d("oauth", "getTokens isRefresh: "+isRefresh);
        String url = Link.shikiUrl + "/oauth/token";
        Log.d("oauth", url);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("oauth", "response: "+response);
                        try{
                            JSONObject authData = new JSONObject(response);
                            accessToken = authData.getString("access_token");
                            refreshToken = authData.getString("refresh_token");
                            endDateTime = authData.getInt("created_at") + authData.getInt("expires_in");
                            saveShikiAuthTokens();
                            if (!isRefresh)
                                getUserId();
                            if (listener != null)
                                listener.onSuccess();
                        } catch (JSONException e){
                            e.printStackTrace();
                            if (listener != null)
                                listener.onError(response);
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("oauth", error.toString());
                        Toast.makeText(context, R.string.load_error, Toast.LENGTH_LONG).show();
                    }
                }) {
            @Override
            public Map getHeaders() throws AuthFailureError {
                HashMap headers = new HashMap();
                headers.put("User-Agent", "AniWatch");
                return headers;
            }
            @Override
            protected Map<String, String> getParams(){
                Map<String, String>  params = new HashMap<>();
                if (isRefresh) {
                    params.put("grant_type", "refresh_token");
                    params.put("refresh_token", refreshToken);
                }else {
                    params.put("grant_type", "authorization_code");
                    params.put("code", authCode);
                    Log.d("oauth", "authCode: "+authCode);
                }
                params.put("client_id", CLIENT_ID);
                params.put("client_secret", CLIENT_SECRET);
                params.put("redirect_uri", REDIRECT_URI);

                return params;
            }
            @Override
            protected Response<String> parseNetworkResponse(NetworkResponse response) {
                Log.d("oauth", "headers: " + response.headers.get("X-Request-Id"));
                return super.parseNetworkResponse(response);
            }
        };
        stringRequest.setTag("Api");
        queue.add(stringRequest);
    }

    public boolean isShikiAuthenticated(){
        return accessToken != null && refreshToken != null && endDateTime != 0;
    }

    public void getUserId(){

    }

    public interface Auth{
        void onSuccess();
        void onError(String response);
    }
}
