package com.buddy.mains;

import android.Manifest;
import android.app.*;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.acker.simplezxing.activity.CaptureActivity;
import com.buddy.R;
import com.buddy.connectToServer.DyChecksum;
import com.buddy.connectToServer.MinaClientHandler;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.File;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.buddy.connectToServer.SendMsgToServer;

/**
 * Created on 2018/01/15/11:30.
 * 增加baby的信息和摄像头的ID
 * @author Songling
 * @version V 1.0.0
 */

public class AddReviseKids extends Activity {
    protected static final int CHOOSE_PICTURE = 0;
    protected static final int TAKE_PICTURE = 1;
    private static final int CROP_SMALL_PICTURE = 2;
    private static final int REQ_CODE_PERMISSION = 0x1111;

    private EditText etName,etCam;
    private Button btnAdd,btnRevise;
    private ImageButton btnscan;

    private String camId;
    private String name;

    private ImageView mImage;
    private Bitmap mBitmap;
    protected static Uri tempUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_revise_kids);

        init();
        ClickListener();
    }

    private void init() {
        etName = (EditText)findViewById(R.id.name);
        etCam = (EditText)findViewById(R.id.camId);
        btnscan = (ImageButton)findViewById(R.id.scan);

        btnAdd = (Button)findViewById(R.id.add);
        mImage = (ImageView)findViewById(R.id.image);
        btnRevise = (Button)findViewById(R.id.revise);
    }

    private void ClickListener(){
        btnscan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ContextCompat.checkSelfPermission(AddReviseKids.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                    // Do not have the permission of camera, request it.
                    ActivityCompat.requestPermissions(AddReviseKids.this, new String[]{Manifest.permission.CAMERA}, REQ_CODE_PERMISSION);
                } else {
                    // Have gotten the permission
                    startCaptureActivityForResult();
                }
            }
        });

        mImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showChoosePicDialog();
            }
        });

        if (KidsList.allInfoCid == null) {
            etName.setHint("姓名唯一，确认后不可修改");
            etName.setHintTextColor(Color.GRAY);
            etName.setEnabled(true);
            btnAdd.setVisibility(View.VISIBLE);
            btnRevise.setVisibility(View.GONE);
            btnAdd.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    getInfo();
                    if (name.equals("") || !(ValidateCamId(camId))) {
                        Toast.makeText(getApplication(),"请确认信息是否正确",Toast.LENGTH_SHORT).show();
                    }else {
                        try {
                            //Send messages to remote main control server,bts = 30,Add child Info
                            SendMsgToServer addInfo = new SendMsgToServer(30, AMapToJson());
                            addInfo.start();
                            Thread.sleep(1000);
                            System.out.println("AMapToJson() ==== " + AMapToJson());
                            //发送 绑定User与Cid 的请求
                            if (MinaClientHandler.CId == null) {
                                Toast.makeText(getApplication(), "提交失败", Toast.LENGTH_SHORT).show();
                            } else {
                                //BindCid bindCid = new BindCid(MinaClientHandler.CId);
                                DyChecksum bindCid = new DyChecksum(70, UserLogOn.userMobile, UserLogOn.userPwd,MinaClientHandler.UId,MinaClientHandler.CId,1);
                                bindCid.start();
                                Thread.sleep(1000);
                                //发送 更新数据库user的kids表 的请求
                                SendMsgToServer updateCid = new SendMsgToServer(21, UpdateCidToJson());
                                updateCid.start();
                                Thread.sleep(1000);
                                switch (MinaClientHandler.acrResult) {
                                    //有隐患。如果连续修改两次，那么MinaClientHandler.ucrResult==0，是这次修改成功的0，还是上次提交成功的0
                                    case 0:
                                        Toast.makeText(getApplication(), "已经提交baby信息", Toast.LENGTH_SHORT).show();
                                        startActivity(new Intent(AddReviseKids.this, KidsList.class));
                                        break;
                                    case 12100:
                                        Toast.makeText(getApplication(), "无效婴儿信息，请确认信息", Toast.LENGTH_SHORT).show();
                                        break;
                                    default:
                                        break;
                                }
                            }
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
            });
        } else {
            etName.setText(KidsList.allInfoName);
            etName.setTextColor(Color.BLACK);
            etName.setEnabled(false);
            btnAdd.setVisibility(View.GONE);
            btnRevise.setVisibility(View.VISIBLE);
            btnRevise.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    getInfo();
                    if (!ValidateCamId(camId)) {
                        Toast.makeText(AddReviseKids.this, "请确认摄像头ID", Toast.LENGTH_SHORT).show();
                    } else {
                        //Send messages to remote main control server,bts = 40,update child Info
                        SendMsgToServer updateInfo = new SendMsgToServer(40, UMapToJson());
                        updateInfo.start();
                        System.out.println("UMapToJson() ====== " + UMapToJson());
                        try {
                            Thread.sleep(500);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }

                        switch (MinaClientHandler.ucrResult) {
                            //有隐患。如果连续修改两次，那么MinaClientHandler.ucrResult==0，是这次修改成功的0，还是上次提交成功的0
                            case 0:
                                Toast.makeText(AddReviseKids.this, "修改成功", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(AddReviseKids.this, KidsList.class));
                                break;
                            case 12100:
                                Toast.makeText(AddReviseKids.this, "无效婴儿信息，请确认信息", Toast.LENGTH_SHORT).show();
                                break;
                            default:
                                break;
                        }

                    }
                }
            });
        }
    }

    private void getInfo() {
        name = etName.getText().toString();
        camId = etCam.getText().toString();
    }

    private String AMapToJson() {
        Gson gson = new GsonBuilder().enableComplexMapKeySerialization().create();
        Map<String,String> addInfo = new HashMap<String,String>();
        addInfo.put("name",name);
        addInfo.put("cameraId",camId);
        return gson.toJson(addInfo);
    }
    private String UMapToJson() {
        Gson gson = new GsonBuilder().enableComplexMapKeySerialization().create();
        Map<String,String> updateInfo = new HashMap<String,String>();
        updateInfo.put("cid", KidsList.allInfoCid);
        updateInfo.put("name",KidsList.allInfoName);
        updateInfo.put("cameraId",camId);
        return gson.toJson(updateInfo);
    }
    private String UpdateCidToJson() {
        Gson gson = new GsonBuilder().enableComplexMapKeySerialization().create();
        Map<String,String> updateCId = new HashMap<String,String>();
        updateCId.put("name", UserLogOn.userMobile);
        updateCId.put("uid", MinaClientHandler.UId);
        return gson.toJson(updateCId);
    }

    private Boolean ValidateCamId(String str) {
        //大小写字母，数字（只要字符串匹配其中任何一个或多个都可以）
        String reg="([A-Z]|[a-z]|[0-9]|[`~!@#$%^&*()+=|{}':;',\\[\\].<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。，、？]){6,15}$";
        Pattern pAll=Pattern.compile(reg);
        Matcher mAll = pAll.matcher(str);
        return mAll.matches();
    }

    protected void showChoosePicDialog(){
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(AddReviseKids.this);
        builder.setTitle("添加图片");
        String[] items = {"选择本地照片","拍照"};
        builder.setNegativeButton("取消",null);
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which){
                    case CHOOSE_PICTURE: // 选择本地照片
                        Intent openAlbumIntent = new Intent(
                                Intent.ACTION_GET_CONTENT);
                        openAlbumIntent.setType("image/*");
                        //用startActivityForResult方法，待会儿重写onActivityResult()方法，拿到图片做裁剪操作
                        startActivityForResult(openAlbumIntent, CHOOSE_PICTURE);
                        break;
                    case TAKE_PICTURE: // 拍照
                        Intent openCameraIntent = new Intent(
                                MediaStore.ACTION_IMAGE_CAPTURE);
                        tempUri = Uri.fromFile(new File(Environment
                                .getExternalStorageDirectory(), "temp_image.jpg"));
                        // 将拍照所得的相片保存到SD卡根目录
                        openCameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, tempUri);
                        startActivityForResult(openCameraIntent, TAKE_PICTURE);
                        break;
                }
            }
        });
        builder.show();
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == MainActivity.RESULT_OK) {
            switch (requestCode) {
                case TAKE_PICTURE:
                    cutImage(tempUri); // 对图片进行裁剪处理
                    break;
                case CHOOSE_PICTURE:
                    cutImage(data.getData()); // 对图片进行裁剪处理
                    break;
                case CROP_SMALL_PICTURE:
                    if (data != null) {
                        setImageToView(data); // 让刚才选择裁剪得到的图片显示在界面上
                    }
                    break;
                case CaptureActivity.REQ_CODE:
                    switch (resultCode) {
                        case RESULT_OK:
                            etCam.setText(data.getStringExtra(CaptureActivity.EXTRA_SCAN_RESULT));  //or do sth
                            break;
                        case RESULT_CANCELED:
                            if (data != null) {
                                // for some reason camera is not working correctly
                                etCam.setText(data.getStringExtra(CaptureActivity.EXTRA_SCAN_RESULT));
                            }
                            break;
                    }
                    break;
            }
        }
    }

    /**
     * 裁剪图片方法实现
     */
    protected void cutImage(Uri uri) {
        if (uri == null) {
            Log.i("alanjet", "The uri is not exist.");
        }
        tempUri = uri;
        Intent intent = new Intent("com.android.camera.action.CROP");
        //com.android.camera.action.CROP这个action是用来裁剪图片用的
        intent.setDataAndType(uri, "image/*");
        // 设置裁剪
        intent.putExtra("crop", "true");
        // aspectX aspectY 是宽高的比例
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        // outputX outputY 是裁剪图片宽高
        intent.putExtra("outputX", 150);
        intent.putExtra("outputY", 150);
        intent.putExtra("return-data", true);
        startActivityForResult(intent, CROP_SMALL_PICTURE);
    }

    /**
     * 保存裁剪之后的图片数据
     */
    protected void setImageToView(Intent data) {
        Bundle extras = data.getExtras();
        if (extras != null) {
            mBitmap = extras.getParcelable("data");
            //这里图片是方形的，可以用一个工具类处理成圆形
            mImage.setImageBitmap(mBitmap);//显示图片
            //在这个地方可以写上上传该图片到服务器的代码
        }
    }

    /**
     * 扫描二维码
     */
    private void startCaptureActivityForResult() {
        Intent intent = new Intent(AddReviseKids.this, CaptureActivity.class);
        Bundle bundle = new Bundle();
        bundle.putBoolean(CaptureActivity.KEY_NEED_BEEP, CaptureActivity.VALUE_BEEP);
        bundle.putBoolean(CaptureActivity.KEY_NEED_VIBRATION, CaptureActivity.VALUE_VIBRATION);
        bundle.putBoolean(CaptureActivity.KEY_NEED_EXPOSURE, CaptureActivity.VALUE_NO_EXPOSURE);
        bundle.putByte(CaptureActivity.KEY_FLASHLIGHT_MODE, CaptureActivity.VALUE_FLASHLIGHT_OFF);
        bundle.putByte(CaptureActivity.KEY_ORIENTATION_MODE, CaptureActivity.VALUE_ORIENTATION_AUTO);
        bundle.putBoolean(CaptureActivity.KEY_SCAN_AREA_FULL_SCREEN, CaptureActivity.VALUE_SCAN_AREA_FULL_SCREEN);
        bundle.putBoolean(CaptureActivity.KEY_NEED_SCAN_HINT_TEXT, CaptureActivity.VALUE_SCAN_HINT_TEXT);
        intent.putExtra(CaptureActivity.EXTRA_SETTING_BUNDLE, bundle);
        startActivityForResult(intent, CaptureActivity.REQ_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case REQ_CODE_PERMISSION: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // User agree the permission
                    startCaptureActivityForResult();
                } else {
                    // User disagree the permission
                    Toast.makeText(this, "You must agree the camera permission request before you use the code scan function", Toast.LENGTH_LONG).show();
                }
            }
            break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        MinaClientHandler.acrResult = 1;
        MinaClientHandler.ucrResult = 1;
    }
}
