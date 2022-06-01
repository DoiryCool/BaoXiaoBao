package com.doiry.baoxiaobao.ui.home;

import static com.doiry.baoxiaobao.utils.configs.BASE_URL;
import static com.doiry.baoxiaobao.utils.configs.FILE_PORT;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.doiry.baoxiaobao.adapter.ShowBillListviewAdapter;
import com.doiry.baoxiaobao.beans.ShowBillListviewBeans;
import com.doiry.baoxiaobao.databinding.FragmentHomeBinding;
import com.doiry.baoxiaobao.interact.InfoInteract;
import com.doiry.baoxiaobao.utils.ConfirmDialog;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ExecutionException;

public class HomeFragment extends Fragment {
    private FragmentHomeBinding binding;
    private ListView msheetListView;
    Bitmap bitmap;

    String uid = null;
    int type = -1;

    final Handler handler = new Handler();
    public static final String PREFERENCE_NAME = "SaveSetting";
    public static int MODE = Context.MODE_ENABLE_WRITE_AHEAD_LOGGING;
    ConfirmDialog confirmDialog = null;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {


        confirmDialog = new ConfirmDialog(getContext(), true);
        binding = FragmentHomeBinding.inflate(inflater, container, false);

        @SuppressLint("WrongConstant") SharedPreferences sp = requireActivity().getSharedPreferences(PREFERENCE_NAME,MODE);
        uid = sp.getString("uid", "");
        type = sp.getInt("USER_TYPE", 1);

        initView();

        View root = binding.getRoot();
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    public int initView(){

        msheetListView = binding.lvSheetlist;
        msheetListView.setFocusable(false);

        InfoInteract.getBills(type, uid, new InfoInteract.getCallback() {
            @SuppressLint("ResourceType")
            @Override
            public void onSuccess(String result) {
                try {
                    JSONArray jsonArray = new JSONArray(result);
                    List<ShowBillListviewBeans> showBillListviewBeans = new ArrayList<ShowBillListviewBeans>();
                    for (int i= 0 ; i < jsonArray.length(); i++){
                        JSONObject info = jsonArray.getJSONObject(i);
                        bitmap = Glide.with(getActivity())
                                .asBitmap()
                                .load(BASE_URL + ":" + FILE_PORT + "/" + info.getString("file"))
                                .centerCrop()
                                .into(60, 60)
                                .get();
                        showBillListviewBeans.add(new ShowBillListviewBeans(
                                bitmap,
                                info.getString("name"),
                                new Float(info.getDouble("amount")),
                                info.getString("remark")));
                    }
                    ShowBillListviewAdapter adapter = new ShowBillListviewAdapter(getActivity(), showBillListviewBeans);
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            msheetListView.setAdapter(adapter);

                            msheetListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                                    confirmDialog.show();
                                    confirmDialog.setMessage("Do you want to Download?");
                                    confirmDialog.setConfirmListener(new ConfirmDialog.ConfirmListener() {
                                        @Override
                                        public void onConfirm() {
                                            Toast.makeText(getActivity(), "SAVING...." , Toast.LENGTH_LONG).show();
                                            saveImage(bitmap);
                                        }
                                        @Override
                                        public void onCancel() {

                                        }
                                    });
                                }
                            });
                            msheetListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                                @Override
                                public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                                    return false;
                                }
                            });
                            }
                    });
                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        return 0;
    }

    private void saveImage(Bitmap image) {
        String saveImagePath = null;
        Random random = new Random();
        String imageFileName = "JPEG_" + "down" + random.nextInt(10) + ".jpg";
        File storageDir = new File(Environment.getExternalStoragePublicDirectory
                (Environment.DIRECTORY_PICTURES) + "test");

        boolean success = true;
        if(!storageDir.exists()){
            success = storageDir.mkdirs();
        }
        if(success){
            File imageFile = new File(storageDir, imageFileName);
            saveImagePath = imageFile.getAbsolutePath();
            try {
                OutputStream fout = new FileOutputStream(imageFile);
                image.compress(Bitmap.CompressFormat.JPEG, 100, fout);
                fout.close();
            } catch (Exception e) {
                e.printStackTrace();
            }

            // Add the image to e system gallery
            galleryAddPic(saveImagePath);
            Toast.makeText(getActivity(), "IMAGE SAVED", Toast.LENGTH_LONG).show();
        }
//        return saveImagePath;
    }

    private void galleryAddPic(String imagePath) {
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        File f = new File(imagePath);
        Uri contentUri = Uri.fromFile(f);
        mediaScanIntent.setData(contentUri);
        getContext().sendBroadcast(mediaScanIntent);
    }
}