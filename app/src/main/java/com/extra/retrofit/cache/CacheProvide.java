package com.extra.retrofit.cache;

import android.content.Context;

import okhttp3.Cache;

/**
 * Created by 戴尔 on 2017/11/9.
 */

public class
CacheProvide {
    Context mContext;

    public CacheProvide(Context context) {
        mContext = context;
    }

    public Cache provideCache() {
        return new Cache(mContext.getCacheDir(), 50*1024 * 1024);
    }
}
