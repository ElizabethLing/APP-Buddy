package com.buddy.stacs;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
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
 * Created on 2017/12/09/21:46.
 *  完成心率显示
 * @author Songling
 * @version V 1.0.0
 */


public class StacsHeartRateActivity extends AppCompatActivity {
    private String health = MinaClientHandler.Health;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.barchart);

        TextView title = (TextView)findViewById(R.id.title);
        title.setText("易宝健康数据统计");
        BarChart barChart1 = (BarChart) findViewById(R.id.bar_chart1);
        BarChart barChart2 = (BarChart) findViewById(R.id.bar_chart2);
        BarChart barChart3 = (BarChart) findViewById(R.id.bar_chart3);
        barChart2.setVisibility(View.GONE);
        barChart3.setVisibility(View.GONE);

        BarChartManager barChartManager1 = new BarChartManager(barChart1);
//        BarChartManager barChartManager2 = new BarChartManager(barChart2);
//        BarChartManager barChartManager3 = new BarChartManager(barChart3);

        //颜色集合
        List<Integer> colours = new ArrayList<>();
        colours.add(Color.GREEN);
        colours.add(Color.BLUE);
        colours.add(Color.RED);

        //线的名字集合
        List<String> names = new ArrayList<>();
        names.add("breath");
        names.add("体温");
        names.add("心率");

//        int count = 0;
//        while(MinaClientHandler.rhrResult == 1){
//            try{
//                Thread.sleep(300);
//                count++;
//                if (MinaClientHandler.rhrResult == 0){
//                    Toast.makeText(getApplication(), "健康数据获取成功", Toast.LENGTH_SHORT).show();
//                    if(health != null){
//                        //ParsingData(health);
//                        barChartManager1.showBarChart(ParsingXData(health),ParsingYData(health), names, colours);
//                        barChartManager1.setXAxis(11f, 0f, 11);
//                        barChartManager1.setDescription("健康数据显示");
//                    }
//                    break;
//                }else if (MinaClientHandler.rhrResult == 201){
//                    Toast.makeText(getApplication(), "没有健康数据，请稍后查看", Toast.LENGTH_SHORT).show();
//                    break;
//                }
//                if (count == 10){
//                    MinaClientHandler.rhrResult = 1;
//                    Toast.makeText(getApplicationContext(),"请求超时，请重试",Toast.LENGTH_LONG).show();
//                    break;
//                }
//            }catch(Exception e){
//                e.printStackTrace();
//            }
//        }

        switch (MinaClientHandler.rhrResult) {
            case 0:
                //Toast.makeText(StacsHeartRateActivity.this, "健康数据获取成功", Toast.LENGTH_SHORT).show();
                if(health != null){
                    //ParsingData(health);
                    barChartManager1.showBarChart(ParsingXData(health),ParsingYData(health), names, colours);
                    barChartManager1.setXAxis(11f, 0f, 11);
                    barChartManager1.setDescription("健康数据显示");
                }
                break;
            case 201:
                Toast.makeText(StacsHeartRateActivity.this, "没有健康数据，请稍后查看", Toast.LENGTH_SHORT).show();
                break;
            default:
                Toast.makeText(StacsHeartRateActivity.this, "健康数据获取失败，请重新获取", Toast.LENGTH_SHORT).show();
                break;
        }
    }
    private ArrayList<Integer> ParsingXData(String str){
        ArrayList<Integer> xValues = new ArrayList<>();
        try {
                JSONObject jsonObject = new JSONObject(str);
                //JSONObject 获取jsonArray
                JSONArray jsonArray = jsonObject.getJSONArray("healths");
                    //遍历，jsonArray获取JSONObject
                    for (int j = 0; j < jsonArray.length(); j++) {
                        JSONObject object = (JSONObject) jsonArray.opt(j);
                        String mStartTime = object.getString("mStartTime");
                        //String mEndTime = object.getString("mEndTime");
                        //这里以开始时间为x轴
                        //yValue3.add(RequestTime.getClock(mostSlpTime));
                        xValues.add(Integer.parseInt(mStartTime.substring(8,10)));
                    }
                    System.out.println("xValues = " + xValues);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return xValues;
    }
    private List<List<Float>> ParsingYData(String str){
        List<List<Float>> yValues = new ArrayList<>();
        try {
            List<Float> yValue = new ArrayList<>();
            List<Float> yValue2 = new ArrayList<>();
            List<Float> yValue3 = new ArrayList<>();
            JSONObject jsonObject = new JSONObject(str);
            //JSONObject 获取jsonArray
            JSONArray jsonArray = jsonObject.getJSONArray("healths");
            //遍历，jsonArray获取JSONObject
            for (int j = 0; j < jsonArray.length(); j++) {
                JSONObject object = (JSONObject) jsonArray.opt(j);
                long temp = object.getLong("temp");
                String	heartRate = object.getString("heartRate");
                long breath = object.getLong("breath");
                //String mEndTime = object.getString("mEndTime");
                //这里以开始时间为x轴
                yValue.add((float) temp);
                yValue2.add((float) breath);
                yValue3.add(Float.parseFloat(heartRate));
            }
            yValues.add(yValue);
            yValues.add(yValue2);
            yValues.add(yValue3);
            System.out.println("yValues = " + yValues);
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
//            for (int i = 0; i < 3; i++) {
//                List<Float> yValue = new ArrayList<>();
//                JSONObject jsonObject = new JSONObject(str);
//                //JSONObject 获取jsonArray
//                JSONArray jsonArray = jsonObject.getJSONArray("healths");
//                if (jsonArray.length() == 0) {
//                    Toast.makeText(getApplicationContext(), "没有统计数据", Toast.LENGTH_LONG).show();
//                } else {
//                    //遍历，jsonArray获取JSONObject
//                    for (int j = 0; j < jsonArray.length(); j++) {
//                        JSONObject object = (JSONObject) jsonArray.opt(j);
//                        long temp = object.getLong("temp");
//                        String	heartRate = object.getString("heartRate");
//                        long breath = object.getLong("breath");
//                        String mStartTime = object.getString("mStartTime");
//                        //String mEndTime = object.getString("mEndTime");
//                        //这里以开始时间为x轴
//                        xValues.add(RequestTime.longToDay(Long.parseLong(mStartTime)));
//                        yValue.add((float) temp);
//                        yValue.add((float) breath);
//                        yValue.add(Float.parseFloat(heartRate));
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
