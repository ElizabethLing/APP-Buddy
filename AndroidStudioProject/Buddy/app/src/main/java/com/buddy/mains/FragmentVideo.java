package com.buddy.mains;

import android.app.*;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import com.buddy.R;
import com.buddy.connectToServer.MinaClientHandler;
import com.buddy.connectToServer.RequestTime;
import io.vov.vitamio.MediaPlayer;
import io.vov.vitamio.Vitamio;
import io.vov.vitamio.widget.MediaController;
import io.vov.vitamio.widget.VideoView;

/**
 * Created on 2017/11/28.
 * 主页面的显示
 *  1、rtmp直播流的播放，设置播放和暂停按钮
 *  2、设置listView显示列表
 * @author Songling
 * @version V 1.0.0
 */
public class FragmentVideo extends Fragment {
    public static String PosUrl;
    private VideoView mVideoView;
    private MediaController mMediaController;
    private Button btnPlay, btnPause;
    private String jsonString;
    private ArrayList<String> videolist = new ArrayList<>();

    private FrameLayout frameLayout;
    private TextView downloadRateView, loadRateView;
    private ProgressBar pb;
    private String UrlTop= "rtmp://211.149.181.66/father/";
    //private String url = "rtmp://live.hkstv.hk.lxdns.com/live/hks";
    //private String Url = "rtmp://211.149.181.66/father/pyq";
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_video, container, false);
        init();

        frameLayout = (FrameLayout)view.findViewById(R.id.framelayout);
        pb = (ProgressBar)view.findViewById(R.id.probar);
        downloadRateView = (TextView) view.findViewById(R.id.download_rate);
        loadRateView = (TextView) view.findViewById(R.id.load_rate);
        mVideoView = (VideoView) view.findViewById(R.id.buffer);

        try {
            PlayRtmpVideo(view);
        } catch (IOException e) {
            e.printStackTrace();
        }
        ListView(view);
        return view;
    }

    private void init() {
        //定义全屏参数
        int flag = WindowManager.LayoutParams.FLAG_FULLSCREEN;
        //获得当前窗体对象
        Window window = getActivity().getWindow();
        //设置当前窗体为全屏显示
        window.setFlags(flag, flag);
    }

    public void PlayRtmpVideo(View view) throws IOException{
        //初始化加载库文件
        if (Vitamio.isInitialized(getActivity())) {
            mVideoView.setVideoURI(Uri.parse(getCamUrl()));
            //mVideoView.setVideoURI(Uri.parse(Url));
            mVideoView.setVideoQuality(MediaPlayer.VIDEOQUALITY_MEDIUM);
            MediaController mediaController = new MediaController(getActivity(),true,frameLayout);
            mVideoView.setMediaController(mediaController);                 //将控制条附在Videoview中
            mediaController.setVisibility(View.VISIBLE);
            mVideoView.setBufferSize(10240); //设置视频缓冲大小
            mVideoView.requestFocus();
            mVideoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mediaPlayer) {
                    mediaPlayer.setPlaybackSpeed(1.0f);
                }
            });
            mVideoView.setOnBufferingUpdateListener(new MediaPlayer.OnBufferingUpdateListener() {
                @Override
                public void onBufferingUpdate(MediaPlayer mp, int percent) {
                    loadRateView.setText(percent + "%");
                }
            });
            mVideoView.setOnInfoListener(new MediaPlayer.OnInfoListener() {
                @Override
                public boolean onInfo(MediaPlayer mp, int what, int extra) {
                    switch (what) {
                        //开始缓冲
                        case MediaPlayer.MEDIA_INFO_BUFFERING_START:
                            if (mVideoView.isPlaying()) {
                                mVideoView.pause();
                                pb.setVisibility(View.VISIBLE);
                                downloadRateView.setText("");
                                loadRateView.setText("");
                                downloadRateView.setVisibility(View.VISIBLE);
                                loadRateView.setVisibility(View.VISIBLE);
                            }
                            break;
                        //缓冲结束
                        case MediaPlayer.MEDIA_INFO_BUFFERING_END:
                            mVideoView.start();
                            pb.setVisibility(View.GONE);
                            downloadRateView.setVisibility(View.GONE);
                            loadRateView.setVisibility(View.GONE);
                            break;
                        //正在缓冲
                        case MediaPlayer.MEDIA_INFO_DOWNLOAD_RATE_CHANGED:
                            downloadRateView.setText("" + extra + "kb/s" + "  ");
                            break;
                    }
                    return true;
                }
            });
        }
    }

    //获取完整直播流地址
    private String getCamUrl() {
//        int count = 0;
//        while(MinaClientHandler.cirResult != 0){
//            try{
//                Thread.sleep(300);
//                count++;
//                if (MinaClientHandler.cirResult == 0){
//                    //Toast.makeText(getActivity(), "已经获取最新通知", Toast.LENGTH_SHORT).show();
//                    break;
//                }else if (MinaClientHandler.cirResult == 201){
//                    Toast.makeText(getActivity(), "直播流获取失败", Toast.LENGTH_SHORT).show();
//                    break;
//                }
//                if (count == 20){
//                    MinaClientHandler.cirResult = 1;
//                    Toast.makeText(getActivity(),"直播流请求超时，请重试",Toast.LENGTH_LONG).show();
//                    break;
//                }
//            }catch(Exception e){
//                e.printStackTrace();
//            }
//        }
        System.out.println("FragmentVideo CamId = " + UrlTop + MinaClientHandler.camId);
        return UrlTop + MinaClientHandler.camId;
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        //屏幕切换时，设置全屏
        if (mVideoView != null){
            mVideoView.setVideoLayout(VideoView.VIDEO_LAYOUT_FIT_PARENT,0 );
        }
        if(this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {   //如果是横屏
            getActivity().getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                                                WindowManager.LayoutParams.FLAG_FULLSCREEN);     //隐藏系统通知栏
            getActivity().requestWindowFeature(Window.FEATURE_NO_TITLE);
            getActivity().getWindowManager().getDefaultDisplay();

            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.MATCH_PARENT);
            frameLayout.setLayoutParams(params);
        }
        super.onConfigurationChanged(newConfig);
    }

    public void ListView(View view) {
        ListView listView = (ListView) view.findViewById(R.id.listView);
        List<Map<String, Object>> listItems = getListItems();
        NotifyAdapter myAdapter = new NotifyAdapter(getActivity(), listItems);
        myAdapter.notifyDataSetChanged();
        listView.setAdapter(myAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                PosUrl = videolist.get(arg2);
                Intent intent = new Intent(getActivity(), VideoPlayer.class);
                System.out.println("arg2 = " + arg2);
                startActivity(intent);
            }
        });
//        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
//            @Override
//            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
//                AlertDialog.Builder builder=new AlertDialog.Builder(getContext());
//                builder.setMessage("确定删除?");
//                builder.setTitle("提示");
//                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialogInterface, int i) {
//                        Toast.makeText(getActivity(), "假的", Toast.LENGTH_SHORT).show();
//                    }
//                });
//                builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialogInterface, int i) {
//                        Toast.makeText(getActivity(), "假的", Toast.LENGTH_SHORT).show();
//                    }
//                });
//                return true;
//            }
//        });
    }

    private List<Map<String, Object>> getListItems() {
        List<Map<String, Object>> listItems = new ArrayList<Map<String, Object>>();
        jsonString = MinaClientHandler.notify;
        if(TextUtils.isEmpty(jsonString)){
            return listItems;
        }
        if (jsonString == null) {
            Toast.makeText(getActivity(),"服务器异常，请稍后重试",Toast.LENGTH_LONG).show();
        }else {
            try {
                JSONObject jsonObject = new JSONObject(jsonString);
                JSONArray jsonArray = jsonObject.getJSONArray("notifs");
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject object = (JSONObject) jsonArray.opt(i);
                    Map<String, Object> listItem = new HashMap<String, Object>();

                    String title = object.getString("title");
                    String startTime = object.getString("startTime");
                    int severity = object.getInt("severity");
                    String comment_code = object.getString("comment_code");
                    String streamFiles = object.getString("streamFiles");
                    String pic = object.getString("pic");

                    listItem.put("pic", pic);
                    listItem.put("title", title);
                    listItem.put("startTime", RequestTime.stampToMinute(startTime));

//                    //添加属性文件comment.properties
//                    listItem.put("comment_code",Comment(comment_code));
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
                    videolist.add(streamFiles);
                }
                return listItems;

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return listItems;
    }

///*
//* 创建本地属性文件，获取对应的建议提醒
//* */
//    private String Comment(String comcode){
//        String comment = null;
//        Properties com = new Properties();
//        try {
//            System.out.println("===========读取comment==============");
//            InputStream in = new BufferedInputStream(new FileInputStream("comment.properties"));
//            com.load(in);
//            comment = com.getProperty(comcode);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return comment;
//    }
    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}



