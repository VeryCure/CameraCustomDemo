package com.jeff.ttxs.cameracustomdemo;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.hardware.Camera;
import android.os.Environment;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.Toast;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

/**
 * Created by ttxs on 2017/6/29.
 */

public class CameraSurfaceView extends SurfaceView implements SurfaceHolder
        .Callback, Camera.AutoFocusCallback
{
    private static final String TAG = "CameraSurfaceView";

    private Context mContext;
    private SurfaceHolder holder;
    private Camera mCamera;

    private int mScreenWidth;
    private int mScreenHeight;

    public CameraSurfaceView(Context context)
    {
        this(context, null);
    }

    public CameraSurfaceView(Context context, AttributeSet attrs)
    {
        this(context, attrs, 0);
    }

    public CameraSurfaceView(Context context, AttributeSet attrs, int
            defStyleAttr)
    {
        super(context, attrs, defStyleAttr);
        mContext = context;
        getScreenMetrix(context);
        initView();
    }

    private void getScreenMetrix(Context context)
    {
        WindowManager WM = (WindowManager) context.getSystemService(Context
                .WINDOW_SERVICE);
        DisplayMetrics outMetrics = new DisplayMetrics();
        WM.getDefaultDisplay().getMetrics(outMetrics);
        mScreenWidth = outMetrics.widthPixels;
        mScreenHeight = outMetrics.heightPixels;
    }

    private void initView()
    {
        holder = getHolder();//获得surfaceHolder引用
        holder.addCallback(this);
        holder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);//设置类型
    }

    public void takePicture()
    {
        //设置参数
        mCamera.takePicture(shutter, raw, jpeg);
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder)
    {
        Log.i(TAG, "surfaceCreated");
        if (mCamera == null)
        {
            mCamera = Camera.open();//开启相机
            try
            {
                mCamera.setPreviewDisplay(holder);//摄像头画面显示在Surface上
            } catch (IOException e)
            {
                e.printStackTrace();
            }
        }

    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width,
                               int height)
    {
        Log.i(TAG, "surfaceChanged");
        setCameraParams(mCamera, mScreenWidth, mScreenHeight);
        mCamera.startPreview();
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder)
    {
        Log.i(TAG, "surfaceDestroyed");
        mCamera.stopPreview();//停止预览
        mCamera.release();//释放相机资源
        mCamera = null;
        holder = null;
    }

    @Override
    public void onAutoFocus(boolean success, Camera Camera)
    {
        if (success)
        {
            Log.i(TAG, "onAutoFocus success=" + success);
        }
    }

    /**
     * 拍照瞬间调用
     */
    private Camera.ShutterCallback shutter = new Camera.ShutterCallback()
    {
        @Override
        public void onShutter()
        {
            Log.i(TAG, "shutter............");
        }
    };

    /**
     * 获得没有压缩过得图片数据
     */
    private Camera.PictureCallback raw = new Camera.PictureCallback()
    {
        @Override
        public void onPictureTaken(byte[] data, Camera camera)
        {
            Log.i(TAG, "raw..........");
        }
    };

    /**
     * 创建jpeg图片回掉函数
     */
    private Camera.PictureCallback jpeg = new Camera.PictureCallback()
    {
        @Override
        public void onPictureTaken(byte[] data, Camera camera)
        {
            BufferedOutputStream bos = null;
            Bitmap bm = null;

            try
            {
                //获取图片
                bm = BitmapFactory.decodeByteArray(data, 0, data.length);
                if (Environment.getExternalStorageState().equals(Environment
                        .MEDIA_MOUNTED))
                {
                    Log.i(TAG, "存储地址：" + Environment
                            .getExternalStorageDirectory());
                    //保存路径
                    String dirPath = Environment.getExternalStorageDirectory()
                        .getAbsolutePath()+File.separator+"jeff"+File
                            .separator;
                    String path = dirPath + System.currentTimeMillis() + ".png";
                    BaseUtils.makeSureFileDirExists(dirPath);
                    File file = new File(path);
                    bos = new BufferedOutputStream(new FileOutputStream(file));
                    bm.compress(Bitmap.CompressFormat.PNG,100,bos);
                }else {
                    Toast.makeText(mContext,"没有检测到SD卡",Toast.LENGTH_SHORT).show();
                }
            } catch (IOException e)
            {
                e.printStackTrace();
            }finally
            {
                try
                {
                    bos.flush();//刷新输出
                    bos.close();//关闭
                    bm.recycle();//回收空间
                    mCamera.stopPreview();//关闭预览
                    mCamera.startPreview();//开启预览
                } catch (IOException e)
                {
                    e.printStackTrace();
                }
            }
        }
    };

    private void setCameraParams(Camera camera, int width, int height)
    {
        Log.i(TAG, "setCameraParams  width=" + width + "  height=" + height);
        Camera.Parameters parameters = mCamera.getParameters();
        // 获取摄像头支持的PictureSize列表
        List<Camera.Size> pictureSizeList = parameters
                .getSupportedPictureSizes();
        for (Camera.Size size : pictureSizeList)
        {
            Log.i(TAG, "pictureSizeList size.width=" + size.width + "  size" +
                    ".height=" + size.height);
        }
        /**从列表中选取合适的分辨率*/
        Camera.Size picSize = getProperSize(pictureSizeList, ((float) height
                / width));
        if (null == picSize)
        {
            Log.i(TAG, "null == picSize");
            picSize = parameters.getPictureSize();
        }
        Log.i(TAG, "picSize.width=" + picSize.width + "  picSize.height=" +
                picSize.height);
        // 根据选出的PictureSize重新设置SurfaceView大小
        float w = picSize.width;
        float h = picSize.height;
        parameters.setPictureSize(picSize.width, picSize.height);
        this.setLayoutParams(new FrameLayout.LayoutParams((int) (height * (h
                / w)), height));

        // 获取摄像头支持的PreviewSize列表
        List<Camera.Size> previewSizeList = parameters
                .getSupportedPreviewSizes();

        for (Camera.Size size : previewSizeList)
        {
            Log.i(TAG, "previewSizeList size.width=" + size.width + "  size" +
                    ".height=" + size.height);
        }
        Camera.Size preSize = getProperSize(previewSizeList, ((float) height)
                / width);
        if (null != preSize)
        {
            Log.i(TAG, "preSize.width=" + preSize.width + "  preSize.height="
                    + preSize.height);
            parameters.setPreviewSize(preSize.width, preSize.height);
        }

        parameters.setJpegQuality(100); // 设置照片质量
        if (parameters.getSupportedFocusModes().contains(android.hardware
                .Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE))
        {
            parameters.setFocusMode(android.hardware.Camera.Parameters
                    .FOCUS_MODE_CONTINUOUS_PICTURE);// 连续对焦模式
        }

        mCamera.cancelAutoFocus();//自动对焦。
        // 设置PreviewDisplay的方向，效果就是将捕获的画面旋转多少度显示
        // TODO 这里直接设置90°不严谨，具体见https://developer.android
        // .com/reference/android/hardware/Camera
        // .html#setPreviewDisplay%28android.view.SurfaceHolder%29
        mCamera.setDisplayOrientation(90);
        mCamera.setParameters(parameters);
    }

    /**
     * 从列表中选取合适的分辨率
     * 默认w:h = 4:3
     * <p>tip：这里的w对应屏幕的height
     * h对应屏幕的width<p/>
     */
    private Camera.Size getProperSize(List<Camera.Size> pictureSizeList,
                                      float screenRatio)
    {
        Log.i(TAG, "screenRatio=" + screenRatio);
        Camera.Size result = null;
        for (Camera.Size size : pictureSizeList)
        {
            float currentRatio = ((float) size.width) / size.height;
            if (currentRatio - screenRatio == 0)
            {
                result = size;
                break;
            }
        }

        if (null == result)
        {
            for (Camera.Size size : pictureSizeList)
            {
                float curRatio = ((float) size.width) / size.height;
                if (curRatio == 4f / 3)
                {// 默认w:h = 4:3
                    result = size;
                    break;
                }
            }
        }

        return result;
    }
}
