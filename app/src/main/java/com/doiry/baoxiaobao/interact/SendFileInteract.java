package com.doiry.baoxiaobao.interact;

import static com.doiry.baoxiaobao.utils.configs.BASE_URL;
import static com.doiry.baoxiaobao.utils.configs.PORT;

import android.util.Log;

import androidx.annotation.NonNull;

import java.io.File;
import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * The type Send file interact.
 */
public class SendFileInteract {
    private static final MediaType MEDIA_TYPE_PNG = MediaType.parse("image/*");
    public static void sendFile(String uid, File file, String desc, String t_id, String amount, final SendFileInteract.sendFileCallback callback) {
        OkHttpClient client = new OkHttpClient.Builder().build();
        RequestBody fileBody = RequestBody.create(MEDIA_TYPE_PNG, file);

        RequestBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("file", "jpg.jpg", fileBody)
                .addFormDataPart("uid", uid)
                .addFormDataPart("remark", desc)
                .addFormDataPart("t_id", t_id)
                .addFormDataPart("amount", amount)
                .build();

        Request request = new Request.Builder()
                .url(BASE_URL + ":" + PORT + "/commit")
                .addHeader("contentType", "application/json;charset=utf-8")
                .post(requestBody)
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

    public interface sendFileCallback {
        void onSuccess(String result);
    }
}