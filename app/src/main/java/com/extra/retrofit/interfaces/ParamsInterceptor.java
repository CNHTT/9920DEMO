package com.extra.retrofit.interfaces;

import java.util.Map;

/**
 * Created by 戴尔 on 2017/11/9.
 */

@FunctionalInterface
public interface ParamsInterceptor {
    Map checkParams(Map params);
}
