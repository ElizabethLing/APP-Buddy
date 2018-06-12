package com.buddy.mains;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.app.Fragment;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.buddy.R;
import com.buddy.setting.SettingActivity;

/**
 * Created on 2017/11/28.
 * @author Songling
 * @version V 1.0.0
 */

public class FragmentMine extends Fragment {
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_mine, container, false);
        view.findViewById(R.id.personalsettting).setOnClickListener(mOnClickListener);
        view.findViewById(R.id.quit).setOnClickListener(mOnClickListener);
        view.findViewById(R.id.clearcache).setOnClickListener(mOnClickListener);
        return view;
    }

    //设置监听事件
    private View.OnClickListener mOnClickListener= new View.OnClickListener(){
        @Override
        public void onClick(View view){
            switch (view.getId()){
                case R.id.personalsettting:
                    startActivity(new Intent(getActivity(), SettingActivity.class));
                    break;
                case R.id.clearcache:
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    Toast.makeText(getActivity(),"已清理缓存",Toast.LENGTH_SHORT).show();
                    break;
                case R.id.quit:
                    onKeyDown(KeyEvent.KEYCODE_BACK,null);
                    break;
                default:
                    break;
            }

        }
    };

    public boolean onKeyDown(int keyCode, KeyEvent event){
        if (keyCode == KeyEvent.KEYCODE_BACK){
            showQuitDialog();//退出对话框
        }
        return false;
    }
    private void showQuitDialog(){
        AlertDialog isExit = new AlertDialog.Builder(getActivity()).create();
        isExit.setTitle("退出提示");
        isExit.setMessage("确定退出登录吗？");
        isExit.setButton(DialogInterface.BUTTON_POSITIVE,"确定",listener);
        isExit.setButton(DialogInterface.BUTTON_NEGATIVE,"取消",listener);
        isExit.show();
    }
    DialogInterface.OnClickListener listener = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialogInterface, int buttonId) {
            switch (buttonId){
                case AlertDialog.BUTTON_POSITIVE:
                    SharedPreferences sp = getActivity().getSharedPreferences("userInfo", Context.MODE_PRIVATE);//Context.MODE_PRIVATE表示SharePrefences的数据只有自己应用程序能访问。
                    SharedPreferences.Editor editor = sp.edit();
                    editor.clear();
                    editor.apply();
                   // System.exit(0);
                    startActivity(new Intent(getActivity(),UserLogOn.class));
                    break;
                case AlertDialog.BUTTON_NEGATIVE:
                    break;
                default:
                    break;
            }
        }
    };

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
