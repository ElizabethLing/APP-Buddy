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
 *  控制显示天、周、月的表情统计显示记录
 * @author Songling
 * @version 1.0.0
 */

public class StacsEmotionActivity extends AppCompatActivity{
    private String day = MinaClientHandler.EmotionDay;
    private String week = MinaClientHandler.EmotionWeek;
    private String month = MinaClientHandler.EmotionMonth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.barchart);
        TextView title = (TextView)findViewById(R.id.title);
        title.setText("易宝情绪数据统计");
        BarChart barChart1 = (BarChart) findViewById(R.id.bar_chart1);
        BarChart barChart2 = (BarChart) findViewById(R.id.bar_chart2);
        BarChart barChart3 = (BarChart) findViewById(R.id.bar_chart3);

        BarChartManager barChartManager1 = new BarChartManager(barChart1);
        BarChartManager barChartManager2 = new BarChartManager(barChart2);
        BarChartManager barChartManager3 = new BarChartManager(barChart3);

        //颜色集合
        List<Integer> colours = new ArrayList<>();
        //colours.add(Color.GREEN);
        colours.add(Color.BLUE);
        colours.add(Color.RED);
        colours.add(Color.CYAN);

        //线的名字集合
        List<String> names = new ArrayList<>();
        //names.add("类型");
        names.add("总次数");
        names.add("总时间");
        names.add("易发时间");

      //测试数据
//        //设置x轴的数据
//        ArrayList<Float> xValues = new ArrayList<>();
//        for (int i = 0; i <= 10; i++) {
//                xValues.add((float) i);
//        }
//
//        //设置y轴的数据()
//        List<List<Float>> yValues = new ArrayList<>();
//        for (int i = 0; i < 4; i++) {
//            List<Float> yValue = new ArrayList<>();
//            for (int j = 0; j <= 10; j++) {
//                yValue.add((float) (Math.random() * 80));
//            }
//            yValues.add(yValue);
//        }

//        int count = 0;
//        while(MinaClientHandler.erResult == 1){
//            try{
//                Thread.sleep(300);
//                count++;
//                if (MinaClientHandler.erResult == 0){
//
//                    Toast.makeText(getApplication(), "情绪数据获取成功", Toast.LENGTH_SHORT).show();
//                    if(day != null){
//                        //ParsingData(day);
//                        barChartManager1.showBarChart(ParsingXData(day),ParsingYData(day), names, colours);
//                        barChartManager1.setXAxis(11f, 0f, 11);
//                        barChartManager1.setDescription("天数据显示");
//                    }
//                    if(week != null){
//                        // ParsingData(week);
//                        barChartManager2.showBarChart(ParsingXData(week),ParsingYData(week), names, colours);
//                        barChartManager2.setXAxis(11f, 0f, 11);
//                        barChartManager2.setDescription("周数据显示");
//                    }
//                    if (month != null){
//                        //ParsingData(month);
//                        barChartManager3.showBarChart(ParsingXData(month),ParsingYData(month), names, colours);
//                        barChartManager3.setXAxis(11f, 0f, 11);
//                        barChartManager3.setDescription("月数据显示");
//                    }
//                    break;
//                }else if (MinaClientHandler.erResult == 201){
//                    Toast.makeText(getApplication(), "没有情绪数据，请稍后查看", Toast.LENGTH_SHORT).show();
//                    break;
//                }
//                if (count == 10){
//                    MinaClientHandler.erResult = 1;
//                    Toast.makeText(getApplicationContext(),"请求超时，请重试",Toast.LENGTH_LONG).show();
//                    break;
//                }
//            }catch(Exception e){
//                e.printStackTrace();
//            }
//        }
        switch (MinaClientHandler.erResult) {
            case 0:
                //Toast.makeText(StacsEmotionActivity.this, "情绪数据获取成功", Toast.LENGTH_SHORT).show();
                if(day != null){
                    //ParsingData(day);
                    barChartManager1.showBarChart(ParsingXData(day),ParsingYData(day), names, colours);
                    barChartManager1.setXAxis(11f, 0f, 11);
                    barChartManager1.setDescription("天数据显示");
                }
                if(week != null){
                   // ParsingData(week);
                    barChartManager2.showBarChart(ParsingXData(week),ParsingYData(week), names, colours);
                    barChartManager2.setXAxis(11f, 0f, 11);
                    barChartManager2.setDescription("周数据显示");
                }
                if (month != null){
                    //ParsingData(month);
                    barChartManager3.showBarChart(ParsingXData(month),ParsingYData(month), names, colours);
                    barChartManager3.setXAxis(11f, 0f, 11);
                    barChartManager3.setDescription("月数据显示");
                }
                break;
            case 201:
                Toast.makeText(StacsEmotionActivity.this, "没有情绪数据，请稍后查看", Toast.LENGTH_SHORT).show();
                break;
            default:
                Toast.makeText(StacsEmotionActivity.this, "情绪数据获取失败，请重新获取", Toast.LENGTH_SHORT).show();
                break;
        }
    }
    private ArrayList<Integer> ParsingXData(String str){
        ArrayList<Integer> xValues = new ArrayList<>();
        try {
            JSONObject jsonObject = new JSONObject(str);
            //JSONObject 获取jsonArray
            JSONArray jsonArray = jsonObject.getJSONArray("emotions");
            System.out.println("StacsEmotionActivity jsonArrayX = " + jsonArray);
            //遍历，jsonArray获取JSONObject
            for (int j = 0; j < jsonArray.length(); j++) {
                JSONObject object = (JSONObject) jsonArray.opt(j);
                String date = object.getString("date");
                xValues.add(RequestTime.longToDay(Long.parseLong(date)));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("emotion xValues = " + xValues);
        return xValues;
    }
    private List<List<Float>> ParsingYData(String str){
        List<List<Float>> yValues = new ArrayList<>();
        try {
            List<Float> yValue = new ArrayList<>();
            List<Float> yValue2 = new ArrayList<>();
            List<Float> yValue3 = new ArrayList<>();
            List<Float> yValue4 = new ArrayList<>();
            JSONObject jsonObject = new JSONObject(str);
            //JSONObject 获取jsonArray
            JSONArray jsonArray = jsonObject.getJSONArray("emotions");
            System.out.println("StacsEmotionActivity jsonArrayY = " + jsonArray);
            //遍历，jsonArray获取JSONObject
            for (int j = 0; j < jsonArray.length(); j++) {
                JSONObject object = (JSONObject) jsonArray.opt(j);
                //int type = object.getInt("type");
                int emtSum = object.getInt("emtSum");
                int emtTotalTime = object.getInt("emtTotalTime");
                int mostEmtTime = object.getInt("mostEmtTime");

                //yValue.add((float) type);
                yValue2.add((float) emtSum);
                yValue3.add(RequestTime.getDurTime(emtTotalTime));
                yValue4.add(RequestTime.getClock(mostEmtTime));
            }
            //yValues.add(yValue);
            yValues.add(yValue2);
            yValues.add(yValue3);
            yValues.add(yValue4);
            System.out.println("emotion yValues = " + yValues);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return yValues;
    }


//    private void ParsingData(String str){
//        ArrayList<Integer> xValues = new ArrayList<>();
//        List<List<Float>> yValues = new ArrayList<>();
//        try {
//            //每组数据有几类
//            for (int i = 0; i < 4; i++) {
//                List<Float> yValue = new ArrayList<>();
//                JSONObject jsonObject = new JSONObject(str);
//                //JSONObject 获取jsonArray
//                JSONArray jsonArray = jsonObject.getJSONArray("emotions");
//                if (jsonArray.length() == 0) {
//                    Toast.makeText(getApplicationContext(), "没有统计数据", Toast.LENGTH_LONG).show();
//                } else {
//                    //遍历，jsonArray获取JSONObject
//                    for (int j = 0; j < jsonArray.length(); j++) {
//                        JSONObject object = (JSONObject) jsonArray.opt(j);
//
//                        int type = object.getInt("type");
//                        String date = object.getString("date");
//                        int emtSum = object.getInt("emtSum");
//                        int emtTotalTime = object.getInt("emtTotalTime");
//                        int mostEmtTime = object.getInt("mostEmtTime");
//
//                        xValues.add(RequestTime.longToDay(Long.parseLong(date)));
//                        yValue.add((float) type);
//                        yValue.add((float) emtSum);
//                        yValue.add((float) emtTotalTime);
//                        yValue.add((float) mostEmtTime);
//                    }
//                    System.out.println("xValues = " + xValues);
//                    yValues.add(yValue);
//                    System.out.println("yValues = " + yValues);
//                }
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }

}
