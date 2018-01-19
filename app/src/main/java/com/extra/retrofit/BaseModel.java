package com.extra.retrofit;

import android.content.Context;
import android.widget.Toast;

/**
 * Created by 戴尔 on 2017/11/9.
 */

public class BaseModel {
    private int code;
    private String msg;
    private String data;
    public boolean trueStatus(Context context) {
        if (code==1) {
            return true;
        } else {
            Toast.makeText(context, msg, Toast.LENGTH_SHORT);
            return false;
        }
    }
}
