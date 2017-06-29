package com.jeff.ttxs.cameracustomdemo;

import android.content.Intent;
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
}
