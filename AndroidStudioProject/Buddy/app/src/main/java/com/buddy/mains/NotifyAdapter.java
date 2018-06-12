package com.buddy.mains;

import android.content.Context;
import android.graphics.Bitmap;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.buddy.R;
import com.buddy.utils.AsyncImageLoader;

import java.util.List;
import java.util.Map;


/**
 * Created on 2017/11/9.11:53.
 *  自定义适配器
 * @author Songling
 * @version 1.0.0
*/

class NotifyAdapter extends BaseAdapter {

    private Context context;
    private List<Map<String,Object>> listItems;
    private LayoutInflater listContainer;

    private String[] list;
    private AsyncImageLoader imageLoader;
    @Override
    public int getCount() {
        return listItems.size();
    }

    @Override
    public Object getItem(int position) {
        return list[position];
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public static final class ListItemView{                //自定义控件集合
        ImageView severity;
        ImageView pic;
        public TextView title;
        TextView comment_code;
        TextView startTime;
    }
    public NotifyAdapter(Context context, List<Map<String,Object>> listItems){
        this.context = context;
        listContainer = LayoutInflater.from(context);   //创建视图容器并设置上下文
        this.listItems = listItems;
        imageLoader = new AsyncImageLoader(context);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        //自定义视图
        ListItemView  listItemView = null;
        if (convertView == null) {
            listItemView = new ListItemView();
            //获取list_item布局文件的视图
            convertView = listContainer.inflate(R.layout.frag_data_alarm_items, null);
            //获取控件对象
            listItemView.severity = (ImageView)convertView.findViewById(R.id.alarm_severity);
            listItemView.title = (TextView)convertView.findViewById(R.id.item_state);
            listItemView.startTime = (TextView)convertView.findViewById(R.id.item_time);
            listItemView.comment_code= (TextView) convertView.findViewById(R.id.item_suggest);
            listItemView.pic = (ImageView)convertView.findViewById(R.id.alarm_state_picture);

            //设置控件集到convertView
            convertView.setTag(listItemView);
        }else {
            listItemView = (ListItemView)convertView.getTag();
        }

            //设置文字和图片
            listItemView.severity.setBackgroundResource((Integer) listItems.get(position).get("severity"));
            listItemView.title.setText((String) listItems.get(position).get("title"));
            listItemView.startTime.setText((String) listItems.get(position).get("startTime"));
            listItemView.comment_code.setText((String) listItems.get(position).get("comment_code"));
            listItemView.pic.setImageResource(R.mipmap.baby);

            String imgUrl = (String)listItems.get(position).get("pic");
            if (!TextUtils.isEmpty(imgUrl)) {
                Bitmap bitmap = imageLoader.loadImage(listItemView.pic, imgUrl);
                if (bitmap != null) {
                    listItemView.pic.setImageBitmap(bitmap);
                }
            }
        return convertView;
    }
}
