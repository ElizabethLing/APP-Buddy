package com.buddy.utils;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.WeakHashMap;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.widget.ImageView;

import com.buddy.R;

/**
 * Created on 2017/12/17/15:48.
 *
 * @author Songling
 * @version V 1.0.0
 */
public class ImageLoader {
// store image in memory
// thread safe map
    Map<String, Bitmap> imgCache = Collections.synchronizedMap(new HashMap<String, Bitmap>());
// not thread safe, may cause loop when rehash
//HashMap<String, Bitmap> imgCache = new HashMap<String, Bitmap>();
private Map<ImageView, String> imageViews=Collections.synchronizedMap(
        new WeakHashMap<ImageView, String>());
        Handler handler;

public ImageLoader() {
        //imgCache = new HashMap<String, Bitmap>();
        handler = new Handler();
        }

final int stub_id = R.drawable.ic_launcher;

// still in UI thread, so i can control imageView directly
public void displayImage(String url, ImageView imageView){
        // update imageViews
        imageViews.put(imageView, url);
        Bitmap map = imgCache.get(url);
        if(map != null){    // all ready download in memory
        //still in UI thread, so i can control imageView directly
        imageView.setImageBitmap(map);
        return;
        }

        // create a thread to download image
        // a good idea is use thread pool
        // but for learning, i just use thread
        createThreadAndDownloadImage(url, imageView);
        imageView.setImageResource(stub_id);
        }

public void createThreadAndDownloadImage(String url, ImageView imageView){
        // thread not synchrony
        // my two thread use the same url
        // so there will be two same image in the imgCache
        Thread th = new DownloadImageThread(url, imageView);
        th.start();
        }


// just download image from internet
private Bitmap getBitmap(String url){
        Bitmap bitmap=null;
        URL imageUrl;
        try {
        imageUrl = new URL(url);

        HttpURLConnection conn = (HttpURLConnection)imageUrl.openConnection();
        conn.setConnectTimeout(30000);
        conn.setReadTimeout(30000);
        conn.setInstanceFollowRedirects(true);
        InputStream is=conn.getInputStream();
        bitmap = BitmapFactory.decodeStream(is);
        is.close();
        return bitmap;
        } catch (MalformedURLException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
        return null;
        } catch (IOException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
        return null;
        }

        }

class DownloadImageThread extends Thread{
    String url;
    ImageView img;
    public DownloadImageThread(String u, ImageView imageView){
        url = u;
        img = imageView;
    }

    public void run(){
        Bitmap bmp = getBitmap(url);
        imgCache.put(url, bmp);
        // after image download there are two methods to communicate with ui thread
        // one is use handler
        // the other is use Activity.runOnUiThread
        // at this time, I decide to use the second method.
            /*
            Activity a = (Activity) img.getContext();
            // run on UI thread
            a.runOnUiThread(new Runnable(){

                @Override
                public void run() {
                    // TODO Auto-generated method stub
                    // 又要把bmp变成final，算了我还是用Handler吧
                    img.setImageBitmap(bmp);
                }

            });
            */

        handler.post(new SetImageViewRunnable(img, bmp, url));

    }
}

    boolean imageViewReused(String url, ImageView img){
        String tag = imageViews.get(img);
        // 被后来的线程更新了
        if(tag == null || !tag.equals(url))
            return true; //
        return false;
    }

class SetImageViewRunnable implements Runnable {

    private ImageView image;
    private Bitmap bmp;
    private String url;

    public SetImageViewRunnable(ImageView img, Bitmap bp, String u) {
        image = img;
        bmp = bp;
        url = u;
    }

    @Override
    public void run() {
        // TODO Auto-generated method stub
        // reuse return
        if (imageViewReused(url, image))
            return; //当前的线程已经过时了，不必设置图片了
        if (bmp != null)
            image.setImageBitmap(bmp);
        else
            image.setImageResource(stub_id);//网络出错
    }
}
}
