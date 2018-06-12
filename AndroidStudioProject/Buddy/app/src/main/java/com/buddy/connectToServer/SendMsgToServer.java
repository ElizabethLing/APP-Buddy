package com.buddy.connectToServer;

import android.util.Log;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.future.ConnectFuture;
import org.apache.mina.core.service.IoConnector;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.transport.socket.nio.NioSocketConnector;

import java.net.InetSocketAddress;

/**
 * Created on 2018/4/3.16:57.
 *
 * @author Songling
 * @version 1.0.0
 */

public class SendMsgToServer extends Thread{
    private static final String TAG = "Send message to Server";
    private static final int svrPort = 9123;
    private int type;
    private String str;
    private byte[] bts;

    public SendMsgToServer(int bts8, String strJson) {
        this.type = bts8;
        this.str = strJson;
    }

    @Override
    public void run() {
        Log.i(TAG, "Start Thread...");
        IoConnector connector = new NioSocketConnector();
        connector.setConnectTimeoutMillis(6000);
        connector.setHandler(new MinaClientHandler());
        IoSession session;

        //connected to mainContrl
        while(true) {
            try {
                // Update the remote server IP if needed
                ConnectFuture connFuture = connector.connect(new InetSocketAddress("211.149.181.66", svrPort));
                connFuture.awaitUninterruptibly();
                session = connFuture.getSession();
                break;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        // Send messages to remote main control server
        protocal();

        //write request
        IoBuffer obuf = IoBuffer.allocate(16);
        obuf.setAutoExpand(true);
        obuf.setAutoShrink(true);
        obuf.put(bts);
        obuf.flip();
        session.write(obuf);

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        connector.dispose(true);
        Log.i(TAG,"Client closed...");
        super.run();
    }

    private void protocal() {
        //s_tmp = "{\"name\":\"ViKey\",\"password\":\"yeKiV\"}";
        System.out.println("Send message to Server = " + str);
        byte[] b_tmp = str.getBytes();
        bts = new byte[16 + str.length()];
        bts[0] = 0;
        bts[1] = 1;
        bts[2] = 2;
        bts[3] = 3;
        bts[4] = 4;
        bts[5] = 5;
        bts[6] = 6;
        bts[7] = 7;
        bts[8] = (byte)type;
        bts[9] = (byte) ((str.length() >> 8) & 0xFF);
        bts[10] = (byte) (str.length() & 0xFF);
        bts[11] = 0;

        int currentTimeStamp = (int) (System.currentTimeMillis() & 0xFFFFFFFFL);
        bts[12] = (byte) ((currentTimeStamp & 0xFF000000) >> 24);
        bts[13] = (byte) ((currentTimeStamp & 0x00FF0000) >> 16);
        bts[14] = (byte) ((currentTimeStamp & 0x0000FF00) >> 8);
        bts[15] = (byte) ((currentTimeStamp & 0x000000FF));

        for (int i = 0; i < str.length(); i++) {
            bts[16 + i] = b_tmp[i];
        }
    }
}
