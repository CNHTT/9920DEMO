package com.szfp.pos;

import android.app.Application;

import com.szfp.utils.Utils;

/**
 * author：ct on 2017/10/19 17:19
 * email：cnhttt@163.com
 */

public class PosApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Utils.init(this);
    }
}
