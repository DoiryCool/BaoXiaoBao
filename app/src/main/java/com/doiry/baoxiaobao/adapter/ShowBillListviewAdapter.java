package com.doiry.baoxiaobao.adapter;

import static com.doiry.baoxiaobao.interact.InfoInteract.checkBillStatus;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.doiry.baoxiaobao.R;
import com.doiry.baoxiaobao.beans.ShowBillListviewBeans;
import com.doiry.baoxiaobao.interact.InfoInteract;

import java.util.List;

public class ShowBillListviewAdapter extends BaseAdapter {
    private Context mContext;
    private List<ShowBillListviewBeans> mBindInfo;

    public Boolean ifOpen = false;

    public ShowBillListviewAdapter(Context context, List<ShowBillListviewBeans> bindedInfo_list){
        mContext = context;
        mBindInfo = bindedInfo_list;
    }

    @Override
    public int getCount() {
        return mBindInfo.size();
    }

    @Override
    public Object getItem(int position) {
        return mBindInfo.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_list_bill, null);
            holder.iv_icon = convertView.findViewById(R.id.showBillProfileImage);
            holder.tv_name = convertView.findViewById(R.id.showBillCommitterName);
            holder.tv_time = convertView.findViewById(R.id.tv_sheet_time);
            holder.tv_amount = convertView.findViewById(R.id.tv_sheet_amount);
            holder.tv_description = convertView.findViewById(R.id.tv_sheet_remark);
            holder.cb_ifProcessed = convertView.findViewById(R.id.cb_sheet_status);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        ShowBillListviewBeans showBillListviewBeans = mBindInfo.get(position);
        holder.iv_icon.setImageBitmap(showBillListviewBeans.bitmap);
        holder.tv_name.setText(showBillListviewBeans.name);
        holder.tv_time.setText(showBillListviewBeans.time);
        holder.tv_amount.setText(showBillListviewBeans.amout.toString());
        holder.tv_description.setText("Remark : \n" + showBillListviewBeans.description);
        holder.cb_ifProcessed.setChecked(showBillListviewBeans.check);
        holder.iv_icon.requestFocus();
        holder.cb_ifProcessed.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                checkBillStatus(1, showBillListviewBeans.id, new InfoInteract.getCallback() {
                    @SuppressLint("ResourceType")
                    @Override
                    public void onSuccess(String result) {

                    }
                });
            }
        });
        return convertView;
    }

    public final class ViewHolder {
        public ImageView iv_icon;
        public TextView tv_name;
        public TextView tv_time;
        public TextView tv_amount;
        public CheckBox cb_ifProcessed;
        public TextView tv_description;
    }

}
