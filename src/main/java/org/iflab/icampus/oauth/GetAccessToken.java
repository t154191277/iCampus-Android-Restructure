package org.iflab.icampus.oauth;

import android.content.Context;

import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.apache.http.Header;
import org.iflab.icampus.http.AsyncHttpIc;
import org.iflab.icampus.http.UrlStatic;
import org.iflab.icampus.utils.StaticVariable;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by hcjcch on 2015/2/14.
 */
public class GetAccessToken {
    public static String getAccessToken(final Context context, String code) {
        AsyncHttpIc.get(UrlStatic.GET_ACCESS_TOKEN, createParams(code), new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                if (statusCode == 200) {
                    try {
                        JSONObject jsonObject = new JSONObject(new String(responseBody));
                        String state = jsonObject.getString("state");
                        if (state.equals("succeed")) {
                            String access_token = jsonObject.getString("access_token");
                            String expires = jsonObject.getString("expires");
                            AccessTokenHandle.saveAccessToken(context, access_token);
                            System.out.println("access_token    " + access_token);
                            AsyncHttpIc.get(UrlStatic.GET_PERSONAL_INFORMATION, createAccessTokenParams(access_token), new AsyncHttpResponseHandler() {
                                @Override
                                public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                                    System.out.println(new String(responseBody));
                                }

                                @Override
                                public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

                                }
                            });
                        } else {
                            //TODO 处理code过期
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {

                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

            }
        });
        return null;
    }

    private static RequestParams createParams(String code) {
        RequestParams params = new RequestParams();
        params.put(StaticVariable.OauthStaticVariable.KEY_CLIENT_ID, StaticVariable.OauthStaticVariable.CLIENT_ID);
        params.put(StaticVariable.OauthStaticVariable.KEY_CLIENT_SECRET, StaticVariable.OauthStaticVariable.CLIENT_SECRET);
        params.put("code", code);
        return params;
    }

    private static RequestParams createAccessTokenParams(String accessToken) {
        RequestParams params = new RequestParams();
        params.put("access_token", accessToken);
        return params;
    }
}
