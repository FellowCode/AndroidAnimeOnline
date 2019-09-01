package com.fellowcode.animewatcher.Api;


import android.content.Context;
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
import com.fellowcode.animewatcher.User.UserShiki;

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

    UserShiki userShiki;

    public Api(Context context) {
        queue = Volley.newRequestQueue(context);
        this.context = context;
        userShiki = new UserShiki(context);
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
                headers.put("UserShiki-Agent", "AniWatch");
                return headers;
            }
        };
        stringRequest.setTag("Api");
        queue.add(stringRequest);
    }

    public void ReqShikiProtect(final String url, final Response.Listener<String> respList) {
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
                headers.put("UserShiki-Agent", "AniWatch");
                headers.put("Authorization", "Bearer " + userShiki.accessToken);
                return headers;
            }
        };
        stringRequest.setTag("Api");
        queue.add(stringRequest);
    }

    public void authInShiki(String authCode, Auth listener){
        getShikiAuthTokens(false, authCode, listener);

    }

    public void refreshShikiTokens(){
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
                            userShiki.accessToken = authData.getString("access_token");
                            userShiki.refreshToken = authData.getString("refresh_token");
                            userShiki.endDateTime = authData.getInt("created_at") + authData.getInt("expires_in");
                            userShiki.save(context);
                            if (!isRefresh)
                                getUserData();
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
                headers.put("UserShiki-Agent", "AniWatch");
                return headers;
            }
            @Override
            protected Map<String, String> getParams(){
                Map<String, String>  params = new HashMap<>();
                if (isRefresh) {
                    params.put("grant_type", "refresh_token");
                    params.put("refresh_token", userShiki.refreshToken);
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

    public void getUserData(){
        Log.d("request", "getUserData");
        Link link = new Link().shiki().whoami();
        ReqShikiProtect(link.get(), new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("response", "user: "+response);
                try{
                    JSONObject data = new JSONObject(response);
                    userShiki.id = data.getInt("id");
                    userShiki.imageUrl = data.getJSONObject("image").getString("x48");
                    userShiki.nickname = data.getString("nickname");
                    userShiki.save(context);
                }catch (JSONException e){
                    e.printStackTrace();
                }
            }
        });
    }

    public void getUserRates(final Response.Listener<String> listener){
        Log.d("request", "getUserData");
        Link link = new Link().shiki().userRates(userShiki.id);
        ReqShikiProtect(link.get(), listener);
    }

    public boolean isShikiAuthenticated(){
        return userShiki.accessToken != null && userShiki.refreshToken != null && userShiki.endDateTime != 0;
    }

    public interface Auth{
        void onSuccess();
        void onError(String response);
    }
}
