package com.szfp.pos.utils;

import com.szfp.pos.greendao.DaoMaster;
import com.szfp.pos.greendao.DaoSession;

import static com.szfp.utils.Utils.getContext;

/**
 * Created by 戴尔 on 2017/11/20.
 */

public class GreenDaoManager {
    private DaoMaster mDaoMaster;
    private DaoSession mDaoSession;
    private static volatile  GreenDaoManager mInstance= null;

    private GreenDaoManager(){
        if (mInstance == null){
            DaoMaster.DevOpenHelper devOpenHelper = new DaoMaster.DevOpenHelper(getContext(),"BETTING");
            mDaoMaster = new DaoMaster(devOpenHelper.getWritableDatabase());
            mDaoSession = mDaoMaster.newSession();
        }
    }


    public static GreenDaoManager getInstance(){
        if (mInstance == null){
            synchronized (GreenDaoManager.class){
                if (mInstance == null)
                    mInstance = new GreenDaoManager();
            }
        }
        return mInstance;
    }

    public DaoMaster getMaster() {
        return mDaoMaster;
    }


    public DaoSession getSession() {
        mDaoSession = mDaoMaster.newSession();
        return mDaoSession;
    }
    public DaoSession getNewSession() {
        mDaoSession = mDaoMaster.newSession();
        return mDaoSession;
    }
}
