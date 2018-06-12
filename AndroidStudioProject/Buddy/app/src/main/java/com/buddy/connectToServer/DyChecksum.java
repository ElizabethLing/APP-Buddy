package com.buddy.connectToServer;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.apache.mina.core.RuntimeIoException;
import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.future.ConnectFuture;
import org.apache.mina.core.service.IoConnector;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.transport.socket.nio.NioSocketConnector;

import java.net.InetSocketAddress;
import java.util.HashMap;
import java.util.Map;
import java.util.zip.CRC32;
import java.util.zip.Checksum;

/**
 * Created on 2018/4/12.20:49.
 *
 * @author Songling
 * @version 1.0.0
 */

public class DyChecksum extends Thread {
    private static final int svrPort = 9123;
    private int reqCode;
    private String arg0,arg1,arg2,arg3;
    private Integer arg4;
    private Checksum checksum;

    public DyChecksum(int bts8, String arg0,String arg1,String arg2) {
        this.reqCode = bts8;
        this.arg0 = arg0;
        this.arg1 = arg1;
        this.arg2 = arg2;
    }
    public DyChecksum(int bts8, String arg0, String arg1,String arg2,String arg3) {
        this.reqCode = bts8;
        this.arg0 = arg0;
        this.arg1 = arg1;
        this.arg2 = arg2;
        this.arg3 = arg3;
    }
    public DyChecksum(int bts8, String arg0, String arg1,String arg2,String arg3,Integer arg4) {
        this.reqCode = bts8;
        this.arg0 = arg0;
        this.arg1 = arg1;
        this.arg2 = arg2;
        this.arg3 = arg3;
        this.arg4 = arg4;
    }

    public void run(){
        //Log.i(TAG, "Start Thread...");
        IoConnector connector = new NioSocketConnector();
        connector.setConnectTimeoutMillis(30000);
        connector.setHandler(new MinaClientHandler());
        IoSession session;
        while (true) {
            try {
                // Update the remote server IP if needed
                ConnectFuture connFuture = connector.connect(new InetSocketAddress("211.149.181.66", svrPort));
                // ConnectFuture connFuture = connector.connect(new InetSocketAddress("10.0.2.15", svrPort));
                connFuture.awaitUninterruptibly();
                session = connFuture.getSession();
                System.out.printf("APP: TCP Started, connecting remote port %d%n", svrPort);
                break;
            } catch (RuntimeIoException e) {
                System.out.println("Cannot connect to the remote server, try 5s later ...");
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e1) {
                    e1.printStackTrace();
                }
            }
        }

        String s_tmp;
        byte[] b_tmp;
        byte[] bts;
        int currentTimeStamp;
        IoBuffer obuf;

        // Start UserLogon Requests - Table User
        byte[] target = new byte[64];
        byte[] tmp = new byte[64];

        for (byte i = 0; i < 64; i++) {
            target[i] = 0;
            tmp[i] = 0;
        }

        if (arg0.length() > 20) {
            arg0 = arg0.substring(0, 20);
        }
        //tmp = Bytes.toBytes(arg0);
        tmp = arg0.getBytes();

        for (byte i = 0; i < arg0.length(); i++) {
            target[i] = tmp[i];
            tmp[i] = 0;
        }

        if (arg1.length() > 20) {
            arg1 = arg1.substring(0, 20);
        }
        //tmp = Bytes.toBytes(strCode);
        tmp = arg1.getBytes();
        for (int i = 0; i < arg1.length(); i++) {
            target[20 + i] = tmp[i];
            tmp[i] = 0;
        }

        currentTimeStamp = (int) (System.currentTimeMillis() & 0xFFFFFFFFL);
        tmp[0] = (byte) ((currentTimeStamp & 0xFF000000) >> 24);
        tmp[1] = (byte) ((currentTimeStamp & 0x00FF0000) >> 16);
        tmp[2] = (byte) ((currentTimeStamp & 0x0000FF00) >> 8);
        tmp[3] = (byte) ((currentTimeStamp & 0x000000FF));

        for (int i = 0; i < 4; i++) {
            target[40 + i] = tmp[i];
            tmp[i] = 0;
        }

        checksum = new CRC32();
        checksum.update(target, 0, 64);

        System.out.printf("\n");

        s_tmp = MapToJson();
        b_tmp = s_tmp.getBytes();
        bts = new byte[16 + s_tmp.length()];
        bts[0] = 0;
        bts[1] = 1;
        bts[2] = 2;
        bts[3] = 3;
        bts[4] = 4;
        bts[5] = 5;
        bts[6] = 6;
        bts[7] = 7;
        bts[8] = (byte) reqCode;
        bts[9] = (byte) ((s_tmp.length() >> 8) & 0xFF);
        bts[10] = (byte) (s_tmp.length() & 0xFF);
        bts[11] = 0;

        bts[12] = (byte) ((currentTimeStamp & 0xFF000000) >> 24);
        bts[13] = (byte) ((currentTimeStamp & 0x00FF0000) >> 16);
        bts[14] = (byte) ((currentTimeStamp & 0x0000FF00) >> 8);
        bts[15] = (byte) ((currentTimeStamp & 0x000000FF));

        for (int i = 0; i < s_tmp.length(); i++) {
            bts[16 + i] = b_tmp[i];
        }

        obuf = IoBuffer.allocate(16);
        obuf.setAutoExpand(true);
        obuf.setAutoShrink(true);
        obuf.put(bts);
        obuf.flip();
        session.write(obuf);
        System.out.printf("DyChecksum :%n  %s%n", s_tmp);
        try {
            Thread.sleep(4000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        connector.dispose(true);
    }
    private String MapToJson(){
        Gson gson = new GsonBuilder().enableComplexMapKeySerialization().create();
        Map<Object,Object> json = new HashMap<Object,Object>();
        switch (reqCode){
            case 20:
//                //提交注册用户信息
//                //Send message to MainControl: {"name":"ling","password":"ling000","phoneNumber":"13939067847","checksum":3605340477}
//                //checksum = phoneNumber + DynCode + timestamp
//                //MinaClient Receiver case=205
//                json.put("phoneNumber",arg0);
//                json.put("name",arg2);
//                json.put("password",arg3);
//                json.put("checksum",String.valueOf(checksum.getValue()));
            case 30:
                //添加新的婴儿信息
                //Send message to MainControl: {"name":"ling","password":"ling000","phoneNumber":"13939067847","checksum":3605340477}
                //checksum = phoneNumber + DynCode + timestamp
                //MinaClient Receiver case=205
                json.put("phoneNumber",arg0);
                json.put("name",arg2);
                json.put("password",arg3);
                json.put("checksum",String.valueOf(checksum.getValue()));
            case 50:
                //提交注册用户信息
                //Send message to MainControl: {"name":"ling","password":"ling000","phoneNumber":"13939067847","checksum":3605340477}
                //checksum = phoneNumber + DynCode + timestamp
                //MinaClient Receiver case=205
                json.put("phoneNumber",arg0);
                json.put("name",arg2);
                json.put("password",arg3);
                json.put("checksum",String.valueOf(checksum.getValue()));
            case 60:
                //登录
                //Send message to MainControl: {"name":"ling","checksum":3605340477}
                //checksum = name + password + timestamp
                //MinaClient Receiver case=195
                json.put("name",arg0);
                json.put("checksum",String.valueOf(checksum.getValue()));
                break;
            case 61:
                //输入手机号获取验证码
                //Send message to MainControl:   {"phoneNumber":"13939067847"}
                //MinaClient Receiver case=194
                json.put("phoneNumber",arg2);
            case 70:
                //绑定婴儿
                //Send message to MainControl: {"uid":00000000000000000000000874184102,"checksum":3313643827,"cidOper":1,"cid":00000000000000000000002914869345}
                //checksum = phoneNumber/name + password + timestamp
                //MinaClient Receiver case = 215
                json.put("uid",arg2);
                json.put("cid",arg3);
                json.put("cidOper",arg4);
                json.put("checksum",String.valueOf(checksum.getValue()));
        }
        return gson.toJson(json);
    }
}

