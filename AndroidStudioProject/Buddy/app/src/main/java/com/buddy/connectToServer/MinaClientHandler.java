package com.buddy.connectToServer;

import android.util.Log;
import android.widget.Toast;

import com.buddy.connectToServer.ServerResponse.AddChildResponse;
import com.buddy.connectToServer.ServerResponse.ChildInfoResponse;
import com.buddy.connectToServer.ServerResponse.DynamicCodeSimResponse;
import com.buddy.connectToServer.ServerResponse.EmotionReptResponse;
import com.buddy.connectToServer.ServerResponse.KidInfo;
import com.buddy.connectToServer.ServerResponse.MoodReptResponse;
import com.buddy.connectToServer.ServerResponse.NotificationResponse;
import com.buddy.connectToServer.ServerResponse.RawEmotionReptResponse;
import com.buddy.connectToServer.ServerResponse.RawHealthReptResponse;
import com.buddy.connectToServer.ServerResponse.RawMoodReptResponse;
import com.buddy.connectToServer.ServerResponse.RawSleepReptResponse;
import com.buddy.connectToServer.ServerResponse.RegisterUserResponse;
import com.buddy.connectToServer.ServerResponse.SleepReptResponse;
import com.buddy.connectToServer.ServerResponse.UpdateChildResponse;
import com.buddy.connectToServer.ServerResponse.UserInfoResponse;
import com.buddy.connectToServer.ServerResponse.UserLogonResponse;
import com.buddy.connectToServer.ServerResponse.UserUpdateResponse;
import com.google.gson.Gson;
import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.core.session.IoSession;

import java.util.List;

public class MinaClientHandler extends IoHandlerAdapter {
    private static final String TAG = "MinaClientHandler";
    private static final byte messageHeadSize = 16; // 128 bits
    public static String notify;
    public static String SleepDay;
    public static String SleepWeek;
    public static String SleepMonth;
    public static String MoodDay;
    public static String MoodWeek;
    public static String MoodMonth;
    public static String EmotionDay;
    public static String EmotionWeek;
    public static String EmotionMonth;
    public static String Health;
    public static String CId;
    public static String UId;
    public static String code;
    public static String sKids;
    public static String kids;
    public static String camId;
    private static byte[] lateBuf = new byte[3276800];
    private static int lbLimit;
    public static int uuResult=1,dcsResult=1,ulResult=1,rurResult=1,ucrResult=1,acrResult=1,uirResult=1,cirResult=1;
    public static int rhrResult=1,erResult=1,rerResult=1,mrResult=1,rmrResult=1,srResult=1,rsrResult=1,nrResult=1;

    @Override
    public void exceptionCaught(IoSession session, Throwable cause)
            throws Exception {
        Log.i(TAG,"APP Abnormal");
        super.exceptionCaught(session, cause);
    }
    @Override
    public void messageSent(IoSession iosession, Object obj) throws Exception {
        Log.i(TAG,"APP Message Sent");
        super.messageSent(iosession, obj);
    }
    @Override
    public void sessionClosed(IoSession iosession) throws Exception {
        Log.i(TAG,"APP Session Closed");
        super.sessionClosed(iosession);
    }
    @Override
    public void sessionCreated(IoSession iosession) throws Exception {
        Log.i(TAG,"APP Session Created");
        super.sessionCreated(iosession);
    }
    @Override
    public void sessionIdle(IoSession iosession, IdleStatus idlestatus)
            throws Exception {
        Log.i(TAG,"APP Session Idle");
        super.sessionIdle(iosession, idlestatus);
    }
    @Override
    public void sessionOpened(IoSession iosession) throws Exception {
        Log.i(TAG,"APP Session Open");
        super.sessionOpened(iosession);
    }
    @Override
    public void messageReceived(IoSession iosession, Object fragment)
            throws Exception {
        Gson gson = new Gson();
        // Fragment Buffer
        IoBuffer fbuf;
        int messageSize, fragmentSize, expMsgLen;
        // Message Bytes and Fragment Bytes
        byte[] nbyte, fbyte;

        fbuf = (IoBuffer) fragment;
        fragmentSize = fbuf.limit();

        // If the lateDec flag is set, get the previous fragment from the lateBuf
        if ( iosession.containsAttribute("lateDec") ) {
            messageSize = lbLimit+fragmentSize;
        }
        else {
            messageSize = fragmentSize;
        }
        nbyte = new byte[messageSize + 3276800];
        fbyte = new byte[fragmentSize];
        // Move the fragment to fbyte[]
        fbuf.get(fbyte, fbuf.position(), fragmentSize);
        // Copy 'late' buffer (old fragment) to nbyte[0] for lbLimit bytes
        System.arraycopy(lateBuf,0,nbyte,0,lbLimit);
        // Copy fbyte (new fragment) to nbyte[lbLimit] for fragmentSize bytes
        System.arraycopy(fbyte,0,nbyte,lbLimit,fragmentSize);

        if ( iosession.containsAttribute("lateDec") ) {
            lbLimit = 0;
            iosession.removeAttribute("lateDec");
        }
        System.out.printf("APP: Received Message: %d Bytes%n", messageSize);

        int i=9;
        expMsgLen = ((nbyte[i]+256)%256)*256+((nbyte[i+1]+256)%256);
//        System.out.printf("  %02x%02x: %d%n", nbyte[i], nbyte[i+1], expMsgLen);

        // More bytes are not received yet
        if ( expMsgLen > (messageSize-16) ) {
            iosession.setAttribute("lateDec");
            System.arraycopy(nbyte,0,lateBuf,0,messageSize);
            lbLimit = messageSize;
            return;
        }
        i = 16;
        byte[] tmp = new byte[messageSize - messageHeadSize];
        for ( int j = 0; i < messageSize; i++,j++ ) {
            tmp[j] = nbyte[i];
        }
        String ibody = new String(tmp);
        int tmpb = nbyte[8] + 256;
        switch (tmpb) {
            case 185: // UserUpdateRequest
                UserUpdateResponse uuBody = gson.fromJson(ibody, UserUpdateResponse.class);
                uuResult = uuBody.result;
                System.out.printf("UserUpdateResponse JSON Parsing: result = %d%n",uuResult);
                break;
            case 194: // Response to DynamicCodeRequest
                DynamicCodeSimResponse dcsBody = gson.fromJson(ibody, DynamicCodeSimResponse.class);
                code = dcsBody.code;
                System.out.println("DynamicCodeSimResponse code = " + code);
                dcsResult = dcsBody.result;
                System.out.printf("DynamicCodeSimResponse JSON Parsing: result = %d%n",dcsResult);
                break;
            case 195: // UserLogonRequest
                UserLogonResponse ulBody = gson.fromJson(ibody, UserLogonResponse.class);
                sKids = ibody;
                UId = ulBody.uid;
                System.out.println("UserLogonResponse sKids = " + sKids);
                ulResult = ulBody.result;
                System.out.printf("UserLogonResponse JSON Parsing: result = %d%n", ulResult);
                break;
            case 205: // RegisterUserResponse
                RegisterUserResponse rurBody = gson.fromJson(ibody, RegisterUserResponse.class);
                UId = rurBody.uid;
                System.out.println("RegisterUserResponse UId = " + UId);
                rurResult = rurBody.result;
                System.out.printf("RegisterUserResponse JSON Parsing: result = %d%n",rurResult);
                break;
            case 215: // UpdateChildResponse
                UpdateChildResponse ucrBody = gson.fromJson(ibody, UpdateChildResponse.class);
                ucrResult = ucrBody.result;
                System.out.printf("UpdateChildResponse JSON Parsing: result = %d%n", ucrResult);
                break;
            case 225: // AddChildResponse
                AddChildResponse acrBody = gson.fromJson(ibody, AddChildResponse.class);
                CId = acrBody.cid;//Cid好像可以不用
                acrResult = acrBody.result;
                System.out.printf("AddChildResponse JSON Parsing: result = %d, cid = %s%n", acrResult, CId);
                break;
            case 234: // UserInfoRequest
                UserInfoResponse uirBody = gson.fromJson(ibody, UserInfoResponse.class);
                System.out.println("UserInfoResponse ibody = " + ibody);
                kids = ibody;
                uirResult = uirBody.result;
                System.out.printf("UserInfoResponse JSON Parsing: result = %d%n",uirResult);
                break;
            case 235: // ChildInfoRequest
                ChildInfoResponse cirBody = gson.fromJson(ibody, ChildInfoResponse.class);
                cirResult = cirBody.result;
                camId = cirBody.cameraId;
                System.out.printf("ChildInfoResponse JSON Parsing: result = %d%n", cirResult);
                System.out.println("ChildInfoResponse camId = " + camId);
                break;
            case 246:
                RawHealthReptResponse rhrBody = gson.fromJson(ibody, RawHealthReptResponse.class);
                rhrResult = rhrBody.result;
                System.out.printf("JSON Parsing: result = %d%n", rhrResult);
                Health = ibody;
                System.out.println("MinaHandler  RawHealthReptResponse = " + Health);
                break;
            case 247:
                EmotionReptResponse erResponse = gson.fromJson(ibody, EmotionReptResponse.class);
                erResult = erResponse.result;
                System.out.printf("JSON Parsing: result = %d%n",erResult);
                switch (erResponse.pd){
                    case 1:
                        EmotionDay = ibody;
                        System.out.println("MinaHandler EmotionDay = " + EmotionDay);
                        break;
                    case 2:
                        EmotionWeek = ibody;
                        System.out.println("MinaHandler EmotionWeek = " + EmotionWeek);
                        break;
                    case 3:
                        EmotionMonth = ibody;
                        System.out.println("MinaHandler EmotionMonth = " + EmotionMonth);
                        break;
                    default:
                        break;
                }
                break;
            case 248:
                RawEmotionReptResponse rerResponse = gson.fromJson(ibody, RawEmotionReptResponse.class);
                rerResult = rerResponse.result;
                System.out.printf("JSON Parsing: result = %d%n",rerResult);
                System.out.println("MinaHandler  RawEmotionReptResponse = " + rerResponse.emotionReptList);
                break;
            case 249:
                MoodReptResponse mrResponse = gson.fromJson(ibody, MoodReptResponse.class);
                mrResult = mrResponse.result;
                System.out.printf("%nJSON Parsing: result = %d%n",mrResult);
                switch (mrResponse.pd){
                    case 1:
                        MoodDay = ibody;
                        System.out.println("MinaHandler MoodDay = " + MoodDay);
                        break;
                    case 2:
                        MoodWeek = ibody;
                        System.out.println("MinaHandler MoodWeek = " + MoodWeek);
                        break;
                    case 3:
                        MoodMonth = ibody;
                        System.out.println("MinaHandler MoodMonth = " + MoodMonth);
                        break;
                    default:
                        break;
                }
                break;
            case 250:
                RawMoodReptResponse rmrResponse = gson.fromJson(ibody, RawMoodReptResponse.class);
                rmrResult = rmrResponse.result;
                System.out.printf("JSON Parsing: result = %d%n",rmrResult);
                System.out.println("MinaHandler  RawMoodResponse = " + rmrResponse.moods);
                break;
            case 251:
                SleepReptResponse srResponse = gson.fromJson(ibody, SleepReptResponse.class);
                srResult = srResponse.result;
                System.out.printf("JSON Parsing: result = %d%n",srResult);
                switch (srResponse.pd) {
                    case 1:
                        SleepDay = ibody;
                        System.out.println("MinaHandler SleepDay = " + SleepDay);
                        break;
                    case 2:
                        SleepWeek = ibody;
                        System.out.println("MinaHandler SleepWeek = " + SleepWeek);
                        break;
                    case 3:
                        SleepMonth = ibody;
                        System.out.println("MinaHandler SleepMonth = " + SleepMonth);
                        break;
                    default:
                        break;
                }
                break;

            case 252:
                RawSleepReptResponse rsrResponse = gson.fromJson(ibody, RawSleepReptResponse.class);
                rsrResult = rsrResponse.result;
                System.out.printf("JSON Parsing: result = %d%n", rsrResult);
                System.out.println("MinaHandler RawSleepResponse = " + rsrResponse.sleeps);
                break;

            case 253:
            case 254:
                NotificationResponse nrBody = gson.fromJson(ibody, NotificationResponse.class);
                nrResult = nrBody.result;
                System.out.printf("JSON Parsing: result = %d%n",nrResult);
                notify = ibody;
                System.out.println("MinaHandler notify = " + notify);
                break;
            default:
                System.out.println("Unsupported Response ibody = " + ibody);
                System.out.println("Unsupported Response");
                break;
        }

        System.out.printf("%n");
    }

}
