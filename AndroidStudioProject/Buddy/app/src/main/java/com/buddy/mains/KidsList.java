package com.buddy.mains;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.buddy.R;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.buddy.connectToServer.MinaClientHandler;
import com.buddy.connectToServer.SendMsgToServer;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created on 2018/04/03/10:28.
 * 拿到列表，显示婴儿ID，点击相应的ID,进入数据
 * @author Songling
 * @version V 1.0.0
 */

public class KidsList extends Activity {
    private String kids;
    public static String allInfoCid;
    public static String allInfoName;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.child_list);

        ImageButton add = (ImageButton)findViewById(R.id.setting);
        ListView listview = (ListView) findViewById(R.id.list);
        listChild(listview);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(KidsList.this,AddReviseKids.class);
                KidsList.allInfoCid = null;
                startActivity(intent);
            }
        });
    }

    private void listChild(ListView listview) {
        final List<Map<String, Object>> childlist = new ArrayList<Map<String, Object>>();
        if (MinaClientHandler.ulResult == 0) {
            kids =  MinaClientHandler.sKids;
        }else if (MinaClientHandler.uirResult == 0){
            kids =  MinaClientHandler.kids;
        }
        System.out.println("KidsList kids = " + kids);
        if (kids == null) {
            Toast.makeText(KidsList.this,"请添加添加婴儿",Toast.LENGTH_LONG).show();
        }else{
            try {
                JSONObject jsonObject = new JSONObject(kids);
                JSONArray jsonArray = (JSONArray) jsonObject.get("kids");
                for (int i = 0; i < jsonArray.length(); i++) {
                    Map<String, Object> listItem = new HashMap<String, Object>();
                    JSONObject obj = (JSONObject) jsonArray.get(i);
                    //后面会添加别的婴儿信息，包括姓名、图片等
                    String cid = obj.getString("CID");
                    String cidname = obj.getString("Name");
                    if (cid.equals("") || cidname.equals("")) {
                        Toast.makeText(KidsList.this, "宝宝信息过期，请重新添加", Toast.LENGTH_LONG).show();
                    }else  {
                        listItem.put("CID", cid);
                        listItem.put("cidname", cidname);
                    }
                    childlist.add(listItem);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            SimpleAdapter simpleAdapter = new SimpleAdapter(getApplicationContext(), childlist,
                    R.layout.children_items, new String[]{"CID","cidname"}, new int[]{R.id.cid,R.id.cidname});
            listview.setAdapter(simpleAdapter);
            listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                    //获取arg2的Cid，并作为之后所有请求的Cid
                    allInfoCid = childlist.get(arg2).get("CID").toString();
                    allInfoName = childlist.get(arg2).get("cidname").toString();
                    //bts = 20,get CamId
                    SendMsgToServer getCamId = new SendMsgToServer(20, CamToJson());
                    getCamId.start();
                    try {
                        Thread.sleep(1000);
                        // bts = 1,send a pollNotification Request to server
                        SendMsgToServer pollNotify = new SendMsgToServer(1, PollingToJson());
                        pollNotify.start();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    int count = 0;
                    while(MinaClientHandler.nrResult == 1){
                        try{
                            Thread.sleep(300);
                            count++;
                            if (MinaClientHandler.nrResult == 0){
                                Toast.makeText(KidsList.this, "已经获取最新通知", Toast.LENGTH_SHORT).show();
                                break;
                            }else if (MinaClientHandler.nrResult == 201){
                                Toast.makeText(KidsList.this, "没有新的通知消息", Toast.LENGTH_SHORT).show();
                                break;
                            }
                            if (count == 20){
                                MinaClientHandler.nrResult = 1;
                                Toast.makeText(KidsList.this,"请求超时，请重试",Toast.LENGTH_LONG).show();
                                break;
                            }
                        }catch(Exception e){
                            e.printStackTrace();
                        }
                    }
                    startActivity(new Intent(KidsList.this, MainActivity.class));
//
//                    try {
//                        Thread.sleep(1000);
//                    } catch (InterruptedException e) {
//                        e.printStackTrace();
//                    }
//                    switch (MinaClientHandler.nrResult) {
//                        case 0:
//                            //这个toast后期可以取消
//                            Toast.makeText(getApplication(), "已经获取最新通知", Toast.LENGTH_SHORT).show();
//                            startActivity(new Intent(KidsList.this, MainActivity.class));
//                            break;
//                        case 201:
//                            Toast.makeText(getApplication(), "没有新的通知消息", Toast.LENGTH_SHORT).show();
//                            startActivity(new Intent(KidsList.this, MainActivity.class));
//                            break;
//                        default:
//                            Toast.makeText(getApplication(), "通知获取失败", Toast.LENGTH_SHORT).show();
//                            break;
//                    }
                }
            });
        }
    }
    private String PollingToJson() {
        Gson gson = new GsonBuilder().enableComplexMapKeySerialization().create();
        Map<String,String> pollNotify= new HashMap<String,String>();
        //另外，这个方法每次写一遍好繁琐，不利于修改，应该换一种写法
        pollNotify.put("cid", allInfoCid);
        pollNotify.put("nid","2");
        pollNotify.put("severity","1");
        return gson.toJson(pollNotify);
    }
    private String CamToJson() {
        Gson gson = new GsonBuilder().enableComplexMapKeySerialization().create();
        Map<String,String> getCam = new HashMap<String,String>();
        getCam.put("cid", allInfoCid);
        getCam.put("name", allInfoName);
        return gson.toJson(getCam);
    }

    @Override
    protected void onDestroy() {
        kids = null;
        MinaClientHandler.ulResult = 1;
        MinaClientHandler.uirResult = 1;
        super.onDestroy();
    }
}
