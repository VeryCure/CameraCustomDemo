package com.jeff.ttxs.cameracustomdemo;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by ttxs on 2017/6/29.
 */

public class CustomCameraActivity extends Activity
{
    @BindView(R.id.custom_surfaceview)
    CameraSurfaceView mSurfaceView;
    private Camera mCamera;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_custom_layout);
        ButterKnife.bind(this);
        checkCameraPermission();
        initCamera();
    }

    private void checkCameraPermission()
    {
        //大于6.0
        if (ContextCompat.checkSelfPermission(this, Manifest.permission
                .CAMERA)== PackageManager.PERMISSION_GRANTED){

        }else {
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.CAMERA},1);
        }
    }

    private void initCamera()
    {
        /*final SurfaceHolder mHolder = mSurfaceView.getHolder();
        mHolder.addCallback(new SurfaceHolder.Callback()
        {
            @Override
            public void surfaceCreated(SurfaceHolder holder)
            {
                //surface创建后被调用，这时可以初始化camera
                mCamera = Camera.open();
                try
                {
                    mCamera.setPreviewDisplay(mHolder);
                } catch (IOException e)
                {
                    e.printStackTrace();
                }
            }

            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int
                    width, int height)
            {
                //surface发生format或size变化时调用，可以开启相机得预览
                mCamera.startPreview();
            }

            @Override
            public void surfaceDestroyed(SurfaceHolder holder)
            {
                //surface销毁调用,停止预览相机,释放资源
                mCamera.stopPreview();
                mCamera.release();
            }
        });*/
    }
}
