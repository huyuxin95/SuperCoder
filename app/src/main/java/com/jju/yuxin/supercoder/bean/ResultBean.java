package com.jju.yuxin.supercoder.bean;

import java.util.List;

/**
 * =============================================================================
 * Copyright (c) 2016 yuxin All rights reserved.
 * Packname com.jju.yuxin.supercoder.bean
 * Created by yuxin.
 * Created time 2016/12/14 0014 下午 4:21.
 * Version   1.0;
 * Describe : 新闻请求 返回结果的Bean类
 * History:
 * ==============================================================================
 */

public class ResultBean {

    /**
     * code : 200
     * msg : success
     * newslist : [{"ctime":"2016-12-14 15:04","title":"英特尔杨旭谈转型：要针对中国制定有效策略","description":"网易IT","picUrl":"http://cms-bucket.nosdn.127.net/3b64e5a7d7234dcaaf9ee6301a20fb8020161214150338.png","url":"http://tech.163.com/16/1214/15/C88MMPJ200097U7T.html"},{"ctime":"2016-12-14 12:33","title":"中国前10月生产5.7万台工业机器人 远超去年全年","description":"网易IT","picUrl":"http://cms-bucket.nosdn.127.net/catchpic/9/91/918018b23715c264bbae664290b4042b.jpg","url":"http://tech.163.com/16/1214/12/C88E3KBU00097U7T.html"},{"ctime":"2016-12-14 11:30","title":"苹果新TV应用上线：聚合各款视频应用的内容","description":"网易IT","picUrl":"http://img4.cache.netease.com/photo/0009/2016-12-14/s_C87RN4LC724Q0009.jpg","url":"http://tech.163.com/16/1214/11/C88AF6GC00097U7T.html"},{"ctime":"2016-12-14 11:34","title":"李丰：为什么全世界都在讨论制造业的回归？","description":"网易IT","picUrl":"http://img3.cache.netease.com/photo/0009/2016-12-14/s_C87RN4LB724Q0009.jpg","url":"http://tech.163.com/16/1214/11/C88AN3D600097U7T.html"},{"ctime":"2016-12-14 11:38","title":"苹果无线耳机丢了一只咋办 苹果：允许单只补买","description":"网易IT","picUrl":"http://cms-bucket.nosdn.127.net/catchpic/9/91/918018b23715c264bbae664290b4042b.jpg","url":"http://tech.163.com/16/1214/11/C88ATVH100097U7T.html"},{"ctime":"2016-12-14 08:08","title":"北京新型路灯杆可为汽车充电 还能免费连WiFi","description":"网易IT","picUrl":"http://img4.cache.netease.com/photo/0009/2016-12-14/t_C87V1SH6724Q0009.jpg","url":"http://tech.163.com/16/1214/08/C87UU9FS00097U7T.html"},{"ctime":"2016-12-14 08:15","title":"空气净化器国标:除PM2.5不小于5立方米每瓦特小","description":"网易IT","picUrl":"http://cms-bucket.nosdn.127.net/catchpic/0/04/043667e5e97d07c9cd17ec09956d09f4.jpg","url":"http://tech.163.com/16/1214/08/C87V9ST500097U7T.html"},{"ctime":"2016-12-14 07:13","title":"延迟近两个月后 苹果AirPods耳机终于开售","description":"网易IT","picUrl":"http://img3.cache.netease.com/photo/0009/2016-12-14/s_C87RN4LE724Q0009.jpg","url":"http://tech.163.com/16/1214/07/C87ROTD600097U7T.html"},{"ctime":"2016-12-14 07:22","title":"意在不动产设备 蓝思科技12.2亿吞下游破产公司","description":"网易IT","picUrl":"http://img3.cache.netease.com/photo/0009/2016-12-14/s_C87RN4LD724Q0009.jpg","url":"http://tech.163.com/16/1214/07/C87SA3T200097U7T.html"},{"ctime":"2016-12-14 07:38","title":"尽快商业化 Alphabet成立无人驾驶汽车公司Waymo","description":"网易IT","picUrl":"http://img3.cache.netease.com/photo/0009/2016-12-14/s_C87RN4LC724Q0009.jpg","url":"http://tech.163.com/16/1214/07/C87T6CRP00097U7T.html"}]
     */

    private int code;
    private String msg;
    /**
     * ctime : 2016-12-14 15:04
     * title : 英特尔杨旭谈转型：要针对中国制定有效策略
     * description : 网易IT
     * picUrl : http://cms-bucket.nosdn.127.net/3b64e5a7d7234dcaaf9ee6301a20fb8020161214150338.png
     * url : http://tech.163.com/16/1214/15/C88MMPJ200097U7T.html
     */

    private List<NewslistBean> newslist;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public List<NewslistBean> getNewslist() {
        return newslist;
    }

    public void setNewslist(List<NewslistBean> newslist) {
        this.newslist = newslist;
    }

}
