package com.szfp.pos.utils;

import com.szfp.pos.Logger;
import com.szfp.pos.greendao.PosRecordDao;
import com.szfp.pos.model.PosRecord;
import com.szfp.utils.ToastUtils;

/**
 * Created by 戴尔 on 2017/11/20.
 */

public class DbHelper {
    public static void AddPosRecord(PosRecord posRecord) {
        try {
            GreenDaoManager.getInstance().getSession().getPosRecordDao().insert(posRecord);
            ToastUtils.showToast("add success");
        }catch (Exception e){
            Logger.debug(e.toString());
        }
    }

    public static PosRecord getPosRecordByTsn(String tsn) {
        PosRecord posRecord;
        try {
            posRecord= GreenDaoManager.getInstance().getSession().getPosRecordDao().queryBuilder().where(PosRecordDao.Properties.Tsn.eq(tsn)).build().unique();
        }catch (Exception e){
            Logger.debug(e.toString());
            return null;
        }
        return posRecord;
    }
}
