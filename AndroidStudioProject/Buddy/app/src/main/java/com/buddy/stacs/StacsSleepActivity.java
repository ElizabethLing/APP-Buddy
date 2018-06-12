package com.buddy.stacs;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;
import android.widget.TextView;
import android.widget.Toast;

import com.buddy.R;
import com.buddy.connectToServer.MinaClientHandler;
import com.buddy.connectToServer.RequestTime;
import com.buddy.manager.BarChartManager;
import com.github.mikephil.charting.charts.BarChart;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created on 2017/11/4.12:18.
 *  控制显示天、周、月的睡眠统计显示记录
 * @author Songling
 * @version 1.0.0
 */

public class StacsSleepActivity extends AppCompatActivity{
    private String day = MinaClientHandler.SleepDay;
    private String week = MinaClientHandler.SleepWeek;
    private String month = MinaClientHandler.SleepMonth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.barchart);
        TextView title = (TextView)findViewById(R.id.title);
        title.setText("易宝睡眠数据统计");
        BarChart barChart1 = (BarChart) findViewById(R.id.bar_chart1);
        BarChart barChart2 = (BarChart) findViewById(R.id.bar_chart2);
        BarChart barChart3 = (BarChart) findViewById(R.id.bar_chart3);

        BarChartManager barChartManager1 = new BarChartManager(barChart1);
        BarChartManager barChartManager2 = new BarChartManager(barChart2);
        BarChartManager barChartManager3 = new BarChartManager(barChart3);

        //颜色集合
        List<Integer> colours = new ArrayList<>();
        colours.add(Color.GREEN);
        colours.add(Color.BLUE);
        colours.add(Color.RED);

        //线的名字集合
        List<String> names = new ArrayList<>();
        names.add("睡眠总次数");
        names.add("睡眠总时间");
        names.add("易睡时间");

//        int count = 0;
//        while(MinaClientHandler.srResult == 1){
//            try{
//                Thread.sleep(300);
//                count++;
//                if (MinaClientHandler.srResult == 0){
//                    Toast.makeText(getApplication(), "睡眠数据获取成功", Toast.LENGTH_SHORT).show();
//                    if (day != null) {
//                        barChartManager1.showBarChart(ParsingXData(day),ParsingYData(day), names, colours);
//                        barChartManager1.setXAxis(11f, 0f, 11);
//                        barChartManager1.setDescription("天数据显示");
//                    }
//                    if (week != null) {
//                        barChartManager2.showBarChart(ParsingXData(week), ParsingYData(week), names, colours);
//                        barChartManager2.setXAxis(11f, 0f, 11);
//                        //barChartManager2.setYAxis(1000, 0f, 11);
//                        barChartManager2.setDescription("周数据显示");
//                    }
//                    if (month != null) {
//                        barChartManager3.showBarChart(ParsingXData(month),ParsingYData(month), names, colours);
//                        barChartManager3.setXAxis(11f, 0f, 11);
//                        barChartManager3.setDescription("月数据显示");
//                    }
//                    break;
//                }else if (MinaClientHandler.srResult == 201){
//                    Toast.makeText(getApplication(), "没有睡眠数据，请稍后查看", Toast.LENGTH_SHORT).show();
//                    break;
//                }
//                if (count == 20){
//                    MinaClientHandler.srResult = 1;
//                    Toast.makeText(getApplicationContext(),"请求超时，请重试",Toast.LENGTH_LONG).show();
//                    break;
//                }
//            }catch(Exception e){
//                e.printStackTrace();
//            }
//        }
        switch (MinaClientHandler.srResult) {
            case 0:
                Toast.makeText(StacsSleepActivity.this, "睡眠数据获取成功", Toast.LENGTH_SHORT).show();
                if (day != null) {
                    barChartManager1.showBarChart(ParsingXData(day),ParsingYData(day), names, colours);
                    barChartManager1.setXAxis(7f, 0f, 11);
                    barChartManager1.setDescription("天数据显示");
                }
                if (week != null) {
                    barChartManager2.showBarChart(ParsingXData(week), ParsingYData(week), names, colours);
                    barChartManager2.setXAxis(7f, 0f, 11);
                    //barChartManager2.setYAxis(1000, 0f, 11);
                    barChartManager2.setDescription("周数据显示");
                }
                if (month != null) {
                    barChartManager3.showBarChart(ParsingXData(month),ParsingYData(month), names, colours);
                    barChartManager3.setXAxis(7f, 0f, 11);
                    barChartManager3.setDescription("月数据显示");
                }
                break;
            case 201:
                Toast.makeText(StacsSleepActivity.this, "没有睡眠数据，请稍后查看", Toast.LENGTH_SHORT).show();
                break;
            default:
                Toast.makeText(StacsSleepActivity.this, "睡眠数据获取失败，请重新获取", Toast.LENGTH_SHORT).show();
                break;
        }
    }
    private ArrayList<Integer> ParsingXData(String str){
        ArrayList<Integer> xValues = new ArrayList<>();
        try {
            JSONObject jsonObject = new JSONObject(str);
            //JSONObject 获取jsonArray
            JSONArray jsonArray = jsonObject.getJSONArray("sleeps");
            //遍历，jsonArray获取JSONObject
            for (int j = 0; j < jsonArray.length(); j++) {
                JSONObject object = (JSONObject) jsonArray.opt(j);
                String date = object.getString("date");
                xValues.add(RequestTime.longToDay(Long.parseLong(date)));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("sleep xValues = " + xValues);
        return xValues;
    }
    private List<List<Float>> ParsingYData(String str){
        List<List<Float>> yValues = new ArrayList<>();
        try {
            JSONObject jsonObject = new JSONObject(str);
            //JSONObject 获取jsonArray
            JSONArray jsonArray = jsonObject.getJSONArray("sleeps");
            //System.out.println("jsonArray = " + jsonArray);
            //每组数据有几类
            List<Float> yValue = new ArrayList<>();
            List<Float> yValue2 = new ArrayList<>();
            List<Float> yValue3 = new ArrayList<>();
            //遍历，jsonArray获取JSONObject
            for (int j = 0; j < jsonArray.length(); j++) {
                JSONObject object = (JSONObject) jsonArray.opt(j);
                int slpSum = object.getInt("slpSum");
                int slpTotalTime = object.getInt("slpTotalTime");
                int mostSlpTime = object.getInt("mostSlpTime");
                yValue.add((float) slpSum);
                yValue2.add(RequestTime.getDurTime(slpTotalTime));
                yValue3.add(RequestTime.getClock(mostSlpTime));
            }
            yValues.add(yValue);
            yValues.add(yValue2);
            yValues.add(yValue3);
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("sleep yValues = " + yValues);
        return yValues;
    }

//    private void ParsingData(String str){
//        try {
//            JSONObject jsonObject = new JSONObject(str);
//            //JSONObject 获取jsonArray
//            JSONArray jsonArray = jsonObject.getJSONArray("sleeps");
//            System.out.println("jsonArray = " + jsonArray);
//            //每组数据有几类
//            List<Float> yValue = new ArrayList<>();
//            List<Float> yValue2 = new ArrayList<>();
//            //遍历，jsonArray获取JSONObject
//            for (int j = 0; j < jsonArray.length(); j++) {
//                JSONObject object = (JSONObject) jsonArray.opt(j);
//                String date = object.getString("date");
//                int slpSum = object.getInt("slpSum");
//                int slpTotalTime = object.getInt("slpTotalTime");
//                //int mostSlpTime = object.getInt("mostSlpTime");
//                xValues.add(RequestTime.longToDay(Long.parseLong(date)));
//                yValue.add((float) slpSum);
//                yValue2.add(RequestTime.getDurTime(slpTotalTime));
//            }
//            yValues.add(yValue);
//            yValues.add(yValue2);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
}
