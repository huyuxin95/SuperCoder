package com.jju.yuxin.supercoder.utils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * =============================================================================
 * Copyright (c) 2016 yuxin All rights reserved.
 * Packname com.jju.yuxin.supercoder.utils
 * Created by yuxin.
 * Created time 2016/12/14 0014 下午 4:08.
 * Version   1.0;
 * Describe :
 * History:
 * ==============================================================================
 */
public class Constant {
    public static final String URL = "http://api.tianapi.com/";
    public static final String Chuangye = "startup";
    public static final String Pingguo = "apple";
    public static final String Keji = "keji";
    public static final String Tiyu = "tiyu";
    public static final String VR = "vr";
    public static final String IT = "it";
    public static final String FAILED_LOADING = "http://mat1.gtimg.com/tech/00Jamesdu/2014/index/remark/2.png";
    public static final int FINISHED = 1;
    public static final int CANCELLED = 2;
    public static final int ERROR = 3;
    public static final int SUCCESS = 4;
    public static final int LOADMORE = 5;
    public static final int SUCCESS_LOAD_DETAIL = 6;
    public static final int DEFAULT_COUNT = 10;

    //https://api.desktoppr.co/1/wallpapers?page=1


    public static final String PIC_URL = "https://api.desktoppr.co/1/wallpapers";


    public List<Integer> zxadlist;
    public List<Integer> cyadlist;
    public List<Integer> ydadlist;


    public List<Integer> getThridAdlist() {
        if (zxadlist == null) {
            zxadlist = new ArrayList<>();
            zxadlist.add(3);
        }
        return zxadlist;
    }
    public List<Integer> getOneAdlist() {
        if (cyadlist == null) {
            cyadlist = new ArrayList<>();
            cyadlist.add(1);
        }
        return cyadlist;
    }

    public List<Integer> getTwoAdlist() {
        if (ydadlist == null) {
            ydadlist = new ArrayList<>();
            ydadlist.add(2);
        }
        return ydadlist;
    }
}
