package com.buddy.mains;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.buddy.R;
import com.buddy.listener.OnReceivedMessage;
import com.buddy.manager.MessageManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.buddy.connectToServer.RequestTime;
import com.buddy.connectToServer.MinaClientHandler;

public class FragDataAlarm extends Fragment implements OnReceivedMessage {
    private List<Map<String, Object>> listItems;
    private NotifyAdapter myAdapter;
    public static String PosUrl;
    private ArrayList<String> videolist = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.frag_data_alarm,container,false);

        ListView(view);
        return view;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        listItems  = new ArrayList<Map<String, Object>>();
        myAdapter = new NotifyAdapter(getActivity(), listItems);
        MessageManager.getInstance().setOnReceivedMessage(this);
    }

    public void ListView(View view) {
        ListView listView = (ListView) view.findViewById(R.id.listView);
        listItems.clear();
        listItems.addAll(getListItems(MinaClientHandler.notify));
        myAdapter = new NotifyAdapter(getActivity(), listItems);
        listView.setAdapter(myAdapter);
        myAdapter.notifyDataSetChanged();

        //ListView列表中的元素的单击事件响应
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                PosUrl = videolist.get(arg2);
                Intent intent = new Intent(getActivity(), VideoPlayer.class);
                startActivity(intent);
            }
        });
    }

    private List<Map<String, Object>> getListItems(String result) {

        List<Map<String, Object>> listItems = new ArrayList<Map<String, Object>>();
        if(TextUtils.isEmpty(result)){
            return listItems;
        }
        String jsonNotifyString = MinaClientHandler.notify;
        if (jsonNotifyString == null) {
            Toast.makeText(getActivity(),"服务器异常，请稍后重试",Toast.LENGTH_LONG).show();
        }else {

            try {
                JSONObject jsonObject = new JSONObject(jsonNotifyString);
                //JSONObject 获取jsonArray
                JSONArray jsonArray = jsonObject.getJSONArray("notifs");
                if (jsonArray == null) {
                    Toast.makeText(getActivity(), "没有最新消息", Toast.LENGTH_SHORT).show();
                }
                //遍历，jsonArray获取JSONObject
                assert jsonArray != null;
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject object = (JSONObject) jsonArray.opt(i);
                    Map<String, Object> listItem = new HashMap<String, Object>();
                    //根据类型获取对应的值
                    String title = object.getString("title");
                    String startTime = object.getString("startTime");
                    int severity = object.getInt("severity");
                    String comment_code = object.getString("comment_code");
                    String streamFiles = object.getString("streamFiles");
                    String pic = object.getString("pic");
                    videolist.add(streamFiles);

                    listItem.put("pic",pic);
                    listItem.put("title", title);
                    listItem.put("startTime", RequestTime.stampToMinute(startTime));

                    switch (comment_code) {
                        case "250":
                            listItem.put("comment_code", "使用驱蚊水");
                            break;
                        case "255":
                            listItem.put("comment_code", "添加辅食");
                            break;
                        case "260":
                            listItem.put("comment_code", "睡觉时间");
                            break;
                        case "265":
                            listItem.put("comment_code", "使用睡袋");
                            break;
                        default:
                            break;
                    }
                    switch (severity) {
                        case 1:
                            listItem.put("severity", R.mipmap.sfirst);
                            break;
                        case 2:
                            listItem.put("severity", R.mipmap.ssecond);
                            break;
                        case 3:
                            listItem.put("severity", R.mipmap.sthree);
                            break;
                        case 4:
                            listItem.put("severity", R.mipmap.sfour);
                            break;
                        default:
                            break;
                    }
                    listItems.add(listItem);
                }
                return listItems;

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return listItems;
    }

    @Override
    public void onReceived(String result) {
        if(null != listItems && null != myAdapter){
            listItems.clear();
            listItems.addAll(getListItems(result));
            myAdapter.notifyDataSetChanged();
        }
    }

}
