package com.buddy.mains;

import android.app.Activity;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.buddy.R;
import com.buddy.utils.HttpDownloader;

import java.io.File;
import java.util.ArrayList;

import io.vov.vitamio.MediaPlayer;
import io.vov.vitamio.widget.MediaController;
import io.vov.vitamio.widget.VideoView;

public class VideoPlayer extends Activity {
    private static ArrayList<String> list = new ArrayList<>();
    private VideoView videoView;
    private MediaController mMediaController;
    private int videoIndex = 0;
    private String[] surl;
    private FrameLayout frame;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.play_url_layout);
        frame = (FrameLayout)findViewById(R.id.framelayout) ;
        videoView = (VideoView) findViewById(R.id.url_videoView);
        Button button = (Button) findViewById(R.id.play);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (FragmentVideo.PosUrl == null) {
                    surl = FragDataAlarm.PosUrl.split(",");
                }else {
                    surl = FragmentVideo.PosUrl.split(",");
                }

                new downloadThread().start();
                play();
            }
        });
    }

    private void playNextVideo() {
        if (videoIndex < surl.length - 1) {
            videoIndex = videoIndex + 1;
            play();
        } else {
            surl = null;
            videoIndex = 0;
        }
    }

    private void play() {
        String filename = null;
        if (surl[videoIndex] == null) {
            Toast.makeText(getApplicationContext(),"视频播放结束",Toast.LENGTH_SHORT).show();
        }
        filename = getFileName(surl[videoIndex]);
        String path = Environment.getExternalStorageDirectory()+ File.separator+"AppBuddy/" + filename;
        System.out.println("path = " + path);
        if (!path.isEmpty())videoView.setVideoPath(path);
        videoView.start();
        MediaController mediaController = new MediaController(this,true,frame);
        videoView.setMediaController(mediaController);                 //将控制条附在Videoview中
        mediaController.setVisibility(View.GONE);
        videoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                playNextVideo();
            }
        });
    }

    public static String getFileName(String oldFileName) {
    String[] filenames = oldFileName.split("_");
    return filenames[filenames.length - 1];
}
    private class downloadThread extends Thread {
        public void run() {
            HttpDownloader httpDownloader = new HttpDownloader();
            String filename = null;
            for (int i = 0; i < surl.length; i++) {
                filename = getFileName(surl[i]);
                int downloadResult = httpDownloader.downloadFiles(surl[i],"AppBuddy", filename);
                System.out.println(filename+" 的下载结果：" + downloadResult);
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        FragmentVideo.PosUrl = null;
        FragDataAlarm.PosUrl = null;
    }
}

