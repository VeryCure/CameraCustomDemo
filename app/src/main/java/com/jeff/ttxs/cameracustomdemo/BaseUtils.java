package com.jeff.ttxs.cameracustomdemo;

import java.io.File;

/**
 * Created by ttxs on 2017/6/30.
 */

public class BaseUtils
{
    public static boolean makeSureFileDirExists(String strFile)
    {
        if (strFile.equals(""))
            return false;

        int nLastSlash = strFile.lastIndexOf('/');
        if (nLastSlash != -1)
            strFile = strFile.substring(0, nLastSlash);

        File f = new File(strFile);
        if (f.exists())
            return true;

        return f.mkdirs();
    }
}
