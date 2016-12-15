package com.jju.yuxin.supercoder.http;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.jju.yuxin.supercoder.bean.NewslistBean;
import com.jju.yuxin.supercoder.bean.ResultBean;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * =============================================================================
 * Copyright (c) 2016 yuxin All rights reserved.
 * Packname com.jju.yuxin.supercoder.http
 * Created by yuxin.
 * Created time 2016/12/14 0014 下午 4:30.
 * Version   1.0;
 * Describe :
 * History:
 * ==============================================================================
 */

public class JsonParserUtils {
    private static final String TAG = JsonParserUtils.class.getSimpleName();
    private static List<NewslistBean> lists;


    public static List<NewslistBean> parserResulet(String result) {
        lists = new ArrayList<>();
        ResultBean resultBean = new ResultBean();
        try {

            Type type = new TypeToken<ResultBean>() {
            }.getType();

            resultBean = new Gson().fromJson(result, type);
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (resultBean.getNewslist() != null) {
            return resultBean.getNewslist();
        } else {
            Log.e(TAG, "parserResulet" + "resultBean:Code" + resultBean.getCode() + "Msg:" + resultBean.getMsg());
            return null;
        }
    }

}
