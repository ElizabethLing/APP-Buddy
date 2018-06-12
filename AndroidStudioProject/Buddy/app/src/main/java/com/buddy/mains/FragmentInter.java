package com.buddy.mains;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.buddy.R;

/**
 * Created by ling on 2017/10/13.
 *
 */

public class FragmentInter extends Fragment {
    private String sCid,sName;
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_inter, container, false);
        TextView tvCid = (TextView)view.findViewById(R.id.Cid);
        TextView tvCidName = (TextView)view.findViewById(R.id.Cid_name);
        sCid = KidsList.allInfoCid;
        sName = KidsList.allInfoName;
        tvCid.setText(getTvCid());
        tvCidName.setText(getNickName());
        RelativeLayout cinfo = (RelativeLayout)view.findViewById(R.id.Cinfo);
        cinfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clickInfo(view);
            }
        });
        return view;
    }
    public void clickInfo(View v){
        switch (v.getId()){
            case R.id.Cinfo:
                Intent intent = new Intent(getActivity(),AddReviseKids.class);
                startActivity(intent);
                break;
//            case R.id.news:
//                Intent intent = new Intent(getActivity(),InputActivity.class);
//                startActivity(intent);
//                break;
//            case R.id.read:
//                Intent intent = new Intent(getActivity(),InputActivity.class);
//                startActivity(intent);
//                break;
            default:
                break;
        }
    }
    private String getTvCid(){
        String str = sCid;
        String strCid = "0123456789";
        if (str != null) {
            strCid= str.substring(str.length()-10,str.length());
        }
        return strCid;
    }
    private String getNickName(){
        String str1 = sName;
        String str = "Json";
        if ( str1 != null) {
            str = str1;
        }
        return str;
    }

}
