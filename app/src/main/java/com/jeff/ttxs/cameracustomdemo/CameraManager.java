package com.jeff.ttxs.cameracustomdemo;

import android.view.SurfaceHolder;

/**
 * Created by ttxs on 2017/6/30.
 */

public class CameraManager implements SurfaceHolder.Callback
{
    /**
     * 单例模式一：懒汉式
     */
    /*private static CameraManager manager = null;
    private CameraManager()
    {
    }
    public static CameraManager getInstance(){
        if (manager == null){
            manager = new CameraManager();
        }
        return manager;
    }*/
    /**
     * 单例模式二：懒汉式（加线程锁）
     */
    /*private static CameraManager manager = null;
    private CameraManager()
    {
    }
    public static synchronized CameraManager getInstance(){
        if (manager == null){
            manager = new CameraManager();
        }
        return manager;
    }*/
    /**
     * 单例模式三：饿汉式
     */
    /*private static CameraManager manager = new CameraManager();
    private CameraManager()
    {
    }
    public static synchronized CameraManager getInstance(){
        return manager;
    }*/
    /**
     * 单例模式四：双重校验
     */
    /*private volatile static CameraManager manager = null;
    private CameraManager()
    {
    }
    public static synchronized CameraManager getInstance(){
        if (manager == null){
            synchronized (CameraManager.class){
                if (manager == null){
                    manager = new CameraManager();
                }
            }
        }
        return manager;
    }*/
    /**
     * 单例模式五：静态内部类模式
     * 优点：保证单利唯一性，延迟单例得实例化
     */
    private CameraManager(){}
    public static final CameraManager getInstance(){
        return cameraHolder.Instance;
    }
    private static class cameraHolder
    {
        private static final CameraManager Instance = new CameraManager();
    }


    private SurfaceHolder mSurfaceHolder;
    public void init(SurfaceHolder holder){
        mSurfaceHolder = holder;
        holder.addCallback(this);
        holder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
    }
    @Override
    public void surfaceCreated(SurfaceHolder holder)
    {

    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width,
                               int height)
    {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder)
    {

    }
}
