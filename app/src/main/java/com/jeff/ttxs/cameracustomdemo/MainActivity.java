package com.jeff.ttxs.cameracustomdemo;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity
{

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        checkCameraPermission();
    }
    @OnClick({R.id.system,R.id.custom})
    public void OnClick(View view){
        switch (view.getId()){
            case R.id.system:
                startActivity(new Intent(this,SystemCameraActivity.class));
                break;
            case R.id.custom:
                startActivity(new Intent(this,CustomCameraActivity.class));
                break;
        }
    }

    /**
     * 运行时权限
     */
    private void checkCameraPermission()
    {
        //相机
        if (ContextCompat.checkSelfPermission(this, Manifest.permission
                .CAMERA)== PackageManager.PERMISSION_GRANTED){

        }else {
            ActivityCompat.requestPermissions(this,new String[]{Manifest
                    .permission.CAMERA},1);
        }
        //读取SDCard
        if (ContextCompat.checkSelfPermission(this,Manifest.permission
                .WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED){
            //已经同意，不需要申请，如果为NPERMISSION_DENIED,则需要申请
        }else {
            ActivityCompat.requestPermissions(this,new String[]{Manifest
                    .permission.WRITE_EXTERNAL_STORAGE},2);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[]
            permissions, @NonNull int[] grantResults)
    {
        switch (requestCode){
            case 1:
                //当申请得权限取消时，grandResults是empty
                if (grantResults.length>0 && grantResults[0] ==
                        PackageManager.PERMISSION_GRANTED){
                    //申请成功
                    LogUtils.e("相机--申请成功");
                }else {
                    //申请失败
                    LogUtils.e("相机--申请失败");
                }
                break;
            case 2:
                if (grantResults.length>0 && grantResults[0] ==
                        PackageManager.PERMISSION_GRANTED){
                    //申请成功
                    LogUtils.e("读取SD--申请成功");
                }else {
                    //申请失败
                    LogUtils.e("读取SD--申请失败");
                }
                break;
        }
    }
}
