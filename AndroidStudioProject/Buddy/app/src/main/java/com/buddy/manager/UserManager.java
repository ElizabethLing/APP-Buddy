package com.buddy.manager;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

import com.buddy.utils.UserInfo;

/**
 * Created on 2018/5/27.10:54.
 * 保存用户信息的管理类
 * @author Songling
 * @version 1.0.0
 */

public class UserManager {

    private static UserManager instance;

    private UserManager() {
    }

    public static UserManager getInstance() {
        if (instance == null) {
            instance = new UserManager();
        }
        return instance;
    }

    /**
     * 保存自动登录的用户信息
     */
    public void saveUserInfo(Context context, String username, String password) {
        SharedPreferences sp = context.getSharedPreferences("userInfo", Context.MODE_PRIVATE);//Context.MODE_PRIVATE表示SharePrefences的数据只有自己应用程序能访问。
        SharedPreferences.Editor editor = sp.edit();
//        if (IsMoblie(username)){
//            editor.putString("phoneNumber",username);
//        }else {
//            editor.putString("ACCOUNT", username);
//        }
        editor.putString("ACCOUNT", username);
        editor.putString("PASSWORD", password);
        editor.apply();
    }

    /**
     * 获取用户信息model
     * @param context
     * @return
     */
    public UserInfo getUserInfo(Context context) {
        SharedPreferences sp = context.getSharedPreferences("userInfo", Context.MODE_PRIVATE);
        UserInfo userInfo = new UserInfo();
//        if (sp.getString("phoneNumber","").equals("")) {
//            userInfo.setUserName(sp.getString("ACCOUNT", ""));
//            System.out.println("sp.getString(ACCOUNT) ====11=== " + sp.getString("ACCOUNT", ""));
//        }else {
//            userInfo.setUserName(sp.getString("phoneNumber", ""));
//        }
        userInfo.setUserName(sp.getString("ACCOUNT", ""));
        userInfo.setPassword(sp.getString("PASSWORD", ""));
        return userInfo;
    }

    /**
     * userInfo中是否有数据
     */
    public boolean hasUserInfo(Context context) {
        UserInfo userInfo = getUserInfo(context);
        //有数据
        return (userInfo != null) && (!TextUtils.isEmpty(userInfo.getUserName())) && (!TextUtils.isEmpty(userInfo.getPassword()));
    }

    private boolean IsMoblie(String phoneNumber) {
        String telRegex = "^((13[0-9])|(14[5,7,9])|(15[^4])|(18[0-9])|(17[0,1,3,5,6,7,8]))\\d{8}$";
        // "[1]"代表第1位为数字1，"[358]"代表第二位可以为3、5、8中的一个，"\\d{8}"代表后面是可以是0～9的数字，有9位。
        return (!TextUtils.isEmpty(phoneNumber)) && phoneNumber.matches(telRegex);
    }
}
