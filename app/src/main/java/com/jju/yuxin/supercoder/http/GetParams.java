package com.jju.yuxin.supercoder.http;

import com.jju.yuxin.supercoder.utils.Constant;

import java.util.HashMap;
import java.util.Map;

/**
 * =============================================================================
 * Copyright (c) 2016 yuxin All rights reserved.
 * Packname com.jju.yuxin.supercoder.http
 * Created by yuxin.
 * Created time 2016/12/14 0014 下午 4:00.
 * Version   1.0;
 * Describe :
 * History:
 * ==============================================================================
 */

public class GetParams {

    private Map<String,String> params_map;
    //API
    private static final String userKey="3ba24154ae3b1731b9820c28a0a4555d";

    public GetParams() {
        params_map=new HashMap<>();
        params_map.put("key",userKey);
        params_map.put("num", Constant.DEFAULT_COUNT+"");
    }

    public Map<String, String> getParams_map() {
        return params_map;
    }

    public void addToParams_map(String key, String values) {
        this.params_map.put(key,values);
    }
}
