package com.doiry.baoxiaobao.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.doiry.baoxiaobao.R;
import com.doiry.baoxiaobao.beans.BindedListviewBeans;
import com.doiry.baoxiaobao.beans.ShowBillListviewBeans;

import java.util.List;

public class ShowBillListviewAdapter extends BaseAdapter {
    private Context mContext;
    private List<ShowBillListviewBeans> mBindInfo;

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
            holder.tv_amount = convertView.findViewById(R.id.showBillCommitterAmount);
            holder.tv_description = convertView.findViewById(R.id.showBillDescription);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        ShowBillListviewBeans showBillListviewBeans = mBindInfo.get(position);
        //holder.iv_icon.setImageResource("bindedListviewBeans.image");
        holder.tv_name.setText(showBillListviewBeans.name);
        holder.tv_amount.setText(showBillListviewBeans.amout.toString());
        holder.tv_description.setText("Remark : \n" + showBillListviewBeans.description);
        holder.iv_icon.requestFocus();
        return convertView;
    }

    public final class ViewHolder {
        public ImageView iv_icon;
        public TextView tv_name;
        public TextView tv_amount;
        public TextView tv_description;
    }

    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        @SuppressLint("DefaultLocale") String desc = String.format("You clicked line %d , The name is %s", position + 1,
                mBindInfo.get(position).name);
        Toast.makeText(mContext, desc, Toast.LENGTH_LONG).show();
    }

    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
        @SuppressLint("DefaultLocale") String desc = String.format("You clicked line %d , The name is %s", position + 1,
                mBindInfo.get(position).name);
        Toast.makeText(mContext, desc, Toast.LENGTH_LONG).show();
        return true;
    }
}
