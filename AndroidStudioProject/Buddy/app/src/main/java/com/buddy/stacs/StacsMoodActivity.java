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
 * Created on 2017/11/1.09:39.
 * 控制显示天、周、月的哭声统计显示记录
 * @author Songling
 * @version 1.0.0
 */

public class StacsMoodActivity extends AppCompatActivity{
    private String day = MinaClientHandler.MoodDay;
    private String week = MinaClientHandler.MoodWeek;
    private String month = MinaClientHandler.MoodMonth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.barchart);
        TextView title = (TextView)findViewById(R.id.title);
        title.setText("易宝哭闹数据统计");
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
        colours.add(Color.CYAN);
        colours.add(Color.RED);

        //线的名字集合
        List<String> names = new ArrayList<>();
        names.add("哭闹类型");
        names.add("哭闹总次数");
        names.add("哭闹总时间");
        names.add("易哭时间");

//        //如果直接等于0，那么不会走此循环
//        int count = 0;
//        while(MinaClientHandler.mrResult == 1){
//            try{
//                Thread.sleep(300);
//                count++;
//                if (MinaClientHandler.mrResult == 0){
//                    Toast.makeText(getApplication(), "哭闹数据获取成功", Toast.LENGTH_SHORT).show();
//                    if(day != null){
////                    ParsingData(day);
////                    barChartManager1.showBarChart(xValues, yValues, names, colours);
//                        barChartManager1.showBarChart(ParsingXData(day),ParsingYData(day), names, colours);
//                        barChartManager1.setXAxis(11f, 0f, 11);
//                        barChartManager1.setYAxis(100, 0f, 11);
//                        barChartManager1.setDescription("天数据显示");
//                    }
////                if(week != null){
//////                    ParsingData(week);
//////                    barChartManager2.showBarChart(xValues, yValues, names, colours);
////                    barChartManager2.showBarChart(ParsingXData(week),ParsingYData(week), names, colours);
////                    barChartManager2.setXAxis(11f, 0f, 11);
////                    barChartManager2.setYAxis(100, 0f, 11);
////                    barChartManager2.setDescription("周数据显示");
////                }
////                if(month != null){
//////                    ParsingData(month);
//////                    barChartManager3.showBarChart(xValues, yValues, names, colours);
////                    barChartManager3.showBarChart(ParsingXData(month),ParsingYData(month), names, colours);
////                    barChartManager3.setXAxis(11f, 0f, 11);
////                    barChartManager3.setYAxis(100, 0f, 11);
////                    barChartManager3.setDescription("月数据显示");
////                }
//                    break;
//                }else if (MinaClientHandler.mrResult == 201){
//                    Toast.makeText(getApplication(), "没有哭闹数据，请稍后查看", Toast.LENGTH_SHORT).show();
//                    break;
//                }
//                if (count == 10){
//                    MinaClientHandler.mrResult = 1;
//                    Toast.makeText(getApplicationContext(),"请求超时，请重试",Toast.LENGTH_LONG).show();
//                    break;
//                }
//            }catch(Exception e){
//                e.printStackTrace();
//            }
//        }

        switch (MinaClientHandler.mrResult) {
            case 0:
                //Toast.makeText(StacsMoodActivity.this, "哭闹数据获取成功", Toast.LENGTH_SHORT).show();
                if(day != null){
//                    ParsingData(day);
//                    barChartManager1.showBarChart(xValues, yValues, names, colours);
                    barChartManager1.showBarChart(ParsingXData(day),ParsingYData(day), names, colours);
                    barChartManager1.setXAxis(7f, 0f, 11);
                    barChartManager1.setYAxis(100, 0f, 11);
                    barChartManager1.setDescription("天数据显示");
                }
                if(week != null){
//                    ParsingData(week);
//                    barChartManager2.showBarChart(xValues, yValues, names, colours);
                    barChartManager2.showBarChart(ParsingXData(week),ParsingYData(week), names, colours);
                    barChartManager2.setXAxis(7f, 0f, 11);
                    barChartManager2.setYAxis(100, 0f, 11);
                    barChartManager2.setDescription("周数据显示");
                }
                if(month != null){
//                    ParsingData(month);
//                    barChartManager3.showBarChart(xValues, yValues, names, colours);
                    barChartManager3.showBarChart(ParsingXData(month),ParsingYData(month), names, colours);
                    barChartManager3.setXAxis(7f, 0f, 11);
                    barChartManager3.setYAxis(100, 0f, 11);
                    barChartManager3.setDescription("月数据显示");
                }
                break;
            case 201:
                Toast.makeText(StacsMoodActivity.this, "没有哭闹数据，请稍后查看", Toast.LENGTH_SHORT).show();
                break;
            default:
                Toast.makeText(StacsMoodActivity.this, "哭闹数据获取失败，请重新获取", Toast.LENGTH_SHORT).show();
                break;
        }

    }
    private ArrayList<Integer> ParsingXData(String str){
        ArrayList<Integer> xValues = new ArrayList<>();
        try {
            JSONObject jsonObject = new JSONObject(str);
            //JSONObject 获取jsonArray
            JSONArray jsonArray = jsonObject.getJSONArray("moods");
            //System.out.println("StacsMoodActivity jsonArrayX = " + jsonArray);
            //遍历，jsonArray获取JSONObject
            for (int j = 0; j < jsonArray.length(); j++) {
                JSONObject object = (JSONObject) jsonArray.opt(j);
                String date = object.getString("date");
                xValues.add(RequestTime.longToDay(Long.parseLong(date)));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("cry xValues = " + xValues);
        return xValues;
    }
    private List<List<Float>> ParsingYData(String str){
        List<List<Float>> yValues = new ArrayList<>();
        try {
            JSONObject jsonObject = new JSONObject(str);
            //JSONObject 获取jsonArray
            JSONArray jsonArray = jsonObject.getJSONArray("moods");
            //System.out.println("StacsMoodActivity jsonArrayY = " + jsonArray);
            List<Float> yValue = new ArrayList<>();
            List<Float> yValue2 = new ArrayList<>();
            List<Float> yValue3 = new ArrayList<>();
            List<Float> yValue4 = new ArrayList<>();
            //遍历，jsonArray获取JSONObject
            for (int j = 0; j < jsonArray.length(); j++) {
                JSONObject object = (JSONObject) jsonArray.opt(j);
                int type = object.getInt("type");
                int moodSum = object.getInt("moodSum");
                int moodTotalTime = object.getInt("moodTotalTime");
                int mostMoodTime = object.getInt("mostMoodTime");
                yValue.add((float) type);
                yValue2.add((float) moodSum);
                yValue3.add(RequestTime.getDurTime(moodTotalTime));
                yValue4.add(RequestTime.getClock(mostMoodTime));
            }
            yValues.add(yValue);
            yValues.add(yValue2);
            yValues.add(yValue3);
            yValues.add(yValue4);
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("cry yValues = " + yValues);
        return yValues;
    }
//    private void ParsingData(String str){
//        ArrayList<Integer> xValues = new ArrayList<>();
//        List<List<Float>> yValues = new ArrayList<>();
//        try {
//            for (int i = 0; i < 3; i++) {
//                List<Float> yValue = new ArrayList<>();
//                JSONObject jsonObject = new JSONObject(str);
//                //JSONObject 获取jsonArray
//                JSONArray jsonArray = jsonObject.getJSONArray("moods");
//                //遍历，jsonArray获取JSONObject
//                for (int j = 0; j < jsonArray.length(); j++) {
//                    JSONObject object = (JSONObject) jsonArray.opt(j);
//                    int type = object.getInt("type");
//                    String date = object.getString("date");
//                    int moodSum = object.getInt("moodSum");
//                    int moodTotalTime = object.getInt("moodTotalTime");
//                    int mostMoodTime = object.getInt("mostMoodTime");
//                    switch (type) {
//                        case 1:
//                            xValues.add(RequestTime.longToDay(Long.parseLong(date)));
//                            yValue.add((float) moodSum);
//                            yValue.add(RequestTime.getDurTime(moodTotalTime));
//                            yValue.add(Float.parseFloat(RequestTime.getClock(mostMoodTime)));
//                        default:
//                            Toast.makeText(getApplicationContext(), "目前只设计了哭声", Toast.LENGTH_LONG).show();
//                    }
//                System.out.println("xValues = " + xValues);
//                yValues.add(yValue);
//                System.out.println("yValues = " + yValues);
//                }
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }

}
