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

import java.util.List;

public class BindedListviewAdapter extends BaseAdapter {
    private Context mContext;
    private List<BindedListviewBeans> mBindInfo;

    public BindedListviewAdapter(Context context, List<BindedListviewBeans> bindedInfo_list){
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
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_list_binded_info, null);
            holder.iv_icon = convertView.findViewById(R.id.bindedInfoListview_Profile_image);
            holder.tv_name = convertView.findViewById(R.id.bindedInfoListview_Name);
            holder.tv_en_number = convertView.findViewById(R.id.bindedInfoListview_Employee_NUmber);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        BindedListviewBeans bindedListviewBeans = mBindInfo.get(position);
        //holder.iv_icon.setImageResource("bindedListviewBeans.image");
        holder.tv_name.setText(bindedListviewBeans.name);
        holder.tv_en_number.setText(bindedListviewBeans.Emp_number);
        holder.iv_icon.requestFocus();
        return convertView;
    }

    public final class ViewHolder {
        public ImageView iv_icon;
        public TextView tv_name;
        public TextView tv_en_number;
    }

    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        @SuppressLint("DefaultLocale") String desc = String.format("%d，%s", position + 1,
                mBindInfo.get(position).name);
        Toast.makeText(mContext, desc, Toast.LENGTH_LONG).show();
    }

    // 处理列表项的长按事件，由接口OnItemLongClickListener触发
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
        @SuppressLint("DefaultLocale") String desc = String.format("您长按了第%d个行星，它的名字是%s", position + 1,
                mBindInfo.get(position).name);
        Toast.makeText(mContext, desc, Toast.LENGTH_LONG).show();
        return true;
    }
}
