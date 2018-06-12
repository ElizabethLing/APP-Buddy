package com.buddy.connectToServer.ServerResponse;

import com.buddy.connectToServer.RequestTime;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created on 2018/5/10.15:44.
 *
 * @author Songling
 * @version 1.0.0
 */

public class ParseData {
    public ArrayList<Integer> xValues = new ArrayList<>();
    public List<List<Float>> yValues = new ArrayList<>();

    private void ParsingData(String str){
        try {
            JSONObject jsonObject = new JSONObject(str);
            //JSONObject 获取jsonArray
            JSONArray jsonArray = jsonObject.getJSONArray("sleeps");
            //每组数据有几类
            List<Float> yValue = new ArrayList<>();
            List<Float> yValue2 = new ArrayList<>();
            //遍历，jsonArray获取JSONObject
            for (int j = 0; j < jsonArray.length(); j++) {
                JSONObject object = (JSONObject) jsonArray.opt(j);
                String date = object.getString("date");
                int slpSum = object.getInt("slpSum");
                int slpTotalTime = object.getInt("slpTotalTime");
                //int mostSlpTime = object.getInt("mostSlpTime");
                xValues.add(RequestTime.longToDay(Long.parseLong(date)));
                yValue.add((float) slpSum);
                yValue2.add(RequestTime.getDurTime(slpTotalTime));
            }
            yValues.add(yValue);
            yValues.add(yValue2);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
