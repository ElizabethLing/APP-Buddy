package com.buddy.mains;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.buddy.R;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.HashMap;
import java.util.Map;

import com.buddy.connectToServer.SendMsgToServer;

public class FragmentData extends Fragment implements View.OnClickListener {
    private TextView alarmTV;
    private TextView StacsTV;

    private FragDataAlarm mFragAlarm;
    private FragDataStacs mFragStacs;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_data, container, false);
        alarmTV = (TextView) view.findViewById(R.id.button_alarm);
        alarmTV.setOnClickListener(this);
        alarmTV.setSelected(true);
        StacsTV = (TextView) view.findViewById(R.id.button_statics);
        StacsTV.setOnClickListener(this);

        setDefaultFragment();
        return view;
    }

    private void setDefaultFragment() {
        // bts = 2,send a pollNotification Request to server
        SendMsgToServer pollNotify = new SendMsgToServer(2, NotifyToJson());
        pollNotify.start();
        alarmTV.setTextColor(getResources().getColor(R.color.orange));
        FragmentManager fm = getFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();
        mFragAlarm = new FragDataAlarm();
        transaction.add(R.id.linearlayout_content, mFragAlarm);
        transaction.commit();
    }

    /**
     * 碎片选择器
     * 第一步：拿到管理者
     * 第二步：开启事务
     * 第三步：替换
     * 第四步：提交
     */
    @Override
    public void onClick(View v) {
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction beginTransaction = fragmentManager.beginTransaction();
        switch (v.getId()) {

            case R.id.button_alarm:
                if (null == mFragAlarm) {
                    mFragAlarm = new FragDataAlarm();
                }
                beginTransaction.replace(R.id.linearlayout_content, mFragAlarm);
                alarmTV.setTextColor(getResources().getColor(R.color.orange));//文字颜色改变
                StacsTV.setTextColor(getResources().getColor(R.color.black));
                // bts = 2,send a Notification Request to server
                SendMsgToServer pollNotify = new SendMsgToServer(2, NotifyToJson());
                pollNotify.start();

                break;
            case R.id.button_statics:
                if (null == mFragStacs) {
                    mFragStacs = new FragDataStacs();
                }
                beginTransaction.replace(R.id.linearlayout_content, mFragStacs);
                alarmTV.setTextColor(getResources().getColor(R.color.black));//文字颜色改变
                StacsTV.setTextColor(getResources().getColor(R.color.orange));
                break;
            default:
                break;
        }
        beginTransaction.commit();
    }
    private String NotifyToJson() {
        Gson gson = new GsonBuilder().enableComplexMapKeySerialization().create();
        Map<String,String> notify= new HashMap<String,String>();
        System.out.println("FragData KidsList.allInfoCid = " + KidsList.allInfoCid);
        notify.put("cid", KidsList.allInfoCid);
        notify.put("nid","2");
        notify.put("severity","1");
        return gson.toJson(notify);
    }

}
