package com.buddy.mains;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.buddy.R;
import com.buddy.connectToServer.DyChecksum;
import com.buddy.connectToServer.MinaClientHandler;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created on 2018/4/3.15:52.
 *
 * @author Songling
 * @version 1.0.0
 */

public class UserRegister extends Activity {
    private EditText etUserName,etPwd,etPwd2;
    private TextView note2;
    private Button btnRegister;
    private String UserName,UserPwd,UserPwd2;
    private String code,phoneNumber;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_register);

        init();
        ClickListener();
    }

    private void init() {
        etUserName = (EditText)findViewById(R.id.user_name);
        etPwd = (EditText)findViewById(R.id.password);
        etPwd2 = (EditText)findViewById(R.id.password0);
        btnRegister = (Button)findViewById(R.id.reg);
        code = MinaClientHandler.code;
        phoneNumber = UserRegistermobile.phoneNumber;
        note2 = (TextView)findViewById(R.id.note2);
        note2.setText("您注册的手机号为 "+ UserRegistermobile.phoneNumber);
    }

    private void ClickListener() {
        btnRegister.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                UserName = etUserName.getText().toString();
                UserPwd = etPwd.getText().toString();
                UserPwd2 = etPwd2.getText().toString();
           if (UserName.equals("") || UserName.length()>20) {
               System.out.println("etUserName = " + etUserName.getText().toString());
                Toast.makeText(UserRegister.this, "请输入合法的用户名", Toast.LENGTH_SHORT).show();
                }else if (!ValidatePassword(UserPwd).equals("符合要求")) {
                    Toast.makeText(UserRegister.this, ValidatePassword(UserPwd), Toast.LENGTH_SHORT).show();
                    }else if (!IsSame(UserPwd, UserPwd2)) {
                        Toast.makeText(UserRegister.this, "请确认两次密码是否相同", Toast.LENGTH_SHORT).show();
                        UserPwd = UserPwd2 = "";
                            } else {
                                // Send messages to remote main control server,bts = 50,注册
                                DyChecksum  DynReg = new DyChecksum(50,phoneNumber,code,UserName,UserPwd);
                                DynReg.start();
                               int count = 0;
                               while(!(MinaClientHandler.rurResult == 0)){
                                   try{
                                       Thread.sleep(300);
                                       count++;
                                       if (count == 10){
                                           MinaClientHandler.rurResult = 1;
                                           Toast.makeText(UserRegister.this,"注册失败，请重新注册",Toast.LENGTH_LONG).show();
                                           break;
                                       }
                                   }catch(Exception e){
                                       e.printStackTrace();
                                   }
                               }
                               if (MinaClientHandler.rurResult == 0){
                                   startActivity(new Intent(UserRegister.this, UserLogOn.class));
                                   Toast.makeText(UserRegister.this,"恭喜！注册成功，请登录",Toast.LENGTH_SHORT).show();
                               }
//
//                                try {
//                                    Thread.sleep(1000);
//                                    if(MinaClientHandler.rurResult == 0){
//                                        Toast.makeText(getApplication(), "恭喜！注册成功，请登录", Toast.LENGTH_SHORT).show();
//                                        startActivity(new Intent(UserRegister.this, UserLogOn.class));
//                                    }else{
//                                        Toast.makeText(getApplication(), "注册失败，请重新注册", Toast.LENGTH_SHORT).show();
//                                    }
//                                } catch (InterruptedException e) {
//                                    e.printStackTrace();
//                                }
                            }
            }
        });
    }

    private boolean IsSame(String pwd,String pwd2){
        return pwd.equals(pwd2);
    }
    private String ValidatePassword(String str) {
        String regEx="[`~!@#$%^&*()+=|{}':;',\\[\\].<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。，、？]{6,16}$";
        //①构造一个模式.
        Pattern p=Pattern.compile(regEx);
        Pattern pA=Pattern.compile("^[A-Z]{6,16}$");
        Pattern pa=Pattern.compile("^[a-z]{6,16}$");
        Pattern pNum=Pattern.compile("^[0-9]{6,16}$");

        //②建造一个匹配器
        Matcher m = p.matcher(str);
        Matcher m1 = pA.matcher(str);
        Matcher m2 = pa.matcher(str);
        Matcher m3 = pNum.matcher(str);

        //大小写字母，数字，特殊字符 全集匹配（只要字符串匹配其中任何一个或多个都可以）
        String reg="([A-Z]|[a-z]|[0-9]|[`~!@#$%^&*()+=|{}':;',\\[\\].<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。，、？]){6,16}$";
        Pattern pAll=Pattern.compile(reg);
        Matcher mAll = pAll.matcher(str);
        //③进行判断，得到结果
        String strReurn = null;
        //因为字符串str如果匹配一个就不可能匹配其他的，具有互异性。还要排除都不匹配的情况，不满足这四项的字符
        if(m.matches()||m1.matches()||m2.matches()||m3.matches()){
            strReurn = "密码中数字、字母、字符至少包含两种";
        }else if(mAll.matches()){
            strReurn = "符合要求";
        }else{
            strReurn = "密码不符合要求";
        }
        return strReurn;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
