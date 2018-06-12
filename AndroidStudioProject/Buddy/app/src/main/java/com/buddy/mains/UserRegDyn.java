package com.buddy.mains;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.buddy.R;
import com.buddy.connectToServer.MinaClientHandler;

/**
 * Created on 2018/5/22.14:19.
 *
 * @author Songling
 * @version 1.0.0
 */

public class UserRegDyn extends Activity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_register2);
        TextView note = (TextView) findViewById(R.id.note);
        note.setText("我们已经发送验证码到"+ UserRegistermobile.noCode + " " + UserRegistermobile.phoneNumber +"请输入短信中的验证码");
        final EditText etDynCode = (EditText)findViewById(R.id.valid);
        if (MinaClientHandler.code != null) {
            etDynCode.setText(MinaClientHandler.code);
        }
        Button bnBack = (Button)findViewById(R.id.bnBack);
        Button bnNext = (Button)findViewById(R.id.bnNext);
        bnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplication(),UserRegister.class));
            }
        });
        bnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplication(),UserRegistermobile.class));
            }
        });
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
