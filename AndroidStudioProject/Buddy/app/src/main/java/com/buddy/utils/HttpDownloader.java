package com.buddy.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created on 2017/11/15/16:15.
 *
 * @author Songling
 * @version V 1.0.0
 */

public class HttpDownloader {
    String line=null;
    StringBuffer strBuffer=new StringBuffer();
    BufferedReader bufferReader=null;

    //可以下载任意文件，例如MP3，并把文件存储在制定目录（-1：下载失败，0：下载成功，1：文件已存在）
    public int downloadFiles(String urlStr, String path, String fileName){
        try {
            FileUtils fileUtils=new FileUtils();
                if (fileUtils.isFileExist(fileName, path)) return 1;//判断文件是否存在
                else {
                    InputStream inputStream = getInputStreamFromUrl(urlStr);
                    File resultFile = fileUtils.write2SDFromInput(fileName, path, inputStream);
                    if (resultFile == null) return -1;
                }
        } catch (Exception e) {
            System.out.println("读写数据异常:"+e);
            return -1;
        }
        return 0;
    }

    public InputStream getInputStreamFromUrl(String urlStr)throws IOException {
        //创建一个URL对象
        URL url=new URL(urlStr);
        //创建一个HTTP链接
        HttpURLConnection urlConn=(HttpURLConnection)url.openConnection();
        //使用IO流获取数据
        InputStream inputStream=urlConn.getInputStream();
        return inputStream;
    }


}
