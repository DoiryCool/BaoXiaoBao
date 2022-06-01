package com.doiry.baoxiaobao.utils;


import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.doiry.baoxiaobao.R;

/**
 * create by devnn on 2019-07-25
 */
public class ConfirmDialog extends Dialog implements View.OnClickListener{

    private ImageButton imgClose;//右上角，关闭图标

    private Button btnConfirm;//确认按钮

    private Button btnCancel;//取消按钮

    private TextView tvMessage;//要显示的消息

    private ConfirmListener confirmListener;//确认按钮的回调

    private boolean showCancelButton;//是否需要显示取消按钮

    public ConfirmDialog(Context context,boolean showCancelButton) {
        super(context);
        this.showCancelButton=showCancelButton;
    }

    //监听器，需要你的Activity实现它
    public interface ConfirmListener{
        void onConfirm();
        void onCancel();
    }

    /**
     * 设置"确定"按钮的监听
     * @param confirmListener
     */
    public void setConfirmListener(ConfirmListener confirmListener) {
        this.confirmListener = confirmListener;
    }

    /**
     * 设置要显示的文字，比如"确定要删除吗？"
     * @param message
     */
    public void setMessage(String message) {
        if (tvMessage != null) {
            tvMessage.setText(message);
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        setContentView(R.layout.layout_dialog_confirm);
        imgClose=findViewById(R.id.dialog_close);
        btnConfirm=findViewById(R.id.dialog_confirm);
        btnConfirm.setOnClickListener(this);
        btnCancel=findViewById(R.id.dialog_cancel);
        btnCancel.setOnClickListener(this);
        if(showCancelButton){
            btnCancel.setVisibility(View.VISIBLE);
        }else{
            btnCancel.setVisibility(View.GONE);
        }
        tvMessage=findViewById(R.id.dialog_message);
        imgClose.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if(v.equals(this.imgClose)){
            dismiss();
        }else if(v.equals(this.btnConfirm)){
            if(confirmListener!=null){
                confirmListener.onConfirm();
            }
            dismiss();
        }else if(v.equals(this.btnCancel)){
            if(confirmListener!=null){
                confirmListener.onCancel();
            }
            dismiss();
        }
    }
}

