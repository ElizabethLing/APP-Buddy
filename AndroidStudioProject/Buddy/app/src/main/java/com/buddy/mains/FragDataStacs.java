package com.buddy.mains;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.buddy.R;
import com.buddy.stacs.StacsMoodActivity;
import com.buddy.stacs.StacsEmotionActivity;
import com.buddy.stacs.StacsHeartRateActivity;
import com.buddy.stacs.StacsSleepActivity;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.text.ParseException;
import java.util.HashMap;
import java.util.Map;

import com.buddy.connectToServer.RequestTime;
import com.buddy.connectToServer.SendMsgToServer;

public class FragDataStacs extends android.app.Fragment{
        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.frag_data_stacs,container,false);
            ImageButton IBtnSleep = (ImageButton)view.findViewById(R.id.sleep);
            ImageButton IBtnCry = (ImageButton)view.findViewById(R.id.cry);
            ImageButton IBtnSport = (ImageButton)view.findViewById(R.id.sport);
            ImageButton IBtnHealth = (ImageButton)view.findViewById(R.id.health);
            ImageButton IBtnEmotion = (ImageButton)view.findViewById(R.id.emotion);

            IBtnSleep.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // 4;	Indicate that it is a getSleepRept Request for RawSleep Table
                    try {
                        new SendMsgToServer(4,SleepToJson(1)).start();
                        Thread.sleep(500);
                        new SendMsgToServer(4,SleepToJson(2)).start();
                        Thread.sleep(500);
                        new SendMsgToServer(4,SleepToJson(3)).start();
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    startActivity(new Intent(getActivity(),StacsSleepActivity.class));
                }
            });
            IBtnCry.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // 6;	Indicate that it is a getMood Request
                    try {
                        new SendMsgToServer(6,MoodToJson(1,0)).start();
                        Thread.sleep(500);
                        new SendMsgToServer(6,MoodToJson(2,0)).start();
                        Thread.sleep(500);
                        new SendMsgToServer(6,MoodToJson(3,0)).start();
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    startActivity(new Intent(getActivity(),StacsMoodActivity.class));
                }
            });
//            IBtnSport.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    Intent intent = new Intent(getActivity(),StacsCryActivity.class);
//                    startActivity(intent);
//                }
//            });
            IBtnHealth.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // 9;	Indicate that it is a getHealthRept Request for RawHealth Table
                    try {
                        new SendMsgToServer(9,HealthToJson(1)).start();
                        Thread.sleep(500);
                        new SendMsgToServer(9,HealthToJson(2)).start();
                        Thread.sleep(500);
                        new SendMsgToServer(9,HealthToJson(3)).start();
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    startActivity(new Intent(getActivity(),StacsHeartRateActivity.class));
                }
            });
            IBtnEmotion.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //8;	 Indicate that it is a getEmotionRept Request for Emotion Table
                    try {
                        new SendMsgToServer(8,EmotionToJson(1,0)).start();
                        Thread.sleep(500);
                        new SendMsgToServer(8,EmotionToJson(2,0)).start();
                        Thread.sleep(500);
                        new SendMsgToServer(8,EmotionToJson(3,0)).start();
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    startActivity(new Intent(getActivity(),StacsEmotionActivity.class));
                }
            });
        return view;
    }

    private String SleepToJson(int pd) {
        Gson gson = new GsonBuilder().enableComplexMapKeySerialization().create();
        Map<String,String> sleep = new HashMap<String,String>();
        sleep.put("cid", KidsList.allInfoCid);
        sleep.put("pd",String.valueOf(pd));
        try {
                sleep.put("from_time", RequestTime.getDay(5));
                sleep.put("to_time", RequestTime.getNowTime());
            } catch (ParseException e) {
                e.printStackTrace();
            }
        return gson.toJson(sleep);
    }

    //pd表示时间周期，i，2，3分别是日周月；type表示请求类型，type = 0表示所有情绪类型
    private String EmotionToJson(int pd,int type) {
        Gson gson = new GsonBuilder().enableComplexMapKeySerialization().create();
        Map<String,String> emotion = new HashMap<String,String>();
        Map<String,Integer> bParams = new HashMap<>();
//        bParams.put("startId",0);
//        bParams.put("pageCount",1);
//        bParams.put("pageSize",10);
//        emotion.put("bParams",String.valueOf(bParams));
        emotion.put("cid", KidsList.allInfoCid);
        emotion.put("pd",String.valueOf(pd));
        emotion.put("type",String.valueOf(type));
        try {
            emotion.put("from_time", RequestTime.getDay(3));
            emotion.put("to_time", RequestTime.getNowTime());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return gson.toJson(emotion);
    }

    private String HealthToJson(int pd) {
        Gson gson = new GsonBuilder().enableComplexMapKeySerialization().create();
        Map<String,String> sleep = new HashMap<String,String>();
        sleep.put("cid", KidsList.allInfoCid);
        sleep.put("pd",String.valueOf(pd));
        try {
            sleep.put("from_time", RequestTime.getDay(10));
            sleep.put("to_time", RequestTime.getNowTime());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return gson.toJson(sleep);
    }
    private String MoodToJson(int pd,int type) {
        Gson gson = new GsonBuilder().enableComplexMapKeySerialization().create();
        Map<String, String> mood = new HashMap<String, String>();
        mood.put("cid", KidsList.allInfoCid);
        mood.put("pd", String.valueOf(pd));
        mood.put("type", String.valueOf(type));
        try {
            mood.put("from_time", RequestTime.getDay(5));
            mood.put("to_time", RequestTime.getNowTime());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return gson.toJson(mood);
    }

}
