package com.buddy.mains;

import android.app.*;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;

import com.buddy.R;

/**
 * Created on 2017/11/28.
 *  1、主页面的底部控制菜单
 *  2、设置默认菜单
 * @author Songling
 * @version V 1.0.0
 */
public class MainActivity extends Activity implements View.OnClickListener {
    private static final String TAG = "MainActivity";
    private FrameLayout aFl, bFl, cFl, dFl;
    private FragmentVideo mFVideo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();

    }
    private void initView() {
        aFl = (FrameLayout) findViewById(R.id.layout_aaa);
        bFl = (FrameLayout) findViewById(R.id.layout_bbb);
        cFl = (FrameLayout) findViewById(R.id.layout_ccc);
        dFl = (FrameLayout) findViewById(R.id.layout_ddd);
        aFl.setOnClickListener(this);
        bFl.setOnClickListener(this);
        cFl.setOnClickListener(this);
        dFl.setOnClickListener(this);
        // 设置默认的Fragment
        setDefaultFragment();
    }
    private void setDefaultFragment() {
        FragmentManager fm = getFragmentManager();
        android.app.FragmentTransaction transaction = fm.beginTransaction();
        mFVideo = new FragmentVideo();
        transaction.replace(R.id.frame_content,mFVideo);
        transaction.commit();
    }

    /**
     * fragment：
     * 拿到FragmentManager管理器，getFragmentManager();
     * 开启Fragment事务
     * 使用当前Fragment的布局replace/add/remove等frame_content的控件
     * 事务提交，commit();一定要提交！！！！
     */

    @Override
    public void onClick(View v) {
        FragmentManager fragmentTransaction = getFragmentManager();
        FragmentTransaction transaction = fragmentTransaction.beginTransaction();
        switch (v.getId()) {
            case R.id.layout_aaa:
                mFVideo = new FragmentVideo();
                transaction.replace(R.id.frame_content, mFVideo);
                break;

            case R.id.layout_bbb:
                FragmentData mFData = new FragmentData();
                transaction.replace(R.id.frame_content, mFData);
                break;

            case R.id.layout_ccc:
                FragmentInter mFInter = new FragmentInter();
                transaction.replace(R.id.frame_content, mFInter);
                break;

            case R.id.layout_ddd:
                FragmentMine mFMine = new FragmentMine();
                transaction.replace(R.id.frame_content, mFMine);
                break;
            default:
                break;
        }
        transaction.commit();
    }
}
