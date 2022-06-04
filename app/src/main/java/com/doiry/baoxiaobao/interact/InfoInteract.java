package com.doiry.baoxiaobao.interact;

import static com.doiry.baoxiaobao.utils.configs.BASE_URL;
import static com.doiry.baoxiaobao.utils.configs.PORT;

import android.util.Log;

import androidx.annotation.NonNull;

import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class InfoInteract {
    public static void getProfile(String phs, final InfoInteract.getCallback callback) {
        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(30, TimeUnit.SECONDS)
                .callTimeout(120, TimeUnit.SECONDS)
                .pingInterval(5, TimeUnit.SECONDS)
                .readTimeout(60, TimeUnit.SECONDS)
                .writeTimeout(60, TimeUnit.SECONDS).build();
        Map m = new HashMap();
        m.put("phone", phs);
        JSONObject jsonObject = new JSONObject(m);
        String jsonStr = jsonObject.toString();
        RequestBody requestBodyJson = RequestBody.create(MediaType.parse("application/json;charset=utf-8"), jsonStr);
        Request request = new Request.Builder()
                .url(BASE_URL + ":" + PORT + "/userProfile")
                .addHeader("contentType", "application/json;charset=utf-8")
                .post(requestBodyJson)
                .build();
        final Call call = client.newCall(request);

        call.enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                Log.e("onFilure", e.getMessage());
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                String result = response.body().string();
                callback.onSuccess(result);
            }
        });
    }

    public static void getbindedInfo(String token, int type, final InfoInteract.getCallback callback) {
        OkHttpClient client = new OkHttpClient.Builder().build();
        Map m = new HashMap();
        m.put("token", token);
        JSONObject jsonObject = new JSONObject(m);
        String jsonStr = jsonObject.toString();
        String sendPost = "/bindedInfo";
        if(type == 1){
            sendPost = "/bindedInfoT";
        }
        RequestBody requestBodyJson = RequestBody.create(MediaType.parse("application/json;charset=utf-8"), jsonStr);
        Request request = new Request.Builder()
                .url(BASE_URL + ":" + PORT + sendPost)
                .addHeader("contentType", "application/json;charset=utf-8")
                .post(requestBodyJson)
                .build();
        final Call call = client.newCall(request);

        call.enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                Log.e("onFilure", e.getMessage());
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                String result = response.body().string();
                callback.onSuccess(result);
            }
        });
    }

    public static void getInfo(String token, final InfoInteract.getCallback callback) {
        OkHttpClient client = new OkHttpClient.Builder().build();
        Map m = new HashMap();
        m.put("token", token);
        JSONObject jsonObject = new JSONObject(m);
        String jsonStr = jsonObject.toString();
        RequestBody requestBodyJson = RequestBody.create(MediaType.parse("application/json;charset=utf-8"), jsonStr);
        Request request = new Request.Builder()
                .url(BASE_URL + ":" + PORT + "/bindInfo")
                .addHeader("contentType", "application/json;charset=utf-8")
                .post(requestBodyJson)
                .build();
        final Call call = client.newCall(request);

        call.enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                Log.e("onFilure", e.getMessage());
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                String result = response.body().string();
                callback.onSuccess(result);
            }
        });
    }

    public static void bindRelation(String token, String uid, final InfoInteract.getCallback callback) {
        OkHttpClient client = new OkHttpClient.Builder().build();
        Map m = new HashMap();
        m.put("token", token);
        m.put("t_id", uid);
        JSONObject jsonObject = new JSONObject(m);
        String jsonStr = jsonObject.toString();
        RequestBody requestBodyJson = RequestBody.create(MediaType.parse("application/json;charset=utf-8"), jsonStr);
        Request request = new Request.Builder()
                .url(BASE_URL + ":" + PORT + "/bind")
                .addHeader("contentType", "application/json;charset=utf-8")
                .post(requestBodyJson)
                .build();
        final Call call = client.newCall(request);

        call.enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                Log.e("onFilure", e.getMessage());
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                String result = response.body().string();
                callback.onSuccess(result);
            }
        });
    }

    public static void getBills(int type, String uid, final InfoInteract.getCallback callback) {
        OkHttpClient client = new OkHttpClient.Builder().build();
        Map m = new HashMap();
        m.put("uid", uid);
        m.put("type", type);
        JSONObject jsonObject = new JSONObject(m);
        String jsonStr = jsonObject.toString();
        RequestBody requestBodyJson = RequestBody.create(MediaType.parse("application/json;charset=utf-8"), jsonStr);
        Request request = new Request.Builder()
                .url(BASE_URL + ":" + PORT + "/bills")
                .addHeader("contentType", "application/json;charset=utf-8")
                .post(requestBodyJson)
                .build();
        final Call call = client.newCall(request);

        call.enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                Log.e("onFilure", e.getMessage());
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                String result = response.body().string();
                callback.onSuccess(result);
            }
        });
    }

    public static void checkBillStatus(int billStatus, String id, final InfoInteract.getCallback callback) {
        OkHttpClient client = new OkHttpClient.Builder().build();
        Map m = new HashMap();
        m.put("billStatus", billStatus);
        m.put("id", id);
        JSONObject jsonObject = new JSONObject(m);
        String jsonStr = jsonObject.toString();
        RequestBody requestBodyJson = RequestBody.create(MediaType.parse("application/json;charset=utf-8"), jsonStr);
        Request request = new Request.Builder()
                .url(BASE_URL + ":" + PORT + "/billStatus")
                .addHeader("contentType", "application/json;charset=utf-8")
                .post(requestBodyJson)
                .build();
        final Call call = client.newCall(request);

        call.enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                Log.e("onFilure", e.getMessage());
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                String result = response.body().string();
                callback.onSuccess(result);
            }
        });
    }

    public interface getCallback {
        void onSuccess(String result);
    }
}
