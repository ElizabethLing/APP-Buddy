package com.buddy.mains;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.buddy.R;
import com.buddy.connectToServer.DyChecksum;
import com.buddy.connectToServer.MinaClientHandler;
import com.buddy.connectToServer.SendMsgToServer;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.HashMap;
import java.util.Map;

/**
 * Created on 2018/5/22.14:12.
 *
 * @author Songling
 * @version 1.0.0
 */

public class UserRegistermobile extends Activity {
    private EditText etMobile;
    private Spinner numberCode;
    public static String phoneNumber;
    public static String noCode;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_register1);

        numberCode = (Spinner)findViewById(R.id.spinner);
        etMobile = (EditText)findViewById(R.id.mobile);
        Button bnReg = (Button)findViewById(R.id.bnReg1);
        bnReg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                noCode = numberCode.getSelectedItem().toString();
                phoneNumber = etMobile.getText().toString();
                if(IsMoblie(phoneNumber)){
                    //获取动态验证码
                    SendMsgToServer getCode = new SendMsgToServer(61,RegToJson());
                    getCode.start();

                    int count = 0;
                    while(MinaClientHandler.dcsResult == 1){
                        try{
                            Thread.sleep(300);
                            count++;
                            if (MinaClientHandler.dcsResult == 0){
                                startActivity(new Intent(getApplication(),UserRegDyn.class));
                                Toast.makeText(UserRegistermobile.this,"验证码已发送",Toast.LENGTH_SHORT).show();
                                break;
                            }
                            if (count == 10){
                                MinaClientHandler.dcsResult = 1;
                                Toast.makeText(UserRegistermobile.this,"请求超时，请重试",Toast.LENGTH_SHORT).show();
                                break;
                            }
                        }catch(Exception e){
                            e.printStackTrace();
                        }
                    }
//                    try {
//                        Thread.sleep(1000);
//                    } catch (InterruptedException e) {
//                        e.printStackTrace();
//                    }
//                    System.out.println("MinaClientHandler.dcsResult = " + MinaClientHandler.dcsResult);
//                    if (MinaClientHandler.dcsResult == 0) {
//                        startActivity(new Intent(getApplication(),UserRegDyn.class));
//                        Toast.makeText(getApplicationContext(),"验证码已发送",Toast.LENGTH_LONG).show();
//                    }else{
//                        Toast.makeText(getApplicationContext(),"获取验证码失败，请重新获取",Toast.LENGTH_LONG).show();
//                    }
                }else{
                    Toast.makeText(UserRegistermobile.this,"请确认手机号是否正确",Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private boolean IsMoblie(String phoneNumber) {
        String telRegex = "^((13[0-9])|(14[5,7,9])|(15[^4])|(18[0-9])|(17[0,1,3,5,6,7,8]))\\d{8}$";
        // "[1]"代表第1位为数字1，"[358]"代表第二位可以为3、5、8中的一个，"\\d{8}"代表后面是可以是0～9的数字，有9位。
        return (!TextUtils.isEmpty(phoneNumber)) && phoneNumber.matches(telRegex);
    }
    private String RegToJson() {
        Gson gson = new GsonBuilder().enableComplexMapKeySerialization().create();
        Map<String,String> regJson = new HashMap<String,String>();
        regJson.put("phoneNumber",phoneNumber);
        return gson.toJson(regJson);
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
