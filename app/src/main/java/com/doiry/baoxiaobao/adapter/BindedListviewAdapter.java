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

import androidx.recyclerview.widget.RecyclerView;

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
        if (convertView == null) { // 转换视图为空
            holder = new ViewHolder(); // 创建一个新的视图持有者
            // 根据布局文件item_list.xml生成转换视图对象
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_bind_listview, null);
            holder.iv_icon = convertView.findViewById(R.id.bindedInfoListview_Profile_image);
            holder.tv_name = convertView.findViewById(R.id.bindedInfoListview_Name);
            holder.tv_en_number = convertView.findViewById(R.id.bindedInfoListview_Employee_NUmber);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        BindedListviewBeans bindedListviewBeans = mBindInfo.get(position);
        holder.iv_icon.setImageResource(bindedListviewBeans.image);
        holder.tv_name.setText(bindedListviewBeans.name);
        holder.tv_en_number.setText(bindedListviewBeans.Emp_number);
        holder.iv_icon.requestFocus();
        return convertView;
    }

    // 定义一个视图持有者，以便重用列表项的视图资源
    public final class ViewHolder {
        public ImageView iv_icon; // 声明行星图片的图像视图对象
        public TextView tv_name; // 声明行星名称的文本视图对象
        public TextView tv_en_number; // 声明行星描述的文本视图对象
    }

    // 处理列表项的点击事件，由接口OnItemClickListener触发
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        @SuppressLint("DefaultLocale") String desc = String.format("您点击了第%d个行星，它的名字是%s", position + 1,
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
