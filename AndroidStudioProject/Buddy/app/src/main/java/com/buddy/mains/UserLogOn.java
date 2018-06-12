package com.buddy.mains;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.buddy.R;
import com.buddy.connectToServer.DyChecksum;
import com.buddy.connectToServer.MinaClientHandler;
import com.buddy.manager.UserManager;

/**
 * Created on 2017/11/28.
 *  1、登录请求
 *  2、考虑Buddy运行时即获取最新消息，于是添加notification请求
 *  3、登录
 *  4、点击测试跳转主页面
 * @author Songling
 * @version V 1.0.0
 */

public class UserLogOn extends Activity{
    private EditText etUserMobile, etUserPwd;
    public static String userMobile;
    public static String userPwd;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_log_on);
        init();
    }

    private void init() {
        // 获取界面中两个编辑框按钮
        etUserMobile = (EditText) findViewById(R.id.userMobile);
        etUserPwd = (EditText) findViewById(R.id.userPwd);
        etUserMobile.setOnEditorActionListener(mEditorAction);
        etUserPwd.setOnEditorActionListener(mEditorAction);

        findViewById(R.id.bnRegister).setOnClickListener(mOnClickListerner);
        findViewById(R.id.bnLogin).setOnClickListener(mOnClickListerner);
        findViewById(R.id.bnFroget).setOnClickListener(mOnClickListerner);
    }

    private View.OnClickListener mOnClickListerner = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.bnLogin:
                    info();
                    break;
                case R.id.bnFroget:
                    System.out.println(" =================bnForget================= ");
                    //startActivity(new Intent(UserLogOn.this, UserForgetPwd.class));
                    break;
                case R.id.bnRegister:
                    startActivity(new Intent(UserLogOn.this, UserRegistermobile.class));
                    break;
                default:
                    break;
            }
        }
    };
    private TextView.OnEditorActionListener mEditorAction = new TextView.OnEditorActionListener() {
        @Override
        public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {
            if (actionId == EditorInfo.IME_ACTION_NEXT){
                etUserPwd.requestFocus();
                Log.i("---","下一步");
            }else if (actionId == EditorInfo.IME_ACTION_DONE){
                //隐藏软键盘
                InputMethodManager imm =(InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                assert imm != null;
                imm.hideSoftInputFromWindow(textView.getWindowToken(), 0);
            }
            return false;
        }
    };

    private void info() {
        userMobile = etUserMobile.getText().toString();
        userPwd = etUserPwd.getText().toString();
        if (userMobile.equals("")) {
            Toast.makeText(UserLogOn.this, "请输入用户名", Toast.LENGTH_LONG).show();
        } else if (userPwd.equals("")) {
            Toast.makeText(UserLogOn.this, "请输入密码", Toast.LENGTH_SHORT).show();
        }else {
            LogOn();
        }
    }

    private void LogOn() {
        //动态登录
        DyChecksum dyl = new DyChecksum(60, userMobile,userPwd,userMobile);
        dyl.start();
        int count = 0;
        while(!(MinaClientHandler.ulResult == 0)){
            try{
                Thread.sleep(300);
                count++;
                if (count == 10){
                    MinaClientHandler.ulResult = 1;
                    Toast.makeText(UserLogOn.this, "登录失败，请重新登录/获取", Toast.LENGTH_SHORT).show();
                    break;
                }
            }catch(Exception e){
                e.printStackTrace();
            }
        }
        if (MinaClientHandler.ulResult == 0){
            //Toast.makeText(this, "登录成功", Toast.LENGTH_SHORT).show();
            try {
                Thread.sleep(700);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            startActivity(new Intent(UserLogOn.this, KidsList.class));
            UserManager.getInstance().saveUserInfo(getApplication(), userMobile, userPwd);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
