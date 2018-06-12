package com.buddy.mains;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.widget.Toast;

import com.buddy.R;
import com.buddy.connectToServer.DyChecksum;
import com.buddy.connectToServer.MinaClientHandler;
import com.buddy.manager.UserManager;

/**
 * Created on 2018/5/27.10:59.
 *
 * @author Songling
 * @version 1.0.0
 */

public class SplashActivity extends Activity{

        private static final int GO_HOME = 0;//去主页
        private static final int GO_LOGIN = 1;//去登录页
        /**
         * 跳转判断
         */
        private Handler mHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case GO_HOME://去主页
                        Intent intent = new Intent(SplashActivity.this, KidsList.class);
                        startActivity(intent);
                        break;
                    case GO_LOGIN://去登录页
                        Intent intent2 = new Intent(SplashActivity.this, UserLogOn.class);
                        startActivity(intent2);
                        break;
                }
                finish();
            }
        };

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_splash);
            //自动登录判断，SharePrefences中有数据，则跳转到主页，没数据则跳转到登录页
            if (UserManager.getInstance().hasUserInfo(this)){
                LogOn();
            } else {
                mHandler.sendEmptyMessageAtTime(GO_LOGIN, 2000);
            }
        }

    private void LogOn() {
        //动态登录
        String account = UserManager.getInstance().getUserInfo(this).getUserName();
        String password = UserManager.getInstance().getUserInfo(this).getPassword();
        UserLogOn.userMobile = account;
        UserLogOn.userPwd = password;
        System.out.println("account = " + account+" ============= password = " + password);
        DyChecksum dyl = new DyChecksum(60, account,password,account);
        dyl.start();
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        switch (MinaClientHandler.ulResult) {
            case 0:
                mHandler.sendEmptyMessageDelayed(GO_HOME, 2000);
                //Toast.makeText(getApplication(), "登录成功", Toast.LENGTH_SHORT).show();
                break;
            default:
                Toast.makeText(SplashActivity.this, "账号过期，请重新登录/获取", Toast.LENGTH_SHORT).show();
                mHandler.sendEmptyMessageAtTime(GO_LOGIN, 2000);
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
